package iso.my.com.inspectionstudentorganization;

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
import iso.my.com.inspectionstudentorganization.Models.Office;
import iso.my.com.inspectionstudentorganization.Models.OfficeList;
import iso.my.com.inspectionstudentorganization.OfficeLists.OfficeAdapter;
import iso.my.com.inspectionstudentorganization.OfficeLists.OfficeUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainMenuOffice extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ImageButton back;
    public static final String URI_OFFLIST = "http://sns.tehranedu.ir/ws/InspectorofficeList.aspx";
    public static final String URI_LOG = "http://sns.tehranedu.ir/ws/InspectorLogin.aspx";
    TextView tv_name;
    ImageButton exit, iconsupport, iconcard;
    LinearLayout lysupp, lycard;
    SharedPreferences pref;
    DriverData driverData = null;
    String name, family, mobile, region, pcode, image, desc, schname, schid, status;
    int id;
    boolean st;
    List<OfficeList> officeList;
    ListView listViewoffice;
    SwipeRefreshLayout swipeRefreshLayout;
    Office office = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_office);
        pref = getSharedPreferences("myprefs", MODE_PRIVATE);


        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ppp", "mainoffice");
        editor.apply();

        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        Log.i("mm", pref.getString("ppp", "22")); ;

        set();
        changeStatusBarColor();
        cardviewclick();
        ShowUserData(URI_LOG);
        ShowInsData(URI_OFFLIST);
        init();
        getoffices();
    }

    private void set() {


        tv_name = findViewById(R.id.username);
        listViewoffice = findViewById(R.id.lvrequstlist);

        exit = findViewById(R.id.btnexit);
        exit.setOnClickListener(v -> {
////            SharedPreferences.Editor editor = pref.edit();
////            editor.clear();
////            editor.commit();
//            Intent intent=new Intent(getApplicationContext(),LoginUser.class);
//            startActivity(intent);

            Intent intent=new Intent(getApplicationContext(),ChooseApk.class);
            startActivity(intent);
        });
    }

    //get mellicode with sharepref
    private String getusername() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", null);
    }

    private String getpass() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("password", null);
    }

    private void cardviewclick() {


        View.OnClickListener onClickSupport = view ->
        {
            Intent i = new Intent(getApplicationContext(), SupportOfficeActivity.class);
            startActivity(i);
        };
        lysupp = findViewById(R.id.lysupport);
        lysupp.setOnClickListener(onClickSupport);
        iconsupport = findViewById(R.id.imgbsupport);
        iconsupport.setOnClickListener(onClickSupport);

        View.OnClickListener onClickHistory = view ->
        {
            Intent i = new Intent(getApplicationContext(), IdentifyOfficeCard.class);

            startActivity(i);
        };

        lycard = findViewById(R.id.lycard);

        lycard.setOnClickListener(onClickHistory);
        iconcard = findViewById(R.id.imgcard);
        iconcard.setOnClickListener(onClickHistory);

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
                getoffices();
                ShowInsData(URI_OFFLIST);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }, error -> error.printStackTrace());
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void showdata() {
        if (driverData != null) {
            tv_name.setText(driverData.getName() + " " + driverData.getFamily() + " - " + "منطقه " + driverData.getRegion());

        }
    }

    private void ShowInsData(String url) {

        String dataurl = URI_OFFLIST + "?id=" + id;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, dataurl, response -> {
            try {

                Log.e("nas", response);
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    schid = jsonObject.getString("ID");
                    schname = jsonObject.getString("com_name");
                    status = jsonObject.getString("Status");
                  //  st = jsonObject.getBoolean("st");

                  office = new Office(schid, schname, status);

                    SharedPreferences.Editor editor = pref.edit();
                    // editor.clear();
                    editor.putString("sid", schid);
                    editor.putString("schname", schname);
                    editor.putString("status", status);
                    editor.putString("region", region);
                    // editor.commit();
                    // editor.clear();
                    editor.apply();
                    //   Log.i("nasnaas", "schid :" + schid + "schname:" + "status:" + status + "region" + region);
                    System.out.println("sid :" + schid + "schname:" + "status:" + status + "region" + region);
                    //    Toast.makeText(MainMenu.this, "schid :" + schid + "schname:" + "status:" + status + "region" + region, Toast.LENGTH_LONG).show();

                }


            } catch (Exception e) {
                e.printStackTrace();

            }
        }, error -> error.printStackTrace());


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void init() {

        officeList = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);
    }


    private void getoffices() {
        System.out.println(URI_OFFLIST + "?id=" + id);
        JsonArrayRequest request = new JsonArrayRequest(
                URI_OFFLIST + "?id=" + id,
                response ->
                {
                    swipeRefreshLayout.setRefreshing(false);
                    officeList = OfficeUtils.jsonArrayToOfficeList(response);
                    updateDisplay();
                },

                error ->
                {
            /*
                    Snackbar sb = Snackbar.make(
                            linearLayout, "مشکل در بارگزاری اطلاعات", Snackbar.LENGTH_SHORT);
                    sb.getView().setBackgroundColor(Color.RED);
                    sb.show();*/
                }
        );
        swipeRefreshLayout.setRefreshing(true);
        App.getRequestQueue().add(request);
    }

    @Override
    public void onRefresh ()
    {
        getoffices();
    }


    private void updateDisplay() {


        OfficeAdapter adapter = new OfficeAdapter(this, R.layout.off_list_item, officeList);
        listViewoffice.setAdapter(adapter);
        /*listViewschools.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println("item1");
            // Intent intent = new Intent(getApplicationContext(), SchoolDetail.class);
            //  String message = "abc";
            //intent.putExtra(EXTRA_MESSAGE, message);
            //   startActivity(intent);
            Log.i("nasnaas","item2");
            Toast.makeText(MainMenu.this, "position:", Toast.LENGTH_LONG).show(););*/

    }

    //set color when toolbar is change color
    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar2));
        }
    }

    //set font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

}
