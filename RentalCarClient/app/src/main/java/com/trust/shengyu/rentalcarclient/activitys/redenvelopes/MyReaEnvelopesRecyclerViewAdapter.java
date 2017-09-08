package com.trust.shengyu.rentalcarclient.activitys.redenvelopes;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.base.BaseRecyclerViewAdapter;

import butterknife.BindView;

import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.EXPIRED;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.UNUSED;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.USED;

/**
 * Created by Trust on 2017/9/4.
 */

public class MyReaEnvelopesRecyclerViewAdapter extends BaseRecyclerViewAdapter {


    private int fragmentType;

    public MyReaEnvelopesRecyclerViewAdapter(Context mContext ,int type) {
        super(mContext);
        this.fragmentType = type;
    }

    @Override
    protected ViewHolder initItemView(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_rea_envelopes_recycler_view, null, false);
        return new MyReaEnvelopesViewHodler(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyReaEnvelopesViewHodler myReaEnvelopesViewHodler = (MyReaEnvelopesViewHodler) holder;
        switch (fragmentType){
            case USED:// 已使用
                myReaEnvelopesViewHodler.itemMyRedRcyLogoImg.setImageResource(R.drawable.ic_used);
                myReaEnvelopesViewHodler.itemMyRedRcyLogoImg.setVisibility(View.VISIBLE);
                break;
            case UNUSED:// 未使用
                myReaEnvelopesViewHodler.itemMyRedRcyLogoImg.setVisibility(View.GONE);
                myReaEnvelopesViewHodler.itemMyRedRcyMoneyTv.setTextColor(mContext.getResources().getColor(R.color.colorOrange));
                break;
            case EXPIRED://已过期
                myReaEnvelopesViewHodler.itemMyRedRcyLogoImg.setImageResource(R.drawable.ic_expired);
                myReaEnvelopesViewHodler.itemMyRedRcyLogoImg.setVisibility(View.VISIBLE);
                break;
        }
    }

    class MyReaEnvelopesViewHodler extends ViewHolder {
        ImageView itemMyRedRcyLogoImg;
        TextView itemMyRedRcyNameTv;
        TextView itemMyRedRcyValidPeriodTv;
        TextView itemMyRedRcyObtainTimeTv;
        TextView itemMyRedRcyMoneyTv;
        public MyReaEnvelopesViewHodler(View itemView) {
            super(itemView);
            itemMyRedRcyLogoImg = (ImageView) itemView.findViewById(R.id.item_my_red_rcy_logo_img);
            itemMyRedRcyNameTv = (TextView) itemView.findViewById(R.id.item_my_red_rcy_name_tv);
            itemMyRedRcyValidPeriodTv = (TextView) itemView.findViewById(R.id.item_my_red_rcy_valid_period_tv);
            itemMyRedRcyObtainTimeTv = (TextView) itemView.findViewById(R.id.item_my_red_rcy_obtain_time_tv);
            itemMyRedRcyMoneyTv = (TextView) itemView.findViewById(R.id.item_my_red_rcy_money_tv);
        }
    }
}
