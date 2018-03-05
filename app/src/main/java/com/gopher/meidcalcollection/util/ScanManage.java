package com.gopher.meidcalcollection.util;

/**
 * Created by Administrator on 2018/1/26.
 */

public class ScanManage {
    private static final String TAG = ScanManage.class.getSimpleName();

    public ScanManage() {
    }




    interface ScanListener {
        void onScanSuccess(String code);

        void onScanFailed(Throwable throwable);
    }
}
