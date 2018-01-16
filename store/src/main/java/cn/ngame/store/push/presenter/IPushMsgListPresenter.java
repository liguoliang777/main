package cn.ngame.store.push.presenter;

/**
 * 控制层接口
 * Created by zeng on 2016/11/24.
 */
public interface IPushMsgListPresenter {

    /**
     * 显示消息列表
     * @param type 消息类型
     */
    void showMsgList(final int type, final int pageNo, final int pageSize);

    /**
     * 将消息标记为已读
     * @param msgType   服务端消息类型
     * @param msgId     服务端消息ID
     */
    void markHasRead(int msgType,long msgId);

    /**
     * 将所有消息标记为已读
     * @param type 0.所有  1.通知  2.活动
     */
    void markAllHasRead(int type);

    /**
     * 删除消息
     * @param id 本地数据库中的Id
     */
    void deleteMsg(int id);

    /**
     * 删除某分类下的所有消息
     * @param type 消息类别
     */
    void deleteAllMsgByType(int type);

}
