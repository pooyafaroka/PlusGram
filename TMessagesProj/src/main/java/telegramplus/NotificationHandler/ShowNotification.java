package telegramplus.NotificationHandler;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.google.firebase.messaging.RemoteMessage;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.ui.LaunchActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import Utility.Config;
import Utility.User;
import ir.mmnotimm.telegramplus.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static org.telegram.messenger.GcmPushListenerService.NOTIFICATION_ID;

/**
 * Created by Pooya on 5/11/2017.
 */

public class ShowNotification {

    private String message;
    private String title;
    private int notification_id = 21547;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void show() {
        String msg = "";
        try {
            msg = URLDecoder.decode(this.message, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            msg = this.message;
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
        builder.setSmallIcon(R.drawable.ic_telegram_logo);
        builder.setLargeIcon(BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), R.drawable.ic_telegram_logo));
        builder.setContentTitle(this.title);
        builder.setContentText(msg);
        NotificationManager notificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        User.PlayDing(ApplicationLoader.applicationContext);
    }

    public void show(Bundle bundle) {
        boolean f_mine = false;
        User user = new User(ApplicationLoader.applicationContext);
        String imei = user.getIMEI();
        String users = bundle.getString("users");
        String[] splitedUser = users.split("@");
        for(int i = 0; i < splitedUser.length; i++)
        {
            if(imei.equals(splitedUser[i]))
            {
                f_mine = true;
            }
        }
        if(f_mine)
        {
            String msg = "";
            try {
                msg = URLDecoder.decode(this.message, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                msg = this.message;
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
            builder.setSmallIcon(R.drawable.ic_telegram_logo);
            builder.setLargeIcon(BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), R.drawable.ic_telegram_logo));
            builder.setContentTitle(this.title);
            builder.setContentText(msg);
            NotificationManager notificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
            User.PlayDing(ApplicationLoader.applicationContext);
        }

    }

    public void showException(Bundle bundle) {
        boolean f_mine = true;
        User user = new User(ApplicationLoader.applicationContext);
        String imei = user.getIMEI();
        String users = bundle.getString("users");
        String[] splitedUser = users.split("@");
        for(int i = 0; i < splitedUser.length; i++)
        {
            if(imei.equals(splitedUser[i]))
            {
                f_mine = false;
            }
        }
        if(f_mine)
        {
            String msg = "";
            try {
                msg = URLDecoder.decode(this.message, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                msg = this.message;
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
            builder.setSmallIcon(R.drawable.ic_telegram_logo);
            builder.setLargeIcon(BitmapFactory.decodeResource(ApplicationLoader.applicationContext.getResources(), R.drawable.ic_telegram_logo));
            builder.setContentTitle(this.title);
            builder.setContentText(msg);
            NotificationManager notificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
            User.PlayDing(ApplicationLoader.applicationContext);
        }
    }
}
