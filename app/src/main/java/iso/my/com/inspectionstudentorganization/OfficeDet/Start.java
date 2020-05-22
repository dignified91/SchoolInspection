package iso.my.com.inspectionstudentorganization.OfficeDet;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Map;
import java.util.concurrent.Callable;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.GeneralClass.SendInfoToServer;

public class Start {

    public Callable<Void> callable;
    public String url;

    public Boolean startedhome;
    public SendInfoToServer sendInfoToServer;
    public Map<String, String> param;

    public Start(Callable<Void> afterStartHome, String url, Map<String, String> params) {
        callable = afterStartHome;
        this.url = url;
        this.param = params;
        starthome();
    }

    public void starthome() {

        sendInfoToServer = new SendInfoToServer(url, param, () ->
        {
            System.out.println("url in start home is :" + url);

            if (sendInfoToServer.getResultSend() == SendInfoToServer.SEND_AND_RESULT_GET)
                afterStartHome(sendInfoToServer.getResultGet());
            else
                Toast.makeText(App.G.getContext(), "خطا", Toast.LENGTH_LONG).show();
            return null;
        });
    }

    public void afterStartHome(String result) throws Exception {
        try {
            startedhome = new JSONArray(result).getJSONObject(0).getString("Status").equals("1");
        } catch (JSONException e) {
            startedhome = false;
        }
        callable.call();
    }

    public boolean isStartedHome() {
        return startedhome;
    }
}
