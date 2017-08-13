package com.trust.shengyu.calltaxi.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.activitys.registerandlogin.LoginActivity;
import com.trust.shengyu.calltaxi.db.DBManager;
import com.trust.shengyu.calltaxi.db.DbHelper;
import com.trust.shengyu.calltaxi.mqtt.MqttCommHelper;
import com.trust.shengyu.calltaxi.mqtt.TrustServer;
import com.trust.shengyu.calltaxi.mqtt.network.CallTaxiCommHelper;
import com.trust.shengyu.calltaxi.tools.L;

import com.trust.shengyu.calltaxi.tools.StatusBar;
import com.trust.shengyu.calltaxi.tools.beans.Bean;
import com.trust.shengyu.calltaxi.tools.beans.MqttResultBean;
import com.trust.shengyu.calltaxi.tools.dialog.TrustDialog;
import com.trust.shengyu.calltaxi.tools.gps.DrawLiner;
import com.trust.shengyu.calltaxi.tools.gps.Positioning;


import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;


import io.reactivex.functions.Consumer;

/**
 * Created by Trust on 2017/5/10.
 */
public abstract class BaseActivity extends AppCompatActivity {
    //    protected MainActivity mainActivity ;
    private Context context = BaseActivity.this;
    private Toast toast;
    protected TrustDialog trustDialog;
    protected Positioning positioning;
    protected Activity mActivity;
    protected DrawLiner drawLiner;
    protected Gson gson;
    protected DbHelper dbHelper;
    protected DBManager dbManager;
    public static boolean mqttConnectionStatus = false;
    protected static CallTaxiCommHelper callTaxiCommHelper;

    protected static TrustServer mqttServer;


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mqttServer = ((TrustServer.TrustBinder)iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mqttServer = null;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Config.activity = this;
        Config.context = this;
        mActivity = this;
        init();
        initPush();
    }

    private void initPush() {
        dbHelper = new DbHelper(context);
        dbManager = new DBManager(context);
        dbManager.selectAllData();

        if(mqttServer == null){
            bindService(new Intent(context,TrustServer.class),serviceConnection, Context.BIND_AUTO_CREATE);
        }


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        StatusBar.setColor(Config.activity, Color.parseColor("#6ED18E"));
        TrustServer.baseActivity = this;

        L.d("base Activity");
        /*
        if (callTaxiCommHelper == null) {
            callTaxiCommHelper = new CallTaxiCommHelper(context);
            callTaxiCommHelper.doClientConnection();
        }
        */

        gson = new Gson();
        drawLiner = new DrawLiner(context);
        positioning = new Positioning();
        trustDialog = new TrustDialog();
        trustDialog.setOnTrustDialogListener(new TrustDialog.onTrustDialogListener() {
            @Override
            public void resultOrderDialog(String startName, String endName, int taxiCast) {
                getOrderDialogResult(startName,endName,taxiCast);
            }
        });
    }

    //------------------------自定义--------------------------------------------
    //dialog 点击回调
    protected void getOrderDialogResult(String startName, String endName, int taxiCast){

    }
    //mqtt 收到push 回调
    protected   MqttCommHelper.onMqttCallBackResultListener onMqttCallBackResultListener = new MqttCommHelper.onMqttCallBackResultListener() {
        @Override
        public void CallBack(String topic, final Object msg) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //此时已在主线程中，可以更新UI了
                    MqttResultBean bean =  gson.fromJson(msg.toString(), MqttResultBean.class);

                    if (bean.getStatus()){
                        switch (bean.getType()){
                            case Config.MQTT_TYPE_PLACE_AN_ORDER:
                                resultMqttTypePlaceAnOrder(null);
                                break;
                            case Config.MQTT_TYPE_START_ORDER:
                                resultMqttTypeStartOrder(bean);
                                break;
                            case Config.MQTT_TYPE_END_ORDER:
                                resultMqttTypeEndOrder(bean);
                                break;
                            case Config.MQTT_TYPE_REFUSED_ORDER:
                                resultMqttTypeRefusedOrder(bean);
                                break;

                            default:
                                resultMqttTypeOther(bean);
                                break;
                        }

                        dissDialog();
                    }else{
//                        resultErrorMqtt("false:"+bean.getErrorMsg());
                    }
                }
            });
        }
    };


    private void resultErrorMqtt(String msg){
        showToast(msg);
    }

    //下订单回调
    public  void resultMqttTypePlaceAnOrder(Bean bean){
        L.d("resultMqttTypePlaceAnOrder");
    };
    //开始订单回调
    public  void resultMqttTypeStartOrder(MqttResultBean bean){L.d("resultMqttTypeStartOrder");};
    //结束订单回调
    public  void resultMqttTypeEndOrder(MqttResultBean bean){L.d("resultMqttTypeEndOrder");};
    //拒绝订单回调
    public  void resultMqttTypeRefusedOrder(MqttResultBean bean){L.d("resultMqttTypeRefusedOrder");};
    //未知消息回调
    public  void resultMqttTypeOther(MqttResultBean bean){};

    public void sendMqttMessage(String topic ,int qos , String msg){
        if(mqttConnectionStatus){
            showDialog(mActivity,"1","2",1);
            mqttServer.sendMqttMsg(topic,qos,msg);
        }else{
            showToast("网络异常,请稍后重试!");

        }
    }

    public void sendMqttMessage(String msg){
        callTaxiCommHelper.publish(Config.sendTopic,1,msg);
    }




    //-------------------------基础配置-------------------------------------------------------------------

    public void requestCallBeack(String url, Map<String,Object> map,int type,boolean isNeed){

    }

    //网络请求回调

    public void resultCallBeack(Object obj,int type,int status){

        if(status == Config.SUCCESS){
            successCallBeack(obj,type);
        }else{
            errorCallBeack(obj,type);
        }


    }
    public void successCallBeack(Object obj,int type){

    }

    public void errorCallBeack(Object bean,int type){

    }

    protected void  baseSetOnClick(final View v){
        RxView.clicks(v).throttleFirst(Config.ClickTheInterval, TimeUnit.SECONDS).
                subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        baseClickResult(v);
                    }
                });
    }

    public void baseClickResult(View v){

    }

    public void  onClickFinsh(final View v, final Activity activity){

    }

    public void  onClickFinsh( final Activity activity){
        activity.finish();
    }

    public void showDialog(Activity activity,String name ,String end , int type){
        dialog  = trustDialog.showOrderDialog(activity,name,end,type);
    }
    Dialog dialog;
    public void dissDialog(){
        trustDialog.dissDialog(dialog);
        dialog = null;
    }

    public void finsh(Activity activity){
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * 申请验证码
     */
    protected void requestCheckNum(long phone) {
        Map<String,Object> map =  new WeakHashMap<>();
        map.put("cp",phone);
//        requestCallBeack(Config.get_check_num, map, Config.getCheckNum, Config.noAdd);
    }

    /**
     * 检测 输入是否为空
     * @param editText
     * @param errorMsg
     * @return
     */
    protected  String baseCheckIsNull(EditText editText,String errorMsg){
        String msg = editText.getText().toString().trim();
        if(!msg.equals("")){
            return msg;
        }else{
            showToast(errorMsg);
            return null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void killAllActivtiy(Context context){
        context.startActivity(new Intent(context, LoginActivity.class));

    }

    protected void showToast(String msg){
        if (toast == null) {
            toast = Toast.makeText(context,
                    msg,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
    Snackbar snackbar;
    protected void showSnackbar(View v,String description,String msg){

        snackbar =  Snackbar.make(v, description,Snackbar.LENGTH_LONG)
                .setAction("Undo", new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                    }
                });
        snackbar.show();
    }

    protected  View bindView( Activity activity , int parentId,int id){
        View view = activity.findViewById(parentId).findViewById(id);
        return view;
    }



}
