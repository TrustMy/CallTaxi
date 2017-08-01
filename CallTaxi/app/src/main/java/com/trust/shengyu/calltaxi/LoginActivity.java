package com.trust.shengyu.calltaxi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                showSnackbar(loginSubmit,"说明","正在登录");
                startActivity(new Intent(context,MainActivity.class));
            }
        }
    }

}
