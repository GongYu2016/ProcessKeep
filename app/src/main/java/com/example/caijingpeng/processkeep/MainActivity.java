package com.example.caijingpeng.processkeep;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.android.pushagent.PushManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "PushDemo";

    // 接收Push消息
    public static final int RECEIVE_PUSH_MSG = 0x100;
    // 接收Push Token消息
    public static final int RECEIVE_TOKEN_MSG = 0x101;
    // 接收Push 自定义通知消息内容
    public static final int RECEIVE_NOTIFY_CLICK_MSG = 0x102;
    // 接收Push LBS 标签上报响应
    public static final int RECEIVE_TAG_LBS_MSG = 0x103;
    @BindView(R.id.text_view)
    TextView textView;

    private JobScheduler mJobScheduler;

    /*
     * 处理提示消息，更新界面
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVE_PUSH_MSG:
                    showMsg((String) msg.obj);
                    break;
                case RECEIVE_TOKEN_MSG:
                    showMsg((String) msg.obj);
                    break;
                case RECEIVE_NOTIFY_CLICK_MSG:
                    showMsg((String) msg.obj);
                    break;
                case RECEIVE_TAG_LBS_MSG:
                    showToast((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MyApplication.instance().setMainActivity(this);
        PushManager.requestToken(MainActivity.this);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 6.0以上提示用户关闭电池优化功能
            isIgnoreBatteryOption(this);

            JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(MainActivity.this, JobSchedulerService.class.getName()));
            builder.setPeriodic(60 * 1000); // 每隔60秒 执行一次
            builder.setPersisted(true);  //  保证应用重启后依然有效

            mJobScheduler = (JobScheduler)
                    getSystemService(Context.JOB_SCHEDULER_SERVICE);
            int res = mJobScheduler.schedule(builder.build());
            if (res <= 0) {
                //If something goes wrong
            }
        }
        else {
            startService(new Intent(getApplicationContext(), GrayService.class));
        }
    }

    /**
     * 针对N以上的Doze模式
     *
     * @param activity
     */
    public static void isIgnoreBatteryOption(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                Intent intent = new Intent();
                String packageName = activity.getPackageName();
                PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    activity.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 显示接收到的Push消息，在页面上
     */
    public void showMsg(final String msg) {
        try {

            mHandler.post(new Runnable() {

                public void run() {
                    String str = "消息内容：" + msg;
                    textView.setText(str);
                    Log.d(TAG, "showMsg:" + str);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 显示接收到的Push消息，弹出Toast
     */
    public void showToast(String msg) {
        Log.d(TAG, "showToast:" + msg);
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }
}
