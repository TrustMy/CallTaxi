package com.trust.shengyu.calltaxi.tools.request;

import com.trust.shengyu.calltaxi.Config;
import com.trust.shengyu.calltaxi.tools.L;
import com.trust.shengyu.calltaxi.tools.request.ssl.TrustAllCerts;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Trust on 2017/8/7.
 */

public class TrustRequest {
    public  int GET = 0x0001,POST = 0x0002;
    public  final int HeaderNull = 0;
    public  final String TokenNull = null;
    public  final boolean addHeader = true,noAdd = false,addToken = true;
    public  final int HeaderJson = 0x00001,HeaderUrlencoded = 0x00002;
    private OkHttpClient okHttpClient ;
    private Request.Builder builder;
    private MediaType mediaType;
    public interface onResultCallBack{
        void CallBack(int code , int status , Object object);
    }

    public onResultCallBack resultCallBack;

    public TrustRequest(onResultCallBack resultCallBack) {
        this.okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(TrustAllCerts.createSSLSocketFactory(),new TrustAllCerts())
                .hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        builder = new Request.Builder();
        this.resultCallBack = resultCallBack;
    }

    /**
     * 网络请求  并拆分
     * @param url
     * @param map  数据
     * @param requestCode  请求tag
     * @param requestType  请求类型
     * @param requestHeader 如果添加请求头 设置请求头种类 如果没有HeaderNull
     * @param token 如果添加token 设置token 如何没有TokenNull
     */
    public void Request(String url , Map<String , Object> map , int requestCode,int requestType,int requestHeader ,String token){
        String msg = null;
        Request request = null;

        switch (requestHeader){
            case HeaderJson:
                msg = new JSONObject(map).toString();
                break;
            case HeaderUrlencoded:
                StringBuffer sb = null;
                for (Map.Entry<String, Object> entry : map.entrySet()){
                    if(sb == null){
                        sb = new StringBuffer();
                        sb.append(entry.getKey()+"="+entry.getValue());
                    }else{
                        sb.append("&"+entry.getKey()+"="+entry.getValue());
                    }
                }
                msg = sb.toString();
                break;
        }

            if(requestType == GET){
                request =  builder.get().url(url).build();
            }else{
                if (msg != null) {
                    if (requestHeader != HeaderNull){
                        request =  returnRequest(url, requestType,msg,token);
                    }else{
                        FormBody.Builder builder = new FormBody.Builder();
                        for (Map.Entry<String, Object> entry : map.entrySet()){
                            builder.add(entry.getKey(),entry.getValue()+"");
                        }
                        FormBody body = builder.build();
                        request  = new Request.Builder().url(url).post(body).build();
                    }
                }
            }
            if (request != null) {
                executeResponse(request,requestCode);
            }

    }

    /**
     * 判断添加header 后判断是否添加token
     * @param url

     * @param requestType
     * @param msg
     * @return
     */

    private Request returnRequest(String url, int requestType ,String msg ,String token) {
        Request request = null;
        RequestBody body = null;

        body = returnBody(requestType,msg);

        if (body != null) {
            if(token!= null){
                request  = builder.url(url).addHeader("Token", token).post(body)
                        .build();
            }else{
                request  = builder.url(url).post(body)
                        .build();
            }
        }
        return request;
    }

    /**
     * 为body 添加header
     * @param requestType
     * @param requestMessage
     * @return
     */
    private RequestBody returnBody(int requestType,String requestMessage){
        RequestBody body = null;
        switch (requestType){
            case HeaderJson:
                mediaType = MediaType.parse("application/json");
                break;
            case HeaderUrlencoded:
                mediaType = MediaType.parse("application/x-www-form-urlencoded");
                break;
        }
        body = RequestBody.create(mediaType, requestMessage);
        return body;
    }


    public void setOnResultCallBack(onResultCallBack onResultCallBack){
        this.resultCallBack = onResultCallBack;
    }


    /**
     * 请求回调
     * @param request
     * @param type
     */
    public void executeResponse(final Request request , final int type) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                resultCallBack.CallBack(type, Config.SUCCESS,"onFailure:"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.d("onResponse:"+response.toString());
                if(response.code() == 200){
                    String json = response.body().string();
                    L.d("json:"+json);
                    if(json != null && !json.equals("")){
                        resultCallBack.CallBack(type, Config.SUCCESS,json);
//                        ResultBean bean = gson.fromJson(json, ResultBean.class);
//                        if(bean!=null && bean.getStatus()){
////                            sendMessage(bean, CaConfig.SUCCESS,type);
//                        }else{
//                            if(bean == null){
////                                sendMessage(error,CaConfig.ERROR,type);
//                            }else{
////                                ErrorResultBean errorResultBean = gson.fromJson(json,ErrorResultBean.class);
////                                if(errorResultBean !=null){
//////                                    sendMessage(errorResultBean.getMessage(),CaConfig.ERROR,type);
////                                }else{
//////                                    sendMessage(error,CaConfig.ERROR,type);
////                                }
//                            }
//                        }
                    }else{
//                        sendMessage(error,CaConfig.ERROR,type);
                    }
                }else{
                    resultCallBack.CallBack(type, Config.ERROR,response.code());
//                    sendMessage("错误code:"+response.code(),CaConfig.ERROR,type);
                }
            }
        });
    }


}
