package com.trust.shengyu.calltaxi.activitys.orderhistory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.base.BaseRecyclerViewAdapter;

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

        ((OrderHistoryViewHolder)holder).textView.setText(((List<String>)ml).get(position));
    }

    class OrderHistoryViewHolder extends ViewHolder{
        TextView textView;
        public OrderHistoryViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.orderhistory_item_tv);
        }
    }
}
