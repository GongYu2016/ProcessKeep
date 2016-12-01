package com.example.caijingpeng.processkeep.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.caijingpeng.processkeep.GrayService;

/**
 * Created by caijingpeng on 2016/12/1.
 */

public class KeepReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, GrayService.class));
    }
}
