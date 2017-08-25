package com.trust.shengyu.calltaxidriver.activitys.orderstatus;

import android.app.Dialog;
import android.content.Intent;
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
import com.trust.shengyu.calltaxidriver.tools.beans.SelectOrdersBean;
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
    private int RESULT_CODE = 2;
    private int orderStatus;//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        ButterKnife.bind(this);
        mapView = (MapView) bindView(this,R.id.order_status_base_map_layout,R.id.base_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        initView();

//        Map<String,Object> map = new WeakHashMap<>();
//        map.put("driver",Config.driver);
//        map.put("status",Config.Driver);
//        requestCallBeack(Config.SERACH_EXECUTE_ORDER,map,Config.TAG_SERACH_EXECUTE_ORDER,
//                trustRequest.GET,Config.token);
    }


    public void init() {
        type = getIntent().getBooleanExtra("type", false);
        if (type) {
            //跳转查看详细信息 选择是否接单
            orderStatusOrderChooseLayout.setVisibility(View.VISIBLE);
            orderStatusCancel.setVisibility(View.INVISIBLE);
            orderStatusOrderStart.setVisibility(View.INVISIBLE);
        } else {
            //抢单后,跳转
            orderStatusOrderChooseLayout.setVisibility(View.GONE);
        }
        orderStatus = getIntent().getIntExtra("orderStatus",-1);

        switch (orderStatus) {
            case Config.OrderStatusBooked://下单成功 没有推送给司机
            case Config.OrderStatusDelivery://异常退出 下单成功 推送给司机
                orderNo = getIntent().getStringExtra("orderNo");
                break;
            case Config.OrderStatusOrders://异常退出 司机已经接单
                orderNo = getIntent().getStringExtra("orderNo");
                L.d("司机已经接单");
                //抢单后,跳转
                orderStatusOrderChooseLayout.setVisibility(View.GONE);
                break;
            case Config.OrderStatusStartOrders://异常退出 司机开始订单
                L.d("司机开始订单");
                break;
            case Config.OrderStatusEndOrders://异常退出 司机结束订单
                orderNo = getIntent().getStringExtra("orderNo");
                L.d("司机结束订单"+orderNo);
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


        //正常接单跳转
        if(getIntent().getSerializableExtra("order") instanceof MqttBeans ){
            MqttBeans bean = (MqttBeans) getIntent().getSerializableExtra("order");
            orderNo = bean.getContent().getOrder().getOrderNo();
            if (bean != null) {
                orderStatusOrderStartTv.setText(bean.getContent().getOrder().getStartAddress());
                orderStatusOrderEndTv.setText(bean.getContent().getOrder().getEndAddress());
                orderStatusOrderCost.setText(bean.getContent().getOrder().getEstimateDuration() + "");
            }
        }else if(getIntent().getSerializableExtra("order") instanceof SelectOrdersBean){
            SelectOrdersBean bean = (SelectOrdersBean) getIntent().getSerializableExtra("order");
            orderNo = bean.getContent().getOrderNo();
            if (bean != null) {
                orderStatusOrderStartTv.setText(bean.getContent().getStartAddress());
                orderStatusOrderEndTv.setText(bean.getContent().getEndAddress());
                orderStatusOrderCost.setText(bean.getContent().getEstimateDuration() + "");
            }
        }

    }


    private void initView() {
        baseSetOnClick(orderStatusOrderDetermine);
        baseSetOnClick(orderStatusOrderStart);
        baseSetOnClick(orderStatusOrderEnd);
        baseSetOnClick(orderStatusCancel);
        baseSetOnClick(orderStatusOrderFinish);
    }

    @Override
    public void baseClickResult(View v) {
        final Map<String, Object> map = new WeakHashMap<>();
        map.put("status", true);
        map.put("time", TrustTools.getSystemTimeString());
        switch (v.getId()) {
            case R.id.order_status_order_determine:
                Map<String, Object> determineMap = new WeakHashMap<>();
                determineMap.put("orderNo", orderNo);
                determineMap.put("receiveTime", TrustTools.getSystemTimeData());
                determineMap.put("driver", Config.driver+"");
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

                        }else{
                            L.e("拒绝订单原因为null");
                        }
                    }
                });
                break;


            case R.id.order_status_finish:
                reslutFinish(Config.ORDER_STATUS_READ);//读过 但是没接单
                break;
        }
    }

    private void reslutFinish(int type) {
        Intent rIntent = new Intent();
        rIntent.putExtra("orderOn", orderNo);
        rIntent.putExtra("orderStatus", type);
        setResult(RESULT_CODE, rIntent);
        finish();
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
                    orderStatusCancel.setVisibility(View.INVISIBLE);
                    orderStatusOrderStart.setVisibility(View.GONE);
                    orderStatusOrderEnd.setVisibility(View.VISIBLE);
                }
                break;

            case Config.TAG_DRIVER_END_ORDER:
                showToast("订单结束");
                reslutFinish(Config.ORDER_STATUS_END);//订单结束
                break;

            case Config.TAG_CANCEL_ORDER:
                CancelOrderBean cancelOrderBean = gson.fromJson(msg,CancelOrderBean.class);
                if (getResultStatus(cancelOrderBean.getStatus(),msg)) {
                  reslutFinish(Config.ORDER_STATUS_CANCEL);//取消订单
                }

                break;

            case Config.TAG_DRIVER_ORDER:
                DriverBean driverBean = gson.fromJson(msg, DriverBean.class);
                if (getResultStatus(driverBean.getStatus(), msg)) {
                    orderStatusOrderChooseLayout.setVisibility(View.GONE);
                    orderStatusOrderStart.setVisibility(View.VISIBLE);
                    orderStatusCancel.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void resultMqttTypeRefusedOrder(RefusedOrderBean bean) {
        L.d("|resultMqttTypeRefusedOrder");
        final Dialog dialog = trustDialog.showErrorOrderDialog(this, bean.getContent().getOrder().getRemark());
        trustDialog.setErrorOrderDialogListener(new TrustDialog.onErrorOrderDialogListener() {
            @Override
            public void CallBack() {
                dialog.dismiss();
                reslutFinish(Config.ORDER_STATUS_CANCEL);
            }
        });
    }
}
