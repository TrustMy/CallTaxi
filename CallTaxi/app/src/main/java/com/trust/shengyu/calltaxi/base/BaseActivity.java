package com.trust.shengyu.calltaxi.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.jakewharton.rxbinding2.view.RxView;
import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.activitys.registerandlogin.LoginActivity;
import com.trust.shengyu.calltaxi.tools.StatusBar;
import com.trust.shengyu.calltaxi.tools.TrustTools;
import com.trust.shengyu.calltaxi.tools.dialog.TrustDialog;
import com.trust.shengyu.calltaxi.tools.gps.DrawLiner;
import com.trust.shengyu.calltaxi.tools.gps.Positioning;
import com.trust.shengyu.calltaxi.tools.request.TrustRequest;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;


import io.reactivex.functions.Consumer;

/**
 * Created by Trust on 2017/5/10.
 */
public class BaseActivity extends AppCompatActivity {
//    protected MainActivity mainActivity ;
    private Context context = BaseActivity.this;
    private Toast toast;
    protected TrustDialog trustDialog;
    protected Positioning positioning;
    protected  Activity mActivity;
    protected DrawLiner drawLiner;
    protected TrustTools trustTools;
    protected TrustRequest trustRequest;

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
        StatusBar.setColor(mActivity,Color.parseColor("#6ED18E"));
    }

    private void init() {

        trustTools = new TrustTools();
        drawLiner = new DrawLiner(context);
        positioning = new Positioning();
        trustDialog = new TrustDialog();
        trustDialog.setOnTrustDialogListener(new TrustDialog.onTrustDialogListener() {
            @Override
            public void resultOrderDialog(String startName, String endName, int taxiCast) {
                getOrderDialogResult(startName,endName,taxiCast);
            }
        });

        trustRequest = new TrustRequest();
        trustRequest.setOnResultCallBack(new TrustRequest.onResultCallBack() {
            @Override
            public void CallBack(int code, int status, Object object) {
                if(status == Config.SUCCESS){
                    successCallBeack(object,code);
                }else{
                    errorCallBeack(object,code);
                }
            }
        });
    }

    //------------------------自定义--------------------------------------------
    protected void getOrderDialogResult(String startName, String endName, int taxiCast){

    }





















    //-------------------------基础配置-------------------------------------------------------------------
    //网络请求
    public void requestCallBeack(String url, Map<String,Object> map,int code  , int requestTpye , int requestHeader ,String token){
        trustRequest.Request(url,map,code,requestTpye,requestHeader,token);
    }

    //网络请求回调

    public void resultCallBeack(Object obj,int type,int status){

        if(status == Config.SUCCESS){
            successCallBeack(obj,type);
        }else{
            errorCallBeack(obj,type);
        }


    }

    //成功回调
    public void successCallBeack(Object obj,int type){

    }
    //失败回调
    public void errorCallBeack(Object bean,int type){

    }
    //防止重复点击
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
    //显示等待框
    public void showDialog(){

    }
    //隐藏等待框
    public void dissDialog(){

    }


    /**
     * 申请验证码
     */
    protected void requestVerificationCode(long phone) {
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









}
