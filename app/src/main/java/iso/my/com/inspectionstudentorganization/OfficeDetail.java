package iso.my.com.inspectionstudentorganization;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.GeneralClass.GPSTracker;
import iso.my.com.inspectionstudentorganization.GeneralClass.MyHttpUtils;
import iso.my.com.inspectionstudentorganization.Models.OfficesDetail;
import iso.my.com.inspectionstudentorganization.OfficeDet.ArrayAdapterofficesDetail;
import iso.my.com.inspectionstudentorganization.OfficeDet.ClickCheckBox;
import iso.my.com.inspectionstudentorganization.OfficeDet.CreateUrl;
import iso.my.com.inspectionstudentorganization.OfficeDet.OffDetailUtils;
import iso.my.com.inspectionstudentorganization.OfficeDet.Start;
import iso.my.com.inspectionstudentorganization.SchoolsDetail.StartIns;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OfficeDetail extends AppCompatActivity implements OnMapReadyCallback, ClickCheckBox {

    private static final int PERMISSION_REQ_CODE = 1234;
    Button start, form, sendform, btnsendform, submit;
    ImageButton back, formcamera;
    ImageView imageviewfo;
    EditText titleform, exp;
    Bitmap fobitmap;
    SharedPreferences pref;
    static int SELECT_FILE1 = 0;
    List<AsyncTask> tasks = new ArrayList<>();
    private String starturl, thestart, titlef, p1, explain;
    public StartIns startIns;
    String URI_LOCATION = "http://sns.tehranedu.ir/ws/inspectorofficelocation.aspx";
    String URI_SCHLISTDET = "http://sns.tehranedu.ir/ws/Inspectoroffice.aspx";
    String UPLOAD_URL = "http://sns.tehranedu.ir/ws/new/officeimageupload.aspx";

    public Double lat, lon;
    List<OfficesDetail> officedetail = null;
    private Start startt;
    String id;
    private ArrayAdapterofficesDetail adapteroffice = null;
    List<OfficesDetail> officesDetails;
    private ArrayAdapterofficesDetail adapter;
    private ListView lvdetail;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int BTN_TRIP__START = 0, BTN_TRIP__END = 1;
    GoogleMap mMap;
    private int statusBtnTrip = BTN_TRIP__START;
    Boolean isChecked;
    boolean[] bytes = new boolean[5];
    CreateUrl createUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_detail);
        set();
        changeStatusBarColor();
        init();
        getdet();
        checkStartIns();
        checkPermissions();

    }

    protected void onResume() {
        super.onResume();
        Log.d("mm", "hey I am resuming detailoffice");
        set();
        changeStatusBarColor();
        checkStartIns();
        init();
        getdet();
    }

    @SuppressLint("CutPasteId")
    private void set() {
        //====================================================================

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbardetailoffice);

        //====================================================================

        exp = findViewById(R.id.exp);

        form = findViewById(R.id.btnsendform);
        form.setOnClickListener(v -> showformpopup());
        form.setVisibility(View.VISIBLE);


        btnsendform = findViewById(R.id.btnsendform);
        btnsendform.setOnClickListener(v ->
        {
            showformpopup();
            //CreateUrl

        });


        submit = findViewById(R.id.btnsub);
        submit.setOnClickListener(v ->
        {

            // onClickBtnSubmit();
            starturl = createUrl.getUrl();
            startt = new Start(() ->
            {

                // Intent intent = new Intent(getApplicationContext(), MainMenuOffice.class);
                // startActivity(intent);

                return null;
            }, starturl, null);

            System.out.println("starturl :::" + starturl);
            alertboxsubmit();

        });


        start = findViewById(R.id.start);
        start.setOnClickListener(v ->
        {
            pref = getSharedPreferences("start_end_office", MODE_PRIVATE);
            if (pref.getString("is_started", "-1").equals("1")) {

                Log.d("mm", "hey on click end clicked");

                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.apply();
                // onclickBtnEnd();
                Intent intent = new Intent(getApplicationContext(), MainMenuOffice.class);
                startActivity(intent);

            } else {
                Log.d("mm", "hey on click start clicked");
                onClickBtnStart();
                statusBtnTrip = BTN_TRIP__END;
                start.setText("بازگشت");
                start.setBackgroundResource(R.drawable.custom_button_end);
            }

        });

        lvdetail = findViewById(R.id.lvdetofflist);

        back = findViewById(R.id.btnback);
        back.setOnClickListener(v -> {
            beforeBtnStart();
            Intent intent = new Intent(getApplicationContext(), MainMenuOffice.class);
            startActivity(intent);
        });

        Button showmap = findViewById(R.id.map);
        showmap.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ShowLocation.class);
            startActivity(intent);
        });

    }

    private void onClickBtnSubmit() {
//        List<CheckBoxSS> checkBoxSSForDrivers = adapter.getCheckBoxSS();
//        for (CheckBoxSS box : checkBoxSSForDrivers) {
//
//            if (box.chkBoxtype.isChecked()& box.chkBoxins.isChecked()
//                    &box.chkBoxeco.isChecked() &box.chkBoxphone.isChecked() &box.chkBoxaddress .isChecked()) {
//                SublmitClickBtn();
//                return;
//            } else {
//                Toast.makeText(this, "لطفا یکی از موارد را فعال کنید33", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private void SublmitClickBtn() {
//        CreateUrl createUrl = new CreateUrl
//                (id,App.G.officesdetail.getOfficetype(), App.G.officesdetail.getEconomic(), App.G.officesdetail.getInsurance(), App.G.officesdetail.getAddress(), App.G.officesdetail.getPhone());
//        System.out.println("creat url"+App.G.officesdetail.getOfficetype());
//
//        List<CheckBoxSS> checkBoxSSs = adapter.getCheckBoxSS();
//        for (int i = 0, len = officesDetails.size(); i < len; i++) {
//            //  isChecked = adapter.checkBoxArr_at_home.get(i).isChecked();
//            CheckBoxSS checkBoxSS = checkBoxSSs.get(i);
//            createUrl.append(officedetail.get(i).getOfficetype(),
//                    officedetail.get(i).getEconomic(),
//                    officedetail.get(i).getInsurance(),
//                    officedetail.get(i).getAddress(),
//                    officedetail.get(i).getPhone(),
//                    isChecked);
//
//            if (checkBoxSS.chkBoxtype.isChecked())
//                checkBoxSS.chkBoxtype.setEnabled(false);
//
//            if (checkBoxSS.chkBoxins.isChecked())
//                checkBoxSS.chkBoxins.setEnabled(false);
//
//            if (checkBoxSS.chkBoxeco.isChecked())
//                checkBoxSS.chkBoxeco.setEnabled(false);
//
//            if (checkBoxSS.chkBoxphone.isChecked())
//                checkBoxSS.chkBoxphone.setEnabled(false);
//
//            if (checkBoxSS.chkBoxaddress.isChecked())
//                checkBoxSS.chkBoxaddress.setEnabled(false);
//
//
//        }
//
//        createUrl.apply();
//
//        starturl= createUrl.getUrl();
//        startt = new Start(() ->
//        {
//
//            //  afterStartFrom();
//
//            Intent intent = new Intent(getApplicationContext(), MainMenuOffice.class);
//            startActivity(intent);
//
//            return null;
//        }, starturl, null);

    }

    private void afterStartFrom() {
        assert startt != null;

        SharedPreferences.Editor editor = getSharedPreferences("start_end_service", MODE_PRIVATE).edit();
        editor.putBoolean("is_started", true);
        editor.apply();


        if (startt.isStartedHome()) {
            System.out.println("uri:" + "");

        } else Toast.makeText(App.getContext(), "خطا", Toast.LENGTH_LONG).show();

    }

    private void beforeBtnStart() {
        SharedPreferences.Editor deditor = getSharedPreferences("start_end_office", MODE_PRIVATE).edit();
        deditor.putString("not_started", "1");
        deditor.apply();
        Log.e("not_started", "nmn");
    }

    //check start Ins sharepref
    private void checkStartIns() {
        pref = getSharedPreferences("start_end_office", MODE_PRIVATE);

        if (pref.getString("is_started", "-1").equals("1")) {
            Log.d("console", "hey is_started = 1");
            statusBtnTrip = BTN_TRIP__END;
            start.setText("بازگشت");
            start.setBackgroundResource(R.drawable.custom_button_end);

        } else if (pref.getString("not_started", "1").equals("1")) {
            Log.d("console", "hey is_ended = 1");
            statusBtnTrip = BTN_TRIP__START;
            start.setText("شروع بازرسی");
        } else {
            Log.d("console", "hey not_started = 1");
            p1 = pref.getString("ppp", "0");
            if (p1.equals("main")) {

                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(intent);
                Log.d("not_starte", "running main");

            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMinZoomPreference(11);
        mMap.setTrafficEnabled(true);
     //   Toast.makeText(this, "Map is Ready!!", Toast.LENGTH_SHORT).show();
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQ_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied to access location", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void onClickBtnStart() {

        Log.d("mm", "office starting onClickBtnStart");
        setlocation();
        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        // id = Integer.parseInt(pref.getString("last__id", "0"));
        id = pref.getString("last__id", "0");
        if (id.equals("0")) {
            Log.d("mm", "hey nothing is in id ");
            return;

        } else {
            Log.d("mm", "hey id is " + id);

        }

        starturl = "http://sns.tehranedu.ir/ws/inspectorofficelocation.aspx"
                + "?id=" + id + "&lat=" + lat + "&lnt=" + lon + "&type=" + "S";
        statusBtnTrip = BTN_TRIP__START;
        startIns = new StartIns(() ->
        {
            afterStart();
            return null;
        }, starturl);

    }


    public void afterStart() {
        System.out.print("i'm here paian");
        Log.d("mm", "hey i'm starting ");


        assert startIns != null;

        SharedPreferences.Editor deditor = getSharedPreferences("start_end_office", MODE_PRIVATE).edit();
        deditor.putString("is_started", "1");
        deditor.apply();
        //deditor.clear();


        if (startIns.isStarterd()) {
            statusBtnTrip = BTN_TRIP__END;
            start.setText("بازگشت");
            start.setBackgroundResource(R.drawable.custom_button_end);
            //  back.setVisibility(View.GONE);

//            pref = getSharedPreferences("myprefs", MODE_PRIVATE);
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putString("ppp", "detailoffice");
//            editor.apply();


        } else Log.d("nasnaas", "error");

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
        requestData.setParameter("type", "leader");
        new OfficeDetail.MyTask().execute(requestData);
        Toast.makeText(getApplicationContext(), "Location is \n lat : " + lat + "\n lon : " + lon, Toast.LENGTH_SHORT).show();

    }


    //httputils for send parametr post method
    @SuppressLint("StaticFieldLeak")
    public class MyTask extends AsyncTask<MyHttpUtils.RequestData, Void, String> {


        @Override
        protected void onPreExecute() {
            if (tasks.isEmpty()) {
                System.out.println("uri:" + "");
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
                System.out.println("uri:" + "");
            }
        }
    }

    private void init() {
        officesDetails = new ArrayList<>();
    }

    private String getid() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("last__id", null);
    }

    private void getdet() {


        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = pref.getString("last__id", "-1");
        Log.d("mm", "hey id is " + id);


        System.out.println(URI_SCHLISTDET + "?id=" + id);
        JsonArrayRequest request = new JsonArrayRequest(
                URI_SCHLISTDET + "?id=" + id,
                response ->
                {
                    officesDetails = OffDetailUtils.jsonArrayToDetailOffice(response);
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
        adapter = new ArrayAdapterofficesDetail(this, R.layout.detail_off_list_item, officesDetails, this);
        lvdetail.setAdapter(adapter);

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////
    //when click send form show this popup
    private void showformpopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(OfficeDetail.this);

        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.office_popup, null);

        builder.setView(dialogView);


        imageviewfo = dialogView.findViewById(R.id.image);
        formcamera = dialogView.findViewById(R.id.fcamera);
        sendform = dialogView.findViewById(R.id.sendform);

        final AlertDialog dialog = builder.create();

        sendform.setOnClickListener(v ->
        {
            asyncsendform();
            Toast.makeText(OfficeDetail.this, "فرم ارسال شد.", Toast.LENGTH_LONG).show();
dialog.dismiss();
        });

        formcamera.setOnClickListener(v -> showFileChooser());
        // Display the custom alert dialog on interface
        dialog.show();

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);

        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void asyncsendform() {

        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = pref.getString("last__id", "0");

        if (id.equals("0")) {
            finish();
            return;
        }

        //Converting Bitmap to String
        String image = getStringImage(fobitmap);


        MyHttpUtils.RequestData requestData = new MyHttpUtils.RequestData(UPLOAD_URL, "POST");
        requestData.setParameter("image", image);
        Log.d("asfimage", image);
        requestData.setParameter("type", "leader");
        requestData.setParameter("id", String.valueOf(id));
        Log.d("id :", String.valueOf(id));
        new OfficeDetail.MyTask().execute(requestData);

        alertbox();
    }

    private void alertbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("تصویر شما اپلود شد.");
        builder.setPositiveButton("باشه!", (dialog, which) -> {

        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void alertboxsubmit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("اطلاعات بازرسی ثبت شد.");
        builder.setPositiveButton("باشه!", (dialog, which) -> {

        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri ffilePath = data.getData();

            try {


                fobitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), ffilePath);

                imageviewfo.setImageBitmap(fobitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////


    //add top of activity color
    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar2));
        }
    }

    //add font


    @Override
    public void onClick(int i, int pos, boolean isChecked) {
        bytes[i - 1] = isChecked;
        explain = exp.getText().toString().trim();
        System.out.println("exp:" + explain);
        createUrl = new CreateUrl(id, App.G.officesdetail.getOfficetype(), App.G.officesdetail.getEconomic(),
                App.G.officesdetail.getInsurance(), App.G.officesdetail.getAddress(), App.G.officesdetail.getPhone(), URLEncoder.encode(exp.getText().toString()));

        createUrl.append(getid(), officesDetails.get(pos).getOfficetype(), officesDetails.get(pos).getEconomic(),
                officesDetails.get(pos).getInsurance(), officesDetails.get(pos).getPhone(), officesDetails.get(pos).getAddress(), URLEncoder.encode(exp.getText().toString()),
                bytes[0], bytes[1], bytes[2], bytes[3], bytes[4]);


        createUrl.apply();


    }

}

