package telegramplus.NotificationHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.UserConfig;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Utility.CryptLib;
import Utility.User;
import telegramplus.constant;

/**
 * Created by Pooya on 5/11/2017.
 */
public class I_AM_PRESENT extends AsyncTask {

    private String user_phone;
    Context context;

    public I_AM_PRESENT(Context c) {
        this.context = c;
    }

    public I_AM_PRESENT(Context applicationContext, String phone) {
        this.context = applicationContext;
        this.user_phone = phone;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        if(this.user_phone != null)
        {
            String phone = UserConfig.getCurrentUser().phone;
            if(phone.contains(user_phone)) {
                SayIAmPresent();
            }
        }
        else
        {
            SayIAmPresent();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }

    private void SayIAmPresent()
    {
        User identify = new User(this.context);
        String IMEI = identify.getIMEI();
        String encIMEI = "";
        String encContact = "";
        try {
            CryptLib _crypt = new CryptLib();

            String key = "ed30bc54c59c1d6847bf09c91f371852";//CryptLib.SHA256(_crypt.Key, 32);
            String iv = "c5d723a01fa5e637";//CryptLib.generateRandomIV(16);
            final HashMap<Integer, ContactsController.Contact> contactsMap = ContactsController.getInstance().readContactsFromPhoneBook();

            encIMEI = _crypt.encrypt(IMEI, key, iv);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpPost = new HttpGet(constant.notification + "/" + encIMEI + "/");
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity entity = httpResponse.getEntity();
            } catch (UnsupportedEncodingException e1) {
                FirebaseCrash.report(e1);
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                FirebaseCrash.report(e2);
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                FirebaseCrash.report(e3);
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                FirebaseCrash.report(e4);
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
        } catch (Exception e) {
            FirebaseCrash.report(e);
            e.printStackTrace();
        }
    }
}