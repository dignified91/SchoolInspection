package iso.my.com.inspectionstudentorganization;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.GeneralClass.GPSTracker;
import iso.my.com.inspectionstudentorganization.Models.SpType;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NonRegSchool extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    ImageButton back;
    Button submitButton;
    LocationManager locationManager;
    MarkerOptions mo;
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    GoogleMap mMap;
    double schlat, schlon;
    EditText schname, schcode,ed_exp;
    Spinner regionspinner, levelspinner, sexspinner;
    private ArrayList<SpType> sptype = new ArrayList<>();
    int regid, levelid, sexid, id,sex_id;
    SharedPreferences pref;
    String nameSchool, codeSchool,exp;
    String URI_REGSCH = "http://sns.tehranedu.ir/ws/insregschool.aspx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.non_reg_school);
        set();

        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else requestLocation();
        if (!isLocationEnabled())
            showAlert(1);

        changeStatusBarColor();
    }

    private void set() {
        back = findViewById(R.id.btnback);
        back.setOnClickListener(v -> onBackPressed());

        //=====================================================================
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbarnonregsch);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Yekan.ttf");
        toolbar_title.setTypeface(face);

        //====================================================================
        schname = findViewById(R.id.schoolname);
        schcode = findViewById(R.id.schoolcode);

        regionspinner = findViewById(R.id.regionspinner);
        levelspinner = findViewById(R.id.levelspinner);
        sexspinner = findViewById(R.id.sexspinner);

        ed_exp=findViewById(R.id.edexp);

        submitButton = findViewById(R.id.submit);


        submitButton.setOnClickListener(v ->
        {
            nameSchool = schname.getText().toString();
            codeSchool = schcode.getText().toString();

            String lat = schlat + "".trim();
            String lon = schlon + "".trim();

            if (nameSchool.isEmpty()) {
                Toast.makeText(App.G.getContext(), "لطفا نام مدرسه را وارد کنید", Toast.LENGTH_LONG).show();
            }

            if (lat.equals("0.0") && lon.equals("0.0"))
                Toast.makeText(App.G.getContext(), "لطفا محل مدرسه را وارد کنید", Toast.LENGTH_LONG).show();

            else {
                Toast.makeText(getApplicationContext(), "اطلاعات کاربری ثبت شد.", Toast.LENGTH_LONG).show();
                getData();
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(intent);

            }
        });

        ///map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        ///map
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mo = new MarkerOptions().position(new LatLng(35.70617388, 51.37748167)).title("My Current Location");

        mapFragment.getMapAsync(this);


        LoadLevel();
        LoadRegion();
        LoadSex();
    }

    private void LoadRegion() {
        final ArrayList<String> reg = new ArrayList<>();
        for (int i = 1; i < 2; i++) {

            sptype.add(new SpType("" + (i + 1), "" + (i + 1)));
            reg.add("منطقه...");
            reg.add("منطقه_1");
            reg.add("منطقه_2");
            reg.add("منطقه_3");
            reg.add("منطقه_4");
            reg.add("منطقه_5");
            reg.add("منطقه_6");
            reg.add("منطقه_7");
            reg.add("منطقه_8");
            reg.add("منطقه_9");
            reg.add("منطقه_10");
            reg.add("منطقه_11");
            reg.add("منطقه_12");
            reg.add("منطقه_13");
            reg.add("منطقه_14");
            reg.add("منطقه_15");
            reg.add("منطقه_16");
            reg.add("منطقه_17");
            reg.add("منطقه_18");
            reg.add("منطقه_19");


        }
        ArrayAdapter dayadapter = new ArrayAdapter(NonRegSchool.this, android.R.layout.simple_spinner_dropdown_item, reg);
        regionspinner.setAdapter(dayadapter);

        regionspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                regid = regionspinner.getSelectedItemPosition();
                System.out.println("regid::" + regid);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

    }

    private void LoadLevel() {
        final ArrayList<String> level = new ArrayList<>();
        for (int i = 1; i < 2; i++) {

            sptype.add(new SpType("" + (i + 1), "" + (i + 1)));
            level.add("مقطع...");
            level.add("پیش دبستانی");
            level.add("دبستان(ابتدایی)");
            level.add("متوسطه عمومی");
            level.add(" متوسطه دوره اول");
            level.add("هنرستان فنی");
            level.add("بازرگانی و حرفه ای");
            level.add("کاردانش");
            level.add("پیش حرفه ای استثنائی");
            level.add("کودکستان استثنائی");
            level.add("استثنائی");
            level.add("متوسط حرفه ای استثنائی");
            level.add("متوسط استثنائی");
            level.add("متوسط دوره اول استثنائی");
            level.add("فنی استثنائی");
            level.add("ابتدایی دوره اول");
            level.add("ابتدایی دوره دوم");


        }
        ArrayAdapter dayadapter = new ArrayAdapter(NonRegSchool.this, android.R.layout.simple_spinner_dropdown_item, level);
        levelspinner.setAdapter(dayadapter);

        levelspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                levelid = levelspinner.getSelectedItemPosition();
                System.out.println("levelid::" + levelid);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    private void LoadSex() {
        final ArrayList<String> sx = new ArrayList<>();
        for (int i = 1; i < 2; i++) {

            sptype.add(new SpType("" + (i + 1), "" + (i + 1)));
            sx.add("جنسیت...");
            sx.add("دخترانه");
            sx.add("پسرانه");


        }
        ArrayAdapter dayadapter = new ArrayAdapter(NonRegSchool.this, android.R.layout.simple_spinner_dropdown_item, sx);
        sexspinner.setAdapter(dayadapter);

        sexspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                sexid = sexspinner.getSelectedItemPosition() - 1;
                System.out.println("dayid::" + sexid);
                if (sexid==0)
                {
                    sex_id=0;

                }
                else if (sexid==1)
                {
                    sex_id=1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMinZoomPreference(14);
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

        GPSTracker gpstracker =  new GPSTracker(this);

      //  Toast.makeText(this, "Location is \n lat : " +  gpstracker.getLatitude() + "\n lon : " + gpstracker.getLongitude(), Toast.LENGTH_SHORT).show();
        Log.d("nasnaas","Location is \n lat : " +  gpstracker.getLatitude() + "\n lon : " + gpstracker.getLongitude());

        LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 13.5f));

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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 30000, 10, this);

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
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (status == 1) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(myIntent);
                        } else
                            requestPermissions(PERMISSIONS, PERMISSION_ALL);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }


    private void getData() {

        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = Integer.parseInt(pref.getString("id", "0"));

        if (id == 0) {
            finish();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);

        nameSchool = schname.getText().toString();
        codeSchool = schcode.getText().toString();
exp=ed_exp.getText().toString();
        levelid = levelspinner.getSelectedItemPosition();
        sexid = sexspinner.getSelectedItemPosition();
        regid = regionspinner.getSelectedItemPosition();


        System.out.println(URI_REGSCH
                + "?insid=" + id
                + "&sex=" + sex_id
                + "&region=" + regid
                + "&level=" + levelid
                + "&lat=" + schlat
                + "&lon=" + schlon
                + "&schname=" + URLEncoder.encode(nameSchool)
                + "&schcode=" + codeSchool
        +"&exp="+ URLEncoder.encode(exp));

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                URI_REGSCH
                        + "?insid=" + id
                        + "&sex=" + sex_id
                        + "&region=" + regid
                        + "&level=" + levelid
                        + "&lat=" + schlat
                        + "&lon=" + schlon
                        + "&schname=" + URLEncoder.encode(nameSchool)
                        + "&schcode=" + codeSchool
                        +"&exp="+URLEncoder.encode(exp), response ->
        {

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);

                    if (jsonObject.optString("status").equals("1")) {
                        Toast.makeText(getApplicationContext(), " اطلاعات با موفقیت ثبت شد.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), MainMenu.class);

                        startActivity(intent);

                    } else {


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
            progressDialog.dismiss();
        }, error ->
        {
            Log.e("Volley", error.toString());
            progressDialog.dismiss();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }


    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar));
        }
    }

    //font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
}
