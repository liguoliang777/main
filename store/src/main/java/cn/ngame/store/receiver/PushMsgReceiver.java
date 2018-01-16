package cn.ngame.store.receiver;

import android.content.Context;
import android.content.Intent;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.google.gson.Gson;

import java.util.List;

import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.SystemUtils;
import cn.ngame.store.push.model.ExtraDataBean;
import cn.ngame.store.push.model.IPushMessageModel;
import cn.ngame.store.push.model.PushMessage;
import cn.ngame.store.push.model.PushMessageModel;
import cn.ngame.store.push.view.MessageDetailActivity;
import cn.ngame.store.push.view.NotifyMsgDetailActivity;

/**
 * 接收百度云推送回调信息和推送到达信息
 * Created by zeng on 2016/11/23.
 */
public class PushMsgReceiver extends PushMessageReceiver{

    public static final String TAG = PushMsgReceiver.class.getName();
    private Gson gson = new Gson();

    /** 设备与推送服务绑定 */
    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
        Log.d(TAG,"onBind ---->>>"+i);
        Log.d(TAG,"onBind ---->>>"+s);
        Log.d(TAG,"onBind ---->>>"+s1);
        Log.d(TAG,"onBind ---->>>"+s2);
        Log.d(TAG,"onBind ---->>>"+s3);
    }

    @Override
    public void onUnbind(Context context, int i, String s) {
        Log.d(TAG,"onUnbind ---->>>"+i);
        Log.d(TAG,"onUnbind ---->>>"+s);
    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    /** 接收透传消息的函数 msgContent 推送的消息  customContentString 自定义内容，为空或者json字符串*/
    @Override
    public void onMessage(Context context, String msgContent, String customContentString) {

        Log.d(TAG,"onMessage ---->>>"+msgContent);
        Log.d(TAG,"onMessage ---->>>"+customContentString);

    }

    /**
     * 接收通知点击的函数
     * @param title                 推送的通知的标题
     * @param description           推送的通知的描述
     * @param customContentString   自定义内容，为空或者json字符串
     * */
    @Override
    public void onNotificationClicked(Context context, String title, String description, String customContentString) {

        Log.d(TAG,"------------------>>>> 点了通知");

        ExtraDataBean bean = gson.fromJson(customContentString, ExtraDataBean.class);
        //判断app进程是否存活
        if(!SystemUtils.isApplicationBroughtToBackground(context)){   //如果存活的话，就直接启动

            //Log.e(TAG,"APP是活的。。。。。。。。。。。。。。。。");
            if(bean.getType() == PushMessage.MSG_TYPE_TZ){

                Intent intent = new Intent(context.getApplicationContext(), NotifyMsgDetailActivity.class);
                intent.putExtra("msgId",bean.getMsgId());
                intent.putExtra("type",bean.getType());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                PushMessage msg = new PushMessage();
                msg.setReceiveDate(System.currentTimeMillis());
                msg.setType(bean.getType());
                msg.setMsgId(bean.getMsgId());
                msg.setIsRead(0);
                msg.setTitle(title);
                msg.setDescription(description);
                intent.putExtra("msg",msg);
                context.getApplicationContext().startActivity(intent);

            }else {

                Intent intent = new Intent(context.getApplicationContext(), MessageDetailActivity.class);
                intent.putExtra("msgId",bean.getMsgId());
                intent.putExtra("type",bean.getType());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
            }

        }else {
            //如果app进程已经被杀死，先重新启动app
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("cn.ngame.store");
            launchIntent.putExtra("msgId",bean.getMsgId());
            launchIntent.putExtra("type",bean.getType());
            if(bean.getType() == PushMessage.MSG_TYPE_TZ){
                PushMessage msg = new PushMessage();
                msg.setReceiveDate(System.currentTimeMillis());
                msg.setType(bean.getType());
                msg.setMsgId(bean.getMsgId());
                msg.setIsRead(0);
                msg.setTitle(title);
                msg.setDescription(description);
                launchIntent.putExtra("msg",msg);
            }
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(launchIntent);

            //Log.e(TAG,"APP死了。。。。。。。。。。。。。。。。");
        }
    }

    /**
     * 接收通知到达的函数
     */
    @Override
    public void onNotificationArrived(Context context, String title, String description, String customContentString) {

        Log.d(TAG,"onNotificationArrived ---->>>"+title);
        Log.d(TAG,"onNotificationArrived ---->>>"+description);
        Log.d(TAG,"onNotificationArrived ---->>>"+customContentString);

        ExtraDataBean bean = gson.fromJson(customContentString, ExtraDataBean.class);

        final IPushMessageModel model = new PushMessageModel(context);
        final PushMessage msg = new PushMessage();

        //Log.e(TAG,"onNotificationArrived ---->>> msgId :"+ bean.getMsgId() + " type "+bean.getType());
        msg.setMsgId(bean.getMsgId());
        msg.setType(bean.getType());

        msg.setIsRead(0);   //0代表未读
        msg.setTitle(title);
        msg.setDescription(description);
        msg.setReceiveDate(System.currentTimeMillis());

        new Thread(new Runnable() {
            @Override
            public void run() {
                model.savePushMessage(msg);
            }
        }).start();

    }
}
