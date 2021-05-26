package id.zelory.compressor.sample.common;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

/* renamed from: id.zelory.compressor.sample.common.BaseFlyContext */
public class BaseFlyContext {
    public static String reasponce;
    private static BaseFlyContext flyContext;
    private Context context;
    private Activity currentActivity;
    private Resources resources;

    private BaseFlyContext() {
    }

    public static BaseFlyContext getInstant() {
        if (flyContext == null) {
            flyContext = new BaseFlyContext();
        }
        return flyContext;
    }

    public Context getApplicationContext() {
        return this.context;
    }

    public void setApplicationContext(Context c) {
        this.context = c;
    }

    public Activity getActivity() {
        return this.currentActivity;
    }

    public void setActivity(Activity a) {
        this.currentActivity = a;
        setResources(a.getResources());
        setApplicationContext(a.getApplicationContext());
    }

    public Resources getResources() {
        return this.resources;
    }

    public void setResources(Resources resources2) {
        this.resources = resources2;
    }
}
