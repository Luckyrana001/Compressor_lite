package id.zelory.compressor.sample.update;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.compressor.imagecompress.R;

import id.zelory.compressor.BuildConfig;
import id.zelory.compressor.sample.MainActivity;


public class CheckInAppUpdateActivity extends AppCompatActivity {

    // Declare the UpdateManager
    UpdateManager mUpdateManager;
    Button skipButton, updateInBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_in_app_update_layout);
        skipButton = findViewById(R.id.skipButton);
        updateInBackground = findViewById(R.id.updateInBackground);
        Intent intent = getIntent();
        boolean isDiffernceGreaterThantwo = intent.getBooleanExtra("isDifferenceMoreThanOne", false);
        if (isDiffernceGreaterThantwo) {
            skipButton.setVisibility(View.INVISIBLE);
            updateInBackground.setVisibility(View.INVISIBLE);
        } else {
            skipButton.setVisibility(View.VISIBLE);
            updateInBackground.setVisibility(View.VISIBLE);

        }

        TextView txtCurrentVersion = findViewById(R.id.txt_current_version);
        TextView txtAvailableVersion = findViewById(R.id.txt_available_version);

        txtCurrentVersion.setText(String.valueOf(BuildConfig.VERSION_CODE));

        // Initialize the Update Manager with the Activity and the Update Mode
        mUpdateManager = UpdateManager.Builder(this);

        // Callback from Available version code
        mUpdateManager.getAvailableVersionCode(new UpdateManager.onVersionCheckListener() {
            @Override
            public void onReceiveVersionCode(final int code) {
                txtAvailableVersion.setText(String.valueOf(code));
            }
        });


/*
        // Callback from Available version code
        mUpdateManager.getAvailableVersionCode(new UpdateManager.onVersionCheckListener() {
            @Override
            public void onReceiveVersionCode(final int code) {
                txtAvailableVersion.setText(String.valueOf(code));
            }
        });*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Continue updates when resumed
        mUpdateManager.continueUpdate();
    }


    public void callFlexibleUpdate(View view) {
        // Start a Flexible Update
        mUpdateManager.mode(UpdateManagerConstant.FLEXIBLE).start();
        Button updateInBackground = findViewById(R.id.updateInBackground);
        updateInBackground.setEnabled(false);

    }

    public void callImmediateUpdate(View view) {
        // Start a Immediate Update
        mUpdateManager.mode(UpdateManagerConstant.IMMEDIATE).start();
        Button immediateUpdate = findViewById(R.id.immediateUpdate);
        immediateUpdate.setEnabled(false);

    }


    public void updateFromGooglePlayStore(View view) {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void callCancelUpdate(View view) {
        // Start a Immediate Update
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }


}
