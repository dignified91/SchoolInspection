package iso.my.com.inspectionstudentorganization.ProUniFormDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.Models.ProFormDetail;


public class ProDetailUtils
{
    public static ArrayList<ProFormDetail> jsonArrayToDetailPro (JSONArray jsonArray)
    {
        ArrayList<ProFormDetail> schdet = new ArrayList<>();
        try
        {
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject json = jsonArray.getJSONObject(i);
                ProFormDetail listhis = new ProFormDetail();

                try
                {
                    App.G.schoolsdetail.setSchoolname(json.getString("name"));
                }
                catch (JSONException ex)
                {
                    App.G.schoolsdetail.setSchoolname("no");
                }
                listhis.setProname(json.getString("name"));
                listhis.setManager(json.getString("manager"));
                listhis.setPhone(json.getString("phone"));
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
