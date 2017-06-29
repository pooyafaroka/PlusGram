package telegramplus.Theming;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Picasso;

import ir.mmnotimm.telegramplus.R;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.Utilities;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import telegramplus.constant;



public class themAdapter extends ArrayAdapter<theme> {

    Context context;

    ProgressDialog prgDialog;

    public themAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public themAdapter(Context context, int resource, List<theme> items) {
        super(context, resource, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.theme_row, null);
        }

        final theme p = getItem(position);

        if (p != null) {
            TextView name = (TextView) v.findViewById(R.id.name);
            ImageView thumb1 = (ImageView) v.findViewById(R.id.thumb1);
            ImageView thumb2 = (ImageView) v.findViewById(R.id.thumb2);
            ImageView thumb3 = (ImageView) v.findViewById(R.id.thumb3);
            ImageView Save = (ImageView) v.findViewById(R.id.save);


            Save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new GETXML(p).execute(constant.THEMES_BASE_URL + p.getImagelink());
                }
            });

            if (name != null) {
                name.setText(p.getName());
            }

            if (thumb1 != null) {
                Picasso.with(context)
                        .load(constant.THEMES_BASE_URL + p.getThumb1())
                        .into(thumb1);
            }


            if (thumb2 != null) {
                Picasso.with(context)
                        .load(constant.THEMES_BASE_URL + p.getThumb2())
                        .into(thumb2);
            }


            if (thumb3 != null) {
                Picasso.with(context)
                        .load(constant.THEMES_BASE_URL + p.getThumb3())
                        .into(thumb3);
            }


        }

        return v;
    }


    protected Dialog onCreateDialog(int id) {
        prgDialog = new ProgressDialog(context);
        prgDialog.setMessage(context.getResources().getString(R.string.updating));
        prgDialog.setMax(100);
        prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        prgDialog.show();
        return prgDialog;
    }


    private class GETIMAGE extends AsyncTask<String, String, Void> {

        theme item;

        public GETIMAGE(theme item) {
            this.item = item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onCreateDialog(0);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);

                String Filename =  item.getImagelink();
                Log.i("TAG", "doInBackground: "+ item.getImagelink());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("accept-charset", "UTF-8");
                c.connect();

                String PATH = "/storage/emulated/0/Telegram/Themes/";
                File file = new File(PATH);
                file.mkdir();


                File outputFile = new File(file, Filename);
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
            apply(context, item);
        }

    }

    private class GETXML extends AsyncTask<String, String, Void> {

        theme item;

        public GETXML(theme item) {
            this.item = item;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onCreateDialog(0);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                String Filename =  item.getXmllink();
                Log.i("TAG", "doInBackground: "+ item.getXmllink());
//
//                HttpURLConnection c = (HttpURLConnection) url.openConnection();
//                c.setRequestMethod("GET");
//                c.setRequestProperty("accept-charset", "UTF-8");
//                c.connect();

                String PATH = "/storage/emulated/0/Telegram/Themes/";
                File file = new File(PATH);
                file.mkdir();


                File outputFile = new File(file, Filename);
                if (outputFile.exists())
                    outputFile.delete();
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = new ByteArrayInputStream(item.getXmldata().getBytes("UTF-8"));
                int lenght = item.getXmldata().length() ;
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
            new GETIMAGE(item).execute(constant.THEMES_BASE_URL + item.getImagelink());
        }

    }


    private void apply(final Context context, theme item) {

        String Filename =  item.getXmllink();
        Log.i("TAG", "doInBackground: "+ item.getXmllink());
        final String xmlFile = "/storage/emulated/0/Telegram/Themes/" + Filename;
        Log.i("TAG", "doInBackground: xmlFile = "+xmlFile);
        final File themeFile = new File(xmlFile);
        Log.i("TAG", "apply: "+themeFile.getName());
        String theme = Utilities.applyThemeFile(themeFile);
        Log.i("TAG", "theme: "+theme);
        if (!theme.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(theme);
            builder.setMessage(LocaleController.getString("ThemeApplied", R.string.ThemeApplied) + "\n" + LocaleController.getString("ClickOkToRestart", R.string.ClickOkToRestart));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Utilities.restartApp();
                }
            });
          builder.show();
        }



    }



}
