package iso.my.com.inspectionstudentorganization;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import iso.my.com.inspectionstudentorganization.GeneralClass.AppErrorListener;
import iso.my.com.inspectionstudentorganization.GeneralClass.AppMultiplePermissionListener;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashScreen extends AppCompatActivity {

    private MultiplePermissionsListener allPermissionsListener;
    private PermissionRequestErrorListener errorListener;

    private static int SPLASH_TIME_OUT = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},201);
//            // alan test kon
//        }


        set();
        changeStatusBarColor();
        try {
            if (shouldAskPermission()) {
                checkAppPermission();
            } else {
               gotoMainActivity();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    private void set() {

        errorListener = new AppErrorListener();
        MultiplePermissionsListener feedbackViewMultiplePermissionListener =
                new AppMultiplePermissionListener(this);
        allPermissionsListener =
                new CompositeMultiplePermissionsListener(feedbackViewMultiplePermissionListener);


        new Handler().postDelayed(() -> {

            Intent i = new Intent(SplashScreen.this, LoginUser.class);
            startActivity(i);
            finish();
        }, SPLASH_TIME_OUT);


    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void checkAppPermission() {
        final String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.FOREGROUND_SERVICE
        };

        Dexter.withActivity(this)
                .withPermissions(permissions)
                .withListener(allPermissionsListener)
                .withErrorListener(errorListener)
                .check();
    }

    public void gotoMainActivity() {
        Intent intent = new Intent(SplashScreen.this, LoginUser.class);
        startActivity(intent);
        SplashScreen.this.finish();
    }

    public static boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.background));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
}