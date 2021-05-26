package id.zelory.compressor.sample.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import org.compressor.imagecompress.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CreateZipFile {

    Context context;
    Activity activity;

    public CreateZipFile(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    //image size : 2448 * 3264
   /* public void createZipFile() throws BadElementException {
        // These are the files to include in the ZIP file
        String[] imageArray = new String[imagesList.size()];
        imageArray = imagesList.toArray(imageArray);

        for (String s : imageArray) {
            System.out.println(s);
        }
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];

        try {
            // Create the ZIP file
            //String target = "target.zip";
            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/pdfFiles");

            File zipFile = new File(docsFolder.getAbsolutePath(), "Sample.zip");

            //OutputStream output = new FileOutputStream(zipFile);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

            // Compress the files
            for (int i=0; i<imageArray.length; i++) {

                File file = new File(imageArray[i]);
                // BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                // Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
                // bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
                // Bitmap bitmap = null;

                FileInputStream in = new FileInputStream(file);


                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(imageArray[i]));

                // Transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                // Complete the entry
                out.closeEntry();
                in.close();
            }

            // Complete the ZIP file
            out.close();
            shareZipFile(zipFile);
        } catch (IOException e) {
            System.out.org.androidluckyguys.docscanner.docsign.digitalSignature.print(e.toString());
        }
    }*/

    public void zipSingleFile(final String sourcePath, final String toLocation) {

        final int BUFFER = 2048;

        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            sourceFile.mkdir();
            Log.i("", "Created a new directory for PDF");
        }

        try {

            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

                    /*if (sourceFile.isDirectory()) {
                        zipSubFolder(out, sourceFile, sourceFile.getParent().length());
                    } else {*/
            byte data[] = new byte[BUFFER];
            FileInputStream fi = new FileInputStream(sourcePath);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            //}
            out.close();
            File zipFile = new File(toLocation);

            shareZipFile(zipFile);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void multipleZipFileAtPath(ArrayList<String> folderNameList, String srcLocation, String toLocation) {


        ArrayList<Uri> zipFileList = new ArrayList<>();

        for (String folderName : folderNameList) {

            String sourcePath = srcLocation + folderName + "/";
            final int BUFFER = 2048;

            File sourceFile = new File(sourcePath);
            try {
                BufferedInputStream origin = null;
                String outputLoc = toLocation + "" + folderName + ".zip";
                FileOutputStream dest = new FileOutputStream(outputLoc);
                ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

                if (sourceFile.isDirectory()) {
                    zipSubFolder(out, sourceFile, sourceFile.getParent().length());
                } else {
                    byte data[] = new byte[BUFFER];
                    FileInputStream fi = new FileInputStream(sourcePath);
                    origin = new BufferedInputStream(fi, BUFFER);
                    ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                }
                out.close();
                File zipFile = new File(toLocation + "" + folderName + ".zip");


                Uri fileUri = Uri.fromFile(zipFile);
                zipFileList.add(fileUri);

            } catch (Exception e) {
                e.printStackTrace();

            }

        }


        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND_MULTIPLE);
        share.setType("application/zip");
        share.putExtra(Intent.EXTRA_SUBJECT, "Multiple Zip File Share");
        //if (!MainActivity.packageName.equals(context.getPackageName())) {
        share.putExtra(Intent.EXTRA_TEXT, "Zip Made By : https://play.google.com/store/apps/details?id=org.compressor.imagecompress");
        // }
        share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, zipFileList);

        activity.startActivity(share);
    }


    public void zipFileAtPath(final String sourcePath, final String toLocation) {


        final int BUFFER = 2048;

        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            sourceFile.mkdir();
            Log.i("", "Created a new directory for PDF");
        }

        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(toLocation);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            if (sourceFile.isDirectory()) {
                zipSubFolder(out, sourceFile, sourceFile.getParent().length());
            } else {
                byte data[] = new byte[BUFFER];
                FileInputStream fi = new FileInputStream(sourcePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(getLastPathComponent(sourcePath));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
            }
            out.close();
            File zipFile = new File(toLocation);

            shareZipFile(zipFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     *
     * Zips a subfolder
     *
     */

    private void zipSubFolder(ZipOutputStream out, File folder, int basePathLength) throws IOException {

        final int BUFFER = 2048;

        File[] fileList = folder.listFiles();
        BufferedInputStream origin = null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                zipSubFolder(out, file, basePathLength);
            } else {
                byte data[] = new byte[BUFFER];
                String unmodifiedFilePath = file.getPath();
                String relativePath = unmodifiedFilePath
                        .substring(basePathLength);
                FileInputStream fi = new FileInputStream(unmodifiedFilePath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(relativePath);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
        }
    }

    /*
     * gets the last path component
     *
     * Example: getLastPathComponent("downloads/example/fileToZip");
     * Result: "fileToZip"
     */
    public String getLastPathComponent(String filePath) {
        String[] segments = filePath.split("/");
        if (segments.length == 0)
            return "";
        String lastPathComponent = segments[segments.length - 1];
        return lastPathComponent;
    }


    private void shareZipFile(File zipFile) {
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/zip");
        share.putExtra(Intent.EXTRA_SUBJECT, zipFile.getName());
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        //if (!MainActivity.packageName.equals(context.getPackageName())) {
        share.putExtra(Intent.EXTRA_TEXT, "Zip Made By : https://play.google.com/store/apps/details?id=org.compressor.imagecompress");
        //}
        share.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, activity.getString(R.string.content_provide_path), zipFile));
        Intent shareIntent = Intent.createChooser(share, null);
        activity.startActivity(shareIntent);
    }

}
