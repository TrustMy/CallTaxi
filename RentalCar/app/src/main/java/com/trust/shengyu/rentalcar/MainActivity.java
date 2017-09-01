package com.trust.shengyu.rentalcar;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.tencent.bugly.crashreport.CrashReport;
import com.trust.shengyu.rentalcar.tools.L;
import com.trust.shengyu.rentalcar.tools.gsons.*;
import com.trust.shengyu.rentalcar.tools.trustbaseinterface.FunctionsManager;
import com.trust.shengyu.rentalcar.tools.trustbaseinterface.FunctonNoParamNotResult;
import com.trust.shengyu.rentalcar.tools.trustbaseinterface.FunctonNoParamWithResult;
import com.trust.shengyu.rentalcar.tools.trustbaseinterface.FunctonWithParamNotResult;
import com.trust.shengyu.rentalcar.tools.trustbaseinterface.FunctonWithParamWithtResult;

public class MainActivity extends BaseActivity {
    private TextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       FunctionsManager functionsManager = FunctionsManager.getFunctionsManager();
        functionsManager.addFunction(new FunctonNoParamNotResult(NO_PARAM_NO_RESULT) {
            @Override
            public void function() {
                Log.d("lhh", "function: 没有参数没有返回值回调");
            }
        });
        functionsManager.invokeFunction(NO_PARAM_NO_RESULT);

        //有返回值

        functionsManager.addFunction(new FunctonNoParamWithResult(NO_PARAM_WITH_RESULT) {
            @Override
            public Object function() {
                return "牛肉";
            }
        });
        String msg = (String) functionsManager.invokeFunction(NO_PARAM_WITH_RESULT,String.class);
        Log.d("lhh", "function: 有返回值回调:"+msg);


        //有参数误返回值
        functionsManager.addFunction(new FunctonWithParamNotResult<String>(WITH_PARAM_NO_RESULT) {
            @Override
            public void function(String o) {
                Log.d("lhh", "function: 有参数,无返回值回调:"+o);
            }
        }).invokeFunction(WITH_PARAM_NO_RESULT,"鸡肉");


        //有参数有返回值
        functionsManager.addFunction(new FunctonWithParamWithtResult<Integer,String>(WITH_PARAM_WITH_RESULT) {
            @Override
            public String function(Integer integer) {
                return "今天是:"+integer+"号";
            }
        });
        String message = (String) functionsManager.invokeFunction(WITH_PARAM_WITH_RESULT,String.class,28);
        Log.d("lhh", "function: 有参数有返回值回调:"+message);




        CrashReport.initCrashReport(getApplicationContext());

        DataBeaas f =  TrustAnalysis.resultTrustBean(json,DataBeaas.class);
        Log.d("lhh", "解析的json :"+f.getData().get(0).getSex());




    }
    private String json ="{\"status\":\"ok\",\"message\":\"登陆成功\",\"data\":[{\"id\":\"1\",\"age\":\"15\",\"sex\":\"女\"},{\"id\":\"1\",\"age\":\"15\",\"sex\":\"男\"}]}";
    private String CommonJson ="{\"status\":\"ok\",\"message\":\"登陆成功\",\"data\":\"\"}";

    public String NO_PARAM_NO_RESULT = "NO_PARAM_NO_RESULT";
    public String NO_PARAM_WITH_RESULT = "NO_PARAM_WITH_RESULT";
    public String WITH_PARAM_NO_RESULT = "WITH_PARAM_NO_RESULT";
    public String WITH_PARAM_WITH_RESULT = "WITH_PARAM_WITH_RESULT";

    @Override
    protected void PermissionError() {
        L.d("PermissionError");
        super.PermissionError();
    }

    @Override
    protected void PermissionSuccess() {
        L.d("PermissionSuccess");
        super.PermissionSuccess();
    }
}
