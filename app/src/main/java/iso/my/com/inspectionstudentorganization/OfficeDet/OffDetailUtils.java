package iso.my.com.inspectionstudentorganization.OfficeDet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.Models.OfficesDetail;


public class OffDetailUtils {
    public static ArrayList<OfficesDetail> jsonArrayToDetailOffice(JSONArray jsonArray) {

        ArrayList<OfficesDetail> officeLists = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                OfficesDetail office = new OfficesDetail();


                App.G.officesdetail.setOfficename(json.getString("com_name"));
              //  App.G.officesdetail.setOfficeid(json.getString("com_id"));
                App.G.officesdetail.setOfficetype(json.getString("edari"));
                App.G.officesdetail.setInsurance(json.getString("inscode"));
                App.G.officesdetail.setEconomic(json.getString("ecocode"));
                App.G.officesdetail.setPhone(json.getString("phone"));
                App.G.officesdetail.setAddress(json.getString("address"));
                App.G.officesdetail.setName(json.getString("name"));
                App.G.officesdetail.setLastname(json.getString("lastname"));
                App.G.officesdetail.setSubno(json.getString("com_register"));

                //web sevice key show az in code estafedeh kon
               App.G.officesdetail.setIsselected_type(json.getBoolean("ch_type"));
                App.G.officesdetail.setIsselected_ins(json.getBoolean("ch_inscode"));
                App.G.officesdetail.setIsselected_address(json.getBoolean("ch_address"));
                App.G.officesdetail.setIsselected_ecocode(json.getBoolean("ch_ecocode"));
                App.G.officesdetail.setIsselected_phone(json.getBoolean("ch_phone"));

             //   App.G.officesdetail.setIsselected_type(true);
//                App.G.officesdetail.setIsselected_ins(true);
//                App.G.officesdetail.setIsselected_address(true);
//                App.G.officesdetail.setIsselected_ecocode(true);
//                App.G.officesdetail.setIsselected_phone(true);

//                String chtype =  App.G.officesdetail.setIsselected_type(json.getBoolean("ch_type")) ;


//                OfficeInspectionType inspectionType = OfficeInspectionType.getTypeFromId(Integer.parseInt(chtype));
//                App.G.officesdetail.setOfficeInspectionType(inspectionType);

                officeLists.add(App.G.officesdetail);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return officeLists;
    }

}
