package com.trust.shengyu.calltaxi.activitys.mainmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.activitys.orderhistory.OrderHistoryActivity;
import com.trust.shengyu.calltaxi.base.BaseActivity;
import com.trust.shengyu.calltaxi.mqtt.network.CallTaxiCommHelper;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.gps.ConversionLocation;
import com.trust.shengyu.calltaxi.tools.gps.Maker;
import com.trust.shengyu.calltaxi.tools.gps.Positioning;
import com.trust.shengyu.calltaxi.tools.gps.routeplan.RoutePlan;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMapActivity extends BaseActivity implements Positioning.PositioningListener {
    @BindView(R.id.map_order_starts)
    Button mapOrderStarts;
    @BindView(R.id.map_order_end)
    Button mapOrderEnd;
    @BindView(R.id.map_order_start_name)
    EditText mapOrderStartName;
    @BindView(R.id.map_order_end_name)
    EditText mapOrderEndName;
    @BindView(R.id.map_order_history)
    Button mapOrderHistory;
    private MapView mapView;
    private AMap aMap;
    private Marker screenMarker = null, growMarker = null;
    private ConversionLocation conversionLocation;
    private RoutePlan routePlan;
    private Context context = MainMapActivity.this;

    private LatLonPoint mStartPoint, mEndPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        ButterKnife.bind(this);
        mapView = (MapView) findViewById(R.id.base_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initMap();
        initView();

    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkersToMap();
            }
        });


        // 设置可视范围变化时的回调的接口方法
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition postion) {

                LatLng latLng = screenMarker.getPosition();
                L.d("目标点:" + latLng.toString());
                mEndPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
                conversionLocation.getAddress(new LatLonPoint(latLng.latitude, latLng.longitude));
                //屏幕中心的Marker跳动
//                startJumpAnimation();
            }
        });
    }

    private void initView() {
        positioning.setPostitioningListener(this);
        positioning.startGps();

        baseSetOnClick(mapOrderStarts);
        baseSetOnClick(mapOrderEnd);
        baseSetOnClick(mapOrderHistory);

        conversionLocation = new ConversionLocation();
        conversionLocation.setAddressListener(adddressListener);

        routePlan = new RoutePlan(aMap);
        routePlan.setOnRoutePlanListener(new RoutePlan.onRoutePlanListener() {
            @Override
            public void result(Object money) {
                trustDialog.showOrderDialog(Config.activity,mapOrderStartName.getText().toString(),mapOrderEndName.getText().toString(),(int)money);
            }
        });
    }

    @Override
    public void baseClickResult(View v) {
        switch (v.getId()) {
            case R.id.map_order_starts:
                routePlan.searchRouteResult(mStartPoint, mEndPoint);


                break;
            case R.id.map_order_end:

                break;
            case R.id.map_order_history:
                startActivity(new Intent(context,OrderHistoryActivity.class));
                break;
        }
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        addMarkerInScreenCenter();
        addGrowMarker();
    }


    /**
     * 添加一个从地上生长的Marker
     */
    public void addGrowMarker() {
        List<LatLng> ml = new ArrayList<>();
        ArrayList<Marker> ma = new ArrayList<>();
        ArrayList<MarkerOptions> l = new ArrayList<>();
        ml.add(new LatLng(31.230133, 121.417701));
        ml.add(new LatLng(31.240133, 121.417701));
        ml.add(new LatLng(31.245133, 121.417701));
        ml.add(new LatLng(31.243133, 121.417701));
        ml.add(new LatLng(31.246133, 121.417701));
        ml.add(new LatLng(31.230133, 121.417701));
        ml.add(new LatLng(31.240133, 121.417701));
        ml.add(new LatLng(31.245133, 121.417701));
        ml.add(new LatLng(31.243133, 121.417701));
        ml.add(new LatLng(31.246133, 121.417701));
        ml.add(new LatLng(31.230133, 121.417701));
        ml.add(new LatLng(31.240133, 121.417701));
        ml.add(new LatLng(31.245133, 121.417701));
        ml.add(new LatLng(31.243133, 121.417701));
        ml.add(new LatLng(31.246133, 121.417701));
        ml.add(new LatLng(31.230133, 121.417701));
        ml.add(new LatLng(31.240133, 121.417701));
        ml.add(new LatLng(31.245133, 121.417701));
        ml.add(new LatLng(31.243133, 121.417701));
        ml.add(new LatLng(31.246133, 121.417701));


        for (int i = 0; i < ml.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                    .position(ml.get(i));
            l.add(markerOptions);

        }
        ma = aMap.addMarkers(l, true);
        startGrowAnimation(ma);
    }

    /**
     * 地上生长的Marker
     */
    private void startGrowAnimation(List<Marker> ma) {
        if (growMarker != null) {
            Animation animation = new ScaleAnimation(0, 1, 0, 1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(1000);
            //设置动画
            growMarker.setAnimation(animation);
            //开始动画
            growMarker.startAnimation();
        }

        for (int i = 0; i < ma.size(); i++) {
            Animation animation = new ScaleAnimation(0, 1, 0, 1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(1000);
            //设置动画
            ma.get(i).setAnimation(animation);
            //开始动画
            ma.get(i).startAnimation();
        }
    }

    /**
     * 在屏幕中心添加一个Marker
     */
    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        screenMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
        //设置Marker在屏幕上,不跟随地图移动
        screenMarker.setPositionByPixels(screenPosition.x, screenPosition.y);

    }


    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation() {

        if (screenMarker != null) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = screenMarker.getPosition();
            L.d("中心点:" + latLng.toString());
            /*
            Point point =  aMap.getProjection().toScreenLocation(latLng);
            point.y -= dip2px(this,125);
            LatLng target = aMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if(input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f)*(1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            screenMarker.setAnimation(animation);
            //开始动画
            screenMarker.startAnimation();
            */

        } else {
            Log.e("amap", "screenMarker is null");
        }
    }


    //dip和px转换
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private ConversionLocation.ConversionLocationAddressListener adddressListener = new ConversionLocation.ConversionLocationAddressListener() {
        @Override
        public void getAddress(boolean status, String msg) {
            if (status) {
                mapOrderEndName.setText(msg);
            }
        }

        @Override
        public void getAddressList(boolean status, ConversionLocation.TripAddress tripAddress) {

        }
    };


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        L.d("定位成功:" + aMapLocation.getLatitude() + "|" + aMapLocation.getLongitude());
        Maker.showMaker(aMap, new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
        mapOrderStartName.setText(aMapLocation.getAddress());
        mStartPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
    }

    @Override
    public void onLocationError(AMapLocation aMapLocation) {
        L.d("定位失败:" + aMapLocation.getErrorCode());
    }

    @Override
    protected void getOrderDialogResult(String startName, String endName, int taxiCast) {
        L.d(startName+endName+taxiCast);
        showSnackbar(mapOrderStartName,"下单成功!请耐心等待司机接单.",null);
        handler.sendEmptyMessageDelayed(1,1000 * 10);
    }

    int num ;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    waitOrder();
                    break;
                case 2:
                    drawLine(2,3);

                    break;
                case 3:
                    drawLine(3,3);
                    break;
            }
        }
    };

    public void waitOrder(){
        showSnackbar(mapOrderStartName,"司机接单了,请耐心等待,司机正在赶往你所在地.",null);
        handler.sendEmptyMessage(2);
    }

    public void drawLine(int what , int nums){
        if(num == nums){
            num = 0;
            handler.removeMessages(what);
            if (what == 2){
                orderStart();
            }else if(what == 3){
                endTrip();
            }
            return;
        }
        num++;
        List<LatLng> ml = new ArrayList<>();
        ml.add(new LatLng(31.245133, 121.417701));
        drawLiner.drawTrickLine(aMap,ml,123123123123L);

        handler.sendEmptyMessageDelayed(what,1000 * 10);
    }

    public void orderStart(){
        showSnackbar(mapOrderStartName,"已经上车,订单生效!",null);
        handler.sendEmptyMessage(3);
    }

    public void endTrip(){
        showSnackbar(mapOrderStartName,"本次行程结束!请支付!",null);

    }
}
