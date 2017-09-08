package com.trust.shengyu.rentalcarclient.activitys.resetpassword;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.base.BaseActivity;
import com.trust.shengyu.rentalcarclient.tools.TrustTools;
import com.trust.shengyu.rentalcarclient.tools.beans.rentalcarbeans.ResultLoginBean;
import com.trust.shengyu.rentalcarclient.tools.gson.TrustAnalysis;

import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.TAG_URL_RETRIEVE_THE_PASSWORD;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.TAG_URL_VERIFICATION_CODE;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.URL_RETRIEVE_THE_PASSWORD;

public class ResetPasswordActivity extends BaseActivity {

    @BindView(R.id.reset_pwd_user)
    EditText resetPwdUser;
    @BindView(R.id.reset_pwd_pwd)
    EditText resetPwdPwd;
    @BindView(R.id.reset_pwd_auth_code)
    EditText resetPwdAuthCode;
    @BindView(R.id.reset_pwd_get_auth_code_btn)
    Button resetPwdGetAuthCodeBtn;
    @BindView(R.id.reset_pwd_submint)
    Button resetPwdSubmintBtn;
    @BindView(R.id.base_back)
    ImageView baseBack;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.reset_pwd_check_pwd)
    EditText resetPwdCheckPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        title.setText("忘记密码");
        baseSetOnClick(resetPwdGetAuthCodeBtn);
        baseSetOnClick(resetPwdSubmintBtn);
        baseBack.setVisibility(View.VISIBLE);
        baseSetOnClick(baseBack);
    }

    @Override
    public void baseClickResult(View v) {
        switch (v.getId()) {
            case R.id.reset_pwd_get_auth_code_btn:
                String authCodePhone = baseCheckIsNull(resetPwdUser, "电话不能为空!");
                requestCheckNum(authCodePhone);
                break;
            case R.id.reset_pwd_submint:
                String phone = baseCheckIsNull(resetPwdUser, "电话不能为空!");
                String pwd = baseCheckIsNull(resetPwdPwd, "密码不能为空!");
                String checkPwd = baseCheckIsNull(resetPwdCheckPwd,"确认密码不能为空!");
                int authCode = Integer.parseInt(baseCheckIsNull(resetPwdAuthCode, "验证码不能为空!"));

                if (null != phone && null != pwd && null != checkPwd &&  0 != authCode) {
                    if (pwd .equals(checkPwd)) {
                        Map<String, Object> map = new WeakHashMap<>();
                        map.put("cellPhone", phone);
                        map.put("password", pwd);
                        map.put("authCode", authCode);

                        requestCallBeack(URL_RETRIEVE_THE_PASSWORD, map, TAG_URL_RETRIEVE_THE_PASSWORD,
                                trustRequest.PUT, null);
                    }else{
                        showToast("密码不一致");
                    }
                }else{
                    showToast("输入有误!请确认后在点击!");
                }

                break;

            case R.id.base_back:
                finish();
                break;
        }
    }

    @Override
    public void successCallBeack(Object obj, int type) {
        String msg = (String) obj;
        switch (type) {
            case TAG_URL_VERIFICATION_CODE:
                new TrustTools<>().Countdown(resetPwdGetAuthCodeBtn, 60);
                break;
            case TAG_URL_RETRIEVE_THE_PASSWORD:
                ResultLoginBean resultLoginBean = TrustAnalysis.resultTrustBean(msg, ResultLoginBean.class);
                if (getResultStatus(resultLoginBean.getStatus(), msg)) {
                    showToast("修改成功!");
                    finish();
                }
                break;
        }
    }
}
