package com.gopher.meidcalcollection.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/11/16.
 */

public class DTO<K, V> extends HashMap<K, V> implements Serializable {

    /**
     * 赋值
     *
     * @param objKey   键值
     * @param objValue 对应值
     */
    public V put(K objKey, V objValue) {
        if (readonly) {
            throw new RuntimeException("属性只读");
        } else {
            return super.put(objKey, objValue);
        }

    }

    /**
     * 只读开关
     */
    private boolean readonly = false;

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    /**
     * 移除空值的Item
     */
    public void removeEmptyValueItem() {
        for (Iterator<K> iterator = keySet().iterator(); iterator.hasNext(); ) {
            Object key = iterator.next();
            if (null == get(key) || "".equals(String.valueOf(get(key)))) {
                remove(key);
            }
        }
    }
}
