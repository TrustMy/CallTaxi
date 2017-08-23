package com.trust.shengyu.calltaxidriver.tools.beans;

/**
 * Created by Trust on 2017/8/21.
 */

public class TypeBean {
    private int types,rawId;
    private String serialNos;

    public String getSerialNos() {
        return serialNos;
    }

    public void setSerialNos(String serialNos) {
        this.serialNos = serialNos;
    }

    public int getRawId() {
        return rawId;
    }

    public void setRawId(int rawId) {
        this.rawId = rawId;
    }

    public int getTypes() {
        return types;
    }

    public void setTypes(int types) {
        this.types = types;
    }

}
