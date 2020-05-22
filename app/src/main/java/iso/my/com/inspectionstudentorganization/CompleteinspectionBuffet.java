package iso.my.com.inspectionstudentorganization;

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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import iso.my.com.inspectionstudentorganization.SchoolsDetail.EndIns;
import iso.my.com.inspectionstudentorganization.GeneralClass.GPSTracker;
import iso.my.com.inspectionstudentorganization.SchoolsDetail.EndIns;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CompleteinspectionBuffet extends AppCompatActivity {

    ImageButton back;
    SharedPreferences pref;
    public EndIns endIns;
    private String endurl;
    int id;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_inspection_buffet);
        changeStatusBarColor();
        set();
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

        //=====================================================================
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbarcompleteins);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Yekan.ttf");
        toolbar_title.setTypeface(face);

        //====================================================================
    }

    private void set() {

        View.OnClickListener onClickSendForm = view -> {
            ///  Toast.makeText(this, "Send Form", Toast.LENGTH_LONG).show();

            Intent i = new Intent(getApplicationContext(), SendFormValuation.class);
            startActivity(i);
        };

        LinearLayout lysendform = findViewById(R.id.lyform);
        lysendform.setOnClickListener(onClickSendForm);

        ImageButton sendform = findViewById(R.id.btnsendform);
        sendform.setOnClickListener(onClickSendForm);

        //
        View.OnClickListener onClickSendError = view -> {
            //    Toast.makeText(this, "Send Error", Toast.LENGTH_LONG).show();

            Intent i = new Intent(getApplicationContext(), SendErrorBuffet.class);
            startActivity(i);
        };

        LinearLayout lysenderror = findViewById(R.id.lyerror);
        lysenderror.setOnClickListener(onClickSendError);

        ImageButton senderror = findViewById(R.id.btnerror);
        senderror.setOnClickListener(onClickSendError);

        //
        View.OnClickListener onClickCheckPrice = view -> {
            //   Toast.makeText(this, "Check Price", Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), SubmitBuffetData.class);
            startActivity(i);
        };

        LinearLayout lycheck = findViewById(R.id.lysubmitbuffet);
        lycheck.setOnClickListener(onClickCheckPrice);

        ImageButton check = findViewById(R.id.btnsubmitbuffet);
        check.setOnClickListener(onClickCheckPrice);

        //
        View.OnClickListener onClickCheckscan = view -> {

            Intent i = new Intent(getApplicationContext(), SubmitManufacturer.class);
            startActivity(i);
        };

        LinearLayout lyscan = findViewById(R.id.lysubmitfactory);
        lyscan.setOnClickListener(onClickCheckscan);

        ImageButton scan = findViewById(R.id.btnsubmitfactory);
        scan.setOnClickListener(onClickCheckscan);

        //
        View.OnClickListener onClickEndIns = view -> {
            setlocation();
            // Toast.makeText(this, "end ins", Toast.LENGTH_LONG).show();
            pref = getSharedPreferences("myprefs", MODE_PRIVATE);
            id = Integer.parseInt(pref.getString("last_id", "-1"));

            Log.i("mm", "hey id of end click is " + id);
            endurl = "http://sns.tehranedu.ir/ws/RecordStatusbuf.aspx"
                    + "?id=" + id + "&lat=" + lat + "&lnt=" + lon + "&type=" + "e";

            System.out.println(endurl);

            endIns = new EndIns(() ->
            {
                Intent i = new Intent(getApplicationContext(), MainMenuBuffet.class);
                startActivity(i);
                return null;
            }, endurl);

        };

        LinearLayout lyend = findViewById(R.id.lyend);
        lyend.setOnClickListener(onClickEndIns);

        ImageButton end = findViewById(R.id.btnend);
        end.setOnClickListener(onClickEndIns);


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {

                //  Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

            } else {

                System.out.println("uri:"+result.getContents());
                Uri uri = Uri.parse(result.getContents()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);


                //  getData();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setlocation() {
        GPSTracker gpstracker = new GPSTracker(this);

        if (gpstracker.canGetLocation()) {
            lat = gpstracker.getLatitude();
            lon = gpstracker.getLongitude();
//            Toast.makeText(this,
//                    "Location is \n lat : " + lat + "\n lon : " + lon,
            //  Toast.LENGTH_SHORT).show();
        } else {
            gpstracker.showGpsAlertDialog();
        }

    }


    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar5));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
}
