package com.ngame.api.model;

/**
 * Created by Administrator on 2017/12/5.
 */

public class Meta {
    private String code;
    private String message;

    public Meta(){super();}
    public String getCode(){return code;}
    public String getMessage(){return message;}
    public void getCode(String s){code = s;}
    public void setMessage(String s){message = s;}
}
