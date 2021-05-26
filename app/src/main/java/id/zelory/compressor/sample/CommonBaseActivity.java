package id.zelory.compressor.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.compressor.imagecompress.BuildConfig;
import org.compressor.imagecompress.R;

import id.zelory.compressor.sample.common.BaseActivity;
import id.zelory.compressor.sample.common.BaseFlyContext;
import id.zelory.compressor.sample.common.FeedbackFragment;

public class CommonBaseActivity extends BaseActivity {
    public static final int COMPRESS_NAV = 6;
    public static final int FEEDBACK_NAV = 1;
    public static final int OCR_NAV = 7;
    public static final int PASSCODE_NAV = 4;
    public static final int QR_CODE_NAV = 3;
    public static final int ROTATE_NAV = 5;
    public static final int SETTINGS_NAV = 2;
    private static FirebaseAnalytics mFirebaseAnalytics;
    String eventCategory;
    String imagePath;
    private int flowStatus;

    public void onEventCapture(String eventName) {
        try {
            Bundle par = new Bundle();
            par.putString(eventName, eventName);
            mFirebaseAnalytics.logEvent(eventName, par);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView((int) R.layout.common_base_activity_layout);



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        String str = "PDF Converter Main Page Opened";
        mFirebaseAnalytics.setUserProperty("MainActivityonCreate", str);
        mFirebaseAnalytics.setCurrentScreen(this, str, null);
        StrictMode.setVmPolicy(new Builder().build());
        this.mControlsView = findViewById(R.id.fullscreen_content_controls);
        Intent intent = getIntent();
        if (intent != null) {
            this.flowStatus = intent.getIntExtra("flowType", 0);
            this.eventCategory = intent.getStringExtra("eventCategory");
            this.imagePath = intent.getStringExtra("imagePath");
        }
        hideSoftKeyboard();
        coordinatiorLayout = (CoordinatorLayout) findViewById(R.id.coordinatiorLayout);
        BaseFlyContext.getInstant().setActivity(this);
        if (this.flowStatus != 1) {
            onBackPressed();
        } else {
            replaceView(R.id.fullscreen_content_controls, new FeedbackFragment(), true, true);
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_activity_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("WrongConstant")
    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        //validateUsbPluggedInOrNot();
    }
    public void onResume() {
        super.onResume();
        //validateUsbPluggedInOrNot();
    }
    public static boolean isConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter("android.hardware.usb.action.USB_STATE"));
        return intent.getExtras().getBoolean("connected");
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
    public void replaceView() {
    }

    public void onClick(View view) {
    }

    public void onFragmentInteraction(Uri uri) {
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* access modifiers changed from: protected */
    public void showAlertMissingInfo(String message) {
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void reLounchApp(String errorMsg) {
    }
}
