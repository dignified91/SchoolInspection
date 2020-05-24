package iso.my.com.inspectionstudentorganization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class IdentifyCard extends AppCompatActivity {

    public static final String URI_LOGIN = "http://sns.tehranedu.ir/ws/InspectorLogin.aspx";

    TextView name, code, region, exp;
    ImageButton back;
    ImageView imageView;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.identify_card);
        set();
        changeStatusBarColor();

        System.out.println(URI_LOGIN);
    }

    @SuppressLint("SetTextI18n")
    private void set() {

        //=====================================================================
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbaridcard);


        //====================================================================

        name = findViewById(R.id.name);
        name.setText(getname()+" "+getfamily());

        code = findViewById(R.id.code);
        code.setText(getpid());

        region = findViewById(R.id.region);
        region.setText("منطقه"+" "+getregion());

        exp = findViewById(R.id.desc);
        exp.setText(getdesc());

        imageView = findViewById(R.id.image);

        String uriimage = "http://sns.tehranedu.ir/Pictures/InspectorImages/";
        imageView.getDrawable();
        Picasso.get()

                .load(uriimage+getimage())
                .placeholder(R.drawable.ic_user)
                .resize(300, 400)
                .centerCrop()
                .onlyScaleDown() // the image will only be resized if it's bigger than 6000x2000 pixels.
                .into(imageView);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

    }

    private String getname() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("name", null);
    }

    private String getfamily() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("family", null);
    }

    private String getdesc() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("desc", null);
    }

    private String getimage() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("image", null);
    }

    private String getpid() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("pcode", null);
    }

    private String getregion() {

        SharedPreferences sharedPreferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        return sharedPreferences.getString("region", null);
    }


    //set color when toolbar is change color
    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar));
        }
    }

    //set font



}
