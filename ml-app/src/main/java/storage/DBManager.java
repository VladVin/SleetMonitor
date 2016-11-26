package storage;

import com.datastax.driver.core.*;
import com.datastax.driver.core.schemabuilder.TableOptions;
import com.datastax.driver.extras.codecs.jdk8.InstantCodec;
import com.datastax.driver.extras.codecs.joda.DateTimeCodec;

import java.sql.Time;
import java.time.Instant;
import java.util.List;

/**
 * Created by VladVin on 26.11.2016.
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

        ResultSet rs = session.execute("select * from sensor_data");
        rs.forEach(row -> System.out.println(row.getString("user_id") + " " + row.getTimestamp("timestamp")));
    }

    public void saveDataEntries(List<UserDataEntry> dataEntries) {
        if (session == null || cluster == null) {
            return;
        }

        PreparedStatement prepared = session.prepare(
                "insert into sensor_data (user_id, x, y, z, lat, lon, timestamp, fall_info)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?)");

        for (UserDataEntry entry : dataEntries) {
            BoundStatement bound = prepared.bind(
                    entry.getUser_id(),
                    entry.getX(),
                    entry.getY(),
                    entry.getZ(),
                    entry.getLat(),
                    entry.getLon(),
                    Instant.ofEpochMilli(entry.getTimestamp()),
                    0
            );
            session.execute(bound);
        }
    }

    public void stop() {
        closeSession();
        closeCluster();
    }

    public void closeSession() {
        if (session != null) {
            session.close();
        }
    }

    public void closeCluster() {
        if (cluster != null) {
            cluster.close();
        }
    }
}
