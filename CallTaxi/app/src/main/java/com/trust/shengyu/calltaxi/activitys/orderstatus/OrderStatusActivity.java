package com.trust.shengyu.calltaxi.activitys.orderstatus;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.base.BaseActivity;
import com.trust.shengyu.calltaxi.mqtt.TrustServer;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.TrustTools;
import com.trust.shengyu.calltaxi.tools.beans.Bean;
import com.trust.shengyu.calltaxi.tools.beans.MqttTypePlaceAnOrder;
import com.trust.shengyu.calltaxi.tools.beans.NObodyOrderBean;
import com.trust.shengyu.calltaxi.tools.beans.RefusedOrderBean;
import com.trust.shengyu.calltaxi.tools.dialog.TrustDialog;
import com.trust.shengyu.calltaxi.tools.gps.Maker;

import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderStatusActivity extends BaseActivity {
    @BindView(R.id.order_status_end)
    CardView orderStatusEnd;
    @BindView(R.id.order_status_driver_msg_layout)
    CardView orderStatusDriverMsgLayout;
    @BindView(R.id.order_status_submit)
    Button orderStatusSubmit;
    private MapView mapView;
    private AMap aMap;
    @BindView(R.id.map_order_cancel)
    Button mapOrderCancel;
    private Dialog orderWaitDialog;
    private int orderStatus;
    private String orderNo;
    private TrustTools trustTools;
    private boolean first = false;
    private boolean orderStatusBoolean = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        mapView = (MapView) bindView(this, R.id.order_status_map, R.id.base_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        ButterKnife.bind(this);
        initMap();

        initLayout();


        if(mqttServer == null){
            bindService(new Intent(OrderStatusActivity.this,TrustServer.class),serviceConnection, Context.BIND_AUTO_CREATE);
        }

        mqttServer.appStatus = true;
        mqttServer.filterOrder();

        baseSetOnClick(mapOrderCancel);
        baseSetOnClick(orderStatusSubmit);
    }

    public void initLayout() {
        setupLocationStyle();
        trustTools = new TrustTools();
        L.d("initLayout");
        rePlaceAnOrder();
        orderStatus = getIntent().getIntExtra("orderStatus", -1);
        L.d("status:" + orderStatus + "|orderNo:" + orderNo);
        switch (orderStatus) {
            case Config.OrderStatusBooked://下单成功 没有推送给司机
            case Config.OrderStatusDelivery://异常退出 下单成功 推送给司机
                orderWaitDialog = trustDialog.showOrderWaitDialog(this);
                orderNo = getIntent().getStringExtra("orderNo");
                trustDialog.setOnClick(new TrustDialog.onClick() {
                    @Override
                    public void CallBack() {
                        trustDialog.showExceptionDescription(OrderStatusActivity.this).setOnClickListener(
                                new TrustDialog.onDialogClickListener() {
                                    @Override
                                    public void resultMessager(String msg) {
                                        if (msg != null) {
                                            Map<String, Object> cancelMap = new WeakHashMap<>();
                                            cancelMap.put("orderNo", orderNo);
                                            cancelMap.put("status", Config.User);
                                            cancelMap.put("remark", msg);
                                            requestCallBeack(Config.CANCEL_ORDER, cancelMap, Config.TAG_CANCEL_ORDER, trustRequest.PUT, Config.token);
                                        } else {
                                            L.e("拒绝订单原因为null");
                                        }
                                    }
                                }
                        );
                    }
                });
                break;
            case Config.OrderStatusOrders://异常退出 司机已经接单
                L.d("司机已经接单");
                orderNo = getIntent().getStringExtra("orderNo");
                driverHasReceivedTheOrder();
                break;
            case Config.OrderStatusStartOrders://异常退出 司机开始订单
                L.d("司机开始订单");
                startOrder();
                break;
            case Config.OrderStatusEndOrders://异常退出 司机结束订单
                orderNo = getIntent().getStringExtra("orderNo");
                L.d("司机结束订单"+orderNo);
                endOrder();
                break;
            case Config.OrderStatusPayments://异常退出  正在支付
                L.d("正在支付");
                break;
            case Config.OrderStatusPaymentsSuccess://异常退出  支付成功
                L.d("支付成功");
                break;
            case Config.OrderStatusPaymentsError://异常退出  支付失败
                L.d("支付失败");
                break;
            case Config.OrderStatusCancelClientOrder://异常退出  客户取消订单
                L.d("客户取消订单");
                break;
            case Config.OrderStatusCancelDriverOrder://异常退出 司机取消订单
                L.d("司机取消订单");
                break;
        }

        /*
        订单长时间未响应   在拉一次   还有一个地方就是 如果没司机的话 后台推送会直接先到下单页面   这个页面是收不到的
         进这个页面的时候需要查看一下 队列里面有没有消息有的话 显示
         */
        trustTools.setCountdown(120).setCountdownCallBack(new TrustTools.CountdownCallBack() {
            @Override
            public void callBackCountDown() {
                if (!orderStatusBoolean) {
                    trustDialog.showNoOneForALongTime(OrderStatusActivity.this).setNoOneForALongTimeListener(new TrustDialog.NoOneForALongTimeListener() {
                        @Override
                        public void CallBack(View view) {
//                            trustDialog.showExceptionDescription(OrderStatusActivity.this).setOnClickListener(
//                                    new TrustDialog.onDialogClickListener() {
//                                        @Override
//                                        public void resultMessager(String msg) {
//                                            if (msg != null) {
                                                Map<String, Object> cancelMap = new WeakHashMap<>();
                                                cancelMap.put("orderNo", orderNo);
                                                cancelMap.put("status", Config.User);
                                                cancelMap.put("remark", "无人接单!");
                                                requestCallBeack(Config.CANCEL_ORDER, cancelMap, Config.TAG_CANCEL_ORDER, trustRequest.PUT, Config.token);
//                                            } else {
//                                                L.e("拒绝订单原因为null");
//                                            }
//                                        }
//                                    }
//                            );
                        }
                    });
                }
            }
        });

    }


    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }


    @Override
    public void baseClickResult(View v) {
        final Map<String, Object> map = new WeakHashMap<>();
        switch (v.getId()) {
            case R.id.map_order_cancel:
                trustDialog.showExceptionDescription(this).setOnClickListener(new TrustDialog.onDialogClickListener() {
                    @Override
                    public void resultMessager(String msg) {
                        map.put("orderNo", orderNo);
                        map.put("status", Config.User);
                        map.put("remark", msg);
                        requestCallBeack(Config.CANCEL_ORDER, map, Config.TAG_CANCEL_ORDER, trustRequest.PUT,
                                Config.token);

                    }
                });

                /*
                maps.put("msg", "我不想坐车了!");
                map.put("status", true);
                map.put("time", TrustTools.getSystemTimeString());
                map.put("type", Config.MQTT_TYPE_REFUSED_ORDER);
                map.put("msg", maps);
                sendMqttMessage(Config.sendTopic, 1, new JSONObject(map).toString());
                finish();
                */
                break;


            case R.id.order_status_submit:
                Map<String,Object> submitMap = new WeakHashMap<>();
                submitMap.put("orderNo",orderNo);
                requestCallBeack("/rest/pay",submitMap,100000,trustRequest.POST,Config.token);
                break;

        }
    }

    @Override
    public void resultMqttTypeStartOrder(MqttTypePlaceAnOrder bean) {
        startOrder();
    }

    /**
     * 开始订单
     */
    private void startOrder() {
        aMap.setMyLocationEnabled(true);
        mapOrderCancel.setVisibility(View.INVISIBLE);
        showSnackbar(mapOrderCancel, "已经上车!", null);
    }

    @Override
    public void resultMqttTypeEndOrder(MqttTypePlaceAnOrder bean) {
        endOrder();
    }

    /**
     * 结束订单
     */
    private void endOrder() {
        aMap.setMyLocationEnabled(false);
        orderStatusDriverMsgLayout.setVisibility(View.VISIBLE);
        orderStatusEnd.setVisibility(View.VISIBLE);
        showSnackbar(mapOrderCancel, "已经下车", null);
    }

    @Override
    public void resultMqttTypeRefusedOrder(RefusedOrderBean bean) {
        L.e("司机拒绝接单");
        showSnackbar(mapOrderCancel, "司机拒绝:", null);
        trustDialog.showErrorOrderDialog(this, bean.getContent().getOrder().getRemark());
        trustDialog.setErrorOrderDialogListener(new TrustDialog.onErrorOrderDialogListener() {
            @Override
            public void CallBack() {
                finish();
            }
        });
    }

    @Override
    public void resultMqttTypePlaceAnOrder(Bean bean) {
        driverHasReceivedTheOrder();
    }

    /**
     * 司机已经接单
     */
    private void driverHasReceivedTheOrder() {
        orderStatusBoolean = true;
        trustDialog.dissDialog(orderWaitDialog);
        orderStatusDriverMsgLayout.setVisibility(View.VISIBLE);
        showSnackbar(mapOrderCancel, "司机接单了!", null);
    }

    @Override
    public void resultMqttTypeNobodyOrder(NObodyOrderBean bean) {
        showSnackbar(mapView, "暂时没有匹配到合适司机,请耐心等待或取消定的那", null);
        if (bean.getStatus() == 0) {
            if (!first) {

                L.d("resultMqttTypeNobodyOrder:" + bean.getContent().getOrder());
                if (!orderStatusBoolean) {
                    reQuest();
                    first = true;
                }
            }

        }
    }

    @Override
    public void successCallBeack(Object obj, int type) {
        switch (type) {
            case Config.TAG_CANCEL_ORDER:
                finish();
                break;
            case 100000:
                showToast("支付成功");
                finish();
                break;
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        TrustServer.baseActivity = this;
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
     * 下订单后 长时间无人接单 重新请求接口
     */

    public void rePlaceAnOrder() {
        if (!orderStatusBoolean) {
            trustTools.setCountdown(15).setCountdownCallBack(new TrustTools.CountdownCallBack() {
                @Override
                public void callBackCountDown() {
                    L.d("rePlaceAnOrder callBackCountDown");
                    reQuest();
                }
            });
        }
    }

    public void reQuest() {
        L.d("reQuest");
        Map<String, Object> map = new WeakHashMap<>();
        map.put("orderNo", orderNo);
        map.put("customer", Config.CustomerId);
        requestCallBeack(Config.REPLACE_AN_ORDER, map, Config.TAG_REPLACE_AN_ORDER,
                trustRequest.POST, Config.token);
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        aMap.setMyLocationEnabled(false);
        super.onDestroy();
        mapView.onDestroy();
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
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
//               baseAmap.setMyLocationEnabled(false);停止定位
        Maker.mobileMarker(aMap, location.getLatitude(), location.getLongitude());

            }
        });


    }

}
