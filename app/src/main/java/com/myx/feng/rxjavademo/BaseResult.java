package com.myx.feng.rxjavademo;

import java.io.Serializable;

/**
 * Created by mayuxin on 2017/3/30.
 */

public class BaseResult implements Serializable{
    private static final long serialVersionUID = 7174487347407677455L;
    private  Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


}
