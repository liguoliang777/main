package cn.ngame.store.push.view;

import java.util.List;

import cn.ngame.store.push.model.PushMessage;

/**
 * 显示层接口
 * Created by zeng on 2016/11/24.
 */
public interface IPushMsgListView {

    /**
     * 显示消息列表
     * @param msgList 消息集合
     */
    void showMsgList(List<PushMessage> msgList);

}
