package com.android.screenshot;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * This class is a demo to show you how to use Shooter.
 */
public class ExampleActivity extends AppCompatActivity {

    private static final int REQ_CODE_PER = 0x2304;
    private static final int REQ_CODE_ACT = 0x2305;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This is an example for using Shooter.
     * This method will request permission and take screenshot on this Activity.
     */
    public void onClickReqPermission(View view) {
        startActivityForResult(createScreenCaptureIntent(), REQ_CODE_PER);
    }

    /**
     * using {@see ScreenShotActivity} to take screenshot on current Activity directly.
     * If you press home it will take screenshot on another app.
     *
     * @param view
     */
    public void onClickShot(View view) {
        startActivityForResult(ScreenShotActivity.createIntent(this, null, 1000), REQ_CODE_ACT);
        toast("Press home key,open another app.");//if you want to take screenshot on another app.
    }

    private Intent createScreenCaptureIntent() {
        //Here using media_projection instead of Context.MEDIA_PROJECTION_SERVICE to  make it successfully build on low api.
        return ((MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE)).createScreenCaptureIntent();
    }

    private String getSavedPath() {
        return getExternalFilesDir("screenshot").getAbsoluteFile() + "/"
                + SystemClock.currentThreadTimeMillis() + ".png";
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQ_CODE_ACT: {
                if (resultCode == RESULT_OK && data != null) {
                    toast("Screenshot saved at " + data.getData().toString());
                } else {
                    toast("You got wrong.");
                }
            }
            break;
            case REQ_CODE_PER: {
                if (resultCode == RESULT_OK && data != null) {
                    Shooter shooter = new Shooter(ExampleActivity.this, resultCode, data);
                    shooter.startScreenShot(getSavedPath(), new Shooter.OnShotListener() {
                                @Override
                                public void onFinish(String path) {
                                    //here is done status.
                                    toast("Screenshot saved at " + path);
                                }

                                @Override
                                public void onError() {
                                    toast("You got wrong.");
                                }
                            }
                    );
                } else if (resultCode == RESULT_CANCELED) {
                    //user canceled.
                } else {

                }
            }
        }
    }

    private void toast(String str) {
        Toast.makeText(ExampleActivity.this, str, Toast.LENGTH_LONG).show();
    }

}
