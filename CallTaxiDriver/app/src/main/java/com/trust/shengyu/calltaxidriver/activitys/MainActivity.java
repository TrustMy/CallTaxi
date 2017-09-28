package com.trust.shengyu.calltaxidriver.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trust.shengyu.calltaxidriver.Config;
import com.trust.shengyu.calltaxidriver.R;
import com.trust.shengyu.calltaxidriver.activitys.orderhistory.OrderHistoryActivity;
import com.trust.shengyu.calltaxidriver.activitys.orderstatus.OrderStatusActivity;
import com.trust.shengyu.calltaxidriver.base.BaseActivity;
import com.trust.shengyu.calltaxidriver.base.BaseRecyclerViewAdapter;
import com.trust.shengyu.calltaxidriver.mqtt.TrustServer;
import com.trust.shengyu.calltaxidriver.tools.L;
import com.trust.shengyu.calltaxidriver.tools.TrustTools;
import com.trust.shengyu.calltaxidriver.tools.beans.Bean;
import com.trust.shengyu.calltaxidriver.tools.beans.DriverBean;
import com.trust.shengyu.calltaxidriver.tools.beans.MqttBeans;
import com.trust.shengyu.calltaxidriver.tools.beans.RefusedOrderBean;
import com.trust.shengyu.calltaxidriver.tools.beans.SelectOrdersBean;
import com.trust.shengyu.calltaxidriver.tools.dialog.TrustDialog;
import com.trust.shengyu.calltaxidriver.tools.interfaces.LocationInterface;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.main_recycler_show_unread_read_num)
    TextView mainRecyclerShowUnreadReadNum;
    @BindView(R.id.main_recycler_show_unread_read)
    LinearLayout mainRecyclerShowUnreadRead;
    @BindView(R.id.main_map_drawerlayout)
    DrawerLayout mainMapDrawerlayout;
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
    @BindView(R.id.main_drawerlayout_trip_history_btn)
    LinearLayout mainDrawerlayoutTripHistoryBtn;
    @BindView(R.id.driver_name)
    TextView driverName;
    private MainRecyclerAdapter mainRecyclerAdapter;
    private Context context = MainActivity.this;
    @BindView(R.id.main_recycler)
    RecyclerView mainRecycler;
    private ArrayList<MqttBeans> ml;
    private int lastPosition;
    private MqttBeans bean;
    private boolean ORDER_MESSAGE = true;//跳转查看详细信息 选择是否接单
    private boolean ORDER_SUMBIT = false;//抢单后,跳转
    private int REQUEST_CODE = 1;
    private String orderNo;//订单号
    private int orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initData();

        if (mqttServer == null) {
            bindService(new Intent(context, TrustServer.class), serviceConnection, Context.BIND_AUTO_CREATE);
//            mqttServer.doClientConnectionMqtt();
//            mqttServer.startGps();
        }


        TrustServer.appStatus = true;

    }


    private void initView() {
        driverName.setText("姓名:"+Config.driverPhone);
        baseSetOnClick(mainDrawerlayoutBlack);
        baseSetOnClick(mainDrawerlayoutTripHistoryBtn);

        Toolbar toolbar = (Toolbar) bindView(this, R.id.main_base_include, R.id.base_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_meuns);
        }

        mainRecyclerAdapter = new MainRecyclerAdapter(context);
        mainRecyclerAdapter.setItemOnClickListener(new BaseRecyclerViewAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClickListener(View v, int pos, Object msg) {
                bean = (MqttBeans) msg;
                L.d("你点击了xxx:" + bean.getContent().getOrder().getStartAddress());
                orderStatus = bean.getContent().getOrder().getStatus();
                toIntent(orderStatus, bean, ORDER_MESSAGE);
            }
        });
        mainRecyclerAdapter.setOnSubimtListener(new MainRecyclerAdapter.onSubimtListener() {
            @Override
            public void resultSubmit(View v, int pos, Object object) {
                bean = (MqttBeans) object;
                L.d("点击了提交订单:" + bean.getContent().getOrder().getStartAddress());
                Map<String, Object> map = new WeakHashMap<>();
                orderNo = bean.getContent().getOrder().getOrderNo();
                map.put("orderNo", orderNo);
                map.put("receiveTime", TrustTools.getSystemTimeData());
                map.put("driver", Config.driver + "");
                requestCallBeack(Config.DRIVER_ORDER, map, Config.TAG_DRIVER_ORDER, trustRequest.POST, Config.token);

            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        mainRecycler.setLayoutManager(layoutManager);
        mainRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                //通过这个lastChildView得到这个view当前的position值

                lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);

                L.d("当前显示最后一个item pos:" + lastPosition);
                if (lastPosition == ml.size() - 1) {
                    L.d("数据源最后一个");

                    mainRecyclerShowUnreadReadNum.setText(0 + "");
                } else {
                    int num = ml.size() - 1 - lastPosition;
                    mainRecyclerShowUnreadReadNum.setText(num + "");
                }
            }
        });
        mainRecycler.setAdapter(mainRecyclerAdapter);


        Map<String, Object> map = new WeakHashMap<>();
        map.put("driver", Config.driver);
        map.put("status", Config.Driver);
        requestCallBeack(Config.SERACH_EXECUTE_ORDER, map, Config.TAG_SERACH_EXECUTE_ORDER,
                trustRequest.GET, Config.token);
    }

    private void initData() {
        ml = new ArrayList<>();
//        hand.sendEmptyMessageDelayed(1, 1000 * 60);
        mainRecyclerShowUnreadRead.setVisibility(View.GONE);

    }

    @Override
    public void baseClickResult(View v) {

        switch (v.getId()) {
            case R.id.main_drawerlayout_black:

                mainMapDrawerlayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.main_drawerlayout_trip_history_btn:
//                startActivity(new Intent(context, OrderHistoryActivity.class));
                break;
        }
    }

    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {

//            ml.add("3");
//            mainRecycler.smoothScrollToPosition(ml.size()-1);
            mainRecyclerAdapter.setMl(ml);
            mainRecyclerAdapter.notifyDataSetChanged();
            hand.sendEmptyMessageDelayed(1, 1000 * 60);

            int num = ml.size() - 1 - lastPosition;
            mainRecyclerShowUnreadReadNum.setText(num + "");
            L.d("有多少条未读:" + num + "|当前是第:" + lastPosition);
        }
    };

    //侧滑调出
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mainMapDrawerlayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    //跳转 orderStatus页面
    public void toIntent(int orderstatus, Bean bean, boolean type) {
        Intent intent = new Intent(MainActivity.this, OrderStatusActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("orderStatus", orderstatus);
        intent.putExtra("order", bean);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    public void resultMqttTypePlaceAnOrder(MqttBeans bean) {
        L.d("来看订单的状态:" + bean.getOrderStatus());
        L.d("来订单了:" + bean.getContent().getOrder().getStartAddress() + "|" + bean.getContent().getOrder().getEndAddress() + "|" +
                bean.getContent().getOrder().getEstimatesAmount());
        ml.add(bean);
        mainRecyclerAdapter.setMl(ml);
        mainRecyclerAdapter.notifyDataSetChanged();
    }


    @Override
    public void successCallBeack(Object obj, int type) {
        String msg = (String) obj;
        switch (type) {
            case Config.TAG_DRIVER_ORDER:
                DriverBean driverBean = gson.fromJson(msg, DriverBean.class);
                if (getResultStatus(driverBean.getStatus(), msg)) {
                    orderStatus = bean.getContent().getOrder().getStatus();
                    toIntent(orderStatus, bean, ORDER_SUMBIT);
                } else {
                    for (int i = 0; i < ml.size(); i++) {
                        if (orderNo.equals(ml.get(i).getContent().getOrder().getOrderNo())) {

                            ml.get(i).setSeeStatus(true);
                            ml.get(i).setOrderStatus(Config.ORDER_STATUS_END);
                        }
                    }
                    mainRecyclerAdapter.setMl(ml);
                    mainRecyclerAdapter.notifyDataSetChanged();
                }
                break;

            case Config.TAG_SERACH_EXECUTE_ORDER:
                String orderMsg = null;
                SelectOrdersBean selectOrdersBean = gson.fromJson(msg, SelectOrdersBean.class);
                if (getResultStatus(selectOrdersBean.getStatus(), msg)) {
                    if (selectOrdersBean.getContent() != null) {
                        orderStatus = selectOrdersBean.getContent().getStatus();
                        toIntent(orderStatus, selectOrdersBean, ORDER_SUMBIT);
                    } else {
                        L.e("没有正在进行的订单");
                    }
                }
                L.d("订单状态:" + orderMsg);
                break;
        }
    }

    @Override
    protected void onResume() {
        TrustServer.baseActivity = this;
        super.onResume();
    }


    public static double myLat = 0, myLon = 0;

    public static LocationInterface locationInterface = new LocationInterface() {
        @Override
        public void getLocation(Location location) {
            if (location != null) {
                myLat = TrustTools.round(location.getLatitude(), 6);
                myLon = TrustTools.round(location.getLongitude(), 6);
            }
            L.d("myLat:" + myLat + "|myLon:" + myLon);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {

            if (data != null) {
                String ordersOn = data.getStringExtra("orderOn");
                int statusType = data.getIntExtra("orderStatus", -1);
                if (statusType == -1) {
                    L.e("statusType = -1");
                }
                for (int i = 0; i < ml.size(); i++) {
                    if (ordersOn.equals(ml.get(i).getContent().getOrder().getOrderNo())) {
                        ml.get(i).setSeeStatus(true);
                        ml.get(i).setOrderStatus(statusType);
                    }
                }
                mainRecyclerAdapter.setMl(ml);
                mainRecyclerAdapter.notifyDataSetChanged();
            } else {
                L.e("ordersOn = null");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void resultMqttTypeRefusedOrder(RefusedOrderBean bean) {
        L.d("|resultMqttTypeRefusedOrder");
        final Dialog dialog = trustDialog.showErrorOrderDialog(this, bean.getContent().getOrder().getRemark());
        trustDialog.setErrorOrderDialogListener(new TrustDialog.onErrorOrderDialogListener() {
            @Override
            public void CallBack() {
                dialog.dismiss();
            }
        });
    }
}
