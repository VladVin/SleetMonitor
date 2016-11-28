package storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class DataConverter {
    public static List<UserDataEntry> parseJson(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        new TypeToken<List<UserDataEntry>>() {}.getType(),
                        new UserDataDeserializer())
                .create();
        return gson.fromJson(json, new TypeToken<List<UserDataEntry>>() {}.getType());
    }
}
