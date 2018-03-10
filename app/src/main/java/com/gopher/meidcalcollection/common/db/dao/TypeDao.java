package com.gopher.meidcalcollection.common.db.dao;

import android.database.Cursor;
import android.util.Log;

import com.gopher.meidcalcollection.common.MApp;
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
        long id = MApp.getDataBase().insertOrThrow(DBConstant.TYPE, null,
                model.toContentValues());
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
                insert((TypeModel) models.get(i));
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
//    public int delete(TypeModel model) throws RuntimeException {
//        return MApp.getDataBase().delete(DBConstant.TYPE, "Id = ?",
//                new String[]{model.getId()});
//    }

    /**
     * 删除指定数据
     *
     * @throws RuntimeException
     */
    public void delete(String typeId) throws RuntimeException {
        String sql = "DELETE FROM " + DBConstant.TYPE + " where typeId='" + typeId + "' ";
        MApp.getDataBase().execSQL(sql);
    }

    /**
     * 删除全部
     *
     * @throws RuntimeException
     */
    public void deleteAll() throws RuntimeException {
        String sql = "DELETE FROM " + DBConstant.TYPE;
        MApp.getDataBase().execSQL(sql);
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
            cur = MApp.getDataBase().rawQuery(sql, null);
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
//        return MApp.getDataBase().update(DBConstant.TYPE, model.toContentValues(), "Id = ?",
//                new String[]{model.getId()});
//    }
}
