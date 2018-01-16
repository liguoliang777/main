package cn.ngame.store.activity.hub;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.AddPointBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.MsgDetailBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.MsgDetailBodyBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.NormalDataBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.AddPointClient;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.GameInfo;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.Token;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.UMEventNameConstant;
import cn.ngame.store.game.presenter.HomeFragmentChangeLayoutListener;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.ReviewScoreView;
import cn.ngame.store.view.StickyScrollView;
import cn.ngame.store.view.zan.HeartLayout;
import cn.ngame.store.widget.TouchImageView;


/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class HubItemOldActivity extends BaseFgActivity implements StickyScrollView.OnScrollChangedListener,
        HomeFragmentChangeLayoutListener {
    private RelativeLayout rl_top, rl_top2;
    private StickyScrollView scrollView;
    private Button leftBt;
    private HubItemOldActivity content;
    //游戏id
    private int msgId = 0;
    private GameInfo gameInfo;
    //下载进度条
    private Timer timer = new Timer();
    private String gameName = "";
    private ReviewScoreView reviewScoreView;
    private float tabTextSize;
    private RatingBar ratingBarBig;
    private float rate;
    private TextView sumbitTv;
    private boolean isPrecented = false;
    private FragmentManager fm;
    private TextView picNubSeletedTv;
    private TextView mFromTv;
    private TextView mTitleTv;
    private TextView mDescTv;
    private TextView mSupportNumTv;
    private ImageView mSupportImageView;
    private HeartLayout heartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);
        }
        main = getLayoutInflater().inflate(R.layout.activity_game_hub_detail, null);
        setContentView(main);
        initView();
        try {
            msgId = getIntent().getIntExtra(KeyConstant.ID, 0);
        } catch (Exception e) {
        }
        content = this;
        fm = getSupportFragmentManager();
        tabTextSize = CommonUtil.dip2px(content, 16f);

        //初始化
        initStatus();
        initTabViewPager();
        //请求数据
        getData();
        initBanner();
    }

    private void initView() {
        mFromTv = (TextView) findViewById(R.id.hub_detail_from_tv);
        mTitleTv = (TextView) findViewById(R.id.game_hub_detail_title_tv);
        mDescTv = (TextView) findViewById(R.id.game_hub_detail_desc_tv);
        mSupportNumTv = (TextView) findViewById(R.id.game_hub_support_tv);
        mSupportImageView = (ImageView) findViewById(R.id.game_hub_support_bt);
        heartLayout = (HeartLayout) findViewById(R.id.heart_layout);
    }

    private void setMsgDetail(MsgDetailBean result) {
        MsgDetailBean.DataBean data = result.getData();
        if (data == null) {
            return;
        }
        mFromTv.setText(data.getPostPublisher());
        gameName = data.getPostTitle();
        mTitleTv.setText(gameName);
        mDescTv.setText(data.getPostContent());
        mSupportNumTv.setText(data.getPointCount() + "");


        String postImage = data.getPostImage();
        if (postImage != null) {
            String[] typeNameArray = postImage.split("\\,");
            for (int i = 0; i < typeNameArray.length; i++) {
                imgs.add(typeNameArray[i]);
            }
        }
        if (data.getIsPoint() == 1) {//已经点赞
            mSupportImageView.setBackgroundResource(R.drawable.zan);
            mSupportImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(content, "已经点过赞了哦~");
                    //heartLayout.addFavor();
                }
            });
        } else {
            mSupportImageView.setBackgroundResource(R.drawable.un_zan);
            mSupportImageView.setEnabled(true);
            mSupportImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickAgree(1, msgId);
                    heartLayout.addFavor();
                }
            });
        }

        new LoadImageThread().start();
    }

    /**
     * @param type 1表示帖子点赞，2表示评论点赞，3表示投票
     * @param id   帖子id/评论id
     */
    public void clickAgree(final int type, int id) {
        mSupportImageView.setEnabled(false);
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
                        mSupportImageView.setEnabled(true);
                        ToastUtil.show(content, "点赞失败哦,请稍后重试~");
                    }

                    @Override
                    public void onNext(NormalDataBean result) {
                        if (content == null || content.isFinishing()) {
                            return;
                        }
                        mSupportImageView.setEnabled(true);
                        if (result != null && result.getCode() == 0) {
                            if (type == 1) { //区分帖子点赞和评论点赞
                                ToastUtil.show(content, "点赞成功~");
                                mSupportNumTv.setText(ConvUtil.NI(mSupportNumTv.getText().toString()) + 1 + "");
                                mSupportImageView.setBackgroundResource(R.drawable.zan);
                                mSupportImageView.setOnClickListener(new View.OnClickListener() {
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

    private int initLocal = 0;
    private ViewGroup pointGroup;
    private ViewPager viewPager;
    private ArrayList<View> imgViews;

    private void initBanner() {
        viewPager = (ViewPager) main.findViewById(R.id.imagePages);
        //picNubSeletedTv = (TextView) main.findViewById(R.id.pic_nub_seleted_show_tv);
        pointGroup = (ViewGroup) main.findViewById(R.id.pointGroup);
    }

    private ArrayList<String> imgs = new ArrayList<>();
    TouchImageView iv;

    public void onHubLayoutClick(View view) {
        ToastUtil.show(content,"点击圈子");
    }

    class LoadImageThread extends Thread {
        @Override
        public void run() {
            //3. 获取LayoutInflater,目的是方便后面得到xml布局文件进行装配
            //4. 创建图片view存储集合
            imgViews = new ArrayList<>();

            Looper.prepare();
            //5. 通过网络方式下载图片,并最终放在集合中
            for (int i = 0; i < imgs.size(); i++) {
                iv = new TouchImageView(content);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                Bitmap bitmap = getHttpBitmap(imgs.get(i));
                //从网上取图片
                iv.setImageBitmap(bitmap);
                imgViews.add(iv);

                iv.setOnImageClickListener(new TouchImageView.OnClickListener() {
                    @Override
                    public void onClick() {
                        //图片点击
                    }
                });
            }
            //6. 获取main.xml layout对象,他是装配其他图片布局的中心点
            //   要记得,它里面声明了一个图片区域ViewPager,以及一个导航指示区域
            // main = getLayoutInflater().inflate(R.layout.activity_show_view, null);
            //9. 下面开始控制导航小圆点,有多少张img,就要做多大的小圆点数组
       /*     pointViews = new ImageView[imgViews.size()];

            if (content == null && content.isFinishing()) {
                return;
            }
            //10. 根据图片集合的长度决定创建多少小圆点ImageView
            for (int i = 0; i < imgViews.size(); i++) {
//                imageView = new ImageView(ShowViewActivity.this);
//                imageView.setLayoutParams(new LayoutParams(15, 15));
//                imageView.setPadding(20, 0, 20, 0);
                LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(
                        ViewPager.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);
                if (i < 1) {
                    pointParams.setMargins(0, 0, 0, 0);
                } else {
                    pointParams.setMargins(10, 0, 0, 0);
                }
                ImageView iv = new ImageView(content);
                iv.setLayoutParams(pointParams);
                pointViews[i] = iv;
                if (i == 0) {
                    //默认选中第一张图片,加入焦点
                    pointViews[i].setBackgroundResource(R.drawable.choosen_radius);
                } else {
                    pointViews[i].setBackgroundResource(R.drawable.white_radius);
                }
                // 把每一个导航小圆点都加入到ViewGroup中
                pointGroup.addView(pointViews[i]);
            }*/
            //表示下载完毕.
            if (content != null && !content.isFinishing()) {
                handler.sendEmptyMessage(0);
            }
            Looper.loop();
        }
    }

    public Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL
                    .openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            // 连接设置获得数据流
            conn.setDoInput(true);
            // 不使用缓存
            conn.setUseCaches(false);
            // 这句可有可无，没有影响
            // conn.connect();
            // 得到数据流
            InputStream is = conn.getInputStream();
            // 解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            // 关闭数据流
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // adapter内部类
    // 指引页面数据适配器
    class ViewPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            // 通过集合的size告诉Adapter共有多少张图片
            return imgViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            // TODO Auto-generated method stub
            // 移除旧的View
            ((ViewPager) arg0).removeView(imgViews.get(arg1));
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            // TODO Auto-generated method stub
            // 获取新的view
            ((ViewPager) arg0).addView(imgViews.get(arg1));
            return imgViews.get(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub
        }
    }

    // 监听器内部类
    // 指引页面更改事件监听器
    class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int arg0) {
         /*   pointViews[arg0].setBackgroundResource(R.drawable.choosen_radius);
            int picNum = pointViews.length;//总数

            for (int i = 0; i < picNum; i++) {
                if (arg0 != i) {
                    pointViews[i].setBackgroundResource(R.drawable.white_radius);
                }
            }*/
            int imgsSize = imgs.size();
            if (imgsSize > 1) {
                picNubSeletedTv.setText(arg0 + 1 + "/" + imgsSize);
            } else {
                picNubSeletedTv.setText("");
            }
        }
    }

    private View main;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    //setContentView(main);
                    int imgsSize = imgs.size();
                    if (imgsSize > 1) {
                        picNubSeletedTv.setText(1 + "/" + imgsSize);
                    } else {
                        picNubSeletedTv.setText("");
                    }
                    //12. 设置viewPager 图片切换Adapter,图片最终能够切换就是在Adapter中实现的
                    viewPager.setAdapter(new ViewPagerAdapter());
                    //13. 设置viewPager 页面改变监听器,利用监听器改变小圆点焦点状态
                    viewPager.setOnPageChangeListener(new PagerChangeListener());
                    //14. 根据需求 初始位置
                    viewPager.setCurrentItem(initLocal);
                    break;
                }
            }
        }
    };
    private String TAG = HubItemOldActivity.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();
        hasStop = false;
    }

    boolean hasStop;

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
        bodyBean.setId(msgId);
        bodyBean.setType(1);
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        float height = 400f;
        if (t < height) {
            float alpha = (height - t) / height;
            if (t < 4) {
                rl_top.setBackgroundResource(R.color.transparent);
                rl_top.setAlpha(1f);
                leftBt.setText("");
            } else {
                rl_top.setAlpha(1 - alpha);
                int color = 1 - alpha > 0 ? R.color.mainColor : R.color.transparent;
                rl_top.setBackgroundResource(color);
                leftBt.setText(gameName);
            }
        } else {
            rl_top.setAlpha(1f);
            leftBt.setText(gameName);
            rl_top.setBackgroundResource(R.color.mainColor);
        }
        scrollView.setStickTop(rl_top.getMeasuredHeight());//设置距离多少悬浮
    }

    @Override
    public void changeLayoutHeight(int height) {
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT);
        layoutParams.height = height;
    }


    private void initStatus() {
        //获取状态栏高度设置给标题栏==========================================
        rl_top = (RelativeLayout) main.findViewById(R.id.rl_top);
        rl_top.setBackgroundResource(R.color.transparent);
        int statusBarHeight = ImageUtil.getStatusBarHeight(content) - 5;
        rl_top.setPadding(0, statusBarHeight, 0, 0);
        //======================================================================
        leftBt = (Button) main.findViewById(R.id.left_bt);
        leftBt.setPadding(CommonUtil.dip2px(content, 18), statusBarHeight, 0, 0);
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //初始化TabLayout   &   ViewPager
    private void initTabViewPager() {
        scrollView = (StickyScrollView) main.findViewById(R.id.scrollView);
        scrollView.setOnScrollListener(this);
    }

    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int UPDATE_TIPS_OR_DOWNLOAD_EXCEPTION = 1;
    private View.OnClickListener gameDetailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
          /*      case R.id.game_detail_feedback_bt:
                    if (CommonUtil.isLogined()) {
                    } else {
                        CommonUtil.showUnLoginDialog(fm, content, R.string.unlogin_msg);
                    }
                    break;*/
            }
        }
    };

    //反馈
    public void showFeedbackDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog_feedback, null);

        View.OnClickListener mDialogClickLstener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                int id = v.getId();
                HashMap<String, String> map = new HashMap<>();
                map.put(KeyConstant.game_Name, gameName);
                if (id == R.id.choose_01_tv) {
                    UPDATE_TIPS_OR_DOWNLOAD_EXCEPTION = 1;
                    //数据埋点  -- 游戏更新提醒
                    MobclickAgent.onEvent(content, UMEventNameConstant.FeedBack_UpdateRemind,
                            map);
                } else if (id == R.id.choose_02_tv) {//下载异常\
                    UPDATE_TIPS_OR_DOWNLOAD_EXCEPTION = 2;
                    MobclickAgent.onEvent(content, UMEventNameConstant.FeedBack_DownloadException,
                            map);
                }
                ToastUtil.show(content, "反馈成功");
                //提交反馈
                if (gameInfo != null) {
                    //postFeedBack();
                } else {
                    ToastUtil.show(content, "网络异常,请稍后重试.");
                }
            }
        };
        inflate.findViewById(R.id.choose_01_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_02_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_cancel_tv).setOnClickListener(mDialogClickLstener);

        dialog.setContentView(inflate);//将布局设置给Dialog
        setDialogWindow(dialog);
    }

    private void setDialogWindow(Dialog dialog) {
        Window dialogWindow = dialog.getWindow(); //获取当前Activity所在的窗体
        dialogWindow.setGravity(Gravity.BOTTOM);//设置Dialog从窗体底部弹出
        WindowManager.LayoutParams params = dialogWindow.getAttributes();   //获得窗体的属性
        //params.y = 20;  Dialog距离底部的距离
        params.width = WindowManager.LayoutParams.MATCH_PARENT;//设置Dialog距离底部的距离
        dialogWindow.setAttributes(params); //将属性设置给窗体
        dialog.show();//显示对话框
    }

    //游戏反馈
    private void postFeedBack() {
        final DialogHelper dialogHelper = new DialogHelper(getSupportFragmentManager(), content);
        dialogHelper.showAlert("提交反馈", true);
        String url = Constant.WEB_SITE + Constant.URL_SUBMIT_FEED_BACK_V4;
        Response.Listener<JsonResult> successListener = new Response.Listener<JsonResult>() {
            @Override
            public void onResponse(JsonResult result) {
                if (dialogHelper != null) {
                    dialogHelper.hideAlert();
                }
                if (result == null) {
                    ToastUtil.show(content, "反馈失败,服务器异常,请稍后重试.");
                    return;
                }
                if (result.code == 0) {
                    ToastUtil.show(content, "反馈成功");
                } else {
                    ToastUtil.show(content, "反馈失败");
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                if (dialogHelper != null) {
                    dialogHelper.hideAlert();
                }
                ToastUtil.show(content, "反馈失败," + getString(R.string.server_exception));
            }
        };

        Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //{"userCode":"UC1500609205627","msgId":146,"appTypeId":0}
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.APP_TYPE_ID, Constant.APP_TYPE_ID_0_ANDROID);
                params.put(KeyConstant.USER_CODE, StoreApplication.userCode);
                params.put(KeyConstant.GAME_ID, String.valueOf(msgId));
                params.put(KeyConstant.gameVersion, gameInfo.versionName);
                params.put(KeyConstant.brand, Build.BRAND);//手机品牌
                params.put(KeyConstant.model, Build.MODEL);//手机型号
                params.put(KeyConstant.bluetooth, "");//蓝牙
                params.put(KeyConstant.system, Build.VERSION.RELEASE);//系统
                params.put(KeyConstant.cpu, Build.CPU_ABI);//蓝牙
                params.put(KeyConstant.isSupportOTG, "0");//是否支持OTG（0表示不支持，1表示支持）
                params.put(KeyConstant.updateOrDownloadTips, String.valueOf(UPDATE_TIPS_OR_DOWNLOAD_EXCEPTION));
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    //提示绑定对话框
    public void showPercentDialog() {
        final Dialog mUnboundDialog = new Dialog(content);
        mUnboundDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //填充对话框的布局
        View percentView = LayoutInflater.from(content).inflate(R.layout.layout_percentage_dialog, null);

        //用户头像+昵称
        SimpleDraweeView iconIv = (SimpleDraweeView) percentView.findViewById(R.id.ic_percent_icon);
        TextView nameTv = (TextView) percentView.findViewById(R.id.ic_percent_user_name);
        iconIv.setImageURI(StoreApplication.userHeadUrl);
        nameTv.setText(StoreApplication.nickName);

        reviewScoreView = (ReviewScoreView) percentView.findViewById(R.id.review_scoreView);
        ratingBarBig = (RatingBar) percentView.findViewById(R.id.rating_bar);
        reviewScoreView.setData(gameInfo);

        sumbitTv = (TextView) percentView.findViewById(R.id.ic_percent_sumbit_bt);
        sumbitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取评分的值
                rate = ratingBarBig.getRating();
                if (rate <= 0) {
                    ToastUtil.show(content, "请点击星星打分哦!");
                    return;
                }
                //评分成功
                ToastUtil.show(content, "评分提交成功!");
                isPrecented = true;
                mUnboundDialog.dismiss();
                //提交评分
                //submitPercent(mUnboundDialog);

                //评分数据埋点
                HashMap<String, String> map = new HashMap<>();
                map.put(KeyConstant.game_Name, gameName);
                MobclickAgent.onEvent(content, UMEventNameConstant.GameGrade,
                        map);
            }
        });
        if (isPrecented) {
            sumbitTv.setText(getString(R.string.precented));
            sumbitTv.setClickable(false);
            sumbitTv.setBackgroundResource(R.drawable.shape_corner12px_cccccc);
        }
        mUnboundDialog.setContentView(percentView);//将布局设置给Dialog
        Window dialogWindow = mUnboundDialog.getWindow(); //获取当前Activity所在的窗体
        dialogWindow.setBackgroundDrawableResource(R.color.transparent);
        //dialogWindow.setGravity(Gravity.CENTER);//设置Dialog从窗体顶部弹出
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = getResources().getDimensionPixelSize(R.dimen.dm445);
        dialogWindow.setAttributes(params); //将属性设置给窗体
        if (content != null && !mUnboundDialog.isShowing()) {
            mUnboundDialog.show();//显示对话框
        }
    }

    //评论
    private void submitPercent(final Dialog mUnboundDialog) {
        String url = Constant.WEB_SITE + Constant.URL_COMMENT_ADD_COMMENT;
        Response.Listener<JsonResult<Token>> successListener = new Response.Listener<JsonResult<Token>>() {
            @Override
            public void onResponse(JsonResult<Token> result) {
                DialogHelper.hideWaiting(getSupportFragmentManager());
                if (result == null) {
                    ToastUtil.show(content, "评分提交失败,服务端异常");
                    return;
                }
                if (result.code == 0) {
                    ToastUtil.show(content, "评分提交成功!");
                    if (content != null && mUnboundDialog != null && mUnboundDialog.isShowing()) {
                        mUnboundDialog.dismiss();
                    }
                } else {
                    ToastUtil.show(content, result.msg);
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                ToastUtil.show(content, "评论失败，请稍候重试!");
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<Token>> versionRequest = new GsonRequest<JsonResult<Token>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<Token>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.GAME_ID, "msgId");
                params.put(KeyConstant.VALUE, (int) Math.floor(rate) + ""); //评分值
                params.put(KeyConstant.TOKEN, StoreApplication.token);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != timer) {
            timer.cancel();
        }
        hasStop = true;
    }
}
