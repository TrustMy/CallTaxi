package com.trust.shengyu.calltaxi.mqtt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.base.BaseActivity;
import com.trust.shengyu.calltaxi.mqtt.network.CallTaxiCommHelper;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.beans.MqttResultBean;

/**
 * Created by Trust on 2017/8/3.
 */

public class TrustServer extends Service {
    private Binder trustBinder = new TrustBinder();
    private Context context  = this;
    protected  CallTaxiCommHelper callTaxiCommHelper;
    protected Gson gson;
    public static BaseActivity baseActivity;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return trustBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (callTaxiCommHelper == null) {
            callTaxiCommHelper = new CallTaxiCommHelper(context);
            callTaxiCommHelper.setOnMqttCallBackResultListener(onMqttCallBackResultListener);
            callTaxiCommHelper.doClientConnection();
        }

    }

    //mqtt 收到push 回调
    protected   MqttCommHelper.onMqttCallBackResultListener onMqttCallBackResultListener = new MqttCommHelper.onMqttCallBackResultListener() {
        @Override
        public void CallBack(String topic, final Object msg) {

            Config.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //此时已在主线程中，可以更新UI了
                    MqttResultBean bean =  gson.fromJson(msg.toString(), MqttResultBean.class);

                    if (bean.getStatus()){
                        switch (bean.getType()){
                            case Config.MQTT_TYPE_PLACE_AN_ORDER:
                                baseActivity.resultMqttTypePlaceAnOrder(null);
                                break;
                            case Config.MQTT_TYPE_START_ORDER:
                                baseActivity.resultMqttTypeStartOrder(bean);
                                break;
                            case Config.MQTT_TYPE_END_ORDER:
                                baseActivity.resultMqttTypeEndOrder(bean);
                                break;
                            case Config.MQTT_TYPE_REFUSED_ORDER:
                                baseActivity.resultMqttTypeRefusedOrder(bean);
                                break;
                            default:
                                baseActivity.resultMqttTypeOther(bean);
                                break;
                        }
                        baseActivity.dissDialog();
                    }else{
//                        resultErrorMqtt("false:"+bean.getErrorMsg());
                    }
                }
            });
        }
    };






    public class TrustBinder extends Binder{
        public TrustServer getService(){
            return TrustServer.this;
        }
    }
}
