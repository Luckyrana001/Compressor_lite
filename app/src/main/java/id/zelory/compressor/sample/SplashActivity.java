package id.zelory.compressor.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.compressor.imagecompress.BuildConfig;
import org.compressor.imagecompress.R;

import java.util.Calendar;
import java.util.Date;

import id.zelory.compressor.sample.common.BaseActivity;
import id.zelory.compressor.sample.update.CheckInAppUpdateActivity;
import id.zelory.compressor.sample.update.UpdateManager;

import static id.zelory.compressor.sample.FileUtil.splitDateTime;


public class SplashActivity extends BaseActivity {
    public static final String pref = "My_Pref";
    public static final String privacy_policy = "privacy_policy";
    public boolean privcayPolicyAccepted = false;
    boolean mainActivityStarted = false;
    TextView dateTimeTodayTv, skipTv, monthTv, appVersionTv;
    RelativeLayout rectangleAdContainer;
    SharedPreferences sharedpreferences;
    ProgressBar pb;
    boolean showAds;

    int availableVersionCode = 0;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    // Declare the UpdateManager
    UpdateManager mUpdateManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        //AudienceNetworkAds.buildInitSettings(this).initialize();
        appVersionTv = findViewById(R.id.appVersionTv);

        appVersionTv.setText("v" + BuildConfig.VERSION_NAME);
        // Initialize the Update Manager with the Activity and the Update Mode
        // Initialize the Update Manager with the Activity and the Update Mode
        mUpdateManager = UpdateManager.Builder(this);

        // Callback from Available version code
        mUpdateManager.getAvailableVersionCode(code -> availableVersionCode = code);

        // Callback from Available version code
        //mUpdateManager.getAvailableVersionCode(code -> availableVersionCode = code);
        sharedpreferences = getSharedPreferences(pref, this.MODE_PRIVATE);
        privcayPolicyAccepted = sharedpreferences.getBoolean(privacy_policy, false);
        // showAds = sharedpreferences.getBoolean(SHOW_ADS_PREF, true);

       /* if (MainActivity.packageName.equals(getPackageName())) {
            SPLASH_TIME_OUT = 300;
        } else {
            if (BuildConfig.DEBUG) {
                SPLASH_TIME_OUT = 500;
            } else {*/
        //SPLASH_TIME_OUT = 2000;
        //  }
        // }
        dateTimeTodayTv = findViewById(R.id.dateTimeTodayTv);
        monthTv = findViewById(R.id.monthTv);
        skipTv = findViewById(R.id.skipTv);
        skipTv.setOnClickListener(view -> goToMainPage());


        Date currentTime = Calendar.getInstance().getTime();
        dateTimeTodayTv.setText(splitDateTime(currentTime.toString()));
        monthTv.setText(getMonth(currentTime.toString()));

        rectangleAdContainer = findViewById(R.id.rectangleAdContainer);
        pb = findViewById(R.id.pb);

        /*sharedpreferences = getSharedPreferences(pref, this.MODE_PRIVATE);
        privcayPolicyAccepted = sharedpreferences.getBoolean(privacy_policy, false);
        showAds = sharedpreferences.getBoolean(SHOW_ADS_PREF, true);
        if (showAds) {
            replaceView(R.id.rectangleAdContainer, new RectangleFragment(), true, true);
        }*/

       /* new Handler().postDelayed(() -> {
            if (mainActivityStarted == false) {
                if (privcayPolicyAccepted) {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Intent i = new Intent(SplashActivity.this, PrivacyPolicyActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);*/

        /*if (showAds)
            loadINterstatialAd();*/

        showProgressbar();

        /*if (SHOW_ADS) {
            showInterstitialAd();
        }*/


    }


    private void showProgressbar() {
        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;
                try {

                    if (BuildConfig.DEBUG) {
                        Thread.sleep(5);
                    } else {
                        if (showAds)
                            Thread.sleep(20);
                        else
                            Thread.sleep(18);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(() -> {
                    pb.setProgress(progressStatus);
                    if (progressStatus == 100) {
                        goToMainPage();
                    } /*else {
                        if (progressStatus != 101)
                            showInterstitialAd();
                    }*/
                });
            }
        }).start(); // Start the operation

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    @Override
    public void replaceView() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public void goToMainPage() {
        //Log.i("current app version code", String.valueOf(BuildConfig.VERSION_CODE));
        if (availableVersionCode <= BuildConfig.VERSION_CODE) {

            if (mainActivityStarted == false) {
                if (privcayPolicyAccepted) {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Intent i = new Intent(SplashActivity.this, PrivacyPolicyActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                finish();
            }
        } else {
            int diff = (availableVersionCode - BuildConfig.VERSION_CODE);


            Intent i = new Intent(SplashActivity.this, CheckInAppUpdateActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (diff > 1) {
                i.putExtra("isDifferenceMoreThanOne", true);
            } else {
                i.putExtra("isDifferenceMoreThanOne", false);
            }

            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public  String getMonth(String name) {

        String[] arrOfStr = name.split(" ");

        String monthName = arrOfStr[1];

        if (monthName.equalsIgnoreCase("Jan")) {
            monthName = "January";
        } else if (monthName.equalsIgnoreCase("Feb")) {
            monthName = "February";
        } else if (monthName.equalsIgnoreCase("Mar")) {
            monthName = "March";
        } else if (monthName.equalsIgnoreCase("Apr")) {
            monthName = "April";
        } else if (monthName.equalsIgnoreCase("May")) {
            monthName = "May";
        } else if (monthName.equalsIgnoreCase("Jun")) {
            monthName = "June";
        } else if (monthName.equalsIgnoreCase("Jul")) {
            monthName = "July";
        } else if (monthName.equalsIgnoreCase("Aug")) {
            monthName = "August";
        } else if (monthName.equalsIgnoreCase("Sep")) {
            monthName = "September";
        } else if (monthName.equalsIgnoreCase("Oct")) {
            monthName = "October";
        } else if (monthName.equalsIgnoreCase("Nov")) {
            monthName = "November";
        } else if (monthName.equalsIgnoreCase("Dec")) {
            monthName = "December";
        }
        return monthName;

    }

}
