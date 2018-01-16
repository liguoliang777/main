package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * @author flan
 * @since 2016/5/9
 */
public class LogoInfo implements Serializable{

    public long id;
    public String gameLogoName;
    public String gameLogoImg;
    public Object isDelete;
    public long createTime;
    public long updateTime;

}
