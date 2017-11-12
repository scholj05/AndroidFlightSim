package jesse.myapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class MenuActivity extends AppCompatActivity {

    private OpenGLView openGLView;
    private OpenGLRenderer openGLRenderer;
    private static boolean supportsEs2;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        openGLView = new OpenGLView(getApplicationContext());
        setContentView(openGLView);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        this.context = this;
    }

    public static boolean ES2Support()
    {
        return supportsEs2;
    }

    public Context getContext()
    {
        return this;
    }

/*
    @Override
    protected void onResume() {
        super.onResume();
        openGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        openGLView.onPause();
    }*/
}
