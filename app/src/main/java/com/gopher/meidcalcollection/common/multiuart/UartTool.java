package com.gopher.meidcalcollection.common.multiuart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import tw.com.prolific.pl2303multilib.PL2303MultiLib;

/**
 * Created by Administrator on 2017/11/27.
 */

public class UartTool implements UartToolIml {
    private static final java.lang.String ACTION_USB_PERMISSION = "com.prolific.pluartmultisimpletest.USB_PERMISSION";
    private static final int MAX_DEVICE_COUNT = 4;
    private UARTSettingInfo uartSettingInfo;
    private Context context;
    private PL2303MultiLib lib;
    public UARTSettingInfo infos[];
    private boolean devicesOpened[] = new boolean[MAX_DEVICE_COUNT];
    private boolean devicesTreadStop[] = new boolean[MAX_DEVICE_COUNT];
    private int deviceCount = 0;

    private static final int DeviceIndex1 = 0;
    private static final int DeviceIndex2 = 1;
    private static final int DeviceIndex3 = 2;

    public UartTool(Builder builder) {
        this.context = builder.context;
        this.uartSettingInfo = builder.uartSettingInfo;
        lib = new PL2303MultiLib((UsbManager) context.getSystemService(Context.USB_SERVICE),
                context, ACTION_USB_PERMISSION);
        infos = new UARTSettingInfo[MAX_DEVICE_COUNT];
        for (int i = 0; i < MAX_DEVICE_COUNT; i++) {
            infos[i] = new UARTSettingInfo();
        }
        deviceCount = lib.PL2303Enumerate();


    }

    public static class Builder {
        private Context context;
        private int index;
        private UARTSettingInfo uartSettingInfo;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder index(int index) {
            this.index = index;
            return this;
        }

        public Builder uartSettingInfo(UARTSettingInfo info) {
            this.uartSettingInfo = info;
            return this;
        }

        public UartTool build() {
            return new UartTool(this);
        }
    }

    @Override
    public Future openUart(int index, UARTSettingInfo info) {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(lib.PLUART_MESSAGE);
            context.registerReceiver(PLMultiLibReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lib != null) {
            if (lib.PL2303IsDeviceConnectedByIndex(index)) {
                UARTSettingInfo uart = infos[index];
                boolean b = lib.PL2303OpenDevByUARTSetting(index,
                        uart.getmBaudrate(),
                        uart.getmDataBits(),
                        uart.getmStopBits(),
                        uart.getmParity(),
                        uart.getmFlowControl());
                if (!b) {
                    release();
                }
            }
        }

        return null;
    }

    @Override
    public Future<Double> readData(final int index, final byte[] readDatas) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future submit = executor.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                StringBuffer sbHex = new StringBuffer();
                int len = lib.PL2303Read(index, readDatas);
                if (len > 0) {
                    for (int i = 0; i < len; i++) {
                        sbHex.append((char) (readDatas[i] & 0x000000FF));
                    }
                }
                return null;
            }
        });
        executor.shutdown();
        return submit;
    }

    @Override
    public Future writeData(int index, byte[] writeDatas) {
        return null;
    }

    @Override
    public void release() {
        if (lib != null) {
            for (int i = 0; i < MAX_DEVICE_COUNT; i++) {
                devicesTreadStop[i] = true;
            }
            if (deviceCount > 0) {
                context.unregisterReceiver(PLMultiLibReceiver);
                lib.PL2303Release();
                lib = null;
            }

        }
    }

    /**
     * 端口监听广播
     */
    private final BroadcastReceiver PLMultiLibReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(lib.PLUART_MESSAGE)) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    String str = (String) extras.get(lib.PLUART_DETACHED);
                    int index = Integer.valueOf(str);
                    if (DeviceIndex1 == index) {
                        devicesOpened[DeviceIndex1] = false;
                    } else if (DeviceIndex2 == index) {
                        devicesOpened[DeviceIndex2] = false;
                    } else if (DeviceIndex3 == index) {
                        devicesOpened[DeviceIndex3] = false;
                    }
                }
            }
        }
    };


}
