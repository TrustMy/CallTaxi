package com.trust.shengyu.calltaxi.network;

import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.mqtt.TrustServer;
import com.trust.shengyu.calltaxi.tools.beans.MqttBean;

/**
 * Created by Trust on 2017/8/13.
 */

public class OrderTopicManage extends Management {


    public OrderTopicManage(Management supperManage, TrustServer trustServer) {
        super(supperManage, trustServer);
    }

    @Override
    protected void interceptor(MqttBean bean) {
        if (bean.getTopic().equals(Config.TestTopics[0])){
            dealWith(bean);
        }else {
            supperManage.interceptor(bean);
        }
    }

    @Override
    protected void dealWith(MqttBean bean) {
//        trustServer.filterType(bean.getMsg());
    }


}
