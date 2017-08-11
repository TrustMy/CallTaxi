package com.trust.shengyu.calltaxidriver;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Trust on 2017/8/1.
 */

public class Config {
    public static Context context ;
    public static Activity activity;
    public static boolean needAdd = true;
    public static boolean noAdd = false;
    public static String token;

    public final static int SUCCESS = 0;
    public final static int ERROR = 1;

    //----------------------测试参数------------------------------------

    //tcp://192.168.1.160:9001 本地测试   //阿里云  tcp://139.196.229.233:9001
    public final static String TestMqttServer = "tcp://139.196.229.233:9001";
    public final static String TestUserName = "changan_yubei_1254";
    public final static String TestPassWord = "changan_022216509606";
    public final static String TestClientId = "changan_yubei_1254";
    public final static String [] TestTopics = {"Placeanorder"};
    public final static String sendTopic = "orderStatus";


    //-----------------------Mqtt消息tag--------------------------------

    public final static int MQTT_TYPE_PLACE_AN_ORDER = 0x001;//下订单
    public final static int MQTT_TYPE_START_ORDER = 0x002;//开始订单
    public final static int MQTT_TYPE_END_ORDER = 0x003;//结束订单
    public final static int MQTT_TYPE_REFUSED_ORDER = 0x004;//拒绝订单
    public final static int MQTT_TYPE_CONNECTION_EXCEPTION = 0x005;//连接断开
    public final static int MQTT_TYPE_CONNECTION_SUCCESS = 0x006;//连接成功

    //-----------------------基本配置参数-------------------------------

    public static int ClickTheInterval = 2;//点击间隔 防止连续点击
    public static long MqttReconnectionTime = 10 * 1000;//mqtt重连时间
}
