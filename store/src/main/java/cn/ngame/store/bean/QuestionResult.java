package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * 评论汇总
 * Created by zeng on 2016/6/17.
 */
public class QuestionResult implements Serializable {

    public long id;

    /**
     * 对应分数值
     */
    public int itemId;
    /**
     * 被评论对象ID
     */
    public int toMatterCode;
    /**
     * 评论总人数
     */
    public int commentPeople;
    /**
     * 总分数
     */
    public int totalPoint;
    /**
     * 分数百分比
     */
    public String percentage;
    /**
     * 创建时间
     */
    private long createTime;
    /**
     * 更新时间
     */
    public long updateTime;

}
