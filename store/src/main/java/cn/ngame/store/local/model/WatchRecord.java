package cn.ngame.store.local.model;

import java.util.Date;

/**
 * 视频观看历史信息
 * Created by zeng on 2016/10/11.
 */
public class WatchRecord {

    private long id;
    private long videoId;
    private String videoName;
    private String videoImageLink;
    private long viewDuration;      //观看时长
    private long videoDuration;     //视频总时长
    private long viewTime;          //观看时间

    public long getId() {
        return id;
    }

    public long getViewTime() {
        return viewTime;
    }

    public void setViewTime(long viewTime) {
        this.viewTime = viewTime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoImageLink() {
        return videoImageLink;
    }

    public void setVideoImageLink(String videoImageLink) {
        this.videoImageLink = videoImageLink;
    }

    public long getViewDuration() {
        return viewDuration;
    }

    public void setViewDuration(long viewDuration) {
        this.viewDuration = viewDuration;
    }

    public long getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(long videoDuration) {
        this.videoDuration = videoDuration;
    }

    public WatchRecord() {
    }

    public WatchRecord(long videoId, String videoName, String videoImageLink, long viewDuration, long videoDuration) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoImageLink = videoImageLink;
        this.viewDuration = viewDuration;
        this.videoDuration = videoDuration;
    }
}
