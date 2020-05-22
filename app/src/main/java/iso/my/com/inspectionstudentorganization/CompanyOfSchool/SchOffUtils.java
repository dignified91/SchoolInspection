package iso.my.com.inspectionstudentorganization.CompanyOfSchool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iso.my.com.inspectionstudentorganization.Models.OfficeDetSchool;


public class SchOffUtils {
    public static ArrayList<OfficeDetSchool> jsonArrayToCheckDetailOffice(JSONArray jsonArray) {

        ArrayList<OfficeDetSchool> officeLists = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                OfficeDetSchool office = new OfficeDetSchool();

                office.setOfficetype(json.getString("type"));
                office.setInsurance(json.getString("inscode"));
                office.setEconomic(json.getString("ecocode"));
                office.setPhone(json.getString("tel"));
                office.setAddress(json.getString("addr"));
                office.setName(json.getString("name"));

                officeLists.add(office);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return officeLists;
    }

}
