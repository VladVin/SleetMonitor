package vladvin.sleetmonitor.sensor_tracker;

public class SensorData {
    private final float accelerometerMaxRange;

    private float x;
    private float y;
    private float z;
    private double lat;
    private double lon;
    private long ts;

    SensorData(float accelerometerMaxRange, float x, float y, float z,
               double latitude, double longitude, long timestamp) {
        if (accelerometerMaxRange != 0.0f) {
            this.accelerometerMaxRange = accelerometerMaxRange;
        } else {
            this.accelerometerMaxRange = 1.0f;
        }
        this.x = normalize(x);
        this.y = normalize(y);
        this.z = normalize(z);
        this.lat = latitude;
        this.lon = longitude;
        this.ts = timestamp;
    }

    public SensorData(SensorData sensorData) {
        this.accelerometerMaxRange = sensorData.accelerometerMaxRange;
        this.x = sensorData.getX();
        this.y = sensorData.getY();
        this.z = sensorData.getZ();

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lon;
    }

    public long getTimestamp() {
        return ts;
    }

    public float getAccelerometerMaxRange() {
        return accelerometerMaxRange;
    }

    private float normalize(float value) {
        return value / accelerometerMaxRange;
    }

    @Override
    public String toString() {
        return "x: " + x + ", " +
                "y: " + y + ", " +
                "z: " + z + ", " +
                "lat: " + lat + ", " +
                "lon: " + lon + ", " +
                "ts: " + ts;
    }
}
