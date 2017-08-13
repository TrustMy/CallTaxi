package com.trust.shengyu.calltaxi.activitys.mainmap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.trust.shengyu.calltaxi.activitys.orderstatus.OrderStatusActivity;
import com.trust.shengyu.calltaxi.activitys.selectend.SelectEndActivity;
import com.trust.shengyu.calltaxi.base.BaseActivity;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.TrustTools;
import com.trust.shengyu.calltaxi.tools.beans.Bean;
import com.trust.shengyu.calltaxi.tools.beans.MqttResultBean;
import com.trust.shengyu.calltaxi.tools.gps.ConversionLocation;
import com.trust.shengyu.calltaxi.tools.gps.Maker;
import com.trust.shengyu.calltaxi.tools.gps.Positioning;
import com.trust.shengyu.calltaxi.tools.gps.routeplan.RoutePlan;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainMapActivity extends BaseActivity implements Positioning.PositioningListener {


    @BindView(R.id.clear)
    Button clear;
    @BindView(R.id.main_map_start_tv)
    TextView mainMapStartTv;
    @BindView(R.id.main_map_drawerlayout)
    DrawerLayout mainMapDrawerlayout;
    @BindView(R.id.main_map_toolbar)
    Toolbar mainMapToolbar;
    @BindView(R.id.main_drawerlayout_black)
    ImageButton mainDrawerlayoutBlack;
    @BindView(R.id.main_drawerlayout_user_logo)
    ImageView mainDrawerlayoutUserLogo;
    @BindView(R.id.main_drawerlayout_user_name)
    TextView mainDrawerlayoutUserName;
    @BindView(R.id.main_drawerlayout_sex)
    ImageView mainDrawerlayoutSex;
    @BindView(R.id.main_drawerlayout_user_status)
    TextView mainDrawerlayoutUserStatus;
    @BindView(R.id.main_drawerlayout_credit_points)
    TextView mainDrawerlayoutCreditPoints;
    @BindView(R.id.main_drawerlayout_trip_history_btn)
    LinearLayout mainDrawerlayoutTripHistoryBtn;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.main_map_end_tv)
    TextView mainMapEndTv;
    @BindView(R.id.main_map_order_submit_cardview_btn)
    CardView mainMapOrderSubmitCardviewBtn;//提交按钮最外面的卡片布局
    @BindView(R.id.main_map_update)
    ImageButton mainMapUpdate;
    @BindView(R.id.main_map_my_self)
    ImageButton mainMapMySelf;
    @BindView(R.id.main_order_cost)
    TextView mainOrderCost;
    @BindView(R.id.main_order_cost_layout)
    LinearLayout mainOrderCostLayout;
    @BindView(R.id.main_map_order_submit_btn)
    Button mainMapOrderSubmitBtn;
    private MapView mapView;
    private AMap aMap;
    private Marker screenMarker = null, growMarker = null;
    private ConversionLocation conversionLocation;
    private RoutePlan routePlan;
    private Context context = MainMapActivity.this;
    private int REQUEST_CODE = 1;

    private LatLonPoint mStartPoint, mEndPoint;
    private String startName, endName;
    private int taxiCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        ButterKnife.bind(this);
        mapView = (MapView) bindView(this, R.id.mainmap_map, R.id.base_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initMap();
        initView();
        if(mqttServer.resultOrderTaskQueue()){
            startActivity(new Intent(context,OrderStatusActivity.class));
        }
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
        mainMapToolbar.setTitle("");
        setSupportActionBar(mainMapToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_meuns);
        }


//        callTaxiCommHelper.setOnMqttCallBackResultListener(onMqttCallBackResultListener);

        positioning.setPostitioningListener(this);
        positioning.startGps();



        baseSetOnClick(clear);

        baseSetOnClick(mainDrawerlayoutBlack);
        baseSetOnClick(mainDrawerlayoutTripHistoryBtn);
        baseSetOnClick(mainMapEndTv);
        baseSetOnClick(mainMapOrderSubmitBtn);


        conversionLocation = new ConversionLocation();
        conversionLocation.setAddressListener(adddressListener);

        routePlan = new RoutePlan(aMap);
        routePlan.setOnRoutePlanListener(new RoutePlan.onRoutePlanListener() {
            @Override
            public void result(Object money) {
                L.d("获取费用");
                int moneys = (int) money;
                taxiCast = moneys;
                mainOrderCostLayout.setVisibility(View.VISIBLE);
                mainOrderCost.setText(moneys + "");

                startName = mainMapStartTv.getText().toString();
                endName = mainMapEndTv.getText().toString();
//                trustDialog.showOrderDialog(Config.activity, mainMapStartTv.getText().toString(), mainMapEndTv.getText().toString(), (int) money);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mainMapDrawerlayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void baseClickResult(View v) {
        Map<String, Object> map = new WeakHashMap<>();
        Map<String, Object> maps = new WeakHashMap<>();

        switch (v.getId()) {



            case R.id.clear:
                dbManager.deleteAll();
                break;

            case R.id.main_drawerlayout_black:
                mainMapDrawerlayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.main_drawerlayout_trip_history_btn:
                startActivity(new Intent(context, OrderHistoryActivity.class));
                break;

            case R.id.main_map_end_tv:
                startActivityForResult(new Intent(context, SelectEndActivity.class), REQUEST_CODE);
                break;

            case R.id.main_map_order_submit_btn:
                maps.put("startName", startName);
                maps.put("endName", endName);
                maps.put("taxiCast", taxiCast);

                map.put("type", Config.MQTT_TYPE_PLACE_AN_ORDER);
                map.put("status", true);
                map.put("msg", maps);
                map.put("time", TrustTools.getSystemTimeString());
                sendMqttMessage(Config.sendTopic, 1, new JSONObject(map).toString());

                startActivity(new Intent(context,OrderStatusActivity.class));
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
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.po)));
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
                mainMapEndTv.setText(msg);
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
        mainMapStartTv.setText(aMapLocation.getAddress());

        mStartPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
    }

    @Override
    public void onLocationError(AMapLocation aMapLocation) {
        L.d("定位失败:" + aMapLocation.getErrorCode());
    }

    @Override
    protected void getOrderDialogResult(String startName, String endName, int taxiCast) {
        L.d(startName + endName + taxiCast);


    }

    int num;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    waitOrder();
                    break;
                case 2:
                    drawLine(2, 3);

                    break;
                case 3:
                    drawLine(3, 3);
                    break;
            }
        }
    };

    public void waitOrder() {
        showSnackbar(mainMapStartTv, "司机接单了,请耐心等待,司机正在赶往你所在地.", null);
        handler.sendEmptyMessage(2);
    }

    public void drawLine(int what, int nums) {
        if (num == nums) {
            num = 0;
            handler.removeMessages(what);
            if (what == 2) {
                orderStart();
            } else if (what == 3) {
                endTrip();
            }
            return;
        }
        num++;
        List<LatLng> ml = new ArrayList<>();
        ml.add(new LatLng(31.245133, 121.417701));
        drawLiner.drawTrickLine(aMap, ml, 123123123123L);

        handler.sendEmptyMessageDelayed(what, 1000 * 10);
    }

    public void orderStart() {
        showSnackbar(mainMapStartTv, "已经上车,订单生效!", null);
        handler.sendEmptyMessage(3);
    }

    public void endTrip() {
        showSnackbar(mainMapStartTv, "本次行程结束!请支付!", null);

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int status;
        if (requestCode == REQUEST_CODE) {
            String msg = data.getStringExtra("end");
            if (msg == null) {
                L.e("什么都没有选择");
                status = View.GONE;
            } else {
                L.d("选择成功");
                status = View.VISIBLE;
                routePlan.searchRouteResult(mStartPoint, mEndPoint);
            }
            mainMapOrderSubmitCardviewBtn.setVisibility(status);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
