package com.gopher.meidcalcollection.db.model;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by Administrator on 2017/12/4.
 */

public class TypeModel extends BaseDBModel {
    //    private String id;
    private String typeId;
    private String typeName;

//    public String getId() {
//        return id;
//    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "TypeModel{" +
                "typeId='" + typeId + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
//        values.put("id", getId());
        values.put("typeId", getTypeId());
        values.put("typeName", getTypeName());
        return values;
    }

    public void fillByCursor(Cursor cursor) {
//        setId(cursor.getString(cursor.getColumnIndexOrThrow("id")));
        setTypeId(cursor.getString(cursor.getColumnIndexOrThrow("typeId")));
        setTypeName(cursor.getString(cursor.getColumnIndexOrThrow("typeName")));
    }
}
