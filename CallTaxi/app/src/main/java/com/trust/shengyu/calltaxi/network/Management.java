package com.trust.shengyu.calltaxi.network;

import com.trust.shengyu.calltaxi.mqtt.TrustServer;
import com.trust.shengyu.calltaxi.tools.beans.MqttBean;

/**
 * Created by Trust on 2017/8/13.
 */

public abstract class Management {
    protected  Management supperManage;
    protected TrustServer trustServer;

    public Management(Management supperManage, TrustServer trustServer) {
        this.supperManage = supperManage;
        this.trustServer = trustServer;
    }

    protected  abstract void interceptor(MqttBean bean);

    protected  abstract void dealWith(MqttBean bean);
}
