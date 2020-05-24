package iso.my.com.inspectionstudentorganization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import iso.my.com.inspectionstudentorganization.Models.OfficeDetSchool;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailOfficeSch extends AppCompatActivity {

    ImageButton back;
    ImageView imageView;
    List<OfficeDetSchool> detailModels;
    String URI_CHECKCOMPANY = "http://sns.tehranedu.ir/ws/checkleader.aspx";
    ListView listView;
    private int id;
  //  private ArrayAdapterCheckoff adapter;
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_office_sch);

        changeStatusBarColor();
        setui();

        back = findViewById(R.id.back);
        back.setOnClickListener(v ->{

            Intent intent=new Intent(getApplicationContext(),DetailOfficeSch.class);
            startActivity(intent);
        });

    //    init();
      //  getdetail();


    }

    private void setui() {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbardetailoffsch);


        imageView=findViewById(R.id.image);

        listView = findViewById(R.id.listview);

    }
    //get with sharepref
    private String getiduser() {

        SharedPreferences sharedPreferences = getSharedPreferences("mypref", MODE_PRIVATE);
        return sharedPreferences.getString("lead", null);
    }


//    private void init() {
//        detailModels = new ArrayList<>();
//    }
//
//    private void getdetail() {
//        Intent intent = getIntent();
//        String umobile = intent.getStringExtra("UserMOBILE");
//
//        System.out.println( URI_CHECKCOMPANY+ "?lead=" + getiduser());
//        JsonArrayRequest request = new JsonArrayRequest(
//                URI_CHECKCOMPANY + "?lead=" + getiduser(),
//                response ->
//                {
//                    detailModels = CheckOffDetUtils.jsonArrayToCheckDetailOffice(response);
//                    updateDisplay();
//
//                },
//
//                error ->
//                {
//
//                }
//        );
//
//        App.getRequestQueue().add(request);
//    }
//
//    private void updateDisplay() {
//        if (detailModels.size() == 0) notFound();
//        adapter = new ArrayAdapterCheckoff(this, R.layout.detail_check_off_item, detailModels);
//        listView.setAdapter(adapter);
//    }

    private void notFound() {
        RelativeLayout relativeLayout = findViewById(R.id.relativelayout);

        relativeLayout.removeAllViews();

        relativeLayout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(this);

        imageView.setImageResource(R.drawable.empty);

        //int size = Math.con(100);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);

        imageView.setLayoutParams(params);

        relativeLayout.addView(imageView);
    }


    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar));
        }
    }


}
