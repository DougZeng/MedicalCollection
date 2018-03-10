package com.gopher.meidcalcollection.common;

/**
 * Created by Administrator on 2017/11/16.
 */

public class DTB<V, K> extends DTO<K, V> {
    private String name;

    private String nameCN;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCN() {
        return nameCN;
    }

    public void setNameCN(String nameCN) {
        this.nameCN = nameCN;
    }
}
