package iso.my.com.inspectionstudentorganization.GeneralClass;


import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import iso.my.com.inspectionstudentorganization.SplashScreen;


public class AppMultiplePermissionListener implements MultiplePermissionsListener {

    private final SplashScreen activity;

    public AppMultiplePermissionListener(SplashScreen activity) {
        this.activity = activity;
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        if (report.areAllPermissionsGranted()) {
            activity.gotoMainActivity();
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                   PermissionToken token) {
        token.continuePermissionRequest();
    }
}
