package com.trust.shengyu.rentalcarclient;

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

    public final static int SUCCESS = 1;
    public final static int ERROR = 0;

    public static String PHONE;



    //----------------------测试参数------------------------------------
    //tcp://192.168.1.160:9001 本地测试   //阿里云  tcp://139.196.229.233:9001
    public final static String TestMqttServer = "tcp://192.168.1.110:29001";
    public final static String TestUserName = "13892929789";
    public final static String TestPassWord = "111111";
    public final static String TestClientId = "mqtt_sy_dataserver";
    public  static String [] TestTopics = {"book/order/"+"5001"};//订阅后面是他的Customer
    public final static String sendTopic = "Placeanorder";
    public static String userPhone;
    //-----------------------Mqtt消息tag--------------------------------

    public final static int MQTT_TYPE_PLACE_AN_ORDER = 0x004;//下订单
    public final static int MQTT_TYPE_NOBODY_ORDER = 0x001;//无人接单
    public final static int MQTT_TYPE_START_ORDER = 0x005;//开始订单
    public final static int MQTT_TYPE_END_ORDER = 0x006;//结束订单
    public final static int MQTT_TYPE_REFUSED_ORDER = 0x002;//取消订单
    public final static int MQTT_TYPE_CONNECTION_EXCEPTION = 0x005;//连接断开
    public final static int MQTT_TYPE_CONNECTION_SUCCESS = 0x006;//连接成功


    //------------------------服务器接口--------------------------------
    //映射 http://192.168.1.111:8082/SYCloudPlatform-1.0   //本地  http://192.168.1.134:8082
    public static String SERVER_URL = "http://192.168.1.102:20080/tomcat";//服务器地址
//    public static String SERVER_URL = "http://192.168.1.134:8081";//本地服务器地址
    public static String PLACE_AN_ORDER = "/rest/book";//下订单
    public static String CANCEL_ORDER = "/rest/cancel";//取消订单
    public static String SERACH_EXECUTE_ORDER = "/rest/executing";//查询正在执行订单
    public static String SERACH_HISTORY_ORDER_PAGING = "/rest/select/orders";//分页查询历史订单
    public static String SERACH_HISTORY_ORDER_ALL = "";//查询全部历史订单
    public static String DRIVER_ORDER = "/rest/receive";//司机接单
    public static String DRIVER_START_ORDER = "/rest/begin";//司机开始订单
    public static String DRIVER_END_ORDER = "/rest/finish";//司机结束订单
    public static String CLIENT_LOGIN = "/rest/user/login";//客户登陆
    public static String CLIENT_INFORMATION = "/rest/customer";//客户登陆
    public static String REPLACE_AN_ORDER = "/rest/match";//下订单后 长时间无人接单 重新请求接口
    public static String UPDATA_APP = "/update/ebikeUpdate.xml";//版本升级


    public static String REGISTER ="/register/customer";//注册
    public static String LOGIN = "/rest/user/login";//登录
    public static String VERIFICATION_CODE = "/register/applySmsCode/";//获取验证码
    public static String GET_USER_INFO = "/rest/customer/";//获取用户当前信息
    public static String GET_USER_COUPON = "/rest/coupon/";//获取用户优惠劵
    public static String RETRIEVE_THE_PASSWORD = "/register/retrievePassword";//找回密码
    public static String IMPROVE_USER_INFO = "/rest/customer/";//用户信息完善
    //-----------------------请求tag------------------------------------
    public static final int TAG_PLACE_AN_ORDER = 0x00001;//下订单
    public static final int TAG_CANCEL_ORDER = 0x00002;//取消订单
    public static final int TAG_DRIVER_ORDER = 0x00003;//接单订单
    public static final int TAG_DRIVER_START_ORDER = 0x00004;//开始订单
    public static final int TAG_DRIVER_END_ORDER = 0x00005;//结束订单
    public static final int TAG_SERACH_EXECUTE_ORDER = 0x00005;//查询正在进行的订单
    public static final int TAG_SERACH_HISTORY_ORDER_PAGING = 0x00006;//分页查询订单
    public static final int TAG_CLIENT_LOGIN = 0x00007;//客户登陆
    public static final int TAG_CLIENT_INFORMATION = 0x00008;//客户信息
    public static final int TAG_REPLACE_AN_ORDER = 0x00009;//订单后 长时间无人接单 重新请求接口
    public static final int TAG_UPDATA_APP = 0x00010;//升级app

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
    public static String CustomerId  = "5001" ;//客户唯一标示
    public static int UserTypeClient = 1;//登录客户唯一标示
    public static int User = 0;//取消订单客户表示
}
