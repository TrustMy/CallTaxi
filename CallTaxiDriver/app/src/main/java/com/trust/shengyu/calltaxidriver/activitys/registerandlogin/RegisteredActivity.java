package com.trust.shengyu.calltaxidriver.activitys.registerandlogin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.trust.shengyu.calltaxidriver.R;
import com.trust.shengyu.calltaxidriver.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisteredActivity extends BaseActivity {
    @BindView(R.id.registerex_user)
    EditText registerexUser;
    @BindView(R.id.registere_verification_code)
    EditText registereVerificationCode;
    @BindView(R.id.registere_verification_code_btn)
    Button registereVerificationCodeBtn;
    @BindView(R.id.registere_pwd)
    EditText registerePwd;
    @BindView(R.id.registere_submit)
    Button registereSubmit;
    @BindView(R.id.registered_login)
    LinearLayout registeredLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        baseSetOnClick(registereSubmit);
        baseSetOnClick(registereVerificationCodeBtn);
        baseSetOnClick(registeredLogin);

    }

    @Override
    public void baseClickResult(View v) {
        switch (v.getId()) {
            case R.id.registere_submit:

                break;
            case R.id.registere_verification_code_btn:
//                trustTools.Countdown(registereVerificationCodeBtn, 60);
                break;
            case R.id.registered_login:
                finish();
                break;
        }
    }
}
