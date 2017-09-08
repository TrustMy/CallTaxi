package com.trust.shengyu.rentalcarclient.activitys.redenvelopes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Trust on 2017/9/4.
 */

public class MyRedEnvelopesAdapter extends FragmentPagerAdapter {
    private List<Fragment> mList;

    public void setmList(List<Fragment> mList) {
        this.mList = mList;
    }

    public MyRedEnvelopesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size():0;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "已使用";
        }else if (position ==1){
            return "未使用";
        }else{
            return "已过期";
        }
    }
}
