package iso.my.com.inspectionstudentorganization.OfficeLists;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


import iso.my.com.inspectionstudentorganization.Models.OfficeList;
import iso.my.com.inspectionstudentorganization.OfficeDetail;
import iso.my.com.inspectionstudentorganization.R;

import static android.content.Context.MODE_PRIVATE;

public class OfficeAdapter extends ArrayAdapter {

    private Activity activity;
    private int res;
    private List<OfficeList> schools;
    private TextView txtSchoolName;
    private ImageButton imgBtn;
    private ImageView btnmore;

    private More more;
    SharedPreferences pref;


    public OfficeAdapter(@NonNull Activity activity, int resource, List<OfficeList> schoolList) {
        super(activity, resource, schoolList);
        this.activity = activity;
        res = resource;
        this.schools = schoolList;



    }

    @SuppressLint("ViewHolder")
    @NonNull

    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = activity.getLayoutInflater().inflate(res, null);
        setTools(view);
        OfficeList school = schools.get(position);
        setValue(school);
        //  setOnClick(position);
        onClick(schools.get(position).getId(), position);
        return view;
    }

    private void onClick(int id, int position) {
        btnmore.setOnClickListener(v -> moreonclick(id, position));
    }

    private void moreonclick(int id, int position) {

        String url = "http://sns.tehranedu.ir/ws/Inspectoroffice.aspx"
                + "?id=" + id;
        System.out.println(url);
        more = new More(() ->
        {
            afterClicked(id, position);
            return null;
        }, url);

    }

    private void afterClicked(int id, int index) {
        assert more != null;

        Intent intent = new Intent(activity, OfficeDetail.class);
        intent.putExtra("id", id);


         pref = this.activity.getSharedPreferences("myprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("last__id", Integer.toString(id));
        editor.commit();


        activity.startActivity(intent);
        notifyDataSetChanged();

    }

    private void setTools(View view) {
        txtSchoolName = view.findViewById(R.id.officename);
        btnmore = view.findViewById(R.id.btnmore);

    }

    private void setValue(OfficeList school) {
        txtSchoolName.setText(school.getName());
    }

}
