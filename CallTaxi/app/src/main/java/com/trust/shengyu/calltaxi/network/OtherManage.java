package com.trust.shengyu.calltaxi.network;

import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.mqtt.TrustServer;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.beans.MqttBean;

/**
 * Created by Trust on 2017/8/13.
 */

public class OtherManage extends Management {

    public OtherManage(Management supperManage, TrustServer trustServer) {
        super(supperManage, trustServer);
    }

    @Override
    protected void interceptor(MqttBean bean) {
        L.d("其他 topic 类型");
        dealWith(null);
    }

    @Override
    protected void dealWith(MqttBean bean) {

    }
}
