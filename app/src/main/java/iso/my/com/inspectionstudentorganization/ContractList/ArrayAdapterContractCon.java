package iso.my.com.inspectionstudentorganization.ContractList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import iso.my.com.inspectionstudentorganization.Models.ContractCon;
import iso.my.com.inspectionstudentorganization.OfficeDet.ClickCheckBox;
import iso.my.com.inspectionstudentorganization.R;


public class ArrayAdapterContractCon extends ArrayAdapter {
    private Activity activity;
    private int res;
    private List<ContractCon> officesDetails;
    private TextView  tv_gcode, tv_ofcode, tv_schname, tv_date, tv_schcode, tv_officename, tv_payment;

    public ArrayAdapterContractCon(@NonNull Activity activity, int resource, List<ContractCon> det) {
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
        ContractCon his = officesDetails.get(position);
        setValue(his);
        return view;
    }

    private void setTools(View view, int Position) {

        tv_officename = view.findViewById(R.id.officename);
        tv_date = view.findViewById(R.id.date);
        tv_gcode = view.findViewById(R.id.gcode);
        tv_ofcode = view.findViewById(R.id.number);
        tv_schcode = view.findViewById(R.id.schcode);
        tv_schname = view.findViewById(R.id.schname);
        tv_payment = view.findViewById(R.id.pay);
    }

    private void setValue(ContractCon officesDetail) {
        tv_officename.setText(officesDetail.getOffname());
        tv_date.setText(officesDetail.getDate());
        tv_gcode.setText(officesDetail.getGcode());
        tv_ofcode.setText(officesDetail.getOfcode());
        tv_payment.setText(officesDetail.getPayment());
        tv_schcode.setText(officesDetail.getSchcode());
        tv_schname.setText(officesDetail.getSchname() + "-" +"منطقه"+ officesDetail.getRegion());

    }


}