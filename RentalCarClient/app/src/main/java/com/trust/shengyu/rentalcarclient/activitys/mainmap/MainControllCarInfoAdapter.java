package com.trust.shengyu.rentalcarclient.activitys.mainmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.base.BaseRecyclerViewAdapter;
import com.trust.shengyu.rentalcarclient.tools.L;
import com.trust.shengyu.rentalcarclient.tools.beans.oldbeans.CarInfoBeans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trust on 2017/9/6.
 */

public class MainControllCarInfoAdapter extends BaseRecyclerViewAdapter {
    private List<CarInfoBeans> carInfoList;
    private List<String> changStatusList = new ArrayList<>();
    private MainControllCarInfoAdapter adapter = MainControllCarInfoAdapter.this;


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


    public CarInfoBeans getChooseCarData(){
        if(changStatusList.size() != 0 ){
            return carInfoList.get(Integer.parseInt(changStatusList.get(0)));
        }else{
            return null;
        }

    }



    @Override
    protected void changeData() {
        carInfoList = ml;
    }

    @Override
    protected ViewHolder initItemView(ViewGroup parent, int viewType) {
        final MainControllCarInfoAdapterViewHolder mainControllCarInfoAdapterViewHolder = new MainControllCarInfoAdapterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_main_controll_car_info,null,false));
        mainControllCarInfoAdapterViewHolder.carStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = mainControllCarInfoAdapterViewHolder.getAdapterPosition();//点击租车
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
        });


        return mainControllCarInfoAdapterViewHolder;
    }

    public MainControllCarInfoAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainControllCarInfoAdapterViewHolder mainControllCarInfoAdapterViewHolder = (MainControllCarInfoAdapterViewHolder) holder;
        carInfoList = ml;
        if (carInfoList.get(position).isStatus()) {
            mainControllCarInfoAdapterViewHolder.carStatusBtn.setBackgroundColor(Color.parseColor("#FF8F25"));//选中
            mainControllCarInfoAdapterViewHolder.carStatusBtn.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            mainControllCarInfoAdapterViewHolder.carStatusBtn.setBackgroundResource(R.drawable.item_recycler_car_info_btn_bg);//未选中
            mainControllCarInfoAdapterViewHolder.carStatusBtn.setTextColor(Color.parseColor("#FF8F25"));
        }
    }

    class MainControllCarInfoAdapterViewHolder extends ViewHolder{
        Button carStatusBtn;
        TextView carModels,carNumber,carMileage,carElectricity;
        public MainControllCarInfoAdapterViewHolder(View itemView) {
            super(itemView);
            carStatusBtn = (Button) itemView.findViewById(R.id.item_main_controll_car_info_car_status);
            carModels = (TextView) itemView.findViewById(R.id.item_main_controll_car_info_car_models);
            carNumber = (TextView) itemView.findViewById(R.id.item_main_controll_car_info_car_number);
            carMileage = (TextView) itemView.findViewById(R.id.item_main_controll_car_info_car_mileage);
            carElectricity = (TextView) itemView.findViewById(R.id.item_main_controll_car_info_car_electricity);
        }
    }
}
