package cn.ngame.store.push.model;

/**
 * 消息详情
 * Created by zeng on 2016/12/2.
 */
public class MessageDetail {

    private long id;
    private String msgIntroduction;
    private String msgContent;
    private String msgType;
    private long createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsgIntroduction() {
        return msgIntroduction;
    }

    public void setMsgIntroduction(String msgIntroduction) {
        this.msgIntroduction = msgIntroduction;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
