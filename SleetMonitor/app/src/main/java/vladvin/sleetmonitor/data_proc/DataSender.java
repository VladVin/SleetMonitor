package vladvin.sleetmonitor.data_proc;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import vladvin.sleetmonitor.sensor_tracker.SensorData;

/**
 * Created by VladVin on 28.11.2016.
 */

public class DataSender {
    private final UuidProvider uuidProvider;
    private EventHubSender eventHubSender;

    public DataSender(Context context) {
        uuidProvider = new UuidProvider(context);
        eventHubSender = new EventHubSender(context);
    }

    public void sendMeasurements(ConcurrentLinkedQueue<SensorData> dataQueue, int count) {
        List<SensorData> data = new ArrayList<>();
        while ((data.size() < count) && (!dataQueue.isEmpty())) {
            data.add(dataQueue.poll());
        }
        sendDataToServer(data);
    }

    private void sendDataToServer(List<SensorData> data) {
        UUID userId = uuidProvider.getUserId();
        UserSensorData usd = new UserSensorData(userId, data);
        String json = DataConverter.toJson(usd);

        if (eventHubSender != null) {
            eventHubSender.sendMessage(json);
        }
    }
}
