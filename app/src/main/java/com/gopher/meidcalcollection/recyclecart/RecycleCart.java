package com.gopher.meidcalcollection.recyclecart;

import android.os.Bundle;
import android.view.View;

import com.gopher.meidcalcollection.R;
import com.gopher.meidcalcollection.common.base.ScanBaseActivity;
import com.gopher.meidcalcollection.common.util.ToolAlert;

/**
 * Created by Administrator on 2018/3/10.
 */

public class RecycleCart extends ScanBaseActivity {
    @Override
    public int bindLayout() {
        return R.layout.activity_recyclecart;
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void resume() {

    }

    @Override
    protected void getScanBarcode(String barcode) {
        ToolAlert.toastShort(barcode);
    }
}
