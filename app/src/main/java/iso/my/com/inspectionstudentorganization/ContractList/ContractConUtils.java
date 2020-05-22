package iso.my.com.inspectionstudentorganization.ContractList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iso.my.com.inspectionstudentorganization.Models.ContractCon;
import iso.my.com.inspectionstudentorganization.Models.School;
import iso.my.com.inspectionstudentorganization.SchoolList.InspectionType;


public class ContractConUtils {
    public static ArrayList<ContractCon> jsonArrayToContract(JSONArray jsonArray) {

        ArrayList<ContractCon> schools = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ContractCon school = new ContractCon();


                school.setGcode(json.getString("gcode"));
                school.setOfcode(json.getString("ofcode"));
                school.setSchcode(json.getString("schcode"));

                school.setDate(json.getString("date"));
                school.setRegion(json.getInt("region"));
                school.setSchname(json.getString("schname"));
                school.setOffname(json.getString("offname"));
                school.setPayment(json.getString("payment"));

                schools.add(school);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return schools;
    }

}
