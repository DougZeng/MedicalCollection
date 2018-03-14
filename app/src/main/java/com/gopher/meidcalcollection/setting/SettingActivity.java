package com.gopher.meidcalcollection.setting;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.gopher.meidcalcollection.R;
import com.gopher.meidcalcollection.common.base.BaseActivity;
import com.gopher.meidcalcollection.common.constant.Config;
import com.gopher.meidcalcollection.common.util.ToolAlert;
import com.gopher.meidcalcollection.common.util.ToolRegex;
import com.gopher.meidcalcollection.common.util.ToolString;
import com.gopher.meidcalcollection.common.util.ssp.SecuredPreferenceStore;

/**
 * Created by Administrator on 2018/3/10.
 */

public class SettingActivity extends BaseActivity {

    private EditText tiet1;
    private EditText tiet2;
    private EditText tiet3;
    private EditText tiet4;
    private SecuredPreferenceStore sharedInstance;

    @Override
    public int bindLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initParms(Bundle parms) {
        sharedInstance = SecuredPreferenceStore.getSharedInstance();
    }

    @Override
    public void initView(View view) {
        tiet1 = (EditText) findViewById(R.id.tiet1);
        tiet2 = (EditText) findViewById(R.id.tiet2);
        tiet3 = (EditText) findViewById(R.id.tiet3);
        tiet4 = (EditText) findViewById(R.id.tiet4);
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void resume() {
        String download_ip = sharedInstance.getString(Config.DOWNLOAD_IP_KEY, "");
        String download_port = sharedInstance.getString(Config.DOWNLOAD_PORT_KEY, "");
        String upload_ip = sharedInstance.getString(Config.UPLOAD_IP_KEY, "");
        String upload_port = sharedInstance.getString(Config.UPLOAD_PORT_KEY, "");
        if (ToolString.isNoBlankAndNoNull(download_ip)) {
            tiet1.setText(download_ip);
        }
        if (ToolString.isNoBlankAndNoNull(download_port)) {
            tiet1.setText(download_port);
        }
        if (ToolString.isNoBlankAndNoNull(upload_ip)) {
            tiet1.setText(upload_ip);
        }
        if (ToolString.isNoBlankAndNoNull(upload_port)) {
            tiet1.setText(upload_port);
        }
    }

    @Override
    public void destroy() {

    }

    public void save_config(View view) {
        String et1 = tiet1.getText().toString().trim();
        if (!ToolRegex.checkIpAddress(et1)) {
            ToolAlert.toastShort("IP地址不规范！");
        }
        String et2 = tiet2.getText().toString().trim();
        Integer integer = Integer.valueOf(et2);
        if (integer <= 1024 || integer >= 65535) {
            ToolAlert.toastShort("端口不规范！");
        }
        String et3 = tiet3.getText().toString().trim();
        if (!ToolRegex.checkIpAddress(et3)) {
            ToolAlert.toastShort("IP地址不规范！");
        }
        String et4 = tiet4.getText().toString().trim();
        Integer integer4 = Integer.valueOf(et4);
        if (integer4 <= 1024 || integer4 >= 65535) {
            ToolAlert.toastShort("端口不规范！");
        }
        sharedInstance.edit().putString(Config.DOWNLOAD_IP_KEY, et1).apply();
        sharedInstance.edit().putString(Config.DOWNLOAD_PORT_KEY, et2).apply();
        sharedInstance.edit().putString(Config.UPLOAD_IP_KEY, et3).apply();
        sharedInstance.edit().putString(Config.UPLOAD_PORT_KEY, et4).apply();
    }
}
