package telegramplus.NotificationHandler;

import android.os.Bundle;

import org.telegram.messenger.ApplicationLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import Utility.Config;
import Utility.DbHelper;
import Utility.User;
import Utility.X_Popup;

/**
 * Created by Pooya on 5/11/2017.
 */

public class PopupHandler {
    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void makePopup() {
        String msg = "";
        try {
            msg = URLDecoder.decode(this.message, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            msg = this.message;
        }
        Config popup = new Config(ApplicationLoader.applicationContext);
        popup.WritePopup(msg);
    }

    public void makePopup(Bundle bundle) {
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
            Config popup = new Config(ApplicationLoader.applicationContext);
            popup.WritePopup(msg);
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
            Config popup = new Config(ApplicationLoader.applicationContext);
            popup.WritePopup(msg);
        }
    }
}
