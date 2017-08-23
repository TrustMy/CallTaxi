package com.trust.shengyu.calltaxi.tools;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
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

public class TrustTools <T extends View> {

    /**
     * 倒计时显示
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

    /**
     * 自定义时间时间倒计时
     * @param time
     */
    public  TrustTools setCountdown( int time){
        Observable
                .fromArray(100)
                .subscribeOn(Schedulers.io())
                .timer(time*1000,TimeUnit.MILLISECONDS)
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
                        countdownCallBack.callBackCountDown();
                    }
                });

        return this;
    }


    public interface CountdownCallBack{
        void callBackCountDown();
    }
    public CountdownCallBack countdownCallBack;

    public void setCountdownCallBack(CountdownCallBack countdownCallBack){
        this.countdownCallBack = countdownCallBack;
    }


    //-----------------------时间--------------------------------------

    /**
     * 获取系统时间
     * @return
     */
    public static String  getSystemTimeString(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = new Date(System.currentTimeMillis());//获取当前时间
        String systemTime = formatter.format(dateTime);
        return systemTime;
    }

    /**
     * 获取系统时间 data
     * @return
     */
    public static long getSystemTimeData(){
        return System.currentTimeMillis();
    }


    public   static   double   round(double v,int   scale){
        if(scale<0){
            throw   new   IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        BigDecimal b   =   new   BigDecimal(Double.toString(v));
        BigDecimal   one   =   new   BigDecimal("1");
        return   b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
