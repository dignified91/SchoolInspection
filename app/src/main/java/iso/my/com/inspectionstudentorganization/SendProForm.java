package iso.my.com.inspectionstudentorganization;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SendProForm extends AppCompatActivity {

    ImageButton back;
    String URI_PRODUCTFORM = "http://sns.tehranedu.ir/ws/lebasform.aspx";
    SharedPreferences pref;
    int id;
    RadioButton btny1, btnn1, btny2, btnn2, btny3, btnn3, btny4, btnn4, btny5, btnn5, btny6, btnn6, btny7, btnn7, rg;
    String form1, form2, form3, form4, form5, form6, form7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_pro_form);

        set();
        changeStatusBarColor();
    }

    private void set() {

        //=====================================================================
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbarsendform);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Yekan.ttf");
        toolbar_title.setTypeface(face);
        //====================================================================

        back = findViewById(R.id.back);
        Button submit = findViewById(R.id.btnsub);
        submit.setOnClickListener(v -> SendData());
        back.setOnClickListener(v -> onBackPressed());


        btny1 = findViewById(R.id.yes1);
        btnn1 = findViewById(R.id.no1);
//        btny1.setChecked(false);
//        btnn1.setChecked(false);

        btny2 = findViewById(R.id.yes2);
        btnn2 = findViewById(R.id.no2);
//        btny2.setChecked(false);
//        btnn2.setChecked(false);

        btny3 = findViewById(R.id.yes3);
        btnn3 = findViewById(R.id.no3);
//        btny3.setChecked(false);
//        btnn3.setChecked(false);

        btny4 = findViewById(R.id.yes4);
        btnn4 = findViewById(R.id.no4);
//        btny4.setChecked(false);
//        btnn4.setChecked(false);

        btny5 = findViewById(R.id.yes5);
        btnn5 = findViewById(R.id.no5);
//        btny5.setChecked(false);
//        btnn5.setChecked(false);

        btny6 = findViewById(R.id.yes6);
        btnn6 = findViewById(R.id.no6);
//        btny6.setChecked(false);
//        btnn6.setChecked(false);

        btny7 = findViewById(R.id.yes7);
        btnn7 = findViewById(R.id.no7);
//        btny7.setChecked(false);
//        btnn7.setChecked(false);

        final RadioGroup group1 =  findViewById(R.id.radio_group1);
        group1.setOnCheckedChangeListener((radioGroup, i) -> {

            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton =  findViewById(selectedId);

            form1 = radioButton.getText().toString();

        });

        //
        final RadioGroup group2 = findViewById(R.id.radio_group2);
        group2.setOnCheckedChangeListener((radioGroup, i) -> {
            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = findViewById(selectedId);

            form2 = radioButton.getText().toString();

        });

        //
        final RadioGroup group3 = findViewById(R.id.radio_group3);
        group3.setOnCheckedChangeListener((radioGroup, i) -> {
            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = findViewById(selectedId);

            form3 = radioButton.getText().toString();

        });

        //
        final RadioGroup group4 = findViewById(R.id.radio_group4);
        group4.setOnCheckedChangeListener((radioGroup, i) -> {
            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = findViewById(selectedId);

            form4 = radioButton.getText().toString();

        });
        //
        final RadioGroup group5 = findViewById(R.id.radio_group5);
        group5.setOnCheckedChangeListener((radioGroup, i) -> {
            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = findViewById(selectedId);

            form5 = radioButton.getText().toString();

        });

        //
        final RadioGroup group6 = findViewById(R.id.radio_group6);
        group6.setOnCheckedChangeListener((radioGroup, i) -> {
            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = findViewById(selectedId);

            form6 = radioButton.getText().toString();

        });

        //
        final RadioGroup group7 = findViewById(R.id.radio_group7);
        group7.setOnCheckedChangeListener((radioGroup, i) -> {
            // get selected radio button from radioGroup
            int selectedId = radioGroup.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            RadioButton radioButton = findViewById(selectedId);

            form7 = radioButton.getText().toString();

        });

    }

    private void alertbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("فرم شما ارسال شد .");
        builder.setPositiveButton("باشه!", (dialog, which) ->
        {
            Intent intent = new Intent(getApplicationContext(), CompleteinspectionProduct.class);
            startActivity(intent);
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    private void SendData() {
        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = Integer.parseInt(pref.getString("last_id", "0"));

        if (id == 0) {
            finish();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);


        System.out.println(URI_PRODUCTFORM
                + "?id=" + id
                + "&f1=" + URLEncoder.encode(form1)
                + "&f2=" + URLEncoder.encode(form2)
                + "&f3=" + URLEncoder.encode(form3)
                + "&f4=" + URLEncoder.encode(form4)
                + "&f5=" + URLEncoder.encode(form5)
                + "&f6=" + URLEncoder.encode(form6)
                + "&f7=" + URLEncoder.encode(form7));

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                URI_PRODUCTFORM
                        + "?id=" + id
                        + "&f1=" + URLEncoder.encode(form1)
                        + "&f2=" + URLEncoder.encode(form2)
                        + "&f3=" + URLEncoder.encode(form3)
                        + "&f4=" + URLEncoder.encode(form4)
                        + "&f5=" + URLEncoder.encode(form5)
                        + "&f6=" + URLEncoder.encode(form6)
                        + "&f7=" + URLEncoder.encode(form7)
                , response ->
        {

            for (int i = 0; i < response.length(); i++) {
                try {

                    JSONObject jsonObject = response.getJSONObject(i);

                    if (jsonObject.optString("status").equals("1")) {
                        //  Toast.makeText(getApplicationContext(), " اطلاعات با موفقیت ثبت شد.", Toast.LENGTH_LONG).show();

                        alertbox();


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
            window.setStatusBarColor(getResources().getColor(R.color.toolbar4));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

}
