package com.trust.shengyu.rentalcarclient.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.trust.shengyu.rentalcarclient.Config;
import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.activitys.registerandlogin.LoginActivity;
import com.trust.shengyu.rentalcarclient.db.DBManager;
import com.trust.shengyu.rentalcarclient.db.DbHelper;
import com.trust.shengyu.rentalcarclient.server.TrustServer;
import com.trust.shengyu.rentalcarclient.tools.L;

import com.trust.shengyu.rentalcarclient.tools.TrustTools;
import com.trust.shengyu.rentalcarclient.tools.beans.oldbeans.Bean;
import com.trust.shengyu.rentalcarclient.tools.beans.oldbeans.MqttTypePlaceAnOrder;
import com.trust.shengyu.rentalcarclient.tools.beans.oldbeans.NObodyOrderBean;
import com.trust.shengyu.rentalcarclient.tools.beans.oldbeans.RefusedOrderBean;
import com.trust.shengyu.rentalcarclient.tools.beans.rentalcarbeans.ResultErrorBean;
import com.trust.shengyu.rentalcarclient.tools.dialog.TrustDialog;
import com.trust.shengyu.rentalcarclient.tools.gdgps.DrawLiner;
import com.trust.shengyu.rentalcarclient.tools.gdgps.Positioning;
import com.trust.shengyu.rentalcarclient.tools.gson.TrustAnalysis;
import com.trust.shengyu.rentalcarclient.tools.request.TrustRequest;


import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;


import io.reactivex.functions.Consumer;

import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.TAG_URL_VERIFICATION_CODE;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.URL_SERVER;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.URL_VERIFICATION_CODE;

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

    protected AMap baseAmap;
    protected MapView baseMapView;
    protected static TrustServer mqttServer;
    protected TrustRequest trustRequest;
    private Dialog waitDialog;

    protected TrustTools trustTools;
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
//        StatusBar.setColor(Config.activity, Color.parseColor("#6ED18E"));
        TrustServer.baseActivity = this;

        /*
        if (callTaxiCommHelper == null) {
            callTaxiCommHelper = new CallTaxiCommHelper(context);
            callTaxiCommHelper.doClientConnection();
        }
        */
        trustRequest = new TrustRequest(resultCallBack , URL_SERVER);
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
        trustTools = new TrustTools();

//        requestCallBeack(Config.SERACH_EXECUTE_ORDER,map,Config.TAG_SERACH_EXECUTE_ORDER,
//                trustRequest.GET);


    }

    //------------------------自定义--------------------------------------------
    //dialog 点击回调
    protected void getOrderDialogResult(String startName, String endName, int taxiCast){

    }

    /*
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
                                resultMqttTypePlaceAnOrder(bean);
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


                    }else{
//                        resultErrorMqtt("false:"+bean.getErrorMsg());
                    }
                }
            });
        }
    };
    */


    private void resultErrorMqtt(String msg){
        showToast(msg);
    }

    //下订单回调
    public  void resultMqttTypePlaceAnOrder(Bean bean){
        L.d("resultMqttTypePlaceAnOrder");
    };
    //开始订单回调
    public  void resultMqttTypeStartOrder(MqttTypePlaceAnOrder bean){L.d("resultMqttTypeStartOrder");};
    //结束订单回调
    public  void resultMqttTypeEndOrder(MqttTypePlaceAnOrder bean){L.d("resultMqttTypeEndOrder");};
    //拒绝订单回调
    public  void resultMqttTypeRefusedOrder(RefusedOrderBean bean){L.d("resultMqttTypeRefusedOrder");};
    //未知消息回调
    public  void resultMqttTypeOther(MqttTypePlaceAnOrder bean){};
    //无人接单回调
    public void resultMqttTypeNobodyOrder(NObodyOrderBean bean){};
    //发送mqtt消息
    public void sendMqttMessage(String topic ,int qos , String msg){
        if(mqttConnectionStatus){

            mqttServer.sendMqttMsg(topic,qos,msg);
        }else{
//            showToast("网络异常,请稍后重试!");
            showToast("网络异常,请重试!");
            L.e("网络异常,请重试!");
        }
    }

    /**
     * 检查返回结果
     * @param status
     * @param msg
     * @return
     */
    protected boolean getResultStatus(int status , String msg){
        if (status == 1) {//成功
            return true;
        }else{
            ResultErrorBean errorBean = TrustAnalysis.resultTrustBean(msg,ResultErrorBean.class);
            showToast(errorBean.getInfo());
            return false;
        }
    }


    protected void Countdown(View view, int time){
        new TrustTools<>().Countdown(view,time);
    }


    //-------------------------基础配置------------------------------------------------------"Bearer V95iRBXYKLOdm3y/eqM0Vz05yYiP53r+T5oIoQ1B1M0="-------------

    public void requestCallBeack(String url, Map<String,Object> map,int requestCode ,int requestType,String token){
//        waitDialog = trustDialog.showWaitDialog(this);
        L.d("requestType:"+requestType);
        showToast("正在请求,请稍后....");
        trustRequest.Request(url,map,requestCode,requestType,trustRequest.HeaderJson,token);
    }



    private TrustRequest.onResultCallBack resultCallBack = new TrustRequest.onResultCallBack() {
        @Override
        public void CallBack(int code, int status, Object object) {
            resultCallBeack(object,code,status);
        }
    };

    //网络请求回调

    public void resultCallBeack(Object obj,int type,int status){
        trustDialog.dissDialog(waitDialog);
        if(status == Config.SUCCESS){
            successCallBeack(obj,type);
        }else{
            errorCallBeack(obj,type);
        }
    }

    //返回成功
    public void successCallBeack(Object obj,int type){

    }
    //返回失败
    public void errorCallBeack(final Object bean, int type){
        showToast(bean.toString());


    }
    //过滤点击延迟
    protected void  baseSetOnClick(final View v){
        RxView.clicks(v).throttleFirst(Config.ClickTheInterval, TimeUnit.SECONDS).
                subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        baseClickResult(v);
                    }
                });
    }
    //点击事件
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
    protected void requestCheckNum(String phone) {
        Map<String,Object> map =  new WeakHashMap<>();
        map.put("cellPhone",phone);
        requestCallBeack(URL_VERIFICATION_CODE,map, TAG_URL_VERIFICATION_CODE,trustRequest.GET_NO_PARAMETER_Name,null);
//        requestCallBeack(Config.get_check_num, map, Config.getCheckNum, Config.noAdd);
    }

    /**
     * 检测 输入是否为空
     * @param view
     * @param errorMsg
     * @return
     */
    protected  String baseCheckIsNull(View view,String errorMsg){
        String msg = null;
        if (view instanceof EditText) {
            msg = ((EditText)view).getText().toString().trim();
        }else if(view instanceof Button){
            msg = ((Button) view).getText().toString().trim();
            if (msg.equals("请选择领取时间")) {
                msg = "";
            }
        }
        if(!msg.equals("")){
            return msg;
        }else{
            showToast(errorMsg);
            return null;
        }
    }

    public void killAllActivtiy(Context context){
        context.startActivity(new Intent(context, LoginActivity.class));

    }

    protected void showToast(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(context,
                            msg,
                            Toast.LENGTH_SHORT);
                } else {
                    toast.setText(msg);
                }
                toast.show();
            }
        });


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


    protected void initMapView(){
        if (baseAmap == null) {
            baseAmap = baseMapView.getMap();
        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        if (baseAmap == null && baseMapView != null) {
            baseAmap = baseMapView.getMap();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (baseMapView != null) {
            baseMapView.onResume();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (baseMapView != null) {
            baseMapView.onPause();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (baseMapView != null) {
            baseMapView.onSaveInstanceState(outState);
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
