package iso.my.com.inspectionstudentorganization.GeneralClass;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;
import java.util.concurrent.Callable;


public class SendInfoToServer
{
    private String url;
    private Map<String, String> param;
    private Callable<Void> callable;

    public static final int SEND = 1, SEND_AND_RESULT_GET = 2, ERROR = 3;

    private int resultSend;
    private String resultGet = "";

    public SendInfoToServer(String url, Map<String, String> param, Callable<Void> afterGet)
    {
        this.url = url;
        this.param = param;
        callable = afterGet;
        runClass();
    }

    public void runClass ()
    {
      //  makeRequest();
        sendAndGet();
    }

    private void makeRequest ()
    {
        if (this.param != null)
        {
            StringBuilder param = new StringBuilder();
            param.append("?");
            int counter = 0, len = this.param.size();
            for (Map.Entry<String, String> entry : this.param.entrySet())
            {
                param.append(entry.getKey() + "=" + entry.getValue());

                if ((counter++) + 1 < len) param.append("&");
            }
            url += param.toString();
        }
        System.out.println(url);
    }

    private void sendAndGet ()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response ->
        {
            if (response.equals("")) resultSend = SEND;
            else
            {
                resultSend = SEND_AND_RESULT_GET;
                resultGet = response;
            }
            afterGet();
        }, error ->
        {
            resultSend = ERROR;
            afterGet();
        });
        Volley.newRequestQueue(App.getContext()).add(stringRequest);
    }

    private void afterGet ()
    {
        try
        {
            callable.call();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getResultSend ()
    {
        return resultSend;
    }

    public String getResultGet ()
    {
        return resultGet;
    }
}
