package com.trust.shengyu.rentalcar.tools.trustbaseinterface;

/**
 * Created by Trust on 2017/8/28.
 */

public abstract class FunctonWithParamWithtResult<Param , Result> extends Function {
    public FunctonWithParamWithtResult(String mFuntionName) {
        super(mFuntionName);
    }

    public abstract Result function(Param param);
}
