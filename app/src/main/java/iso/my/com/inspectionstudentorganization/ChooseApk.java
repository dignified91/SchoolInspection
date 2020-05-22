package iso.my.com.inspectionstudentorganization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChooseApk extends AppCompatActivity {

    ImageButton exit;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_apk);
        set();
        changeStatusBarColor();
    }

    private void set() {

        View.OnClickListener onClickOffice = view -> {
            ///  Toast.makeText(this, "Send Form", Toast.LENGTH_LONG).show();

//           Intent i = new Intent(getApplicationContext(), MainMenuOffice.class);
//           startActivity(i);
            alertbox();
        };

        LinearLayout lyoffice = findViewById(R.id.lyoffice);
        lyoffice.setOnClickListener(onClickOffice);

        ImageButton office = findViewById(R.id.btnoffice);
        office.setOnClickListener(onClickOffice);

        //
        View.OnClickListener onClickSchool = view -> {

            Intent i = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(i);
          //  alertbox();
        };

        LinearLayout lyschool = findViewById(R.id.lyservice);
        lyschool.setOnClickListener(onClickSchool);

        ImageButton school = findViewById(R.id.btnservice);
        school.setOnClickListener(onClickSchool);

        //
        View.OnClickListener onClickUniForm = view -> {
            ///  Toast.makeText(this, "Send Form", Toast.LENGTH_LONG).show();

           // Intent i = new Intent(getApplicationContext(), MainMenuUniForm.class);
          //  startActivity(i);
            alertbox();
        };

        LinearLayout lyuniform = findViewById(R.id.lyuniform);
        lyuniform.setOnClickListener(onClickUniForm);

        ImageButton uniform = findViewById(R.id.btnuniform);
        uniform.setOnClickListener(onClickUniForm);

        //
        View.OnClickListener onClickProduct = view -> {

//            Intent i = new Intent(getApplicationContext(), MainMenuProductUniForm.class);
//            startActivity(i);
            alertbox();

        };

        LinearLayout lyproduct = findViewById(R.id.lyproduct);
        lyproduct.setOnClickListener(onClickProduct);

        ImageButton btnproduct = findViewById(R.id.btnproduct);
        btnproduct.setOnClickListener(onClickProduct);

        //
        View.OnClickListener onClickBuffet = view -> {

           // Intent i = new Intent(getApplicationContext(), MainMenuBuffet.class);
        //    startActivity(i);
      alertbox();
        };

        LinearLayout lybuffet = findViewById(R.id.lybuffet);
        lybuffet.setOnClickListener(onClickBuffet);

        ImageButton btnbuffet = findViewById(R.id.btnbuffet);
        btnbuffet.setOnClickListener(onClickBuffet);

        //
        View.OnClickListener onClickProBuffet = view -> {

//            Intent i = new Intent(getApplicationContext(), SendForm.class);
//            startActivity(i);
            alertbox();
        };

        LinearLayout lyprobuffet = findViewById(R.id.lyprobuffet);
        lyprobuffet.setOnClickListener(onClickProBuffet);

        ImageButton btnprobuffet = findViewById(R.id.btnprobuffet);
        btnprobuffet.setOnClickListener(onClickProBuffet);


        exit = findViewById(R.id.btnexit);
        exit.setOnClickListener(v -> {
            pref = getSharedPreferences("myprefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();

            Intent intent=new Intent(getApplicationContext(),LoginUser.class);
            startActivity(intent);
        });
    }

    private void alertbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("در حال آماده سازی می باشد.");
        builder.setPositiveButton("باشه!", (dialog, which) ->
        {
            //  Intent intent = new Intent(getApplicationContext(), Menu.class);
            //   startActivity(intent);
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    public void onBackPressed()
    {
        return;
    }
    //set color when toolbar is change color
    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar));
        }
    }
    //set font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

}
