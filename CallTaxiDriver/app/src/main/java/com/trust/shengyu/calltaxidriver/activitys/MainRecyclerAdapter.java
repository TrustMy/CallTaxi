package com.trust.shengyu.calltaxidriver.activitys;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trust.shengyu.calltaxidriver.Config;
import com.trust.shengyu.calltaxidriver.R;
import com.trust.shengyu.calltaxidriver.activitys.orderhistory.OrderHistoryAdatper;
import com.trust.shengyu.calltaxidriver.base.BaseRecyclerViewAdapter;
import com.trust.shengyu.calltaxidriver.tools.beans.MqttBeans;
import com.trust.shengyu.calltaxidriver.tools.beans.OrderBean;

import java.util.List;

/**
 * Created by Trust on 2017/8/14.
 */

public class MainRecyclerAdapter extends BaseRecyclerViewAdapter {
    public MainRecyclerAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    protected ViewHolder initItemView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_order_item,null);
        final MainRecycler mainRecycler = new MainRecyclerAdapter.MainRecycler(view);
        mainRecycler.orderSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = mainRecycler.getAdapterPosition();
                subimtListener.resultSubmit(view,pos,ml.get(pos));
            }
        });
        return mainRecycler;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainRecycler mainRecycler = (MainRecycler) holder;
        List<MqttBeans> orderBeanList = ml;
        if(orderBeanList.get(position).isSeeStatus()){
            mainRecycler.orderSubmit.setBackgroundColor(Color.parseColor("#c4c4c4"));
        }
        switch (orderBeanList.get(position).getContent().getOrder().getStatus()) {
            case Config.ORDER_STATUS_READ://只是读过订单
                mainRecycler.orderSubmit.setText("已阅览");
                mainRecycler.orderSubmit.setBackgroundColor(Color.parseColor("#f3ce1d"));
                break;
            case Config.ORDER_STATUS_CANCEL://取消订单了
                mainRecycler.orderSubmit.setText("已取消");
                mainRecycler.orderSubmit.setEnabled(false);//不可点击
                mainRecycler.itemView.setEnabled(false);
                break;
            case Config.ORDER_STATUS_END://订单已经结束了
                mainRecycler.orderSubmit.setText("已结束");
                mainRecycler.orderSubmit.setEnabled(false);//不可点击
                mainRecycler.itemView.setEnabled(false);
                break;
        }


        mainRecycler.orderStartTv.setText(orderBeanList.get(position).getContent().getOrder().getStartAddress());
        mainRecycler.orderEndTv.setText(orderBeanList.get(position).getContent().getOrder().getEndAddress());
        mainRecycler.orderFareTv.setText("+"+orderBeanList.get(position).getContent().getOrder().getEstimatesAmount()+"");
    }

    class MainRecycler extends ViewHolder{
        ImageView userImg;
        TextView userNameTv,distanceTv,orderFareTv,orderTimeTv,orderStartTv,orderEndTv,orderSubmit;
        public MainRecycler(View itemView) {
            super(itemView);
            userImg = (ImageView) itemView.findViewById(R.id.main_recycler_order_item_user_img);
            userNameTv = (TextView) itemView.findViewById(R.id.main_recycler_order_item_user_name);
            distanceTv = (TextView) itemView.findViewById(R.id.main_recycler_order_item_user_distance);
            orderFareTv = (TextView) itemView.findViewById(R.id.main_recycler_order_item_order_fare);
            orderTimeTv = (TextView) itemView.findViewById(R.id.main_recycler_order_item_order_time);
            orderStartTv = (TextView) itemView.findViewById(R.id.main_recycler_order_item_order_start_name);
            orderEndTv = (TextView) itemView.findViewById(R.id.main_recycler_order_item_order_end_name);
            orderSubmit = (TextView) itemView.findViewById(R.id.main_recycler_order_item_order_submit);
        }
    }


    public interface onSubimtListener{
        void resultSubmit(View v,int pos , Object object);
    }
    private onSubimtListener subimtListener;

    public void setOnSubimtListener(onSubimtListener subimtListener){
        this.subimtListener = subimtListener;
    }
}
