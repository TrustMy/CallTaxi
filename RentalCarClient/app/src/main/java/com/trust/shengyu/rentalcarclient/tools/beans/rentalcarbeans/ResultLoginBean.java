package com.trust.shengyu.rentalcarclient.tools.beans.rentalcarbeans;

/**
 * Created by Trust on 2017/9/7.
 */

public class ResultLoginBean {

    /**
     * status : 1
     * info : 登录成功
     * code : 1002
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
