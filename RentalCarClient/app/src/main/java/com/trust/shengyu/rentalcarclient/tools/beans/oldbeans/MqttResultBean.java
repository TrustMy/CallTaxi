package com.trust.shengyu.rentalcarclient.tools.beans.oldbeans;

/**
 * Created by Trust on 2017/8/8.
 */

public class MqttResultBean extends Bean {

    /**
     * type : 1
     * status : true
     */

    private int type;
    private boolean status;
    private String msg;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public boolean getStatus() {
        return status;
    }
}
