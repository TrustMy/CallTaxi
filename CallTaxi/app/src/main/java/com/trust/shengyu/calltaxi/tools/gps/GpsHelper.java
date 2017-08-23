package com.trust.shengyu.calltaxi.tools.gps;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.activitys.mainmap.MainMapActivity;
import com.trust.shengyu.calltaxi.base.BaseActivity;
import com.trust.shengyu.calltaxi.mqtt.TrustServer;
import com.trust.shengyu.calltaxi.tools.L;

import org.json.JSONObject;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by jiayang on 2016/7/26.
 */
public class GpsHelper implements Runnable {


    private static Context context = Config.context;

    private Handler handler = null;

    private static boolean isWorking = false;



    private long serialNumber;


    private MediaPlayer mp;

    private int Status = 3, gpsStatusNum = -1;

    private double Distance;

    private String Place = "";

    private boolean IsStopGps = false;


    private float maxSpeed = 300;

    private int Num = 0;

    private int Speeding = 1;//超速

    private NotificationManager nm;

    private Notification notification;

    private Intent intenthhh;

    private int normal = 1;


    private boolean isOverSpeed = false;

    private PendingIntent pendingIntent;


    private int GpsLoactionMaxTime = 0;

    public Handler getHandler() {
        return handler;
    }

    public static Handler mainGpsHanddler = null;




    private long startTime, endTime;//记录行程起点和终点
    private boolean IsFirst = false;//第一次 记录开始时间
    public byte status = 0;//行程状态  0未结束  1结束;


    public static Handler myServerHandler;

    long day = 86400000;


    //记录行程信息
    SharedPreferences.Editor gpsDelaySPF;
    private SharedPreferences gpsDelaySPFGet;








    /**
     * 初始化
     */
    public void init() {
        gpsDelaySPFGet = context.getSharedPreferences("gpsDelaySPF",
                Activity.MODE_PRIVATE);

        gpsDelaySPF = context.getSharedPreferences("gpsDelaySPF", Context.MODE_PRIVATE).edit();

//        logger.info("GpsHelper init");


        /*
        // 读取超速报警阈值
        SharedPreferences prefs = context.getSharedPreferences("CommParams", Context.MODE_PRIVATE);

        maxSpeed = prefs.getFloat("maxSpeed", Config.maxSpeed);//超速設置成 60


        SharedPreferences.Editor editor = prefs.edit();

        editor.putFloat("maxSpeed", maxSpeed);

        editor.commit();
        */
        maxSpeed = 300;

        initGpsManager();



    }

    @Override
    public void run() {

        Looper.prepare();

        init();

        Looper.loop();

    }


    protected LocationManager locationMgr;
    protected Location gpsLocation = null;
    protected String gpsProvider;
    protected boolean gpsFixed = false;

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Status = status;
            switch (status) {
                // GPS状态为可见时
                case LocationProvider.AVAILABLE:
//                    logger.info("当前GPS状态为可见状态");
                    gpsFixed = true;
                    Log.i("lh", "onStatusChanged: GPS 正常工作");
                    break;
                // GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
//                    logger.info("当前GPS状态为服务区外状态");
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    gpsLocation = locationMgr.getLastKnownLocation(gpsProvider);
                    gpsFixed = false;
                    Log.i("lh", "onStatusChanged: GPS 服务区外状态");
                    break;
                // GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
//                    logger.info("当前GPS状态为暂停服务状态");
                    gpsLocation = locationMgr.getLastKnownLocation(gpsProvider);
                    gpsFixed = false;
                    Log.i("lh", "onStatusChanged: GPS 暂停服务状态");
                    break;
            }

        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {

        }


        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onLocationChanged(Location location) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }


            if (location != null) {
                float speed = gpsLocation.getSpeed() * 3.6f;   //转成 公里/时
                normal++;
                if (speed >= maxSpeed) {
                    ++Speeding;
                    Log.v("lhh", "GpsHelper Speeding" + Speeding);
                    if (Speeding >= 5) {


                    }

                } else {
                    isOverSpeed = false;
                    Speeding = 0;
                }

                if (normal >= 5) {
                    normal = 0;
                    MainMapActivity.locationInterface.getLocation(location);
                }
            }else{
                L.e("定位为:"+null);
            }
            }
    };

    protected void initGpsManager() {
//        logger.info("init GpsManager");

        locationMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(true);
        criteria.setBearingRequired(true);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);


        //Settings.Secure.setLocationProviderEnabled(context.getContentResolver(),
        //        LocationManager.GPS_PROVIDER, true);

        gpsProvider = locationMgr.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        gpsLocation = locationMgr.getLastKnownLocation(gpsProvider);
    }


    public static boolean gpsStarted = false;


    /**
     * 开启GPS监听器
     */
    public void startGpsListening() {
        L.d("startGpsListening:"+gpsStarted);
        if (!gpsStarted) {


//            String msg = gpsDelaySPFGet.getString("PositioningFailedMessage",null);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationMgr.requestLocationUpdates(gpsProvider, 2000, 0, locationListener);


            gpsStarted = true;
            isWorking = true;
            status = 0;
        }
    }


        /**
         * 关闭GPS监听器
         */
        protected void stopGpsListening() {
            if (gpsStarted) {

                isWorking = false;

                Speeding = 0;

                Num = 0;

                GpsLoactionMaxTime = 0;

                gpsStatusNum = -1;

                Distance = -100;//  don't stopgps
                Place = "";//地点

//            logger.info("stop GPS listening");

                locationMgr.removeUpdates(locationListener);
                //locationMgr.removeNmeaListener(nmeaListener);

                gpsStarted = false;

                status = 1;//行程结束

                clearMessage();

            }

        }



        private void clearMessage() {

            Observable
                    .fromArray(100)
                    .subscribeOn(Schedulers.io())
                    .timer(3 * 1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {

                        /*
                        gpsDelaySPF.putString("PositioningFailedMessage", null);
                        gpsDelaySPF.commit();
                        */

                            startTime = 0;
                            endTime = 0;

                            IsFirst = true;//恢复表示

                            L.d("clear");
                        }
                    });
        }

}

