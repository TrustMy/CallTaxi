package com.trust.shengyu.rentalcarclient.activitys.redenvelopes;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.EXPIRED;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.UNUSED;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.USED;

public class MyRedEnvelopesActivity extends BaseActivity {

    @BindView(R.id.my_red_envelopes_tablayout)
    TabLayout myRedEnvelopesTablayout;
    @BindView(R.id.my_red_envelopes_viewpager)
    ViewPager myRedEnvelopesViewpager;

    private MyRedEnvelopesAdapter myRedEnvelopesAdapter;
    private List<Fragment> ml = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_red_envelopes);
        ButterKnife.bind(this);

        initView();
        initFragment();
    }

    private void initFragment() {
        ml.add(new MyReaEnvelopesFragment(USED));
        ml.add(new MyReaEnvelopesFragment(UNUSED));
        ml.add(new MyReaEnvelopesFragment(EXPIRED));
        myRedEnvelopesAdapter.setmList(ml);
        myRedEnvelopesAdapter.notifyDataSetChanged();
    }

    private void initView() {
        myRedEnvelopesAdapter = new MyRedEnvelopesAdapter(getSupportFragmentManager());
        myRedEnvelopesViewpager.setAdapter(myRedEnvelopesAdapter);
        myRedEnvelopesTablayout.setupWithViewPager(myRedEnvelopesViewpager);
    }
}
