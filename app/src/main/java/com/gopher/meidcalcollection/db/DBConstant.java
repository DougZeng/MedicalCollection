package com.gopher.meidcalcollection.db;

/**
 * Created by Administrator on 2017/12/4.
 */

public class DBConstant {
    public static final String DB_NAME = "medicalcollection.db";
    public static final int DB_VERSION = 1;
    //表名
    public static final java.lang.String USERINFO = "UserInfo";
    public static final java.lang.String TYPE = "Type";
    //字段名
    public static final String id = "id";
    //UserInfo
    public static final String LOGINNAME = "loginName";
    public static final String NAME = "name";
    public static final String DEPNAME = "depName";
    public static final String HOSNAME = "hosName";
    public static final String CARDCODE = "cardCode";
    public static final String CARDTYPE = "cardType";
    //Type

    public static final String TYPEID = "typeId";
    public static final String TYPENAME = "typeName";

    //建表
    public static final String sql_userinfo = "CREATE TABLE IF NOT EXISTS "
            + USERINFO + " ("
//            + id+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LOGINNAME + " TEXT, "
            + NAME + " TEXT, "
            + DEPNAME + " TEXT, "
            + HOSNAME + " TEXT, "
            + CARDCODE + " TEXT, "
            + CARDTYPE + " TEXT)";

    public static final String sql_type = "CREATE TABLE IF NOT EXISTS "
            + TYPE + " ("
//            + id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TYPEID + " TEXT, "
            + TYPENAME + " TEXT)";

}
