package com.trust.shengyu.rentalcarclient.tools.beans.rentalcarbeans;

/**
 * Created by Trust on 2017/9/7.
 */

public class ResultErrorBean {

    /**
     * status : 0
     * info : 用户名或者密码错误
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
