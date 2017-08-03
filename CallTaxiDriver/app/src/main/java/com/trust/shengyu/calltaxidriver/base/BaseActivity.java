package com.trust.shengyu.calltaxidriver.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.jakewharton.rxbinding2.view.RxView;
import com.trust.shengyu.calltaxidriver.Config;
import com.trust.shengyu.calltaxidriver.R;
import com.trust.shengyu.calltaxidriver.activitys.registerandlogin.LoginActivity;
import com.trust.shengyu.calltaxidriver.tools.dialog.TrustDialog;
import com.trust.shengyu.calltaxidriver.tools.gps.DrawLiner;
import com.trust.shengyu.calltaxidriver.tools.gps.Positioning;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Config.activity = this;
        Config.context = this;
        init();
        initPush();
    }

    private void initPush() {

    }

    private void init() {
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
    protected void getOrderDialogResult(String startName, String endName, int taxiCast){

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

    public void showDialog(){

    }

    public void dissDialog(){

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









}
