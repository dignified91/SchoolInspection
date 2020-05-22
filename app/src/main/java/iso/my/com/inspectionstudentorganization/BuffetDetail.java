package iso.my.com.inspectionstudentorganization;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;


import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.GeneralClass.GPSTracker;
import iso.my.com.inspectionstudentorganization.GeneralClass.MyHttpUtils;
import iso.my.com.inspectionstudentorganization.Models.SchoolsDetail;
import iso.my.com.inspectionstudentorganization.Models.SpType;
import iso.my.com.inspectionstudentorganization.SchoolsDetail.ArrayAdapterForShowDetailSch;
import iso.my.com.inspectionstudentorganization.SchoolsDetail.SchDetailUtils;
import iso.my.com.inspectionstudentorganization.SchoolsDetail.StartIns;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BuffetDetail extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private static final int PERMISSION_REQ_CODE = 1234;
    public Double lat, lon;
    List<AsyncTask> tasks = new ArrayList<>();

    String URI_LOCATION = "http://sns.tehranedu.ir/ws/RecordStatusInspector.aspx";
    String URI_SCHLISTDET = "http://sns.tehranedu.ir/ws/InspectorSchool.aspx";
    ArrayList<String> typeerror;
    ArrayList<SpType> spt = new ArrayList<>();
    Button start, error;
    ImageButton back;
    TextView tv_name, tv_region;
    SharedPreferences pref;
    public StartIns startIns;
    private String starturl, krooki;
    public int id;
    List<SchoolsDetail> schoolsdetail;
    private ArrayAdapterForShowDetailSch adapter;
    private ListView lvdetail;
    private GoogleMap mMap;
    LocationManager locationManager;
    LatLng latlng;
    final static int PERMISSION_ALL = 1;
    final static String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buffet_detail);


        Log.i("mm", "hey I am showing details");

        set();
        if (Build.VERSION.SDK_INT >= 23 && !isPermissionGranted()) {
            requestPermissions(PERMISSIONS, PERMISSION_ALL);
        } else requestLocation();
        if (!isLocationEnabled())
            showAlert(1);
        changeStatusBarColor();

        init();
        getdet();
        getLocations();
        //checkPermissions();
        beforeBtnStart();
    }

//    private void checkPermissions() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSION_REQ_CODE);
//        }
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == PERMISSION_REQ_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Permission denied to access location", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onResume() {
        super.onResume();
        Log.i("mm", "hey I am resuming details");
        set();
        changeStatusBarColor();
        init();
        getdet();
    }

    //set ui
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void set() {
        //====================================================================

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbardetailschool);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Yekan.ttf");
        toolbar_title.setTypeface(face);
        //====================================================================

        ///map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ///map
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //`    mo = new MarkerOptions().position(new LatLng(35.70617388, 51.37748167)).title("My Current Location");


        typeerror = new ArrayList<>();
        spt = new ArrayList<>();

        tv_name = findViewById(R.id.schoolname);

        tv_region = findViewById(R.id.region);


        start = findViewById(R.id.start);
        start.setOnClickListener(v ->
        {
            Log.i("nmn", "hey on click start clicked");
            onClickBtnStart();
        });

        lvdetail = findViewById(R.id.lvdethislist);

        back = findViewById(R.id.btnback);
        back.setOnClickListener(v -> {
            beforeBtnStart();
            Intent intent = new Intent(getApplicationContext(), MainMenuBuffet.class);
            startActivity(intent);
        });

    }

    ///set map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(17);
        mMap.setTrafficEnabled(true);

    }

    @Override
    public void onLocationChanged(Location location) {

        try {

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

                    //(35.7289354372477, 51.44374079592284)

                    System.out.println("krooki is ::" + krooki);
                    StringBuilder sb = new StringBuilder();
                    String[] separated = krooki.split(",");

                    String sp2 = separated[0];
                    String sp22 = sp2.replace("(", " ");
                    String sp4 = separated[1];
                    String sp44 = sp4.replace(")", " ");
                    sb.append(sp22).append(sp44.replace(")", " "));

                    System.out.println("lat :" + sp22);
                    System.out.println("lon :" + sp44);

                    latlng = new LatLng(Double.parseDouble(sp22), Double.parseDouble(sp44));

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(sp22), Double.parseDouble(sp44)))
                            .title("محل مدرسه")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


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

    //send location when click start button with post method
    public void setlocation() {
        GPSTracker gpstracker = new GPSTracker(this);

        if (gpstracker.canGetLocation()) {
            lat = gpstracker.getLatitude();
            lon = gpstracker.getLongitude();
            Toast.makeText(this,
                    "Location is \n lat : " + lat + "\n lon : " + lon,
                    Toast.LENGTH_SHORT).show();
        } else {
            gpstracker.showGpsAlertDialog();
        }


        // Called when a new location is found by the network location provider.
        MyHttpUtils.RequestData requestData = new MyHttpUtils.RequestData(URI_LOCATION, "POST");
        requestData.setParameter("lat", String.valueOf(lat));
        requestData.setParameter("lon", String.valueOf(lon));
        requestData.setParameter("type", "s");
        new BuffetDetail.MyTask().execute(requestData);
        Toast.makeText(getApplicationContext(), "Location is \n lat : " + lat + "\n lon : " + lon, Toast.LENGTH_SHORT).show();

    }

    //httputils for send parametr post method
    public class MyTask extends AsyncTask<MyHttpUtils.RequestData, Void, String> {


        @Override
        protected void onPreExecute() {
            if (tasks.isEmpty()) {
                //  pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(MyHttpUtils.RequestData... params) {
            MyHttpUtils.RequestData reqData = params[0];

            return MyHttpUtils.getDataHttpUrlConnection(reqData);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                result = "null";
            }
            //  tv.setText(result);
            tasks.remove(this);
            if (tasks.isEmpty()) {
                //   pb.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void beforeBtnStart() {
        SharedPreferences.Editor deditor = getSharedPreferences("start_end_service", MODE_PRIVATE).edit();
        deditor.putString("not_started", "1");
        deditor.apply();
        Log.e("not_started", "nmn");
    }

    private void onClickBtnStart() {

        setlocation();
        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = Integer.parseInt(pref.getString("last_id", "0"));
        if (id == 0) {
            Log.i("mm", "hey nothing is in id ");
            return;

        } else {
            Log.i("mm", "hey id is " + Integer.toString(id));

        }

        starturl = "http://sns.tehranedu.ir/ws/RecordStatusbuf.aspx"
                + "?id=" + id + "&lat=" + lat + "&lnt=" + lon + "&type=" + "s";
        startIns = new StartIns(() ->
        {
            afterStart();
            return null;
        }, starturl);


    }

    public void afterStart() {
        System.out.print("i'm here paian");
        Log.i("mm", "hey i'm starting ");

        assert startIns != null;

        SharedPreferences.Editor deditor = getSharedPreferences("start_end_service", MODE_PRIVATE).edit();
        deditor.putString("is_started", "1");
        deditor.apply();


        if (startIns.isStarterd()) {
            Intent intent = new Intent(this, CompleteinspectionBuffet.class);
            startActivity(intent);

        } else Log.d("nnn", "خطا");
    }

    private void init() {
        schoolsdetail = new ArrayList<>();
    }

    private void getdet() {


        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = Integer.parseInt(pref.getString("last_id", "-1"));
        Log.i("mm", "hey id is " + id);


        System.out.println(URI_SCHLISTDET + "?id=" + id);
        JsonArrayRequest request = new JsonArrayRequest(
                URI_SCHLISTDET + "?id=" + id,
                response ->
                {
                    schoolsdetail = SchDetailUtils.jsonArrayToDetailSchool(response);
                    updateDisplay();

                },

                error ->
                {

                }
        );

        App.getRequestQueue().add(request);

    }

    private void updateDisplay() {

        //  if (schoolsdetail.size() == 0) notFound();
        adapter = new ArrayAdapterForShowDetailSch(this, R.layout.detail_sch_list_item, schoolsdetail);
        lvdetail.setAdapter(adapter);

    }

    private String getpass() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("last_id", null);
    }

    //add top of activity color
    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar5));
        }
    }

    //add font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
}

