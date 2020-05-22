package iso.my.com.inspectionstudentorganization.GeneralClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;


public class GetInfo {

    private boolean taken;

    private SendInfoToServer sendInfoToServer;
    private Callable<Void> callable;
    private String url, code, pass;
    public static final String
            KEY_JSON__PIC = "Image",
KEY_NAME="FName",
    KEY_FAMILY="LName",
    KEY_CODE="PersonnelCode",
    KEY_REGION="Region",
    KEY_DESC="Description";


    private Map<String, String> userInfo;

    public GetInfo(Callable<Void> afterGet, String url, String code, String pass) {
        callable = afterGet;
        this.url = url;
        this.code = code;
        this.pass = pass;
        get();
    }

    private void get() {
        Map<String, String> param = new LinkedHashMap<>();

        String NAME_KEY = "username";
        String PASS_KEY = "password";

        param.put(NAME_KEY, code);
        System.out.println("code:::"+code);
        param.put(PASS_KEY, pass);
        System.out.println("pass:::"+pass);
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
            String pic,desc,name,family,code,region;
            jsonObject = new JSONObject(jsonArray.getString(0));
            pic = jsonObject.getString(KEY_JSON__PIC);
            desc = jsonObject.getString(KEY_DESC);
            name = jsonObject.getString(KEY_NAME);
            family = jsonObject.getString(KEY_FAMILY);
            code = jsonObject.getString(KEY_CODE);
            region = jsonObject.getString(KEY_REGION);

            userInfo.put(KEY_JSON__PIC, pic);
            userInfo.put(KEY_NAME,name );
            userInfo.put(KEY_FAMILY, family);
            userInfo.put(KEY_CODE, code);
            userInfo.put(KEY_DESC, desc);
            userInfo.put(KEY_REGION, region);

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
