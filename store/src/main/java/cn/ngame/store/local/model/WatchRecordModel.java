package cn.ngame.store.local.model;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cn.ngame.store.StoreApplication;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.db.DatabaseManager;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.TextUtil;

/**
 * 本地管理，视频观看记录model层接口
 * Created by zeng on 2016/10/11.
 */
public class WatchRecordModel implements IWatchRecordModel {

    private DatabaseManager dbManager;

    public WatchRecordModel(Context context){
        dbManager = DatabaseManager.getInstance(context);
    }

    /**
     * 同步本地与服务器上的观看记录
     */
    @Override
    public void synchronizeWatchRecord() {

        try {
            Thread.sleep(3000); //休眠5秒钟后同步，防止网络卡顿未登录成功
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final String userCode = StoreApplication.user == null ? null : StoreApplication.user.userCode;
        //如果用户未登录删除本地数据库
        if(TextUtil.isEmpty(StoreApplication.token) || TextUtil.isEmpty(userCode)){
            return;
        }

        try {
            //1.查询本地数库中的记录
            List<WatchRecord> records = dbManager.queryAllHistory();

            //2.上传本地数据到服务器端
            for(WatchRecord record : records){

                final WatchRecord wh = record;
                String url = Constant.WEB_SITE + Constant.URL_WATCH_RECORD_ADD;
                RequestFuture<JsonResult> future = RequestFuture.newFuture();
                Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST,url,
                        future,future,new TypeToken<JsonResult>(){}.getType()){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("token",StoreApplication.token);
                        params.put("userCode",userCode);
                        params.put("appTypeId","0");
                        params.put("videoId",String.valueOf(wh.getVideoId()));
                        params.put("videoName",wh.getVideoName());
                        params.put("videoImageLink",wh.getVideoImageLink());
                        params.put("videoDuration",String.valueOf(wh.getVideoDuration()));
                        params.put("viewDuration",String.valueOf(wh.getViewDuration()));
                        params.put("watchDate",String.valueOf(System.currentTimeMillis()));

                        return params;
                    }
                };
                StoreApplication.requestQueue.add(versionRequest);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //3.删除本地数据库中的记录
        dbManager.deleteAllWatchHistory();
    }

    @Override
    public void addWatchRecord(final WatchRecord wh) {

        final String userCode = StoreApplication.user == null ? null : StoreApplication.user.userCode;

        //如果用户未登录删除本地数据库
        if(TextUtil.isEmpty(StoreApplication.token) || TextUtil.isEmpty(userCode)){

            dbManager.addWatchHistory(wh);

        }else {

            String url = Constant.WEB_SITE + Constant.URL_WATCH_RECORD_ADD;
            RequestFuture<JsonResult> future = RequestFuture.newFuture();
            Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST,url,
                    future,future,new TypeToken<JsonResult>(){}.getType()){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("token",StoreApplication.token);
                    params.put("userCode",userCode);
                    params.put("appTypeId","0");
                    params.put("videoId",String.valueOf(wh.getVideoId()));
                    params.put("videoName",wh.getVideoName());
                    params.put("videoImageLink",wh.getVideoImageLink());
                    params.put("videoDuration",String.valueOf(wh.getVideoDuration()));
                    params.put("viewDuration",String.valueOf(wh.getViewDuration()));
                    params.put("watchDate",String.valueOf(System.currentTimeMillis()));

                    return params;
                }
            };
            StoreApplication.requestQueue.add(versionRequest);
        }


    }

    @Override
    public void deleteRecord(final String token,final String userCode,List<WatchRecord> deleteData) {

        //如果用户未登录删除本地数据库
        if(TextUtil.isEmpty(token) || TextUtil.isEmpty(userCode)){

            dbManager.deleteWatchHistoryById(deleteData);

        }else {

            StringBuilder sb = new StringBuilder();
            for(WatchRecord record : deleteData){
                sb.append(record.getVideoId()).append("_");
            }
            final String videoIdStr = sb.substring(0,sb.length()-1);

            String url = Constant.WEB_SITE + Constant.URL_WATCH_RECORD_DELETE;
            RequestFuture<JsonResult> future = RequestFuture.newFuture();
            Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST,url,
                    future,future,new TypeToken<JsonResult>(){}.getType()){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("token",token);
                    params.put("userCode",userCode);
                    params.put("videoStr",videoIdStr);
                    params.put("appTypeId","0");
                    return params;
                }
            };
            StoreApplication.requestQueue.add(versionRequest);
        }
    }

    @Override
    public WatchRecordGroup queryWatchRecord(final String token, final String userCode) {

        //如果用户未登录查询本地数据库
        if(TextUtil.isEmpty(token) || TextUtil.isEmpty(userCode)){

            WatchRecordGroup group = new WatchRecordGroup();
            try {

                List<WatchRecord> weekData = dbManager.queryWeekHistory();
                List<WatchRecord> otherDate = dbManager.queryOtherHistory();

                group.setWeekPlayRecord(weekData);
                group.setEarlierPlayRecord(otherDate);

            } catch (ParseException e) {
                e.printStackTrace();
                return group;
            }
            return group;

        }else { //登录用户查询服务器上的数据

            String url = Constant.WEB_SITE + Constant.URL_WATCH_RECORD_QUERY;
            RequestFuture<JsonResult<WatchRecordGroup>> future = RequestFuture.newFuture();
            Request<JsonResult<WatchRecordGroup>> versionRequest = new GsonRequest<JsonResult<WatchRecordGroup>>(Request.Method.POST,url,
                    future,future,new TypeToken<JsonResult<WatchRecordGroup>>(){}.getType()){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("token",token);
                    params.put("userCode",userCode);
                    params.put("appTypeId","0");
                    return params;
                }
            };
            StoreApplication.requestQueue.add(versionRequest);

            WatchRecordGroup group = null;
            try {
                JsonResult<WatchRecordGroup> result = future.get();
                if(result != null && result.code == 0 && result.data != null){
                    group = result.data;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return group;
        }

    }

    @Override
    public List<WatchRecord> queryWeekWatchRecord() {

        List<WatchRecord> res = null;
        try {
            res = dbManager.queryWeekHistory();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<WatchRecord> queryOtherWatchRecord() {

        List<WatchRecord> res = null;
        try {
            res = dbManager.queryOtherHistory();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }
}
