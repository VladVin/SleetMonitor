package storage;

import java.util.List;

/**
 * Created by VladVin on 26.11.2016.
 */
public class StorageManager {
    private final DBManager dbm;

    public StorageManager() {
        dbm = new DBManager();
    }

    public void saveEventData(String json) {
        List<UserDataEntry> dataEntries = DataConverter.parseJson(json);

        dbm.saveDataEntries(dataEntries);
    }

    public void stopDBManager() {
        dbm.stop();
    }
}
