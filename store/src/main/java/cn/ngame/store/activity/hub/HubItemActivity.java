package cn.ngame.store.activity.hub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.AddPointBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.MsgDetailBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.NormalDataBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.PostDetailBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.AddPointClient;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostDetailClient;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.zan.HeartLayout;


/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class HubItemActivity extends BaseFgActivity {
    private HubItemActivity content;
    private int msgId = 0;
    private TextView mTitleTv, mFromTv, mDescTv, mTimeTv, mHubNameTv, mWatchNum, mSupportNumTv;
    private ImageView mSupportBt;
    private HeartLayout heartLayout;
    private SimpleDraweeView mFromIcon, mHubImageView;
    private PostDetailBean.DataBean.ShowPostCategoryBean hubInfo;
    private RelativeLayout hubLayout;
    private LinearLayout imageLayout;
    private List<PostDetailBean.DataBean.PostImageListBean> postImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initStatusBar();
        setContentView(R.layout.activity_game_hub_detail);
        initView();
        try {
            msgId = getIntent().getIntExtra(KeyConstant.ID, 0);
        } catch (Exception e) {
        }
        content = this;
        //请求数据
        getData();
    }

    private void initView() {
        mFromTv = findViewById(R.id.hub_detail_from_tv);
        mFromIcon = findViewById(R.id.game_hub_sdv);
        mTitleTv = findViewById(R.id.game_hub_detail_title_tv);
        mDescTv = findViewById(R.id.game_hub_detail_desc_tv);
        mTimeTv = findViewById(R.id.hub_detail_time_tv);
        mSupportNumTv = findViewById(R.id.game_hub_support_tv);
        mSupportBt = findViewById(R.id.game_hub_support_bt);
        mWatchNum = findViewById(R.id.see_numb_tv);
        mHubNameTv = findViewById(R.id.hub_detail_hub_name_tv);
        mHubImageView = findViewById(R.id.hub_detail_hub_iv);
        heartLayout = findViewById(R.id.heart_layout);
        hubLayout = findViewById(R.id.hub_detail_hub_layout);

        imageLayout = findViewById(R.id.hub_item_layout);
    }

    protected static final String TAG = HubItemActivity.class.getSimpleName();

    private void setMsgDetail(PostDetailBean result) {
        PostDetailBean.DataBean data = result.getData();
        Log.d(TAG, "获取单条数据: ==" + data);
        if (data == null) {
            return;
        }
        mFromIcon.setImageURI(data.getPostRoleHeadPhoto());
        mFromTv.setText(data.getPostRoleName());
        mTitleTv.setText(data.getPostTitle());
        mTimeTv.setText(String.valueOf(DateUtils.getRelativeTimeSpanString(
                data.getUpdateTime())).replace(" ", ""));
        mDescTv.setText(data.getPostContent());
        mSupportNumTv.setText(data.getPointNum() + "赞");
        mWatchNum.setText(String.valueOf(data.getWatchNum()));
        hubInfo = data.getShowPostCategory();
        if (hubInfo != null) {
            mHubNameTv.setText(hubInfo.getPostCategoryName());
            mHubImageView.setImageURI(hubInfo.getPostCategoryUrl());
            hubLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra(KeyConstant.postId, hubInfo.getId());
                    intent.setClass(content, CircleActivity.class);
                    startActivity(intent);
                }
            });
        }
        postImageList = data.getPostImageList();
        LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.dm300));
        pointParams.topMargin = getResources().getDimensionPixelSize(R.dimen.dm010);
        if (postImageList != null) {
            for (int i = 0; i < postImageList.size(); i++) {
                SimpleDraweeView iv = new SimpleDraweeView(content);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv.setLayoutParams(pointParams);
                iv.setImageURI(postImageList.get(i).getPostImageAddress());

                imageLayout.addView(iv);
            }
        }

        if (data.getIsPoint() == 1) {
            mSupportBt.setBackgroundResource(R.drawable.zan);
            mSupportNumTv.setTextColor(ContextCompat.getColor(content, R.color.mainColor));
            mSupportBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(content, "已经点过赞了哦~");
                    //heartLayout.addFavor();
                }
            });
        } else {
            mSupportBt.setBackgroundResource(R.drawable.un_zan);
            mSupportBt.setEnabled(true);
            mSupportBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAgree(1, msgId);
                    heartLayout.addFavor();
                }
            });
        }

    }

    /**
     * @param type 1表示帖子点赞，2表示评论点赞，3表示投票
     * @param id   帖子id/评论id
     */
    public void clickAgree(final int type, int id) {
        mSupportBt.setEnabled(false);
        //帖子id
        AddPointBodyBean bodyBean = new AddPointBodyBean();
        User user = StoreApplication.user;
        if (user != null) {
            bodyBean.setUserCode(user.userCode);
        } else {
            bodyBean.setDeviceOnlyNum(StoreApplication.deviceId);
        }
        bodyBean.setAppTypeId(Constant.APP_TYPE_ID_0_ANDROID);
        bodyBean.setPostId(id);  //帖子id
        new AddPointClient(this, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<NormalDataBean>() {
                    @Override
                    public void onError(Throwable e) {
                        mSupportBt.setEnabled(true);
                        ToastUtil.show(content, "点赞失败哦,请稍后重试~");
                    }

                    @Override
                    public void onNext(NormalDataBean result) {
                        if (content == null || content.isFinishing()) {
                            return;
                        }
                        mSupportBt.setEnabled(true);
                        if (result != null && result.getCode() == 0) {
                            if (type == 1) { //区分帖子点赞和评论点赞
                                ToastUtil.show(content, "点赞成功~");
                                mSupportNumTv.setText(ConvUtil.NI(mSupportNumTv.getText().toString()) + 1 + "赞");
                                mSupportNumTv.setTextColor(ContextCompat.getColor(content, R.color.mainColor));
                                mSupportBt.setBackgroundResource(R.drawable.zan);
                                mSupportBt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ToastUtil.show(content, "已经点过赞了哦~");
                                        //heartLayout.addFavor();
                                    }
                                });
                            } else {
                            }
                        } else {
                            ToastUtil.show(content, "点赞失败哦,请稍后重试~");
                        }
                    }
                });
    }

    /**
     * 获取游戏详情
     */
    private void getData() {
        MsgDetailBodyBean bodyBean = new MsgDetailBodyBean();
        User user = StoreApplication.user;
        if (user != null) {
            bodyBean.setUserCode(user.userCode);
        } else {
            bodyBean.setDeviceOnlyNum(StoreApplication.deviceId);
        }
        bodyBean.setPostId(msgId);
        bodyBean.setAppTypeId(Constant.APP_TYPE_ID_0_ANDROID);
        new PostDetailClient(this, bodyBean).observable()
                .subscribe(new ObserverWrapper<PostDetailBean>() {
                    @Override
                    public void onError(Throwable e) {
                        //("服务器开小差咯~");
                    }

                    @Override
                    public void onNext(PostDetailBean result) {
                        if (result != null && result.getCode() == 0) {
                            setMsgDetail(result);
                        } else {
                            //ToastUtil.show(content, "获取失败");
                            // mDescTv.setText("获取失败~");
                        }
                    }
                });
    }

    public void onHubItemBackClick(View view) {
        finish();
    }
}
