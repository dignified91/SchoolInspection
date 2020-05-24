package iso.my.com.inspectionstudentorganization.GeneralClass;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.List;

import iso.my.com.inspectionstudentorganization.Completeinspection;
import iso.my.com.inspectionstudentorganization.R;


/**
 * Created by Asus on 1/27/2018.
 */


public class MessageReciver extends FirebaseMessagingService {

    List<AsyncTask> tasks = new ArrayList<>();
    SharedPreferences pref;

    @Override
    public void onNewToken(String token) {
        synctoken(token);
        Log.d("nas", "MessageReciver");
        Log.d("nas", "token token :" + token);
        Log.i("nas", "id is :" + getid());

    }

    //get mellicode with sharepref
    private String getid() {

        pref = getSharedPreferences("myprefs", MODE_PRIVATE);
        return pref.getString("personid", "0");
    }

    public void synctoken(final String token) {

        Log.d("nas", getid());

        String urlTokenWebService = "http://ws.refahicenter.ir/WebServices/savetoken.aspx";
        MyHttpUtils.RequestData requestData = new MyHttpUtils.RequestData(urlTokenWebService, "POST");
        requestData.setParameter("token", String.valueOf(token));
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", String.valueOf(token));
        editor.apply();

        requestData.setParameter("personid", getid());
        new MyTask().execute(requestData);

        Log.i("nas", "token is :" + token);
        Log.i("nas", "id is :" + getid());

        if (!getid().equals("0")) {

            Log.i("nas", "start id is :" + getid());

        } else {

            Log.i("nas", "last id is :" + getid());
        }

    }

    //httputils for send parametr post method
    @SuppressLint("StaticFieldLeak")
    public class MyTask extends AsyncTask<MyHttpUtils.RequestData, Void, String> {


        @Override
        protected void onPreExecute() {
            if (tasks != null) {
                //  pb.setVisibility(View.VISIBLE);
                Log.i("nas", "last id is :" + getid());
            }
            assert tasks != null;
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
            }
            //  tv.setText(result);
            tasks.remove(this);
            if (tasks.isEmpty()) {
                Log.i("nas", "last id is :" + getid());
                //   pb.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("nas", "on message Recived");

        String body = remoteMessage.getData().get("body");
        String title = remoteMessage.getData().get("title");
        String type=remoteMessage.getData().get("type");
       ShowNotification(body, title,type);


    }

    //method for get url
    private void ShowNotification(String messageBody, String title,String type) {

        Intent intent;
       intent = new Intent(getApplicationContext(), Completeinspection.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.sazman)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setLights(Color.RED, 3000, 3000);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }

}
