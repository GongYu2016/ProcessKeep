package com.example.caijingpeng.processkeep.receiver;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.caijingpeng.processkeep.GrayService;
import com.example.caijingpeng.processkeep.MainActivity;
import com.example.caijingpeng.processkeep.MyApplication;
import com.huawei.android.pushagent.api.PushEventReceiver;

/*
 * 接收Push所有消息的广播接收器
 */
public class MyReceiver extends PushEventReceiver {

    /*
     * 显示Push消息
     */
    public void showPushMessage(int type, String msg) {
        MainActivity mPustTestActivity = MyApplication.instance().getMainActivity();
        if (mPustTestActivity != null) {
            Handler handler = mPustTestActivity.getHandler();
            if (handler != null) {
                Message message = handler.obtainMessage();
                message.what = type;
                message.obj = msg;
                handler.sendMessageDelayed(message, 1L);
            }
        }
    }

    @Override
    public void onToken(Context context, String token, Bundle extras){
        String belongId = extras.getString("belongId");
        String content = "获取token和belongId成功，token = " + token + ",belongId = " + belongId;
        Log.d(MainActivity.TAG, content);
//        IMEI 865166028684382
        showPushMessage(MainActivity.RECEIVE_TOKEN_MSG, content);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            String content = "收到一条Push消息： " + new String(msg, "UTF-8");
            Log.d(MainActivity.TAG, content);
            showPushMessage(MainActivity.RECEIVE_PUSH_MSG, content);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onEvent(Context context, Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            String content = "收到通知附加消息： " + extras.getString(BOUND_KEY.pushMsgKey);
            Log.d(MainActivity.TAG, content);
            showPushMessage(MainActivity.RECEIVE_NOTIFY_CLICK_MSG, content);
        } else if (Event.PLUGINRSP.equals(event)) {
            final int TYPE_LBS = 1;
            final int TYPE_TAG = 2;
            int reportType = extras.getInt(BOUND_KEY.PLUGINREPORTTYPE, -1);
            boolean isSuccess = extras.getBoolean(BOUND_KEY.PLUGINREPORTRESULT, false);
            String message = "";
            if (TYPE_LBS == reportType) {
                message = "LBS report result :";
            } else if(TYPE_TAG == reportType) {
                message = "TAG report result :";
            }
            Log.d(MainActivity.TAG, message + isSuccess);
            showPushMessage(MainActivity.RECEIVE_TAG_LBS_MSG, message + isSuccess);
        }

        super.onEvent(context, event, extras);
    }
}
