package storage;

import java.util.List;
import java.util.stream.Collectors;

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

        List<String> usersIds = dataEntries.stream()
                .map(UserDataEntry::getUser_id)
                .distinct()
                .collect(Collectors.toList());

        dbm.updateUsersStatuses(usersIds);
    }

    public void stopDBManager() {
        dbm.stop();
    }
}
