package iso.my.com.inspectionstudentorganization;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import iso.my.com.inspectionstudentorganization.GeneralClass.Text_converter;
import iso.my.com.inspectionstudentorganization.Models.SpType;
import iso.my.com.inspectionstudentorganization.OfficeDet.EndIns;

public class CheckPrice extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    ImageButton back;
    Button submit_error, back_main, calculate;
    TextView show_price;
    private ArrayList<SpType> sptype = new ArrayList<>();
    Spinner dayspinner, carspinner, regionspinner, servicespinner, peoplespinner;
    private GoogleMap mMap;
    LocationManager locationManager;
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    String sp22, sp44, krooki, car_id, endurl, URI_SCHLISTDET = "http://sns.tehranedu.ir/ws/InspectorSchool.aspx";
    String URI_CHECKPRICE = "http://sns.tehranedu.ir/ws/checkprice.aspx";

    SharedPreferences pref;
     Double schlat, schlon, stlat, stlon;
    LatLng latlng;
    int id, dayid, day_id, servid, regid, carid, peid, price;
    public EndIns endIns;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_price);
        set();

        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else requestLocation();
        if (!isLocationEnabled())
            showAlert(1);

        changeStatusBarColor();

    }

    private void set() {

        //=====================================================================
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbarcheckprice);

        //====================================================================

        back = findViewById(R.id.btnback);
        back.setOnClickListener(v -> onBackPressed());

        calculate = findViewById(R.id.calculate);
        calculate.setOnClickListener(v -> SendData());

        back_main = findViewById(R.id.backmain);
        back_main.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Completeinspection.class);
            startActivity(intent);
        });

        submit_error = findViewById(R.id.submiterror);
        submit_error.setOnClickListener(v -> {
            pref = getSharedPreferences("myprefs", MODE_PRIVATE);
            id = Integer.parseInt(pref.getString("last_id", "-1"));

            String desc = "تخلف مغایرت قیمت سرویس دانش آموز";

            Log.i("mm", "hey id of end click is " + id);
            endurl = "http://sns.tehranedu.ir/ws/InsertInspectorReports.aspx"
                    + "?id=" + id + "&inf_id=" + "7" + "&type=" + "1" + "&des=" + URLEncoder.encode(desc);

            System.out.println("error price:" + endurl);

            endIns = new EndIns(() ->
            {
                showalertok();
//                Intent i = new Intent(getApplicationContext(), MainMenu.class);
//                startActivity(i);
                return null;
            }, endurl);
        });

        show_price = findViewById(R.id.showprice);
        dayspinner = findViewById(R.id.dayspinner);
        carspinner = findViewById(R.id.carspinner);
        regionspinner = findViewById(R.id.regionspinner);
        servicespinner = findViewById(R.id.servicespinner);
        peoplespinner = findViewById(R.id.peoplespinner);
        LoadDays();
        LoadCars();
        LoadRegions();
        LoadServices();
        LoadPeople1();
        LoadPeople2();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        ///map
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        getLocations();
    }

    private void LoadDays() {
        final ArrayList<String> day = new ArrayList<>();
        for (int i = 1; i < 2; i++) {

            sptype.add(new SpType("" + (i + 1), "" + (i + 1)));
            day.add("تعداد روز ها...");
            day.add("22 روز");
            day.add("26 روز");


        }
        ArrayAdapter<String> dayadapter = new ArrayAdapter<>(CheckPrice.this, android.R.layout.simple_spinner_dropdown_item, day);
        dayspinner.setAdapter(dayadapter);

        dayspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                dayid = dayspinner.getSelectedItemPosition();
                System.out.println("dayid::" + dayid);
                if (dayid == 1) {
                    day_id = 22;
                } else if (dayid == 2) {
                    day_id = 26;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    private void LoadCars() {
        final ArrayList<String> car = new ArrayList<>();
        for (int i = 1; i < 2; i++) {

            sptype.add(new SpType("" + (i + 1), "" + (i + 1)));
            car.add("نوع خودرو...");
            car.add("سواری");
            car.add("ون");
            car.add("مینی بوس");
            car.add("اتوبوس");


        }
        ArrayAdapter<String> caradapter = new ArrayAdapter<>(CheckPrice.this, android.R.layout.simple_spinner_dropdown_item, car);
        carspinner.setAdapter(caradapter);
        carspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                carid = carspinner.getSelectedItemPosition();
                System.out.println("carid::" + carid);

                if (carid == 1) {
                    car_id = "s";
                    LoadPeople1();

                } else if (carid == 2) {
                    car_id = "v";
                    LoadPeople2();

                } else if (carid == 3) {
                    car_id = "m";
                    LoadPeople2();


                } else if (carid == 4) {
                    car_id = "m";
                    LoadPeople2();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void LoadRegions() {
        final ArrayList<String> reg = new ArrayList<>();
        for (int i = 1; i < 2; i++) {

            sptype.add(new SpType("" + (i + 1), "" + (i + 1)));
            reg.add(" نوع منطقه...");
            reg.add("منطقه نوع 1");
            reg.add("منطقه نوع 2");

        }
        ArrayAdapter<String> regadapter = new ArrayAdapter<>(CheckPrice.this, android.R.layout.simple_spinner_dropdown_item, reg);
        regionspinner.setAdapter(regadapter);
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

    private void LoadServices() {
        final ArrayList<String> serv = new ArrayList<>();
        for (int i = 1; i < 2; i++) {

            sptype.add(new SpType("" + (i + 1), "" + (i + 1)));
            serv.add("نوع سرویس...");
            serv.add("سرویس رفت و برگشت");
            serv.add("سرویس تک مسیره");


        }
        ArrayAdapter<String> servadapter = new ArrayAdapter<>(CheckPrice.this, android.R.layout.simple_spinner_dropdown_item, serv);
        servicespinner.setAdapter(servadapter);
        servicespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                servid = servicespinner.getSelectedItemPosition();
                System.out.println("servid::" + servid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void LoadPeople1() {
        final ArrayList<String> pe = new ArrayList<>();
        for (int i = 1; i < 2; i++) {

            sptype.add(new SpType("" + (i + 1), "" + (i + 1)));
            pe.add("تعداد نفرات...");
            pe.add("4 نفر");
            pe.add("3 نفر");

        }
        ArrayAdapter<String> peadapter = new ArrayAdapter<>(CheckPrice.this, android.R.layout.simple_spinner_dropdown_item, pe);
        peoplespinner.setAdapter(peadapter);
        peoplespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                peid = peoplespinner.getSelectedItemPosition();
                System.out.println("peid::" + peid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void LoadPeople2() {
        final ArrayList<String> pe = new ArrayList<>();
        for (int i = 1; i < 2; i++) {

            sptype.add(new SpType("" + (i + 1), "" + (i + 1)));
            pe.add("تعداد نفرات...");
            pe.add("10 نفر");
            pe.add("8 نفر");

        }
        ArrayAdapter<String> peadapter = new ArrayAdapter<>(CheckPrice.this, android.R.layout.simple_spinner_dropdown_item, pe);
        peoplespinner.setAdapter(peadapter);
        peoplespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                peid = peoplespinner.getSelectedItemPosition();
                System.out.println("peid::" + peid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    ///set map
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
            stlat = latLng.latitude;
            stlon = latLng.longitude;
            mMap.clear();
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.addMarker(markerOptions);
        });
    }

    @Override
    public void onLocationChanged(Location location) {

        try {
            System.out.println("uri:" + "");
//            LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(myCoordinates));
//
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                    new LatLng(location.getLatitude(), location.getLongitude()), 13.5f));


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

    @SuppressLint("MissingPermission")
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
        locationManager.requestLocationUpdates(provider, 20000, 20, this);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                    } else
                        requestPermissions(PERMISSIONS, PERMISSION_ALL);
                })
                .setNegativeButton("Cancel", (paramDialogInterface, paramInt) -> finish());
        dialog.show();
    }


    private String getpass() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("last_id", null);
    }

    private void Text_converter(TextView textView) {
        Text_converter n = new Text_converter(textView);
        n.transfer();
    }

    private void getLocations() {

        String url = URI_SCHLISTDET + "?id=" + getpass();
        System.out.println("url" + url);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {

            try {

                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    krooki = jsonObject.getString("krooki");
                    // lat = jsonObject.getDouble("krooki");
                    //  lon = jsonObject.getDouble("sch_lon");

                    //(35.7289354372477, 51.44374079592284)

                    System.out.println("krooki is ::" + krooki);
                    StringBuilder sb = new StringBuilder();
                    String[] separated = krooki.split(",");
                    //  String sp1 = separated[0];
                    String sp2 = separated[0];
                    sp22 = sp2.replace("(", " ");
                    String sp4 = separated[1];
                    sp44 = sp4.replace(")", " ");
                    sb.append(sp22).append(sp44.replace(")", " "));

//                    locations.setText(sb.toString());

                    System.out.println("lat :" + sp22);
                    System.out.println("lon :" + sp44);

                    latlng = new LatLng(Double.parseDouble(sp22), Double.parseDouble(sp44));

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(sp22), Double.parseDouble(sp44)))
                            .title("محل مدرسه")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                    //    mMap.addMarker(new MarkerOptions().title("رهنمای دماوند"));

                    // Moving CameraPosition to last clicked position
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                    // Setting the zoom level in the map on last position is clicked
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

                }

            } catch (JSONException e) {

                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }, error -> {
            Log.e("TError", "Login Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

        }) {
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void SendData() {
        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = Integer.parseInt(pref.getString("last_id", "0"));

        if (id == 0) {
            finish();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);

        dayid = dayspinner.getSelectedItemPosition();
        carid = carspinner.getSelectedItemPosition();
        servid = servicespinner.getSelectedItemPosition();
        regid = regionspinner.getSelectedItemPosition();
        peid = peoplespinner.getSelectedItemPosition();

        System.out.println(URI_CHECKPRICE
                + "?sch_id=" + id
                + "&dayid=" + day_id
                + "&carid=" + car_id
                + "&semid=" + servid
                + "&regid=" + regid
                + "&peid=" + peid
                + "&stlat=" + stlat
                + "&stlon=" + stlon
                + "&schlat=" + sp22.trim()
                + "&schlon=" + sp44.trim());

        @SuppressLint("SetTextI18n") JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                URI_CHECKPRICE
                        + "?sch_id=" + id
                        + "&dayid=" + day_id
                        + "&carid=" + car_id
                        + "&semid=" + servid
                        + "&regid=" + regid
                        + "&peid=" + peid
                        + "&stlat=" + stlat
                        + "&stlon=" + stlon
                        + "&schlat=" + sp22.trim()
                        + "&schlon=" + sp44.trim(), response ->
        {

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);

                    if (jsonObject.optString("status").equals("1")) {
                        Toast.makeText(getApplicationContext(), " اطلاعات با موفقیت ثبت شد.", Toast.LENGTH_LONG).show();

                        price = jsonObject.getInt("price");

                        Log.i("price:::", "price:" + price);

                        show_price.setText(price + "ریال");

//                        Intent intent = new Intent(getApplicationContext(), SchoolRegister.class);
//
//                        startActivity(intent);

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

    private void showalertok() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("تخلف مورد نظر ثبت شد.");
        builder.setPositiveButton("باشه!", (dialog, which) -> {
            //  Intent intent = new Intent(getApplicationContext(), Menu.class);
            //   startActivity(intent);
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar));
        }
    }



}
