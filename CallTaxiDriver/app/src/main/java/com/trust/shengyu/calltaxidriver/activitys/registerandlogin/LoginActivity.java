package com.trust.shengyu.calltaxidriver.activitys.registerandlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trust.shengyu.calltaxidriver.Config;
import com.trust.shengyu.calltaxidriver.R;
import com.trust.shengyu.calltaxidriver.activitys.AndroidPermissionTool;
import com.trust.shengyu.calltaxidriver.activitys.MainActivity;
import com.trust.shengyu.calltaxidriver.base.BaseActivity;
import com.trust.shengyu.calltaxidriver.tools.L;
import com.trust.shengyu.calltaxidriver.tools.beans.DriverInformation;

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
//        callTaxiCommHelper.publish("trust",1,"发送msg");
        /*
        callTaxiCommHelper.unbundledTopics("trust");
        callTaxiCommHelper.publish("trust",1,"解绑后msg");
        */

//        trustDialog.showWaitDialog(this);

        AndroidPermissionTool tool = new AndroidPermissionTool();
        tool.checkPermission(this);
    }




    private void initDate() {

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
                    Config.driverPhone = user;
                    String pwd = baseCheckIsNull(loginPwd, "密码错误!");
                    if (pwd != null) {
//                showSnackbar(loginSubmit,"说明",null);

//                        finish();
                        Map<String,Object> map = new WeakHashMap<>();
                        map.put("userName",user);
                        map.put("password",pwd);
                        map.put("userType", Config.UserTypeDriver);

                        requestCallBeack(Config.DRIVER_GET_TOKEN,map,Config.TAG_DRIVER_GET_TOKEN,
                                trustRequest.POST,null);
                    }
                }
                break;
            case R.id.login_forgetpassword:

                break;
            case R.id.login_registered:
                intent.setClass(context,RegisteredActivity.class);
                startActivity(intent);
                break;
        }



    }

    @Override
    public void successCallBeack(Object obj, int type) {
        String msg = (String) obj;
        switch (type) {
            case Config.TAG_DRIVER_GET_TOKEN:
                requestCallBeack(Config.DRIVER_INFORMATION,null,Config.TAG_DRIVER_INFORMATION,
                        trustRequest.GET,Config.token);
                break;
            case Config.TAG_DRIVER_INFORMATION:
                DriverInformation bean = gson.fromJson(msg,DriverInformation.class);
                if(getResultStatus(bean.getStatus(),msg)){
                    Config.driver = bean.getContent().getDriverId();
                    L.d(" Config.driver:"+ Config.driver);
                    initServer();

                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                }
                break;
        }
    }
}
