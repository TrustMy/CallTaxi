package com.trust.shengyu.calltaxidriver.activitys.orderstatus;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.trust.shengyu.calltaxidriver.Config;
import com.trust.shengyu.calltaxidriver.R;
import com.trust.shengyu.calltaxidriver.activitys.MainActivity;
import com.trust.shengyu.calltaxidriver.base.BaseActivity;
import com.trust.shengyu.calltaxidriver.mqtt.TrustServer;
import com.trust.shengyu.calltaxidriver.tools.L;
import com.trust.shengyu.calltaxidriver.tools.TrustTools;
import com.trust.shengyu.calltaxidriver.tools.beans.CancelOrderBean;
import com.trust.shengyu.calltaxidriver.tools.beans.DiverOrderBean;
import com.trust.shengyu.calltaxidriver.tools.beans.DriverBean;
import com.trust.shengyu.calltaxidriver.tools.beans.MqttBeans;
import com.trust.shengyu.calltaxidriver.tools.beans.OrderBean;
import com.trust.shengyu.calltaxidriver.tools.beans.RefusedOrderBean;
import com.trust.shengyu.calltaxidriver.tools.dialog.TrustDialog;

import org.json.JSONObject;

import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderStatusActivity extends BaseActivity {

    @BindView(R.id.order_status_order_start_tv)
    TextView orderStatusOrderStartTv;
    @BindView(R.id.order_status_order_end_tv)
    TextView orderStatusOrderEndTv;
    @BindView(R.id.order_status_order_cost)
    TextView orderStatusOrderCost;
    @BindView(R.id.order_status_order_determine)
    Button orderStatusOrderDetermine;
    @BindView(R.id.order_status_finish)
    Button orderStatusOrderFinish;
    @BindView(R.id.order_status_order_choose_layout)
    LinearLayout orderStatusOrderChooseLayout;
    @BindView(R.id.order_status_order_start)
    Button orderStatusOrderStart;
    @BindView(R.id.order_status_order_end)
    Button orderStatusOrderEnd;
    @BindView(R.id.order_status_cancel)
    Button orderStatusCancel;

    private boolean type;
    private MapView mapView;
    private String orderNo;
    private String CancelMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        ButterKnife.bind(this);
        mapView = (MapView) bindView(this,R.id.order_status_base_map_layout,R.id.base_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        initView();
    }


    public void init() {
        type = getIntent().getBooleanExtra("type", false);
        if (type) {
            //跳转查看详细信息 选择是否接单
            orderStatusOrderChooseLayout.setVisibility(View.VISIBLE);
        } else {
            //抢单后,跳转
            orderStatusOrderChooseLayout.setVisibility(View.GONE);
        }
        MqttBeans bean = (MqttBeans) getIntent().getSerializableExtra("order");
        orderNo = bean.getContent().getOrder().getOrderNo();
        if (bean != null) {
            orderStatusOrderStartTv.setText(bean.getContent().getOrder().getStartAddress());
            orderStatusOrderEndTv.setText(bean.getContent().getOrder().getEndAddress());
            orderStatusOrderCost.setText(bean.getContent().getOrder().getEstimateDuration() + "");
        }
    }


    private void initView() {
        baseSetOnClick(orderStatusOrderDetermine);
        baseSetOnClick(orderStatusOrderStart);
        baseSetOnClick(orderStatusOrderEnd);
        baseSetOnClick(orderStatusCancel);
    }

    @Override
    public void baseClickResult(View v) {
        final Map<String, Object> map = new WeakHashMap<>();
        map.put("status", true);
        map.put("time", TrustTools.getSystemTimeString());
        switch (v.getId()) {
            case R.id.order_status_order_determine:
                Map<String, Object> determineMap = new WeakHashMap<>();
                map.put("orderNo", orderNo);
                map.put("receiveTime", TrustTools.getSystemTimeData());
                map.put("driver", Config.driver+"");
                requestCallBeack(Config.DRIVER_ORDER, determineMap, Config.TAG_DRIVER_ORDER, trustRequest.POST,Config.token);
                break;
            case R.id.order_status_order_start:
                Map<String,Object> startMap = new WeakHashMap<>();
                startMap.put("orderNo",orderNo);
                startMap.put("startTime",TrustTools.getSystemTimeData());
                startMap.put("driver",Config.driver+"");
                startMap.put("startLat", MainActivity.myLat);
                startMap.put("startLng",MainActivity.myLon);
                requestCallBeack(Config.DRIVER_START_ORDER,startMap,Config.TAG_DRIVER_START_ORDER,
                        trustRequest.POST,Config.token);
                /*
                map.put("type", Config.MQTT_TYPE_START_ORDER);
                sendMqttMessage(Config.sendTopic, 1, new JSONObject(map).toString());
                */
                break;
            case R.id.order_status_order_end:
                Map<String,Object> endMap = new WeakHashMap<>();
                endMap.put("orderNo",orderNo);
                endMap.put("endTime",TrustTools.getSystemTimeData());
                endMap.put("driver",Config.driver+"");
                endMap.put("endAddress","终点");
                endMap.put("endLat",MainActivity.myLat);
                endMap.put("endLng",MainActivity.myLon);
                requestCallBeack(Config.DRIVER_END_ORDER,endMap,Config.TAG_DRIVER_END_ORDER,
                        trustRequest.POST,Config.token);
                /*
                map.put("type", Config.MQTT_TYPE_END_ORDER);
                sendMqttMessage(Config.sendTopic, 1, new JSONObject(map).toString());
                */
                break;


            case R.id.order_status_cancel:
                trustDialog.showExceptionDescription(this).setOnClickListener(new TrustDialog.onDialogClickListener() {


                    @Override
                    public void resultMessager(String msg) {
                        if (msg != null) {
                            CancelMsg = msg;
                            Map<String, Object> cancelMap = new WeakHashMap<>();
                            cancelMap.put("orderNo",orderNo);
                            cancelMap.put("status",Config.Driver);
                            cancelMap.put("remark",msg);
                            requestCallBeack(Config.CANCEL_ORDER,cancelMap,Config.TAG_CANCEL_ORDER,trustRequest.PUT
                            ,Config.token);

                            /*
                            map.put("type", Config.MQTT_TYPE_REFUSED_ORDER);
                            map.put("msg", msg);
                            sendMqttMessage(Config.sendTopic, 1, new JSONObject(map).toString());
                            */
                            finish();
                        }else{
                            L.e("拒绝订单原因为null");
                        }
                    }
                });
                break;


            case R.id.order_status_finish:
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
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {

        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void successCallBeack(Object obj, int type) {
        String msg = (String) obj;
        switch (type){
            case Config.TAG_DRIVER_START_ORDER:
                DiverOrderBean diverOrderBean = gson.fromJson(msg,DiverOrderBean.class);
                if(getResultStatus(diverOrderBean.getStatus(),msg)){
                    L.d("开始成功");
                    Map<String, Object> map = new WeakHashMap<>();
                    map.put("status", true);
                    map.put("time", TrustTools.getSystemTimeString());
                    map.put("type", Config.MQTT_TYPE_START_ORDER);
                    sendMqttMessage(Config.sendTopic, 1, new JSONObject(map).toString());
                }
                break;

            case Config.TAG_DRIVER_END_ORDER:
                final Map<String, Object> map = new WeakHashMap<>();
                map.put("status", true);
                map.put("time", TrustTools.getSystemTimeString());
                map.put("type", Config.MQTT_TYPE_END_ORDER);
                sendMqttMessage(Config.sendTopic, 1, new JSONObject(map).toString());
                break;

            case Config.TAG_CANCEL_ORDER:
                CancelOrderBean cancelOrderBean = gson.fromJson(msg,CancelOrderBean.class);
                if (getResultStatus(cancelOrderBean.getStatus(),msg)) {
                    final Map<String, Object> canCelMap = new WeakHashMap<>();
                    canCelMap.put("status", true);
                    canCelMap.put("time", TrustTools.getSystemTimeString());
                    canCelMap.put("type", Config.MQTT_TYPE_REFUSED_ORDER);
                    canCelMap.put("msg", CancelMsg);
                    sendMqttMessage(Config.sendTopic, 1, new JSONObject(canCelMap).toString());
                }

                break;

            case Config.TAG_DRIVER_ORDER:
                DriverBean driverBean = gson.fromJson(msg, DriverBean.class);
                if (getResultStatus(driverBean.getStatus(), msg)) {
                    orderStatusOrderChooseLayout.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void resultMqttTypeRefusedOrder(RefusedOrderBean bean) {
        L.d("|resultMqttTypeRefusedOrder");
        trustDialog.showErrorOrderDialog(this,bean.getContent().getOrder().getRemark());
    }
}
