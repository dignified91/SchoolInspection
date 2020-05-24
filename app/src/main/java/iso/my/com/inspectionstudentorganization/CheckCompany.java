package iso.my.com.inspectionstudentorganization;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import iso.my.com.inspectionstudentorganization.CompanyOfSchool.ArrayAdapterschoffDet;
import iso.my.com.inspectionstudentorganization.CompanyOfSchool.SchOffUtils;
import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.Models.OfficeDetSchool;
import iso.my.com.inspectionstudentorganization.SchoolsDetail.EndIns;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CheckCompany extends AppCompatActivity {

    ImageButton back;
    Button check, submit_error;
    EditText ed_office;
    SharedPreferences pref;
    String URI_CHECKCOMPANY = "http://sns.tehranedu.ir/ws/checkleader.aspx";
    String leadname, result, endurl;
    LinearLayout linearLayout;
    List<OfficeDetSchool> officesDetail;
    ArrayAdapterschoffDet adapter;
    int id;
    //SchOffAdapter adapter;

    RecyclerView recyclerView;
    ActionMode.Callback actionModeCallback;
    ListView listView;
    public EndIns endIns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_company);
        set();
        changeStatusBarColor();
    }

    private void set() {
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

        //=====================================================================
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbarcheckcompany);

        //====================================================================
        ed_office = findViewById(R.id.txtname_or_subcode);

        linearLayout = findViewById(R.id.llchoff);
        linearLayout.setVisibility(View.INVISIBLE);

        check = findViewById(R.id.checktrue);
        check.setOnClickListener(v -> getData());

        listView = findViewById(R.id.listview);

        submit_error = findViewById(R.id.seterror);
        submit_error.setOnClickListener(v -> {
            pref = getSharedPreferences("myprefs", MODE_PRIVATE);
            id = Integer.parseInt(pref.getString("last_id", "-1"));

            String desc = "تخلف عدم بکارگیری پیمانکار مجاز";

            Log.i("mm", "hey id of end click is " + id);
            endurl = "http://sns.tehranedu.ir/ws/InsertInspectorReports.aspx"
                    + "?id=" + id + "&inf_id=" + "9" + "&type=" + "2" + "&des=" + URLEncoder.encode(desc);

            System.out.println("error price:" + endurl);

            endIns = new EndIns(() ->
            {
                showalertok();

                return null;
            }, endurl);
        });


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

    private void getData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        leadname = ed_office.getText().toString().trim();

        System.out.println("uri_lead :" + URI_CHECKCOMPANY
                + "?lead=" + URLEncoder.encode(leadname));

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URI_CHECKCOMPANY
                + "?lead=" + URLEncoder.encode(leadname)

                , response ->
        {

            for (int i = 0; i < response.length(); i++) {


                try {
                    JSONObject jsonObject = response.getJSONObject(i);
                    result = jsonObject.getString("status");
                    if (result.equals("1")) {
                        //  Toast.makeText(getApplicationContext(), "شرکت پیمانکاری وجود دارد.", Toast.LENGTH_LONG).show();

                        linearLayout.setVisibility(View.VISIBLE);

                        init();
                        getlist();

//                        name = jsonObject.getString("name");
//                        insno = jsonObject.getString("insno");
//                        econo = jsonObject.getString("econo");
//                        tel = jsonObject.getString("tel");
//                        typeoff = jsonObject.getString("type");
//                        addressoff = jsonObject.getString("address");

                    } else if (result.isEmpty()){
                       // Toast.makeText(getApplicationContext(), "شرکت پیمانکاری وجود ندارد.", Toast.LENGTH_LONG).show();
                          alertbox();
                    }


                } catch (JSONException matin) {
                   // System.out.println(matin);

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

    private void alertbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("شرکت پیمانکاری وجود ندارد.");
        builder.setPositiveButton("باشه!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  Intent intent = new Intent(getApplicationContext(), Menu.class);
                //   startActivity(intent);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void init() {
        officesDetail = new ArrayList<>();

//        recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//
//                Intent intent = new Intent(getApplicationContext(), CheckCompany.class);
//
//                pref = getSharedPreferences("mypref", MODE_PRIVATE);
//                SharedPreferences.Editor eeditor = pref.edit();
//                eeditor.putString("lead", officesDetail.get(position).getName());
//                eeditor.apply();
//                eeditor.commit();
//
//                System.out.println("lead is" + officesDetail.get(position).getName());
//                startActivity(intent);
//                startActivity(intent);
//                //  Toast.makeText(getApplicationContext(), usersList.get(position).getId() + " is selected!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));
//
//
//        actionModeCallback = new ActionMode.Callback() {
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
//
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//
//                return false;
//
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//
//
//                recyclerView.post(() -> {
//
//                });
//            }
//
//        };
    }

    private void getlist() {

        leadname = ed_office.getText().toString().trim();
        System.out.println(URI_CHECKCOMPANY + "?lead=" + URLEncoder.encode(leadname));
        JsonArrayRequest request = new JsonArrayRequest(
                URI_CHECKCOMPANY + "?lead=" + URLEncoder.encode(leadname),
                response ->
                {
                    officesDetail = SchOffUtils.jsonArrayToCheckDetailOffice(response);
                    updateDisplay();

                },

                error ->
                {

                }
        );


        request.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.getRequestQueue().add(request);
    }

    private void updateDisplay() {

        //  if (schoolsdetail.size() == 0) notFound();
        adapter = new ArrayAdapterschoffDet(this, R.layout.detail_check_off_item, officesDetail);
//        adapter=new SchOffAdapter(officesDetail, (SchOffAdapter.MessageAdapterListenerr) this);
        listView.setAdapter(adapter);

    }

    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar));
        }
    }


}
