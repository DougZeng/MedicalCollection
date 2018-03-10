package com.gopher.meidcalcollection.common.db.dao;

import android.database.Cursor;
import android.util.Log;

import com.gopher.meidcalcollection.common.MApp;
import com.gopher.meidcalcollection.common.db.DBConstant;
import com.gopher.meidcalcollection.common.db.model.BaseDBModel;
import com.gopher.meidcalcollection.common.db.model.UserInfoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class UserInfoDao {
    public static final String TAG = UserInfoDao.class.getSimpleName();

    /**
     * 向表中添加一个数据
     */
    public long insert(UserInfoModel model) throws RuntimeException {
        long id = MApp.getDataBase().insertOrThrow(DBConstant.USERINFO, null,
                model.toContentValues());
        Log.e(TAG, "id = " + id);
//        model.setId(String.valueOf(id));
        return id;
    }

    /**
     * 向表中添加一组数据
     */
    public void insert(List<BaseDBModel> models) throws RuntimeException {
        try {
            MApp.getDataBase().beginTransaction();
            // 通过事务来设置插入操作
            for (int i = 0; i < models.size(); i++) {
                insert((UserInfoModel) models.get(i));
            }
            MApp.getDataBase().setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, e.toString());
        } finally {
            MApp.getDataBase().endTransaction();
        }
    }

    /**
     * 从表中删除指定记录
     *
     * @param model
     * @return 删除的记录数
     * @throws RuntimeException
     */
//    public int delete(UserInfoModel model) throws RuntimeException {
//        return MApp.getDataBase().delete(DBConstant.USERINFO, "Id = ?",
//                new String[]{model.getId()});
//    }

    /**
     * 删除指定数据
     *
     * @throws RuntimeException
     */
    public void delete(String cardCode) throws RuntimeException {
        String sql = "DELETE FROM " + DBConstant.USERINFO + " where cardCode='" + cardCode + "' ";
        MApp.getDataBase().execSQL(sql);
    }

    /**
     * 删除全部
     *
     * @throws RuntimeException
     */
    public void deleteAll() throws RuntimeException {
        String sql = "DELETE FROM " + DBConstant.USERINFO;
        MApp.getDataBase().execSQL(sql);
    }

    /**
     * 获取表中的全部记录
     *
     * @return
     * @throws RuntimeException
     */
    public List<UserInfoModel> findAll() throws RuntimeException {
        List<UserInfoModel> models = null;
        String sql = "SELECT * FROM " + DBConstant.USERINFO;
        Cursor cur = null;
        try {
            cur = MApp.getDataBase().rawQuery(sql, null);
            models = new ArrayList<UserInfoModel>();
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                UserInfoModel model = new UserInfoModel();
                model.fillByCursor(cur);
                models.add(model);
            }
        } catch (RuntimeException e) {
            models = null;
            throw e;
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
        return models;
    }

    /**
     * 更新指定记录
     *
     * @param model
     * @return 更新的记录数
     * @throws RuntimeException
     */
    public int update(UserInfoModel model) throws RuntimeException {
        return MApp.getDataBase().update(DBConstant.USERINFO, model.toContentValues(), "cardCode = ?",
                new String[]{model.getCardCode()});
    }

    public UserInfoModel findByCardCode(String card_code) throws RuntimeException {
        UserInfoModel model = null;
        String sql = "SELECT * FROM " + DBConstant.USERINFO + " WHERE cardCode = ?";
        Cursor cur = null;
        try {
            cur = MApp.getDataBase().rawQuery(sql, new String[]{card_code});
            if (cur.moveToFirst()) {
                model = new UserInfoModel();
                model.fillByCursor(cur);
            }
        } catch (RuntimeException e) {
            model = null;
            throw e;
        } finally {
            if (cur != null) {
                cur.close();
            }
        }
        return model;
    }
}
