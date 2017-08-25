package com.trust.shengyu.calltaxidriver.tools.beans;

/**
 * Created by Trust on 2017/8/25.
 */

public class ErrorBean {

    /**
     * status : 0
     * info : 该订单已被取消
     */

    private int status;
    private String info;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }
}
