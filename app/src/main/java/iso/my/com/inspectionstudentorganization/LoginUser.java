package iso.my.com.inspectionstudentorganization;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginUser extends AppCompatActivity {

    //login user test:?username=22&password=123456
    public static final String URI_LOGIN = "http://sns.tehranedu.ir/ws/InspectorLogin.aspx";
    SharedPreferences pref;
    String username, password, p1;
    EditText ed_username, ed_password;
    ImageButton login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_design);
        set();
        changeStatusBarColor();
        openPreActivity();
        checkLogin();

    }

    private void set() {

        ed_username = findViewById(R.id.txtusername);
        ed_password = findViewById(R.id.txtpass);
        ed_password.setTypeface(Typeface.DEFAULT);
        ed_password.setTransformationMethod(new PasswordTransformationMethod());


        login = findViewById(R.id.btnlogin);
        login.setOnClickListener(v -> checkempty());
    }

    private void checkLogin() {
        //if the user is already logged in we will directly start the profile activity
        if (App.getInstance().isLoggedIn()) {
            finish();
            Log.i("mm", "hey this is checking login");
            startActivity(new Intent(this, ChooseApk.class));
            return;
        }
    }

    private void checkempty() {
        username = ed_username.getText().toString().trim();
        password = ed_password.getText().toString().trim();
        if (username.isEmpty()) {
            Toast.makeText(getApplicationContext(), "نام کاربری را وارد کنید.", Toast.LENGTH_LONG).show();
        } else if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "رمز عبور را وارد کنید.", Toast.LENGTH_LONG).show();
        } else {
            getData();
        }
        getData();

    }

    private void saveusername(String mellicode) {
        SharedPreferences.Editor editor = getSharedPreferences("myprefs", MODE_PRIVATE).edit();
        editor.putString("username", mellicode);
        editor.apply();
    }

    private void savepass(String password) {
        SharedPreferences.Editor editor = getSharedPreferences("myprefs", MODE_PRIVATE).edit();
        editor.putString("password", password);
        editor.apply();

    }

    private void openPreActivity() {
        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        p1 = pref.getString("ppp", "0");

        Log.i("mm", "hey current state is " + p1);

        if (p1.equals("main")) {

            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(intent);

            Log.i("mm", "running main");

        } else if (p1.equals("mainoffice")) {

            Intent intent = new Intent(getApplicationContext(), MainMenuOffice.class);
            startActivity(intent);

            Log.i("mm", "running mainoffice");

        } else if (p1.equals("mainuniform")) {

            Intent intent = new Intent(getApplicationContext(), MainMenuUniForm.class);
            startActivity(intent);

            Log.i("mm", "running mainuniform");


        }
        else if (p1.equals("mainprouniform")) {

            Intent intent = new Intent(getApplicationContext(), MainMenuProductUniForm.class);
            startActivity(intent);

            Log.i("mm", "running mainprouniform");


        }
        else if (p1.equals("mainbuffet")) {

            Intent intent = new Intent(getApplicationContext(), MainMenuBuffet.class);
            startActivity(intent);

            Log.i("mm", "running mainbuffet");


        }
        else if (p1.equals("detail")) {
            System.out.println("detail1");
            Intent intent = new Intent(getApplicationContext(), SchoolDetail.class);
            Log.i("mm", p1);

            Log.i("mm", "hey I am runnig detail");

            startActivity(intent);


        } else {
            Log.i("mm", "very first excution !!! ");

        }


    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        System.out.println(URI_LOGIN
                + "?username=" + username
                + "&password=" + password);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URI_LOGIN
                + "?username=" + username
                + "&password=" + password
                , response ->
        {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);


                    if (!jsonObject.optString("FName").isEmpty() || !jsonObject.optString("FName").equals(null)) {
                        Intent intent = new Intent(this, ChooseApk.class);
                        Log.i("mm", "hey this is get data activity");
                        startActivity(intent);

                        saveusername(username);
                        savepass(password);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
            progressDialog.dismiss();
        }, error -> Toast.makeText(getApplicationContext(), "اطلاعات نا معتبر است ، لطفا مجدد تلاش کنید.", Toast.LENGTH_LONG).show());
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }
}
