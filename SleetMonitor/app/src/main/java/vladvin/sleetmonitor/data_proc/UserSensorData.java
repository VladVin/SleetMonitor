package vladvin.sleetmonitor.data_proc;

import java.util.List;
import java.util.UUID;

import vladvin.sleetmonitor.sensor_tracker.SensorData;

/**
 * Created by VladVin on 28.11.2016.
 */

public class UserSensorData {
    private UUID userId;
    private List<SensorData> data;

    public UserSensorData(UUID userId, List<SensorData> data) {
        this.userId = userId;
        this.data = data;
    }

    public UUID getUserId() {
        return userId;
    }

    public List<SensorData> getData() {
        return data;
    }
}
