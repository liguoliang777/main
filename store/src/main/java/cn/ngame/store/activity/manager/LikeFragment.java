package cn.ngame.store.activity.manager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBean;
import com.jzt.hol.android.jkda.sdk.bean.manager.LikeListBody;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.main.LikeListClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.main.MainHomeActivity;
import cn.ngame.store.adapter.LikeFragmentAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.NetUtil;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.ActionItem;
import cn.ngame.store.view.QuickAction;

/**
 * 下载更新fragment (懒加载-当滑动到当前fragment时，才去加载。而不是进入到activity时，加载所有fragment)
 * Created by gp on 2017/3/3 0003.
 */
@SuppressLint("WrongConstant")
public class LikeFragment extends BaseSearchFragment {

    private ListView listView;
    private PageAction pageAction;
    public static int PAGE_SIZE = 15;
    protected QuickAction mItemClickQuickAction;
    private LikeFragmentAdapter likeAdapter;
    private static MainHomeActivity content;
    private List<LikeListBean.DataBean.GameListBean> gameList;
    private TextView emptyTv;

    public static LikeFragment newInstance(String type, int arg, MainHomeActivity context) {
        LikeFragment fragment = new LikeFragment();
        content = context;
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putInt("typeValue", arg);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_installed;
    }

    private List<TimerTask> timerTasks = new ArrayList<>();

    @Override
    protected void initViewsAndEvents(View view) {
        listView =  view.findViewById(R.id.listView);
        emptyTv =  view.findViewById(R.id.empty_tv);
    }

    private void getLikeList() {
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);

        initPop();
        likeAdapter = new LikeFragmentAdapter(content, getSupportFragmentManager(), mItemClickQuickAction, timerTasks);
        listView.setAdapter(likeAdapter);

        emptyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String retryStr = emptyTv.getText().toString();
                if (retryStr.endsWith("点击重试")) {
                    getLikeList();
                }
            }
        });

        //tabPosition :0=全部   1=手柄   2=破解   3=汉化  4=特色
        emptyTv.setVisibility(View.VISIBLE);
        if (!NetUtil.isNetworkConnected(content)) {
            emptyTv.setText(getString(R.string.no_network));
            return;
        }
        if (gameList == null || gameList.size() == 0) {
            emptyTv.setText("正在加载...");
        }
     /*   if (gameList != null) {
            gameList.clear();
        }*/
        LikeListBody bodyBean = new LikeListBody();
        bodyBean.setUserCode(StoreApplication.userCode);
        bodyBean.setStartRecord(pageAction.getCurrentPage());
        bodyBean.setRecords(PAGE_SIZE);
        new LikeListClient(content, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<LikeListBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(getActivity(), APIErrorUtils.getMessage(e));
                        Log.d(TAG, "onError: " + e.getMessage());
                        emptyTv.setText("获取喜欢列表失败~点击重试");
                    }

                    @Override
                    public void onNext(LikeListBean result) {
                        if (result != null && result.getCode() == 0) {
                            LikeListBean.DataBean data = result.getData();
                            if (data != null) {
                                if (gameList != null) {
                                    gameList.clear();
                                }
                                if (null != likeAdapter) {
                                    likeAdapter.setDate(gameList);
                                }
                                gameList = data.getGameList();
                                if (gameList == null || gameList.size() == 0) {
                                    emptyTv.setText("您的喜欢列表为空哦~");
                                    emptyTv.setVisibility(View.VISIBLE);
                                } else {
                                    if (null != likeAdapter) {
                                        likeAdapter.setDate(gameList);
                                    } else {
                                    }
                                    emptyTv.setVisibility(View.GONE);
                                }
                            } else {
                                emptyTv.setText("您的喜欢列表为空哦~");
                                emptyTv.setVisibility(View.VISIBLE);
                            }
                        } else {
                            //ToastUtil.show(getActivity(), result.getMsg());
                            emptyTv.setText(getString(R.string.server_exception) + "点击重试");
                            emptyTv.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void initPop() {
        // 设置Action
        mItemClickQuickAction = new QuickAction(content, QuickAction.VERTICAL);
        ActionItem pointItem = new ActionItem(1, "不再喜欢");
        mItemClickQuickAction.addActionItem(pointItem);

        mItemClickQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                if (pos == 0 && null != likeAdapter) {
                    final Dialog dialog = new Dialog(content);
                    //填充对话框的布局
                    View inflate = LayoutInflater.from(content).inflate(R.layout.layout_dialog_cancel_like, null);

                    View.OnClickListener mDialogClickLstener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (v.getId() == R.id.choose_right_tv) {//游戏更新
                                //获取gameId  传给服务器 不再喜欢
                                int gamePosition = likeAdapter.getItemGameId();
                                if (gameList != null && gamePosition < gameList.size()) {
                                    cancelFavorite(gamePosition);
                                }
                            }
                            dialog.cancel();
                        }
                    };
                    inflate.findViewById(R.id.choose_right_tv).setOnClickListener(mDialogClickLstener);
                    inflate.findViewById(R.id.choose_cancel_tv).setOnClickListener(mDialogClickLstener);

                    dialog.setContentView(inflate);//将布局设置给Dialog
                    setDialogWindow(dialog);
                }
                //取消弹出框
                source.dismiss();
            }
        });
    }

    private void setDialogWindow(Dialog dialog) {
        Window dialogWindow = dialog.getWindow(); //获取当前Activity所在的窗体
        dialogWindow.setGravity(Gravity.CENTER);//设置Dialog从窗体底部弹出
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = dialogWindow.getAttributes();   //获得窗体的属性
        //params.y = 20;  Dialog距离底部的距离
        params.width = getResources().getDimensionPixelSize(R.dimen.dm440);//设置Dialog距离底部的距离
        dialogWindow.setAttributes(params); //将属性设置给窗体
        dialog.show();//显示对话框
    }

    private void cancelFavorite(final int position) {
        final long gameId = gameList.get(position).getId();
        Log.d(TAG, "cancelFavorite: " + gameId);
        DialogHelper.showWaiting(getSupportFragmentManager(), "加载中...");
        String url = Constant.WEB_SITE + Constant.URL_DEL_FAVORITE;
        Response.Listener<JsonResult> successListener = new Response.Listener<JsonResult>() {
            @Override
            public void onResponse(JsonResult result) {
                DialogHelper.hideWaiting(getSupportFragmentManager());
                if (result == null) {
                    ToastUtil.show(content, getString(R.string.server_exception));
                    return;
                }
                if (result.code == 0) {
                    ToastUtil.show(content, "取消喜欢成功");
                    if (gameList.size() > position) {
                        gameList.remove(position);
                    }
                    if (null != likeAdapter) {
                        likeAdapter.setDate(gameList);
                    }
                } else {
                    ToastUtil.show(content, "取消喜欢失败");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                DialogHelper.hideWaiting(getSupportFragmentManager());
                ToastUtil.show(content, "取消喜欢失败,请稍后重试");
                Log.d(TAG, "取消喜欢失败：网络连接错误！" + volleyError.getMessage());
            }
        };

        Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //{"userCode":"UC1500609205627","gameId":146,"appTypeId":0}
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.GAME_ID, String.valueOf(gameId));
                params.put(KeyConstant.USER_CODE, StoreApplication.userCode);
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && null != likeAdapter) {
            likeAdapter.clean();
            likeAdapter = null;
            for (TimerTask timerTask : timerTasks) {
                timerTask.cancel();
            }
            timerTasks.clear();
        }
    }

    @Override
    protected void onFirstUserVisible() {
        getLikeList();
    }

    protected final static String TAG = LikeFragment.class.getSimpleName();

    protected void onUserVisible() {
      /*  if (null != likeAdapter) {
            likeAdapter.clean();
        }*/
        getLikeList();
    }


    @Override
    protected void onUserInvisible() {
    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }
}
