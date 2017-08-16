package com.trust.shengyu.calltaxidriver.mqtt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.trust.shengyu.calltaxidriver.Config;
import com.trust.shengyu.calltaxidriver.base.BaseActivity;
import com.trust.shengyu.calltaxidriver.mqtt.network.CallTaxiCommHelper;
import com.trust.shengyu.calltaxidriver.tools.L;
import com.trust.shengyu.calltaxidriver.tools.beans.MqttBean;
import com.trust.shengyu.calltaxidriver.tools.beans.MqttResultBean;
import com.trust.shengyu.calltaxidriver.tools.beans.OrderBean;


import java.util.LinkedList;

/**
 * Created by Trust on 2017/8/3.
 */

public class TrustServer extends Service {
    private Binder trustBinder = new TrustBinder();
    private Context context  = this;
    protected CallTaxiCommHelper callTaxiCommHelper;
    protected Gson gson;
    public static BaseActivity baseActivity;
    LinkedList<MqttBean> orderTaskQueue = new LinkedList();
    LinkedList<MqttBean> otherTaskQueue = new LinkedList();
    public static boolean appStatus = false;
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
            gson = new Gson();
        }

    }

    //mqtt 收到push 回调
    protected   MqttCommHelper.onMqttCallBackResultListener onMqttCallBackResultListener = new MqttCommHelper.onMqttCallBackResultListener() {
        @Override
        public void CallBack(String topic, final Object msg) {
            L.d("topic:"+topic+"|msg:"+msg);
            if(!appStatus){
                //分类 通知类型
                if (topic.equals(Config.TestTopics[0])){
                    orderTaskQueue.add(new MqttBean(topic,msg.toString()));
                }else{
                    otherTaskQueue.add(new MqttBean(topic,msg.toString()));
                }
            }else{
                //分类 通知类型
                if (topic.equals(Config.TestTopics[0])){
                    resultMqttTypr(msg.toString());
                }else{
                    //其他同志
                }

            }


        }
    };

    public boolean resultOrderTaskQueue(){
        if(!orderTaskQueue.isEmpty()){
            return true;
        }else{
            return false;
        }
    }


    //实时显示通知
    public void resultMqttTypr(String msg){
        //此时已在主线程中，可以更新UI了
        MqttResultBean bean =  gson.fromJson(msg.toString(), MqttResultBean.class);

        if (bean.getStatus()){
            switch (bean.getType()){
                case Config.MQTT_TYPE_PLACE_AN_ORDER:
                    L.d("MQTT_TYPE_PLACE_AN_ORDER");
                    OrderBean orderBean = gson.fromJson(msg.toString(), OrderBean.class);
                    baseActivity.resultMqttTypePlaceAnOrder(orderBean);
                    break;
                case Config.MQTT_TYPE_START_ORDER:
                    L.d("MQTT_TYPE_START_ORDER");
                    baseActivity.resultMqttTypeStartOrder(bean);
                    break;
                case Config.MQTT_TYPE_END_ORDER:
                    L.d("MQTT_TYPE_END_ORDER");
                    baseActivity.resultMqttTypeEndOrder(bean);
                    break;
                case Config.MQTT_TYPE_REFUSED_ORDER:
                    L.d("MQTT_TYPE_REFUSED_ORDER");
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

    public void filterOrder(){
        MqttResultBean resultBean;
        if(!orderTaskQueue.isEmpty()){
            //有数据

            //已结束订单
            for(MqttBean bean : orderTaskQueue){
                resultBean = checkType(bean.getMsg());
                if(resultBean.getType()== Config.MQTT_TYPE_END_ORDER){
                    L.d("订单结束");
                    baseActivity.resultMqttTypeEndOrder(resultBean);
                    orderTaskQueue.clear();
                    break;
                }
            }

            //拒绝订单
            if(!orderTaskQueue.isEmpty()) {
                for (MqttBean bean : orderTaskQueue) {
                    resultBean = checkType(bean.getMsg());
                    if (resultBean.getType() == Config.MQTT_TYPE_REFUSED_ORDER) {
                        L.d("拒绝订单:"+resultBean.getMsg());
                        baseActivity.resultMqttTypeRefusedOrder(resultBean);
                        orderTaskQueue.clear();
                        break;
                    }
                }
            }



            if(!orderTaskQueue.isEmpty()) {
                //开始订单
                for (MqttBean bean : orderTaskQueue) {
                    resultBean = checkType(bean.getMsg());
                    if (resultBean.getType() == Config.MQTT_TYPE_START_ORDER) {
                        L.d("订单开始");
                        baseActivity.resultMqttTypeStartOrder(resultBean);
                        orderTaskQueue.clear();
                        break;
                    }
                }
            }



            //等待 订单回复
            if(!orderTaskQueue.isEmpty()) {
                for (MqttBean bean : orderTaskQueue) {
                    resultBean = checkType(bean.getMsg());
                    if (resultBean.getType() == Config.MQTT_TYPE_PLACE_AN_ORDER) {
                        L.d("订单回复");
                        baseActivity.resultMqttTypePlaceAnOrder(resultBean);
                        orderTaskQueue.clear();
                        break;
                    }
                }
            }

        }else{
            //没有数据
            L.d("没有数据");
        }
    }

    public MqttResultBean checkType(String msg){
        MqttResultBean bean =  gson.fromJson(msg.toString(), MqttResultBean.class);
        return bean;
    }


    public void sendMqttMsg(String topic ,int qos , String msg){
        callTaxiCommHelper.publish(topic,qos,msg);
    }



    public class TrustBinder extends Binder{
        public TrustServer getService(){
            return TrustServer.this;
        }
    }
}
