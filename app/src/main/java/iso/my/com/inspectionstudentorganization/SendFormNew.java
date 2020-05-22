package iso.my.com.inspectionstudentorganization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SendFormNew extends AppCompatActivity {

    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_form_new);
        //=====================================================================
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbarsendform);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Yekan.ttf");
        toolbar_title.setTypeface(face);
        //====================================================================

        changeStatusBarColor();

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

        View.OnClickListener onClickSendForm1 = view -> {
            ///  Toast.makeText(this, "Send Form", Toast.LENGTH_LONG).show();

            Intent i = new Intent(getApplicationContext(), ValuationForm_School.class);
            startActivity(i);
        };

        LinearLayout lyform1 = findViewById(R.id.lyform1);
        lyform1.setOnClickListener(onClickSendForm1);

        ImageButton form1 = findViewById(R.id.btnsendform1);
        form1.setOnClickListener(onClickSendForm1);


        View.OnClickListener onClickSendForm2 = view -> {
            ///  Toast.makeText(this, "Send Form", Toast.LENGTH_LONG).show();

            Intent i = new Intent(getApplicationContext(), ValuationForm_Master1.class);
            startActivity(i);
        };

        LinearLayout lyform2 = findViewById(R.id.lyform2);
        lyform2.setOnClickListener(onClickSendForm2);

        ImageButton form2 = findViewById(R.id.btnsendform2);
        form2.setOnClickListener(onClickSendForm2);

        View.OnClickListener onClickSendForm3 = view -> {
            ///  Toast.makeText(this, "Send Form", Toast.LENGTH_LONG).show();

            Intent i = new Intent(getApplicationContext(), ValuationForm_Master2.class);
            startActivity(i);
        };

        LinearLayout lyform3 = findViewById(R.id.lyform3);
        lyform3.setOnClickListener(onClickSendForm3);

        ImageButton form3 = findViewById(R.id.btnsendform3);
        form3.setOnClickListener(onClickSendForm3);


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
