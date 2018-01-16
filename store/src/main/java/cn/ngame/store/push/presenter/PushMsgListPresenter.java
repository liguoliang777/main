package cn.ngame.store.push.presenter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;

import java.util.List;

import cn.ngame.store.push.model.IPushMessageModel;
import cn.ngame.store.push.model.PushMessage;
import cn.ngame.store.push.model.PushMessageModel;
import cn.ngame.store.push.view.IPushMsgListView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 控制层实现类
 * Created by zeng on 2016/11/24.
 */
public class PushMsgListPresenter implements IPushMsgListPresenter {

    public static final String TAG = PushMsgListPresenter.class.getName();

    private IPushMessageModel msgModel;
    private IPushMsgListView msgView;
    Handler handler = new Handler();

    public PushMsgListPresenter(Context context, IPushMsgListView msgView) {
        this.msgView = msgView;
        msgModel = new PushMessageModel(context);
    }

    @Override
    public void showMsgList(final int type, final int pageNo, final int pageSize) {
        Log.d(TAG, "显示消息: ");
        Observable.just(msgModel.getPushMessage(type, pageNo, pageSize))
                //指定了被观察者执行的线程环境
                .subscribeOn(Schedulers.newThread())
                //将接下来执行的线程环境指定为io线程
                //.observeOn(Schedulers.io())
                //此处可以使用操作符进行类型转换等其他操作
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverWrapper<List<PushMessage>>()
                {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }

                    @Override
                    public void onNext(List<PushMessage> msgList) {
                        for (PushMessage pushMessage : msgList) {
                            Log.d(TAG, "消息Id:"+pushMessage.getId());
                            Log.d(TAG, "消息MsgId:"+pushMessage.getMsgId());
                            Log.d(TAG, "消息Title:"+pushMessage.getTitle());
                            Log.d(TAG, "消息Description:"+pushMessage.getDescription());
                        }
                        Log.d(TAG, "获取消息列表大小:"+msgList.size());
                        msgView.showMsgList(msgList);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG,"结束观察者...");
//                    }
                });

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                final List<PushMessage> msgList = msgModel.getPushMessage(type, pageNo, pageSize);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        msgView.showMsgList(msgList);
                    }
                });
            }
        }).start();*/

    }

    @Override
    public void markHasRead(final int msgType,final long msgId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                msgModel.markMsgHasRead(msgType,msgId);
            }
        }).start();

    }

    /**
     * 将所有消息标记为已读
     * @param type 0.所有  1.通知  2.活动
     */
    @Override
    public void markAllHasRead(final int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                msgModel.markAllHasRead(type);
            }
        }).start();
    }

    @Override
    public void deleteMsg(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                msgModel.deletePushMessage(id);
            }
        }).start();
    }

    @Override
    public void deleteAllMsgByType(int type) {

    }

}
