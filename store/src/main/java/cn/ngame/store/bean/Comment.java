package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * 内容评论的实体类
 * Created by zeng on 2016/5/30.
 */
public class Comment implements Serializable{

    public long id;
    /**
     * 1.游戏2.视频
     */
    public int categoryId;
    /**
     * 选项
     */
    public int chooseItem;
    /**
     * 分数
     */
    public float chooseItemValue;

    /**
     * 用户ID
     */
    public String userCode;
    /**
     * 用户昵称
     */
    public String nickName;
    /**
     * 被评论对象的ID
     */
    public int toMatterCode;
    /**
     * 评论内容
     */
    public String content;
    /**
     * 用户头像url
     */
    public String headPhoto;

    /**
     * 发表时间
     */
    public long createTime;
    /**更新时间*/
    public long updateTime;

}
