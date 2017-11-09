package com.myx.feng.rxjavademo;

import java.io.Serializable;

/**
 * Created by mayuxin on 2017/3/31.
 */

public class Result implements Serializable {
    private static final long serialVersionUID = -5300972933429130203L;
    /**
     * <0 数据返回异常
     * >=0 数据返回正常
     * =0 数据返回正常 但是为空数据
     * >0 数据返回正常且有数据
     */
    private int errorCode;//
    private String msg;
    private String source;// file web

    public Result(){
        source="web";
    }
    public String getSource() {
        return source;
    }

    public void  setSource(String source) {
        this.source = source;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Result{" +
                "errorCode=" + errorCode +
                ", msg='" + msg + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
