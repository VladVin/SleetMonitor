package storage;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VladVin on 02.12.2016.
 */
public class LocationDeserializer {
    public static List<Location> deserialize(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Location>>(){}.getType());
    }

    public static String serialize(List<Location> locs) {
        Gson gson = new Gson();
        return gson.toJson(locs, new TypeToken<List<Location>>(){}.getType());
    }
}
