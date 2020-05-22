package iso.my.com.inspectionstudentorganization;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SendSchoolForm extends AppCompatActivity {

    ImageButton back;
    WebView mwebView;
    String url = "http://sns.tehranedu.ir/regions/inspectorlebas_mob1.aspx";
    int id;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_school_form);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            initFields();
            setListeners();
        }

        set();
        changeStatusBarColor();
    }

    private void set() {

        //=====================================================================
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbarsendschoolform);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Yekan.ttf");
        toolbar_title.setTypeface(face);
        //====================================================================

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());
    }

    public void initFields() {
        // TODO Auto-generated method stub

        mwebView = findViewById(R.id.webview);

        mwebView.getSettings().setJavaScriptEnabled(true);
        mwebView.getSettings().setBuiltInZoomControls(false);
        mwebView.getSettings().setNeedInitialFocus(false);
        mwebView.getSettings().setAllowFileAccess(true);


    }

    public void setListeners() {
        // TODO Auto-generated method stub
        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = Integer.parseInt(pref.getString("last_id", "-1"));
        Log.i("mm", "hey id is " + id);

        String uurl = url + "?ins_id=" + id;
        mwebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                mwebView.loadUrl(uurl);

                view.clearHistory();
            }


        });

        mwebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

            }

        });

        mwebView.loadUrl(uurl);
        System.out.println("uurl:" + uurl);

        final SendSchoolForm.MyJavaScriptInterface myJavaScriptInterface
                = new SendSchoolForm.MyJavaScriptInterface(this);
        mwebView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");


    }

    public class MyJavaScriptInterface {
        Context mContext;

        MyJavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            // webView.loadUrl("javascript:document.getElementById(\"Button3\").innerHTML = \"bye\";");
        }

        @JavascriptInterface
        public void openAndroidDialog() {
            AlertDialog.Builder myDialog
                    = new AlertDialog.Builder(SendSchoolForm.this);
            myDialog.setTitle("DANGER!");

            myDialog.setMessage("You can do what you want!");
            myDialog.setPositiveButton("ON", null);
            myDialog.show();
        }

    }

    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar3));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

}
