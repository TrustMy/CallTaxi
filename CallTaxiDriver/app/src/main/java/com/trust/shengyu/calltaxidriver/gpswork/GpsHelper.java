package com.trust.shengyu.calltaxidriver.gpswork;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
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
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.Gson;

import com.trust.shengyu.calltaxidriver.Config;
import com.trust.shengyu.calltaxidriver.activitys.MainActivity;
import com.trust.shengyu.calltaxidriver.mqtt.TrustServer;
import com.trust.shengyu.calltaxidriver.tools.L;
import com.trust.shengyu.calltaxidriver.tools.TrustTools;
import com.trust.shengyu.calltaxidriver.tools.beans.TypeAlarmBean;
import com.trust.shengyu.calltaxidriver.tools.beans.TypeBean;
import com.trust.shengyu.calltaxidriver.tools.beans.TypeGpsBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    private TrustServer trustServer;

    private long serialNumber;

    public void setTrustServer(TrustServer trustServer) {
        this.trustServer = trustServer;
    }

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

    private int zhengchang = 1;


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
        gson = new Gson();
        gpsDelaySPFGet = context.getSharedPreferences("gpsDelaySPF",
                Activity.MODE_PRIVATE);

        gpsDelaySPF = context.getSharedPreferences("gpsDelaySPF", Context.MODE_PRIVATE).edit();

//        logger.info("GpsHelper init");

        handler = new GpsHandler(this);
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
            GpsStatus gpsStatus = locationMgr.getGpsStatus(null); // 获取当前状态
            // 获取默认最大卫星数
            int maxSatellites = gpsStatus.getMaxSatellites();

            Log.i("lh", "maxSatellites: " + maxSatellites);

            if (location != null) {
                gpsLocation = location;

                gpsFixed = true;

            } else {
                gpsLocation = locationMgr.getLastKnownLocation(gpsProvider);
                gpsFixed = false;
                Log.i("lh", "onLocationChanged: location ＝＝ null");
            }

            Bundle data = new Bundle();
            float speed = gpsLocation.getSpeed() * 3.6f;   //转成 公里/时
//            Log.v("lhh", "maxSpeed" + maxSpeed + "当前速度:" + speed);
            ++zhengchang;


            if (speed >= maxSpeed) {

                ++Speeding;
//                Log.v("lhh", "GpsHelper Speeding" + Speeding);
                if (Speeding >= 5) {

//                    logger.info("---- Report OverSpeed Alarm ---");

                    // 生成 超速报警 消息

                    Map<String,Object> map = new WeakHashMap<>();
                    map.put("type",Config.typeAlarm);
                    map.put("serialNo",TrustTools.getSystemTimeData());
                    map.put("terminalId",Config.Customer);
                    map.put("driver",Config.driver);
                    map.put("lat",gpsLocation.getLatitude() );
                    map.put("lon",gpsLocation.getLongitude() );
                    map.put("alt",gpsLocation.getAltitude());
                    map.put("speed",speed);
                    map.put("carSerialNumber",Config.Customer);
                    map.put("bear",gpsLocation.getBearing());
                    map.put("bear",Config.EngineStatus);
                    map.put("gpsTime", TrustTools.getGPSNumTime(gpsLocation.getTime()));
                    toJson(Config.typeGPS,TrustTools.getSystemTimeData(),map);

                    //播放报警信息
                    mp.start();
                    Num++;
                    isOverSpeed = true;
                    Speeding = 0;

                    Map<String,Object> gpsMap = new WeakHashMap<>();
                    gpsMap.put("type",Config.typeGPS);
                    gpsMap.put("serialNo",TrustTools.getSystemTimeData());
                    gpsMap.put("terminalId",Config.Customer);
                    gpsMap.put("driver",Config.driver);
                    gpsMap.put("lat",gpsLocation.getLatitude() );
                    gpsMap.put("lon",gpsLocation.getLongitude() );
                    gpsMap.put("alt",gpsLocation.getAltitude());
                    gpsMap.put("speed",speed);
                    gpsMap.put("bear",gpsLocation.getBearing());
                    gpsMap.put("EngineStatus",Config.EngineStatus);
                    gpsMap.put("gpsTime",TrustTools.getGPSNumTime(gpsLocation.getTime() ));

                    toJson(Config.typeGPS,TrustTools.getSystemTimeData(),gpsMap);
                    L.d("alarmMessage in  gps message  send Success !!!!!");


                }/* else {
                    isOverSpeed = false;
                }*/

            } else {
                isOverSpeed = false;
                Speeding = 0;
            }

            if (zhengchang >= 5) {

//                logger.info("---- Report GPS Data ---");


                zhengchang = 0;

                Map<String,Object> map = new WeakHashMap<>();
                map.put("type",Config.typeGPS);
                map.put("serialNo",TrustTools.getSystemTimeData());
                map.put("terminalId",Config.Customer);
                map.put("driver",Config.driver);
                map.put("lat", TrustTools.round(gpsLocation.getLatitude(),6));
                map.put("lon",TrustTools.round(gpsLocation.getLongitude(),6));
                map.put("alt",gpsLocation.getAltitude());
                map.put("speed",speed);
                map.put("bear",gpsLocation.getBearing());
                map.put("EngineStatus",Config.EngineStatus);
                map.put("gpsTime",TrustTools.getGPSNumTime(gpsLocation.getTime() ));

                toJson(Config.typeGPS,TrustTools.getSystemTimeData(),map);

                MainActivity.locationInterface.getLocation(gpsLocation);
            }
            Log.i("lh", "当前坐标 location.getLatitude():" + location.getLatitude() + "location.getLongitude() :" + location.getLongitude());
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
    protected void startGpsListening() {
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

            //locationMgr.addNmeaListener(nmeaListener);
            locationMgr.addGpsStatusListener(listener);

            gpsStarted = true;
            isWorking = true;
            status = 0;
        }
    }

    GpsStatus.Listener listener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            switch (event) {             //第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:

                    break;
                //卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:


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

                    /*
                    GpsStatus gpsStatus = locationMgr.getGpsStatus(null);

                    //获取卫星颗数的默认最大值 
                    int maxSatellites=gpsStatus.getMaxSatellites();
                    //创建一个迭代器保存所有卫星  
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    int count = 0;

                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s =  iters.next();
//                        L.d("s.getSnr();"+s.getSnr());
                        if(s.getSnr()>30)
                        {

                            count++;
                            if(count >=4)
                            {
                                satelliteInterface.getSatellite(count);
                            }
                            else
                            {
                                satelliteInterface.getSatellite(count);

                            }



                        }else{
                            satelliteInterface.getSatellite(count);
                        }


//                        Log.i("lh", "onGpsStatusChanged: 信噪比"+s.getSnr());
                    }


//                    Log.i("lh", "onGpsStatusChanged: 信噪比----------");

                    break;//定位启动 
*/
            }

            }
            ;

        }

        ;

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
    /*
        public void CheckIsWook(ElectronicFenceJsonBean bean,double MyLatitude, double MyLongitude)
        {
            L.d("autoClose success");
            for (int i = 0; i < bean.getCircleFences().size(); i++) {

                double sum = MapDistance.getDistance(MyLatitude, MyLongitude, bean.getCircleFences().get(i).getLat(), bean.getCircleFences().get(i).getLng());

//                if(sum > Bean.getCircleFences().get(i).getRadius())
                if (sum < bean.getCircleFences().get(i).getRadius()) {
                    gpsStatusNum = i;
                    GpsLoactionMaxTime++;
                    Distance = sum;//  测试阶段 在控件上显示  距离
                    Place = bean.getCircleFences().get(i).getCircleName();//地点

                    L.d("GpsLoactionMaxTime:"+GpsLoactionMaxTime);
                    if (GpsLoactionMaxTime >= Config.autoClose)   //GPS 自动关闭
                    {
                        L.d("关闭");
                        IsStopGps = true;
                        Message message = Message.obtain();
                        message.what = Config.CloseFence;
                        message.obj = IsStopGps;
                        mainGpsHanddler.sendMessage(message);
                        IsStopGps = false;
                        GpsLoactionMaxTime = 0;
                    }
                } else

                {
                    if (gpsStatusNum == i) {
                        Distance = -100;
                        Place = "";
                    }
                }

            }
        }
        */


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

    private Gson gson;
    public void toJson(int type,long serialNumber,Map<String , Object> map)
    {
        synchronized (this){
            TypeBean typeBean = null;
            String msg = new JSONObject(map).toString();
            switch (type) {
                case Config.typeGPS:
                    typeBean = gson.fromJson(msg, TypeGpsBean.class);
                    break;
                case Config.typeAlarm:
                    typeBean = gson.fromJson(msg, TypeAlarmBean.class);
                    break;
            }
            typeBean.setTypes(type);
            typeBean.setSerialNos(serialNumber+"");
            typeBean.setRawId(1);

            Config.taskQueue.add(typeBean);
            trustServer.checkType(typeBean);
        }
    }

}

