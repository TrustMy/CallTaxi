package com.trust.shengyu.calltaxidriver.tools.request;



import com.trust.shengyu.calltaxidriver.Config;
import com.trust.shengyu.calltaxidriver.tools.L;
import com.trust.shengyu.calltaxidriver.tools.request.ssl.TrustAllCerts;

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
    public  int GET = 0x0001,POST = 0x0002,PUT = 0x0003;
    public  final int HeaderNull = 0;
    public  final String TokenNull = null;
    public  final boolean addHeader = true,noAdd = false,addToken = true;
    public  final int HeaderJson = 0x00001,HeaderUrlencoded = 0x00002;
    private OkHttpClient okHttpClient ;
    private Request.Builder builder;
    private MediaType mediaType;
    private String serverUrl;
    public interface onResultCallBack{
        void CallBack(int code, int status, Object object);
    }

    public onResultCallBack resultCallBack;

    public TrustRequest(onResultCallBack resultCallBack , String serverUrl) {
        this.okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(TrustAllCerts.createSSLSocketFactory(),new TrustAllCerts())
                .hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier())
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
        builder = new Request.Builder();
        this.resultCallBack = resultCallBack;
        this.serverUrl = serverUrl;
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
        String urls = serverUrl+url;
        Request request = null;

        switch (requestHeader){
            case HeaderJson:
                if (map != null) {
                    msg = new JSONObject(map).toString();
                }
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

        if (msg != null) {

        }
            if(requestType == GET){
                if (token != null) {
                    builder.addHeader("Token", token);
                }
                if(map != null){
                    StringBuffer sb = null;
                    for (Map.Entry<String, Object> entry : map.entrySet()){
                        if(sb == null){
                            sb = new StringBuffer();
                            if (entry.getValue() instanceof String){
                                sb.append("?"+entry.getKey()+"= \""+entry.getValue()+"\"");
                            }else{
                                sb.append("?"+entry.getKey()+"="+entry.getValue());
                            }

                        }else{
                            if (entry.getValue() instanceof String){
                                sb.append("&"+entry.getKey()+"= \""+entry.getValue()+"\"");
                            }else{
                                sb.append("&"+entry.getKey()+"="+entry.getValue());
                            }

                        }
                    }
                    builder.addHeader("Token",token);

                    request =  builder.get().url(urls+ sb.toString()).build();
                }else{

                    request =  builder.get().url(urls).build();
                }
            }else {
                if (msg != null) {
                    if (requestHeader != HeaderNull){
                        request =  returnRequest(urls,requestType, requestHeader,msg,token);
                    }else{
                        FormBody.Builder builder = new FormBody.Builder();
                        for (Map.Entry<String, Object> entry : map.entrySet()){
                            builder.add(entry.getKey(),entry.getValue()+"");
                        }
                        FormBody body = builder.build();
                        request  = new Request.Builder().url(urls).post(body).build();
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

     * @param requestHeader
     * @param msg
     * @return
     */

    private Request returnRequest(String url, int requestType,int requestHeader ,String msg ,String token) {
        Request request = null;
        RequestBody body = null;
        builder = new Request.Builder();
        body = returnBody(requestHeader,msg);

        if (body != null) {
            builder.url(url);
            if(token!= null){
               builder.addHeader("Token", token);
            }
            if (requestType == POST){
                builder.post(body);
            }else if (requestType == PUT){
                builder.put(body);
            }

            request = builder.build();
        }
        return request;
    }

    /**
     * 为body 添加header
     * @param requestHeader
     * @param requestMessage
     * @return
     */
    private RequestBody returnBody(int requestHeader,String requestMessage){
        RequestBody body = null;
        switch (requestHeader){
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
                resultCallBack.CallBack(type, Config.ERROR,"onFailure:"+e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.d("onResponse:"+response.toString());
                if(response.code() == 200){
                    if(type == Config.TAG_DRIVER_GET_TOKEN){
                        Config.token = response.header("Token");
                    }

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
