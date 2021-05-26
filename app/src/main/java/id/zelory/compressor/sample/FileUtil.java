package id.zelory.compressor.sample;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/* renamed from: id.zelory.compressor.sample.FileUtil */
class FileUtil {
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final int EOF = -1;

    private FileUtil() {
    }

    public static String splitDateTime(String name) {
        return name.split(" ")[2];
    }

    public static String getMonth(String name) {
        String monthName = name.split(" ")[1];
        if (monthName.equalsIgnoreCase("Jan")) {
            return "January";
        }
        if (monthName.equalsIgnoreCase("Feb")) {
            return "February";
        }
        if (monthName.equalsIgnoreCase("Mar")) {
            return "March";
        }
        if (monthName.equalsIgnoreCase("Apr")) {
            return "April";
        }
        if (monthName.equalsIgnoreCase("May")) {
            return "May";
        }
        if (monthName.equalsIgnoreCase("Jun")) {
            return "June";
        }
        if (monthName.equalsIgnoreCase("Jul")) {
            return "July";
        }
        if (monthName.equalsIgnoreCase("Aug")) {
            return "August";
        }
        if (monthName.equalsIgnoreCase("Sep")) {
            return "September";
        }
        if (monthName.equalsIgnoreCase("Oct")) {
            return "October";
        }
        if (monthName.equalsIgnoreCase("Nov")) {
            return "November";
        }
        if (monthName.equalsIgnoreCase("Dec")) {
            return "December";
        }
        return monthName;
    }

    public static int getTimeDifference(Date d2, Date d1, int timeUnit) {
        Date diff = new Date(d2.getTime() - d1.getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(diff);
        int hours = calendar.get(11);
        int minutes = calendar.get(12);
        int seconds = calendar.get(13);
        if (timeUnit == 11) {
            return hours;
        }
        if (timeUnit == 12) {
            return minutes;
        }
        return seconds;
    }

    public static void updateNewTimeOnAdsShowClick() {
        try {
            Calendar c = Calendar.getInstance();
            new SimpleDateFormat("dd-MMM-yyyy HH:MM:ss");
            MainActivity.newClickTime = c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateNewStartTime() {
        try {
            Calendar c = Calendar.getInstance();
            new SimpleDateFormat("dd-MMM-yyyy HH:MM:ss");
            MainActivity.startDateTime = c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboardOnClick(Context context, IBinder windowToken) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(windowToken, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public static File from(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String fileName = getFileName(context, uri);
        String[] splitName = splitFileName(fileName);
        File tempFile = null;
        try {
            tempFile = File.createTempFile(splitName[0], splitName[1]);
        }catch (Exception e){
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            tempFile = File.createTempFile(timestamp.getTime()+"",splitName[1]);
        }
        tempFile = rename(tempFile, fileName);
        tempFile.deleteOnExit();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            copy(inputStream, out);
            inputStream.close();
        }

        if (out != null) {
            out.close();
        }
        return tempFile;
    }


    private static String[] splitFileName(String fileName) {
        String name = fileName;
        String extension = "";
        int i = fileName.lastIndexOf(".");
        if (i != -1) {
            name = fileName.substring(0, i);
            extension = fileName.substring(i);
        }
        return new String[]{name, extension};
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0034, code lost:
        if (r1 == null) goto L_0x0042;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003d, code lost:
        if (r1 != null) goto L_0x003f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003f, code lost:
        r1.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf(File.separator);
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private static File rename(File file, String newName) {
        File newFile = new File(file.getParent(), newName);
        if (!newFile.equals(file)) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old " + newName + " file");
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to " + newName);
            }
        }
        return newFile;
    }

    private static long copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int n;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
