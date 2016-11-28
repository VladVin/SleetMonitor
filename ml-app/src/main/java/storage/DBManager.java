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
class DBManager {
    private static final String KEYSPACE_NAME = "white_snow";
    private static final String KEYSPACE_HOST = "127.0.0.1";

    private final Cluster cluster;
    private final Session session;

    DBManager() {
        cluster = Cluster.builder().addContactPoint(KEYSPACE_HOST).build();
        session = cluster.connect(KEYSPACE_NAME);

        cluster.getConfiguration().getCodecRegistry()
                .register(InstantCodec.instance);
    }

    void saveDataEntries(List<UserDataEntry> dataEntries) {
        if (session == null || cluster == null) {
            return;
        }

        PreparedStatement ps = session.prepare(
                "insert into sensor_data (user_id, x, y, z, lat, lon, timestamp, fall_status)" +
                "values (?, ?, ?, ?, ?, ?, ?, ?)");
        BatchStatement bs = new BatchStatement();

        for (UserDataEntry entry : dataEntries) {
            bs.add(ps.bind(
                    entry.getUser_id(),
                    entry.getX(),
                    entry.getY(),
                    entry.getZ(),
                    entry.getLat(),
                    entry.getLon(),
                    entry.getTimestamp(),
                    -1
            ));
        }
        session.executeAsync(bs);
    }

    void updateUsersStatuses(List<String> usersIds) {
        if (session == null || cluster == null) {
            return;
        }

        PreparedStatement ps = session.prepare(
                "update update_info set is_updated = true where user_id = ?"
        );
        BatchStatement bs = new BatchStatement();

        for (String userId : usersIds) {
            bs.add(ps.bind(
                    userId
            ));
        }
        session.executeAsync(bs);
    }

    void stop() {
        closeSession();
        closeCluster();
    }

    private void closeSession() {
        if (session != null) {
            session.close();
        }
    }

    private void closeCluster() {
        if (cluster != null) {
            cluster.close();
        }
    }
}
