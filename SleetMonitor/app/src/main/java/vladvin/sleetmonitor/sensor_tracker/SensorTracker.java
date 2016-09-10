package vladvin.sleetmonitor.sensor_tracker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import vladvin.sleetmonitor.app_ctx.ApplicationCtx;

public class SensorTracker implements SensorEventListener {
    private final static String TAG = "SensorTracker";

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float accelerometerMaxRange;

    public SensorTracker() {
        this.sensorManager = (SensorManager) ApplicationCtx.getContext().getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.accelerometerMaxRange = accelerometer.getMaximumRange();
        this.sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        SensorData sensorData = new SensorData(
                accelerometerMaxRange,
                sensorEvent.values[0],
                sensorEvent.values[1],
                sensorEvent.values[2],
                sensorEvent.timestamp);

        // TODO: send to broker
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
