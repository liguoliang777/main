package cn.ngame.store.push.model;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cn.ngame.store.StoreApplication;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.db.DatabaseManager;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;

/**
 * 推送消息服务层实现类
 * Created by zeng on 2016/11/24.
 */
public class PushMessageModel implements IPushMessageModel{

    private DatabaseManager dbManager;

    public PushMessageModel(Context context) {
        this.dbManager = DatabaseManager.getInstance(context);
    }

    @Override
    public void savePushMessage(PushMessage msg) {
        if(dbManager != null){
            dbManager.addPushMsg(msg);
        }
    }

    @Override
    public void markMsgHasRead(int msgType,long msgId) {
        if(dbManager != null){
            dbManager.markMsgHasRead(msgType,msgId);
        }
    }

    @Override
    public void markAllHasRead(int type) {
        if(dbManager != null){
            dbManager.markMsgAllHasRead(type);
        }
    }

    @Override
    public void deletePushMessageByType(int type) {
        if(dbManager != null){
            dbManager.deleteAllPushMsg();
        }
    }

    @Override
    public void deletePushMessage(int id) {
        if(dbManager != null){
            dbManager.deletePushMsgById(id);
        }
    }

    @Override
    public List<PushMessage> getPushMessage(int type, int pageNo, int pageSize) {

        if(dbManager != null){

            List<PushMessage> msgList = dbManager.queryPushMessage(pageNo,pageSize,type);
            return msgList;

        }
        return null;
    }

    @Override
    public MessageDetail getMsgDetail(int type, final long msgId) {

        String url = Constant.WEB_SITE + Constant.URL_PUSH_MSG_DETAIL;
        RequestFuture<JsonResult<MessageDetail>> future = RequestFuture.newFuture();
        Request<JsonResult<MessageDetail>> versionRequest = new GsonRequest<JsonResult<MessageDetail>>(Request.Method.POST,url,
                future,future,new TypeToken<JsonResult<MessageDetail>>(){}.getType()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("msgId",String.valueOf(msgId));
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);

        MessageDetail detail = null;
        try {
            JsonResult<MessageDetail> result = future.get();
            if(result != null && result.code == 0 && result.data != null){
                detail = result.data;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return detail;
    }

    @Override
    public int getUnReadMsgCount(int type) {
        int count = 0;
        if(dbManager != null){
            count = dbManager.getUnReadMsgCount(type);
        }
        return count;
    }
}
