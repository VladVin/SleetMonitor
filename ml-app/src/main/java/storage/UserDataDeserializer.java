package storage;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VladVin on 26.11.2016.
 */
public class UserDataDeserializer implements JsonDeserializer<List<UserDataEntry>> {
    @Override
    public List<UserDataEntry> deserialize(
            JsonElement jsonElement, Type type,
            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        List<UserDataEntry> dataEntries = new ArrayList<>();

        String user_id = jsonElement.getAsJsonObject().get("user_id").getAsString();
        JsonArray dataArray = jsonElement.getAsJsonObject().get("data").getAsJsonArray();

        for (JsonElement elem : dataArray) {
            JsonObject elemObj = elem.getAsJsonObject();
            UserDataEntry dataEntry = new UserDataEntry(
                    user_id,
                    elemObj.get("x").getAsFloat(),
                    elemObj.get("y").getAsFloat(),
                    elemObj.get("z").getAsFloat(),
                    elemObj.get("lat").getAsDouble(),
                    elemObj.get("lon").getAsDouble(),
                    elemObj.get("ts").getAsLong()
            );
            dataEntries.add(dataEntry);
        }

        return dataEntries;
    }
}
