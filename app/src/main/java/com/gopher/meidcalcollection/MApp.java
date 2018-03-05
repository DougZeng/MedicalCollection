package com.gopher.meidcalcollection;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.gopher.meidcalcollection.api.API;
import com.gopher.meidcalcollection.api.URLConsts;
import com.gopher.meidcalcollection.base.App;
import com.gopher.meidcalcollection.db.DBHelper;
import com.gopher.meidcalcollection.db.bill.TypeBill;
import com.gopher.meidcalcollection.db.bill.UserInfoBill;
import com.gopher.meidcalcollection.db.model.TypeModel;
import com.gopher.meidcalcollection.db.model.UserInfoModel;
import com.gopher.meidcalcollection.util.MyNetworkListener;
import com.gopher.meidcalcollection.util.ToolHTTP;
import com.gopher.meidcalcollection.util.ToolSocket;
import com.gopher.meidcalcollection.util.ToolString;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by Administrator on 2017/11/17.
 */

public class MApp extends App {
    protected static File externalAppDir;// 该应用程序在SD卡上文件根目录
    // 数据库实例
    private static SQLiteDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        externalAppDir = new File(Environment.getExternalStorageDirectory(), getPackageName());
        //启动Service
        Intent mIntent = new Intent(this, MyNetworkListener.class);
        startService(mIntent);
        getDataBase();
//        getData();

//        testUpload();
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

    private void getData() {
        final ExecutorService service = Executors.newFixedThreadPool(2);
        final String userInfoURL = URLConsts.getUserInfoURL();
        ToolHTTP.post(userInfoURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {

                if (!service.isShutdown()) {
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
                                    Log.i(TAG, "model = " + model.toString());
                                    long insert = instance.insert(model);
                                    Log.i(TAG, "insert = " + insert);
                                    statue = insert + "";

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.i(TAG, "status = " + statue);

                            return statue;
                        }

                    });
                    try {
                        String s = submit.get();
                        Log.i(TAG, "userInfoCallback " + s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable
                    throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

        ToolHTTP.post(URLConsts.getTypeURL(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONArray response) {
                if (!service.isShutdown()) {
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
                                    Log.i(TAG, "model = " + model.toString());

                                    long insert = instance.insert(model);
                                    Log.i(TAG, "insert = " + insert);
                                    statue = insert + "";

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.i(TAG, "status = " + statue);

                            return statue;
                        }
                    });
                    try {
                        String s = submit.get();
                        Log.i(TAG, "typeInfoCallback " + s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                service.shutdown();
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
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

    @Override
    public void exit() {
        try {
            //停止网络监听
            Intent mIntent = new Intent(this, MyNetworkListener.class);
            stopService(mIntent);

            //取消所有请求
            ToolHTTP.stopAllRequest();
            //关闭所有Activity
            removeAll();
            //退出进程
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
