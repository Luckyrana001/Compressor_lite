package id.zelory.compressor.sample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import org.compressor.imagecompress.R;

/* renamed from: id.zelory.compressor.sample.PrivacyPolicyActivity */
public class PrivacyPolicyActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    WebView webview;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.privacy_policy_layout);
        this.sharedpreferences = getSharedPreferences("My_Pref", 0);
        init();
    }

    public void AcceptPrivacyPolicy(View v) {
        Editor editor = getSharedPreferences("My_Pref", 0).edit();
        editor.putBoolean("privacy_policy", true);
        editor.apply();
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void init() {
        this.webview = (WebView) findViewById(R.id.webview);
        this.webview.loadUrl("file:///android_asset/privacy_policy.html");
        this.webview.requestFocus();
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Loading");
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();
        this.webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                try {
                    PrivacyPolicyActivity.this.progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
