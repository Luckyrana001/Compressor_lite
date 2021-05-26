package id.zelory.compressor.sample.common;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import org.compressor.imagecompress.BuildConfig;
import org.compressor.imagecompress.R;

import id.zelory.compressor.sample.listeners.IResponseReceivedNotifyInterface;
import id.zelory.compressor.sample.listeners.ResponseArgs;

/* renamed from: id.zelory.compressor.sample.common.FeedbackFragment */
public class FeedbackFragment extends BaseFragment implements IResponseReceivedNotifyInterface {
    Float selectedRating;
    int version_code;
    String version_name;
    private EditText feedbackEt;
    private RatingBar ratingBar;
    private EditText subjectEt;

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (!capitalizeNext || !Character.isLetter(c)) {
                if (Character.isWhitespace(c)) {
                    capitalizeNext = true;
                }
                phrase.append(c);
            } else {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
            }
        }
        return phrase.toString();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feedback_layout, container, false);
    }

    public void setTAG(String TAG) {
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout(view);
        this.version_name = BuildConfig.VERSION_NAME;
        this.version_code = 10;
        EditText editText = this.subjectEt;
        StringBuilder sb = new StringBuilder();
        sb.append("Feedback for Image Compressor Lite (");
        sb.append(this.version_name);
        sb.append(")");
        editText.setText(sb.toString());
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        int version = VERSION.SDK_INT;
        String versionRelease = VERSION.RELEASE;
        StringBuilder sb = new StringBuilder();
        String str = " Manufacture ";
        sb.append(str);
        sb.append(capitalize(manufacturer));
        String str2 = " \n Model ";
        sb.append(str2);
        sb.append(model);
        String str3 = " \n Version ";
        sb.append(str3);
        sb.append(version);
        String str4 = " \n VersionRelease ";
        sb.append(str4);
        sb.append(versionRelease);
        String str5 = " \n App Version Name ";
        sb.append(str5);
        sb.append(this.version_name);
        String str6 = " \n App Version Code ";
        sb.append(str6);
        sb.append(this.version_code);
        String str7 = " \n\n\n Please enter your feedback here...";
        sb.append(str7);
        Log.e("  MyActivity", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(capitalize(manufacturer));
        sb2.append(str2);
        sb2.append(model);
        sb2.append(str3);
        sb2.append(version);
        sb2.append(str4);
        sb2.append(versionRelease);
        sb2.append(str5);
        sb2.append(this.version_name);
        sb2.append(str6);
        sb2.append(this.version_code);
        sb2.append(str7);
        return sb2.toString();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.send && validateUserName()) {
            updateRating();
        }
        return super.onOptionsItemSelected(item);
    }

    /* access modifiers changed from: protected */
    public void setupActionbar() {
        super.setupActionbar();
        getBaseActivity().getmActionBar().show();
        getBaseActivity().getmActionBar().setTitle((CharSequence) getResources().getString(R.string.feedback_title));
        getBaseActivity().getmActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menu.findItem(R.id.send) != null) {
            menu.findItem(R.id.send).setVisible(true);
        }
        if (menu.findItem(R.id.reset) != null) {
            menu.findItem(R.id.reset).setVisible(false);
        }
    }

    private void initLayout(View view) {
        this.subjectEt = (EditText) view.findViewById(R.id.subjectEt);
        this.feedbackEt = (EditText) view.findViewById(R.id.feedbackEt);
        this.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        this.ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public final void onRatingChanged(RatingBar ratingBar, float f, boolean z) {
                FeedbackFragment.this.lambda$initLayout$0$FeedbackFragment(ratingBar, f, z);
            }
        });
    }

    public /* synthetic */ void lambda$initLayout$0$FeedbackFragment(RatingBar ratingBar2, float rating, boolean fromUser) {
        this.selectedRating = Float.valueOf(rating);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(5);
        }
    }

    public boolean validateUserName() {
        hideKeyboard();
        if (!this.feedbackEt.getText().toString().trim().equals("")) {
            return true;
        }
        showToast(getActivity().getResources().getString(R.string.feedback_field_blank));
        requestFocus(this.feedbackEt);
        return false;
    }

    public void onClick(View v) {
        v.getId();
    }

    public void updateRating() {
        if (Utils.isNetworkConnected(getActivity(), true, R.style.AppCompatAlertDialogStyle)) {
            Intent intent = new Intent("android.intent.action.SENDTO");
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra("android.intent.extra.EMAIL", new String[]{"a1digitalmediatechnology@gmail.com"});
            intent.putExtra("android.intent.extra.SUBJECT", this.subjectEt.getText().toString().trim());
            intent.putExtra("android.intent.extra.TEXT", this.feedbackEt.getText().toString().trim());
            startActivity(Intent.createChooser(intent, "Email via..."));
            return;
        }
        Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_connection), 1).show();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
    }

    public void responseReceived(ResponseArgs responseArgs) {
        progressDialog.dismiss();
    }
}
