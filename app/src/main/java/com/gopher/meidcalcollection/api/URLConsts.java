package com.gopher.meidcalcollection.api;

/**
 * Created by Administrator on 2017/12/5.
 */

public class URLConsts {
    public static String getUserInfoURL() {
        String url = "http://" + API.DOWNLOAD_IP + ":" + API.DOWNLOAD_POINT + "/yf/rest/dataInterface/getUserInfo.rest";
        return url;
    }

    public static String getTypeURL() {
        String url = "http://" + API.DOWNLOAD_IP + ":" + API.DOWNLOAD_POINT + "/yf/rest/dataInterface/getType.rest";
        return url;
    }
}
