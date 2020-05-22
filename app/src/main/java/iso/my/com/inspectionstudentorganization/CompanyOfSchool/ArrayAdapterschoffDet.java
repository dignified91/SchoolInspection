package iso.my.com.inspectionstudentorganization.CompanyOfSchool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import iso.my.com.inspectionstudentorganization.Models.OfficeDetSchool;
import iso.my.com.inspectionstudentorganization.R;


public class ArrayAdapterschoffDet extends ArrayAdapter {
    private Activity activity;
    private int res;
    private List<OfficeDetSchool> officesDetails;
    private TextView tv_officename,tv_insurance,tv_economic,tv_address,tv_phone,tv_officetype;


    public ArrayAdapterschoffDet(@NonNull Activity activity, int resource, List<OfficeDetSchool> det) {
        super(activity, resource, det);
        this.activity = activity;
        res = resource;
        this.officesDetails = det;

    }

    @SuppressLint("ViewHolder")
    @NonNull

    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = activity.getLayoutInflater().inflate(res, null);
        setTools(view, position);
        OfficeDetSchool his = officesDetails.get(position);
        setValue(his);
        return view;
    }

    private void setTools(View view, int Position) {

        tv_officename = view.findViewById(R.id.officename);
        tv_insurance = view.findViewById(R.id.insurance);
        tv_economic = view.findViewById(R.id.economic);
        tv_phone = view.findViewById(R.id.phone);
        tv_address = view.findViewById(R.id.address);
        tv_officetype = view.findViewById(R.id.officetype);
    }

    private void setValue(OfficeDetSchool officesDetail) {

        tv_officename.setText(officesDetail.getName() );
        tv_insurance.setText(officesDetail.getInsurance());
        tv_economic.setText(officesDetail.getEconomic());
        tv_phone.setText(officesDetail.getPhone());
        tv_address.setText(officesDetail.getAddress());
        tv_officetype.setText(officesDetail.getOfficetype());
    }


}