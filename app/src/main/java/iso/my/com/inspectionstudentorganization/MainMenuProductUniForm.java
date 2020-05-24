package iso.my.com.inspectionstudentorganization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.Models.DriverData;
import iso.my.com.inspectionstudentorganization.Models.ProForm;
import iso.my.com.inspectionstudentorganization.Models.ProForms;
import iso.my.com.inspectionstudentorganization.ProductUniForm.ProductUtils;
import iso.my.com.inspectionstudentorganization.ProductUniForm.ProductsAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainMenuProductUniForm extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String URI_PROFORMLIST = "http://sns.tehranedu.ir/ws/InspectorLebasList.aspx";
    public static final String URI_LOG = "http://sns.tehranedu.ir/ws/InspectorLogin.aspx";
    TextView tv_name;
    ImageButton exit, iconsupport, iconcard;
    LinearLayout lysupp, lycard;
    SharedPreferences pref;
    DriverData driverData = null;
    String name, family, mobile, region, manager, pcode, image, desc, schname, schid, status;
    Boolean st;
    int id;
    List<ProForm> proForms;
    ListView listViewschools;
    SwipeRefreshLayout swipeRefreshLayout;
    ProForms proFormss = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_uni_product_form);
        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ppp", "mainprouniform");
        editor.apply();

        ShowUserData(URI_LOG);
        ShowInsData(URI_PROFORMLIST);

        set();
        changeStatusBarColor();
        cardviewclick();
        init();
        getschools();

    }

    protected void onResume() {
        super.onResume();
        ShowUserData(URI_LOG);
        ShowInsData(URI_PROFORMLIST);
      /*  init();
        getschools();
        updateDisplay();*/
    }

    private void set() {

        tv_name = findViewById(R.id.username);
        listViewschools = findViewById(R.id.lvrequstlist);

        exit = findViewById(R.id.btnback);
        exit.setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), ChooseApk.class);
            startActivity(intent);
        });

    }

    private void cardviewclick() {


        View.OnClickListener onClickSupport = view ->
        {
            Intent i = new Intent(getApplicationContext(), SupportProUniFormActivity.class);
            startActivity(i);
        };
        lysupp = findViewById(R.id.lysupport);
        lysupp.setOnClickListener(onClickSupport);
        iconsupport = findViewById(R.id.imgbsupport);
        iconsupport.setOnClickListener(onClickSupport);

        View.OnClickListener onClickHistory = view ->
        {
            Intent i = new Intent(getApplicationContext(), IdentifyProUniFormCard.class);

            startActivity(i);
        };

        lycard = findViewById(R.id.lycard);

        lycard.setOnClickListener(onClickHistory);
        iconcard = findViewById(R.id.imgcard);
        iconcard.setOnClickListener(onClickHistory);

    }

    private String getusername() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", null);
    }

    private String getpass() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("password", null);
    }

    private void ShowUserData(String url) {

        String dataurl = URI_LOG + "?username=" + getusername() + "&password=" + getpass();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, dataurl, response -> {
            try {

                Log.e("nas", response);
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    id = jsonObject.getInt("ID");
                    name = jsonObject.getString("FName");
                    family = jsonObject.getString("LName");
                    mobile = jsonObject.getString("Mobile");
                    region = jsonObject.getString("Region");
                    pcode = jsonObject.getString("PersonnelCode");
                    image = jsonObject.getString("Image");
                    desc = jsonObject.getString("Description");

                    driverData = new DriverData(String.valueOf(id), name, family, mobile, region, pcode, image, desc);

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("id", String.valueOf(id));
                    editor.putString("name", name);
                    editor.putString("family", family);
                    editor.putString("mobile", mobile);
                    editor.putString("region", region);
                    editor.putString("pcode", pcode);
                    editor.putString("image", image);
                    editor.putString("desc", desc);
                    editor.apply();

                    Log.i("nasnaas", "id:" + id + "name:" + name
                            + "family:" + family + "mobile:" + mobile + "region"
                            + region + "pcode" + pcode + "image" + image + "desc" + desc);
                }

                showdata();
                getschools();
                ShowInsData(URI_PROFORMLIST);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }, Throwable::printStackTrace);
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    @SuppressLint("SetTextI18n")
    private void showdata() {
        if (driverData != null) {
            tv_name.setText(driverData.getName() + " " + driverData.getFamily() + " - " + "منطقه " + driverData.getRegion());

        }
    }

    private void ShowInsData(String url) {

        String dataurl = URI_PROFORMLIST + "?id=" + id;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, dataurl, response -> {
            try {

                Log.e("nas", response);
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    schid = jsonObject.getString("ID");
                    schname = jsonObject.getString("name");
                    status = jsonObject.getString("Status");
                    manager = jsonObject.getString("manager");
                    st = jsonObject.getBoolean("st");

                    proFormss = new ProForms(schid, schname, status, manager, st);

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("sid", schid);
                    editor.putString("schname", schname);
                    editor.putString("status", status);
                    editor.putString("manager", manager);

                    editor.apply();
                    //   Log.i("nasnaas", "schid :" + schid + "schname:" + "status:" + status + "region" + region);
                    System.out.println("sid :" + schid + "schname:" + "status:" + status + "manager" + manager);
                    //    Toast.makeText(MainMenu.this, "schid :" + schid + "schname:" + "status:" + status + "region" + region, Toast.LENGTH_LONG).show();

                }


            } catch (Exception e) {
                e.printStackTrace();

            }
        }, Throwable::printStackTrace);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void init() {

        proForms = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void getschools() {
        System.out.println(URI_PROFORMLIST + "?id=" + id);
        JsonArrayRequest request = new JsonArrayRequest(
                URI_PROFORMLIST + "?id=" + id,
                response ->
                {
                    swipeRefreshLayout.setRefreshing(false);
                    proForms = ProductUtils.jsonArrayToProductList(response);
                    updateDisplay();
                },

                error ->
                {

                }
        );
        swipeRefreshLayout.setRefreshing(true);
        App.getRequestQueue().add(request);
    }

    @Override
    public void onRefresh() {
        getschools();
    }

    private void updateDisplay() {

        ProductsAdapter adapter = new ProductsAdapter(this, R.layout.pro_list_item, proForms);
        listViewschools.setAdapter(adapter);

    }

    //set color when toolbar is change color
    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar4));
        }
    }

    //set font


}
