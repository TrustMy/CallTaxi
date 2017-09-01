package com.trust.shengyu.rentalcar.tools.trustbaseinterface;

/**
 * Created by Trust on 2017/8/28.
 */

public abstract class FunctonWithParamNotResult <Param> extends Function {
    public FunctonWithParamNotResult(String mFuntionName) {
        super(mFuntionName);
    }

    public abstract void function(Param param);
}
