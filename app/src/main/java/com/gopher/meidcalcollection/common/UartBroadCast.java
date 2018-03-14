package com.gopher.meidcalcollection.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gopher.meidcalcollection.common.base.BaseBroadcastReceiver;
import com.gopher.meidcalcollection.common.constant.Config;

/**
 * Created by Administrator on 2018/3/10.
 */

public class UartBroadCast extends BaseBroadcastReceiver {
    private UartService.OnUartResultListener onUartResultListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Config.UART_WEIGHT_ACTION)) {
            Bundle bundle = intent.getExtras();
            String weight = bundle.getString(Config.WEIGHT);
            onUartResultListener.onSuccess(weight);
        }
    }

    public UartBroadCast() {
    }

    public UartBroadCast(UartService.OnUartResultListener onUartResultListener) {
        this.onUartResultListener = onUartResultListener;
    }
}
