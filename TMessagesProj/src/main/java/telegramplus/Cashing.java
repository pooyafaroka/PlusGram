package telegramplus;

import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.crash.FirebaseCrash;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import telegramplus.Theming.theme;



public class Cashing implements Serializable {

    public static List<theme> themes = new ArrayList<>();





    public static void setThemes(Context context) {
        new getThemes(context, constant.GET_THEMES).execute();
    }



    static class getThemes extends AsyncTask<String, Void, String> {
        Context c;
        private String MYURL = "";


        public getThemes(Context c, String url) {
            this.c = c;
            this.MYURL = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(MYURL);
                httpGet.addHeader("Cache-Control", "no-cache");
                HttpResponse response = httpClient.execute(httpGet);

                HttpEntity entity = response.getEntity();
                String s = IOUtils.toString(entity.getContent(), "utf-8");

                themes = new ArrayList<>();

                JSONObject json = null;
                JSONArray items = null;
                try {
                    json = new JSONObject(s);
                    items = json.getJSONArray("themes");


                    for (int i = 0; i < items.length(); i++) {
                        JSONObject arr = items.getJSONObject(i);
                        theme item = new theme(arr.getString("name"),
                                arr.getString("description"),
                                arr.getString("thumb1"),
                                arr.getString("thumb2"),
                                arr.getString("thumb3"),
                                arr.getString("xmllink"),
                                arr.getString("imagelink"),
                                arr.getString("xmldata"));
                        themes.add(item);

                    }


                } catch (JSONException e) {
                    FirebaseCrash.report(e);
                    e.printStackTrace();
                }

            } catch (IOException e) {
                FirebaseCrash.report(e);
            }

            return "success";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }


    }
}

