package com.gopher.meidcalcollection.common.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.gopher.meidcalcollection.common.util.SysEnv;
import com.gopher.meidcalcollection.common.util.ToolNetwork;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Administrator on 2017/11/16.
 */

public abstract class App extends Application {
    /**
     * 对外提供整个应用生命周期的Context
     **/
    private static Context instance;
    /**
     * 整个应用全局可访问数据集合
     **/
    private static Map<String, Object> gloableData = new HashMap<String, Object>();
    /***寄存整个应用Activity**/
    private static final Stack<WeakReference<Activity>> activitys = new Stack<WeakReference<Activity>>();
    /**
     * 日志输出标志
     **/
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 应用程序版本versionName
     **/
    public static String version = "error";
    /**
     * 设备ID
     **/
    public static String deviceId = "error";

    /**
     * ImageLoader
     **/
    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();

    }

    /**
     * 初始化工作
     */
    private void init() {
        //图片加载器

        try {
            //应用程序版本
            version = SysEnv.getVersionName();
            //设备ID
            deviceId = SysEnv.DEVICE_ID;

        } catch (Exception e) {
            Log.e(TAG, "初始化设备ID、获取应用程序版本失败，原因：" + e.getMessage());
        }
    }

    /**
     * 获取ImageLoader
     *
     * @return
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


    /**
     * 获取网络是否已连接
     *
     * @return
     */
    public static boolean isNetworkReady() {
        return ToolNetwork.getInstance().init(instance).isConnected();
    }

    /**
     * 获取网络是否已连接
     *
     * @return
     */
    public static boolean isNetworkReady(Context mContext) {
        return ToolNetwork.getInstance().init(mContext).isConnected();
    }

    /**
     * 对外提供Application Context
     *
     * @return
     */
    public static Context gainContext() {
        return instance;
    }

    /**
     * 往Application放置数据（最大不允许超过5个）
     *
     * @param strKey   存放属性Key
     * @param strValue 数据对象
     */
    public static void assignData(String strKey, Object strValue) {
        if (gloableData.size() > 5) {
            throw new RuntimeException("超过允许最大数");
        }
        gloableData.put(strKey, strValue);
    }

    /**
     * 从Applcaiton中取数据
     *
     * @param strKey 存放数据Key
     * @return 对应Key的数据对象
     */
    public static Object gainData(String strKey) {
        return gloableData.get(strKey);
    }

    /**
     * 将Activity压入Application栈
     *
     * @param task 将要压入栈的Activity对象
     */
    public void pushTask(WeakReference<Activity> task) {
        activitys.push(task);
    }

    /**
     * 将传入的Activity对象从栈中移除
     *
     * @param task
     */
    public static void removeTask(WeakReference<Activity> task) {
        activitys.remove(task);
    }

    /**
     * 移除全部（用于整个应用退出）
     */
    public void removeAll() {
        //finish所有的Activity
        for (WeakReference<Activity> task : activitys) {
            Activity mActivity = task.get();
            if (null != mActivity && !mActivity.isFinishing()) {
                mActivity.finish();
            }
        }
    }

    /**
     * 退出整个APP，关闭所有activity/清除缓存等等
     */
    public abstract void exit();
}
