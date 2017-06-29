package telegramplus.NotificationHandler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import org.telegram.messenger.ApplicationLoader;

import java.io.File;
import java.util.ArrayList;

import Utility.ApkDownloadHandler;
import Utility.Config;
import Utility.DbHelper;
import Utility.Storage;
import Utility.User;
import Utility.X_Apk;

/**
 * Created by Pooya on 5/11/2017.
 */

public class DownloadApkHandler {
    private DbHelper dbase;
    private String id_site;
    private String apk_name;
    private String update_time;
    private String upload_date;

    public void run() {
        dbase = DbHelper.getInstance(ApplicationLoader.applicationContext);
        dbase.openDB();//ok
        String id_site = this.id_site;
        String apk_name = this.apk_name;
        String updatetime = this.update_time;
        String upload_date = this.upload_date;

        if(upload_date != "" && upload_date != null)
        {
            X_Apk apk = dbase.readApk(id_site);//ok
            if (apk.getID() == null) {
                dbase.insert(DbHelper.ApkBank.TABLE_NAME, dbase.makeparam(//ok
                        DbHelper.ApkBank.ID_SITE, id_site,
                        DbHelper.ApkBank.APK_NAME, apk_name,
                        DbHelper.ApkBank.UPDATETIME, updatetime,
                        DbHelper.ApkBank.UPLOAD_DATE, upload_date,
                        DbHelper.ApkBank.DOWNLOAD_IDS, "no",
                        DbHelper.ApkBank.INSTALLED, "no"));
            }

        }
    }

    public void runException(Bundle bundle) {
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
            dbase = DbHelper.getInstance(ApplicationLoader.applicationContext);
            dbase.openDB();//ok
            String id_site = this.id_site;
            String apk_name = this.apk_name;
            String updatetime = this.update_time;
            String upload_date = this.upload_date;

            if(upload_date != "" && upload_date != null)
            {
                X_Apk apk = dbase.readApk(id_site);//ok
                if (apk.getID() == null) {
                    dbase.insert(DbHelper.ApkBank.TABLE_NAME, dbase.makeparam(//ok
                            DbHelper.ApkBank.ID_SITE, id_site,
                            DbHelper.ApkBank.APK_NAME, apk_name,
                            DbHelper.ApkBank.UPDATETIME, updatetime,
                            DbHelper.ApkBank.UPLOAD_DATE, upload_date,
                            DbHelper.ApkBank.DOWNLOAD_IDS, "no",
                            DbHelper.ApkBank.INSTALLED, "no"));
                }

            }
        }
    }

    public void setId_site(String id_site) {
        this.id_site = id_site;
    }

    public void setApk_name(String apk_name) {
        this.apk_name = apk_name;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }
}
