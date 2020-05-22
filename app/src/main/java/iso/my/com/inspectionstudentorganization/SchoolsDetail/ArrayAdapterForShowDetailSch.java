package iso.my.com.inspectionstudentorganization.SchoolsDetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iso.my.com.inspectionstudentorganization.Models.Address;
import iso.my.com.inspectionstudentorganization.Models.SchoolsDetail;
import iso.my.com.inspectionstudentorganization.R;


public class ArrayAdapterForShowDetailSch extends ArrayAdapter {
    private Activity activity;
    private int res;
    private List<SchoolsDetail> schoolsDetailList;
    private TextView tvname, tvregion, tvaddress, tvexp,tvgender,tvtype;
    private String uurl = "https://api.neshan.org/v2/reverse";
    Address address = null;
    private String formatted_address, neighbourhood, city, state, municipality_zone, in_traffic_zone, in_odd_even_zone;
    String sp22, sp44;

    public ArrayAdapterForShowDetailSch(@NonNull Activity activity, int resource, List<SchoolsDetail> det) {
        super(activity, resource, det);
        this.activity = activity;
        res = resource;
        this.schoolsDetailList = det;


    }

    @SuppressLint("ViewHolder")
    @NonNull

    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = activity.getLayoutInflater().inflate(res, null);
        setTools(view);
        SchoolsDetail his = schoolsDetailList.get(position);
        setValue(his);
        senddatatoserverlocationstart(his);
        return view;
    }

    private void setTools(View view) {
        tvname = view.findViewById(R.id.schoolname);
        tvregion = view.findViewById(R.id.region);
        tvtype = view.findViewById(R.id.type);
        tvgender = view.findViewById(R.id.gender);
        tvaddress = view.findViewById(R.id.schaddress);
        tvexp = view.findViewById(R.id.schexp);
    }

    private void setValue(SchoolsDetail schList) {
        tvname.setText(schList.getSchoolname());
        tvregion.setText("منطقه " + schList.getRegion());
        tvexp.setText(schList.getExp());
        tvaddress.setText(schList.getKrooki());

        StringBuilder sb = new StringBuilder();
        String[] separated = tvaddress.getText().toString().split(",");
        String sp2 = separated[0];
         sp22 = sp2.replace("(", "");
        String sp4 = separated[1];
         sp44 = sp4.replace(")", "");
        sb.append(sp22).append(sp44.replace(")", ""));

        System.out.println("lat :" + sp22);
        System.out.println("lon :" + sp44);

        tvgender.setText(schList.getGender());

        if (schList.getGender().equals("true"))
        {
            tvgender.setText("دخترانه");
        }
        else if (schList.getGender().equals("false"))
        {
            tvgender.setText("پسرانه");
        }

        tvtype.setText(schList.getType());

        if (schList.getType().equals("1"))
        {
            tvtype.setText("پیش دبستانی");
        }
        else if (schList.getType().equals("2"))
        {
            tvtype.setText("دبستان(ابتدایی)");
        }
        else if (schList.getType().equals("3"))
        {
            tvtype.setText("متوسطه عمومی");
        }
        else if (schList.getType().equals("4"))
        {
            tvtype.setText("متوسطه دوره اول");
        }
        else if (schList.getType().equals("5"))
        {
            tvtype.setText("هنرستان فنی");
        }
        else if (schList.getType().equals("6"))
        {
            tvtype.setText("بازرگانی و حرفه ای");
        }
        else if (schList.getType().equals("7"))
        {
            tvtype.setText("کاردانش");
        }else if (schList.getType().equals("8"))
        {
            tvtype.setText("پیش حرفه ای استثنائی");
        }
        else if (schList.getType().equals("9"))
        {
            tvtype.setText("کودکستان استثنائی");
        }
        else if (schList.getType().equals("10"))
        {
            tvtype.setText("استثنائی");
        }
        else if (schList.getType().equals("11"))
        {
            tvtype.setText("متوسط حرفه ای استثنائی");
        }
        else if (schList.getType().equals("12"))
        {
            tvtype.setText("متوسط استثنائی");
        }
        else if (schList.getType().equals("13"))
        {
            tvtype.setText("متوسط دوره اول استثنائی");
        }
        else if (schList.getType().equals("14"))
        {
            tvtype.setText("فنی استثنائی");
        }else if (schList.getType().equals("15"))
        {
            tvtype.setText("ابتدایی دوره اول");
        }
        else if (schList.getType().equals("16"))
        {
            tvtype.setText("ابتدایی دوره دوم");
        }


    }

    private void senddatatoserverlocationstart(SchoolsDetail missionDetail) {
        String url = uurl + "?lat=" + sp22 + "&lng=" + sp44.trim();
        String Api_Key = "service.0rQa2hB6MbyPsnUSBTCYZP1V4P2wQ7tQzj1CBDIn";

        System.out.println("url is :" + url);
        RequestQueue requestQueue = Volley.newRequestQueue(activity.getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response ->
        {
            try {
                Log.d("nas", response);
                JSONObject jsonObject = new JSONObject(response);

                neighbourhood = jsonObject.getString("neighbourhood");
                formatted_address = jsonObject.getString("formatted_address");
                municipality_zone = jsonObject.getString("municipality_zone");
                in_traffic_zone = jsonObject.getString("in_traffic_zone");
                in_odd_even_zone = jsonObject.getString("in_odd_even_zone");
                city = jsonObject.getString("city");
                state = jsonObject.getString("state");
                address = new Address(neighbourhood, formatted_address, municipality_zone,
                        in_traffic_zone, in_odd_even_zone, city, state);


                System.out.println("adress is " +
                        "formatted_address :" + formatted_address +
                        "neighbourhood :" + neighbourhood +
                        "city :" + city +
                        "state :" + state +
                        "municipality_zone :" + municipality_zone +
                        "in_traffic_zone :" + in_traffic_zone +
                        "in_odd_even_zone :" + in_odd_even_zone);

                setValue(missionDetail);
                setAddressstart();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> error.printStackTrace()) {
            /** Passing some request headers* */
            @Override
            public Map getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Api-Key", Api_Key);
                return headers;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void setAddressstart() {
        tvaddress.setText(address.getFormatted_address());

    }

}