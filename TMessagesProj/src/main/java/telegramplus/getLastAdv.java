package telegramplus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import ir.mmnotimm.telegramplus.BuildConfig;
import ir.mmnotimm.telegramplus.R;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.BuildVars;

import java.io.IOException;



public class getLastAdv extends AsyncTask {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;
    int prevnotifId = 0, notifId, code = 0;
    String title = "-", message = "-", link = "-", channelUsername = "-", channelJoinLink = "-" , lastJoinLink = "xxx" ;
    boolean firstTime = false;


    public getLastAdv(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("mainconfig", 0);
        edit = sharedPreferences.edit();
        prevnotifId = sharedPreferences.getInt("notifId", -1);
        lastJoinLink = sharedPreferences.getString("lastJoinLink", "xxx");
        if (prevnotifId == -1)
            firstTime = true;


        Log.i("TAG", "getLastAdv: notifId = " + prevnotifId);

    }


    @Override
    protected Object doInBackground(Object[] objects) {

        try {

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(constant.GET_LAST_NOTIF);
            httpGet.addHeader("Cache-Control", "no-cache");
            HttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            String s = IOUtils.toString(entity.getContent(), "utf-8");
            JSONObject json = null;
            JSONObject notif = null;
            try {
                json = new JSONObject(s);
                notif = json.getJSONObject("notif");

                notifId = notif.getInt("id");
                code = notif.getInt("code");
                title = notif.getString("title");
                message = notif.getString("text");
                channelUsername = notif.getString("channelUsername");
                channelJoinLink = notif.getString("channelJoinLink");
                link = notif.getString("link");

                return true;
            } catch (JSONException e) {
                FirebaseCrash.report(e);
                e.printStackTrace();
            }

        } catch (IOException e) {
            FirebaseCrash.report(e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (firstTime || prevnotifId == notifId) {
            code = 0;
        }

        if (code != 0)
            switch (code) {
                case 1:
                    sendNotification(context, message, link, title, 0);
                    break;
                case 2: // open app with channel user name
                    link = "https://telegram.me/" + channelUsername;
                    sendNotification(context, message, link, title, 2);
                    break;
                case 3: // open app witch channel join link
                    sendNotification(context, message, channelJoinLink, title, 2);
                    break;
                case 4 :
                    edit.putString("lastJoinLink",channelJoinLink);
                    break;
            }

        edit.putInt("notifId", notifId);
        edit.commit();
    }





    // for open a url in browser
    private void sendNotification(Context context, String messageBody, String url, String title, int id) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (id == 2) {
            if (!BuildConfig.DEBUG) {
                intent.setPackage(BuildVars.BUILD_PACKAGENAME);
            } else
                intent.setPackage(BuildVars.BUILD_PACKAGENAME);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        Bitmap image = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification)
                .setLargeIcon(image)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(id /* ID of notification */, notificationBuilder.build());
    }




}