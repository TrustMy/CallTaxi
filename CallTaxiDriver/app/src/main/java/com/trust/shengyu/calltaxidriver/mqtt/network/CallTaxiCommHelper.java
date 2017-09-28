package com.trust.shengyu.calltaxidriver.mqtt.network;

import android.content.Context;

import com.trust.shengyu.calltaxidriver.Config;
import com.trust.shengyu.calltaxidriver.mqtt.MqttCommHelper;
import com.trust.shengyu.calltaxidriver.tools.L;

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
        clientId = Config.driver + "";
        SubmitTopic = "book/order/"+Config.driver;
        Config.CSubmitTopic = SubmitTopic;
        L.d("mqtt  初始化 :"+SubmitTopic);
    }
}
