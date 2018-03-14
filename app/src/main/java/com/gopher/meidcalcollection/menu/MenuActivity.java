package com.gopher.meidcalcollection.menu;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.gopher.meidcalcollection.JobSchedulerService;
import com.gopher.meidcalcollection.R;
import com.gopher.meidcalcollection.common.base.BaseActivity;
import com.gopher.meidcalcollection.common.util.ToolAlert;
import com.gopher.meidcalcollection.recyclecart.RecycleCart;
import com.gopher.meidcalcollection.setting.SettingActivity;
import com.gopher.meidcalcollection.transfercar.TransferCar;
import com.gopher.meidcalcollection.transferworkshop.TransferWorkshop;

/**
 * Created by Administrator on 2018/3/7.
 */

public class MenuActivity extends BaseActivity {

    private JobScheduler mJobScheduler;

    @Override
    public int bindLayout() {
        return R.layout.activity_menu;
    }

    @Override
    public void initParms(Bundle parms) {
        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void doBusiness(Context mContext) {
//        startJobSchedulerService();
    }

    @SuppressLint("NewApi")
    private void startJobSchedulerService() {
        JobInfo.Builder builder = new JobInfo.Builder(1,
                new ComponentName(getPackageName(), JobSchedulerService.class.getName()));

        builder.setPeriodic(3000);


        if (mJobScheduler.schedule(builder.build()) <= JobScheduler.RESULT_FAILURE) {
            //If something goes wrong
            ToolAlert.toastShort("schedule <= 0");
        }
    }


    @Override
    public void resume() {

    }

    @SuppressLint("NewApi")
    @Override
    public void destroy() {
        mJobScheduler.cancelAll();
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

    public void set(View view) {
        getOperation().forward(SettingActivity.class);
    }
}
