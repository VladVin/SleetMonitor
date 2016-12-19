package storage;

public class Id {
    private String userId;
    private long time;

    public Id(String userId, long time) {
        this.userId = userId;
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public long getTime() {
        return time;
    }
}
