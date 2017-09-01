package com.trust.shengyu.rentalcarclient.activitys;

import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.amap.api.services.core.LatLonPoint;
import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.base.BaseActivity;
import com.trust.shengyu.rentalcarclient.tools.L;
import com.trust.shengyu.rentalcarclient.tools.TrustTools;

import java.util.ArrayList;
import java.util.List;

public class WifiActivity extends BaseActivity {
    Marker screenMarker = null;//中心点
    Marker growMarker = null;//车辆
    private LatLng destinationLatLng ;//目标地点
    private List<Marker> targetPointList = new ArrayList<>();//获取的目标点
    private LatLng latlng = new LatLng(39.761, 116.434);//汽车的例子
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        AndroidPermissionTool androidPermissionTool = new AndroidPermissionTool();
        androidPermissionTool.checkPermission(this);

//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
//        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
//
//        wifiBrodCast wifiBrodCast = new wifiBrodCast();
//        registerReceiver(wifiBrodCast, filter);

        baseMapView = (MapView) bindView(this,R.id.test_map,R.id.base_map);
        baseMapView.onCreate(savedInstanceState);// 此方法必须重写

        initMapView();
        initMap();

        try {
            String version = TrustTools.resultAppVersion(this);
            L.d("version:"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void initMap() {
        baseAmap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        baseAmap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle();

        baseAmap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkersToMap();
            }
        });



        // 设置可视范围变化时的回调的接口方法
        baseAmap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition position) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition postion) {
                destinationLatLng = screenMarker.getPosition();
                L.d("目标点:" + destinationLatLng.toString());
                //每次滑动一段距离后 把之前的点从地图清除 重新绘制新点
                for (Marker marker : targetPointList) {
                    marker.destroy();
                }
//                conversionLocation.getAddress(new LatLonPoint(endLatLng.latitude, endLatLng.longitude));
                //屏幕中心的Marker跳动
//                startJumpAnimation();
            }
        });
    }


    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {

        addMarkerInScreenCenter();

        addGrowMarker();
    }

    /**
     * 在屏幕中心添加一个Marker
     */
    private void addMarkerInScreenCenter() {
        LatLng latLng = baseAmap.getCameraPosition().target;
        Point screenPosition = baseAmap.getProjection().toScreenLocation(latLng);
        MarkerOptions icon = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.purple_pin));
        screenMarker = baseAmap.addMarker(icon);
        screenMarker.setObject("center");
        //设置Marker在屏幕上,不跟随地图移动
        screenMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
    }


    /**
     * 添加一个从地上生长的Marker
     */
    public void addGrowMarker() {
        if(growMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                    .position(latlng);
            growMarker = baseAmap.addMarker(markerOptions);
            targetPointList.add(growMarker);
        }
        startGrowAnimation();
    }


    /**
     * 地上生长的Marker
     */
    private void startGrowAnimation() {
        if(growMarker != null) {
            Animation animation = new ScaleAnimation(0,1,0,1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(1000);
            //设置动画
            growMarker.setAnimation(animation);
            //开始动画
            growMarker.startAnimation();
        }
    }

    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle(){
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        // 将自定义的 myLocationStyle 对象添加到地图上
        baseAmap.setMyLocationStyle(myLocationStyle);
    }
}
