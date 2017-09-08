package com.trust.shengyu.rentalcarclient.activitys.registerandlogin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trust.shengyu.rentalcarclient.Config;
import com.trust.shengyu.rentalcarclient.R;
import com.trust.shengyu.rentalcarclient.activitys.MainControllActivity;
import com.trust.shengyu.rentalcarclient.activitys.checkinfo.UserMessageActivity;
import com.trust.shengyu.rentalcarclient.activitys.resetpassword.ResetPasswordActivity;
import com.trust.shengyu.rentalcarclient.base.BaseActivity;
import com.trust.shengyu.rentalcarclient.tools.L;
import com.trust.shengyu.rentalcarclient.tools.TrustTools;
import com.trust.shengyu.rentalcarclient.tools.beans.oldbeans.UpdataApp;
import com.trust.shengyu.rentalcarclient.tools.beans.oldbeans.UpdataAppData;
import com.trust.shengyu.rentalcarclient.tools.beans.oldbeans.UserBean;
import com.trust.shengyu.rentalcarclient.tools.beans.rentalcarbeans.ResultLoginBean;
import com.trust.shengyu.rentalcarclient.tools.beans.rentalcarbeans.ResultUserInfoBean;
import com.trust.shengyu.rentalcarclient.tools.gson.TrustAnalysis;
import com.trust.shengyu.rentalcarclient.tools.request.TrustRequest;
import com.trust.shengyu.rentalcarclient.tools.updateapp.CheckVersionTask;
import com.trust.shengyu.rentalcarclient.tools.updateapp.UpdataInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.TAG_URL_GET_USER_COUPON;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.TAG_URL_GET_USER_INFO;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.TAG_URL_LOGIN;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.URL_GET_USER_COUPON;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.URL_GET_USER_INFO;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.URL_LOGIN;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.USER_STATUS_CERTIFIED_ERROR;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.USER_STATUS_CERTIFIED_ING;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.USER_STATUS_CERTIFIED_SUCCESS;
import static com.trust.shengyu.rentalcarclient.RentalcarClientConfig.USER_STATUS_NOT_CERTIFIED;

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

    private String user;
    private Context context = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initDate();
    }

    private void initDate() {
//        WifiManager wifiManager  = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        List<android.net.wifi.ScanResult> list =  wifiManager.getScanResults();
//        for (int i = 0; i < list.size(); i++) {
//            Log.d("lhh", "initDate:  wifi:BSSID"+list.get(i).BSSID+"|SSID:"+list.get(i).SSID);
//        }
        TrustRequest s = new TrustRequest(new TrustRequest.onResultCallBack() {
            @Override
            public void CallBack(int code, int status, final Object object) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String updateMsg = (String) object;
                        UpdataApp updataApp = new UpdataApp();
                        UpdataAppData contentData = new UpdataAppData();

                        try {
                            JSONObject jsonObject = new JSONObject(updateMsg);
                            updataApp.setStatus(jsonObject.getInt("status"));
                            updataApp.setInfo(jsonObject.getString("info"));
                            String content = jsonObject.getString("content");

                            JSONObject contentJson = new JSONObject(content);
                            contentData.setVersionNo(contentJson.getString("versionNo"));
                            contentData.setDescription(contentJson.getString("description"));
                            contentData.setUrl(contentJson.getString("url"));
                            updataApp.setContent(contentData);
                            Thread thread = new Thread(new CheckVersionTask(context, TrustTools.resultAppVersion(context),
                                     new UpdataInfo(contentData.getVersionNo(),contentData.getUrl(),contentData.getDescription())));
                            thread.start();

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
//
//
                    }
                });
            }
        },"http://192.168.1.123:8082");
        Map<String,Object> sssss =new WeakHashMap<>();
        sssss.put("appType",1);
        s.Request("/rest/appVersion/version",sssss,Config.TAG_UPDATA_APP,trustRequest.GET,trustRequest.HeaderNull,null);

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

                user = baseCheckIsNull(loginUser, "账号错误!");
                if (user != null) {
                    String pwd = baseCheckIsNull(loginPwd, "密码错误!");
                    if (pwd != null) {
//                showSnackbar(loginSubmit,"说明",null);
                        Config.userPhone = user;
                        String version  =TrustTools.resultAppVersion(this);
                        Map<String,Object> map = new WeakHashMap<>();
                        map.put("userName",user);
                        map.put("password",pwd);
                        map.put("userType",Config.UserTypeClient);
                        map.put("appVersion",version!=null?version:"1.0.0");
                        requestCallBeack(URL_LOGIN,map,TAG_URL_LOGIN,trustRequest.POST,null);

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
//            case Config.TAG_CLIENT_LOGIN:
//                requestCallBeack(Config.CLIENT_INFORMATION,null,Config.TAG_CLIENT_INFORMATION,
//                        trustRequest.GET,Config.token);
//                break;
//
//            case Config.TAG_CLIENT_INFORMATION:
//                UserBean bean = gson.fromJson(msg,UserBean.class);
//                if(getResultStatus(bean.getStatus(),msg)){
//                    startActivity(new Intent(LoginActivity.this,UserMessageActivity.class));
//                }
//                break;
            case TAG_URL_LOGIN:
                if (TrustTools.checkIsJson(msg)) {
                    ResultLoginBean loginBean = TrustAnalysis.resultTrustBean(msg,ResultLoginBean.class);
                    if (getResultStatus(loginBean.getStatus(),msg)) {

                        Config.PHONE = user;
//                    startActivity(new Intent(LoginActivity.this,UserMessageActivity.class));
                        Map<String,Object> map = new WeakHashMap<>();
                        map.put("phone",user);
                        requestCallBeack(URL_GET_USER_INFO,map,TAG_URL_GET_USER_INFO,trustRequest.GET_NO_PARAMETER_Name,
                                Config.token);
                    }
                }else{
                    showToast("服务器返回的数据不是json:"+msg);
                }

                break;
            case TAG_URL_GET_USER_INFO:

                ResultUserInfoBean resultUserInfoBean = TrustAnalysis.resultTrustBean(msg,ResultUserInfoBean.class);
                if (getResultStatus(resultUserInfoBean.getStatus(),msg)) {
                    checkUserStatus(resultUserInfoBean);

                    L.d("当前 信息getStatus:"+resultUserInfoBean.getContent().getStatus());
                    Map<String,Object> map = new WeakHashMap<>();
                    map.put("phone",user);
                    requestCallBeack(URL_GET_USER_COUPON,map,TAG_URL_GET_USER_COUPON,trustRequest.GET_NO_PARAMETER_Name,
                            Config.token);
                }

                break;

            case TAG_URL_GET_USER_COUPON:
                L.d("优惠劵信息:"+msg);

                break;
        }
    }

    private void checkUserStatus( ResultUserInfoBean resultUserInfoBean) {
        Intent intent = new Intent();
        switch (resultUserInfoBean.getContent().getStatus()) {
            case USER_STATUS_NOT_CERTIFIED://没有认证
                intent.putExtra("userStatus",USER_STATUS_NOT_CERTIFIED);
                intent.setClass(context,UserMessageActivity.class);
                break;
            case USER_STATUS_CERTIFIED_ING://认证中
                intent.putExtra("userStatus",USER_STATUS_CERTIFIED_ING);
                intent.setClass(context,MainControllActivity.class);
                break;
            case USER_STATUS_CERTIFIED_SUCCESS://认证通过
                intent.putExtra("userStatus",USER_STATUS_CERTIFIED_SUCCESS);
                intent.setClass(context,MainControllActivity.class);
                break;
            case USER_STATUS_CERTIFIED_ERROR://认证失败
                intent.putExtra("userStatus",USER_STATUS_CERTIFIED_ERROR);
                intent.putExtra("errorInfo",resultUserInfoBean.getContent().getDescription());
                intent.setClass(context,UserMessageActivity.class);
                break;
        }
        startActivity(intent);
    }
}
