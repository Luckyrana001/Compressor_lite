package id.zelory.compressor.sample.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.afollestad.materialdialogs.MaterialDialog;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import org.compressor.imagecompress.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import id.zelory.compressor.sample.MainActivity;

import static id.zelory.compressor.sample.MainActivity.CREATE_FILE;

//import static org.androidluckyguys.docscanner.pdfExtract.PdfUtils.pdftoText;

public class CreatePdfAndPreview {

    Context context;
    Activity activity;

    public CreatePdfAndPreview(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /*public void createMultiplePdf(String dest, int action, ArrayList<String> folderList, String basePath) {
        try {

            Rectangle pageSize = PageSize.A4;

            float width = PageSize.A4.getWidth();
            float height = PageSize.A4.getHeight();

            ArrayList<Uri> pdfList = new ArrayList<>();

            for (String folderName : folderList) {
                ArrayList<String> imagesList = new ArrayList<>();
                imagesList = MainActivity.directoryList.get(folderName);


                float left = 0;
                float right = 0;
                float top = 10;
                float bottom = 20;
                //String USER_PASS = MainActivity.password;

                //Rectangle two = new Rectangle(2448,3264);
                File docsFolder = new File(Environment.getExternalStorageDirectory() + "/pdfFiles");
                if (!docsFolder.exists()) {
                    docsFolder.mkdir();
                    Log.i("", "Created a new directory for PDF");
                }

                String[] imageArray = new String[imagesList.size()];
                imageArray = imagesList.toArray(imageArray);

                for (String s : imageArray) {
                    System.out.println(s);
                }

                File pdfFile = new File(docsFolder.getAbsolutePath(), folderName + ".pdf");

                OutputStream output = new FileOutputStream(pdfFile);
                Document document = null;

                *//*if (pdfFormat == 0) {

                    document = new Document(PageSize.A3);
                    pageSize = PageSize.A3;
                } else if (pdfFormat == 1) {
*//*
                    document = new Document(PageSize.A4);
                    pageSize = PageSize.A4;
               *//* } else if (pdfFormat == 2) {

                    document = new Document(PageSize.A5);
                    pageSize = PageSize.A5;
                } else if (pdfFormat == 3) {

                    document = new Document(PageSize.LEGAL);
                    pageSize = PageSize.LEGAL;

                } else if (pdfFormat == 4) {

                    document = new Document(PageSize.EXECUTIVE);
                    pageSize = PageSize.EXECUTIVE;
                } else if (pdfFormat == 5) {
                    document = new Document(PageSize.POSTCARD);
                    pageSize = PageSize.POSTCARD;

                } else if (pdfFormat == 6) {

                    document = new Document(PageSize.LETTER);
                    pageSize = PageSize.LETTER;
                }*//*

                PdfWriter writer = PdfWriter.getInstance(document, output);


                *//*if (pdfEncryption) {
                    writer.setEncryption(USER_PASS.getBytes(), null,
                            PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
                }*//*


     *//*  MyFooter event = new MyFooter();
                writer.setPageEvent(event);*//*

                document.open();
                document.setMargins(left, right, top, bottom);


                //  pageSize = PageSize.A4;

                // document.setPageSize(two);

                for (String image : imageArray) {
                    Image img = Image.getInstance(image);

                    width = pageSize.getWidth();
                    height = pageSize.getHeight();

                    img.scaleToFit(width, height);
                    // img = Image.getInstance(image);


                    document.newPage();
                    // img.setAbsolutePosition(0, 100);
                    document.add(img);

                }
                // document.add(new Paragraph(mContentEditText.getText().toString()));

                document.close();


                Uri fileUri = Uri.fromFile(pdfFile);
                pdfList.add(fileUri);

                //previewPdf(pdfFile, action);

            }


            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND_MULTIPLE);
            share.setType("application/pdf");
            share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, pdfList);

            activity.startActivity(share);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    public void createPdf(String dest, int action, ArrayList<String> imagesList, String name, boolean directShareRequired) throws IOException, DocumentException {
        try {
            float left = 36;
            float right = 36;
            float top = 36;
            float bottom = 72;
            String USER_PASS = "password";//MainActivity.password;

            //Rectangle two = new Rectangle(2448,3264);
            File docsFolder = new File(dest);
            if (!docsFolder.exists()) {
                docsFolder.mkdir();
                Log.i("", "Created a new directory for PDF");
            }

            String[] imageArray = new String[imagesList.size()];
            imageArray = imagesList.toArray(imageArray);

            for (String s : imageArray) {
                System.out.println(s);
            }

            File pdfFile = new File(docsFolder.getAbsolutePath(), name + ".pdf");
            OutputStream output = new FileOutputStream(pdfFile);
            Document document = null;
            Rectangle pageSize;

            pageSize = PageSize.A4;
            /*if (pdfFormat == 0) {

                document = new Document(PageSize.A3, 0, 10, 10, 20);
                pageSize = PageSize.A3;
            } else if (pdfFormat == 1) {
*/
            document = new Document(PageSize.A4, 0, 10, 10, 20);
            pageSize = PageSize.A4;
          /*  } else if (pdfFormat == 2) {

                document = new Document(PageSize.A5, 0, 10, 10, 20);
                pageSize = PageSize.A5;
            } else if (pdfFormat == 3) {

                document = new Document(PageSize.LEGAL, 0, 10, 10, 20);
                pageSize = PageSize.LEGAL;

            } else if (pdfFormat == 4) {

                document = new Document(PageSize.EXECUTIVE, 0, 10, 10, 20);
                pageSize = PageSize.EXECUTIVE;
            } else if (pdfFormat == 5) {
                document = new Document(PageSize.POSTCARD, 0, 10, 10, 20);
                pageSize = PageSize.POSTCARD;

            } else if (pdfFormat == 6) {

                document = new Document(PageSize.LETTER, 0, 10, 10, 20);
                pageSize = PageSize.LETTER;
            }*/


            PdfWriter writer = PdfWriter.getInstance(document, output);
            if (false) {
                writer.setEncryption(USER_PASS.getBytes(), null, PdfWriter.ALLOW_PRINTING, PdfWriter.ENCRYPTION_AES_128);
            }

            if (false) {
                MyFooter event = new MyFooter();
                writer.setPageEvent(event);
            }

            document.open();
            // document.setMargins(left, right, top, bottom);


            // float width1 = pageSize.getWidth();
            //  float height2 = pageSize.getHeight();
            // document.setPageSize(two);

            for (String image : imageArray) {

                Image img = Image.getInstance(image);
                img.scaleToFit(pageSize.getWidth(), pageSize.getHeight() - 72);
                float x = (pageSize.getWidth() - img.getScaledWidth()) / 2;
                float y = (pageSize.getHeight() - img.getScaledHeight()) / 2;
                img.setAbsolutePosition(x, y);


                //img.scaleToFit(width1, height2);
                // img = Image.getInstance(image);
                //document.setPageSize(pageSize);
                document.newPage();
                // img.setAbsolutePosition(0, 100);
                document.add(img);

            }
            // document.add(new Paragraph(mContentEditText.getText().toString()));

            document.close();
            if (directShareRequired) {
                previewPdf(pdfFile, action);
            }else{
                MainActivity.savePdfToPhoneFilePath = pdfFile .getAbsolutePath();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void previewPdf(File pdfFile, int action) {


        try {

            if (action == 0) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.fromFile(pdfFile));
                intent.putExtra(Intent.EXTRA_SUBJECT, pdfFile.getName());
                //if (!MainActivity.packageName.equals(context.getPackageName())) {
                intent.putExtra(Intent.EXTRA_TEXT, "Created By : https://play.google.com/store/apps/details?id=org.compressor.imagecompress");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                //}
                activity.startActivity(intent);

            }

          else  if (action == 1) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_TITLE, pdfFile.getName());
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, activity.getString(R.string.content_provide_path), pdfFile));
                 intent.putExtra(Intent.EXTRA_TEXT, "Created By : https://play.google.com/store/apps/details?id=org.compressor.imagecompress");
                Intent shareIntent = Intent.createChooser(intent, null);
                activity.startActivity(shareIntent);

            }
            else  if (action == 2) {
                createFile(FileProvider.getUriForFile(context, activity.getString(R.string.content_provide_path), pdfFile));
            }
        } catch (Exception e) {
            e.printStackTrace();

            new MaterialDialog.Builder(context)
                    .title("PDF App not Found")
                    .content("Your Phone doesn't have any in-built pdf opener app, Please download one from Google Play Store.")
                    .positiveText(R.string.CANCEL)
                    .negativeText(R.string.OK)
                    .cancelable(false)
                    .iconRes(R.drawable.ic_dialog_alert)
                    //.neutralColorRes(R.color.Salmon_fourth_addon_bottom)
                    //.negativeColorRes(R.color.Salmon_fourth_addon_bottom)
                    .positiveColorRes(R.color.ytl_theme_blue_color)
                    .onPositive((dialog, which) -> {

                    })

                    .onNegative((dialog, which) -> dialog.cancel()).show();
        }
    }


    private void createFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, "invoice.pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, pickerInitialUri);

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS));

        activity.startActivityForResult(intent, CREATE_FILE);
    }
    class MyFooter extends PdfPageEventHelper {
        Font ffont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

        public void onEndPage(PdfWriter writer, Document document) {

            PdfContentByte cb = writer.getDirectContent();
            // Phrase header = new Phrase("Scanned By : A-One Scanner", ffont);
            Phrase footer = new Phrase("Pdf Fotter text", ffont);
           /* ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                    header,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.top() + 5, 0);*/
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                    footer,
                    (document.right() - document.left()) / 2 + document.leftMargin(),
                    document.bottom() - 5, 0);
        }
    }

    public class FooterTable extends PdfPageEventHelper {
        protected PdfPTable footer;

        public FooterTable(PdfPTable footer) {
            this.footer = footer;
        }

        public void onEndPage(PdfWriter writer, Document document) {

            footer.writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());
        }
    }


    public class Header extends PdfPageEventHelper {

        protected Phrase header;

        public void setHeader(Phrase header) {
            this.header = header;
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte canvas = writer.getDirectContentUnder();
            ColumnText.showTextAligned(canvas, Element.ALIGN_RIGHT, header, 559, 806, 0);
        }
    }


}
