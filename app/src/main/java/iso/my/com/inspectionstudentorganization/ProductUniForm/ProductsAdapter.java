package iso.my.com.inspectionstudentorganization.ProductUniForm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


import iso.my.com.inspectionstudentorganization.Models.ProForm;
import iso.my.com.inspectionstudentorganization.ProductUniFormDetail;
import iso.my.com.inspectionstudentorganization.R;
import iso.my.com.inspectionstudentorganization.SchoolList.InspectionType;
import iso.my.com.inspectionstudentorganization.SchoolList.More;

import static android.content.Context.MODE_PRIVATE;

public class ProductsAdapter extends ArrayAdapter {

    private Activity activity;
    private int res;
    private List<ProForm> proForms;
    private TextView txtSchoolName;
    private ImageView btnmore;
    public static List<CheckBox> checkBoxArr;
    private CheckBox checkBox;
    private More more;
    SharedPreferences pref;


    public ProductsAdapter(@NonNull Activity activity, int resource, List<ProForm> proForms) {
        super(activity, resource, proForms);
        this.activity = activity;
        res = resource;
        this.proForms = proForms;
        checkBoxArr = new ArrayList<>();
    }

    @SuppressLint("ViewHolder")
    @NonNull

    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = activity.getLayoutInflater().inflate(res, null);
        setTools(view);
        ProForm proFormss = proForms.get(position);
        setValue(proFormss);
        onClick(proForms.get(position).getId(), position);
        return view;
    }

    private void onClick(int id, int position) {
        btnmore.setOnClickListener(v -> moreonclick(id, position));
    }

    private void moreonclick(int id, int position) {

        String url = "http://sns.tehranedu.ir/ws/InspectorlebasList.aspx"
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

        Intent intent = new Intent(activity, ProductUniFormDetail.class);
        intent.putExtra("id", id);


        pref = this.activity.getSharedPreferences("myprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("last_id", Integer.toString(id));
        editor.commit();

        activity.startActivity(intent);
        notifyDataSetChanged();

    }

    private void setTools(View view) {
        txtSchoolName = view.findViewById(R.id.proname);
        btnmore = view.findViewById(R.id.btnmore);
        checkBox = view.findViewById(R.id.btncheck);
    }

    private void setValue(ProForm proForm) {
        txtSchoolName.setText(proForm.getName());

        if (proForm.getInspectionType().equals(InspectionType.END_INSPECTION)) {
            checkBox.setChecked(true);
            btnmore.setVisibility(View.INVISIBLE);
        }
        else
            checkBox.setChecked(false);
            checkBoxArr.add(checkBox);
    }
}
