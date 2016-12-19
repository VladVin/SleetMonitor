import location_service.LocationService;
import storage.*;

import java.util.List;

import static spark.Spark.get;

public class App {
    private static LocationService locService;

    public static void main(String[] args) {
        DBManager dbm = new DBManager();
        GeoRTree geoRTree = new GeoRTree();
        locService = new LocationService(geoRTree, dbm);
        locService.start();

        get("/searchLocations/:rect", (req, res) -> {
            String json = req.params(":rect").substring(5);
            List<Location> locs = LocationDeserializer.deserialize(json);
            List<Location> searchRes = locService.search(locs.get(0), locs.get(1));
            return LocationDeserializer.serialize(searchRes); }
        );
    }
}
