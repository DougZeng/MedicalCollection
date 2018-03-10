package com.gopher.meidcalcollection.common.db.bill;

import android.util.Log;

import com.gopher.meidcalcollection.common.db.dao.TypeDao;
import com.gopher.meidcalcollection.common.db.model.BaseDBModel;
import com.gopher.meidcalcollection.common.db.model.TypeModel;

import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class TypeBill {
    public static final String TAG = TypeBill.class.getSimpleName();
    private static TypeBill instances;
    private TypeDao typeDao;

    private TypeBill() {
        typeDao = new TypeDao();
    }

    public static synchronized TypeBill getInstance() {
        if (instances == null) {
            instances = new TypeBill();
        }
        return instances;
    }

    public long insert(TypeModel model) {
        long id = -1;
        try {
            id = typeDao.insert(model);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return id;
    }

    /**
     * 批量插入
     */
    public boolean insert(List<BaseDBModel> model) {
        try {
            typeDao.insert(model);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
        return true;
    }

    /**
     * 删除全部记录
     *
     * @return -1：出现异常；0：未处理；1：成功
     */
    public int deleteAll() {
        int result = 0;
        try {
            typeDao.deleteAll();
            result = 1;
        } catch (Exception e) {
            result = -1;
            // TODO 记录异常日志
            Log.e(TAG, e.toString());
        }
        return result;
    }

    /**
     * 删除cardCode
     *
     * @return -1：出现异常；0：未处理；1：成功
     */
    public int delete(String typeId) {
        int result = 0;
        try {
            typeDao.delete(typeId);
            result = 1;
        } catch (Exception e) {
            result = -1;
            // TODO 记录异常日志
            Log.e(TAG, e.toString());
        }
        return result;
    }

    /**
     * 更新指定记录
     *
     * @param model
     * @return 更新的记录数，或者-1
     */
//    public synchronized int update(TypeModel model) {
//        int count = 0;
//        try {
//            count = typeDao.update(model);
//        } catch (RuntimeException e) {
//            // TODO 添加日志
//            count = -1;
//            Log.e(TAG, e.toString());
//        }
//        return count;
//    }


    /**
     * @return
     */
    public List<TypeModel> findAll() {
        List<TypeModel> models = null;
        try {
            models = typeDao.findAll();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return models;
    }
}
