package id.zelory.compressor.sample;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import org.compressor.imagecompress.R;


public class AppRateDialog extends DialogFragment implements SmileRating.OnSmileySelectionListener, SmileRating.OnRatingSelectedListener {


    private final String TAG = MainActivity.class.getSimpleName();
    ImageView cancelBtn;
    LinearLayout sendDetailFeedbackLl, rateUsOnPlayStoreLl;
    private SmileRating mSmileRating;

    public AppRateDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static AppRateDialog newInstance(String title) {
        AppRateDialog frag = new AppRateDialog();
        Bundle args = new Bundle();
        args.putString("rowId", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
        switch (smiley) {
            case SmileRating.BAD:
                Log.i(TAG, "Bad");
                sendDetailFeedbackLl.setVisibility(View.VISIBLE);
                rateUsOnPlayStoreLl.setVisibility(View.GONE);
                //openFeedbackPage();
                //dismiss();
                break;
            case SmileRating.GOOD:
                Log.i(TAG, "Good");
                sendDetailFeedbackLl.setVisibility(View.VISIBLE);
                rateUsOnPlayStoreLl.setVisibility(View.GONE);
                openPlayStoreLink();
                dismiss();
                break;
            case SmileRating.GREAT:
                Log.i(TAG, "Great");
                sendDetailFeedbackLl.setVisibility(View.GONE);
                rateUsOnPlayStoreLl.setVisibility(View.VISIBLE);
                openPlayStoreLink();
                dismiss();
                break;
            case SmileRating.OKAY:
                Log.i(TAG, "Okay");
                sendDetailFeedbackLl.setVisibility(View.VISIBLE);
                rateUsOnPlayStoreLl.setVisibility(View.GONE);
                //openPlayStoreLink();
                // dismiss();
                break;
            case SmileRating.TERRIBLE:
                Log.i(TAG, "Terrible");
                sendDetailFeedbackLl.setVisibility(View.VISIBLE);
                rateUsOnPlayStoreLl.setVisibility(View.GONE);
                //openFeedbackPage();
                //dismiss();
                break;
            case SmileRating.NONE:
                Log.i(TAG, "None");
                sendDetailFeedbackLl.setVisibility(View.VISIBLE);
                rateUsOnPlayStoreLl.setVisibility(View.GONE);
                // openFeedbackPage();
                // dismiss();
                break;
        }
    }

    public void openFeedbackPage() {
        Intent commonActivity = new Intent(getActivity(), CommonBaseActivity.class);
        commonActivity.putExtra("flowType", CommonBaseActivity.FEEDBACK_NAV);
        startActivity(commonActivity);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void openPlayStoreLink() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            getActivity().startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            getActivity().startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }
    }

    @Override
    public void onRatingSelected(int level, boolean reselected) {
        Log.i(TAG, "Rated as: " + level + " - " + reselected);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.app_rate_dialog_sticker, container, false);
        LinearLayout nativeAdContainer = view.findViewById(R.id.fragment_container_delete);


        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        setCancelable(false);


        return view;
    }


    private void showRatingOptionAnimator(View view) {

        mSmileRating = view.findViewById(R.id.ratingView);
        mSmileRating.setOnSmileySelectionListener(this);
        mSmileRating.setOnRatingSelectedListener(this);
        // Typeface typeface = Typeface.createFromAsset(getAssets(), "MetalMacabre.ttf");
        // mSmileRating.setTypeface(typeface);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showRatingOptionAnimator(view);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(view1 -> dismiss());

        sendDetailFeedbackLl = view.findViewById(R.id.sendDetailFeedbackLl);
        sendDetailFeedbackLl.setOnClickListener(view1 -> {
                    openFeedbackPage();
                    dismiss();
                }
        );


        rateUsOnPlayStoreLl = view.findViewById(R.id.rateUsOnPlayStoreLl);
        rateUsOnPlayStoreLl.setOnClickListener(view1 -> {
            openPlayStoreLink();
            dismiss();
        });


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                //cancelTask = true;
                return true;
            }

            return false;
        });

        sendDetailFeedbackLl.setVisibility(View.GONE);
        rateUsOnPlayStoreLl.setVisibility(View.VISIBLE);
        mSmileRating.setSelectedSmile(BaseRating.GREAT);
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }


}
