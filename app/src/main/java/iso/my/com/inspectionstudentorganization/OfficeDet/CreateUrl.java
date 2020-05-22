package iso.my.com.inspectionstudentorganization.OfficeDet;

import java.util.LinkedHashMap;
import java.util.Map;

public class CreateUrl {
    private String MAIN_URL = "http://sns.tehranedu.ir/ws/inspectorofficesubmit.aspx?";

    public final String
            KEY_ID = "id=",
            KEY_TYPE = "type=",
            KEY_ECOCODE = "ecocode=",
            KEY_INSCODE = "inscode=",
            KEY_ADDRESS = "address=",
            KEY_PHONE = "phone=",
    KEY_EXP = "exp=";

    public String url;
    public String Id;
    public Map<String, Boolean> request;
    String type, ecocode, inscode, phone, address,exp;

    int len;
    StringBuilder tempUrl = new StringBuilder();

    public CreateUrl(String Id, String type, String ecocode, String inscode, String add, String phone,String exp) {

        this.Id = Id;
        this.type = type;
        this.ecocode = ecocode;
        this.inscode = inscode;
        this.address = add;
        this.phone = phone;
        this.exp = exp;

        request = new LinkedHashMap<>();
    }

    public void append(String id, String type, String ecocode, String inscode, String phone, String address,String exp,
                       boolean st1, boolean st2, boolean st3, boolean st4, boolean st5) {

        this.Id = KEY_ID + id + "&";
        this.type = KEY_TYPE +st1 + "&";
        this.ecocode = KEY_ECOCODE+ st2 + "&";
        this.inscode = KEY_INSCODE + st3 + "&";
        this.phone = KEY_PHONE + st4 + "&";
        this.address = KEY_ADDRESS  + st5+"&";
        this.exp = KEY_EXP  +exp;
    }

    public void apply() {
        requests();
        url = MAIN_URL + tempUrl.toString();
    }

    public String requests() {

        tempUrl.append(Id).append(type).append(ecocode).append(inscode).append(phone).append(address).append(exp);
        return tempUrl.toString();
    }

//    public String requests() {
//
////        boolean trueOrFalse;
////
////        String octans = URLEncoder.encode("^", "UTF-8");
////
////        for (Map.Entry<String, Boolean> entry : request.entrySet()) {
////
////            trueOrFalse = entry.getValue();
////
////            tempUrl.append(KEY_ID)
////                    .append("=")
////                    .append(URLEncoder.encode(Id))
////                    .append("&")
////                    .append(KEY_TYPE)
////                    .append("=")
////                    .append(URLEncoder.encode(type))
////                    .append(octans)
////                    .append(trueOrFalse)
////                    .append("&")
////                    .append(KEY_ECOCODE)
////                    .append("=")
////                    .append(ecocode)
////                    .append(octans)
////                    .append(trueOrFalse)
////                    .append("&")
////                    .append(KEY_INSCODE)
////                    .append("=")
////                    .append(URLEncoder.encode(inscode))
////                    .append(octans)
////                    .append(trueOrFalse)
////                    .append("&")
////                    .append(KEY_PHONE)
////                    .append("=")
////                    .append(URLEncoder.encode(phone))
////                    .append(octans)
////                    .append(trueOrFalse)
////                    .append("&")
////                    .append(KEY_ADDRESS)
////                    .append("=")
////                    .append(URLEncoder.encode(address))
////                    .append(octans)
////                    .append(trueOrFalse);
////
////      }
//
//       return tempUrl.toString();
//    }

    public String getUrl() {
        return url;
    }
}
