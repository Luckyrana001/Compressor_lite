package id.zelory.compressor.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.compressor.imagecompress.R;

public class AppCloseDialogFragment extends DialogFragment  {
    private static final String TAG = AppCloseDialogFragment.class.getSimpleName();
    private static final int COLOR_LIGHT_GRAY = 0xff90949c;
    private static final int COLOR_DARK_GRAY = 0xff4e5665;
    private static final int COLOR_CTA_BLUE_BG = 0xff4080ff;
    private static final int MIN_HEIGHT_DP = 200;
    private static final int MAX_HEIGHT_DP = 320;
    private static final int DEFAULT_HEIGHT_DP = 320;
    private static final int DEFAULT_PROGRESS_DP = 90;
    private TextView cancelBtn, okBtn;
    private int mLayoutHeightDp = DEFAULT_HEIGHT_DP;

    private int mAdBackgroundColor, mTitleColor, mCtaTextColor, mContentColor, mCtaBgColor;

    private ViewGroup mNativeAdContainer;
    private Spinner mBackgroundColorSpinner;
    private Button mShowCodeButton, mReloadButton;
    private SeekBar mSeekBar;
    private View mAdView;

    public AppCloseDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AppCloseDialogFragment newInstance(String title) {
        AppCloseDialogFragment frag = new AppCloseDialogFragment();
        Bundle args = new Bundle();
        args.putString("rowId", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_native_ad_template, container, false);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(view1 -> dismiss());

        okBtn = view.findViewById(R.id.okBtn);
        okBtn.setOnClickListener(view12 -> {
            dismiss();
            getActivity().finish();

        });



        return view;
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }



}