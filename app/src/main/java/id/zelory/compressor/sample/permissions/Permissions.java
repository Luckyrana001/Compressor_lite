package id.zelory.compressor.sample.permissions;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.ActivityCompat;

import org.compressor.imagecompress.R;

public class Permissions {
    Activity activity;
    Context context;

    public Permissions(Context context2, Activity activity2) {
        this.context = context2;
        this.activity = activity2;
    }

    public boolean checkPermission(final String str, String str2, final int i) {
        PermissionUtil.checkPermission(this.activity, this.context, str, str2, new PermissionUtil.PermissionAskListener() {
            public void onPermissionAsk() {
                ActivityCompat.requestPermissions(Permissions.this.activity, new String[]{str}, i);
                Constants.permissionAllowed = false;
            }

            public void onPermissionPreviouslyDenied() {
                ActivityCompat.requestPermissions(Permissions.this.activity, new String[]{str}, i);
                Constants.permissionAllowed = false;
            }

            public void onPermissionDisabled() {
                if (i == 20) {
                    Permissions permissions = Permissions.this;
                    permissions.askUserToAllowPermissionFromSetting(permissions.activity.getResources().getString(R.string.camera_permission_msg));
                } else {
                    Permissions permissions2 = Permissions.this;
                    permissions2.askUserToAllowPermissionFromSetting(permissions2.activity.getResources().getString(R.string.storage_permisson_msg));
                }
                Constants.permissionAllowed = false;
            }

            public void onPermissionGranted() {
                Constants.permissionAllowed = true;
            }
        });
        return Constants.permissionAllowed;
    }

    /* access modifiers changed from: private */
    public void askUserToAllowPermissionFromSetting(String str) {
        Builder builder = new Builder(this.activity);
        builder.setTitle(this.activity.getResources().getString(R.string.permission_required));
        builder.setMessage(str).setCancelable(false).setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", Permissions.this.activity.getPackageName(), null));
                Permissions.this.activity.startActivityForResult(intent, 20);
            }
        }).setNegativeButton(this.activity.getResources().getString(R.string.cancel), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }
}
