package com.gopher.meidcalcollection.common;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gopher.meidcalcollection.common.base.BaseService;
import com.gopher.meidcalcollection.common.constant.Config;
import com.gopher.meidcalcollection.common.uart.UartConsts;
import com.gopher.meidcalcollection.common.util.ToolAlert;
import com.gopher.meidcalcollection.common.util.async.TaskExecutor;


import tw.com.prolific.driver.pl2303.PL2303Driver;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by Administrator on 2018/3/10.
 */

public class UartService extends BaseService {
    private static final String TAG = "UartService";
    private PL2303Driver mSerial;
    private static final boolean SHOW_DEBUG = true;

    char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
    private Intent uartIntent = null;
    private Bundle bundle = null;


    @Override
    public void onCreate() {
        super.onCreate();
        mSerial = new PL2303Driver((UsbManager) getSystemService(Context.USB_SERVICE),
                this, UartConsts.ACTION_USB_PERMISSION);

        // check USB host function.
        if (!mSerial.PL2303USBFeatureSupported()) {
            ToolAlert.toastShort("No Support USB host API");
            mSerial = null;
        }

        if (!mSerial.enumerate()) {
            ToolAlert.toastShort("no more devices found");
        }
        try {
            Thread.sleep(15000);
            openUsbSerial();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Leave onCreate");
        uartIntent = new Intent();
        bundle = new Bundle();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = Service.START_STICKY;
        TaskExecutor.startTimerTask(new Runnable() {
            @Override
            public void run() {
                readDataFromSerial();
            }
        }, 100, 100);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mSerial.isConnected()) {
            mSerial.end();
        }
        super.onDestroy();
    }

    private void openUsbSerial() {
        Log.d(TAG, "Enter  openUsbSerial");

        if (mSerial == null) {

            Log.d(TAG, "No mSerial");
            return;
        }
        if (mSerial.isConnected()) {
            if (SHOW_DEBUG) {
                Log.d(TAG, "openUsbSerial : isConnected ");
            }
            if (!mSerial.InitByBaudRate(PL2303Driver.BaudRate.B19200, 700)) {
                if (!mSerial.PL2303Device_IsHasPermission()) {
                    ToolAlert.toastShort("cannot open, maybe no permission");
                    Log.i(TAG, "cannot open, maybe no permission");
                    Toast.makeText(this, "cannot open, maybe no permission", Toast.LENGTH_SHORT).show();
                }

                if (mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip())) {
                    Log.d(TAG, "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.");
                }
            } else {
//                ToolAlert.toastShort("connected : OK");
                Log.d(TAG, "connected : OK");
                Log.d(TAG, "Exit  openUsbSerial");


            }
        }//isConnected
        else {
            Toast.makeText(this, "Connected failed, Please plug in PL2303 cable again!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "connected failed, Please plug in PL2303 cable again!");
        }
    }//openUsbSerial


    private void readDataFromSerial() {

        int len;
        byte[] rbuf = new byte[13];
        final StringBuffer sbHex = new StringBuffer();

        Log.d(TAG, "Enter readDataFromSerial");

        if (null == mSerial)
            return;

        if (!mSerial.isConnected())
            return;

        len = mSerial.read(rbuf);
        if (len < 0) {
            Log.d(TAG, "Fail to bulkTransfer(read data)");
            return;
        }

        if (len > 0) {
            if (SHOW_DEBUG) {
                Log.d(TAG, "read len : " + len);
            }
            //rbuf[len] = 0;
            for (int j = 0; j < len; j++) {
                if (j > 2 && j < 10) {
                    char c = (char) (rbuf[j] & 0x000000FF);
                    for (int i = 0; i < chars.length; i++) {
                        if (c == chars[i]) {
                            sbHex.append(c);
                        }
                    }
                }
            }
            String ascii = sbHex.toString();
            log.i(TAG, ascii);
            bundle.putString(Config.WEIGHT, ascii);
            uartIntent.setAction(Config.UART_WEIGHT_ACTION);
            uartIntent.putExtras(bundle);
            sendBroadcast(uartIntent);

        } else {
            if (SHOW_DEBUG) {
                Log.d(TAG, "read len : 0 ");
            }
            return;
        }


        Log.d(TAG, "Leave readDataFromSerial");
    }

    public interface OnUartResultListener {
        void onSuccess(String weight);

        void onFailed();
    }

}
