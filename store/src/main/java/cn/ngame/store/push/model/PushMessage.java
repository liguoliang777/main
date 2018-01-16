package cn.ngame.store.push.model;

import java.io.Serializable;

/**
 * 推送消息的实体类
 * Created by zeng on 2016/11/24.
 */
public class PushMessage implements Serializable{

    public static final int MSG_TYPE_TZ = 1;
    public static final int MSG_TYPE_HD = 2;

    public PushMessage() {}

    public PushMessage(long msgId, int type, int isRead, String title, String description, long receiveDate) {
        this.msgId = msgId;
        this.type = type;
        this.isRead = isRead;
        this.title = title;
        this.description = description;
        this.receiveDate = receiveDate;
    }

    /**本地数据库的ID*/
    private int id;
    /** 消息在服务端的id*/
    private long msgId;
    /** 消息类型*/
    private int type;
    /** 是否已读 0 未读 1已读*/
    private int isRead = 0;
    /** 消息标题*/
    private String title;
    /** 消息简介*/
    private String description;
    /** 消息接收时间 */
    private long receiveDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(long receiveDate) {
        this.receiveDate = receiveDate;
    }
}
