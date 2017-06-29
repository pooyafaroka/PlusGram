/*
 * This is the source code of Telegram for Android v. 3.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2016.
 */

package org.telegram.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import org.telegram.messenger.ApplicationLoader;
import ir.mmnotimm.telegramplus.R;

import java.util.Timer;
import java.util.TimerTask;

import Utility.DbHelper;

public class ShortcutActivity extends Activity {
    private static final String MY_TAG = "Alireza";
    DbHelper dbase;
    long startTime;
    Timer timer;
    TimerTask_D timerTaskD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(R.style.Theme_TMessages);
        getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Only Portrait
        InitObjects();
        Step_DBase(); // Start with checking DBase...
    }

    class TimerTask_D extends TimerTask {
        @Override
        public void run() {
            if( !dbase.IsBusy()) {//ok
                timer.cancel();
                timer.purge();
                timer = null;
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        Log.d(MY_TAG, "DB Installed in " + (System.currentTimeMillis() - startTime) + " ms.");
                        Step_Lock();
                    }
                });
            }
        }
    }

    private void Step_DBase() {
        dbase.openDB();//ok
        if( dbase.IsBusy()) {//ok
            Log.d(MY_TAG, "Installing DBase...");
            startTime = System.currentTimeMillis(); // -Remove
            timerTaskD = new TimerTask_D();
            timer = new Timer();
            timer.schedule(timerTaskD, 100, 100);
        }
        else {
            Log.d(MY_TAG, "DBase is Already Installed.");
            Step_Lock();
        }
    }
    private void Step_Lock() {
        Intent intent = getIntent();
        intent.setClassName(ApplicationLoader.applicationContext.getPackageName(), "org.telegram.ui.LaunchActivity");
        startActivity(intent);
        finish();
    }

    public void InitObjects() {
        dbase = DbHelper.getInstance(this);
    }

}

