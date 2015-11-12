package vladvin.sleetmonitordata;

/**
 * Created by VladVin on 17.10.2015.
 */
public class SensorData {
    private final float normDenom;

    private float x;
    private float y;
    private float z;

    public SensorData(float normDenom, float x, float y, float z) {
        if (normDenom != 0.0f) {
            this.normDenom = normDenom;
        } else {
            this.normDenom = 1.0f;
        }
        this.x = normalize(x);
        this.y = normalize(y);
        this.z = normalize(z);
    }

    public SensorData(SensorData sensorData) {
        this.normDenom = sensorData.normDenom;
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

    public float getNormDenom() {
        return normDenom;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setX(float x, float normDenom) {
        this.x = normalize(x);
    }

    public void setY(float y, float normDenom) {
        this.y = normalize(y);
    }

    public void setZ(float z, float normDenom) {
        this.z = normalize(z);
    }

    private float normalize(float value) {
        return value / normDenom;
    }
}
