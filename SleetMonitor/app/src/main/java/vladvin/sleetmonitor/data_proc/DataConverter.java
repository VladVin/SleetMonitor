package vladvin.sleetmonitor.data_proc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import vladvin.sleetmonitor.sensor_tracker.SensorData;

/**
 * Created by VladVin on 28.11.2016.
 */

class DataConverter {
    static String toJson(UserSensorData usd) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(UserSensorData.class, new UserSensorDataSerializer())
                .create();
        return gson.toJson(usd);
    }

    private static class UserSensorDataSerializer implements JsonSerializer<UserSensorData> {
        @Override
        public JsonElement serialize(UserSensorData src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();

            obj.addProperty("user_id", src.getUserId().toString());

            JsonArray data = new JsonArray();
            for (SensorData sd : src.getData()) {
                JsonObject item = new JsonObject();
                item.addProperty("x", sd.getX());
                item.addProperty("y", sd.getY());
                item.addProperty("z", sd.getZ());
                item.addProperty("lat", sd.getLatitude());
                item.addProperty("lon", sd.getLongitude());
                item.addProperty("ts", sd.getTimestamp());
                data.add(item);
            }
            obj.add("data", data);

            return obj;
        }
    }
}
