package com.trust.shengyu.calltaxidriver.activitys;

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
import com.trust.shengyu.calltaxidriver.tools.beans.OrderBean;
import com.trust.shengyu.calltaxidriver.tools.interfaces.LocationInterface;

import org.json.JSONObject;

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
    private MainRecyclerAdapter mainRecyclerAdapter;
    private Context context = MainActivity.this;
    @BindView(R.id.main_recycler)
    RecyclerView mainRecycler;
    private ArrayList<Object> ml;
    private int lastPosition;
    private MqttBeans bean;
    private boolean ORDER_MESSAGE = true;//跳转查看详细信息 选择是否接单
    private boolean ORDER_SUMBIT = false;//抢单后,跳转

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        initData();
        mqttServer.startGps();
        TrustServer.appStatus = true;


    }



    private void initView() {

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
                toIntent(bean, ORDER_MESSAGE);
            }
        });
        mainRecyclerAdapter.setOnSubimtListener(new MainRecyclerAdapter.onSubimtListener() {
            @Override
            public void resultSubmit(View v, int pos, Object object) {
                bean = (MqttBeans) object;
                L.d("点击了提交订单:" + bean.getContent().getOrder().getStartAddress());
                Map<String, Object> map = new WeakHashMap<>();
                map.put("orderNo", bean.getContent().getOrder().getOrderNo());
                map.put("receiveTime", TrustTools.getSystemTimeData());
                map.put("driver", Config.driver+"");
                requestCallBeack(Config.DRIVER_ORDER, map, Config.TAG_DRIVER_ORDER, trustRequest.POST,Config.token);

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



        Map<String,Object> map = new WeakHashMap<>();
        map.put("driver",Config.Customer);
        map.put("status",Config.UserTypeDriver);
        requestCallBeack(Config.SERACH_EXECUTE_ORDER,map,Config.TAG_SERACH_EXECUTE_ORDER,
                trustRequest.GET,Config.token);
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
                startActivity(new Intent(context, OrderHistoryActivity.class));
                break;
        }
    }

    Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            ml.add("3");
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
    public void toIntent(MqttBeans bean, boolean type) {
        Intent intent = new Intent(MainActivity.this, OrderStatusActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("order", bean);
        startActivity(intent);
    }


    @Override
    public void resultMqttTypePlaceAnOrder(MqttBeans bean) {

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

//                    Map<String, Object> determine = new WeakHashMap<>();
//                    determine.put("status", true);
//                    determine.put("type", Config.MQTT_TYPE_PLACE_AN_ORDER);
//                    sendMqttMessage(Config.sendTopic, 1, new JSONObject(determine).toString());
                    toIntent(bean, ORDER_SUMBIT);

                }
                break;
        }
    }

    @Override
    protected void onResume() {
        TrustServer.baseActivity = this;
        super.onResume();
    }


    public static double myLat = 31.232185,myLon = 121.413141;

    public static LocationInterface locationInterface = new LocationInterface() {
        @Override
        public void getLocation(Location location) {
            if (location != null){
                myLat = TrustTools.round(location.getLatitude(),6);
                myLon = TrustTools.round(location.getLongitude(),6);
            }
//            L.d("myLat:"+myLat+"|myLon:"+myLon);
        }
    };
}
