package com.trust.shengyu.rentalcarclient.server.network;

import android.content.Context;

import com.trust.shengyu.rentalcarclient.Config;
import com.trust.shengyu.rentalcarclient.server.MqttCommHelper;

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
        submintTopics = Config.TestTopics;
        clientId = Config.TestClientId;
        sendTopic = Config.sendTopic;
    }
}
