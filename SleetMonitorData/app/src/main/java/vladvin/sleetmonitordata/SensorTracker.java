package vladvin.sleetmonitordata;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import vladvin.sleetmonitordata.Exceptions.DataWriterException;

/**
 * Created by VladVin on 19.10.2015.
 */
public class SensorTracker implements SensorEventListener {

    private final static String TAG = "SensorTracker";

    private enum MeasurementsTypes { FALL, NON_FALL }
    private enum TrackingStatus { BLOOM, READY, FAILING, SAVED, BEACH }

    private MainActivity activity;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private DataWriter dataWriter;
    private ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

    private float accelerometerMaxRange;
    private long minDelay;
    private SensorData lastMeasurement;
    private SensorData currentMeasurement;
    private long lastTimestamp = 0;
    private TrackingStatus status = TrackingStatus.SAVED;
    private boolean isPaused = true;
    private long trackingTime = 0;   //msec
    private final static int MAX_MEASUREMENT_TIME = 1500;  // msec
    private final static int MAX_TEMP_MEASUREMENTS = 50;
    private final static float IMPULSE_FACTOR = 0.01f;
    private final static float IMPULSE_COUNT_THRESHOLD = 20;
    private final Queue<Pair<Long, SensorData>> measurements;
    private int countMeasurements = 0;
    private final Queue<SensorData> tempMeasurements;
    private MeasurementsTypes currentMeasurementType = MeasurementsTypes.NON_FALL;
    private int fallsCount = 0;
    private int nonFallsCount = 0;

    public SensorTracker(MainActivity activity, DataWriter dataWriter) {
        this.activity = activity;
        this.dataWriter = dataWriter;
        this.dataWriter.execute();
        this.sensorManager = (SensorManager) ApplicationCtx.getContext().getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.accelerometerMaxRange = accelerometer.getMaximumRange();
        this.minDelay = accelerometer.getMinDelay() / 1000L;
        this.lastMeasurement = new SensorData(accelerometerMaxRange, 0.0f, 0.0f, 0.0f);
        this.currentMeasurement = new SensorData(accelerometerMaxRange, 0.0f, 0.0f, 0.0f);
        this.measurements = new ConcurrentLinkedQueue<>();
        this.tempMeasurements = new LinkedList<>();

        // Must be FASTEST to improve data resolution!
        this.sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void changeFallState(boolean isFall) {
        if (isFall) {
            currentMeasurementType = MeasurementsTypes.FALL;
        } else {
            currentMeasurementType = MeasurementsTypes.NON_FALL;
        }
        dataWriter.changeFallState(isFall);
    }

    public void play() {
        isPaused = false;
        dataWriter.writePlay();
    }

    public void pause() {
        initializeTracker();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isPaused) {
            return;
        }
        if (lastTimestamp == 0) {
            lastTimestamp = event.timestamp - minDelay * 1000000L;
        }
        long delay = (event.timestamp - lastTimestamp) / 1000000L;
        lastTimestamp = event.timestamp;

        currentMeasurement.setX(event.values[0], accelerometerMaxRange);
        currentMeasurement.setY(event.values[1], accelerometerMaxRange);
        currentMeasurement.setZ(event.values[2], accelerometerMaxRange);

        addTempMeasurement(currentMeasurement);

        if (status == TrackingStatus.FAILING) {
            trackingTime += delay;
            Pair<Long, SensorData> item = new Pair<Long, SensorData>(trackingTime, new SensorData(currentMeasurement));
            measurements.add(item);
            countMeasurements++;

            if (trackingTime >= MAX_MEASUREMENT_TIME) {
                incrementFallsCount();
                dataWriter.fetData(measurements, countMeasurements);
                status = TrackingStatus.SAVED;
                Log.d(TAG, "Saved...");
                playLongBeep();
            }
        }
        boolean isIdle = isIdle();
        if (isIdle) {
            if (status == TrackingStatus.BLOOM) {
                status = TrackingStatus.READY;
                Log.d(TAG, "Ready...");
                playBeep();
            } else if (status == TrackingStatus.SAVED) {
                status = TrackingStatus.BEACH;
                Log.d(TAG, "Beach...");
            }
        } else {
            if (status == TrackingStatus.READY) {
                status = TrackingStatus.FAILING;
                Log.d(TAG, "Failing...");
                this.trackingTime = 0;
                countMeasurements = 0;
            } else if (status == TrackingStatus.BEACH) {
                status = TrackingStatus.BLOOM;
                Log.d(TAG, "Blooming...");
            }
        }

        lastMeasurement.setX(currentMeasurement.getX());
        lastMeasurement.setY(currentMeasurement.getY());
        lastMeasurement.setZ(currentMeasurement.getZ());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void initializeTracker() {
        lastTimestamp = 0;
        status = TrackingStatus.SAVED;
        trackingTime = 0;
        isPaused = true;
        tempMeasurements.clear();
        measurements.clear();
    }

    private void addTempMeasurement(SensorData measurement) {
        if (tempMeasurements.size() >= MAX_TEMP_MEASUREMENTS) {
            tempMeasurements.remove();
        }
        tempMeasurements.add(new SensorData(measurement));
    }

    private boolean isIdle() {
        if (tempMeasurements.size() <= 1) {
            return false;
        }
        Iterator<SensorData> iterator = tempMeasurements.iterator();
        SensorData sData = iterator.next();
        SensorData prevSData = new SensorData(sData);
        int impulseCount = 0;
        while (iterator.hasNext()) {
            sData = iterator.next();
            if (Math.abs(sData.getX() - prevSData.getX()) > IMPULSE_FACTOR ||
                    Math.abs(sData.getY() - prevSData.getY()) > IMPULSE_FACTOR ||
                    Math.abs(sData.getZ() - prevSData.getZ()) > IMPULSE_FACTOR) {
                impulseCount++;
            }
            prevSData = new SensorData(sData);
        }
        if (impulseCount >= IMPULSE_COUNT_THRESHOLD) {
            return false;
        } else {
            return true;
        }
    }

    private void incrementFallsCount() {
        switch (currentMeasurementType) {
            case FALL:
                fallsCount++;
                activity.updateFallsCount(true, fallsCount);
                break;
            case NON_FALL:
                nonFallsCount++;
                activity.updateFallsCount(false, nonFallsCount);
                break;
        }
    }

    private void playBeep() {
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
    }

    private void playLongBeep() {
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 500);
    }

    private void printMeasurement(Pair<Long, SensorData> item) {
        Log.d(TAG, String.valueOf(item.first) + ": " + String.valueOf(item.second.getX()) + " " +
                String.valueOf(item.second.getY()) + " " + String.valueOf(item.second.getZ()));
    }

    private void printData(final List<Pair<Long, SensorData>> data, final int count) {
        for (int i = 0; i < count; i++) {
            try {
                Pair<Long, SensorData> item = data.remove(0);
                Log.d(TAG, String.valueOf(item.first) + ": " + String.valueOf(item.second.getX()) + " " +
                        String.valueOf(item.second.getY()) + " " + String.valueOf(item.second.getZ()) + " size: " + data.size());
            } catch (Exception e) {
                break;
            }
        }
    }
}
