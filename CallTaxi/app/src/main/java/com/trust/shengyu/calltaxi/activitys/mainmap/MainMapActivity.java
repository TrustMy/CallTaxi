package com.trust.shengyu.calltaxi.activitys.mainmap;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
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
import com.trust.shengyu.calltaxi.mqtt.TrustServer;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.TrustTools;
import com.trust.shengyu.calltaxi.tools.beans.NObodyOrderBean;
import com.trust.shengyu.calltaxi.tools.beans.PlaceAnOrderBean;
import com.trust.shengyu.calltaxi.tools.beans.RefusedOrderBean;
import com.trust.shengyu.calltaxi.tools.beans.SelectOrdersBean;
import com.trust.shengyu.calltaxi.tools.dialog.TrustDialog;
import com.trust.shengyu.calltaxi.tools.gdgps.ConversionLocation;
import com.trust.shengyu.calltaxi.tools.gdgps.Maker;
import com.trust.shengyu.calltaxi.tools.gdgps.Positioning;
import com.trust.shengyu.calltaxi.tools.gdgps.routeplan.RoutePlan;
import com.trust.shengyu.calltaxi.tools.interfaces.LocationInterface;

import java.math.BigDecimal;
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
    private double taxiCast;
    private LatLng startLatLng ,endLatLng;
    private String orderNo;
    private int orderStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);
        ButterKnife.bind(this);
        mapView = (MapView) bindView(this, R.id.mainmap_map, R.id.base_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        initMap();
        initView();


//        if(mqttServer.resultOrderTaskQueue()){
//            startActivity(new Intent(context,OrderStatusActivity.class));
//        }
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
                endLatLng = screenMarker.getPosition();
                L.d("目标点:" + endLatLng.toString());
                mEndPoint = new LatLonPoint(endLatLng.latitude, endLatLng.longitude);

                conversionLocation.getAddress(new LatLonPoint(endLatLng.latitude, endLatLng.longitude));
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
                taxiCast = (double) money;
                mainOrderCostLayout.setVisibility(View.VISIBLE);
                mainOrderCost.setText(taxiCast + "");

                startName = mainMapStartTv.getText().toString();
                endName = mainMapEndTv.getText().toString();
//                trustDialog.showOrderDialog(Config.activity, mainMapStartTv.getText().toString(), mainMapEndTv.getText().toString(), (int) money);
            }
        });

        Map<String,Object> map = new WeakHashMap<>();
        map.put("driver",Config.userId+"");
        map.put("status",Config.User);
        requestCallBeack(Config.SERACH_EXECUTE_ORDER,map,Config.TAG_SERACH_EXECUTE_ORDER,
                trustRequest.GET,Config.token);
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

        switch (v.getId()) {

            case R.id.clear:
                dbManager.deleteAll();
                break;

            case R.id.main_drawerlayout_black:
                mainMapDrawerlayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.main_drawerlayout_trip_history_btn:
//                startActivity(new Intent(context, OrderHistoryActivity.class));
                break;

            case R.id.main_map_end_tv:
                startActivityForResult(new Intent(context, SelectEndActivity.class), REQUEST_CODE);
                break;

            case R.id.main_map_order_submit_btn:

                if (myLat != 0.0 && myLon != 0.0) {
                    Map<String,Object> map = new WeakHashMap<>();
                    map.put("startAddress",startName);
                    map.put("endAddress",endName);
                    map.put("customer",Config.userId+"");
                    map.put("locationLat",myLat);
                    map.put("locationLng",myLon);

                    map.put("estimateDuration",Config.OrderEstimateDuration);
                    map.put("estimatesAmount",taxiCast + 0.1);

                    requestCallBeack(Config.PLACE_AN_ORDER,map,Config.TAG_PLACE_AN_ORDER,trustRequest.POST,Config.token);

                }else{
                    L.d("定位坐标:"+myLat+"|"+myLon);
                    showToast("定位失败,请确保GPS有信号!"+myLat+"|"+myLon);
                }


//                startActivity(new Intent(context,OrderStatusActivity.class));

                break;
        }
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
//        addMarkerInScreenCenter();
//        addGrowMarker();
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
        startLatLng = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
        Maker.showMaker(aMap, new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
        mainMapStartTv.setText(aMapLocation.getAddress());

        mStartPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
    }

    @Override
    public void onLocationError(AMapLocation aMapLocation) {
        L.d("定位失败:" + aMapLocation.getErrorCode());
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
            String endAddress = data.getStringExtra("endAddress");
            LatLonPoint endLonPoint = data.getParcelableExtra("endLatLng");
            if (data == null) {
                L.e("什么都没有选择");
                status = View.GONE;
            } else {
                mainMapEndTv.setText(endAddress);
                mEndPoint = endLonPoint;
                L.d("选择成功 endAddress:"+endAddress+"|endLonPoint:"+endLonPoint.getLatitude()+
                "|long:"+endLonPoint.getLongitude());
                status = View.VISIBLE;
                routePlan.searchRouteResult(mStartPoint, mEndPoint);
            }
            mainMapOrderSubmitCardviewBtn.setVisibility(status);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void successCallBeack(Object obj, int type) {
        String msg = (String) obj;
        switch (type) {
            case Config.TAG_PLACE_AN_ORDER:
                PlaceAnOrderBean placeAnOrderBean = gson.fromJson(msg,PlaceAnOrderBean.class);
                if(getResultStatus(placeAnOrderBean.getStatus(),msg)){
                    orderNo = placeAnOrderBean.getContent().getOrderNo();
                    orderStatus = placeAnOrderBean.getContent().getStatus();

                    L.d("下单成功:"+orderNo);
                    toIntent();
                }
                break;

            case Config.TAG_SERACH_EXECUTE_ORDER:
                String orderMsg = null;
                SelectOrdersBean selectOrdersBean = gson.fromJson(msg,SelectOrdersBean.class);
                if(getResultStatus(selectOrdersBean.getStatus(),msg)){
                    if (selectOrdersBean.getContent() != null) {
                        orderNo = selectOrdersBean.getContent().getOrderNo();
                        orderStatus = selectOrdersBean.getContent().getStatus();
                        toIntent();
                    }else{
                        L.e("没有正在进行的订单");
                    }
                }
                L.d("订单状态:"+orderMsg);
                break;
        }
    }

    private void toIntent() {
        Intent intent = new Intent(MainMapActivity.this,OrderStatusActivity.class);
        intent.putExtra("orderStatus",
                orderStatus);
        intent.putExtra("orderNo",
                orderNo);
        startActivity(intent);
    }
    private static double myLat =31.232186,myLon =121.4132;

    public static LocationInterface locationInterface = new LocationInterface() {
        @Override
        public void getLocation(final Location location) {
            if (location != null){
                Config.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        myLat = TrustTools.round(location.getLatitude(),6);
                        myLon = TrustTools.round(location.getLongitude(),6);
                        L.d("myLat:"+myLat+"|myLon:"+myLon);
                    }
                });

            }

        }
    };


    @Override
    public void resultMqttTypeRefusedOrder(RefusedOrderBean bean) {
        L.e("司机拒绝接单");
        showSnackbar(mapView, "司机拒绝:", null);
        final Dialog dialog = trustDialog.showErrorOrderDialog(this, bean.getContent().getOrder().getRemark());
        trustDialog.setErrorOrderDialogListener(new TrustDialog.onErrorOrderDialogListener() {
            @Override
            public void CallBack() {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void resultMqttTypeNobodyOrder(NObodyOrderBean bean) {
        showSnackbar(mapView, "暂时没有匹配到合适司机,请耐心等待或取消订单!", null);
    }
}
