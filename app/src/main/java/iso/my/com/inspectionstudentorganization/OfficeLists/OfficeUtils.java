package iso.my.com.inspectionstudentorganization.OfficeLists;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iso.my.com.inspectionstudentorganization.Models.OfficeList;


public class OfficeUtils {
    public static ArrayList<OfficeList> jsonArrayToOfficeList(JSONArray jsonArray) {

        ArrayList<OfficeList> officeLists = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                OfficeList office = new OfficeList();

                try {
                    office.setId(json.getInt("ID"));
                } catch (JSONException ex) {
                    office.setId(1);
                }
                office.setName(json.getString("com_name"));


                officeLists.add(office);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return officeLists;
    }

}
