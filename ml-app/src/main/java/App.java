import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import event_hub.Receiver;
import storage.DataConverter;
import storage.StorageContext;
import storage.StorageManager;

/**
 * Created by VladVin on 15.11.2016.
 */
public class App {
    public static void main(String[] args) {
        StorageManager sm = new StorageManager();
        StorageContext.setStorageManager(sm);
        Receiver receiver = new Receiver();
        receiver.register();
//        sm.stopDBManager();
    }
}
