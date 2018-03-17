package com.gopher.meidcalcollection.common;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import com.gopher.meidcalcollection.common.base.BaseService;
import com.gopher.meidcalcollection.common.constant.Config;
import com.gopher.meidcalcollection.common.uart.UartConsts;
import com.gopher.meidcalcollection.common.util.ToolAlert;
import com.gopher.meidcalcollection.common.util.async.TaskExecutor;
import com.orhanobut.logger.Logger;


import tw.com.prolific.driver.pl2303.PL2303Driver;


/**
 * Created by Administrator on 2018/3/10.
 */

public class UartService extends BaseService {
    private PL2303Driver mSerial;

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

        Logger.d( "Leave onCreate");
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
        super.onDestroy();
        if (mSerial.isConnected()) {
            mSerial.end();
        }

    }

    private void openUsbSerial() {
        Logger.d("Enter  openUsbSerial");

        if (mSerial == null) {

            Logger.d("No mSerial");
            return;
        }
        if (mSerial.isConnected()) {
            Logger.d("openUsbSerial : isConnected ");
            if (!mSerial.InitByBaudRate(PL2303Driver.BaudRate.B19200, 700)) {
                if (!mSerial.PL2303Device_IsHasPermission()) {
                    ToolAlert.toastShort("cannot open, maybe no permission");
                    Logger.i("cannot open, maybe no permission");
                    ToolAlert.toastShort("cannot open, maybe no permission");
                }

                if (mSerial.PL2303Device_IsHasPermission() && (!mSerial.PL2303Device_IsSupportChip())) {
                    Logger.d( "cannot open, maybe this chip has no support, please use PL2303HXD / RA / EA chip.");
                }
            } else {
//                ToolAlert.toastShort("connected : OK");
                Logger.d("connected : OK");
                Logger.d("Exit  openUsbSerial");


            }
        }//isConnected
        else {
            Logger.d("connected failed, Please plug in PL2303 cable again!");
        }
    }//openUsbSerial


    private void readDataFromSerial() {

        int len;
        byte[] rbuf = new byte[13];
        final StringBuffer sbHex = new StringBuffer();

        Logger.d("Enter readDataFromSerial");

        if (null == mSerial)
            return;

        if (!mSerial.isConnected())
            return;

        len = mSerial.read(rbuf);
        if (len < 0) {
            Logger.d("Fail to bulkTransfer(read data)");
            return;
        }

        if (len > 0) {
            Logger.d("read len : " + len);
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
            Logger.i("ascii = %s ", ascii);
            bundle.putString(Config.WEIGHT, ascii);
            uartIntent.setAction(Config.UART_WEIGHT_ACTION);
            uartIntent.putExtras(bundle);
            sendBroadcast(uartIntent);

        } else {
            Logger.d("read len : 0 ");
            return;
        }


        Logger.d("Leave readDataFromSerial");
    }

    public interface OnUartResultListener {
        void onSuccess(String weight);

        void onFailed();
    }

}
