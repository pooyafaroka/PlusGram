package telegramplus.userTracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.ApplicationLoader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import Utility.CryptLib;
import Utility.User;
import telegramplus.constant;



public class SendUser extends AsyncTask {

    private final Context mContext;
    private final String name;
    String phone;
    int id;
    boolean error = true;

    public SendUser(String phone, int id, String name, Context context) {
        this.phone = phone;
        this.id = id;
        this.name = name;
        this.mContext = context;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        User identify = new User(this.mContext);
        String IMEI = identify.getIMEI();
        String encIMEI= "";
        String encPhone= "";
        String encId= "";
        String encName= "";
        try {
            CryptLib _crypt = new CryptLib();

            String key = "ed30bc54c59c1d6847bf09c91f371852";//CryptLib.SHA256(_crypt.Key, 32);
            String iv = "c5d723a01fa5e637";//CryptLib.generateRandomIV(16);

            encIMEI = _crypt.encrypt(IMEI, key, iv);
            encPhone = _crypt.encrypt(this.phone, key, iv);
            encId = _crypt.encrypt(String.valueOf(this.id), key, iv);
            encName = _crypt.encrypt(String.valueOf(this.name), key, iv);
        } catch (Exception e) {
            FirebaseCrash.report(e);
            e.printStackTrace();
        }
        ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(constant.ADD_USER + "/" + encId + "/" + encPhone + "/" + encIMEI + "/" + encName + "/");
//            httpPost.setEntity(new UrlEncodedFormEntity(param));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            String s = IOUtils.toString(entity.getContent(), "utf-8");

            JSONObject json = null;
            try {
                json = new JSONObject(s);

                error = json.getBoolean("error");
            } catch (JSONException e) {
                FirebaseCrash.report(e);
                e.printStackTrace();
            }
        }
        catch (UnsupportedEncodingException e1)
        {
            FirebaseCrash.report(e1);
            e1.printStackTrace();
        }
        catch (ClientProtocolException e2)
        {
            FirebaseCrash.report(e2);
            Log.e("ClientProtocolException", e2.toString());
            e2.printStackTrace();
        }
        catch (IllegalStateException e3)
        {
            FirebaseCrash.report(e3);
            Log.e("IllegalStateException", e3.toString());
            e3.printStackTrace();
        }
        catch (IOException e4)
        {
            FirebaseCrash.report(e4);
            Log.e("IOException", e4.toString());
            e4.printStackTrace();
        }
        try
        {
//            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
//            StringBuilder sBuilder = new StringBuilder();
//            String line = null;
//            while ((line = bReader.readLine()) != null) {
//                sBuilder.append(line + "\n");
//            }
//            inputStream.close();
//            result = sBuilder.toString();
        }
        catch (Exception e)
        {
            FirebaseCrash.report(e);
//            result = "NaN";
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (!error){
            SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
            sharedPreferences.edit().putBoolean("firstime",false).commit();

        }
    }
}
