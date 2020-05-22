package iso.my.com.inspectionstudentorganization.OfficeDet;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.Callable;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.GeneralClass.SendInfoToServer;


public class EndIns
{
    private Callable<Void> callable;
    private String url;

    private boolean ended;
    private SendInfoToServer sendInfoToServer;

    public EndIns(Callable<Void> afterSubmit, String url)
    {
        callable = afterSubmit;
        this.url = url;
        endins();
    }

    private void endins ()
    {
        //System.out.print("URL: "+url);
        sendInfoToServer = new SendInfoToServer(url, null, () ->
        {
            if (sendInfoToServer.getResultSend() == SendInfoToServer.SEND_AND_RESULT_GET)
                afterend(sendInfoToServer.getResultGet());
            else Toast.makeText(App.G.getContext(), "خطا", Toast.LENGTH_LONG).show();
            return null;
        });
    }

    private void afterend (String result) throws Exception
    {
        try

        {
            ended = new JSONArray(result).getJSONObject(0).getString("Status").equals("1");
            //endins();
        }
        catch (JSONException e)
        {
            ended = false;
        }
        callable.call();
    }


    public boolean isEnded ()
    {
        return ended;
    }
}
