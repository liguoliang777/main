package com.ngame.api.model;

/**
 * Created by Administrator on 2017/12/5.
 */

public class Response<T> {
    private T data;
    private Meta meta;
    private Pagination pagination;

    public Response(){super();}
    public T getData(){return data;}
    public Meta getMeta(){return meta;}
    public Pagination getPagination(){return pagination;}
    public void setData(T d){data = d;}
    public void setMeta(Meta m){meta = m;}
    public void setPagination(Pagination p){pagination = p;}
}
