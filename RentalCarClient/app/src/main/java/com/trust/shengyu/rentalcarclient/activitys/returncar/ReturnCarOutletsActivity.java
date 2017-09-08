package com.trust.shengyu.rentalcarclient.activitys.returncar;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.base.BaseActivity;
import com.trust.shengyu.rentalcarclient.tools.beans.oldbeans.CarInfoBeans;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReturnCarOutletsActivity extends BaseActivity {

    @BindView(R.id.base_back)
    ImageView baseBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.return_car_outlets_recycler_view)
    RecyclerView returnCarOutletsRecyclerView;
    @BindView(R.id.return_car_outlets_choose_btn)
    Button returnCarOutletsChooseBtn;

    private ReturnCarOutletsAdapter returnCarOutletsAdapter;
    private Context context = ReturnCarOutletsActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_car_outlets);
        ButterKnife.bind(this);

        initView();

        initData();
    }

    private void initData() {
        List<CarInfoBeans> ml = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ml.add(new CarInfoBeans("车牌号:"+i,"型号:"+i,"历程:"+i,"电量:"+i,false));
        }
        returnCarOutletsAdapter.setMl(ml);
        returnCarOutletsAdapter.notifyDataSetChanged();
    }

    private void initView() {
        baseBack.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        title.setText("还车点查询");

        baseSetOnClick(baseBack);
        baseSetOnClick(search);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        returnCarOutletsRecyclerView.setLayoutManager(layoutManager);
        returnCarOutletsAdapter = new ReturnCarOutletsAdapter(context);
        returnCarOutletsRecyclerView.setAdapter(returnCarOutletsAdapter);
    }

    @Override
    public void baseClickResult(View v) {
        switch (v.getId()) {
            case R.id.base_back:
                finish();
                break;
            case R.id.search:
                showToast("点了搜索");
                break;
        }
    }
}
