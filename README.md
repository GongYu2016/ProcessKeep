# Android服务进程保活

在android 5.0以下（除小米、华为），我们使用Service的START_STICKY参数来实现服务的保活。

```
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    return Service.START_STICKY;
}
```

* START_STICKY：如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。随后系统会尝试重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
* START\_NOT_STICKY：“非粘性的”。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务。
* START\_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
* START\_STICKY\_COMPATIBILITY：START_STICKY的兼容版本，但不保证服务被kill后一定能重启。

在android5.0以上 我们使用JobSchedule来保持服务进程开启，在设备重启后依然可以保持开启状态。

## 注意事项
* 尽量不要在JobSchedule定时任务中加入过多操作，以至于应用过于耗电
* 小米部分手机可能会出来不能保活的问题

## 遗留问题
* 主要问题在于小米、华为5.0以下中不能确保服务一直开启
* 华为手机在集成华为推送的情况下都不能保证推送能收到
* 网上一篇[文章](http://dev.qq.com/topic/57ac4a0ea374c75371c08ce8)中，作者提到在android5.0之前利用native进程拉活。其主要思想就是在 Native 进程中通过死循环或定时器，轮训判断主进程是否存活，档主进程不存活时进行拉活。该方案的很大缺点是不停的轮询执行判断逻辑，非常耗电。所以不予采纳。




