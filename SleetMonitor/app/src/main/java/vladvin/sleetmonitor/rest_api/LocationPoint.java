package vladvin.sleetmonitor.rest_api;

public class LocationPoint {
    private double lat;
    private double lon;

    public LocationPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
