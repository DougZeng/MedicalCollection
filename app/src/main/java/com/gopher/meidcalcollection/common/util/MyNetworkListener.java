package com.gopher.meidcalcollection.common.util;

import com.gopher.meidcalcollection.common.MApp;
import com.gopher.meidcalcollection.common.NetworkStateService;

/**
 * Created by Administrator on 2017/11/17.
 */

public class MyNetworkListener extends NetworkStateService {
    @Override
    public void onNoNetwork() {
        ToolAlert.toastShort(MApp.gainContext(), "OMG 木有网络了~~");
    }

    @Override
    public void onNetworkChange(String networkType) {
        ToolAlert.toastShort(MApp.gainContext(), "当前网络：" + networkType);
    }

}
