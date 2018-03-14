package com.gopher.meidcalcollection.common.db.dao;

import android.database.Cursor;
import android.util.Log;

import com.gopher.meidcalcollection.common.TotalApp;
import com.gopher.meidcalcollection.common.db.DBConstant;
import com.gopher.meidcalcollection.common.db.model.BaseDBModel;
import com.gopher.meidcalcollection.common.db.model.TypeModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class TypeDao {
    public static final String TAG = TypeDao.class.getSimpleName();

    /**
     * 向表中添加一个数据
     */
    public long insert(TypeModel model) throws RuntimeException {
        long id = TotalApp.getDataBase().insertOrThrow(DBConstant.TYPE, null,
                model.toContentValues());
//        model.setId(String.valueOf(id));
        return id;
    }

    /**
     * 向表中添加一组数据
     */
    public void insert(List<BaseDBModel> models) throws RuntimeException {
        try {
            TotalApp.getDataBase().beginTransaction();
            // 通过事务来设置插入操作
            for (int i = 0; i < models.size(); i++) {
                insert((TypeModel) models.get(i));
            }
            TotalApp.getDataBase().setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, e.toString());
        } finally {
            TotalApp.getDataBase().endTransaction();
        }
    }

    /**
     * 从表中删除指定记录
     *
     * @param model
     * @return 删除的记录数
     * @throws RuntimeException
     */
//    public int delete(TypeModel model) throws RuntimeException {
//        return TotalApp.getDataBase().delete(DBConstant.TYPE, "Id = ?",
//                new String[]{model.getId()});
//    }

    /**
     * 删除指定数据
     *
     * @throws RuntimeException
     */
    public void delete(String typeId) throws RuntimeException {
        String sql = "DELETE FROM " + DBConstant.TYPE + " where typeId='" + typeId + "' ";
        TotalApp.getDataBase().execSQL(sql);
    }

    /**
     * 删除全部
     *
     * @throws RuntimeException
     */
    public void deleteAll() throws RuntimeException {
        String sql = "DELETE FROM " + DBConstant.TYPE;
        TotalApp.getDataBase().execSQL(sql);
    }

    /**
     * 获取表中的全部记录
     *
     * @return
     * @throws RuntimeException
     */
    public List<TypeModel> findAll() throws RuntimeException {
        List<TypeModel> models = null;
        String sql = "SELECT * FROM " + DBConstant.TYPE;
        Cursor cur = null;
        try {
            cur = TotalApp.getDataBase().rawQuery(sql, null);
            models = new ArrayList<TypeModel>();
            for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                TypeModel model = new TypeModel();
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
//    public int update(TypeModel model) throws RuntimeException {
//        return TotalApp.getDataBase().update(DBConstant.TYPE, model.toContentValues(), "Id = ?",
//                new String[]{model.getId()});
//    }
}
