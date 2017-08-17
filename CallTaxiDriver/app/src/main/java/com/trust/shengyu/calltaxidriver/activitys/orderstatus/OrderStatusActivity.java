package com.trust.shengyu.calltaxidriver.activitys.orderstatus;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.trust.shengyu.calltaxidriver.Config;
import com.trust.shengyu.calltaxidriver.R;
import com.trust.shengyu.calltaxidriver.base.BaseActivity;
import com.trust.shengyu.calltaxidriver.tools.L;
import com.trust.shengyu.calltaxidriver.tools.TrustTools;
import com.trust.shengyu.calltaxidriver.tools.beans.DiverOrderBean;
import com.trust.shengyu.calltaxidriver.tools.beans.OrderBean;
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
        OrderBean bean = (OrderBean) getIntent().getSerializableExtra("order");
        orderNo = bean.getMsg().getOrderNo();
        if (bean != null) {
            orderStatusOrderStartTv.setText(bean.getMsg().getStartName());
            orderStatusOrderEndTv.setText(bean.getMsg().getEndName());
            orderStatusOrderCost.setText(bean.getMsg().getTaxiCast() + "");
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
                Map<String, Object> determine = new WeakHashMap<>();
                determine.put("status", true);
                determine.put("type", Config.MQTT_TYPE_PLACE_AN_ORDER);
                sendMqttMessage(Config.sendTopic, 1, new JSONObject(determine).toString());
                break;
            case R.id.order_status_order_start:
                Map<String,Object> startMap = new WeakHashMap<>();
                startMap.put("orderNo",orderNo);
                startMap.put("startTime",TrustTools.getSystemTimeData());
                startMap.put("driver",Config.Customer);
                requestCallBeack(Config.DRIVER_START_ORDER,startMap,Config.TAG_DRIVER_START_ORDER,
                        trustRequest.POST);
                /*
                map.put("type", Config.MQTT_TYPE_START_ORDER);
                sendMqttMessage(Config.sendTopic, 1, new JSONObject(map).toString());
                */
                break;
            case R.id.order_status_order_end:
                Map<String,Object> endMap = new WeakHashMap<>();
                endMap.put("orderNo",orderNo);
                endMap.put("endTime",TrustTools.getSystemTimeData());
                endMap.put("driver",Config.Customer);
                endMap.put("endAddress","终点");
                endMap.put("endLat",31.222398);
                endMap.put("endLng",121.41810);
                requestCallBeack(Config.DRIVER_END_ORDER,endMap,Config.TAG_DRIVER_END_ORDER,
                        trustRequest.POST);
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
                            map.put("type", Config.MQTT_TYPE_REFUSED_ORDER);
                            map.put("msg", msg);
                            sendMqttMessage(Config.sendTopic, 1, new JSONObject(map).toString());
                            finish();
                        }else{
                            L.e("拒绝订单原因为null");
                        }
                    }
                });
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
        }
    }
}
