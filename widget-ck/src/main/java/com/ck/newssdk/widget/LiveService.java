package com.ck.newssdk.widget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class LiveService extends Service {
    private NewAppWidget mNewAppWidget;

    @Override
    public void onCreate() {
//        GuardHelper.startDaemon(LiveService.this.getApplicationContext(), LiveService.class.getName());
        mNewAppWidget = new NewAppWidget();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.BOOT_COMPLETED");
        mNewAppWidget.onReceive(this, intent);
        System.out.println("AA-LiveService.onCreate");
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("AA-LiveService.onStartCommand");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
