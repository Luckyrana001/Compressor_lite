package id.zelory.compressor.sample;

import android.os.Environment;

import java.io.File;

/* renamed from: id.zelory.compressor.sample.Common */
public class Common {
    public static final int REQUEST_CODE = 1022;
    public static final int REQUEST_CODE_ANNOTATE = 6023;
    public static final int REQUEST_CODE_DIR_FULL_LIST_SELECTOR = 9001;
    public static final int REQUEST_CODE_DIR_SELECTOR = 9000;
    public static final int REQUEST_CODE_EDIT = 4022;
    public static final int REQUEST_CODE_GALLERY = 2022;
    public static final int REQUEST_CODE_PDF = 5022;
    public static final int REQUEST_CODE_PREVIEW_IMGAGE = 7022;
    public static final int REQUEST_CODE_SIGNATURE = 6022;
    public static String IMAGE_DIR_PATH = null;
    public static String IMAGE_NAME = null;
    public static String IMAGE_PATH = null;
    public static String PDF_PATH = null;
    public static int PICK_IMAGE_MULTIPLE = 1001;
    public static int PICK_PDF = 1002;
    public static int READ_REQUEST_CODE = 3022;
    public static File file;
    public static String newDirectoryName;
    public static int pdfQuality = 0;
    public static File tempfile;

    static {
        String str = "";
        IMAGE_PATH = str;
        IMAGE_DIR_PATH = str;
        IMAGE_NAME = str;
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getPath());
        sb.append("/PdfConverter/DocumentPdf.pdf");
        PDF_PATH = sb.toString();
        newDirectoryName = str;
    }
}
