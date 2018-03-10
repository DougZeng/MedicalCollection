package com.gopher.meidcalcollection.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gopher.meidcalcollection.R;
import com.gopher.meidcalcollection.common.UartService;
import com.gopher.meidcalcollection.common.base.BaseActivity;
import com.gopher.meidcalcollection.recyclecart.RecycleCart;
import com.gopher.meidcalcollection.transfercar.TransferCar;
import com.gopher.meidcalcollection.transferworkshop.TransferWorkshop;

/**
 * Created by Administrator on 2018/3/7.
 */

public class MenuActivity extends BaseActivity {


    @Override
    public int bindLayout() {
        return R.layout.activity_menu;
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void doBusiness(Context mContext) {
        Intent uartIntent = new Intent(this, UartService.class);
        startService(uartIntent);
    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }

    public void recycleCart(View view) {
        getOperation().forward(RecycleCart.class);
    }

    public void transferWorkshop(View view) {
        getOperation().forward(TransferWorkshop.class);
    }

    public void transferCar(View view) {
        getOperation().forward(TransferCar.class);
    }
}
