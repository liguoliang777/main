package cn.ngame.store.push.presenter;

/**
 * 消息详情控制页面
 * Created by zeng on 2016/11/25.
 */
public interface IPushMsgDetailPresenter {

    /**
     * 显示消息详情
     * @param type      消息类型
     * @param msgId     消息的服务端ID
     */
    void showDetail(int type,long msgId);

    /**
     * 将消息标记为已读
     * @param msgType   服务端消息类型
     * @param msgId     服务端消息ID
     */
    void markHasRead(int msgType,long msgId);

}
