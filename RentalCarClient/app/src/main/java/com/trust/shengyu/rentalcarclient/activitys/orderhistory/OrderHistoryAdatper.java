package com.trust.shengyu.rentalcarclient.activitys.orderhistory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.base.BaseRecyclerViewAdapter;
import com.trust.shengyu.rentalcarclient.tools.beans.OrderHistoryBean;

import java.util.List;

/**
 * Created by Trust on 2017/8/3.
 */

public class OrderHistoryAdatper extends BaseRecyclerViewAdapter {

    public OrderHistoryAdatper(Context mContext) {
        super(mContext);
    }

    @Override
    protected BaseRecyclerViewAdapter.ViewHolder initItemView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_item,null);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderHistoryViewHolder viewHolder = (OrderHistoryViewHolder) holder;
        viewHolder.OrderTime.setText(((List<OrderHistoryBean>)ml).get(position).getOrderTime()+"");
        viewHolder.money.setText(((List<OrderHistoryBean>)ml).get(position).getMoney()+"");
        viewHolder.tripTime.setText(((List<OrderHistoryBean>)ml).get(position).getTripTime()+"");
        viewHolder.tripMileage.setText(((List<OrderHistoryBean>)ml).get(position).getTripMileage()+"");
        viewHolder.licensePlate.setText(((List<OrderHistoryBean>)ml).get(position).getLicensePlate()+"");
    }

    class OrderHistoryViewHolder extends ViewHolder{
        TextView OrderTime,money,tripTime,tripMileage,licensePlate;
        public OrderHistoryViewHolder(View itemView) {
            super(itemView);
            OrderTime = (TextView) itemView.findViewById(R.id.orderhistory_item_tv);
            money = (TextView) itemView.findViewById(R.id.order_history_item_money);
            tripTime = (TextView) itemView.findViewById(R.id.order_history_item_tripTime);
            tripMileage = (TextView) itemView.findViewById(R.id.order_history_item_tripMileage);
            licensePlate = (TextView) itemView.findViewById(R.id.order_history_item_licensePlate);
        }
    }
}
