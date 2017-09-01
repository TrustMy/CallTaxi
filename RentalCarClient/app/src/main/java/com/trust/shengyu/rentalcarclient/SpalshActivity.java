package com.trust.shengyu.rentalcarclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.trust.shengyu.rentalcarclient.activitys.registerandlogin.LoginActivity;

import butterknife.BindView;

public class SpalshActivity extends TrustBaseActivity {

    @BindView(R.id.activity_spalsh_logo)
    ImageView activitySpalshLogo;
    @BindView(R.id.activity_spalsh_time)
    TextView activitySpalshTime;

    private String logoUrl = "https://b-ssl.duitang.com/uploads/item/201509/15/20150915145054_5idKU.jpeg";

    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_spalsh;
    }

    @Override
    protected void initLocalData() {

    }

    @Override
    protected void initNetworkData() {


        Glide.with(SpalshActivity.this).load(logoUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(activitySpalshLogo);


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 3; i > -1; i--) {
                    Message message = Message.obtain();
                    message.what = 1;
                    message.arg1 = i;
                    handler.sendMessageDelayed(message,1000);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        new Thread(runnable).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int timeNum = msg.arg1;
            activitySpalshTime.setText(timeNum+"");
            if (timeNum==0) {
                Intent intent = new Intent();
                intent.setClass(SpalshActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };


}
