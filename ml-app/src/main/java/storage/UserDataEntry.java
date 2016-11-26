package storage;

/**
 * Created by VladVin on 26.11.2016.
 */
public class UserDataEntry {
    private String user_id;
    private float x, y, z;
    private double lat, lon;
    private long timestamp;

    public UserDataEntry(String user_id, float x, float y, float z, double lat, double lon, long timestamp) {
        this.user_id = user_id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
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

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
