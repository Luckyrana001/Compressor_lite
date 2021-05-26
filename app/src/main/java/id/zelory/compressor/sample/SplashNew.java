package id.zelory.compressor.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.compressor.imagecompress.R;

import java.util.Calendar;
import java.util.Date;

/* renamed from: id.zelory.compressor.sample.SplashNew */
public class SplashNew extends AppCompatActivity {
    public static final String pref = "My_Pref";
    public static final String privacy_policy = "privacy_policy";
    private static int SPLASH_TIME_OUT = 400;
    private final String TAG = SplashNew.class.getSimpleName();
    public boolean privcayPolicyAccepted = false;
    TextView dateTimeTodayTv;
    boolean mainActivityStarted = false;
    TextView monthTv;
    /* renamed from: pb */
    ProgressBar f16pb;
    RelativeLayout rectangleAdContainer;
    SharedPreferences sharedpreferences;
    TextView skipTv;
    private Handler handler = new Handler();
    private int progressStatus = 0;


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

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.splash_screen_layout);
        this.sharedpreferences = getSharedPreferences("My_Pref", 0);
        this.privcayPolicyAccepted = this.sharedpreferences.getBoolean("privacy_policy", false);
        SPLASH_TIME_OUT = 10000;
        this.dateTimeTodayTv = (TextView) findViewById(R.id.dateTimeTodayTv);
        this.monthTv = (TextView) findViewById(R.id.monthTv);
        this.skipTv = (TextView) findViewById(R.id.skipTv);
        this.skipTv.setOnClickListener(new OnClickListener() {
            public final void onClick(View view) {
                SplashNew.this.lambda$onCreate$0$SplashNew(view);
            }
        });
        Date currentTime = Calendar.getInstance().getTime();
        this.dateTimeTodayTv.setText(FileUtil.splitDateTime(currentTime.toString()));
        this.monthTv.setText(getMonth(currentTime.toString()));
        this.f16pb = (ProgressBar) findViewById(R.id.pb);
        showProgressbar();
    }

    public /* synthetic */ void lambda$onCreate$0$SplashNew(View view) {
        goToMainPage();
    }

    private void showProgressbar() {
        new Thread(new Runnable() {
            public final void run() {
                SplashNew.this.lambda$showProgressbar$2$SplashNew();
            }
        }).start();
    }

    public /* synthetic */ void lambda$showProgressbar$2$SplashNew() {
        while (true) {
            int i = this.progressStatus;
            if (i < 100) {
                this.progressStatus = i + 1;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.handler.post(new Runnable() {
                    public final void run() {
                        SplashNew.this.lambda$null$1$SplashNew();
                    }
                });
            } else {
                return;
            }
        }
    }

    public /* synthetic */ void lambda$null$1$SplashNew() {
        this.f16pb.setProgress(this.progressStatus);
        if (this.progressStatus == 100) {
            goToMainPage();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void goToMainPage() {
        if (!this.mainActivityStarted) {
            if (this.privcayPolicyAccepted) {
                Intent i = new Intent(this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(i);
            } else {
                Intent i2 = new Intent(this, PrivacyPolicyActivity.class);
                i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(i2);
            }
            finish();
        }
    }

    public void onDestroy() {

        super.onDestroy();
    }
}
