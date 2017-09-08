package com.trust.shengyu.rentalcarclient.activitys.registerandlogin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.base.BaseActivity;
import com.trust.shengyu.rentalcarclient.tools.L;
import com.trust.shengyu.rentalcarclient.tools.TrustTools;
import com.trust.shengyu.rentalcarclient.tools.beans.rentalcarbeans.ResultLoginBean;
import com.trust.shengyu.rentalcarclient.tools.gson.TrustAnalysis;

import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.TAG_URL_REGISTER;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.TAG_URL_VERIFICATION_CODE;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.URL_REGISTER;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.URL_VERIFICATION_CODE;

public class RegisteredActivity extends BaseActivity {
    @BindView(R.id.registerex_user)
    EditText registerexUser;
    @BindView(R.id.registere_verification_code)
    EditText registereVerificationCode;

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
        registereVerificationCodeBtn = (Button) findViewById(R.id.registere_verification_code_btn);
        baseSetOnClick(registereSubmit);
        baseSetOnClick(registereVerificationCodeBtn);
        baseSetOnClick(registeredLogin);

    }

    @Override
    public void baseClickResult(View v) {
        switch (v.getId()) {
            case R.id.registere_submit:
                String phone = baseCheckIsNull(registerexUser,"电话号码不能为空!");
                String pwd = baseCheckIsNull(registerePwd,"密码不能为空!");
                int verificationCode = Integer.parseInt(baseCheckIsNull(registereVerificationCode,"验证码不能为空!"));
                if (null != phone && null != pwd && 0 != verificationCode) {
                    Map<String,Object> map = new WeakHashMap<>();
                    map.put("cellPhone",phone);
                    map.put("password",pwd);
                    map.put("authCode",verificationCode);
                    requestCallBeack(URL_REGISTER,map,TAG_URL_REGISTER,trustRequest.POST,null);
                }
                break;
            case R.id.registere_verification_code_btn:
                String  verificationCodePhone = baseCheckIsNull(registerexUser,"电话号码不能为空!");
                if (verificationCodePhone != null) {
                    requestCheckNum(verificationCodePhone);
//                    trustTools.Countdown(registereVerificationCodeBtn, 60);

                }

                break;
            case R.id.registered_login:
                finish();
                break;
        }
    }


    @Override
    public void successCallBeack(Object obj, int type) {
        String msg = (String) obj;
        switch (type){
            case TAG_URL_VERIFICATION_CODE:
                L.d("这是验证码返回:"+msg);
                trustTools.Countdown(registereVerificationCodeBtn, 60);
                break;

            case TAG_URL_REGISTER:
                L.d("登录返回:"+msg);
                ResultLoginBean resultLoginBean = TrustAnalysis.resultTrustBean(msg,ResultLoginBean.class);
                if (getResultStatus(resultLoginBean.getStatus(),msg)) {
                    showToast("注册成功!");
                    finish();
                }
                break;
        }
    }
}
