package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * 图片的详细信息
 * Created by zeng on 2016/6/3.
 */
public class GameImage implements Serializable {

    public long id;
    public String imageDesc;
    public String imageLink;
    public long gameId;
    public long orderNo;
    public byte type;

}
