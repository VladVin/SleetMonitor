package storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataConverter {
    public static List<UserDataEntry> parseJson(String json) {
        List<UserDataEntry> dataEntries = new ArrayList<>();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        new TypeToken<List<UserDataEntry>>() {}.getType(),
                        new UserDataDeserializer())
                .create();
        return gson.fromJson(json, new TypeToken<List<UserDataEntry>>() {}.getType());
    }
}
