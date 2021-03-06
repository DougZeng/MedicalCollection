package com.gopher.meidcalcollection.common.db.bill;


import com.gopher.meidcalcollection.common.db.dao.UserInfoDao;
import com.gopher.meidcalcollection.common.db.model.BaseDBModel;
import com.gopher.meidcalcollection.common.db.model.UserInfoModel;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class UserInfoBill {

    private static UserInfoBill instance;
    private UserInfoDao userInfoDao;

    private UserInfoBill() {
        super();
        userInfoDao = new UserInfoDao();
    }

    public static synchronized UserInfoBill getInstance() {
        if (instance == null) {
            instance = new UserInfoBill();
        }
        return instance;
    }

    public long insert(UserInfoModel model) {
        long id = -1;
        try {
            id = userInfoDao.insert(model);
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return id;
    }

    /**
     * 批量插入
     */
    public boolean insert(List<BaseDBModel> model) {
        try {
            userInfoDao.insert(model);
        } catch (Exception e) {
            Logger.e(e.toString());
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
            userInfoDao.deleteAll();
            result = 1;
        } catch (Exception e) {
            result = -1;
            // TODO 记录异常日志
            Logger.e(e.toString());
        }
        return result;
    }

    /**
     * 删除cardCode
     *
     * @return -1：出现异常；0：未处理；1：成功
     */
    public int delete(String cardCode) {
        int result = 0;
        try {
            userInfoDao.delete(cardCode);
            result = 1;
        } catch (Exception e) {
            result = -1;
            // TODO 记录异常日志
            Logger.e(e.toString());
        }
        return result;
    }

    /**
     * 更新指定记录
     *
     * @param model
     * @return 更新的记录数，或者-1
     */
    public synchronized int update(UserInfoModel model) {
        int count = 0;
        try {
            count = userInfoDao.update(model);
        } catch (RuntimeException e) {
            // TODO 添加日志
            count = -1;
            Logger.e(e.toString());
        }
        return count;
    }


    /**
     * @return
     */
    public List<UserInfoModel> findAll() {
        List<UserInfoModel> models = null;
        try {
            models = userInfoDao.findAll();
        } catch (Exception e) {
            Logger.e(e.toString());
        }
        return models;
    }

    public synchronized UserInfoModel findByCardCode(String card_code) {
        UserInfoModel model;
        try {
            model = userInfoDao.findByCardCode(card_code);
        } catch (Exception e) {
            model = null;
            Logger.e(e.toString());
        }
        return model;
    }
}
