package storage;

import com.datastax.driver.core.*;
import com.datastax.driver.extras.codecs.jdk8.InstantCodec;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by VladVin on 02.12.2016.
 */
public class DBManager {
    private static final String KEYSPACE_NAME = "white_snow";
    private static final String KEYSPACE_HOST = "127.0.0.1";

    private final Cluster cluster;
    private final Session session;

    public DBManager() {
        cluster = Cluster.builder().addContactPoint(KEYSPACE_HOST).build();
        session = cluster.connect(KEYSPACE_NAME);

        cluster.getConfiguration().getCodecRegistry()
                .register(InstantCodec.instance);
    }

    public List<FallInfo> getFalls(long tsOffset) {
        if (session == null || cluster == null) {
            return null;
        }

        PreparedStatement ps = session.prepare(
                "select user_id, timestamp, lat, lon from sensor_data where fall_status = 1 and timestamp > ? allow filtering;"
        );

        List<FallInfo> falls = new ArrayList<>();

        ResultSet rs = session.execute(ps.bind(tsOffset));
        for (Row row : rs.all()) {
            String userId = row.get("user_id", String.class);
            long timestamp = row.get("timestamp", Long.class);
            double lat = row.get("lat", Double.class);
            double lon = row.get("lon", Double.class);
            FallInfo f = new FallInfo(new Id(userId, timestamp), new Location(lat, lon));
            falls.add(f);
        }
        return falls;
    }
}
