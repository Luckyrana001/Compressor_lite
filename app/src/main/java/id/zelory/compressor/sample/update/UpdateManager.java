package id.zelory.compressor.sample.update;


import android.app.Activity;
import android.content.IntentSender;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import java.lang.ref.WeakReference;

import static id.zelory.compressor.sample.update.UpdateManagerConstant.FLEXIBLE;


public class UpdateManager implements LifecycleObserver {

    private static final String TAG = "InAppUpdateManager";
    private static UpdateManager instance;
    private WeakReference<AppCompatActivity> mActivityWeakReference;
    // Default mode is FLEXIBLE
    private int mode = FLEXIBLE;

    // Creates instance of the manager.
    private AppUpdateManager appUpdateManager;

    // Returns an intent object that you use to check for an update.
    private Task<AppUpdateInfo> appUpdateInfoTask;
    private InstallStateUpdatedListener listener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState installState) {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                Log.d(TAG, "An update has been downloaded");
                popupSnackbarForCompleteUpdate();
                // startSplash();
            }
        }
    };


    private UpdateManager(AppCompatActivity activity) {
        mActivityWeakReference = new WeakReference<>(activity);
        this.appUpdateManager = AppUpdateManagerFactory.create(getActivity());
        this.appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        activity.getLifecycle().addObserver(this);
    }

    public static UpdateManager Builder(AppCompatActivity activity) {
        if (instance == null) {
            instance = new UpdateManager(activity);
        }
        Log.d(TAG, "Instance created");
        return instance;
    }

    public UpdateManager mode(int mode) {
        String strMode = mode == FLEXIBLE ? "FLEXIBLE" : "IMMEDIATE";
        Log.d(TAG, "Set update mode to : " + strMode);
        this.mode = mode;
        return this;
    }

    public void start() {
        if (mode == FLEXIBLE) {
            setUpListener();
        }
        checkUpdate();
    }

    private void checkUpdate() {
        // Checks that the platform will allow the specified type of update.
        Log.d(TAG, "Checking for updates");
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(mode)) {
                    // Request the update.
                    Log.d(TAG, "Update available");
                    startUpdate(appUpdateInfo);
                } else {
                    Log.d(TAG, "No Update available");
                }
            }
        });
    }

//    public static void handleResult(int requestCode, int resultCode){
//        Log.d("LIBRARY_ZMA", "Req code Update : " + requestCode);
//        if (requestCode == UpdateManagerConstant.REQUEST_CODE) {
//            Log.d("LIBRARY_ZMA", "Result code Update : " + resultCode);
//            if (resultCode != RESULT_OK) {
//                Log.d("LIBRARY_ZMA", "Update flow failed! Result code: " + resultCode);
//                // If the update is cancelled or fails,
//                // you can request to start the update again.
//            }
//        }
//    }

    private void startUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            Log.d(TAG, "Starting update");
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    mode,
                    getActivity(),
                    UpdateManagerConstant.REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            Log.d(TAG, "" + e.getMessage());
        }
    }

   /* public void startSplash(){
        Intent i = new Intent(getActivity(), SplashActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getActivity().startActivity(i);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        getActivity().finish();
    }*/

    private void setUpListener() {
        appUpdateManager.registerListener(listener);
    }

    public void continueUpdate() {
        if (instance.mode == FLEXIBLE) {
            continueUpdateForFlexible();
        } else {
            continueUpdateForImmediate();
        }
    }

    private void continueUpdateForFlexible() {
        instance.appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        // If the update is downloaded but not installed,
                        // notify the user to complete the update.
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            Log.d(TAG, "An update has been downloaded");
                            instance.popupSnackbarForCompleteUpdate();
                        }
                    }
                });
    }

    private void continueUpdateForImmediate() {
        instance.appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                    @Override
                    public void onSuccess(AppUpdateInfo appUpdateInfo) {
                        if (appUpdateInfo.updateAvailability()
                                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                            // If an in-app update is already running, resume the update.
                            try {
                                instance.appUpdateManager.startUpdateFlowForResult(
                                        appUpdateInfo,
                                        instance.mode,
                                        getActivity(),
                                        UpdateManagerConstant.REQUEST_CODE);
                            } catch (IntentSender.SendIntentException e) {
                                Log.d(TAG, "" + e.getMessage());
                            }
                        }
                    }
                });
    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        getActivity().getWindow().getDecorView().findViewById(android.R.id.content),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.show();
    }

    public void getAvailableVersionCode(final onVersionCheckListener onVersionCheckListener) {
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    // Request the update.
                    Log.d(TAG, "Update available");
                    int availableVersionCode = appUpdateInfo.availableVersionCode();
                    onVersionCheckListener.onReceiveVersionCode(availableVersionCode);
                } else {
                    Log.d(TAG, "No Update available");
                }
            }
        });
    }

    private Activity getActivity() {
        return mActivityWeakReference.get();
    }

    private void unregisterListener() {
        if (appUpdateManager != null && listener != null) {
            appUpdateManager.unregisterListener(listener);
            Log.d(TAG, "Unregistered the install state listener");
        }
    }

    @OnLifecycleEvent(Event.ON_DESTROY)
    public void onDestroy() {
        unregisterListener();
    }

    public interface onVersionCheckListener {

        void onReceiveVersionCode(int code);
    }
}