package id.zelory.compressor.sample.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.VmPolicy.Builder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import id.zelory.compressor.sample.listeners.OnFragmentInteractionListener;

/* renamed from: id.zelory.compressor.sample.common.BaseActivity */
public abstract class BaseActivity extends AppCompatActivity implements OnFragmentInteractionListener, OnClickListener {
    public static CoordinatorLayout coordinatiorLayout;
    public static boolean isActive = false;
    public static boolean isConnected = false;
    public View mControlsView;
    protected NetworkChangeReceiver receiver;
    ActionBar mActionBar;
    private Typeface fontAwesome;
    private Typeface newJuneRegular;

    public abstract void replaceView();

    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            Utils.hideKeyboardOnClick(this, findViewById(android.R.id.content).getWindowToken());
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    public void showSnackBar(String msg) {
        Snackbar.make((View) coordinatiorLayout, (CharSequence) msg, 0).show();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.setVmPolicy(new Builder().build());
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        isActive = true;
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        isActive = true;
        NetworkChangeReceiver networkChangeReceiver = this.receiver;
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        isActive = true;
    }

    public void replaceView(int layout, BaseFragment fragment, boolean isAddToBackStack, boolean... isAnim) {
        if (fragment != null) {
            try {
                String fragmentName = fragment.getClass().getSimpleName();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(layout, fragment, fragmentName);
                String simpleName = fragment.getClass().getSimpleName();
                fragmentTransaction.addToBackStack(fragmentName);
                fragmentTransaction.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String message, int lenght) {
        Toast.makeText(getApplicationContext(), message, lenght).show();
    }

    public void popBackStackFromName(String fragment) {
        getSupportFragmentManager().popBackStack(fragment, 1);
    }

    public void setFitSystemLayoutMode(boolean mode) {
        CoordinatorLayout coordinatorLayout = coordinatiorLayout;
        if (coordinatorLayout != null) {
            coordinatorLayout.setFitsSystemWindows(mode);
        }
    }

    public ActionBar getmActionBar() {
        if (this.mActionBar == null) {
            this.mActionBar = getSupportActionBar();
        }
        this.mActionBar.setDisplayHomeAsUpEnabled(true);
        this.mActionBar.show();
        exitFullScreenMode();
        return this.mActionBar;
    }

    public void showActionBar() {
        getmActionBar();
    }

    public void setFullScreenMode() {
        this.mControlsView.setSystemUiVisibility(4871);
    }

    public void exitFullScreenMode() {
    }

    protected void hideKeyboard() {
        if (getCurrentFocus() != null
                && getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus()
                            .getWindowToken(),
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    public Typeface getNewJuneRegular() {
        return this.newJuneRegular;
    }

    public Typeface getFontAwesome() {
        return this.fontAwesome;
    }

    /* access modifiers changed from: protected */
    public void registerNetworkReciever() {
        new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.receiver = new NetworkChangeReceiver();
    }

    /* renamed from: id.zelory.compressor.sample.common.BaseActivity$NetworkChangeReceiver */
    public class NetworkChangeReceiver extends BroadcastReceiver {
        public NetworkChangeReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            isNetworkAvailable(context);
        }

        private boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo state : info) {
                        if (state.getState() == State.CONNECTED) {
                            if (!BaseActivity.isConnected) {
                                Log.v("Connection available", "Now you are connected to Internet!");
                                BaseActivity.isConnected = true;
                            }
                            return true;
                        }
                    }
                }
            }
            Log.v("Connection Lost", "You are not connected to Internet!");
            BaseActivity.isConnected = false;
            return false;
        }
    }
}
