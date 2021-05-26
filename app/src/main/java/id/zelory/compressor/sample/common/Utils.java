package id.zelory.compressor.sample.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings.System;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;

import org.compressor.imagecompress.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import id.zelory.compressor.sample.Common;

/* renamed from: id.zelory.compressor.sample.common.Utils */
public class Utils {
    public static Uri reducedSizeUri = null;
    Context context;

    public Utils(Context context2) {
        this.context = context2;
    }

    public static void hideKeyboardOnClick(Context context2, IBinder windowToken) {
        ((InputMethodManager) context2.getSystemService("input_method")).hideSoftInputFromWindow(windowToken, 2);
    }

    public static String fileSize(File file) {
        return readableFileSize(file.length());
    }

    public static String readableFileSize(long size) {
        if (size <= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(size);
            sb.append(" B");
            return sb.toString();
        }
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10((double) size) / Math.log10(1024.0d));
        StringBuilder sb2 = new StringBuilder();
        sb2.append(new DecimalFormat("#,##0.##").format(((double) size) / Math.pow(1024.0d, (double) digitGroups)));
        sb2.append(" ");
        sb2.append(units[digitGroups]);
        return sb2.toString();
    }



    public static boolean isNetworkConnected(Context context2, boolean isShowMessage, int teamId) {
        return isNetworkConnected(context2, isShowMessage, false, teamId);
    }

    public static boolean isNetworkConnected(Context context2, boolean isShowMessage, boolean isClose, int teamId) {
        String message;
        try {
            ConnectivityManager cm = (ConnectivityManager) context2.getSystemService("connectivity");
            String str = "";
            boolean isConnected = true;
            if (System.getInt(context2.getContentResolver(), "airplane_mode_on", 0) != 0) {
                message = "Airplane Mode Turn On, Use Wi-Fi or Cellular data to Access Data";
            } else {
                message = context2.getResources().getString(R.string.no_internet_connection);
            }
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnected()) {
                isConnected = false;
            }
            if (!isConnected && isShowMessage) {
                showNoNetworkMessage(context2, isClose, message, teamId);
            }
            return isConnected;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void showNoNetworkMessage(final Context context2, boolean isClose, String message, int teamId) {
        new Builder(context2).content((CharSequence) message).positiveText((int) R.string.action_settings).negativeText((int) R.string.OK).cancelable(false).iconRes(R.drawable.ic_dialog_alert).positiveColorRes(R.color.ytl_theme_blue_color).onPositive(new SingleButtonCallback() {
            public void onClick(MaterialDialog dialog, DialogAction which) {
                Intent intent = new Intent("android.settings.SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context2.startActivity(intent);
                dialog.dismiss();
            }
        }).onNegative(new SingleButtonCallback() {
            public void onClick(MaterialDialog dialog, DialogAction which) {
                dialog.cancel();
            }
        }).show();
    }

    public static void customAlertDialog(Context context2, AlertDialog alertDialog) {
        if (alertDialog != null) {
            Button button = alertDialog.getButton(-2);
            Button b = alertDialog.getButton(-1);
        }
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int height;
        int width;
        float bitmapRatio = ((float) image.getWidth()) / ((float) image.getHeight());
        if (bitmapRatio > 1.0f) {
            width = maxSize;
            height = (int) (((float) width) / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (((float) height) * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static void bitmapToUriConvert(String path, Bitmap mBitmap) {
        try {
            FileOutputStream out = new FileOutputStream(new File(path), false);
            mBitmap.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap cloneBitmap(Bitmap bitmap) {
        return bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap getBitmap(Context context2, Uri uri) throws IOException {
        return Media.getBitmap(context2.getContentResolver(), uri);
    }

    public static String splitImageName(String name) {
        String[] arrOfStr = name.split("/");
        return arrOfStr[arrOfStr.length - 1];
    }

    public static String splitImageNameTwo(String name) {
        String[] arrOfStr = name.split("_");
        int length = arrOfStr.length;
        StringBuilder sb = new StringBuilder();
        sb.append(arrOfStr[0]);
        sb.append(" ");
        sb.append(arrOfStr[1]);
        return sb.toString();
    }

    public static String splitName(String name) {
        if (!name.contains("New Doc ")) {
            return name;
        }
        String str = " ";
        String[] arrOfStr = name.split(str);
        StringBuilder sb = new StringBuilder();
        sb.append(arrOfStr[0]);
        sb.append(str);
        sb.append(arrOfStr[1]);
        sb.append(str);
        sb.append(arrOfStr[2]);
        return sb.toString();
    }

    public static String splitDate(String date) {
        return date.split("GMT")[0];
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter) {
        return Bitmap.createScaledBitmap(realImage, realImage.getWidth(), realImage.getHeight(), filter);
    }
}
