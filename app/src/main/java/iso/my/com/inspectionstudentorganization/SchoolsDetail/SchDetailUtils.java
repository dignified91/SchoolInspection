package iso.my.com.inspectionstudentorganization.SchoolsDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.Models.SchoolsDetail;

public class SchDetailUtils
{
    public static ArrayList<SchoolsDetail> jsonArrayToDetailSchool (JSONArray jsonArray)
    {
        ArrayList<SchoolsDetail> schdet = new ArrayList<>();
        try
        {
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject json = jsonArray.getJSONObject(i);
                SchoolsDetail listhis = new SchoolsDetail();


              //  listhis.setImportant(true);

                try
                {
                    App.G.schoolsdetail.setSchoolname(json.getString("name"));
                }
                catch (JSONException ex)
                {
                    App.G.schoolsdetail.setSchoolname("no");
                }
                listhis.setSchoolname(json.getString("name"));
                listhis.setRegion(json.getString("region"));
                listhis.setGender(json.getString("gender"));
                listhis.setType(json.getString("type"));
             //   listhis.setLat(json.getDouble("lat"));
             listhis.setKrooki(json.getString("krooki"));
                listhis.setExp(json.getString("exp"));
                schdet.add(listhis);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return schdet;
    }

}
