import location_service.LocationService;
import storage.*;

import java.util.List;

import static spark.Spark.get;

public class App {
    private static LocationService locService;

    public static void main(String[] args) {
//        String json = "[{\"lat\":0.325235, \"lon\":0.23425}, {\"lat\":0.325235, \"lon\":0.23425}]";
//        new Location(3, 3), new Location(7, 7)

        DBManager dbm = new DBManager();
        GeoRTree geoRTree = new GeoRTree();
        locService = new LocationService(geoRTree, dbm);
        locService.start();

//        String json = "[ {\"lat\" : 55.63274425319409, \"lon\" : 48.62411465495825 } , {\"lat\" : 55.8723253804882, \"lon\" : 48.871307373046875 } ]";
//        List<Location> locs = LocationDeserializer.deserialize(json);
//        List<Location> searchRes = locService.search(locs.get(0), locs.get(1));

        get("/searchLocations/:rect", (req, res) -> {
            String json = req.params(":rect").substring(5);
            List<Location> locs = LocationDeserializer.deserialize(json);
            List<Location> searchRes = locService.search(locs.get(0), locs.get(1));
            return LocationDeserializer.serialize(searchRes); }
        );
    }
}
