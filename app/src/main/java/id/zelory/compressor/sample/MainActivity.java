package id.zelory.compressor.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.integration.IntegrationHelper;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.itextpdf.text.DocumentException;
import com.leinardi.android.speeddial.SpeedDialView;
import com.splunk.mint.Mint;

import org.compressor.imagecompress.BuildConfig;
import org.compressor.imagecompress.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import id.zelory.compressor.sample.common.BaseActivity;
import id.zelory.compressor.sample.common.CreatePdfAndPreview;
import id.zelory.compressor.sample.common.CreateZipFile;
import id.zelory.compressor.sample.common.FolderUtils;
import id.zelory.compressor.sample.common.GenerateImagePath;
import id.zelory.compressor.sample.permissions.PermissionResultCallback;
import id.zelory.compressor.sample.permissions.PermissionUtils;
import id.zelory.compressor.sample.permissions.Permissions;
import me.echodev.resizer.Resizer;

import static id.zelory.compressor.sample.Common.REQUEST_CODE_DIR_FULL_LIST_SELECTOR;
import static id.zelory.compressor.sample.FileUtil.hideKeyboardOnClick;
import static id.zelory.compressor.sample.FileUtil.updateNewStartTime;
import static id.zelory.compressor.sample.permissions.Constants.MY_PERMISSIONS_REQUEST_CAMERA;
import static id.zelory.compressor.sample.permissions.Constants.MY_PERMISSIONS_REQUEST_STORAGE;
import static id.zelory.compressor.sample.permissions.Constants.WRITE_STORAGE_PERMISSION;

public class MainActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void showAdsAgain() {


        IronSourc_AppKey = getResources().getString(R.string.app_id);
        interAdsPlacement = getResources().getString(R.string.interstatial_home);
        interBannerPlacement = getResources().getString(R.string.home_banner_ad);

        IronSource.init(this, IronSourc_AppKey, IronSource.AD_UNIT.BANNER, IronSource.AD_UNIT.INTERSTITIAL);

        showBanner();
        loadINterstatialAd();
    }

    private void getExternalIntentData() {

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } /*else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        }else {
            showToast("Not able to parse Data");
            // Handle other intents, such as being started from the home screen
        } */
    }

    void handleSendImage(Intent intent) {
        clearImage();
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

        if (imageUri != null) {
            try {
                actualImage = FileUtil.from(this, imageUri);
                actualImageView.setImageBitmap(BitmapFactory.decodeFile(actualImage.getAbsolutePath()));
                actualSizeTextView.setText(String.format("Size : %s", getReadableFileSize(actualImage.length())));
                clearImage();
            } catch (IOException e) {
                showError("Failed to read picture data!");
                e.printStackTrace();
            }
        }
    }

    private void loadNavigationView() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void replaceView() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        hideKeyboardOnClick(this, findViewById(android.R.id.content).getWindowToken());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ads_icon:
                backpressclicked = false;
                /*if (interstitialAd != null && interstitialAd.isAdLoaded())
                    interstitialAd.show();
                else
                    loadINterstatialAd();*/
                showInterstitialAd();


                //showSnackBar("You will see a Advertisement Soon!");
                break;
            case android.R.id.home:

                /*Intent home = new Intent(this, IdSelectionIntermediateScreen.class);
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(home);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);*/
                finish();

                break;

            case R.id.share_icon:
                if (compressedImage != null) {
                    showShareOption();
                } else {
                    showError("Please compress the file first!");
                }
                //shareCompressedImage();

                break;

            case R.id.save_icon:
                saveImageTopHone();
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void saveImageTopHone(){
        if (compressedImage != null) {

            //creating Calendar instance
            Calendar calendar = Calendar.getInstance();
            //Returns current time in millis
            long timeMilli2 = calendar.getTimeInMillis();
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_TITLE, timeMilli2+".jpg");

            startActivityForResult(intent, CREATE_FILE);
        } else {
            showError("Please compress the file first!");
        }
    }
    public static String savePdfToPhoneFilePath ;

    private void savePdfTopHone(){
        if (compressedImage != null) {
            try {
                ArrayList<String> fileList = new ArrayList<>();
                fileList.add(compressedImage.getAbsolutePath());

                createPdfAndPreview.createPdf(getFilesDir() + "/PdfFiles/", 3, fileList, "Resized_file", false);

                //creating Calendar instance
                Calendar calendar = Calendar.getInstance();
                //Returns current time in millis
                long timeMilli2 = calendar.getTimeInMillis();
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_TITLE, timeMilli2+".pdf");

                startActivityForResult(intent, CREATE_PDF_FILE);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        } else {
            showError("Please compress the file first!");
        }
    }
    private void createDefaultDir() {
        // External sdcard location
        File mediaStorageDir = new File(getFilesDir(), "/PdfConverter");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MainActivity", "Oops! Failed create  directory");
            }
        }
        // create pdfFiles folder
        File docsFolder = new File(getFilesDir() + "/pdfFiles");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i("", "Created a new directory for PDF");
        }

        // create pdfFiles folder
       /* File sigFolder = new File(Environment.getExternalStorageDirectory() + "/PdfConverter/Signatures");
        if (!sigFolder.exists()) {
            sigFolder.mkdir();
            Log.i("", "Created a new directory for sigFolder");
        }*/


    }

    private void showShareOption() {

        createDefaultDir();

        new MaterialDialog.Builder(this)
                .title(R.string.share)
                .items(R.array.share_items)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if(text.equals("Save PDF to Phone")){
                            savePdfTopHone();
                        }
                        else if (text.equals("Save Image to Phone")) {
                            saveImageTopHone();
                        }
                       else if (text.equals("Share Image (JPG)")) {
                            shareCompressedImage();

                        } else if (text.equals("Share Image (Zip)")) {

                            createZipFile.zipFileAtPath(compressedImage.getAbsolutePath(),
                                    getFilesDir() + "/pdfFiles/Resized_file.zip");

                        } else if (text.equals("Share PDF file")) {
                            ArrayList<String> fileList = new ArrayList<>();
                            fileList.add(compressedImage.getAbsolutePath());

                            try {
                                createPdfAndPreview.createPdf(getFilesDir() + "/pdfFiles/", 1, fileList, "Resized_file", true);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                })
                .show();

    /*} else

    {
        new MaterialDialog.Builder(this)
                .content("No Image Found to Share!")
                // .content("Your Phone doesn't have any inbuilt pdf opener app, Please download one from Google Play Store.")
                .positiveText(R.string.OK)
                // .negativeText(R.string.OK)
                .cancelable(false)
                .iconRes(R.drawable.ic_dialog_alert)
                // .neutralColorRes(R.color.Salmon_fourth_addon_bottom)
                // .negativeColorRes(R.color.Salmon_fourth_addon_bottom)
                .positiveColorRes(R.color.ytl_theme_blue_color)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {

                        dialog.cancel();

                    }
                }).show();
    }*/

    }

    @Override
    public void onResume() {
        super.onResume();
        //validateUsbPluggedInOrNot();
        IronSource.onResume(this);
    }

    private void showBanner() {
        try {

            if (BuildConfig.DEBUG) {
                IntegrationHelper.validateIntegration(this);
                IronSource.setAdaptersDebug(true);
            }

            // 用户选择同意遵守CDPR AND CPPA
            IronSource.setConsent(true);
            IronSource.setMetaData("do_not_sell", "true");

            Bundle extras = new Bundle();
            extras.putString("npa", "1");

            /*AdRequest.Builder builder = new AdRequest.Builder();
            builder.tagForChildDirectedTreatment(true);
            builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
*/

            final FrameLayout bannerContainer = findViewById(R.id.bannerContainer);
            banner = IronSource.createBanner(this, ISBannerSize.BANNER);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT);
            bannerContainer.addView(banner, 0, layoutParams);

            banner.setBannerListener(new BannerListener() {
                @Override
                public void onBannerAdLoaded() {
                    // Called after a banner ad has been successfully loaded
                }

                @Override
                public void onBannerAdLoadFailed(IronSourceError error) {
                    // Called after a banner has attempted to load an ad but failed.
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bannerContainer.removeAllViews();
                        }
                    });
                }

                @Override
                public void onBannerAdClicked() {
                    // Called after a banner has been clicked.
                }

                @Override
                public void onBannerAdScreenPresented() {
                    // Called when a banner is about to present a full screen content.
                }

                @Override
                public void onBannerAdScreenDismissed() {
                    // Called after a full screen content has been dismissed
                }

                @Override
                public void onBannerAdLeftApplication() {
                    // Called when a user would be taken out of the application context.
                }
            });

            IronSource.loadBanner(banner, interBannerPlacement);
            IntegrationHelper.validateIntegration(activity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAppCLoseDialog(final Activity activity) {
        try {

            SharedPreferences sharedpreferences = activity.getSharedPreferences(myPreference, activity.MODE_PRIVATE);


            AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity, R.style.AlertDialogTheme);
            View mView = activity.getLayoutInflater().inflate(R.layout.rate_us_five_star_layout, null);

            CheckBox rateUsBtn = mView.findViewById(R.id.do_not_ask_again_cb);

            rateUsBtn.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // write here your code for example ...
                if (isChecked) {
                    isCheckedCb = true;
                    SharedPreferences.Editor editor = activity.getSharedPreferences(myPreference, activity.MODE_PRIVATE).edit();
                    editor.putBoolean(donotshowAgain, true);
                    editor.apply();
                } else {
                    isCheckedCb = false;
                    SharedPreferences.Editor editor = activity.getSharedPreferences(myPreference, activity.MODE_PRIVATE).edit();
                    editor.putBoolean(donotshowAgain, false);
                    editor.apply();
                }
            });

            mBuilder.setPositiveButton("Rate Us!", (dialogInterface, i) -> {
                if (isCheckedCb) {
                    SharedPreferences.Editor editor = activity.getSharedPreferences(donotshowAgain, activity.MODE_PRIVATE).edit();
                    editor.putBoolean(donotshowAgain, true);
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = activity.getSharedPreferences(donotshowAgain, activity.MODE_PRIVATE).edit();
                    editor.putBoolean(donotshowAgain, false);
                    editor.apply();
                }

                FragmentManager fm = getSupportFragmentManager();
                AppRateDialog alertDialog = AppRateDialog.newInstance("rateApp");
                alertDialog.show(fm, "fragment_alert");


                dialogInterface.dismiss();
            });

            mBuilder.setNegativeButton("Close", (dialogInterface, i) -> {
                if (isCheckedCb) {
                    SharedPreferences.Editor editor = activity.getSharedPreferences(donotshowAgain, activity.MODE_PRIVATE).edit();
                    editor.putBoolean(donotshowAgain, true);
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = activity.getSharedPreferences(donotshowAgain, activity.MODE_PRIVATE).edit();
                    editor.putBoolean(donotshowAgain, false);
                    editor.apply();
                }

                dialogInterface.dismiss();
                activity.finish();
            });

            mBuilder.setNeutralButton("Dismiss", (dialogInterface, i) -> {
                if (isCheckedCb) {
                    SharedPreferences.Editor editor = activity.getSharedPreferences(donotshowAgain, activity.MODE_PRIVATE).edit();
                    editor.putBoolean(donotshowAgain, true);
                    editor.apply();
                } else {
                    SharedPreferences.Editor editor = activity.getSharedPreferences(donotshowAgain, activity.MODE_PRIVATE).edit();
                    editor.putBoolean(donotshowAgain, false);
                    editor.apply();
                }
                dialogInterface.dismiss();
            });

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // showAppCloseDialog();
        // backpressclicked = true;


        sharedpreferences = getSharedPreferences(myPreference, this.MODE_PRIVATE);
        doNotShowAgain = sharedpreferences.getBoolean(donotshowAgain, false);


        if (!doNotShowAgain && secDiff % 2 == 0) {
            showAppCLoseDialog(MainActivity.this);
            /*FragmentManager fm = getSupportFragmentManager();
            AppRateDialog alertDialog = AppRateDialog.newInstance("rateApp");
            alertDialog.show(fm, "fragment_alert");*/

        } else {

            FragmentManager fm = getSupportFragmentManager();
            AppCloseDialogFragment alertDialog = AppCloseDialogFragment.newInstance("CloseApp");
            alertDialog.show(fm, "fragment_alert");

        }
        secDiff++;

        //finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        //validateUsbPluggedInOrNot();
        IronSource.destroyBanner(banner);
    }

    public void validateUsbPluggedInOrNot() {
        if (!BuildConfig.DEBUG) {
            if (isConnected(this)) {
                new MaterialDialog.Builder(this)
                        .title(R.string.security_alert)
                        .icon(getResources().getDrawable(R.drawable.ic_dialog_alert))
                        .content(R.string.security_reason_usb_cable_plugged_in)
                        .positiveText(R.string.OK)
                        // .negativeText(R.string.OK)
                        .cancelable(false)
                        .iconRes(R.drawable.ic_dialog_alert)
                        // .neutralColorRes(R.color.Salmon_fourth_addon_bottom)
                        // .negativeColorRes(R.color.Salmon_fourth_addon_bottom)
                        .positiveColorRes(R.color.ytl_theme_blue_color)
                        .onPositive((dialog, which) -> {

                            finish();
                        }).show();
            }
        }
    }

    private void showAppCloseDialog() {
        new MaterialDialog.Builder(this)
                .content("Do you really want to exit App!")
                .positiveText(R.string.OK)
                .negativeText(R.string.CANCEL)
                .cancelable(true)
                .iconRes(R.drawable.ic_dialog_alert)
                // .neutralColorRes(R.color.Salmon_fourth_addon_bottom)
                .onPositive((dialog, which) -> {
                    dialog.cancel();
                    finish();
                })

                .onNegative((dialog, which) -> dialog.cancel()).show();
    }


    private void requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            Snackbar.make(coordinatiorLayout, R.string.permission_must_required_msg,
                    Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }).show();

        } else {
            Snackbar.make(coordinatiorLayout, "camera un-available", Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }


    private void requestReadStoragePermission() {
        // Permission has not been granted and must be requested.
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with cda button to request the missing permission.
            Snackbar.make(coordinatiorLayout, R.string.storage_permission_must_required_msg,
                    Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_STORAGE);
                }
            }).show();

        } else {
            Snackbar.make(coordinatiorLayout, "Read Storage Permission Unavailable", Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_STORAGE);
        }
    }

    private void setLayout() {


        seekBarCompress = findViewById(R.id.seekbar_compression_value);
        qualityValueTv = findViewById(R.id.qualityValueTv);
        qualityValueTv.setText(quality + " %");
        seekBarCompress.setMax(100);
        seekBarCompress.setProgress(quality);
        seekBarCompress.setOnSeekBarChangeListener(this);

        saveCompressedImage = findViewById(R.id.saveCompressedImage);
        compressImage = findViewById(R.id.compressImage);
        compressImage.setOnClickListener(view -> compressImage());


        saveCompressedImage.setOnClickListener(view -> {
            if (compressedImage != null) {
                showShareOption();
            } else {
                showError("Please compress the file first!");
            }
            //  shareCompressedImage();
            showInterstitialAd();
        });
        actualImageView.setOnClickListener(view -> {
            captureImage();
        });

        compressedImageView.setOnClickListener(view -> {
            status = permissionUtilityClass.check_permission(permissionsArraylist, getResources().getString(R.string.permission_must_required_msg), 1);
            if (status) {
                if (compressedImage != null) {

                    Uri photoURI = FileProvider.getUriForFile(context, getString(R.string.content_provide_path), compressedImage);

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(photoURI, "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    startActivity(intent);
                }
            }
        });
    }

    private void shareCompressedImage() {
        //  showInterstitialAd();
        if (compressedImage != null) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            //sendIntent.setType("text/plain");
            //sendIntent.setType("image/*");
            sendIntent.setType("image/*");
            sendIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, getString(R.string.content_provide_path), compressedImage));
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        } else {
            showError("Please compress the file first!");
        }
    }

    public void chooseImage(View view) {captureImage();
    }

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public boolean validateEmptyInput(EditText editText, TextInputLayout textInputLayout, String errorMsg) {

        if (editText.getText().toString().trim().isEmpty()) {
            textInputLayout.setError(errorMsg);
            requestFocus(editText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }



    private int getRandomColor() {
        Random rand = new Random();
        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }

    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        int value = (int) (size / Math.pow(1024, digitGroups));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public int getFileSize(long size) {
        if (size <= 0) {
            return 0;
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        int value = (int) (size / Math.pow(1024, digitGroups));
        //return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        return value;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if (seekBar.getId() == R.id.seekbar_compression_value) {

            quality = progress;
            if (quality == 100) {

            }
            qualityValueTv.setText(quality + " %");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        // redirects to utils
        permissionUtilityClass.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            IronSource.destroyBanner(banner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION", "GRANTED");
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY", "GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION", "DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION", "NEVER ASK AGAIN");
    }

    private void captureImage() {
// BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission is already available, start camera preview
                Snackbar.make(coordinatiorLayout,
                        "Camera Permission Available",
                        Snackbar.LENGTH_SHORT).show();
                startActivityForResult(chooseIntent.getPickImageIntent(), 22);
            } else {
                // Permission is missing and must be requested.
                requestCameraPermission();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean status;
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_extract_any_to_pdf) {
            permissionsArraylist = new ArrayList<>();
            permissionsArraylist.add(WRITE_STORAGE_PERMISSION);
            status = permissionUtilityClass.check_permission(permissionsArraylist, getResources().getString(R.string.storage_permission_must_required_msg), 1);
            if (status) {
              /*  createDir(Environment.getExternalStorageDirectory().getPath() + "/PdfFiles");


                Intent intent = new Intent(getApplicationContext(), ScrollableTabsActivity.class);
                intent.putExtra("isFromDocumentActivity", "no");
                intent.putExtra("imageListSize", 0);
                startActivityForResult(intent, PICK_PDF);*/


            }
        }


        if (id == R.id.nav_crop) {
            openImageCropIntent();
        }
        // Handle the camera action
        else if (id == R.id.nav_camera) {
            openCamera();
        } else if (id == R.id.nav_privacy_policy) {
            Intent i = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (id == R.id.nav_extract_pdf) {
            onEventCapture("nav_extract_pdf Main");

            //openPdfSearchViewer();

        } else if (id == R.id.nav_sign_doc) {
            onEventCapture("nav_sign doc Main");
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=org.docsign.digitalSignature")));

        } else if (id == R.id.nav_gallery) {
            openGallery();
        } else if (id == R.id.nav_settings) {
            onEventCapture("nav_settings Main");

            Intent commonActivity = new Intent(getApplicationContext(), CommonBaseActivity.class);
            commonActivity.putExtra("flowType", CommonBaseActivity.SETTINGS_NAV);
            startActivity(commonActivity);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }
        /* else if (id == R.id.nav_manage) {


            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() +  File.separator + "pdfFiles" );
            startActivity(Intent.createChooser(intent, "Open folder"));

        } */

        else if (id == R.id.nav_ocr_scanner) {
            onEventCapture("nav_ocr_scanner Main");

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=org.textScanner.ImageToTextOcr")));

        } else if (id == R.id.nav_qr_scanner) {
            onEventCapture("nav_qr_scanner Main");

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=org.androidluckyguys.barcodescanner")));

        } else if (id == R.id.nav_rate_us) {
            onEventCapture("nav_rate_us Main");
            //showAppCLoseDialog(MainActivity.this);
            FragmentManager fm = getSupportFragmentManager();
            AppRateDialog alertDialog = AppRateDialog.newInstance("rateApp");
            alertDialog.show(fm, "fragment_alert");


           /* Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
            }*/
        } else if (id == R.id.nav_share) {
            onEventCapture("nav_share Main");

            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, "Image Compressor Lite");
            share.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + context.getPackageName());

            startActivity(Intent.createChooser(share, "Share link!"));

        } else if (id == R.id.nav_qr_code) {
            /*Intent commonActivity = new Intent(getApplicationContext(),CommonBaseActivity.class);
            commonActivity.putExtra("flowType",CommonBaseActivity.QR_CODE_NAV);
            startActivity(commonActivity);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/


        } else if (id == R.id.nav_send) {


            Intent commonActivity = new Intent(getApplicationContext(), CommonBaseActivity.class);
            commonActivity.putExtra("flowType", CommonBaseActivity.FEEDBACK_NAV);
            startActivity(commonActivity);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onEventCapture(String eventName) {

        try {
            Bundle par = new Bundle();
            par.putString(eventName, eventName);
            mFirebaseAnalytics.logEvent(eventName, par);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void openImageCropIntent() {
        onEventCapture("nav_Crop");

            Intent startCropActivity = new Intent(this, imagepicker.MainActivity.class);
            startActivityForResult(startCropActivity, CROP_IMAGE);
    }

    private void openGallery() {
        onEventCapture("nav_gallery Main");

        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
*/
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
       /* } else {
            // Permission is missing and must be requested.
            requestReadStoragePermission();
        }*/


    }

    private void openCamera() {
        onEventCapture("nav_camera Main");

          captureImage();

    }


    private void moveToPhoneImagePath() {
        if (compressedImage != null) {
            /*Toast.makeText(context, "Choose Folder to save file", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(Intent.createChooser(i, "Choose Folder to save file"), REQUEST_CODE_DIR_FULL_LIST_SELECTOR);
*/
            ArrayList<String> fileList = new ArrayList<>();
            fileList.add(compressedImage.getAbsolutePath());

            try {
                createPdfAndPreview.createPdf(getFilesDir() + "/pdfFiles/", 2, fileList, "Resized_File", true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showError("Please compress the file first!");
        }
    }

    public void showSnackBar(String msg) {
        if (coordinatiorLayout != null) {
            Snackbar snackbar = Snackbar.make(coordinatiorLayout, msg, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void loadINterstatialAd() {
        try {
            IronSource.loadInterstitial();
            IronSource.setInterstitialListener(new MyInterAdsListener());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showInterstitialAd() {
        if (!BuildConfig.DEBUG){
            try {
          /*  utils.updateNewTimeOnAdsShowClick();
            secDiff = utils.getTimeDifference(newClickTime, startDateTime, Calendar.SECOND);
            if (secDiff >= 20) {*/

                if (IronSource.isInterstitialReady()) {
                    IronSource.showInterstitial(interAdsPlacement);
                } else {
                    FileUtil.updateNewStartTime();
                    loadINterstatialAd();
                }
                // }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    }

    private void showAlertDialogAndExitApp(String message) {


        new MaterialDialog.Builder(context)
                .content(R.string.rooted_device_security_check)
                .title(R.string.rooted_device)
                .positiveText(R.string.close_app)
                //.negativeText(R.string.CANCEL)
                .cancelable(false)
                .iconRes(R.drawable.ic_dialog_alert)
                .positiveColorRes(R.color.rose)
                .onPositive((dialog, which) -> {
                    finish();
                })

                .onNegative((dialog, which) -> dialog.cancel())
                .show();


    }

    class MyInterAdsListener implements InterstitialListener {

        @Override
        public void onInterstitialAdReady() {
            Log.i(TAG, "onInterstitialAdReady: ");
        }

        @Override
        public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
            Log.i(TAG, "onInterstitialAdLoadFailed: " + ironSourceError.getErrorMessage());
        }

        @Override
        public void onInterstitialAdOpened() {
            Log.i(TAG, "onInterstitialAdOpened: ");
        }

        @Override
        public void onInterstitialAdClosed() {
            Log.i(TAG, "onInterstitialAdClosed: ");
        }

        @Override
        public void onInterstitialAdShowSucceeded() {
            Log.i(TAG, "onInterstitialAdShowSucceeded: ");
        }

        @Override
        public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {
            Log.i(TAG, "onInterstitialAdShowFailed: " + ironSourceError.getErrorMessage());
        }

        @Override
        public void onInterstitialAdClicked() {
            Log.i(TAG, "onInterstitialAdClicked: ");

        }
    }
}
