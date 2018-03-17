package com.gopher.meidcalcollection.common.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.orhanobut.logger.Logger;

/**
 * Created by Administrator on 2017/11/17.
 */

public abstract class BaseService extends Service {

    @Override
    public void onCreate() {
        Logger.d( "BaseService-->onCreate()");
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        Logger.d("BaseService-->onStart()");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d( "BaseService-->onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Logger.d( "BaseService-->onDestroy()");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("BaseService-->onBind()");
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d("BaseService-->onUnbind()");
        return super.onUnbind(intent);
    }
}
