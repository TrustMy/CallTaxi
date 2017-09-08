package com.trust.shengyu.rentalcarclient.activitys.returncar;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.base.BaseRecyclerViewAdapter;
import com.trust.shengyu.rentalcarclient.tools.beans.oldbeans.CarInfoBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trust on 2017/9/8.
 */

public class ReturnCarOutletsAdapter extends BaseRecyclerViewAdapter {
    private List<CarInfoBeans> carInfoList;
    private List<String> changStatusList = new ArrayList<>();
    private ReturnCarOutletsAdapter adapter = this;
    @Override
    protected ViewHolder initItemView(ViewGroup parent, int viewType) {
        return new ReturnCarOutletsAdapterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_return_car_outlets_adapter,null,false));
    }

    public ReturnCarOutletsAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReturnCarOutletsAdapterViewHolder adapterViewHolder = (ReturnCarOutletsAdapterViewHolder) holder;
        carInfoList = ml;
        if (carInfoList.get(position).isStatus()) {
            adapterViewHolder.layout.setBackgroundColor(Color.parseColor("#00ff00"));
        }else{
            adapterViewHolder.layout.setBackgroundColor(Color.parseColor("#ff0000"));
        }
    }


    @Override
    protected void ItemOnClick(int pos) {
        if (checkSelectList(pos)) {
            carInfoList = ml;
            boolean status = carInfoList.get(pos).isStatus();
            if (status) {
                carInfoList.get(pos).setStatus(false);
            }else{
                carInfoList.get(pos).setStatus(true);
            }
            adapter.notifyItemChanged(pos);
        }else{
            Toast.makeText(mContext,"小伙子你多选了!",Toast.LENGTH_LONG).show();
        }
    }

    class ReturnCarOutletsAdapterViewHolder extends ViewHolder{
        CardView layout;
        public ReturnCarOutletsAdapterViewHolder(View itemView) {
            super(itemView);
            layout = (CardView) itemView.findViewById(R.id.item_return_car_outlets_layout);
        }
    }


    /**
     *  单选逻辑
     * @param pos
     * @return
     */

    private boolean  checkSelectList(int pos){
        if (changStatusList.indexOf(pos+"") != -1) {//能找到
            if (changStatusList.size() == 1) {//取消  删除
                changStatusList.remove(pos+"");
                return true;
            }else {
                return false;
            }
        }else{//找不到
            if (changStatusList.size() < 1) {//添加 选中
                changStatusList.add(pos+"");
                return  true;
            }else {
                return false;
            }
        }
    }

    @Override
    protected void changeData() {
        carInfoList = ml;
    }
}
