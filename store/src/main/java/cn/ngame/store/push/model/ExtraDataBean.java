package cn.ngame.store.push.model;

/**
 * 百度推送 通知类型的 自定义额外数据
 * Created by zeng on 2016/11/25.
 */
public class ExtraDataBean {

    private long msgId;
    private int type;

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
}
