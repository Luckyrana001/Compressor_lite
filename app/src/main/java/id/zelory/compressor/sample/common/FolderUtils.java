package id.zelory.compressor.sample.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;

/* renamed from: id.zelory.compressor.sample.common.FolderUtils */
public class FolderUtils {
    private static final String PRIMARY_VOLUME_NAME = "primary";
    static String TAG = "TAG";

    public static boolean isKitkat() {
        return VERSION.SDK_INT == 19;
    }

    public static boolean isAndroid5() {
        return VERSION.SDK_INT >= 21;
    }

    @NonNull
    public static String getSdCardPath() {
        String sdCardDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            return new File(sdCardDirectory).getCanonicalPath();
        } catch (IOException ioe) {
            Log.e(TAG, "Could not get SD directory", ioe);
            return sdCardDirectory;
        }
    }

    public static ArrayList<String> getExtSdCardPaths(Context con) {
        ArrayList<String> paths = new ArrayList<>();
        File[] files = ContextCompat.getExternalFilesDirs(con, "external");
        File firstFile = files[0];
        for (File file : files) {
            if (file != null && !file.equals(firstFile)) {
                int index = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (index < 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected external file dir: ");
                    sb.append(file.getAbsolutePath());
                    Log.w("", sb.toString());
                } else {
                    String path = file.getAbsolutePath().substring(0, index);
                    try {
                        path = new File(path).getCanonicalPath();
                    } catch (IOException e) {
                    }
                    paths.add(path);
                }
            }
        }
        return paths;
    }

    @Nullable
    public static String getFullPathFromTreeUri(@Nullable Uri treeUri, Context con) {
        if (treeUri == null) {
            return null;
        }
        String volumePath = getVolumePath(getVolumeIdFromTreeUri(treeUri), con);
        if (volumePath == null) {
            return File.separator;
        }
        if (volumePath.endsWith(File.separator)) {
            volumePath = volumePath.substring(0, volumePath.length() - 1);
        }
        String documentPath = getDocumentPathFromTreeUri(treeUri);
        if (documentPath.endsWith(File.separator)) {
            documentPath = documentPath.substring(0, documentPath.length() - 1);
        }
        if (documentPath.length() <= 0) {
            return volumePath;
        }
        if (documentPath.startsWith(File.separator)) {
            StringBuilder sb = new StringBuilder();
            sb.append(volumePath);
            sb.append(documentPath);
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(volumePath);
        sb2.append(File.separator);
        sb2.append(documentPath);
        return sb2.toString();
    }

    private static String getVolumePath(String volumeId, Context con) {
        String str = volumeId;
        if (VERSION.SDK_INT < 21) {
            return null;
        }
        try {
            StorageManager mStorageManager = (StorageManager) con.getSystemService("storage");
            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList", new Class[0]);
            Method getUuid = storageVolumeClazz.getMethod("getUuid", new Class[0]);
            Method getPath = storageVolumeClazz.getMethod("getPath", new Class[0]);
            Method isPrimary = storageVolumeClazz.getMethod("isPrimary", new Class[0]);
            Object result = getVolumeList.invoke(mStorageManager, new Object[0]);
            int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String uuid = (String) getUuid.invoke(storageVolumeElement, new Object[0]);
                if (((Boolean) isPrimary.invoke(storageVolumeElement, new Object[0])).booleanValue() && PRIMARY_VOLUME_NAME.equals(str)) {
                    return (String) getPath.invoke(storageVolumeElement, new Object[0]);
                }
                if (uuid != null && uuid.equals(str)) {
                    return (String) getPath.invoke(storageVolumeElement, new Object[0]);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @TargetApi(21)
    private static String getVolumeIdFromTreeUri(Uri treeUri) {
        String[] split = DocumentsContract.getTreeDocumentId(treeUri).split(":");
        if (split.length > 0) {
            return split[0];
        }
        return null;
    }

    @TargetApi(21)
    public static String getDocumentPathFromTreeUri(Uri treeUri) {
        String[] split = DocumentsContract.getTreeDocumentId(treeUri).split(":");
        if (split.length < 2 || split[1] == null) {
            return File.separator;
        }
        return split[1];
    }
}
