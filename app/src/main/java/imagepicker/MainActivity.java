package imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.splunk.mint.Mint;

import org.compressor.imagecompress.R;

import id.zelory.compressor.sample.common.BaseActivity;

public class MainActivity extends BaseActivity  {
    private final String TAG = MainActivity.class.getSimpleName();
    private ImageView imageView;
    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 3000;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    public void replaceView() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_or_gallery_selection);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //AudienceNetworkAds.buildInitSettings(this).initialize();
        showBanner();
        imageView = findViewById(R.id.imageView);
        Mint.disableNetworkMonitoring();
        Mint.initAndStartSession(this.getApplication(), "781b0a9b");

        (findViewById(R.id.startCameraBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PickerBuilder(MainActivity.this, PickerBuilder.SELECT_FROM_CAMERA)
                        .setOnImageReceivedListener(imageUri -> {
                            //Toast.makeText(MainActivity.this,"Got image - " + imageUri,Toast.LENGTH_LONG).show();
                            imageView.setImageURI(imageUri);

                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("imageUri", imageUri);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        })
                        .setImageName("Image")
                        .setImageFolderName("ImageCompressFolder")
                        .withTimeStamp(false)
                        .setCropScreenColor(getResources().getColor(R.color.colorPrimary))
                        .start();
            }
        });

        (findViewById(R.id.startGalleryBtn)).setOnClickListener(v -> new PickerBuilder(MainActivity.this, PickerBuilder.SELECT_FROM_GALLERY)
                .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                    @Override
                    public void onImageReceived(Uri imageUri) {
                        imageView.setImageURI(imageUri);
                        // Toast.makeText(MainActivity.this,"Got image - " + imageUri,Toast.LENGTH_LONG).show();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("imageUri", imageUri);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                })
                .setImageName("Image")
                .setImageFolderName("ImageCompressFolder")
                .setCropScreenColor(getResources().getColor(R.color.colorPrimary))
                .setOnPermissionRefusedListener(new PickerBuilder.onPermissionRefusedListener() {
                    @Override
                    public void onPermissionRefused() {

                    }
                })
                .start());

    }



        public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
    private void showBanner() {

    }



    @Override
    public void onClick(View view) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void onEventCapture(String eventName) {

        try {
            Bundle par = new Bundle();
            par.putString(eventName, eventName);
            mFirebaseAnalytics.logEvent(eventName, par);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
