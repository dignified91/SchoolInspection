package iso.my.com.inspectionstudentorganization.GeneralClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;


public class GetInfoSchool {

    private boolean taken;

    private SendInfoToServer sendInfoToServer;
    private Callable<Void> callable;
    private String url, code;
    public static final String
            KEY_JSON__NAME="name",
    KEY_JSON__SCHID="SchoolID",
    KEY_JSON__REGION="region";


    private Map<String, String> userInfo;

    public GetInfoSchool(Callable<Void> afterGet, String url, String code) {
        callable = afterGet;
        this.url = url;
        this.code = code;

        get();
    }

    private void get() {
        Map<String, String> param = new LinkedHashMap<>();

        String NAME_KEY = "id";

        param.put(NAME_KEY, code);
        sendInfoToServer = new SendInfoToServer(url, param, () ->
        {
            afterGet();
            return null;
        });
    }

    private void afterGet() throws Exception {
        taken = (sendInfoToServer != null && sendInfoToServer.getResultSend() == SendInfoToServer.SEND_AND_RESULT_GET);

        if (isTaken()) {
            try {
                spliteInfo(new JSONArray(sendInfoToServer.getResultGet()));
            } catch (JSONException je) {
                taken = false;
            }
        }
        callable.call();
    }

    private void spliteInfo(JSONArray jsonArray) throws JSONException {
        if (jsonArray.length() > 0) {
            this.userInfo = new LinkedHashMap<>();

            JSONObject jsonObject;
            String name,region,schid;
            jsonObject = new JSONObject(jsonArray.getString(0));
            name = jsonObject.getString(KEY_JSON__NAME);
            region = jsonObject.getString(KEY_JSON__REGION);
            schid = jsonObject.getString(KEY_JSON__SCHID);
            userInfo.put(KEY_JSON__NAME, name);
            userInfo.put(KEY_JSON__SCHID, schid);
            userInfo.put(KEY_JSON__REGION, region);
        } else taken = false;
    }

    public boolean isTaken() {
        return taken;
    }

    public String getUsersInfo(String key) {
        try {
            return userInfo.get(key);
        } catch (Exception e) {
            return "";
        }
    }
}
