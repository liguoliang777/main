package com.jzt.hol.android.jkda.sdk.bean.gamehub;

/**
 * Created by gp on 2017/3/4 0004.
 */

public class NormalDataBean {


    /**
     * code : -2
     * data : null
     * innerResult : null
     * map : null
     * msg : token信息已过期
     */

    private int code;
    private Object data;
    private Object innerResult;
    private Object map;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getInnerResult() {
        return innerResult;
    }

    public void setInnerResult(Object innerResult) {
        this.innerResult = innerResult;
    }

    public Object getMap() {
        return map;
    }

    public void setMap(Object map) {
        this.map = map;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
