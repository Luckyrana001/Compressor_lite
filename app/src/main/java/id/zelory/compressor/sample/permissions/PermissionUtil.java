package id.zelory.compressor.sample.permissions;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;

import androidx.core.app.ActivityCompat;

public class PermissionUtil {

    public static boolean shouldAskPermission() {
        return VERSION.SDK_INT >= 23;
    }

    private static boolean shouldAskPermission(Context context, String str) {
        return shouldAskPermission() && ActivityCompat.checkSelfPermission(context, str) != 0;
    }

    public static void checkPermission(Activity activity, Context context, String str, String str2, PermissionAskListener permissionAskListener) {
        if (!shouldAskPermission(context, str)) {
            permissionAskListener.onPermissionGranted();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity, str)) {
            permissionAskListener.onPermissionPreviouslyDenied();
        } else if (PrefrenceUtil.isFirstTimeAskingPermission(context, str, str2)) {
            PrefrenceUtil.firstTimeAskingPermission(context, str, false, str2);
            permissionAskListener.onPermissionAsk();
        } else {
            permissionAskListener.onPermissionDisabled();
        }
    }

    public interface PermissionAskListener {
        void onPermissionAsk();

        void onPermissionDisabled();

        void onPermissionGranted();

        void onPermissionPreviouslyDenied();
    }
}
