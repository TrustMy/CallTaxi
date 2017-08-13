package com.trust.shengyu.calltaxi.activitys.selectend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.activitys.orderstatus.OrderStatusActivity;
import com.trust.shengyu.calltaxi.base.BaseActivity;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.beans.Bean;
import com.trust.shengyu.calltaxi.tools.beans.MqttResultBean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectEndActivity extends BaseActivity {
    private int RESULT_CODE = 2;
    @BindView(R.id.finishbtn)
    Button finishbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_end);
        ButterKnife.bind(this);

        baseSetOnClick(finishbtn);
    }

    @Override
    public void baseClickResult(View v) {
        Intent rIntent = new Intent();
        rIntent.putExtra("end", "结束地址");
        setResult(RESULT_CODE, rIntent);
        finish();
    }



}
