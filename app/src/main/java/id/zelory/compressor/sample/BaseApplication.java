package id.zelory.compressor.sample;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;

public class BaseApplication extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(@Nullable Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        context = getApplicationContext();

        //Mint.disableNetworkMonitoring();
        //Mint.initAndStartSession(this, "781b0a9b");
    }
}
