package storage;

/**
 * Created by VladVin on 26.11.2016.
 */
public class StorageContext {
    private static StorageManager storageManager;

    public static void setStorageManager(StorageManager storageManager) {
        StorageContext.storageManager = storageManager;
    }

    public static StorageManager getStorageManager() {
        return storageManager;
    }
}
