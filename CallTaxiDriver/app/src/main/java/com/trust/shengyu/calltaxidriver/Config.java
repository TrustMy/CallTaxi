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


    //------------------------服务器接口--------------------------------
    public static String SERVER_URL = "http://192.168.1.111:8082/SYCloudPlatform-1.0";//服务器地址
    public static String PLACE_AN_ORDER = "/rest/book";//下订单
    public static String CANCEL_ORDER = "/rest/cancel";//取消订单
    public static String SERACH_EXECUTE_ORDER = "";//查询正在实行订单
    public static String SERACH_HISTORY_ORDER_PAGING = "";//分页查询历史订单
    public static String SERACH_HISTORY_ORDER_ALL = "";//分页查询历史订单
    public static String DRIVER_ORDER = "/rest/receive";//司机接单
    public static String DRIVER_START_ORDER = "/rest/begin";//司机开始订单
    public static String DRIVER_END_ORDER = "/rest/finish";//司机结束订单



    //-----------------------请求tag------------------------------------
    public static final int TAG_PLACE_AN_ORDER = 0x00001;//下订单
    public static final int TAG_CANCEL_ORDER = 0x00002;//取消订单
    public static final int TAG_DRIVER_ORDER = 0x00003;//接单订单
    public static final int TAG_DRIVER_START_ORDER = 0x00004;//开始订单
    public static final int TAG_DRIVER_END_ORDER = 0x00005;//结束订单


    //-----------------------订单状态-----------------------------------
    public static final int OrderStatusBooked = 0;//预定订单，未支付 ,未派送
    public static final int OrderStatusDelivery = 1;//预定订单，未支付 已派送
    public static final int OrderStatusOrders = 2;//预定订单，未支付 已接受
    public static final int OrderStatusStartOrders = 3;//预定订单，未支付,开始订单
    public static final int OrderStatusEndOrders = 4;//预定订单，未支付,完成订单
    public static final int OrderStatusPayments = 5;//预定订单，支付中
    public static final int OrderStatusPaymentsSuccess = 6;//预定订单，支付成功
    public static final int OrderStatusPaymentsError = 7;//预定订单，支付失败
    public static final int OrderStatusCancelClientOrder = 8;//预定订单，客户取消订单
    public static final int OrderStatusCancelDriverOrder = 9;//预定订单，司机取消订单

    //-----------------------基本配置参数-------------------------------

    public static int ClickTheInterval = 2;//点击间隔 防止连续点击
    public static long MqttReconnectionTime = 10 * 1000;//mqtt重连时间

    public static int OrderEstimateDuration = 2000;//预估订单持续时间
    public static String Customer = "driver_789";//客户唯一标示
    public static int UserTypeDriver = 1;//司机唯一标示
}
