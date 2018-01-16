package cn.ngame.store.bean;

/**
 * Created by Administrator on 2016/12/31 0031.
 */

public class UpLoadBean {


    /**
     * code : -2
     * data : null
     * innerResult : null
     * map : null
     * msg : token信息已过期
     */

    private int code;
    private String data;
    private Object innerResult;
    private Object map;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
