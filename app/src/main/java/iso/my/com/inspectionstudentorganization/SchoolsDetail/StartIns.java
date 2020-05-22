package iso.my.com.inspectionstudentorganization.SchoolsDetail;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.Callable;

import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.GeneralClass.SendInfoToServer;


public class StartIns
{
    public Callable<Void> callable;
    public String url;

    public boolean starterd;
    public SendInfoToServer sendInfoToServer;

    public StartIns(Callable<Void> afterStart, String url)
    {
        callable = afterStart;
        this.url = url;
        startins();
    }
//http://sns.tehranedu.ir/(S(2k4qmxnxlevfwk1dujsxqfum))/ws/RecordStatusInspector.aspx?id=12&lat=null&lnt=null&type=e
  // http://sns.tehranedu.ir/(S(y2youm2e2ubbrpcbdngs2cky))/ws/RecordStatusInspector.aspx?id=12&lat=35.71280228&lnt=51.33756375&type=e
    public void startins ()
    {
System.out.println(url);
        sendInfoToServer = new SendInfoToServer(url, null, () ->
        {
            if (sendInfoToServer.getResultSend() == SendInfoToServer.SEND_AND_RESULT_GET)
                afterStart(sendInfoToServer.getResultGet());
            else
                Toast.makeText(App.G.getContext(), "خطا", Toast.LENGTH_LONG).show();
            return null;
        });
    }

    private void afterStart (String result) throws Exception
    {
        try
        {
            starterd = new JSONArray(result).getJSONObject(0).getString("Status").equals("1");
        }
        catch (JSONException e)
        {
            starterd = false;
        }

        callable.call();
    }

    public boolean isStarterd ()
    {
        return starterd;
    }
}
