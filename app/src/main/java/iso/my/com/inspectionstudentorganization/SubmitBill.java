package iso.my.com.inspectionstudentorganization;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import iso.my.com.dlibrary.DatePicker;
import iso.my.com.dlibrary.components.DateItem;
import iso.my.com.dlibrary.interfaces.DateSetListener;
import iso.my.com.inspectionstudentorganization.GeneralClass.App;
import iso.my.com.inspectionstudentorganization.GeneralClass.MyHttpUtils;
import iso.my.com.inspectionstudentorganization.Models.Price;
import iso.my.com.inspectionstudentorganization.Models.SaveFile;
import iso.my.com.inspectionstudentorganization.utils.FileUtils;

public class SubmitBill extends AppCompatActivity {

    ImageButton back, camera, folder;
    Button upload, senderror;
    ImageView imageViewer;
    TextView tv_billno,tv_billpay,tv_price;
    List<AsyncTask> tasks = new ArrayList<>();
    private String currentPicturePath = "";
    String number,pay,schprice;
    int SELECT_FILE1 = 0;
    public int  id,status;
    SharedPreferences pref;
    String UPLOAD_URL = "http://sns.tehranedu.ir/ws/billimage.aspx";
    String URI_SENDBILL = "http://sns.tehranedu.ir/ws/billsubmit.aspx";//?ins_sch_id=1&inf_id=2"
    String URI_SHOWPRICE = "http://sns.tehranedu.ir/ws/billpayment.aspx";
    EditText fromDate;
    Price price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_bill);
        set();
        changeStatusBarColor();
        ShowUserData(URI_SHOWPRICE);
    }

    private void set() {
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.toolbarsubmitbill);

        back = findViewById(R.id.btnback);
        back.setOnClickListener(v -> onBackPressed());

        tv_price=findViewById(R.id.price);
        tv_billno=findViewById(R.id.numberbill);
        tv_billpay=findViewById(R.id.payment);
        fromDate=findViewById(R.id.date);
        //mEndDate = new Date();

        senderror = findViewById(R.id.btnsubmit);
//        senderror.setOnClickListener(view -> {
//            desc = ed_errorexp.getText().toString().trim();
//
//            if (desc.isEmpty())
//                Toast.makeText(App.G.getContext(), "لطفا توضیحات را وارد کنید", Toast.LENGTH_LONG).show();
//
//            else {
//                Toast.makeText(getApplicationContext(), "تخلف مورد نظر ثبت شد.", Toast.LENGTH_LONG).show();
//
//            }
//        });
        senderror.setOnClickListener(v -> getdataerror());
    }

    private void getdataerror() {

        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = Integer.parseInt(pref.getString("last_id", "0"));

        if (id == 0) {
            finish();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(this);

        number = tv_billno.getText().toString().trim();
        pay = tv_billpay.getText().toString().trim();


        System.out.println(URI_SENDBILL
                + "?sch_id=" + id
                + "&date=" + fromDate.getText().toString().trim()
                + "&price=" + pay
                + "&billno=" + number);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                URI_SENDBILL
                        + "?sch_id=" + id
                        + "&date=" + fromDate.getText().toString().trim()
                        + "&price=" + pay
                        + "&billno=" + number, response ->
        {

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject jsonObject = response.getJSONObject(i);


                    if (jsonObject.optString("status").equals("1")) {
                        // Toast.makeText(SendError.class, "ثبت اطلاعات با مشکل مواجه شد ، لطفا دوباره تلاش کنید.", Toast.LENGTH_LONG).show();

                        showselectpopup();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
            progressDialog.dismiss();
        }, error ->
        {
            Log.e("Volley", error.toString());
            progressDialog.dismiss();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

       //add top of activity color
    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.toolbar));
        }
    }

    ////////////////////
    private void ShowUserData(String url) {

        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = Integer.parseInt(pref.getString("last_id", "0"));

        if (id == 0) {
            finish();
            return;
        }


        String dataurl = URI_SHOWPRICE + "?sch_id=" + id;

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, dataurl, response -> {
            try {

                Log.e("nas", response);
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    status=jsonObject.getInt("status");

                    if (status==0) {

                      tv_price.setText("تایید نشده!");
                       // notFound();

                    }

                    else if (status==1) {
                        schprice = jsonObject.getString("payment");

                       price = new Price(schprice);

                    }
                    else {  tv_price.setText(0);}
                }

                showdata();


            } catch (Exception e) {
                e.printStackTrace();

            }
        }, error -> error.printStackTrace());
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    private void showdata() {

        if (price != null) {

            tv_price.setText(price.getSchprice());

        }
    }

    ///////////////////////////////////////////////////////////////////////////////////
    public void openCamera() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File image = generatePicturePath(this);
            if (image != null) {
                if (Build.VERSION.SDK_INT >= 24) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", image));
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                }
                currentPicturePath = image.getAbsolutePath();
            }
            startActivityForResult(takePictureIntent, 13);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openGallery() {

        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 14);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void uploadToServer(String url) {
        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        id = Integer.parseInt(pref.getString("last_id", "0"));

        if (id == 0) {
            finish();
            return;
        }


        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("image", base64FromFile(new File(currentPicturePath))));
                    nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(id)));
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(params[0]);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    String st = EntityUtils.toString(response.getEntity(), "UTF-8");
                    Log.v("log_tag", "In the try Loop" + st);
System.out.println("image:"+base64FromFile(new File(currentPicturePath)));
                    return st;

                } catch (Exception e) {
                    Log.v("log_tag", "Error in http connection " + e.toString());
                    return e.toString();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                //showdata(s);
            }
        }.execute(url);
    }

    public static File generatePicturePath(Context context) {
        try {
            File storageDir = getAlbumDir(context);
            Date date = new Date();
            date.setTime(Long.parseLong(System.currentTimeMillis() + randomInteger(4)));
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.US).format(date);
            System.out.println("timestamp:"+timeStamp);
            return new File(storageDir, "IMG_" + timeStamp + ".jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static File getAlbumDir(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getCacheDir(context);
        }
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");
            if (!storageDir.mkdirs()) {
                if (!storageDir.exists()) {
                    return null;
                }
            }
        }

        return storageDir;
    }

    public static File getCacheDir(Context context) {
        String state = null;
        try {
            state = Environment.getExternalStorageState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (state == null || state.startsWith(Environment.MEDIA_MOUNTED)) {
            try {
                File file = context.getExternalCacheDir();
                if (file != null) {
                    return file;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            File file = context.getCacheDir();
            if (file != null) {
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File("");
    }

    public static void addMediaToGallery(Context context, String fromPath) {
        if (fromPath == null) {
            return;
        }
        File f = new File(fromPath);
        Uri contentUri = Uri.fromFile(f);
        addMediaToGallery(context, contentUri);
    }

    public static void addMediaToGallery(Context context, Uri uri) {
        if (uri == null) {
            return;
        }
        try {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(uri);
            context.sendBroadcast(mediaScanIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFullImageFromFilePath(ImageView imageview, String path) {
        Bitmap myBitmap = BitmapFactory.decodeFile(path);

        try {
            ExifInterface exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            myBitmap = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), matrix, true);
            imageview.setImageBitmap(myBitmap);
        } catch (Exception e) {

        }

    }

    public static boolean isempty(String string) {
        return string == null || string.equals("") || string.equalsIgnoreCase("null");
    }

    public static String randomInteger(int len) {
        return String.valueOf(len < 1 ? 0 : new Random()
                .nextInt((9 * (int) Math.pow(10, len - 1)) - 1)
                + (int) Math.pow(10, len - 1));
    }

    public static String base64FromFile(File filepath) {
        try {
            Bitmap bm = BitmapFactory.decodeFile(filepath.getAbsolutePath());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] byteArrayImage = baos.toByteArray();
            return Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }

    ////when click send form show a popup with upload button
    private void showselectpopup() {

        SELECT_FILE1 = 2;

        AlertDialog.Builder builder = new AlertDialog.Builder(SubmitBill.this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.select_popup, null);

        builder.setView(dialogView);

        imageViewer = dialogView.findViewById(R.id.image);
        upload = dialogView.findViewById(R.id.upload);
        camera = dialogView.findViewById(R.id.camera);
        folder = dialogView.findViewById(R.id.folder);
        final AlertDialog dialog = builder.create();


        camera.setOnClickListener(v -> openCamera());
        folder.setOnClickListener(v -> openGallery());

        upload.setOnClickListener(v -> {
            Log.e("nnupload", "mmm");
            //  if (erbitmap == null) {
            // myBitmap is empty/blank
            //    erroralertbox();
            //}
            //else
            //  {
            Log.e("nnupload", "m2");
            uploadToServer(UPLOAD_URL);
            alertbox();
            Log.e("nnupload", "m3");
            dialog.dismiss();
            //   }


        });

        // Display the custom alert dialog on interface
        dialog.show();

    }


    private void alertbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("تصویر شما اپلود شد.");
        builder.setPositiveButton("باشه!", (dialog, which) ->
        {
            //  Intent intent = new Intent(getApplicationContext(), Menu.class);
            //   startActivity(intent);
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void erroralertbox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("لطفا عکس مورد نظر را انتخاب کنید.");
        builder.setPositiveButton("باشه!", (dialog, which) -> {
            //  Intent intent = new Intent(getApplicationContext(), Menu.class);
            //   startActivity(intent);
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 13) {
                addMediaToGallery(this, currentPicturePath);
                Log.e("MYTAG", "currentPicturePath camera: " + currentPicturePath);
            } else if (requestCode == 14) {
                if (data == null || data.getData() == null) {
                    return;
                }
                try {
                    currentPicturePath = FileUtils.getPath(this, data.getData());
                    Log.e("MYTAG", "currentPicturePath Gallery: " + currentPicturePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    currentPicturePath = "";
                }
            }

            if (!isempty(currentPicturePath)) {
                setFullImageFromFilePath(imageViewer, currentPicturePath);
                // getBarcode();
            }
        }
    }

    //httputils for send parametr post method
    public class MyTask extends AsyncTask<MyHttpUtils.RequestData, Void, String> {


        @Override
        protected void onPreExecute() {
            if (tasks.isEmpty()) {
                //  pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected String doInBackground(MyHttpUtils.RequestData... params) {
            MyHttpUtils.RequestData reqData = params[0];

            return MyHttpUtils.getDataHttpUrlConnection(reqData);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                result = "null";
            }
            //  tv.setText(result);
            tasks.remove(this);
            if (tasks.isEmpty()) {
                //   pb.setVisibility(View.INVISIBLE);
            }
        }
    }

    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

}
