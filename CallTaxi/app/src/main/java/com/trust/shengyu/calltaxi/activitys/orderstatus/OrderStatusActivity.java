package com.trust.shengyu.calltaxi.activitys.orderstatus;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        mapView = (MapView) bindView(this, R.id.order_status_map, R.id.base_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        ButterKnife.bind(this);
        initMap();


        mqttServer.appStatus = true;
        mqttServer.filterOrder();

        baseSetOnClick(mapOrderCancel);
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
                maps.put("msg", "我不想坐车了!");
                map.put("status", true);
                map.put("time", TrustTools.getSystemTimeString());
                map.put("type", Config.MQTT_TYPE_REFUSED_ORDER);
                map.put("msg", maps);
                sendMqttMessage(Config.sendTopic, 1, new JSONObject(map).toString());
                finish();
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
        orderStatusDriverMsgLayout.setVisibility(View.VISIBLE);
        showSnackbar(mapOrderCancel, "司机接单了!", null);
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
