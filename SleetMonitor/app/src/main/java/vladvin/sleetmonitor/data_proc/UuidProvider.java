package vladvin.sleetmonitor.data_proc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by VladVin on 28.11.2016.
 */

public class UuidProvider {
    private UUID userId;
    private static final String PREFERENCES_NAME = "UserIdSettings";
    private static final String USER_ID_PREF_KEY = "userId";

    UuidProvider(Context context) {
        readUserIdFromPreferences(context);
    }

    UUID getUserId() {
        return userId;
    }

    private void readUserIdFromPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, 0);
        String id = settings.getString(USER_ID_PREF_KEY, null);
        if (id == null) {
            id = UUID.randomUUID().toString();
            saveNewUserId(settings, id);
        }
        userId = UUID.fromString(id);
    }

    private void saveNewUserId(SharedPreferences settings, String id) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(USER_ID_PREF_KEY, id);
        editor.apply();
    }
}
