package id.zelory.compressor.sample.common;

import android.content.Context;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.Log;

import androidx.core.content.FileProvider;

import org.compressor.imagecompress.R;

import java.io.File;
import java.util.Objects;

/* renamed from: id.zelory.compressor.sample.common.GenerateImagePath */
public class GenerateImagePath {
    private Context context;

    public GenerateImagePath(Context context2) {
        this.context = context2;
    }

    public Uri getOutputMediaFileUri(int type) {
        if (VERSION.SDK_INT <= 23) {
            return Uri.fromFile(getOutputMediaFile(type));
        }
        return FileProvider.getUriForFile(this.context, context.getResources().getString(R.string.content_provide_path), (File) Objects.requireNonNull(getOutputMediaFile(type)));
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File("/storage/emulated/0/", "Pictures");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("Error", "Oops! Failed create  directory");
            return null;
        } else if (type != 1) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(mediaStorageDir.getPath());
            sb.append(File.separator);
            sb.append("image.jpg");
            return new File(sb.toString());
        }
    }
}
