package com.gopher.meidcalcollection.common.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gopher.meidcalcollection.common.TotalApp;
import com.gopher.meidcalcollection.common.util.Operation;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Administrator on 2017/11/17.
 */

public abstract class BaseFragment extends Fragment implements IBaseFragment {
    /**
     * 当前Fragment渲染的视图View
     **/
    private View mContextView = null;
    /**
     * 共通操作
     **/
    protected Operation mOperation = null;
    /**
     * 依附的Activity
     **/
    protected Activity mContext = null;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //缓存当前依附的activity
        mContext = activity;
        Logger.d("BaseFragment-->onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("BaseFragment-->onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.d("BaseFragment-->onCreateView()");
        // 渲染视图View
        if (null == mContextView) {
            //初始化参数
            initParms(getArguments());

            View mView = bindView();
            if (null == mView) {
                mContextView = inflater.inflate(bindLayout(), container, false);
            } else {
                mContextView = mView;
            }
            // 控件初始化
            initView(mContextView);
            // 实例化共通操作
            mOperation = new Operation(getActivity());
            // 业务处理
            doBusiness(getActivity());
        }

        return mContextView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Logger.d("BaseFragment-->onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Logger.d("BaseFragment-->onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        Logger.d("BaseFragment-->onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        Logger.d("BaseFragment-->onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        Logger.d("BaseFragment-->onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Logger.d("BaseFragment-->onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Logger.d("BaseFragment-->onDestroy()");
        super.onDestroy();
        RefWatcher refWatcher = TotalApp.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    @Override
    public void onDetach() {
        Logger.d("BaseFragment-->onDetach()");
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mContextView != null && mContextView.getParent() != null) {
            ((ViewGroup) mContextView.getParent()).removeView(mContextView);
        }
    }

    /**
     * 获取当前Fragment依附在的Activity
     *
     * @return
     */
    public Activity getContext() {
        return getActivity();
    }

    /**
     * 获取共通操作机能
     */
    public Operation getOperation() {
        return this.mOperation;
    }
}
