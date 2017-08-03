package com.trust.shengyu.calltaxi.mqtt.network;

import android.content.Context;

import com.trust.shengyu.calltaxi.mqtt.MqttCommHelper;

/**
 * Created by Trust on 2017/8/3.
 */

public class CallTaxiCommHelper extends MqttCommHelper {

    public CallTaxiCommHelper(Context context) {
        super(context);
    }

    @Override
    protected void iniServer() {
        host = "tcp://192.168.1.12:61613";
        userName = "admin";
        passWord = "password";
        topics = new String[]{"trust","lh"};
        clientId = "trustDream";
    }
}
