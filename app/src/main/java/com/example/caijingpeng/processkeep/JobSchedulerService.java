package com.example.caijingpeng.processkeep;

import android.app.ActivityManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.List;

/**
 * Created by caijingpeng on 2016/12/1.
 */

public class JobSchedulerService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, jobParameters));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        mJobHandler.removeMessages(1);
        return false;
    }

    private Handler mJobHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage( Message msg ) {
            Toast.makeText( getApplicationContext(),
                    "JobService task running", Toast.LENGTH_SHORT )
                    .show();

            if (!isServiceWork(getApplicationContext(), "com.example.caijingpeng.processkeep.GrayService")) {
                startService(new Intent(getApplicationContext(), GrayService.class));
            }

            jobFinished((JobParameters)msg.obj, false);
            return true;
        }

    } );

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
