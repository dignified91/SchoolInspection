package iso.my.com.inspectionstudentorganization.OfficeDet;

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

import iso.my.com.inspectionstudentorganization.Models.OfficesDetail;
import iso.my.com.inspectionstudentorganization.R;


public class ArrayAdapterofficesDetail extends ArrayAdapter {
    private Activity activity;
    private int res;
    private List<OfficesDetail> officesDetails;
    private TextView tv_officename, tv_insurance, tv_economic, tv_phone, tv_officetype, tv_address, tv_managername, tv_subno;
//    public static List<CheckBox> checkBoxArr;
    CheckBox checkBox_type, checkBox_phone, checkBox_address, checkBox_ins, checkBox_eco;
    public ClickCheckBox clickCheckBox;

    public ArrayAdapterofficesDetail(@NonNull Activity activity, int resource, List<OfficesDetail> det, ClickCheckBox clickCheckBox) {
        super(activity, resource, det);
        this.activity = activity;
        res = resource;
        this.officesDetails = det;
        this.clickCheckBox = clickCheckBox;

    }

    @SuppressLint("ViewHolder")
    @NonNull

    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = activity.getLayoutInflater().inflate(res, null);
        setTools(view, position);
        OfficesDetail his = officesDetails.get(position);
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
        tv_managername = view.findViewById(R.id.manager);
        tv_subno = view.findViewById(R.id.subno);

        checkBox_type = view.findViewById(R.id.checktype);
        checkBox_phone = view.findViewById(R.id.checkphone);
        checkBox_address = view.findViewById(R.id.checkaddress);
        checkBox_ins = view.findViewById(R.id.checkins);
        checkBox_eco = view.findViewById(R.id.checkeco);

        checkBox_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                     @Override
                                                     public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                         clickCheckBox.onClick(1,Position, isChecked);
                                                     }
                                                 }
        );


        checkBox_eco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        clickCheckBox.onClick(2,Position, isChecked);
                                                    }
                                                }
        );
        checkBox_ins.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        clickCheckBox.onClick(3,Position, isChecked);
                                                    }
                                                }
        );
        checkBox_phone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                      @Override
                                                      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                          clickCheckBox.onClick(4,Position, isChecked);
                                                      }
                                                  }
        );
        checkBox_address.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                        @Override
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            clickCheckBox.onClick(5,Position, isChecked);
                                                        }
                                                    }
        );

    }

    private void setValue(OfficesDetail officesDetail) {

        //HashMap<String, String> hashMap = new HashMap<String, String>();

        //  hashMap.put("tv_officename", new Integer(1));

        tv_insurance.setText(officesDetail.getInsurance());
        tv_economic.setText(officesDetail.getEconomic());
        tv_phone.setText(officesDetail.getPhone());
        tv_address.setText(officesDetail.getAddress());
        tv_officetype.setText(officesDetail.getOfficetype());
        tv_subno.setText(officesDetail.getSubno());
        tv_managername.setText(officesDetail.getName() + " " + officesDetail.getLastname());


        checkBox_type.setChecked(officesDetail.getIsselected_type());
        checkBox_phone.setChecked(officesDetail.getIsselected_phone());
        checkBox_ins.setChecked(officesDetail.getIsselected_ins());
        checkBox_address.setChecked(officesDetail.getIsselected_address());
        checkBox_eco.setChecked(officesDetail.getIsselected_ecocode());


//        if (officesDetail.getOfficeInspectionType().equals(OfficeInspectionType.Type)) {
//            checkBox_type.setChecked(officesDetail.getIsselected_type());
//            //   checkBox.setVisibility(View.INVISIBLE);
//
//        }
//        checkBoxArr.add(checkBox_type);
//        checkBoxArr.add(checkBox_phone);
//        checkBoxArr.add(checkBox_ins);
//        checkBoxArr.add(checkBox_address);
//        checkBoxArr.add(checkBox_eco);


    }


}