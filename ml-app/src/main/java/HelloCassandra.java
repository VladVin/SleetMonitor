import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 * Created by VladVin on 15.11.2016.
 */
public class HelloCassandra {
    public static void main(String[] args) {
        Cluster cluster;
        Session session;

        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect("FamilyDB");

        ResultSet rs = session.execute("SELECT * FROM Person");
        rs.forEach(row -> System.out.println(row.getString("lastname") + " " + row.getInt("age")));
    }
}
