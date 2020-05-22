package iso.my.com.inspectionstudentorganization.SchoolBuffetList;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.Callable;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.GeneralClass.SendInfoToServer;


public class More
{
    private Callable<Void> callable;
    private String url;

    private boolean submited;
    private SendInfoToServer sendInfoToServer;

    public More(Callable<Void> afterSubmit, String url)
    {
        callable = afterSubmit;
        this.url = url;
        submit();
    }

    private void submit ()
    {
        sendInfoToServer = new SendInfoToServer(url, null, () ->
        {
            if (sendInfoToServer.getResultSend() == SendInfoToServer.SEND_AND_RESULT_GET)
                afterSubmit(sendInfoToServer.getResultGet());
            else Toast.makeText(App.G.getContext(), "خطا", Toast.LENGTH_LONG).show();
            return null;
        });
    }

    private void afterSubmit (String result) throws Exception
    {
        try
        {
            submited = new JSONArray(result).getJSONObject(0).getString("statuslebas").equals("1");
        }
        catch (JSONException e)
        {
            submited = false;
        }
        callable.call();
    }

    public boolean isSubmited ()
    {
        return submited;
    }
}
