package com.gopher.meidcalcollection.common.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;

import com.gopher.meidcalcollection.common.UartBroadCast;
import com.gopher.meidcalcollection.common.UartService;
import com.gopher.meidcalcollection.common.constant.Config;
import com.gopher.meidcalcollection.common.util.ToolScanner;

/**
 * Created by Administrator on 2018/3/7.
 */

public abstract class ScanBaseActivity extends BaseActivity implements ToolScanner.OnScanSuccessListener, UartService.OnUartResultListener {


    private ToolScanner mToolScanner;
    private UartBroadCast uartBroadCast;




    @Override
    public void doBusiness(Context mContext) {
        mToolScanner = new ToolScanner(this);
        uartBroadCast = new UartBroadCast(this);
        IntentFilter filter = new IntentFilter(Config.UART_WEIGHT_ACTION);
        registerReceiver(uartBroadCast, filter);
    }


    @Override
    public void destroy() {
        unregisterReceiver(uartBroadCast);
        mToolScanner.onDestroy();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        mToolScanner.analysisKeyEvent(event);
        return true;
    }

    @Override
    public void onScanSuccess(String barcode) {
        getScanBarcode(barcode);
    }

    protected abstract void getScanBarcode(String barcode);


    protected abstract void getUartWeight(String weight);

    @Override
    public void onSuccess(String weight) {
        getUartWeight(weight);
    }

    @Override
    public void onFailed() {

    }
}
