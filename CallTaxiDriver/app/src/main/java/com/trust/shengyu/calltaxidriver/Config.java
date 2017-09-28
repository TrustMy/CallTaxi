package com.trust.shengyu.calltaxidriver;

import android.app.Activity;
import android.content.Context;

import java.util.LinkedList;

/**
 * Created by Trust on 2017/8/1.
 */

public class Config {
    public static Context context ;
    public static Activity activity;
    public static boolean needAdd = true;
    public static boolean noAdd = false;
    public static String token;

    public final static int SUCCESS = 1;
    public final static int ERROR = 0;

    //----------------------测试参数------------------------------------

    //tcp://192.168.1.160:9001 本地测试   //阿里云  tcp://139.196.229.233:9001
    //外网访问  tcp://58.246.120.86:9001
    public final static String TestMqttServer = "tcp://58.246.120.86:9001";
    public final static String TestUserName = "ls_driver";
    public final static String TestPassWord = "mqtt_test";
    public  static String TestClientId = Config.driver + "";
    public  static String [] TestTopics = {"book/order/"+Config.driver};//订阅后面是他的Customer

    public final static String sendTopic = "orderStatus";
    public static String CSubmitTopic ;


    //-----------------------Mqtt消息tag--------------------------------

    public final static int MQTT_TYPE_PLACE_AN_ORDER = 0x001;//下订单
    public final static int MQTT_TYPE_START_ORDER = 0x002;//开始订单
    public final static int MQTT_TYPE_END_ORDER = 0x004;//结束订单
    public final static int MQTT_TYPE_REFUSED_ORDER = 0x003;//取消订单订单
    public final static int MQTT_TYPE_CONNECTION_EXCEPTION = 0x005;//连接断开
    public final static int MQTT_TYPE_CONNECTION_SUCCESS = 0x006;//连接成功

    //映射 http://192.168.1.111:8082/SYCloudPlatform-1.0   //本地  http://192.168.1.134:8082
    //外网  //58.246.120.86:8081/tomcat
    //------------------------服务器接口--------------------------------
    public static String SERVER_URL = "http://58.246.120.86:8081/tomcat";//服务器地址
    public static String PLACE_AN_ORDER = "/rest/book";//下订单
    public static String CANCEL_ORDER = "/rest/cancel";//取消订单
    public static String SERACH_EXECUTE_ORDER = "/rest/executing";//查询正在执行订单
    public static String SERACH_HISTORY_ORDER_PAGING = "/rest/select/orders";//分页查询历史订单
    public static String SERACH_HISTORY_ORDER_ALL = "";//查询全部历史订单
    public static String DRIVER_ORDER = "/rest/receive";//司机接单
    public static String DRIVER_START_ORDER = "/rest/begin";//司机开始订单
    public static String DRIVER_END_ORDER = "/rest/finish";//司机结束订单
    public static String DRIVER_GET_TOKEN = "/rest/user/login";//司机端获取token
    public static String DRIVER_INFORMATION = "/rest/driver";//司机 获取信息




    //-----------------------请求tag------------------------------------
    public static final int TAG_PLACE_AN_ORDER = 0x00001;//下订单
    public static final int TAG_CANCEL_ORDER = 0x00002;//取消订单
    public static final int TAG_DRIVER_ORDER = 0x00003;//接单订单
    public static final int TAG_DRIVER_START_ORDER = 0x00004;//开始订单
    public static final int TAG_DRIVER_END_ORDER = 0x00005;//结束订单
    public static final int TAG_SERACH_EXECUTE_ORDER = 0x00009;//查询正在进行的订单
    public static final int TAG_SERACH_HISTORY_ORDER_PAGING = 0x00006;//分页查询订单
    public static final int TAG_DRIVER_GET_TOKEN = 0x00007;//获取司机token
    public static final int TAG_DRIVER_INFORMATION = 0x00008;//获取司机信息


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

    //-----------------------GPS配置---------------------------------------
    public static final int typeGPS = 0x9999,typeAlarm = 0x9998,typeTrip= 0x9997;
    public static final int EngineStatus =  3;//引擎状态
    public static int driver ;
    public static LinkedList taskQueue = new LinkedList();
    public static String driverPhone;//司机账号
    public static final String topicSubscription = "changan/yubei/apk/hotfix",topicGps = "data/gps",topicAlarm = "data/alarm",
            topicTrip = "data/trip";



    //-----------------------基本配置参数-------------------------------

    public static int ClickTheInterval = 2;//点击间隔 防止连续点击
    public static long MqttReconnectionTime = 10 * 1000;//mqtt重连时间

    public static int OrderEstimateDuration = 2000;//预估订单持续时间
    public static String Customer = "11111111111";//客户唯一标示
    public static int UserTypeDriver = 2;//登录司机唯一标示
    public static int Driver = 1;//下单操作司机唯一标示


    public static final int ORDER_STATUS_READ = 1;//只是查看过订单
    public static final int ORDER_STATUS_CANCEL = 2;//取消订单
    public static final int ORDER_STATUS_END = 3;//结束订单

}
