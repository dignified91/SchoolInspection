package iso.my.com.inspectionstudentorganization;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import iso.my.com.dlibrary.DatePicker;
import iso.my.com.dlibrary.components.DateItem;
import iso.my.com.dlibrary.interfaces.DateSetListener;
import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.Models.SpType;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SubmitBuffetData extends AppCompatActivity implements DateSetListener {


    //fromDate.getText().toString().trim()
    ImageButton back;
    Button submit;
    TextView fromDate;
    EditText ed_name, ed_family, ed_mobile, ed_dimention1, ed_dimention2, ed_number;
    Spinner typespinner, malekspinner, jobspinner;
    Date mEndDate;
    Date mStartDate;

    private ArrayList<SpType> sptype = new ArrayList<>();
    SharedPreferences pref;

    String URI_BUFFETINSERT = "http://sns.tehranedu.ir/ws/bufinsert.aspx";
    String name, family, mobile, dim1, dim2, number,dimention;
    int id, typeid, malekid, jobid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_buffet_data);
        set();
        changeStatusBarColor();
    }

    private void set() {
        //=====================================================================
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbarbuffetinfo);

        //====================================================================

        back = findViewById(R.id.btnback);
        back.setOnClickListener(v -> onBackPressed());


        setLocale("fa");
        fromDate = findViewById(R.id.date);
        //mEndDate = new Date();
        mStartDate = new Date();
        fromDate.setOnTouchListener((v, event) ->
        {
            int id = v.getId() == R.id.date ? 1 : 2;
            Calendar minDate = Calendar.getInstance();
            Calendar maxDate = Calendar.getInstance();
            maxDate.set(Calendar.YEAR, maxDate.get(Calendar.YEAR) + 10);
            minDate.set(Calendar.YEAR, minDate.get(Calendar.YEAR) - 10);

            DatePicker.Builder builder = new DatePicker.Builder()
                    .id(id)
                    .minDate(minDate)
                    .maxDate(maxDate)
                    .setRetainInstance(true);

            if (v.getId() == R.id.date)
                builder.date(mStartDate.getDay(), mStartDate.getMonth(), mStartDate.getYear());
            else
                builder.date(mEndDate.getCalendar());

            builder.build(SubmitBuffetData.this)
                    .show(getSupportFragmentManager(), "");

            return false;
        });

        ed_name = findViewById(R.id.name);
        ed_family = findViewById(R.id.family);
        ed_mobile = findViewById(R.id.mobile);
        ed_dimention1 = findViewById(R.id.distance1);
        ed_dimention2 = findViewById(R.id.distance2);
        ed_number = findViewById(R.id.number);

        typespinner = findViewById(R.id.typespinner);
        malekspinner = findViewById(R.id.malekspinner);
        jobspinner = findViewById(R.id.jobspinner);


        submit = findViewById(R.id.btnsubmit);
        submit.setOnClickListener(view -> {

            name = ed_name.getText().toString().trim();
            family = ed_family.getText().toString().trim();
            mobile = ed_mobile.getText().toString().trim();
            dim1 = ed_dimention1.getText().toString().trim();
            dim2 = ed_dimention2.getText().toString().trim();
            number = ed_number.getText().toString().trim();

            if (name.isEmpty())
                Toast.makeText(App.G.getContext(), "لطفا نام متصدی را وارد کنید", Toast.LENGTH_LONG).show();

            else if (family.isEmpty())
                Toast.makeText(App.G.getContext(), "لطفا نام خانوادگی متصدی را وارد کنید", Toast.LENGTH_LONG).show();

            else if (mobile.isEmpty())
                Toast.makeText(App.G.getContext(), "لطفا موبایل متصدی را وارد کنید", Toast.LENGTH_LONG).show();
            else if (dim1.isEmpty())
                Toast.makeText(App.G.getContext(), "لطفا طول بوفه را وارد کنید", Toast.LENGTH_LONG).show();
            else if (dim2.isEmpty())
                Toast.makeText(App.G.getContext(), "لطفا عرض بوفه را وارد کنید", Toast.LENGTH_LONG).show();
            else if (number.isEmpty())
                Toast.makeText(App.G.getContext(), "لطفا شماره ثبتی بوفه را وارد کنید", Toast.LENGTH_LONG).show();

            else {
                Toast.makeText(getApplicationContext(), "اطلاعات بوفه ثبت شد.", Toast.LENGTH_LONG).show();
                getdata();


            }
        });

        LoadType();
        LoadMalek();
        LoadJob();

    }

    private void LoadType() {
        final ArrayList<String> type = new ArrayList<>();
        for (int i = 1; i < 2; i++) {

            sptype.add(new SpType("" + (i + 1), "" + (i + 1)));
            type.add(" نوع بوفه...");
            type.add("نوع 1");
            type.add("نوع 2");
            type.add("نوع 3");
            type.add("نوع 4");

        }
        ArrayAdapter typeadapter = new ArrayAdapter(SubmitBuffetData.this, android.R.layout.simple_spinner_dropdown_item, type);
        typespinner.setAdapter(typeadapter);
        typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                typeid = typespinner.getSelectedItemPosition() + 1;
                System.out.println("typeid::" + typeid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void LoadMalek() {
        final ArrayList<String> malek = new ArrayList<>();
        for (int i = 1; i < 2; i++) {

            sptype.add(new SpType("" + (i + 1), "" + (i + 1)));
            malek.add(" نوع مالکیت ...");
            malek.add("اجاره");
            malek.add("مالک ");

        }
        ArrayAdapter malekadapter = new ArrayAdapter(SubmitBuffetData.this, android.R.layout.simple_spinner_dropdown_item, malek);
        malekspinner.setAdapter(malekadapter);
        malekspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                malekid = malekspinner.getSelectedItemPosition() + 1;
                System.out.println("malekid::" + malekid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    private void LoadJob() {
        final ArrayList<String> job = new ArrayList<>();
        for (int i = 1; i < 2; i++) {

            sptype.add(new SpType("" + (i + 1), "" + (i + 1)));
            job.add(" نوع متصدی...");
            job.add("نیروی آزاد");
            job.add("نیروی بازنشسته");
            job.add("نیروی شاغل در آموزش و پرورش");

        }
        ArrayAdapter jobadapter = new ArrayAdapter(SubmitBuffetData.this, android.R.layout.simple_spinner_dropdown_item, job);
        jobspinner.setAdapter(jobadapter);
        jobspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                jobid = jobspinner.getSelectedItemPosition() + 1;
                System.out.println("jobid::" + jobid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


    @Override
    public void onDateSet(int id, @Nullable Calendar calendar, int day, int month, int year) {
        if (id == 1) {
            mStartDate.setDate(day, month, year);
            fromDate.setText(mStartDate.getDate());

        } else {
           // mEndDate.setDate(day, month, year);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale("fa");
    }

    public void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    class Date extends DateItem {
        String getDate() {
            Calendar calendar = getCalendar();
            return String.format(Locale.US,
                    "%d/%d/%d",
                    getYear(), getMonth(), getDay(),
                    calendar.get(Calendar.YEAR),
                    +calendar.get(Calendar.MONTH) + 1,
                    +calendar.get(Calendar.DAY_OF_MONTH));
        }
    }

    private void getdata() {
        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = Integer.parseInt(pref.getString("last_id", "0"));

        if (id == 0) {
            finish();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);

        typeid = typespinner.getSelectedItemPosition();
        malekid = malekspinner.getSelectedItemPosition();
        jobid = jobspinner.getSelectedItemPosition();

        name = ed_name.getText().toString().trim();
        family = ed_family.getText().toString().trim();
        mobile = ed_mobile.getText().toString().trim();
        dim1 = ed_dimention1.getText().toString().trim();
        dim2 = ed_dimention2.getText().toString().trim();
        number = ed_number.getText().toString().trim();

         dimention = URLEncoder.encode(dim1 + "-" + dim2);
        System.out.println(URI_BUFFETINSERT
                + "?ins_id=" + id
                + "&type=" + typeid
                + "&malek=" + malekid
                + "&job=" + jobid
                + "&name=" + URLEncoder.encode(name)
                + "&family=" + URLEncoder.encode(family)
                + "&mobile=" + mobile
                + "&dimention=" + dimention
                + "&number=" + number
        );

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                URI_BUFFETINSERT
                        + "?ins_id=" + id
                        + "&type=" + typeid
                        + "&malek=" + malekid
                        + "&job=" + jobid
                        + "&name=" + URLEncoder.encode(name)
                        + "&family=" + URLEncoder.encode(family)
                        + "&mobile=" + mobile
                        + "&dimention=" + dimention
                        + "&number=" + number, response ->
        {

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);

                    if (jsonObject.optString("status").equals("1")) {
                        Toast.makeText(getApplicationContext(), " اطلاعات با موفقیت ثبت شد.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(), CompleteinspectionBuffet.class);
                        startActivity(intent);

                    } else {

                        Toast.makeText(getApplicationContext(), " ثبت اطلاعات با مشکل مواجه شد.", Toast.LENGTH_LONG).show();

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
            window.setStatusBarColor(getResources().getColor(R.color.toolbar5));
        }
    }




}
