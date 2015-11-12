package vladvin.sleetmonitordata;

import android.app.Application;
import android.content.Context;

/**
 * Created by VladVin on 19.10.2015.
 */
public class ApplicationCtx extends Application {
    private static ApplicationCtx instance;

    public static ApplicationCtx getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
