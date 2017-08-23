package com.trust.shengyu.calltaxi.activitys.registerandlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.R;
import com.trust.shengyu.calltaxi.activitys.mainmap.MainMapActivity;
import com.trust.shengyu.calltaxi.activitys.resetpassword.ResetPasswordActivity;
import com.trust.shengyu.calltaxi.base.BaseActivity;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.beans.UserBean;

import org.json.JSONObject;

import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_forgetpassword)
    TextView loginForgetpassword;
    @BindView(R.id.login_registered)
    LinearLayout loginRegistered;
    @BindView(R.id.login_user)
    EditText loginUser;
    @BindView(R.id.login_pwd)
    EditText loginPwd;
    @BindView(R.id.login_submit)
    Button loginSubmit;


    private Context context = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initDate();
        Map<String,Object> map = new WeakHashMap<>();
        map.put("version","1.0.1");
        map.put("url","www.baidu.com");
        map.put("description","有新版本,请尽快升级!");
        L.d(new JSONObject(map).toString());
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
        switch (v.getId()){
            case R.id.login_submit:

                String user = baseCheckIsNull(loginUser, "账号错误!");
                if (user != null) {
                    String pwd = baseCheckIsNull(loginPwd, "密码错误!");
                    if (pwd != null) {
//                showSnackbar(loginSubmit,"说明",null);
                        Config.userPhone = user;
                        Map<String,Object> map = new WeakHashMap<>();
                        map.put("userName",user);
                        map.put("password",pwd);
                        map.put("userType",Config.UserTypeClient);
                        requestCallBeack(Config.CLIENT_LOGIN,map,Config.TAG_CLIENT_LOGIN,trustRequest.POST,null);

                    }
                }
                break;
            case R.id.login_registered:
                intent.setClass(context,RegisteredActivity.class);
                startActivity(intent);
                break;

            case R.id.login_forgetpassword:
                intent.setClass(context,ResetPasswordActivity.class);
                startActivity(intent);
                break;
        }



//        requestCallBeack("https://www.baidu.com/s?wd=%E9%AB%98%E5%BE%B7%E5%9C%B0%E5%9B%BE&rsv_spt=1&rsv_iqid=0xc8842b5800001ad0&issp=1&f=8&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_sug3=12&rsv_sug1=7&rsv_sug7=101&rsv_sug2=0&inputT=1576&rsv_sug4=3039&rsv_sug=1",
//                null ,1,trustRequest.GET,trustRequest.HeaderNull,trustRequest.TokenNull);

//        requestCallBeack("http://180.168.194.98:7888/CAWebserver-0.1/registry/reg",map,2,trustRequest.POST,trustRequest.HeaderUrlencoded,trustRequest.TokenNull);
    }


    @Override
    public void successCallBeack(Object obj, int type) {
        String msg = (String) obj;
        switch (type) {
            case Config.TAG_CLIENT_LOGIN:
                requestCallBeack(Config.CLIENT_INFORMATION,null,Config.TAG_CLIENT_INFORMATION,
                        trustRequest.GET,Config.token);
                break;

            case Config.TAG_CLIENT_INFORMATION:
                UserBean bean = gson.fromJson(msg,UserBean.class);
                if(getResultStatus(bean.getStatus(),msg)){
                    startActivity(new Intent(LoginActivity.this,MainMapActivity.class));
                }
                break;
        }

    }
}
