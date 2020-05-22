package iso.my.com.inspectionstudentorganization.GeneralClass;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class MyHttpUtils {

    public static String inputStreamToString(InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDataHttpClient(String uri) {   // needs  INTERNET permission
        HttpClient client = new DefaultHttpClient();
        HttpGet method = new HttpGet(uri);
        try {
            HttpResponse response = client.execute(method);
            String content = MyHttpUtils.inputStreamToString(response.getEntity().getContent());
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDataHttpUrlConnection(RequestData requestData) {
        String uri = requestData.getUri();
        if ("GET".equals(requestData.getMethod()) && !requestData.getParams().isEmpty()) {
            uri += "?" + requestData.getEncodedParams();
        }
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(requestData.getMethod());
            if ("POST".equals(requestData.getMethod()) && !requestData.getParams().isEmpty()) {
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(requestData.getEncodedParams());
                writer.flush();
                writer.close();
            }
            String result = inputStreamToString(con.getInputStream());
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    ///
    public static String encodeParameters(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        try {
            for (String key : params.keySet()) {
                String value = params.get(key);
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(URLEncoder.encode(value, "UTF-8"));
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static class RequestData {
        private String uri = "";
        private String method = "GET";
        private Map<String, String> params;

        public RequestData() {
            this("", "GET");
        }

        public RequestData(String uri, String method) {
            this.uri = uri;
            this.method = method;
            params = new HashMap<>();
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public Map<String, String> getParams() {
            return params;
        }

        public void setParams(Map<String, String> params) {
            if (params == null) {
                this.params = new HashMap<>();
            }
            this.params = params;
        }

        public void setParameter(String key, String value) {
            if (this.params == null) {
                this.params = new HashMap<>();
            }
            this.params.put(key, value);
        }

        public String getEncodedParams() {
            StringBuilder sb = new StringBuilder();
            try {
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    if (sb.length() > 0) {
                        sb.append("&");
                    }
                    sb.append(key);
                    sb.append("=");
                    sb.append(URLEncoder.encode(value, "UTF-8"));
                }
                return sb.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }

    }

}
