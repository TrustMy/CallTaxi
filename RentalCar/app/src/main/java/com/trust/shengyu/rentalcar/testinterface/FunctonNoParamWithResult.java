package com.trust.shengyu.rentalcar.testinterface;

/**
 * Created by Trust on 2017/8/28.
 */

public abstract class FunctonNoParamWithResult<Result> extends Function {
    public FunctonNoParamWithResult(String mFuntionName) {
        super(mFuntionName);
    }

    public abstract Result function();
}
