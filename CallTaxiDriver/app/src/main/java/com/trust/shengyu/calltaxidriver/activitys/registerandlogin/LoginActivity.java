package com.trust.shengyu.calltaxidriver.activitys.registerandlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trust.shengyu.calltaxidriver.R;
import com.trust.shengyu.calltaxidriver.activitys.MainActivity;
import com.trust.shengyu.calltaxidriver.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_forgetpassword)
    TextView loginForgetpassword;
    @BindView(R.id.login_registered)
    LinearLayout loginRegistered;
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
//        callTaxiCommHelper.publish("trust",1,"发送msg");
        /*
        callTaxiCommHelper.unbundledTopics("trust");
        callTaxiCommHelper.publish("trust",1,"解绑后msg");
        */

//        trustDialog.showWaitDialog(this);
    }


    private void initDate() {
//        WifiManager wifiManager  = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        List<android.net.wifi.ScanResult> list =  wifiManager.getScanResults();
//        for (int i = 0; i < list.size(); i++) {
//            Log.d("lhh", "initDate:  wifi:BSSID"+list.get(i).BSSID+"|SSID:"+list.get(i).SSID);
//        }
    }


    private void initView() {
        baseSetOnClick(loginSubmit);
        baseSetOnClick(loginForgetpassword);
        baseSetOnClick(loginRegistered);
    }

    @Override
    public void baseClickResult(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.login_submit:
                String user = baseCheckIsNull(loginUser, "账号错误!");
                if (user != null) {
                    String pwd = baseCheckIsNull(loginPwd, "密码错误!");
                    if (pwd != null) {
//                showSnackbar(loginSubmit,"说明",null);
                        intent.setClass(context, MainActivity.class);
//                        finish();
                    }
                }
                break;
            case R.id.login_forgetpassword:

                break;
            case R.id.login_registered:
                intent.setClass(context,RegisteredActivity.class);
                break;
        }
        startActivity(intent);


    }


}
