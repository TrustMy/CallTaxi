package com.trust.shengyu.rentalcar.mqtt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Trust on 2017/7/10.
 */

public abstract class MqttCommHelper {
    protected MqttAndroidClient client;
    protected MqttConnectOptions conOpt;
    DBManager dbManager;
    private final int initConnectionError = 0x001,connectionError = 0x002;

    public static boolean pushStatus = false;

    public static boolean doConnect = true;


    public void setMqttSendControll() {

    }

    protected String host ;
    protected String userName ;
    protected String passWord ;
    protected String myTopic ;
    protected String clientId ;
    protected String [] submintTopics ;
    protected int[] qoss = {1,1};
    protected String sendTopic;
    protected Context context;

    public MqttCommHelper(Context context) {
        this.context = context;
        dbManager = new DBManager(context);
        iniServer();
        initMqtt();
    }

    protected abstract void iniServer() ;

    public static boolean initMqttStatus = false;

    public boolean workStatus = false;



    private void initMqtt(){
        synchronized (this){
            if(!initMqttStatus){
                initMqttStatus= true;
                L.d("mqtt config :"+host+"|name:"+userName+"|password:"+passWord);
                // 服务器地址（协议+地址+端口号）
                String uri = host;
                client = new MqttAndroidClient(context, uri, clientId);
                // 设置MQTT监听并且接受消息
                client.setCallback(mqttCallback);

                conOpt = new MqttConnectOptions();

                // 设置超时时间，单位：秒
                conOpt.setConnectionTimeout(10);
                // 心跳包发送间隔，单位：秒
                conOpt.setKeepAliveInterval(20);
                // 用户名
                conOpt.setUserName(userName);
                // 密码
                conOpt.setPassword(passWord.toCharArray());
                //设置保存session 接收离线消息 // false 离线保存  true离线清空
                conOpt.setCleanSession(false);
                //自动重连
//                conOpt.setAutomaticReconnect(true);
                // last will message

                String message = "{\"terminal_uid\":\"" + clientId + "\"}";
                String topic = myTopic;
                Integer qos = 0;
                Boolean retained = false;
                if ((!message.equals("")) || (!topic.equals(""))) {
                    // 最后的遗嘱
                    try {
//                conOpt.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
                    } catch (Exception e) {
//                    doConnect = false;
                        iMqttActionListener.onFailure(null, e);
                    }
                }
                L.i("init mqtt ");
            }
        }
    }

    // MQTT监听并且接受消息
    protected MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
//            dbManager.addData("接收到的"+new String(message.getPayload())+"|"+ TrustTools.getSystemTimeString());
            mqttResultCallBack.CallBack(topic,new String(message.getPayload()));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
            L.d("deliveryComplete : success");
            pushStatus = false;

        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
            doConnect = true;
//            L.e("connectionLost error : "+arg0.getMessage());

            mqttConnectionStatus(Config.MQTT_TYPE_CONNECTION_EXCEPTION);
        }
    };

    private void mqttConnectionStatus(int status) {

        if (status == Config.MQTT_TYPE_CONNECTION_SUCCESS) {
            BaseActivity.mqttConnectionStatus = true;
        }else{
            BaseActivity.mqttConnectionStatus = false;
        }


        Observable
                .fromArray(100)
                .subscribeOn(Schedulers.io())
                .timer(Config.MqttReconnectionTime, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ObservableTransformer<Object, Object>() {
                    @Override
                    public ObservableSource<Object> apply(Observable<Object> upstream) {
                        return upstream.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        doClientConnection();
                    }
                });
    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            doConnect = false;
                // 订阅myTopic话题
//                client.subscribe(myTopic,1);
                subscribeTopic(submintTopics[0],1);
            mqttConnectionStatus(Config.MQTT_TYPE_CONNECTION_SUCCESS);

            L.d(" mqtt connection success conOpt.isAutomaticReconnect():"+conOpt.isAutomaticReconnect());

        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            // 连接失败，重连
            L.e("connection onFailure :"+arg1.getMessage());

            mqttConnectionStatus(Config.MQTT_TYPE_CONNECTION_EXCEPTION);
            /*
            if(workStatus){
                mqttSendControll.sendMessage();
            }
            */

        }
    };


    /**
     * 绑定主题
     * @param topic
     * @param qos
     */

    public void subscribeTopic(String topic, int qos){
        try {
            client.subscribe(topic,qos);
            L.d("订阅主题:"+topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解绑主题
     * @param topic
     */
    public void unbundledTopics(String topic){
        try {
            client.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /** 连接MQTT服务器 */
    public void doClientConnection() {
        if (doConnect) {
            if (!client.isConnected()) {
                try {
                    client.connect(conOpt, null, iMqttActionListener);
                    L.i("doClientConnection ");
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            } else {
                L.e("doClientConnection  client.isConnected():" + client.isConnected() + "|" +
                        "isConnectIsNomarl():" + isConnectIsNomarl());
            }
        }

    }

    /** 判断网络是否连接 */
    public  boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            L.i("Mqtt intentType:"+name);
            return true;
        } else {
            L.i("Mqtt intentTypeError");
            return false;
        }
    }

    public  void publish(String topic ,Integer qos  ,String msg){
        synchronized (this){
            if(doConnect){
                mqttConnectionStatus(Config.MQTT_TYPE_CONNECTION_EXCEPTION);
                return;
            }

            if( client!=null){
                //端口  8878   注册
                Boolean retained = false;
                if(!pushStatus && msg != null){
                    try {
                        L.d("topic:"+topic+"publish :"+msg);
                        client.publish(topic, msg.getBytes(), qos.intValue(), retained.booleanValue());
                        dbManager.addData("发送的json:"+msg+"|"+TrustTools.getSystemTimeString());
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void disMqtt(){
        try {
            client.disconnect();
            initMqttStatus = false;
            workStatus = false;
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public interface onMqttCallBackResultListener{
        void CallBack(String topic, Object msg);
    }

    public onMqttCallBackResultListener mqttResultCallBack;


    public void setWorkStatus(boolean workStatus) {
        this.workStatus = workStatus;
    }

    public void setOnMqttCallBackResultListener(onMqttCallBackResultListener onMqttCallBackResultListener){
        mqttResultCallBack = onMqttCallBackResultListener;
    }
}
