package storage;

/**
 * Created by VladVin on 02.12.2016.
 */
public class FallInfo {
    private Id id;
    private Location loc;

    public FallInfo(Id id, Location loc) {
        this.id = id;
        this.loc = loc;
    }

    public Id getId() {
        return id;
    }

    public Location getLoc() {
        return loc;
    }
}
