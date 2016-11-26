import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import storage.DataConverter;
import storage.StorageManager;

/**
 * Created by VladVin on 15.11.2016.
 */
public class HelloCassandra {
    public static void main(String[] args) {
        String json = "{\n" +
                "    \"user_id\": \"new_user1\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"x\": 0.425,\n" +
                "            \"y\": 0.345,\n" +
                "            \"z\": 0.725,\n" +
                "            \"lat\": 0.532634563,\n" +
                "            \"lon\": 0.283765404,\n" +
                "            \"ts\": 1480161696\n" +
                "        },\n" +
                "        {\n" +
                "            \"x\": -0.542,\n" +
                "            \"y\": -0.245,\n" +
                "            \"z\": -0.125,\n" +
                "            \"lat\": 0.535673433,\n" +
                "            \"lon\": 0.283345346,\n" +
                "            \"ts\": 1480161687\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        String json1 = "{\"data\": \"value\"}";

        DataConverter.parseJson(json);

//        StorageManager sm = new StorageManager();
//        sm.saveEventData("");
//        sm.stopDBManager();
    }
}
