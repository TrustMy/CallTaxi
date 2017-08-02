package com.trust.shengyu.calltaxi.activitys;

import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trust.shengyu.calltaxi.BaseActivity;
import com.trust.shengyu.calltaxi.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {
    private Context context = LoginActivity.this;

    @BindView(R.id.login_user)
    EditText loginUser;
    @BindView(R.id.login_pwd)
    EditText loginPwd;
    @BindView(R.id.login_submit)
    Button loginSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initDate();

    }

    private void initDate() {
        WifiManager wifiManager  = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<android.net.wifi.ScanResult> list =  wifiManager.getScanResults();
        for (int i = 0; i < list.size(); i++) {
            Log.d("lhh", "initDate:  wifi:BSSID"+list.get(i).BSSID+"|SSID:"+list.get(i).SSID);
        }
    }


    private void initView() {
        baseSetOnClick(loginSubmit);
    }

    @Override
    public void baseClickResult(View v) {
        String user = baseCheckIsNull(loginUser,"账号错误!");
        if (user != null) {
            String pwd = baseCheckIsNull(loginPwd,"密码错误!");
            if (pwd != null) {
//                showSnackbar(loginSubmit,"说明",null);
                startActivity(new Intent(context,MainMapActivity.class));
            }
        }
    }





}
