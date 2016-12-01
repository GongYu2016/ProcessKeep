package com.example.caijingpeng.processkeep;

import android.app.Notification;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by caijingpeng on 2016/12/1.
 */

public class GrayService extends Service {

    private static final int GRAY_SERVICE_ID = 999;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent localIntent = new Intent();
        localIntent.setClass(this, GrayService.class);
        this.startService(localIntent);
    }


}
