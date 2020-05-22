package iso.my.com.inspectionstudentorganization;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.Models.School;
import iso.my.com.inspectionstudentorganization.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegSchool extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener {

    Spinner SchoolSpinner;
    private EditText txtGetNameSchool;
    public static final String URI_SCHREGISTER = "http://ontimenews.ir/webs/regschool.aspx";
    String result;
    double schlat, schlon;
    ArrayList<School> school = new ArrayList<>();
    private final int KEY_VALUE_SCHCODE = 0, KEY_VALUE_SCHDATA = 1;
    private Map<Integer, String[]> values;
    ArrayList<String> schoolname;
    String URI_SCHLIST = "http://ontimenews.ir/webs/schlist.aspx";
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    private GoogleMap mMap;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.non_reg_school);
        //=====================================================================
       // TextView toolbar_title = findViewById(R.id.toolbar_title);
      //  toolbar_title.setText(R.string.schsubmit);
        //====================================================================

//        SchoolSpinner = findViewById(R.id.schSpinner);
//
//        ImageButton help = findViewById(R.id.help);
//        help.setOnClickListener(v -> show());

//        ImageButton back = findViewById(R.id.back);
//        back.setOnClickListener(v -> onBackPressed());

        set();

        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else {
            requestLocation();
        }
        if (!isLocationEnabled()) {
            showAlert(1);
        }


    }

    private String getMelli() {

        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        return sharedPreferences.getString("melli", null);
    }

    private void set() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
      //  txtGetNameSchool = findViewById(R.id.id__school_register__txt_get_name_school);



//        Button submitButton = findViewById(R.id.btnsubmit);
//
//
//        submitButton.setOnClickListener(v ->
//        {
//            String nameSchool = txtGetNameSchool.getText().toString().trim();
//            String lat = schlat + "".trim();
//            String lon = schlon + "".trim();
//
//            if (lat.equals("0.0") && lon.equals("0.0"))
//                Toast.makeText(App.G.getContext(), "لطفا محل مدرسه را وارد کنید", Toast.LENGTH_LONG).show();
//
//            else {
//                Toast.makeText(getApplicationContext(), "اطلاعات کاربری ثبت شد.", Toast.LENGTH_LONG).show();
//                getData();
////                Intent intent = new Intent(getApplicationContext(), Menu.class);
////                startActivity(intent);
//
//
//            }
//        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(13);
        mMap.setTrafficEnabled(true);
        mMap.setOnMapClickListener(latLng ->
        {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(latLng.latitude + " : " + latLng.longitude);
            schlat = latLng.latitude;
            schlon = latLng.longitude;
            mMap.clear();
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.addMarker(markerOptions);
        });

    }

    @Override
    public void onLocationChanged(Location location) {

        try {

            LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13.5f));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void requestLocation() {

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(provider, 10000, 10, this);
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isPermissionGranted() {
        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.v("mylog", "Permission is granted");
            return true;
        } else {
            Log.v("mylog", "Permission not granted");
            return false;
        }
    }

    private void showAlert(final int status) {
        String message, title, btnText;
        if (status == 1) {
            message = "Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                    "use this app";
            title = "Enable Location";
            btnText = "Location Settings";
        } else {
            message = "Please allow this app to access location!";
            title = "Permission access";
            btnText = "Grant";
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton(btnText, (paramDialogInterface, paramInt) ->
                {
                    if (status == 1) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(PERMISSIONS, PERMISSION_ALL);
                    }
                })
                .setNegativeButton("Cancel", (paramDialogInterface, paramInt) -> finish());
        dialog.show();
    }

    private void getData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        System.out.println(URI_SCHREGISTER
                + "?mastermelli=" + getMelli()
                + "&schname=" + (values.get(SchoolSpinner.getSelectedItemPosition()))[KEY_VALUE_SCHCODE]
                + "&schlon=" + schlon
                + "&schlat=" + schlat);


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URI_SCHREGISTER
                + "?mastermelli=" + getMelli()
                + "&schname=" + (values.get(SchoolSpinner.getSelectedItemPosition()))[KEY_VALUE_SCHCODE]
                + "&schlon=" + schlon
                + "&schlat=" + schlat
                , response ->
        {

            for (int i = 0; i < response.length(); i++) {
                try {

                    JSONObject jsonObject = response.getJSONObject(i);

                    result = jsonObject.getString("schstatus");

                    if (result.equals("1")) {
                        Toast.makeText(getApplicationContext(), "اطلاعات کاربری ثبت شد.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "ارسال اطلاعات با خطا مواجه شد.", Toast.LENGTH_LONG).show();

                    }


                } catch (JSONException matin) {
                    System.out.println(matin);

                    matin.printStackTrace();
                    progressDialog.dismiss();
                }
            }
            progressDialog.dismiss();
        },
                error ->
                {

                    Log.e("Volley", error.toString());
                    progressDialog.dismiss();
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
