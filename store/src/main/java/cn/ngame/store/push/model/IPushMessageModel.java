package cn.ngame.store.push.model;

import java.util.List;

/**
 * 消息推送服务层接口
 * Created by zeng on 2016/11/24.
 */
public interface IPushMessageModel {

    /**
     * 保存推送消息到本地数据库
     */
    void savePushMessage(PushMessage msg);

    /**
     * 将消息标记为已读
     * @param msgType   服务端消息类型
     * @param msgId     服务端消息ID
     */
    void markMsgHasRead(int msgType,long msgId);

    /**
     * 将所有消息标记为已读
     * @param type 0.所有  1.通知  2.活动
     */
    void markAllHasRead(int type);

    /**
     * 删除某一类型的所有推送消息
     */
    void deletePushMessageByType(int type);

    /**
     * 删除本地数据库中的消息记录
     * @param id 本地数据库中的ID
     */
    void deletePushMessage(int id);

    /**
     * 分页查询消息数据
     * @param type          消息类别
     * @param pageNo        页码
     * @param pageSize      每页的条数
     * @return 消息列表
     */
    List<PushMessage> getPushMessage(int type,int pageNo,int pageSize);

    /**
     * 获取消息详情
     * @param type      消息类别
     * @param msgId     消息在服务端的ID
     */
    MessageDetail getMsgDetail(int type,long msgId);

    /**
     * 查询未读消息数
     * @param type 消息类型  0.所有  1.通知 2.活动
     * @return 未读消息数量
     */
    int getUnReadMsgCount(int type);

}
