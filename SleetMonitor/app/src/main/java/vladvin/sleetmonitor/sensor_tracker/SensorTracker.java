package vladvin.sleetmonitor.sensor_tracker;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import vladvin.sleetmonitor.data_proc.DataSender;

public class SensorTracker implements SensorEventListener {
    private final static String TAG = "SensorTracker";

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float accelerometerMaxRange;

    private ConcurrentLinkedQueue<SensorData> measurements;
    private static final long SEND_DATA_TIME_DIFF = 1000L;   // in ms
    private long lastSendingDataTimestamp = 0L;

    private Activity activity;
    private LocationTracker locationTracker;
    private final DataSender dataSender;

    public SensorTracker(Activity activity, LocationTracker locationTracker, DataSender dataSender) {
        this.activity = activity;
        this.sensorManager = (SensorManager)
                activity.getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.accelerometerMaxRange = accelerometer.getMaximumRange();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        this.locationTracker = locationTracker;
        this.measurements = new ConcurrentLinkedQueue<>();
        this.dataSender = dataSender;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long timestamp = new Date().getTime();

        if (locationTracker == null) {
            return;
        }

        Location lastLocation = locationTracker.getLastKnownLocation();
        if (lastLocation == null) {
            return;
        }

        SensorData sensorData = new SensorData(
                accelerometerMaxRange,
                sensorEvent.values[0],
                sensorEvent.values[1],
                sensorEvent.values[2],
                lastLocation.getLatitude(),
                lastLocation.getLongitude(),
                timestamp);

        measurements.add(sensorData);

        if (timestamp - lastSendingDataTimestamp >
                SEND_DATA_TIME_DIFF) {
            if (dataSender != null) {
                dataSender.sendMeasurements(measurements, measurements.size());
            }
            lastSendingDataTimestamp = timestamp;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
