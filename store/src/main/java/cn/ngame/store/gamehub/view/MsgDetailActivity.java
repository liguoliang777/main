package cn.ngame.store.gamehub.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.AddCommentBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.AddPointBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.BrowseHistoryBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentListBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.CommentListBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.MsgDetailBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.MsgDetailBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.NormalDataBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.AddCommentClient;
import com.jzt.hol.android.jkda.sdk.services.gamehub.AddPointClient;
import com.jzt.hol.android.jkda.sdk.services.gamehub.BrowseHistoryClient;
import com.jzt.hol.android.jkda.sdk.services.gamehub.CommentListClient;
import com.jzt.hol.android.jkda.sdk.services.gamehub.MsgDetailClient;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.MsgDetailGridAdapter;
import cn.ngame.store.adapter.MsgDetailListAdapter;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.utils.APIErrorUtils;
import cn.ngame.store.core.utils.FileUtil;
import cn.ngame.store.user.view.LoginActivity;
import cn.ngame.store.user.view.RegisterActivity;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.util.DateUtil;
import cn.ngame.store.util.StringUtil;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.popupwin.MsgDetailPop;
import cn.ngame.store.widget.pulllistview.PullToRefreshBase;
import cn.ngame.store.widget.pulllistview.PullToRefreshListView;

import static cn.ngame.store.R.id.pulllistview;


/**
 * 帖子详情
 * Created by gp on 2017/2/16 0016.
 */

public class MsgDetailActivity extends BaseFgActivity implements View.OnClickListener {

    private final int MSG_TYPE_GL = 6;
    private final int MSG_TYPE_QZ = 7;
    private final int MSG_TYPE_TP = 8;
    private LinearLayout ll_back;
    private RelativeLayout ll_share, rl_clickAgree;
    private PullToRefreshListView pullListView;
    private EditText et_content;
    private FrameLayout fl_comment;
    private TextView tv_comment_num;
    private TextView tv_send;
    private TextView tv_type, tv_title, tv_name, tv_time, tv_content, tv_msgId, tv_msg_agreeNum;
    private ImageView iv_icon, iv_msg_agree;
    private GridView gridView;
    private RelativeLayout rl_bot;
    MsgDetailListAdapter listAdapter;
    private PageAction pageAction;
    public static int PAGE_SIZE = 10;
    int msgId; //帖子id
    int msgType; //帖子类型
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private MsgDetailGridAdapter gradAdapter;
    private String[] imgs;
    ArrayList<String> gridList = new ArrayList<>();
    List<CommentListBean.DataBean> list = new ArrayList<>();
    private List<Integer> agreeList = new ArrayList<>();
    private MsgDetailPop popupwin;
    private int isReport = 0; //帖子是否举报 1已举报 （多个用户不能同时举报）

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_detail_activity);
        pageAction = new PageAction();
        pageAction.setCurrentPage(0);
        pageAction.setPageSize(PAGE_SIZE);
        init();
        getMsgDetail(); //帖子详情
        runService(); //帖子评论列表
        browsingHistory(); //浏览记录
    }

    private void init() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_share = (RelativeLayout) findViewById(R.id.ll_share);
        pullListView = (PullToRefreshListView) findViewById(pulllistview);
        et_content = (EditText) findViewById(R.id.et_content);
        fl_comment = (FrameLayout) findViewById(R.id.fl_comment);
        tv_comment_num = (TextView) findViewById(R.id.tv_comment_num);
        tv_send = (TextView) findViewById(R.id.tv_send);
        et_content = (EditText) findViewById(R.id.et_content);
        rl_bot = (RelativeLayout) findViewById(R.id.rl_bot);
        rl_bot.setOnClickListener(this);
        et_content.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        //头布局
        View view = View.inflate(this, R.layout.msg_detail_head_layout, null);
        initHeadView(view);
        //头布局放入listView中
        if (pullListView.getRefreshableView().getHeaderViewsCount() == 0) {
            pullListView.getRefreshableView().addHeaderView(view);
        }

        initListView();
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_content.getText().toString().trim().length() > 0) {
                    tv_send.setEnabled(true);
                    tv_send.setFocusableInTouchMode(false);
                    tv_send.setTextColor(getResources().getColor(R.color.mainColor));
                } else {
                    tv_send.setEnabled(false);
                    tv_send.setTextColor(getResources().getColor(R.color.color999999));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    tv_send.setVisibility(View.GONE);
                    fl_comment.setVisibility(View.VISIBLE);
                    StringUtil.hideKeyBorad(MsgDetailActivity.this);
                } else {
                    User user = StoreApplication.user;
                    if (user == null) {
                        showPop();
                    } else {
                        fl_comment.setVisibility(View.GONE);
                        tv_send.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent gv = new Intent();
                gv.setClass(MsgDetailActivity.this, ShowViewActivity.class);
                gv.putStringArrayListExtra("viewImages", gridList);
                gv.putExtra("selectPosition", position);
                startActivity(gv);
            }
        });
        msgId = getIntent().getIntExtra("msgId", 0);
        User user = StoreApplication.user;
        if (user == null) {
            et_content.setKeyListener(null); //获得焦点后不会弹出输入法
        }
    }

    private void browsingHistory() {
        BrowseHistoryBodyBean bodyBean = new BrowseHistoryBodyBean();
        User user = StoreApplication.user;
        if (user != null) {
            bodyBean.setUserCode(user.userCode);
        } else {
            bodyBean.setDeviceOnlyNum(StoreApplication.deviceId);
        }
        bodyBean.setPostId(msgId);
        new BrowseHistoryClient(this, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<NormalDataBean>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(MsgDetailActivity.this, APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(NormalDataBean result) {
                        if (result != null && result.getCode() == 0) {

                        } else {
                            ToastUtil.show(MsgDetailActivity.this, result.getMsg());
                        }
                    }
                });
    }

    // 查询帖子详情
    private void getMsgDetail() {
        MsgDetailBodyBean bodyBean = new MsgDetailBodyBean();
        User user = StoreApplication.user;
        if (user != null) {
            bodyBean.setUserCode(user.userCode);
        } else {
            bodyBean.setDeviceOnlyNum(StoreApplication.deviceId);
        }
        bodyBean.setId(msgId);
        bodyBean.setType(1);
        new MsgDetailClient(this, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<MsgDetailBean>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(MsgDetailActivity.this, APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(MsgDetailBean result) {
                        if (result != null && result.getCode() == 0) {
                            setMsgDetail(result);
                        } else {
                            ToastUtil.show(MsgDetailActivity.this, result.getMsg());
                        }
                    }
                });
    }

    // 帖子详情头部
    private void setMsgDetail(MsgDetailBean result) {
        if (result.getData() == null) {
            return;
        }
        isReport = result.getData().getIsReport();
        msgType = result.getData().getPostTagId();
        tv_type.setText(StringUtil.getGameHubMsgType(msgType));
        tv_title.setText(result.getData().getPostTitle());
        imageLoader.displayImage(result.getData().getHeadPhoto(), iv_icon, FileUtil.getModelOptions(R.drawable.ic_def_logo_720_288, 0));
        tv_name.setText(result.getData().getPostPublisher());
        tv_time.setText(DateUtil.getStrTime_ymd_hm(result.getData().getCreateTime()));
        tv_content.setText(result.getData().getPostContent());
        if (StringUtil.isEmpty(result.getData().getPostImage())) {
            gridView.setVisibility(View.GONE);
        } else {
            gridList.clear();
            if (result.getData().getPostImage().contains(",")) {
                imgs = result.getData().getPostImage().split(",");
                for (int i = 0; i < imgs.length; i++) {
                    gridList.add(imgs[i]);
                }
            } else {
                gridList.add(result.getData().getPostImage());
            }
            if (gradAdapter == null) {
                gradAdapter = new MsgDetailGridAdapter(this, gridList);
                gridView.setAdapter(gradAdapter);
            } else {
                gradAdapter.setList(gridList);
            }
            //计算高度
            StringUtil.setGridViewHeightBasedOnChildren(gridView, 3);
        }
        tv_msg_agreeNum.setText(result.getData().getPointCount() + "");
        if (result.getData().getIsPoint() == 1) { //已经点赞
            tv_msg_agreeNum.setTextColor(getResources().getColor(R.color.mainColor));
            iv_msg_agree.setBackgroundResource(R.drawable.msg_detail_disagree);
            tv_msg_agreeNum.setEnabled(false);
            iv_msg_agree.setEnabled(false);
        }
        tv_msgId.setText("本条帖子ID：" + result.getData().getId());
    }

    private void initHeadView(View view) {
        tv_type = (TextView) view.findViewById(R.id.tv_type);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_msgId = (TextView) view.findViewById(R.id.tv_msgId);
        tv_msg_agreeNum = (TextView) view.findViewById(R.id.tv_msg_agreeNum);
        rl_clickAgree = (RelativeLayout) view.findViewById(R.id.rl_clickAgree);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        iv_msg_agree = (ImageView) view.findViewById(R.id.iv_msg_agree);
        gridView = (GridView) view.findViewById(R.id.gridView);
        rl_clickAgree.setOnClickListener(this);
    }

    public void initListView() {
        pullListView.setPullRefreshEnabled(false);
        pullListView.setPullLoadEnabled(false);
        pullListView.setScrollLoadEnabled(false);
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
        pullListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullListView.setPullLoadEnabled(true);
                pageAction.setCurrentPage(0);
                runService();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //少于指定条数不加载
                if (pageAction.getTotal() < pageAction.getPageSize()) {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                    return;
                }
                if (pageAction.getCurrentPage() * pageAction.getPageSize() < pageAction.getTotal()) {
                    pageAction.setCurrentPage(pageAction.getCurrentPage() == 0 ? pageAction.getCurrentPage() + 2 : pageAction.getCurrentPage() + 1);
                    runService();
                } else {
                    pullListView.setHasMoreData(false);
                    pullListView.onPullUpRefreshComplete();
                }
            }
        });
    }

    // 评论列表
    private void runService() {
        CommentListBodyBean bodyBean = new CommentListBodyBean();
        bodyBean.setCode(msgId);
        User user = StoreApplication.user;
        if (user != null) {
            bodyBean.setUserCode(user.userCode);
        } else {
            bodyBean.setDeviceOnlyNum(StoreApplication.deviceId);
        }
        bodyBean.setPageIndex(pageAction.getCurrentPage());
        bodyBean.setPageSize(PAGE_SIZE);
        new CommentListClient(this, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<CommentListBean>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(MsgDetailActivity.this, APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(CommentListBean result) {
                        if (result != null && result.getCode() == 0) {
                            listData(result);
                        } else {
                            ToastUtil.show(MsgDetailActivity.this, result.getMsg());
                        }
                    }
                });
    }

    public void listData(CommentListBean result) {
        if (result.getData() == null) {
            return;
        }
        if (pageAction.getCurrentPage() == 0) {
            this.list.clear(); //清除数据
            if (result.getData() == null || result.getData().size() == 0) {
                pullListView.onPullUpRefreshComplete();
                pullListView.onPullDownRefreshComplete();
                pullListView.setLastUpdatedLabel(new Date().toLocaleString());
                pullListView.getRefreshableView().setAdapter(listAdapter); //数据位空时，加载头部
                return;
            }
        }
        if (result.getData().size() > 0) {
            pageAction.setTotal(result.getTotals());
            this.list.addAll(result.getData());
        }
        tv_comment_num.setVisibility(View.VISIBLE);
        tv_comment_num.setText(result.getTotals() + "");
        if (listAdapter == null) {
            listAdapter = new MsgDetailListAdapter(this, list);
            pullListView.getRefreshableView().setAdapter(listAdapter);
        } else {
            listAdapter.setList(list, null);
        }
        //设置下位列表
        if ((list.size() == 0 && pageAction.getTotal() == 0) || list.size() >= pageAction.getTotal()) {
            pullListView.setPullLoadEnabled(false);
        } else {
            pullListView.setPullLoadEnabled(true);
        }
        if (pageAction.getCurrentPage() > 0 && result.getData().size() > 0) { //设置上拉刷新后停留的地方
            int index = pullListView.getRefreshableView().getFirstVisiblePosition();
            View v = pullListView.getRefreshableView().getChildAt(0);
            int top = (v == null) ? 0 : (v.getTop() - v.getHeight());
            pullListView.getRefreshableView().setSelectionFromTop(index, top);
        }
        pullListView.onPullUpRefreshComplete();
        pullListView.onPullDownRefreshComplete();
        pullListView.setLastUpdatedLabel(new Date().toLocaleString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                this.finish();
                break;
            case R.id.ll_share:
                Intent intent = new Intent();
                intent.setClass(this, ReportActivity.class);
                intent.putExtra("isReport", isReport);
                intent.putExtra("postId", msgId);
                startActivity(intent);
                break;
            case R.id.tv_send:
                tv_send.setEnabled(false);
                sendComment();
                StringUtil.hideKeyBorad(MsgDetailActivity.this);
                break;
            case R.id.rl_clickAgree: //帖子点赞
                clickAgree(1, msgId, 0);
                break;
            case R.id.tv_toLogin:
                popupwin.dismiss();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.tv_toRegister:
                popupwin.dismiss();
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.et_content:
                User user = StoreApplication.user;
                if (user == null) {
                    showPop();
                } else {
                    fl_comment.setVisibility(View.GONE);
                    tv_send.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void showPop() {
        popupwin = new MsgDetailPop(this, this);
        popupwin.showAtLocation(findViewById(R.id.rl_match), Gravity.CENTER, 0, 0);
    }

    /**
     * @param type     1表示帖子点赞，2表示评论点赞，3表示投票
     * @param id       帖子id/评论id
     * @param position listview的item
     */
    public void clickAgree(final int type, int id, final int position) {
        //帖子id
        AddPointBodyBean bodyBean = new AddPointBodyBean();
        User user = StoreApplication.user;
        if (user != null) {
            bodyBean.setUserCode(user.userCode);
        } else {
            bodyBean.setDeviceOnlyNum(StoreApplication.deviceId);
        }
        bodyBean.setType(type);  //type：1表示帖子点赞，2表示评论点赞，3表示投票
        bodyBean.setPostId(id);  //帖子id
        new AddPointClient(this, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<NormalDataBean>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(MsgDetailActivity.this, APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(NormalDataBean result) {
                        if (result != null && result.getCode() == 0) {
                            if (type == 1) { //区分帖子点赞和评论点赞
                                tv_msg_agreeNum.setText(ConvUtil.NI(tv_msg_agreeNum.getText().toString()) + 1 + "");
                                tv_msg_agreeNum.setTextColor(getResources().getColor(R.color.mainColor));
                                iv_msg_agree.setBackgroundResource(R.drawable.msg_detail_disagree);
                                rl_clickAgree.setEnabled(false);
                            } else {
                                if (listAdapter != null) {
                                    agreeList.add(new Integer(position));
                                    listAdapter.setList(list, agreeList);
                                }
                            }
                        } else {
                            ToastUtil.show(MsgDetailActivity.this, result.getMsg());
                        }
                    }
                });
    }

    // 发送评论
    private void sendComment() {
        AddCommentBodyBean bodyBean = new AddCommentBodyBean();
        bodyBean.setToken(StoreApplication.token);
        bodyBean.setToMatterCode(msgId);
        bodyBean.setContent(et_content.getText().toString().trim());
        if (msgType == 1) {//攻略6  求助7 投票8
            bodyBean.setValue(MSG_TYPE_GL);
        } else if (msgType == 2) {
            bodyBean.setValue(MSG_TYPE_QZ);
        } else if (msgType == 3) {
            bodyBean.setValue(MSG_TYPE_TP);
        } else {
            bodyBean.setValue(MSG_TYPE_GL);
        }
        new AddCommentClient(this, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<NormalDataBean>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(MsgDetailActivity.this, APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(NormalDataBean result) {
                        if (result != null && result.getCode() == 0) {
                            et_content.setText("");
                            pullListView.setPullLoadEnabled(true);
                            pageAction.setCurrentPage(0);
                            runService();
                        } else {
                            ToastUtil.show(MsgDetailActivity.this, result.getMsg());
                        }
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right); //页面关闭时动画
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listAdapter = null;
        gradAdapter = null;
        agreeList = null;
    }
}
