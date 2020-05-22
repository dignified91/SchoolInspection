package iso.my.com.inspectionstudentorganization.SchoolBuffetList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iso.my.com.inspectionstudentorganization.Models.School;
import iso.my.com.inspectionstudentorganization.SchoolList.InspectionType;


public class SchoolsUtils {
    public static ArrayList<School> jsonArrayToRequestList(JSONArray jsonArray) {

        ArrayList<School> schools = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                School school = new School();

                try {
                    school.setId(json.getInt("ID"));
                }
                catch (JSONException ex) {
                    school.setId(1);
                }
                school.setName(json.getString("name"));

           //  String status =  json.optString("Status");
                String status =  json.optString("statusbuf");
                InspectionType inspectionType = InspectionType.getTypeFromId(Integer.parseInt(status));
                 school.setInspectionType(inspectionType);

                schools.add(school);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return schools;
    }

}
