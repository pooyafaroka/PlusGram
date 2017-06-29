/*
 * This is the source code of Telegram for Android v. 1.3.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2016.
 */

package org.telegram.messenger;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;

import org.telegram.tgnet.ConnectionsManager;

import java.io.File;
import java.util.ArrayList;

import Utility.ApkDownloadHandler;
import Utility.DbHelper;
import Utility.JSON;
import Utility.User;
import Utility.X_Apk;

import static android.content.Context.DOWNLOAD_SERVICE;

public class ScreenReceiver extends BroadcastReceiver {

    ApkDownloadHandler downloadHandler;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
//            downloadHandler = new ApkDownloadHandler();
//            downloadHandler.DownloadApk(context);
            ApplicationLoader.isScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
//            downloadHandler = new ApkDownloadHandler();
//            downloadHandler.CancelDownloadApk(context);
            ApplicationLoader.isScreenOn = true;
        }
        NotificationCenter.getInstance().postNotificationName(NotificationCenter.screenStateChanged);
    }
}
