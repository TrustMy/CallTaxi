package com.trust.shengyu.rentalcar.tools.trustbaseinterface;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Trust on 2017/8/28.
 */

public class FunctionsManager<Result> {
    private static FunctionsManager functionsManager;

    public FunctionsManager() {
        mFunctonNoParamNotResult = new HashMap<>();
        mFunctonNoParamWithResult = new HashMap<>();
        mFunctonWithParamNotResult = new HashMap<>();
        mFunctonWithParamWithtResult = new HashMap<>();
    }

    public static FunctionsManager getFunctionsManager(){
        if (functionsManager == null) {
            functionsManager = new FunctionsManager();
        }
        return functionsManager;
    }

    private HashMap<String , FunctonNoParamNotResult> mFunctonNoParamNotResult;
    private HashMap<String , FunctonNoParamWithResult> mFunctonNoParamWithResult;
    private HashMap<String , FunctonWithParamNotResult> mFunctonWithParamNotResult;
    private HashMap<String , FunctonWithParamWithtResult> mFunctonWithParamWithtResult;


    //无参数无返回值

    public FunctionsManager addFunction(FunctonNoParamNotResult function){
        mFunctonNoParamNotResult.put(function.mFuntionName,function);
        return  this;
    }


    public void invokeFunction(String functionName){
        if(TextUtils.isEmpty(functionName)){
            return;
        }
        if (mFunctonNoParamNotResult != null) {
            FunctonNoParamNotResult f = mFunctonNoParamNotResult.get(functionName);
            if (f != null) {
                f.function();
            }else{
                try {
                    throw new FunctionException("没有这个接口:"+functionName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    //无参数有返回值


    public FunctionsManager addFunction(FunctonNoParamWithResult function){
        mFunctonNoParamWithResult.put(function.mFuntionName,function);
        return  this;
    }

    public Result invokeFunction(String functionName,Class<Result> c){
        if(TextUtils.isEmpty(functionName)){
            return null;
        }
        if (mFunctonNoParamWithResult != null) {
            FunctonNoParamWithResult f = mFunctonNoParamWithResult.get(functionName);
            if (f != null) {
                if (c != null) {
                    return c.cast(f.function());
                }else{
                    return (Result)f.function();
                }
            }else{
                try {
                    throw new FunctionException("没有这个接口:"+functionName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }

        return  null;
    }




    //有参数无返回值

    public FunctionsManager addFunction(FunctonWithParamNotResult function){
        mFunctonWithParamNotResult.put(function.mFuntionName,function);
        return  this;
    }


    public <Param> void invokeFunction(String functionName , Param data){
        if(TextUtils.isEmpty(functionName)){
            return;
        }
        if (mFunctonWithParamNotResult != null) {
            FunctonWithParamNotResult f = mFunctonWithParamNotResult.get(functionName);
            if (f != null) {
                f.function(data);
            }else{
                try {
                    throw new FunctionException("没有这个接口:"+functionName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //有参数有返回值
    public FunctionsManager addFunction(FunctonWithParamWithtResult function){
        mFunctonWithParamWithtResult.put(function.mFuntionName,function);
        return  this;
    }

    public <Result,Param>Result invokeFunction(String functionName,Class<Result> c,Param data){
        if(TextUtils.isEmpty(functionName)){
            return null;
        }
        if (mFunctonWithParamWithtResult != null) {
            FunctonWithParamWithtResult f = mFunctonWithParamWithtResult.get(functionName);
            if (f != null) {
                if (c != null) {
                    return c.cast(f.function(data));
                }else{
                    return (Result)f.function(data);
                }
            }else{
                try {
                    throw new FunctionException("没有这个接口:"+functionName);
                } catch (FunctionException e) {
                    e.printStackTrace();
                }
            }
        }

        return  null;
    }


}
