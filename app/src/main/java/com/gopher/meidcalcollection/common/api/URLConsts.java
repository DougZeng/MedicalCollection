package com.gopher.meidcalcollection.common.api;

import android.content.Context;

import com.gopher.meidcalcollection.common.constant.Config;
import com.gopher.meidcalcollection.common.util.ssp.SecuredPreferenceStore;

/**
 * Created by Administrator on 2017/12/5.
 */

public class URLConsts {

    private static SecuredPreferenceStore sharedInstance;

    public static void init(Context context) {
        sharedInstance = SecuredPreferenceStore.getSharedInstance();
    }

    private static String getDownloadIP() {
        String downIP = sharedInstance.getString(Config.DOWNLOAD_IP_KEY, "");
        return downIP=="" ? API.DOWNLOAD_IP : downIP;
    }

    private static String getDownloadPort() {
        String downloadPort = sharedInstance.getString(Config.DOWNLOAD_PORT_KEY, "");
        return downloadPort == "" ? API.DOWNLOAD_POINT : downloadPort;
    }

    private static String getUploadIP() {
        String uploadIP = sharedInstance.getString(Config.UPLOAD_IP_KEY, "");
        return uploadIP == "" ? API.UP_IP : uploadIP;
    }

    private static String getUploadPort() {
        String uploadPort = sharedInstance.getString(Config.UPLOAD_PORT_KEY, "");
        return uploadPort == "" ? API.UP_POINT : uploadPort;
    }


    public static String getUserInfoURL() {
        String url = "http://" + getDownloadIP() + ":" + getDownloadPort() + "/yf/rest/dataInterface/getUserInfo.rest";
        return url;
    }

    public static String getTypeURL() {
        String url = "http://" + getDownloadIP() + ":" + getDownloadPort() + "/yf/rest/dataInterface/getType.rest";
        return url;
    }

    public static String getTimeURL() {
        String url = "http://" + getDownloadIP() + ":" + getDownloadPort() + "/yf/rest/dataInterface/getCurrentTime.rest";
        return url;
    }
}
