package com.trust.shengyu.rentalcarclient;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Trust on 2017/8/30.
 */

public abstract class TrustBaseActivity  extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");
        }
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        initLocalData();
        initNetworkData();
    }

    /**
     * @return 绑定子activity  布局文件
     */
    protected abstract int getLayoutId();


    /**
     * 初始化本地数据
     */
    protected abstract void initLocalData();

    /**
     * 初始化网络数据
     */
    protected abstract void initNetworkData();


}
