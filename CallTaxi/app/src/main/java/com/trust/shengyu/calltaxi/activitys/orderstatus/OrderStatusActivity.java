package com.trust.shengyu.calltaxi.activitys.orderstatus;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.base.BaseActivity;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.TrustTools;
import com.trust.shengyu.calltaxi.tools.beans.Bean;
import com.trust.shengyu.calltaxi.tools.beans.MqttResultBean;

import org.json.JSONObject;

import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderStatusActivity extends BaseActivity {
    @BindView(R.id.order_status_end)
    CardView orderStatusEnd;
    @BindView(R.id.order_status_driver_msg_layout)
    CardView orderStatusDriverMsgLayout;
    private MapView mapView;
    private AMap aMap;
    @BindView(R.id.map_order_cancel)
    Button mapOrderCancel;
    private Dialog orderWaitDialog;
    private int orderStatus;
    private String orderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        mapView = (MapView) bindView(this, R.id.order_status_map, R.id.base_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        ButterKnife.bind(this);
        initMap();

        initLayout();

        mqttServer.appStatus = true;
        mqttServer.filterOrder();

        baseSetOnClick(mapOrderCancel);
    }

    public void initLayout(){
        orderStatus = getIntent().getIntExtra("orderStatus",-1);
        L.d("status:"+orderStatus+"|orderNo:"+orderNo);
        switch (orderStatus) {
            case Config.OrderStatusBooked://下单成功 没有推送给司机
            case Config.OrderStatusDelivery://异常退出 下单成功 推送给司机
                orderWaitDialog = trustDialog.showOrderWaitDialog(this);
                orderNo = getIntent().getStringExtra("orderNo");
                break;
            case Config.OrderStatusOrders://异常退出 司机已经接单
                break;
            case Config.OrderStatusStartOrders://异常退出 司机开始订单
                break;
            case Config.OrderStatusEndOrders://异常退出 司机结束订单
                break;
            case Config.OrderStatusPayments://异常退出  正在支付
                break;
            case Config.OrderStatusPaymentsSuccess://异常退出  支付成功
                break;
            case Config.OrderStatusPaymentsError://异常退出  支付失败
                break;
            case Config.OrderStatusCancelClientOrder://异常退出  客户取消订单
                break;
            case Config.OrderStatusCancelDriverOrder://异常退出 司机取消订单
                break;
        }
    }


    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }


    @Override
    public void baseClickResult(View v) {
        Map<String, Object> map = new WeakHashMap<>();
        Map<String, Object> maps = new WeakHashMap<>();
        switch (v.getId()) {
            case R.id.map_order_cancel:
                map.put("orderNo",orderNo);
                map.put("status",Config.UserTypeClient);
                map.put("remark","理由是,我不想坐车了.");
                requestCallBeack(Config.CANCEL_ORDER,map,Config.TAG_CANCEL_ORDER,trustRequest.PUT);

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

        }
    }

    @Override
    public void resultMqttTypeStartOrder(MqttResultBean bean) {
        showSnackbar(mapOrderCancel, "已经上车!", null);
    }

    @Override
    public void resultMqttTypeEndOrder(MqttResultBean bean) {
        orderStatusDriverMsgLayout.setVisibility(View.VISIBLE);
        orderStatusEnd.setVisibility(View.VISIBLE);
        showSnackbar(mapOrderCancel, "已经下车", null);
    }

    @Override
    public void resultMqttTypeRefusedOrder(MqttResultBean bean) {
        L.e("司机拒绝接单");
        showSnackbar(mapOrderCancel, "司机拒绝:" + bean.getMsg(), null);
        trustDialog.showErrorOrderDialog(this,bean.getMsg());
    }

    @Override
    public void resultMqttTypePlaceAnOrder(Bean bean) {
        trustDialog.dissDialog(orderWaitDialog);
        orderStatusDriverMsgLayout.setVisibility(View.VISIBLE);
        showSnackbar(mapOrderCancel, "司机接单了!", null);
    }


    @Override
    public void successCallBeack(Object obj, int type) {
        switch (type){
            case Config.TAG_CANCEL_ORDER:
                L.d("返回结果:"+obj);
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
}
