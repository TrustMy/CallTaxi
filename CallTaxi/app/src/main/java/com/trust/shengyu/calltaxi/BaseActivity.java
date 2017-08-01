package com.trust.shengyu.calltaxi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;



import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;



import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Trust on 2017/5/10.
 */
public class BaseActivity extends AppCompatActivity {
    protected MainActivity mainActivity ;
    private Context context = BaseActivity.this;



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

    }


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

    public void  onClick(final View v){
//        RxView.clicks(v).throttleFirst(2, TimeUnit.SECONDS).
//                subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(@NonNull Object o) throws Exception {
//                        clickResult(v);
//                    }
//                });
    }

    public void clickResult(View v){

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

    public void showWaitToast(Context context,String msg,int time){

    }
    public void showErrorToast(Context context,String msg,int time){

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
    protected  String checkMessage(EditText editText,String errorMsg){
        String msg = editText.getText().toString().trim();
        if(!msg.equals("")){
            return msg;
        }else{
            showErrorToast(context,errorMsg,1);
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


}
