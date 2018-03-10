package com.gopher.meidcalcollection.common.base;

import android.content.Context;
import android.view.KeyEvent;
import com.gopher.meidcalcollection.common.util.ToolScanner;

/**
 * Created by Administrator on 2018/3/7.
 */

public abstract class ScanBaseActivity extends BaseActivity implements ToolScanner.OnScanSuccessListener {

    private ToolScanner mToolScanner;



    @Override
    public void doBusiness(Context mContext) {

        mToolScanner = new ToolScanner(this);
    }



    @Override
    public void destroy() {
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


}
