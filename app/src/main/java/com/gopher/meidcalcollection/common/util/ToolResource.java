package com.gopher.meidcalcollection.common.util;

import android.content.Context;

import com.gopher.meidcalcollection.common.base.App;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/11/16.
 */

public class ToolResource {

    private static Context mContext = App.gainContext();

    private static Class<?> CDrawable = null;

    private static Class<?> CLayout = null;

    private static Class<?> CId = null;

    private static Class<?> CAnim = null;

    private static Class<?> CStyle = null;

    private static Class<?> CString = null;

    private static Class<?> CArray = null;

    static {
        try {
            CDrawable = Class.forName(mContext.getPackageName() + ".R$drawable");
            CLayout = Class.forName(mContext.getPackageName() + ".R$layout");
            CId = Class.forName(mContext.getPackageName() + ".R$id");
            CAnim = Class.forName(mContext.getPackageName() + ".R$anim");
            CStyle = Class.forName(mContext.getPackageName() + ".R$style");
            CString = Class.forName(mContext.getPackageName() + ".R$string");
            CArray = Class.forName(mContext.getPackageName() + ".R$array");

        } catch (ClassNotFoundException e) {
            Logger.i(e.getMessage());
        }
    }

    public static int getDrawableId(String resName) {
        return getResId(CDrawable, resName);
    }

    public static int getLayoutId(String resName) {
        return getResId(CLayout, resName);
    }

    public static int getIdId(String resName) {
        return getResId(CId, resName);
    }

    public static int getAnimId(String resName) {
        return getResId(CAnim, resName);
    }

    public static int getStyleId(String resName) {
        return getResId(CStyle, resName);
    }

    public static int getStringId(String resName) {
        return getResId(CString, resName);
    }

    public static int getArrayId(String resName) {
        return getResId(CArray, resName);
    }

    private static int getResId(Class<?> resClass, String resName) {
        if (resClass == null) {
            Logger.e("getRes(null, %s )", resName);
            throw new IllegalArgumentException("ResClass is not initialized. Please make sure you have added neccessary resources. Also make sure you have " + mContext.getPackageName() + ".R$* configured in obfuscation. field=" + resName);
        }

        try {
            Field field = resClass.getField(resName);
            return field.getInt(resName);
        } catch (Exception e) {
            Logger.e( "getRes(" + resClass.getName() + ", " + resName + ")");
            Logger.e( "Error getting resource. Make sure you have copied all resources (res/) from SDK to your project. ");
            Logger.e( e.getMessage());
        }

        return -1;
    }
}
