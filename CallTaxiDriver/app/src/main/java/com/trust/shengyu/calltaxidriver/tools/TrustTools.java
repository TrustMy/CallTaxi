package com.trust.shengyu.calltaxidriver.tools;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Trust on 2017/8/7.
 */

public class TrustTools<T extends View> {

    /**
     * 倒计时
     * @param value
     * @param time
     */
    public  void  Countdown(final T value , int time){
        final int count = time;
        Observable.interval(0,1, TimeUnit.SECONDS).take(count+1).map(new Function<Long, Object>() {
            @Override
            public Object apply(@NonNull Long aLong) throws Exception {
                L.d("定时器: count:"+count+"|aLong:"+aLong);
                return count-aLong;
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        value.setEnabled(false);//不可点击
                    }
                }).observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        checkT(value,o+"秒");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        value.setEnabled(true);//不可点击
                        checkT(value,"获取验证码");
                    }
                });
    }

    private void checkT(T v,String msg){
        if(v instanceof Button){
            ((Button)v).setText(msg);
        }else if(v instanceof TextView){
            ((TextView)v).setText(msg);
        }
    }


    //-----------------------时间--------------------------------------
    public static String  getSystemTimeString(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = new Date(System.currentTimeMillis());//获取当前时间
        String systemTime = formatter.format(dateTime);
        return systemTime;
    }

}
