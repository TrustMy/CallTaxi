package com.trust.shengyu.rentalcarclient.activitys.redenvelopes;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trust.shengyu.rentalcarclient.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Trust on 2017/9/4.
 */

public class MyReaEnvelopesFragment extends Fragment {
    View view;
    @BindView(R.id.fragment_my_red_envelopes_recycler_view)
    RecyclerView fragmentMyRedEnvelopesRecyclerView;
    Unbinder unbinder;
    private Context context;
    private int fragmentType;
    private  MyReaEnvelopesRecyclerViewAdapter myReaEnvelopesRecyclerViewAdapter;
    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public MyReaEnvelopesFragment(int fragmentType) {
        this.fragmentType = fragmentType;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(context).inflate(R.layout.fragment_my_red_envelopes, null, false);
        unbinder = ButterKnife.bind(this, view);

        initView();
        initData();
        return view;
    }

    private void initData() {
        List<String> ml = new ArrayList<>();
        for (int i = 20; i > 0; i--) {
            ml.add("this is :"+i);
        }
        myReaEnvelopesRecyclerViewAdapter.setMl(ml);
        myReaEnvelopesRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void initView() {
        myReaEnvelopesRecyclerViewAdapter = new MyReaEnvelopesRecyclerViewAdapter(context,fragmentType);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        fragmentMyRedEnvelopesRecyclerView.setLayoutManager(layoutManager);
        fragmentMyRedEnvelopesRecyclerView.setAdapter(myReaEnvelopesRecyclerViewAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
