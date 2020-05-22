package iso.my.com.inspectionstudentorganization.GeneralClass;

import android.widget.TextView;

import java.util.StringTokenizer;

/**
 * Created by Asus on 2/3/2018.
 */

public class Text_converter {
    TextView textView;


    public Text_converter(TextView textView) {
        this.textView = textView;
    }

    public void transfer(){

        try
        {
            String value = textView.getText().toString();


            if (value != null && !value.equals(""))
            {

                if(value.startsWith(".")){
                    textView.setText("0.");
                }
//                if(value.startsWith("0") && !value.startsWith("0.")){
//                    textView.setText("");
//
//                }
                String str = textView.getText().toString().replaceAll(",", "");
                if (value.startsWith("-")){
                    str = str.replace("-","");
                    if (!value.equals(""))
                        textView.setText("-"+getDecimalFormattedString(str));
                }else {

                    if (!value.equals(""))
                        textView.setText(getDecimalFormattedString(str));
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    private static String getString(String value){
        try
        {
            String str="";


            if (value != null && !value.equals(""))
            {

                if(value.startsWith(".")){
                    str = "0.";
                }

                str = str.replaceAll(",", "");
                if (!value.equals(""))
                    return getDecimalFormattedString(str);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return value;
        }
        return value;
    }



    public static String getDecimalFormattedString(String value)
    {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1)
        {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt( -1 + str1.length()) == '.')
        {
            j--;
            str3 = ".";
        }
        for (int k = j;; k--)
        {
            if (k < 0)
            {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3)
            {
                str3 = "," + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }

    }

    public static String trimCommaOfString(String string) {
//        String returnString;
        if(string.contains(",")){
            return string.replace(",","");}
        else {
            return string;
        }

    }

}
