package com.trust.shengyu.calltaxi.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Trust on 2017/8/3.
 */

public abstract class BaseRecyclerViewAdapter <T> extends RecyclerView.Adapter <BaseRecyclerViewAdapter.ViewHolder> {
    protected List<T> ml;//数据源
    protected Context mContext;

    public void setMl(List<T> ml) {
        this.ml = ml;
    }

    public BaseRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewHolder viewHolder = initItemView(parent,viewType);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = viewHolder.getAdapterPosition();
                mItemOnClickListener.itemOnClickListener(view,pos,ml.get(pos));
            }
        });
        return viewHolder;
    }



    protected  ViewHolder initItemView(ViewGroup parent, int viewType){
        return  null;
    };


    @Override
    public int getItemCount() {
        return ml!= null ?ml.size():0;
    }


    protected class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener){
        mItemOnClickListener = itemOnClickListener;
    }

    public interface ItemOnClickListener{
        void itemOnClickListener(View v,int pos,Object msg);
    }

    public ItemOnClickListener mItemOnClickListener;
}
