package com.trust.shengyu.calltaxidriver.tools.beans;

/**
 * Created by Trust on 2017/8/13.
 */

public class MqttBean extends Bean {
    private String topic , msg;

    public MqttBean(String topic, String msg) {
        this.topic = topic;
        this.msg = msg;
    }


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
