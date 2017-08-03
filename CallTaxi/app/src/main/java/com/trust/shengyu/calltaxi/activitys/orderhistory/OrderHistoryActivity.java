package com.trust.shengyu.calltaxi.activitys.orderhistory;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.base.BaseActivity;
import com.trust.shengyu.calltaxi.base.BaseRecyclerViewAdapter;
import com.trust.shengyu.calltaxi.tools.L;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistoryActivity extends BaseActivity {

    @BindView(R.id.orderhistory_recyclerview)
    RecyclerView orderRecyclerview;
    private OrderHistoryAdatper orderHistoryAdapterl;
    private Context context = OrderHistoryActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        List<String>  ms = new ArrayList<>();
        ms.add("1111111111111111111111111111111111111111");
        ms.add("222222222222222222222222222222222222222");
        ms.add("333333333333333333333333333333333333");
        ms.add("44444444444444444444444444444444");
        ms.add("555555555555555555555555555");
        orderHistoryAdapterl.setMl(ms);
        orderHistoryAdapterl.notifyDataSetChanged();
    }

    private void initView() {
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
