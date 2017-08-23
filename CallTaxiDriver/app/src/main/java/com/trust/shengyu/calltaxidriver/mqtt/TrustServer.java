package com.trust.shengyu.calltaxidriver.mqtt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.trust.shengyu.calltaxidriver.Config;
import com.trust.shengyu.calltaxidriver.base.BaseActivity;
import com.trust.shengyu.calltaxidriver.gpswork.CommonMessage;
import com.trust.shengyu.calltaxidriver.gpswork.GpsHelper;
import com.trust.shengyu.calltaxidriver.mqtt.network.CallTaxiCommHelper;
import com.trust.shengyu.calltaxidriver.tools.L;
import com.trust.shengyu.calltaxidriver.tools.TrustTools;
import com.trust.shengyu.calltaxidriver.tools.beans.MqttBean;
import com.trust.shengyu.calltaxidriver.tools.beans.MqttBeans;
import com.trust.shengyu.calltaxidriver.tools.beans.MqttResultBean;
import com.trust.shengyu.calltaxidriver.tools.beans.OrderBean;
import com.trust.shengyu.calltaxidriver.tools.beans.RefusedOrderBean;
import com.trust.shengyu.calltaxidriver.tools.beans.TypeAlarmBean;
import com.trust.shengyu.calltaxidriver.tools.beans.TypeBean;
import com.trust.shengyu.calltaxidriver.tools.beans.TypeGpsBean;
import com.trust.shengyu.calltaxidriver.tools.beans.TypeTripBean;


import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Trust on 2017/8/3.
 */

public class TrustServer extends Service {
    private Binder trustBinder = new TrustBinder();
    private Context context  = this;
    protected CallTaxiCommHelper callTaxiCommHelper;
    protected Gson gson;
    public static BaseActivity baseActivity;
    LinkedList<MqttBeans> orderTaskQueue = new LinkedList();
    LinkedList<MqttBean> otherTaskQueue = new LinkedList();
    public static boolean appStatus = false;

    private Handler gpsHandler;
    private GpsHelper gpsHelper;
    protected static ExecutorService threadPool = Executors.newCachedThreadPool();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return trustBinder;
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();
        if (callTaxiCommHelper == null) {
            callTaxiCommHelper = new CallTaxiCommHelper(context);
            callTaxiCommHelper.setOnMqttCallBackResultListener(onMqttCallBackResultListener);
            callTaxiCommHelper.doClientConnection();
            gson = new Gson();
            handler.sendEmptyMessageDelayed(1,1000 * 10);
        }

        gpsHelper = new GpsHelper();
        threadPool.execute(gpsHelper);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gpsHandler = gpsHelper.getHandler();
        gpsHelper.setTrustServer(this);

    }

    //mqtt 收到push 回调
    protected   MqttCommHelper.onMqttCallBackResultListener onMqttCallBackResultListener = new MqttCommHelper.onMqttCallBackResultListener() {
        @Override
        public void CallBack(String topic, final Object msg) {
            L.d("topic:"+topic+"|msg:"+msg);
//            if(!appStatus){
//                //分类 通知类型
//                if (topic.equals(Config.TestTopics[0])){
////                    orderTaskQueue.add(new MqttBeans(topic,msg.toString()));
//                }else{
//                    otherTaskQueue.add(new MqttBean(topic,msg.toString()));
//                }
//            }else{
                //分类 通知类型
                if (topic.equals(Config.TestTopics[0])){
                    resultMqttTypr(msg.toString());
                }else{
                    //其他同志
                }

//            }


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
        MqttBeans bean =  gson.fromJson(msg.toString(), MqttBeans.class);

        if (bean.getStatus() ==  Config.SUCCESS){
            switch (bean.getType()){
                case Config.MQTT_TYPE_PLACE_AN_ORDER:
                    L.d("MQTT_TYPE_PLACE_AN_ORDER");

                    baseActivity.resultMqttTypePlaceAnOrder(bean);
                    break;
                case Config.MQTT_TYPE_START_ORDER:
                    L.d("MQTT_TYPE_START_ORDER");
//                    baseActivity.resultMqttTypeStartOrder(bean);
                    break;
                case Config.MQTT_TYPE_END_ORDER:
                    L.d("MQTT_TYPE_END_ORDER");
//                    baseActivity.resultMqttTypeEndOrder(bean);
                    break;
                case Config.MQTT_TYPE_REFUSED_ORDER:
                    RefusedOrderBean refusedOrderBean = gson.fromJson(msg,RefusedOrderBean.class);

                    L.d("MQTT_TYPE_REFUSED_ORDER");
                    baseActivity.resultMqttTypeRefusedOrder(refusedOrderBean);
                    break;
                default:
//                    baseActivity.resultMqttTypeOther(bean);
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

            /*
            //已结束订单
            for(MqttBeans bean : orderTaskQueue){
//                resultBean = checkType(bean.getMsg());
                if(bean.getType()== Config.MQTT_TYPE_END_ORDER){
                    L.d("订单结束");
                    baseActivity.resultMqttTypeEndOrder(resultBean);
                    orderTaskQueue.clear();
                    break;
                }
            }
            */

            /*
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
            */


/*
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
            */


/*
            //等待 订单回复
            if(!orderTaskQueue.isEmpty()) {
                for (MqttBeans bean : orderTaskQueue) {
                    resultBean = checkType(bean.getMsg());
                    if (resultBean.getType() == Config.MQTT_TYPE_PLACE_AN_ORDER) {
                        L.d("订单回复");
                        baseActivity.resultMqttTypePlaceAnOrder(resultBean);
                        orderTaskQueue.clear();
                        break;
                    }
                }
            }
  */
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





    public  void startGps(){
        // 通知GPS线程开始采集GPS数据
        gpsHandler.sendEmptyMessage(CommonMessage.MSG_START_GPS_LISTENING);

    }

    private String type;
    private Integer qos;
    public void checkType(TypeBean typeBean){
        if(!Config.taskQueue.isEmpty()){
            typeBean = (TypeBean) Config.taskQueue.peek();
            Config.taskQueue.remove(typeBean);

            String message = null;
            if(typeBean != null){
                switch (typeBean.getTypes()){
                    case Config.typeGPS:
                        type = Config.topicGps;
                        qos  = 1;
                        message = typeGPSMessage(typeBean);
                        break;

                    case Config.typeAlarm:
                        type = Config.topicAlarm;
                        qos  = 2;
                        message = typeAlarmMessage(typeBean);
                        break;

                    case Config.typeTrip:
                        type = Config.topicTrip;
                        qos  = 2;
                        message = typeTripMessage(typeBean);
                        break;
                }
                if(message !=null){
                    sendMqttMsg(type,qos,message);
//                    L.i("trustServer sendMsg success");
                }
            }
        }

    }

    public void sendMqttMsg(String topic, Integer qos, String msg){
        callTaxiCommHelper.publish(topic,qos,msg);
//        L.i("trustServer sendMsg success");
    }



    public String typeGPSMessage(TypeBean typeBean){
        TypeGpsBean bean = (TypeGpsBean) typeBean;
//        String result = "GPS;2;77232937658;1170;"+bean.getGpsTime()+
//                ";1;29.617029;106.587460;187;"+bean.getSpeed()
//                +";225;4;;;;;;;;;;;;;";
//        String msg  = "GPS;2;77232937658;1170;20170718120000;1;29.617029;106.587460;187;0.0;225;4;;;;;;;;;;;;;";
        String gps = "GPS;"+bean.getSerialNo()+";"+bean.getTerminalId()+";"+bean.getDriver()+";"+
                bean.getGpsTime()+";1;"+bean.getLat()+";"+bean.getLon()+";"+
                bean.getAlt()+";"+bean.getSpeed()+";"+bean.getBear()+";"+bean.getEngineStatus()+
                ";;;;;;;;;;;;;";
//        L.d("生成的 :"+gps);
        return gps;
    }

    public String typeAlarmMessage(TypeBean typeBean){
        TypeAlarmBean bean = (TypeAlarmBean) typeBean;
//        String alrm = "ALARM;9;3733887419;1181;20170704124141,29.621736,106.580213;4;1,68.45352;;;;;;;;;;;;;;;";

        String alarm = "ALARM;"+bean.getSerialNos()+";"+bean.getCarSerialNumber()+";"+bean.getDriver()+
                ";"+bean.getGpsTime()+","+bean.getLat()+","+bean.getLon()+";4;1,"+bean.getSpeed()+";;;;;;;;;;;;;;;";
        L.d("生成的 alarm:"+alarm);
        return alarm;

    }

    public String typeTripMessage(TypeBean typeBean){
        TypeTripBean bean = (TypeTripBean) typeBean;
//        String trip = "TRIP;2;16669202523;1181;2017-07-04 12:30:00;0.000000;0.000000;2017-07-04 13:00:00;0.000000;0.000000;0;2017061216315";

        String trip = "TRIP;"+bean.getSerialNos()+";"+bean.getCarSerialNumber()+";"+bean.getDriveId()+";"+
                TrustTools.getGPSNumTime(bean.getFireOnTime())+
                ";"+bean.getFireOnLat()+";"+bean.getFireOnLon()+";"+TrustTools.getGPSNumTime(bean.getFireOffTime())+
                ";"+bean.getFireOffLat()+";"+bean.getFireOffLon()+";" +
                bean.getTripStatus()+";"+bean.getTripTag();
        L.d("生成的trip:"+trip);
        return trip;
    }
}
