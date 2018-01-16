package cn.ngame.store.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 视频信息的实体类
 * Created by zeng on 2016/6/7.
 */
public class VideoInfo implements Serializable {

    public static final String TAG = VideoInfo.class.getSimpleName();
    public int gameVideoType;
    /**
     * 预览图片地址
     */
    public String gameVideoName;
    /***/
    public String gameVideoLink;
    /***/
    public String gameImageLink;
    /***/
    public String appTypeId;
    /** 推荐短语 */
    /**
     * 更新时间
     */
    public long updateTime;
    public long gameId;
    public int uploadId;
    public long id;
    /**
     * 视频名称
     */
    public String videoName;
    /**
     * 视频下载地址
     */
    public String videoLink;
    /**
     * 预览图片地址
     */
    public String videoImageLink;

    /**
     * 视频简介
     */
    public String videoIntroduce;
    /**
     * 视频产地
     */
    public String produceCountry;
    /**
     * 视频所属类别
     */
    public List<VideoType> videoTypeList;
    /**
     * 上映时间
     */
    public String releaseDate;
    /**
     * 视频时长
     */
    public String duration;

    /**
     * 评分
     */
    public float percentage;

    /**
     * 评论人数
     */
    public float commentPeople;
    /**
     * 是否被推荐
     */
    public String videoRec;
    /**
     * 视频文件名
     */
    public String fileName;
    /**
     * 视频MD5值
     */
    public String md5;
    /**
     * 分类ID
     */
    public int videoTypeId;
    /**
     * 类别ID
     */
    public int videoLabelId;
    /**
     * 排序号
     */
    public long orderNo;
    /**
     * 创建时间
     */
    public long createTime;
    /**
     * 推荐短语
     */
    public String simpleDesc;
    /**
     * 是否是360全局 0.不是，1.是
     */
    public int isPanorama = 0;
    /**
     * 相关推荐
     */
    public List<VideoInfo> videoList3;
}
