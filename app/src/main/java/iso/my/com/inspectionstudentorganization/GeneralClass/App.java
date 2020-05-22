package iso.my.com.inspectionstudentorganization.GeneralClass;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import iso.my.com.inspectionstudentorganization.LoginUser;
import iso.my.com.inspectionstudentorganization.Models.OfficesDetail;
import iso.my.com.inspectionstudentorganization.Models.SchoolsDetail;
import iso.my.com.inspectionstudentorganization.Models.User;

import iso.my.com.inspectionstudentorganization.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class App extends Application {

    private static final String SHARED_PREF_NAME = "sharedpref";
    private static final String KEY_USERNAME = "user";
    private static final String KEY_PASS = "pass";

    private static Context context;
    private static App instance;
    private static RequestQueue requestQueue;

    @Override
    public void onCreate() {
        G.setContext(getApplicationContext());
        super.onCreate();

        instance = this;

//        CalligraphyConfig.initDefault(
//                new CalligraphyConfig.Builder()
//                        .setDefaultFontPath("fonts/Yekan.ttf")
//                        .setFontAttrId(R.attr.fontPath)
//                        .build()
//        );

        context = getApplicationContext();

    }

    public static synchronized App getInstance() {
        return instance;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_PASS, user.getPass());
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_PASS, null)

        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        context.startActivity(new Intent(context, LoginUser.class));
    }

    public static synchronized RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(instance.getApplicationContext());
        }
        return requestQueue;
    }

    public static Context getContext() {
        return context;
    }

    public static class G {
        private static Activity activity;
        private static Context context;
        public static SchoolsDetail schoolsdetail=new SchoolsDetail();

        public  static OfficesDetail officesdetail=new OfficesDetail();

        public static Activity getActivity() {
            return activity;
        }

        public static void setActivity(Activity activity) {
            G.activity = activity;
        }

        public static Context getContext() {
            return context;
        }

        public static void setContext(Context context) {
            G.context = context;
        }
    }

}
