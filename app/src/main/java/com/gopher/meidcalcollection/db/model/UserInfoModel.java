package com.gopher.meidcalcollection.db.model;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Administrator on 2017/12/4.
 */

public class UserInfoModel extends BaseDBModel {

//    private String id;
    private String loginName;
    private String name;
    private String depName;
    private String hosName;
    private String cardCode;
    private String cardType;

//    public String getId() {
//        return id;
//    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getHosName() {
        return hosName;
    }

    public void setHosName(String hosName) {
        this.hosName = hosName;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return "UserInfoModel{" +
//                "id='" + id + '\'' +
                ", loginName='" + loginName + '\'' +
                ", name='" + name + '\'' +
                ", depName='" + depName + '\'' +
                ", hosName='" + hosName + '\'' +
                ", cardCode='" + cardCode + '\'' +
                ", cardType='" + cardType + '\'' +
                '}';
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
//        values.put("id", getId());
        values.put("loginName", getLoginName());
        values.put("name", getName());
        values.put("depName", getDepName());
        values.put("hosName", getHosName());
        values.put("cardCode", getCardCode());
        values.put("cardType", getCardType());
        return values;
    }

    public void fillByCursor(Cursor cursor) {
//        setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
        setLoginName(cursor.getString(cursor.getColumnIndexOrThrow("loginName")));
        setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        setDepName(cursor.getString(cursor.getColumnIndexOrThrow("depName")));
        setHosName(cursor.getString(cursor.getColumnIndexOrThrow("hosName")));
        setCardCode(cursor.getString(cursor.getColumnIndexOrThrow("cardCode")));
        setCardType(cursor.getString(cursor.getColumnIndexOrThrow("cardType")));
    }
}
