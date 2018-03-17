package com.gopher.meidcalcollection.common;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.widget.TextClock;
import android.widget.TextView;

import com.gopher.meidcalcollection.BuildConfig;
import com.gopher.meidcalcollection.common.base.BaseActivity;
import com.gopher.meidcalcollection.common.constant.Config;
import com.gopher.meidcalcollection.common.api.API;
import com.gopher.meidcalcollection.common.api.URLConsts;
import com.gopher.meidcalcollection.common.base.App;
import com.gopher.meidcalcollection.common.db.DBHelper;
import com.gopher.meidcalcollection.common.db.bill.TypeBill;
import com.gopher.meidcalcollection.common.db.bill.UserInfoBill;
import com.gopher.meidcalcollection.common.db.model.TypeModel;
import com.gopher.meidcalcollection.common.db.model.UserInfoModel;
import com.gopher.meidcalcollection.common.util.MyNetworkListener;
import com.gopher.meidcalcollection.common.util.ToolAlert;
import com.gopher.meidcalcollection.common.util.ToolHTTP;
import com.gopher.meidcalcollection.common.util.ToolSocket;
import com.gopher.meidcalcollection.common.util.ToolString;
import com.gopher.meidcalcollection.common.util.ssp.DefaultRecoveryHandler;
import com.gopher.meidcalcollection.common.util.ssp.SecuredPreferenceStore;
import com.gopher.meidcalcollection.menu.MenuActivity;
import com.gopher.meidcalcollection.recyclecart.RecycleCart;
import com.gopher.meidcalcollection.transfercar.TransferCar;
import com.gopher.meidcalcollection.transferworkshop.TransferWorkshop;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.Screen;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by Administrator on 2017/11/17.
 */

public class TotalApp extends App {
    protected static File externalAppDir;// 该应用程序在SD卡上文件根目录
    // 数据库实例
    private static SQLiteDatabase database;
    private static TotalApp totalApp;

    public static RefWatcher getRefWatcher(Context context) {
        TotalApp application = (TotalApp) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    ExecutorService service;

    @Override
    public void onCreate() {
        super.onCreate();
        totalApp = this;
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
        refWatcher = LeakCanary.install(this);

        try {
            SecuredPreferenceStore.init(gainContext(), new DefaultRecoveryHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        externalAppDir = new File(Environment.getExternalStorageDirectory(), getPackageName());

        //启动Service
        Intent mIntent = new Intent(this, MyNetworkListener.class);
        startService(mIntent);

        service = Executors.newFixedThreadPool(5);
        getDataBase();
        URLConsts.init(this);
        String userInfoURL = URLConsts.getUserInfoURL();
        String typeURL = URLConsts.getTypeURL();
        String timeURL = URLConsts.getTimeURL();
        getData(userInfoURL, typeURL, timeURL);

//        testUpload();
        saveConfig();
        showConfig();


        TextClock textClock = new TextClock(this);
        textClock.setTextSize(18);
        textClock.setTextColor(Color.parseColor("#FFFF0000"));//red
        textClock.setFormat24Hour("yyyy-MM-dd HH:mm:ss");//"yyyy-MM-dd hh:mm, EEEE"
        FloatWindow
                .with(getApplicationContext())
                .setView(textClock)
                .setWidth(300)                   //100px
                .setHeight(Screen.width, 0.4f)    //屏幕宽度的 20%
                .setX(100)                       //100px
                .setY(Screen.height, 0.1f)        //屏幕高度的 30%
                .setFilter(true, BaseActivity.class)
                .setDesktopShow(false)                //默认 false
                .setMoveType(MoveType.inactive )         //不可拖动
                .build();
    }

    private void showConfig() {
        Logger.i("showConfig: %d", System.currentTimeMillis());
        SecuredPreferenceStore sharedInstance = SecuredPreferenceStore.getSharedInstance();
        String string = sharedInstance.getString(Config.MODE_KEY, "");
        Logger.i("showConfig: %s", string);
        Logger.i("showConfig: %d", System.currentTimeMillis());
        ToolAlert.toastShort(string);
    }

    private void saveConfig() {
        Logger.i("showConfig: %d", System.currentTimeMillis());
        SecuredPreferenceStore preferenceStore = SecuredPreferenceStore.getSharedInstance();
        preferenceStore.edit().putString(Config.MODE_KEY, Config.MODE_CART).apply();
        Logger.i("showConfig: %d", System.currentTimeMillis());
    }

    private void testUpload() {
        String datas = "##0118ST=22;" +
                "CN=2011;PW=123456;" +
                "MN=010021705080003;" +
                "CP=&&DataTime=20171128105111-20171128105139;" +
                "operatorMan=42010300001000710071;" +
                "handoverMan=42010100001000051370;" +
                "type=44-44;" +
                "labelid=44000000055668-44000000055669;" +
                "weight-Rtd=6.90-12.49," +
                "weight-Flag=N;&&0000";
        ToolSocket instance = ToolSocket.getInstance();
        instance.setAutoClose(true);
        byte[] bytes = instance.doSocket(API.UP_IP, API.UP_POINT, ToolString.stringToByte(datas, ToolString.getCodeType(1)));
    }

    private void getData(String userInfoURL, String typeURL, String timeURL) {
        initInfo(userInfoURL);
        initTypeInfo(typeURL);
//        initTime(timeURL);
    }

    private void initTime(String timeURL) {
        ToolHTTP.get(timeURL, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                super.onSuccess(statusCode, headers, responseString);
                Logger.d(responseString);
            }
        });
    }

    private void initTypeInfo(String typeURL) {
        ToolHTTP.post(typeURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Logger.json(response.toString());
                Future<String> submit = service.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        String statue = "";
                        TypeBill instance = TypeBill.getInstance();
                        instance.deleteAll();
                        TypeModel model = new TypeModel();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                model.setTypeId(jsonObject.optString("typeId"));
                                model.setTypeName(jsonObject.optString("typeName"));
                                Logger.json(jsonObject.toString());

                                long insert = instance.insert(model);
                                Logger.d("insert = %d", insert);
                                statue = insert + "";

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Logger.i("status = %s", statue);

                        return statue;
                    }
                });
                try {
                    String s = submit.get();
                    Logger.i("typeInfoCallback %s", s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void initInfo(String userInfoURL) {


        ToolHTTP.post(userInfoURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Logger.json(response.toString());
                Future<String> submit = service.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        String statue = "";
                        UserInfoBill instance = UserInfoBill.getInstance();
                        instance.deleteAll();
                        UserInfoModel model = new UserInfoModel();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                model.setLoginName(jsonObject.optString("login_name"));
                                model.setName(jsonObject.optString("name"));
                                model.setDepName(jsonObject.optString("dep_name"));
                                model.setHosName(jsonObject.optString("hos_name"));
                                String card_code = jsonObject.optString("card_code");
                                model.setCardCode(card_code);
                                model.setCardType(jsonObject.optInt("card_type") + "");
                                Logger.json(jsonObject.toString());
                                long insert = instance.insert(model);
                                Logger.i("insert = %d", insert);
                                statue = insert + "";

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Logger.i("status = %s" + statue);

                        return statue;
                    }

                });
                try {
                    String s = submit.get();
                    Logger.i("userInfoCallback %s" + s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public static SQLiteDatabase getDataBase() {
        if (database == null) {
            database = DBHelper.getDatabase();
        }
        return database;
    }


    /**
     * 获取该应用程序在SD卡上文件根目录
     *
     * @return
     */
    public static File getExternalAppDir() {
        if (!externalAppDir.exists() || !externalAppDir.isDirectory()) {
            externalAppDir.mkdirs();
        }
        return externalAppDir;
    }

    public static TotalApp getMApp() {
        return totalApp;
    }

    @Override
    public void exit() {
        try {
            //停止网络监听
            Intent mIntent = new Intent(this, MyNetworkListener.class);
            stopService(mIntent);

            //取消所有请求
            ToolHTTP.stopAllRequest();

            //销毁
            FloatWindow.destroy();
            //关闭所有Activity
            removeAll();
            //退出进程
            System.exit(0);
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }
}
