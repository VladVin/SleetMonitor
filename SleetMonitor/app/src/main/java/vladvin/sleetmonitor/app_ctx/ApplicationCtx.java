package vladvin.sleetmonitor.app_ctx;

import android.app.Application;
import android.content.Context;

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
