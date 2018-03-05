package com.gopher.meidcalcollection.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gopher.meidcalcollection.MApp;

import java.io.File;

/**
 * Created by Administrator on 2017/12/4.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;
    static String path = new File(MApp.getExternalAppDir(),
            "databases" + File.separatorChar + DBConstant.DB_NAME)
            .getAbsolutePath();

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    /**
     * 在构造函数中创建数据库
     *
     * @return
     */
    public static DBHelper getInstance() {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper(MApp.gainContext(), path,
                            null, DBConstant.DB_VERSION);
                }
            }
        }
        return instance;
    }

    /**
     * 获取SQLiteDatabase对象
     *
     * @return
     */
    public static SQLiteDatabase getDatabase() {
        return getInstance().getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, DBConstant.DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 0:
                // 执行创表语句
                db.execSQL(DBConstant.sql_userinfo);
                db.execSQL(DBConstant.sql_type);

            case 1:
                // TODO　当版本升级到2的时候，在这里写

        }

    }
}
