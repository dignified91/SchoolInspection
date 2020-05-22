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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SubmitManufacturer extends AppCompatActivity {

    ImageButton back;
    Button submit;
    EditText ed_factory, ed_distributor, ed_mobile, ed_brand;
    TextView list;
    SharedPreferences pref;
    String URI_BUFFETFACTORY = "http://sns.tehranedu.ir/ws/buf_mb.aspx";
    String factory, distributor, mobile, brand;
    int id, row_number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_manufacturer);
        set();
        changeStatusBarColor();
    }

    private void set() {
        //=====================================================================
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbarsubmanufacture);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Yekan.ttf");
        toolbar_title.setTypeface(face);

        //====================================================================
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

        ed_factory = findViewById(R.id.factoryname);
        ed_distributor = findViewById(R.id.Distributor);
        ed_mobile = findViewById(R.id.mobile);
        ed_brand = findViewById(R.id.brand);

        list = findViewById(R.id.brandlist);

        submit = findViewById(R.id.btnsubmit);
        submit.setOnClickListener(view -> {

            factory = ed_factory.getText().toString().trim();
            distributor = ed_distributor.getText().toString().trim();
            mobile = ed_mobile.getText().toString().trim();
            brand = ed_brand.getText().toString().trim();


            if (factory.isEmpty())
                Toast.makeText(App.G.getContext(), "لطفا نام شرکت را وارد کنید", Toast.LENGTH_LONG).show();

            else if (distributor.isEmpty())
                Toast.makeText(App.G.getContext(), "لطفا نام توزیع کننده را وارد کنید", Toast.LENGTH_LONG).show();

            else if (mobile.isEmpty())
                Toast.makeText(App.G.getContext(), "لطفا موبایل توزیع کننده را وارد کنید", Toast.LENGTH_LONG).show();

            else if (brand.isEmpty())
                Toast.makeText(App.G.getContext(), "لطفا برند شرکت توزیع کننده را وارد کنید", Toast.LENGTH_LONG).show();

            else {
                Toast.makeText(getApplicationContext(), "اطلاعات شرکت توزیع کننده ثبت شد.", Toast.LENGTH_LONG).show();
                getdata();

            }
        });

    }


    private void getdata() {
        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = Integer.parseInt(pref.getString("last_id", "0"));

        if (id == 0) {
            finish();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);


        factory = ed_factory.getText().toString().trim();
        distributor = ed_distributor.getText().toString().trim();
        mobile = ed_mobile.getText().toString().trim();
        brand = ed_brand.getText().toString().trim();


        System.out.println(URI_BUFFETFACTORY
                + "?ins_id=" + id
                + "&factory=" + URLEncoder.encode(factory)
                + "&distributor=" + URLEncoder.encode(distributor)
                + "&mobile=" + mobile
                + "&brand=" + URLEncoder.encode(brand)

        );

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                URI_BUFFETFACTORY
                        + "?ins_id=" + id
                        + "&factory=" + URLEncoder.encode(factory)
                        + "&distributor=" + URLEncoder.encode(distributor)
                        + "&mobile=" + mobile
                        + "&brand=" + URLEncoder.encode(brand)
                , response ->
        {


            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);

                    if (jsonObject.optString("status").equals("1")) {

                        Toast.makeText(getApplicationContext(), " اطلاعات با موفقیت ثبت شد.", Toast.LENGTH_LONG).show();

                        if (row_number == 0) {
                            list.setVisibility(View.VISIBLE);
                            row_number++;
                            list.setText(row_number + "-  " + factory + "  -  " + brand);
                        } else if (row_number > 0) {
                            row_number++;
                            String books_list_items = list.getText().toString();
                            list.setText(books_list_items + " \n" + row_number + "-  " + factory + "  -  " + brand);
                        }
                        String empty_text_view = "";
                        ed_factory.setText(empty_text_view);
                        ed_mobile.setText(empty_text_view);
                        ed_distributor.setText(empty_text_view);
                        ed_brand.setText(empty_text_view);


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
