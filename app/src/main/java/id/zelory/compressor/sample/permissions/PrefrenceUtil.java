package id.zelory.compressor.sample.permissions;

import android.content.Context;

public class PrefrenceUtil {
    public static void firstTimeAskingPermission(Context context, String str, boolean z, String str2) {
        context.getSharedPreferences(str2, 0).edit().putBoolean(str, z).apply();
    }

    public static boolean isFirstTimeAskingPermission(Context context, String str, String str2) {
        return context.getSharedPreferences(str2, 0).getBoolean(str, true);
    }
}
