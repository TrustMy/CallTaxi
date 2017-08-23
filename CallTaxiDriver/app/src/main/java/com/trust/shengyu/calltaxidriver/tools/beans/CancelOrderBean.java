package com.trust.shengyu.calltaxidriver.tools.beans;

/**
 * Created by Trust on 2017/8/18.
 */

public class CancelOrderBean {

    /**
     * status : 1
     * info : 订单取消成功
     * code :
     */

    private int status;
    private String info;
    private String code;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public String getInfo() {
        return info;
    }

    public String getCode() {
        return code;
    }
}
