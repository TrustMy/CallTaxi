package com.trust.shengyu.calltaxi.activitys.orderhistory;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.base.BaseActivity;
import com.trust.shengyu.calltaxi.base.BaseRecyclerViewAdapter;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.beans.OrderHistoryBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistoryActivity extends BaseActivity {

    @BindView(R.id.orderhistory_recyclerview)
    RecyclerView orderRecyclerview;
    private OrderHistoryAdatper orderHistoryAdapterl;
    private Context context = OrderHistoryActivity.this;
    private int indexPage = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);
        initView();
        initData();

        Map<String,Object> map = new WeakHashMap<>();
        map.put("driver",Config.CustomerId);
        map.put("indexPage",indexPage);
        map.put("status",Config.UserTypeClient);

        requestCallBeack(Config.SERACH_HISTORY_ORDER_PAGING,map,Config.TAG_SERACH_HISTORY_ORDER_PAGING
        ,trustRequest.GET,Config.token);

    }

    private void initData() {
        List<OrderHistoryBean>  ms = new ArrayList<>();
        ms.add(new OrderHistoryBean(1111L,15,30,10,"沪A1111111"));
        ms.add(new OrderHistoryBean(1111L,15,30,10,"沪A1111111"));
        ms.add(new OrderHistoryBean(1111L,15,30,10,"沪A1111111"));
        ms.add(new OrderHistoryBean(1111L,15,30,10,"沪A1111111"));
        ms.add(new OrderHistoryBean(1111L,15,30,10,"沪A1111111"));
        ms.add(new OrderHistoryBean(1111L,15,30,10,"沪A1111111"));
        ms.add(new OrderHistoryBean(1111L,15,30,10,"沪A1111111"));
        ms.add(new OrderHistoryBean(1111L,15,30,10,"沪A1111111"));
        ms.add(new OrderHistoryBean(1111L,15,30,10,"沪A1111111"));
        ms.add(new OrderHistoryBean(1111L,15,30,10,"沪A1111111"));
        ms.add(new OrderHistoryBean(1111L,15,30,10,"沪A1111111"));

        orderHistoryAdapterl.setMl(ms);
        orderHistoryAdapterl.notifyDataSetChanged();
    }

    private void initView() {
        TextView textView = (TextView) bindView(this,R.id.irderhistory_toolbar,R.id.title);
        textView.setText("行程记录");
        orderHistoryAdapterl = new OrderHistoryAdatper(context);
        orderHistoryAdapterl.setItemOnClickListener(new BaseRecyclerViewAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClickListener(View v, int pos, Object msg) {
                L.d("你点击了xxx:"+pos);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        orderRecyclerview.setLayoutManager(layoutManager);
        orderRecyclerview.setAdapter(orderHistoryAdapterl);

    }
}
