package iso.my.com.inspectionstudentorganization.ProductUniForm;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iso.my.com.inspectionstudentorganization.Models.ProForm;
import iso.my.com.inspectionstudentorganization.SchoolList.InspectionType;


public class ProductUtils {
    public static ArrayList<ProForm> jsonArrayToProductList(JSONArray jsonArray) {

        ArrayList<ProForm> proFormArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ProForm pf = new ProForm();

                try {
                    pf.setId(json.getInt("ID"));
                }

                catch (JSONException ex) {
                    pf.setId(1);
                }

                pf.setName(json.getString("name"));

                String status =  json.optString("Status");

                InspectionType inspectionType = InspectionType.getTypeFromId(Integer.parseInt(status));
                pf.setInspectionType(inspectionType);

                proFormArrayList.add(pf);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return proFormArrayList;
    }

}
