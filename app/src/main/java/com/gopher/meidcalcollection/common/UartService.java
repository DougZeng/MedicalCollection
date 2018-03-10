package com.gopher.meidcalcollection.common;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;

import com.gopher.meidcalcollection.common.base.BaseService;

/**
 * Created by Administrator on 2018/3/10.
 */

public class UartService extends BaseService {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = Service.START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }
}
