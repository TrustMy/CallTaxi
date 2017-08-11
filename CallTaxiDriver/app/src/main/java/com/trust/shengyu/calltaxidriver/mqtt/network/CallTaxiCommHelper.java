package com.trust.shengyu.calltaxidriver.mqtt.network;

import android.content.Context;

import com.trust.shengyu.calltaxidriver.Config;
import com.trust.shengyu.calltaxidriver.mqtt.MqttCommHelper;

/**
 * Created by Trust on 2017/8/3.
 */

public class CallTaxiCommHelper extends MqttCommHelper {

    public CallTaxiCommHelper(Context context) {
        super(context);
    }

    @Override
    protected void iniServer() {
        host = Config.TestMqttServer;
        userName = Config.TestUserName;
        passWord = Config.TestPassWord;
        SubmitTopics = Config.TestTopics;
        clientId = Config.TestClientId;
    }
}
