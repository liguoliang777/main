package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * 游戏软件的实体类
 * Created by zeng on 2016/5/16.
 */
public class RecommendTopicsItemInfo implements Serializable {
    private int id;//该专题id
    private String title; //该专题名字
    private String selectDesc;//该专题  精选描述
    private String selectImage;//该专题图片
    private int appTypeId;//安卓
    private int isDelete;
    private int isCarousel;
    private int orderNo;
    private Object createTime;
    private Object updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelectDesc() {
        return selectDesc;
    }

    public void setSelectDesc(String selectDesc) {
        this.selectDesc = selectDesc;
    }

    public String getSelectImage() {
        return selectImage;
    }

    public void setSelectImage(String selectImage) {
        this.selectImage = selectImage;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public int getIsCarousel() {
        return isCarousel;
    }

    public void setIsCarousel(int isCarousel) {
        this.isCarousel = isCarousel;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
}
