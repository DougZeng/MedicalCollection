package com.gopher.meidcalcollection.common.base;

import android.content.BroadcastReceiver;

/**
 * Created by Administrator on 2017/11/17.
 */

public abstract class BaseBroadcastReceiver extends BroadcastReceiver {
    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();
}
