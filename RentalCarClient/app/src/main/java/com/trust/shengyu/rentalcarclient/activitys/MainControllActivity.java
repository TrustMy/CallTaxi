package com.trust.shengyu.rentalcarclient.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

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
import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.activitys.mainmap.MainControllCarInfoAdapter;
import com.trust.shengyu.rentalcarclient.activitys.returncar.ReturnCarOutletsActivity;
import com.trust.shengyu.rentalcarclient.base.BaseActivity;
import com.trust.shengyu.rentalcarclient.tools.L;
import com.trust.shengyu.rentalcarclient.tools.TrustTools;
import com.trust.shengyu.rentalcarclient.tools.beans.oldbeans.CarInfoBeans;
import com.trust.shengyu.rentalcarclient.tools.beans.oldbeans.TakeTheCarBean;
import com.trust.shengyu.rentalcarclient.tools.gdgps.Maker;
import com.trust.shengyu.rentalcarclient.tools.popupwindow.TrusPopupWinds;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.USER_STATUS_CERTIFIED_ING;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.USER_STATUS_CERTIFIED_SUCCESS;

public class MainControllActivity extends BaseActivity {
    Marker screenMarker = null;//中心点
    Marker growMarker = null;//车辆
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.layout_community_info)
    CardView layoutCommunityInfo;
    @BindView(R.id.main_controll_car_info_recy)
    RecyclerView mainControllCarInfoRecy;
    @BindView(R.id.main_controll_city_info_tv)
    TextView mainControllCityInfoTv;
    @BindView(R.id.main_controll_return_car_outlets_btn)
    Button mainControllReturnCarOutletsBtn;
    private LatLng destinationLatLng;//目标地点
    private List<Marker> targetPointList = new ArrayList<>();//获取的目标点
    private LatLng latlng = new LatLng(39.761, 116.434);//汽车的例子
    private int status;//客户当前认证状态
    private boolean isFitst = false;//是否第一次进来 true 定位点放到地图中心 false 什么都不做
    private Location userLocation;//客户定位高德;
    private List<Marker> markerList;//车辆点的集合;
    private Marker beforMarker;//点击后 把前一个点  图标变回来
    private List<TakeTheCarBean> theCarBeanList;//测试车辆点

    private TrusPopupWinds trusPopupWinds;//网点详情

    private MainControllCarInfoAdapter mainControllCarInfoAdapter;

    private Context context = MainControllActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_controll);
        ButterKnife.bind(this);

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

        baseMapView = (MapView) bindView(this, R.id.test_map, R.id.base_map);
        baseMapView.onCreate(savedInstanceState);// 此方法必须重写

        initMapView();
        initMap();

        String version = TrustTools.resultAppVersion(this);
        L.d("version:" + version);

        status = getIntent().getIntExtra("status", -1);
        if (status == USER_STATUS_CERTIFIED_ING) {
            showToast("资料正在认证中,请耐心等待!");
        }
        int userStatus = getIntent().getIntExtra("userStatus", -1);
        switch (userStatus) {
            case USER_STATUS_CERTIFIED_ING://认证中
                showToast("资料正在认证中,请耐心等待!");
                break;
            case USER_STATUS_CERTIFIED_SUCCESS://认证通过
                showToast("认证成功");
                break;
        }

        initView();
    }

    private void initView() {
        title.setText("xx租车");
        mainControllCarInfoAdapter = new MainControllCarInfoAdapter(context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mainControllCarInfoRecy.setLayoutManager(layoutManager);
        mainControllCarInfoRecy.setAdapter(mainControllCarInfoAdapter);

        baseSetOnClick(mainControllReturnCarOutletsBtn);
    }

    private void initMap() {
//        baseAmap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
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


        baseAmap.setOnMarkerClickListener(onMarkerClickListener);
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
        screenMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
    }

    /**
     * 监听地图上 maker点击事件
     */
    private AMap.OnMarkerClickListener onMarkerClickListener = new AMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (beforMarker != null) {
                beforMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
            }
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.car));
            beforMarker = marker;
            L.d("点击后  maker info:" + theCarBeanList.get((int) marker.getObject()).getInfo()
            );
            Maker.mobileMarker(baseAmap, theCarBeanList.get((int) marker.getObject()).getDataLatLng().latitude,
                    theCarBeanList.get((int) marker.getObject()).getDataLatLng().longitude);

//            trustDialog.showCommunityInfo(MainControllActivity.this);

            List<CarInfoBeans> ml = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                ml.add(new CarInfoBeans("车牌号:" + i, "型号x:" + i, "11", "0" + i, false));
            }
            mainControllCarInfoAdapter.setMl(ml);
            mainControllCarInfoAdapter.notifyDataSetChanged();


            layoutCommunityInfo.setVisibility(View.VISIBLE);


            return true;//true  消费 false 跳过 向下传递
        }
    };


    /**
     * 添加一个从地上生长的Marker
     */
    public void addGrowMarker() {
        theCarBeanList = new ArrayList<>();//测试汽车点
        theCarBeanList.add(new TakeTheCarBean(new LatLng(31.228787, 121.417818), "这是第一个点"));
        theCarBeanList.add(new TakeTheCarBean(new LatLng(31.229705, 121.418719), "这是第二个点"));
        theCarBeanList.add(new TakeTheCarBean(new LatLng(31.230512, 121.418741), "这是第三个点"));
        theCarBeanList.add(new TakeTheCarBean(new LatLng(31.228897, 121.423332), "这是第四个点"));
        theCarBeanList.add(new TakeTheCarBean(new LatLng(31.229521, 121.416681), "这是第五个点"));

        ArrayList<MarkerOptions> growMarkerList = new ArrayList<>();

        for (int i = 0; i < theCarBeanList.size(); i++) {
            L.e("下标是:" + i + "|infor:" + theCarBeanList.get(i).getInfo());
            MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                    .position(theCarBeanList.get(i).getDataLatLng());
            growMarkerList.add(markerOptions);
        }


//        if(growMarker == null) {
//            MarkerOptions markerOptions = new MarkerOptions().
//                    icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
//                    .position(new LatLng());
//            growMarker = baseAmap.addMarker(markerOptions);
//            targetPointList.add(growMarker);
//        }
        markerList = baseAmap.addMarkers(growMarkerList, false);
        startGrowAnimation(markerList);
    }


    /**
     * 地上生长的Marker
     */
    private void startGrowAnimation(List<Marker> markerList) {
//        if(growMarker != null) {
//            Animation animation = new ScaleAnimation(0,1,0,1);
//            animation.setInterpolator(new LinearInterpolator());
//            //整个移动所需要的时间
//            animation.setDuration(1000);
//            //设置动画
//            growMarker.setAnimation(animation);
//            //开始动画
//            growMarker.startAnimation();
//        }

        for (int i = 0; i < markerList.size(); i++) {
            markerList.get(i).setObject(i);
            Animation animation = new ScaleAnimation(0, 1, 0, 1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(1000);
            //设置动画
            markerList.get(i).setAnimation(animation);
            //开始动画
            markerList.get(i).startAnimation();
        }
    }

    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle() {
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
        baseAmap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
//               baseAmap.setMyLocationEnabled(false);停止定位
                userLocation = location;
                if (!isFitst) {
                    isFitst = true;
                    mainControllCityInfoTv.setText(location.getExtras().getString("City"));
                    Maker.mobileMarker(baseAmap, location.getLatitude(), location.getLongitude());
                }
            }
        });


    }


    @Override
    public void baseClickResult(View v) {
        switch (v.getId()) {
            case R.id.main_controll_return_car_outlets_btn:
                CarInfoBeans carInfoBean = mainControllCarInfoAdapter.getChooseCarData();
                if (carInfoBean == null) {
                    showToast("请选择一个车辆!");
                }else{
                    startActivity(new Intent(context, ReturnCarOutletsActivity.class));
                }
                break;
        }
    }
}
