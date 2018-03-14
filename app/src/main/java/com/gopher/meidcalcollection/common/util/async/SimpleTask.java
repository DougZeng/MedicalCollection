package com.gopher.meidcalcollection.common.util.async;

/**
 * Created by Administrator on 2018/3/10.
 * 简单的异步任务，仅仅指定返回结果的类型，不可输入参数
 */

public abstract class SimpleTask<T> extends AsyncTask<Object, Object, T> {

    @Override
    protected T doInBackground(Object... params) {
        return doInBackground();
    }

    protected abstract T doInBackground();
}
