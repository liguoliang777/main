package cn.ngame.store.activity.main;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.AppCarouselBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.BrowseHistoryBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.AppCarouselClient;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.activity.classify.ClassifyFragment;
import cn.ngame.store.activity.hub.HubFragment;
import cn.ngame.store.activity.hub.HubPostsActivity;
import cn.ngame.store.activity.manager.DownloadCenterActivity;
import cn.ngame.store.activity.manager.ManagerFragment;
import cn.ngame.store.activity.rank.RankActivity;
import cn.ngame.store.activity.rank.RankFragment;
import cn.ngame.store.activity.sm.AboutNgameZoneActivity;
import cn.ngame.store.activity.sm.AdCooperativeActivity;
import cn.ngame.store.activity.sm.JoypadSettingsActivity;
import cn.ngame.store.activity.sm.SystemSettingsActivity;
import cn.ngame.store.adapter.FragmentViewPagerAdapter;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.VersionInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.FileLoadService;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.AppInstallHelper;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.LoginHelper;
import cn.ngame.store.core.utils.TextUtil;
import cn.ngame.store.core.utils.UMEventNameConstant;
import cn.ngame.store.exception.NoSDCardException;
import cn.ngame.store.fragment.SimpleDialogFragment;
import cn.ngame.store.game.view.GameDetailActivity;
import cn.ngame.store.local.model.IWatchRecordModel;
import cn.ngame.store.local.model.WatchRecordModel;
import cn.ngame.store.ota.model.OtaService;
import cn.ngame.store.push.model.IPushMessageModel;
import cn.ngame.store.push.model.PushMessage;
import cn.ngame.store.push.model.PushMessageModel;
import cn.ngame.store.push.view.MessageDetailActivity;
import cn.ngame.store.push.view.MsgCenterActivity;
import cn.ngame.store.push.view.NotifyMsgDetailActivity;
import cn.ngame.store.search.view.SearchActivity;
import cn.ngame.store.user.view.LoginActivity;
import cn.ngame.store.user.view.UserCenterActivity;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.view.DialogModel;


/**
 * 首页底部tab栏
 * Created by gp on 2017/3/14 0014.
 */
@SuppressLint("WrongConstant")
public class MainHomeActivity extends BaseFgActivity implements View.OnClickListener {
    public String TAG = MainHomeActivity.class.getSimpleName();
    private static MainHomeActivity context;
    private boolean isExit = false;     //是否安装后第一次启动
    private IFileLoad fileLoad;
    private Timer timer = new Timer();
    private Handler handler = new Handler();
    private NumberProgressBar progressBar;
    private RemoteViews remoteViews = null;
    private Notification notification = null;
    private NotificationManager mNotificationManager = null;
    private VersionInfo versionInfo = null;
    private boolean isRunningBackground = false;
    private boolean isDownloading = false;
    private boolean isChecking = false;
    private RecommendFragment selectedFragment;
    private RankFragment rankingFragment;
    private ClassifyFragment discoverFragment;
    private ManagerFragment administrationFragment;
    private int currentMenu;
    private FragmentViewPagerAdapter adapter;
    private FragmentManager fragmentManager;
    private LinearLayout home, menu_game_hub, video, manager;
    private Button bt_home, bt_game, bt_video, bt_manager;
    private TextView tv_home, tv_video, tv_manager, mEditProfileTv, tv_notifi_num, menu_gamehub_tv, mSmNicknameTv, mTitleTv;
    private int colorDark;
    private int colorNormal;
    private String imgUrl;
    private List<Fragment> mfragmentlist = new ArrayList<>();
    private int rbIndex;
    private ImageView im_toSearch, mRankBt;
    private FrameLayout fl_notifi;
    private SimpleDraweeView mIconIv;
    private String pwd;
    private SlidingMenu mSlidingMenu;
    private SimpleDraweeView mSmIconIv;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ImageView mDownloadBt, mHubBt;
    private Button menu_game_hub_bt;
    private HubFragment gameHubFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //InMobiSdk.init(this, Constant.InMobiSdk_Id);
        //........ ....................通知栏  >= 4.4(KITKAT)...................
        Intent serviceIntent = new Intent(this, FileLoadService.class);
        startService(serviceIntent);
        //-----------------------------------------------------------------------------
        setContentView(R.layout.activity_main_home);
        context = this;
        initStatusBar();
        //首页弹出广告dialog
        // showAdverDialog();
        //得到设备id
        CommonUtil.verifyStatePermissions(this);
        preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        editor = preferences.edit();
//        mfragmentlist = getFragmentList();
//        FragmentManager FragmentManager = getSupportFragmentManager();
//        mTransaction = FragmentManager.beginTransaction();
//        mTransaction.add(R.id.main_list_fragments, mfragmentlist.get(0));
//        mTransaction.commit();
//        rbIndex = 0;

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager.setOffscreenPageLimit(5);

        //初始化底部菜单控件
   /*     rl_top = (RelativeLayout) findViewById(R.id.main_home_title_rlay);
        rl_top.setBackgroundResource(R.color.transparent);
        int statusBarHeight = ImageUtil.getStatusBarHeight(context);
        rl_top.setPadding(0, statusBarHeight, 0, 0);*/
        home = (LinearLayout) findViewById(R.id.menu_home_ll);
        //game = (LinearLayout) findViewById(R.id.menu_game_ll);
        menu_game_hub = (LinearLayout) findViewById(R.id.menu_game_hub);
        video = (LinearLayout) findViewById(R.id.menu_video);
        manager = (LinearLayout) findViewById(R.id.menu_manager);

        bt_home = (Button) findViewById(R.id.menu_home_bt1);
        //bt_game = (Button) findViewById(R.id.menu_game_bt);
        bt_video = (Button) findViewById(R.id.menu_video_bt);
        bt_manager = (Button) findViewById(R.id.menu_manager_bt);

        tv_home = (TextView) findViewById(R.id.menu_home_tv);
        //tv_game = (TextView) findViewById(R.id.menu_game_tv);
        menu_gamehub_tv = (TextView) findViewById(R.id.menu_gamehub_tv);
        tv_video = (TextView) findViewById(R.id.menu_video_tv);
        tv_manager = (TextView) findViewById(R.id.menu_manager_tv);
        menu_game_hub_bt = (Button) findViewById(R.id.menu_game_hub_bt);

        //标题上面的消息和搜索
        im_toSearch = (ImageView) findViewById(R.id.im_toSearch);
        fl_notifi = (FrameLayout) findViewById(R.id.fl_notifi);
        tv_notifi_num = (TextView) findViewById(R.id.tv_notifi_num); //右上角消息数目

        mIconIv = (SimpleDraweeView) findViewById(R.id.iv_icon_title);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mDownloadBt = findViewById(R.id.main_download_bt);
        mHubBt = findViewById(R.id.main_hub_bt);
        mRankBt = findViewById(R.id.main_rank_bt);
        im_toSearch.setOnClickListener(this);
        fl_notifi.setOnClickListener(this);
        mIconIv.setOnClickListener(this);
        mDownloadBt.setOnClickListener(this);
        mHubBt.setOnClickListener(this);
        mRankBt.setOnClickListener(this);

        colorDark = getResources().getColor(R.color.mainColor);
        colorNormal = getResources().getColor(R.color.color_333333);

//        init(viewPager, getSupportFragmentManager());
        fragmentManager = getSupportFragmentManager();
        setCurrentMenu(0);    //当前选中标签

        setOnTouchListener(this.new MenuOnTouchListener());

        pwd = StoreApplication.passWord;
        //如果用户没有主动退出，则重新登录
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtil.isEmpty(pwd)) {
                    LoginHelper loginHelper = new LoginHelper(context);
                    loginHelper.reLoadSP();
                }
            }
        }).start();

        //申请SD卡读写权限
        CommonUtil.verifyStoragePermissions(this);

        fileLoad = FileLoadManager.getInstance(this);
        //判断是否有新版本APP
        checkUpdate();

        //同步本地观看记录到服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                IWatchRecordModel watchRecordModel = new WatchRecordModel(context);
                watchRecordModel.synchronizeWatchRecord();
            }
        }).start();

        if (StoreApplication.isReceiveMsg) {
            //启动百度推送
            //PushSettings.enableDebugMode(this, true);       //打开debug开关
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, Constant.PUSH_API_KEY);
            PushManager.disableLbs(this);   //关闭精确LBS推送模式
        }
        //判断App是否从通知栏消息进来，如果是，直接启动消息详情页
        isFromNotification();

        //侧边栏
        initSlidingMenu();
        //头像(只能放在最后)
        setUserIcon();

     /*   if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.READ_LOGS,
             Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest
                    .permission.SYSTEM_ALERT_WINDOW, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 777);
        }*/
        //Intent intent = new Intent(this, SplashFullscreenActivity.class);
        //startActivity(intent);
    }

    /*  @Override
      public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
          android.util.Log.d(TAG, "onRequest6.0PermissionsResult: " + requestCode);

      }*/
    //DisplayImageOptions roundOptions = FileUtil.getRoundOptions(R.drawable.ic_def_logo_188_188, 360);

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    //侧边栏
    private void initSlidingMenu() {
        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸屏幕的模式
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);//右边空白区可以触摸
        // mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//右边全屏幕可以触摸

        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);//阴影宽度
        mSlidingMenu.setShadowDrawable(R.color.semitransparent);
        mSlidingMenu.setBehindWidthRes(R.dimen.sliding_menu_offset); // 菜单右边宽度
        mSlidingMenu.setFadeEnabled(false);//是否有渐变
        mSlidingMenu.setFadeDegree(0.35f);     // 设置 左边菜单 渐入渐出效果的值
        mSlidingMenu.setOffsetFadeDegree(0.4f);// 设置 右边 渐入渐出效果的值

        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        mSlidingMenu.setMenu(R.layout.left_menu);

        findSMViewById(R.id.sm_system_settings);
        findSMViewById(R.id.sm_joypad_settings);
        findSMViewById(R.id.sm_about_us);
        findSMViewById(R.id.sm_ad);

        mSmIconIv = (SimpleDraweeView) findViewById(R.id.top_icon_sdv);
        mSmNicknameTv = (TextView) findViewById(R.id.sm_top_nikename_tv);
        mEditProfileTv = (TextView) findViewById(R.id.edit_profile_click);
        mSmIconIv.setOnClickListener(mSmClickLstener);
        mSmNicknameTv.setOnClickListener(mSmClickLstener);
        mEditProfileTv.setOnClickListener(mSmClickLstener);
        //int statusBarHeight = ImageUtil.getStatusBarHeight(this);
        /**
         7 设置SlidingMenu滑动的拖拽效果   slidingMenu.setBehindScrollScale(0);

         8 设置SlidingMenu判断打开状态 并 自动关闭或开启
         menu.toggle();
         如果SlidingMenu 它是open的，它会被关闭，反之亦然。

         9 设置SlidingMenu触碰屏幕的范围
         menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
         menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
         设置菜单滑动，触碰屏幕的范围setTouchModeAbove

         10 设置SlidingMenu关闭器监听
         监听主要分2种 open 和 close
         open:
         menu.setOnOpenedListener(onOpenListener);//监听slidingmenu打开之后调用
         menu.setOnOpenListener(onOpenListener);//监听slidingmenu打开时调用
         close:
         两个监听器 注意看了 一个是 closed 一个是 close
         menu.setOnClosedListener(listener);
         menu.setOnCloseListener(listener);

         这两个的区别就是
         menu.OnCloseListener(OnClosedListener);//监听Slidingmenu关闭时事件
         menu.OnClosedListener(OnClosedListener);//监听Slidingmenu关闭后事件
         */
        //menu.showMenu();//显示SlidingMenu
        //menu.showContent();//显示内容
    }

    private void findSMViewById(int id) {
        findViewById(id).setOnClickListener(mSmClickLstener);
    }

    View.OnClickListener mSmClickLstener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.sm_top_nikename_tv
                    || id == R.id.top_icon_sdv
                    || id == R.id.edit_profile_click
                    ) {//系统设置
                if (pwd != null && !"".endsWith(pwd) || !Constant.PHONE.equals(StoreApplication.loginType)) {
                    startActivity(new Intent(context, UserCenterActivity.class));
                } else {
                    startActivity(new Intent(context, LoginActivity.class));
                }
            } else if (id == R.id.sm_system_settings) {//系统设置
                Intent setIntent = new Intent(context, SystemSettingsActivity.class);
                startActivity(setIntent);
            } else if (id == R.id.sm_joypad_settings) {//手柄设置
                startActivity(new Intent(context, JoypadSettingsActivity.class));
            } else if (id == R.id.sm_about_us) {//关于Ngame
                startActivity(new Intent(context, AboutNgameZoneActivity.class));
            } else if (id == R.id.sm_ad) {//广告与合作
                startActivity(new Intent(context, AdCooperativeActivity.class));
            }

            //右上角消息状态
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mSlidingMenu != null && mSlidingMenu.isMenuShowing()) {
                                mSlidingMenu.toggle(false);
                            }
                        }
                    }, 300);
                }
            }).start();

            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);//左进,右出
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //主界面顶部头像
        boolean isAvatarChanged = preferences.getBoolean(KeyConstant.AVATAR_HAS_CHANGED, true);
        if (isAvatarChanged) {
            setUserIcon();
        }
        //右上角的消息
        new Thread(new Runnable() {
            @Override
            public void run() {
                IPushMessageModel pushModel = new PushMessageModel(context);
                final int count = pushModel.getUnReadMsgCount(0);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (count == 0) {
                            tv_notifi_num.setVisibility(View.GONE);
                        } else {
                            tv_notifi_num.setVisibility(View.VISIBLE);
                            tv_notifi_num.setText(count + "");
                        }
                    }
                });
            }
        }).start();
    }

    private void setUserIcon() {
        pwd = StoreApplication.passWord;
        if ((pwd != null && !"".endsWith(pwd)) || !Constant.PHONE.equals(StoreApplication.loginType)) {
            //DisplayImageOptions roundOptions = FileUtil.getRoundOptions(R.color.colorPrimary, 360);
            String userHeadUrl = StoreApplication.userHeadUrl;
            mIconIv.setImageURI(userHeadUrl);
            mSmIconIv.setImageURI(userHeadUrl);
            mSmNicknameTv.setText(StoreApplication.nickName);
            mEditProfileTv.setVisibility(View.VISIBLE);
        } else {
            mIconIv.setImageURI("");
            mSmIconIv.setImageURI("");
            mSmNicknameTv.setText("点击登录");
            mEditProfileTv.setVisibility(View.INVISIBLE);
        }
        editor.putBoolean(KeyConstant.AVATAR_HAS_CHANGED, false).commit();
    }
    //    private List<Fragment> getFragmentList() {
//        List<Fragment> fragmentlist = new ArrayList<>();
//        fragmentlist.add(SelectedFragment.newInstance(0));
//        fragmentlist.add(RankingFragment.newInstance(""));
//        fragmentlist.add(GameHubFragment.newInstance());
//        fragmentlist.add(ClassificationFragment.newInstance(""));
//        fragmentlist.add(AdministrationFragment.newInstance());
//        return fragmentlist;
//    }

    private void switchFragment(int i) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = mfragmentlist.get(i);
        if (!fragment.isAdded()) {
            transaction.hide(mfragmentlist.get(rbIndex)).add(R.id.main_list_fragments, fragment);
        } else {
            transaction.hide(mfragmentlist.get(rbIndex)).show(fragment);
        }
        transaction.commit();
        rbIndex = i;
    }

    //请求广告图片地址
    private void showAdverDialog() {
        BrowseHistoryBodyBean bean = new BrowseHistoryBodyBean();
        bean.setType(41);
        new AppCarouselClient(this, bean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<AppCarouselBean>() {
                    @Override
                    public void onError(Throwable e) {
//                        ToastUtil.show(MainHomeActivity.this, APIErrorUtils.getMessage(e));
                    }

                    @Override
                    public void onNext(AppCarouselBean result) {
                        if (result != null && result.getCode() == 0) {
                            showCarousel(result);
                        } else {
//                            ToastUtil.show(MainHomeActivity.this, result.getMsg());
                        }
                    }
                });
    }

    public void showCarousel(final AppCarouselBean result) {
        imgUrl = result.getData().get(0).getAdvImageLink();
        final DialogModel dialogModel = new DialogModel(context, imgUrl);
        dialogModel.show();
        dialogModel.sdv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogModel.dismiss();
                Intent intent = new Intent(context, GameDetailActivity.class);
                intent.putExtra("id", result.getData().get(0).getGameId());
                startActivity(intent);
            }
        });
        dialogModel.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogModel.dismiss();
            }
        });
    }

    /**
     * 底部切换栏（弃用）
     *
     * @param
     */
    public void init(ViewPager viewPager, FragmentManager manager) {
//        this.fragmentManager = manager;

        if (viewPager != null) {
//            this.viewPager = viewPager;
            ArrayList<Fragment> list = new ArrayList<>();
//            mStickyLV.add(SelectedFragment.newInstance(0));
//            mStickyLV.add(GameFragment.getInstance(manager));
//            mStickyLV.add(GameHubFragment.getInstance());
//            mStickyLV.add(VideoFragment.getInstance());
//            mStickyLV.add(LocalFragment.getInstance());

            list.add(RecommendFragment.newInstance(0));
            //list.add(RankFragment.newInstance(""));
            list.add(HubFragment.newInstance(0));
            list.add(ClassifyFragment.newInstance(""));
            list.add(ManagerFragment.newInstance(context));

            adapter = new FragmentViewPagerAdapter(manager);
            adapter.setDate(list);
            viewPager.setAdapter(adapter);
        }
    }

    /**
     * 设置当前选中的菜单项
     *
     * @param currentMenu
     */
    public void setCurrentMenu(int currentMenu) {
        this.currentMenu = currentMenu;

        bt_home.setSelected(false);
        //bt_game.setSelected(false);
        bt_video.setSelected(false);
        bt_manager.setSelected(false);
        menu_game_hub_bt.setSelected(false);

        tv_home.setTextColor(colorNormal);
        //tv_game.setTextColor(colorNormal);
        menu_gamehub_tv.setTextColor(colorNormal);
        tv_video.setTextColor(colorNormal);
        tv_manager.setTextColor(colorNormal);

//        if (viewPager != null) {
//            viewPager.setCurrentItem(currentMenu);
//        }
//        switchFragment(currentMenu);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (currentMenu) {
            case 0://推荐
                if (null == selectedFragment) {
                    selectedFragment = new RecommendFragment();
                    transaction.add(R.id.main_list_fragments, selectedFragment);
                } else {
                    transaction.show(selectedFragment);
                }
                selectedFragment.scroll2Top();
                selectedFragment.setShow(true);
                if (null != discoverFragment) {
                    discoverFragment.setShow(false);
                }
                bt_home.setSelected(true);
                mTitleTv.setText("热门游戏");
                fl_notifi.setVisibility(View.VISIBLE);
                im_toSearch.setVisibility(View.VISIBLE);
                mDownloadBt.setVisibility(View.GONE);
                mHubBt.setVisibility(View.GONE);
                mRankBt.setVisibility(View.GONE);
                tv_home.setTextColor(colorDark);

                //埋点
                MobclickAgent.onEvent(context, UMEventNameConstant.mainRecommendButtonClickCount);

                break;
          /*  case 1://排行
                if (null == rankingFragment) {
                    rankingFragment = new RankFragment();
                    transaction.add(R.id.main_list_fragments, rankingFragment);
                } else {
                    transaction.show(rankingFragment);
                }
                if (null != discoverFragment) {
                    discoverFragment.setShow(false);
                }
                selectedFragment.setShow(false);
                bt_game.setSelected(true);
                mTitleTv.setText("排行榜");
                fl_notifi.setVisibility(View.GONE);
                im_toSearch.setVisibility(View.VISIBLE);
                mDownloadBt.setVisibility(View.GONE);
                mHubBt.setVisibility(View.GONE);
                tv_game.setTextColor(colorDark);
                break;*/
            case 3://分类
                if (null == discoverFragment) {
                    discoverFragment = new ClassifyFragment();
                    transaction.add(R.id.main_list_fragments, discoverFragment);
                } else {
                    transaction.show(discoverFragment);
                }
                discoverFragment.scroll2Top();
                discoverFragment.setShow(true);
                selectedFragment.setShow(false);
                bt_video.setSelected(true);
                mTitleTv.setText("分类");
                mDownloadBt.setVisibility(View.GONE);
                mHubBt.setVisibility(View.GONE);
                fl_notifi.setVisibility(View.GONE);
                im_toSearch.setVisibility(View.VISIBLE);
                mRankBt.setVisibility(View.VISIBLE);
                tv_video.setTextColor(colorDark);
                MobclickAgent.onEvent(context, UMEventNameConstant.mainDiscoverButtonClickCount);
                break;
            case 2://圈子
                if (null == gameHubFragment) {
                    gameHubFragment = new HubFragment();
                    transaction.add(R.id.main_list_fragments, gameHubFragment);
                } else {
                    transaction.show(gameHubFragment);
                }
                menu_game_hub_bt.setSelected(true);
                mTitleTv.setText(R.string.main_tab_04);
                fl_notifi.setVisibility(View.GONE);
                mDownloadBt.setVisibility(View.GONE);
                mHubBt.setVisibility(View.VISIBLE);
                mRankBt.setVisibility(View.GONE);
                im_toSearch.setVisibility(View.GONE);
                menu_gamehub_tv.setTextColor(colorDark);
                MobclickAgent.onEvent(context, UMEventNameConstant.mainCircleButtonClickCount);
                break;
            case 4://管理
                if (null == administrationFragment) {
                    administrationFragment = ManagerFragment.newInstance(context);
                    transaction.add(R.id.main_list_fragments, administrationFragment);
                } else {
                    transaction.show(administrationFragment);
                }
                selectedFragment.setShow(false);
                if (null != discoverFragment) {
                    discoverFragment.setShow(false);
                }
                bt_manager.setSelected(true);
                mTitleTv.setText("管理");
                mDownloadBt.setVisibility(View.VISIBLE);
                mRankBt.setVisibility(View.GONE);
                im_toSearch.setVisibility(View.GONE);
                mHubBt.setVisibility(View.GONE);
                fl_notifi.setVisibility(View.GONE);
                tv_manager.setTextColor(colorDark);
                MobclickAgent.onEvent(context, UMEventNameConstant.mainManagerButtonClickCount);
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != selectedFragment) {
            transaction.hide(selectedFragment);
        }
     /*   if (null != rankingFragment) {
            transaction.hide(rankingFragment);
        }*/
        if (null != gameHubFragment) {
            transaction.hide(gameHubFragment);
        }
        if (null != discoverFragment) {
            transaction.hide(discoverFragment);
        }
        if (null != administrationFragment) {
            transaction.hide(administrationFragment);
        }
    }

    /**
     * 设置菜单触摸事件监听器
     *
     * @param listener
     */
    public void setOnTouchListener(View.OnTouchListener listener) {
        home.setOnTouchListener(listener);
        //game.setOnTouchListener(listener);
        menu_game_hub.setOnTouchListener(listener);
        video.setOnTouchListener(listener);
        manager.setOnTouchListener(listener);
    }

    /**
     * 内部类用于处理底部菜单点击后页面跳转
     */
    public class MenuOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (v.getId()) {
                case R.id.menu_home_ll:
                    if (currentMenu != R.id.menu_home_ll && action == MotionEvent.ACTION_UP) {
                        setCurrentMenu(0);
                    }
                    break;
             /*   case R.id.menu_game_ll:
                    if (currentMenu != R.id.menu_game_ll && action == MotionEvent.ACTION_UP) {
                        setCurrentMenu(1);
                    }
                    break;*/
                case R.id.menu_game_hub:
                    if (currentMenu != R.id.menu_game_hub && event.getAction() == MotionEvent.ACTION_UP) {
                        setCurrentMenu(2);
                    }
                    break;
                case R.id.menu_video:
                    if (currentMenu != R.id.menu_video && action == MotionEvent.ACTION_UP) {
                        setCurrentMenu(3);
                    }
                    break;
                case R.id.menu_manager:
                    if (currentMenu != R.id.menu_manager && action == MotionEvent.ACTION_UP) {
                        setCurrentMenu(4);
                    }
                    break;
            }
            return true;
        }
    } // 底部切换栏 结束

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //判断App是否从通知栏消息进来，如果是，直接启动消息详情页
        long pushMsgId = intent.getLongExtra("msgId", 0);
        int pushMsgType = intent.getIntExtra("type", 0);
        //Log.e(TAG,"id "+pushMsgId +" type "+pushMsgType);
        if (pushMsgId > 0 && pushMsgType > 0) {
            if (pushMsgType == PushMessage.MSG_TYPE_HD) {

                Intent msgIntent = new Intent(this, MessageDetailActivity.class);
                msgIntent.putExtra("msgId", pushMsgId);
                msgIntent.putExtra("type", pushMsgType);
                startActivity(msgIntent);

            } else if (pushMsgType == PushMessage.MSG_TYPE_TZ) {

                PushMessage msg = (PushMessage) intent.getSerializableExtra("msg");
                Intent msgIntent = new Intent(this, NotifyMsgDetailActivity.class);
                msgIntent.putExtra("msg", msg);
                startActivity(msgIntent);
            }

        }
    }

    /**
     * 判断App是否从通知栏消息进来，如果是，直接启动消息详情页
     */
    private void isFromNotification() {

        long pushMsgId = getIntent().getLongExtra("msgId", 0);
        int pushMsgType = getIntent().getIntExtra("type", 0);
        if (pushMsgId > 0 && pushMsgType > 0) {
            if (pushMsgType == PushMessage.MSG_TYPE_HD) {

                Intent msgIntent = new Intent(this, MessageDetailActivity.class);
                msgIntent.putExtra("msgId", pushMsgId);
                msgIntent.putExtra("type", pushMsgType);
                startActivity(msgIntent);
            } else if (pushMsgType == PushMessage.MSG_TYPE_TZ) {
                PushMessage msg = (PushMessage) getIntent().getSerializableExtra("msg");
                Intent msgIntent = new Intent(this, NotifyMsgDetailActivity.class);
                msgIntent.putExtra("msg", msg);
                startActivity(msgIntent);
            }

        }
    }
    /**
     * 处理按钮点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case but_search:
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
                break;
            case but_msg:
                Intent msgIntent = new Intent(this, PushMessageActivity.class);
                startActivity(msgIntent);
                break;*/
            case R.id.im_toSearch:
                startActivity(new Intent(context, SearchActivity.class));
                break;
            case R.id.fl_notifi:
                startActivity(new Intent(context, MsgCenterActivity.class));
                break;
            case R.id.main_download_bt:
                startActivity(new Intent(context, DownloadCenterActivity.class));
                break;
            case R.id.main_hub_bt:
                startActivity(new Intent(context, HubPostsActivity.class));
                break;
            case R.id.main_rank_bt:
                startActivity(new Intent(context, RankActivity.class));
                MobclickAgent.onEvent(context, UMEventNameConstant.mainRankButtonClickCount);
                break;
            case R.id.iv_icon_title:
                if (null != mSlidingMenu) {
                    mSlidingMenu.toggle();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        //关闭后台服务
        stopService(new Intent(this, FileLoadService.class));
        //关闭OTA升级服务
        stopService(new Intent(this, OtaService.class));
        super.onDestroy();
        MobclickAgent.onKillProcess(context);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    /**
     * 双击退出程序
     */
    private void exitBy2Click() {
        if (!isExit) {
            isExit = true;
            ToastUtil.show(context, "再点一次退出");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            FileLoadManager manager = FileLoadManager.getInstance(this);
            manager.destroy();
            finish();
        }
    }


    /**
     * 检测是否有更新
     */
    private void checkUpdate() {
        if (StoreApplication.net_status != Constant.NET_STATUS_WIFI) {
            return;
        }
        if (isChecking) {
            return;
        }
        if (isDownloading) {
            if (isRunningBackground) {
                //Toast.makeText(context, "更新！", Toast.LENGTH_SHORT).show();
            } else {
                showProgressDialog();
            }
            return;
        }

        String url = Constant.WEB_SITE + Constant.URL_APP_UPDATE;
        Response.Listener<JsonResult<VersionInfo>> successListener = new Response.Listener<JsonResult<VersionInfo>>() {
            @Override
            public void onResponse(JsonResult<VersionInfo> result) {

                if (result == null || result.code != 0) {
                    isChecking = false;
                    return;
                }

                versionInfo = result.data;
                if (versionInfo != null) {

                    //如果后台正在升级，则直接显示进度框
                    GameFileStatus downloadFileInfo = fileLoad.getGameFileLoadStatus(versionInfo.fileName, versionInfo.url,
                            versionInfo.packageName, versionInfo.versionCode);
                    if (downloadFileInfo != null) {
                        if (downloadFileInfo.getStatus() == GameFileStatus.STATE_DOWNLOAD || downloadFileInfo.getStatus() ==
                                GameFileStatus.STATE_PAUSE) {

                            showProgressDialog();
                            doUpdateUi();
                            isChecking = false;
                            return;
                        }
                    }

                    //判读是否需要更新
                    int localVersion = CommonUtil.getVersionCode(context);
                    android.util.Log.d(TAG, "版本:" + localVersion);
                    if (localVersion < versionInfo.versionCode) {
                        showUpdateDialog();
                        CommonUtil.verifyStoragePermissions(context); //申请读写SD卡权限
                    } else {
                        //Toast.makeText(MainHomeActivity.this,"当前已是最新版本",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Toast.makeText(MainHomeActivity.this,"检测失败：服务端异常！",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                }
                isChecking = false;
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
                isChecking = false;
            }
        };

        Request<JsonResult<VersionInfo>> versionRequest = new GsonRequest<JsonResult<VersionInfo>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<VersionInfo>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("appType", "0");
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
        isChecking = true;
    }

    /**
     * 显示更新对话框
     */
    private void showUpdateDialog() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("updateDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.setDialogWidth(260);


        LayoutInflater inflater = getLayoutInflater();
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_update, null);
        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);

        String fileSizeStr = Formatter.formatFileSize(context, versionInfo.fileSize);

        tv_title.setText("有新版本：V" + versionInfo.versionName + "（" + fileSizeStr + "）");
        TextView tv_summary = (TextView) contentView.findViewById(R.id.tv_summary);
        tv_summary.setText(versionInfo.content);

        dialogFragment.setContentView(contentView);

        dialogFragment.setNegativeButton(R.string.update_now, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();

                fileLoad.load(versionInfo.fileName, versionInfo.url, versionInfo.md5, versionInfo.packageName, versionInfo
                        .versionCode, versionInfo.fileName, versionInfo.url, versionInfo.id, false);
                showProgressDialog();   //显示进度条对话框
                doUpdateUi();           //启动更新进度条线程
            }
        });
        dialogFragment.show(ft, "updateDialog");
    }

    /**
     * 显示下载进度的对话框
     */
    private void showProgressDialog() {

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("progressDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();

        dialogFragment.setCancelable(false);
        dialogFragment.setDialogWidth(255);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_download, null);
        progressBar = (NumberProgressBar) contentView.findViewById(R.id.progress_bar);

        dialogFragment.setContentView(contentView);

        dialogFragment.setPositiveButton("关闭", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();

                fileLoad.delete(versionInfo.url);

                if (mNotificationManager != null) {
                    mNotificationManager.cancel(1);
                }
                isDownloading = false;
                finish();
            }
        });

       /* dialogFragment.setNegativeButton(R.string.update_background, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogFragment.dismiss();

                remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification_download);
                notification = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContent(remoteViews)
                        .build();
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, notification);
                isRunningBackground = true;

            }
        });*/
        dialogFragment.show(ft, "progressDialog");

    }

    private void doUpdateUi() {
        isDownloading = true;
        //执行更新进度条的操作
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isDownloading) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        GameFileStatus downloadFileInfo = fileLoad.getGameFileLoadStatus(versionInfo.fileName, versionInfo.url,
                                versionInfo.packageName, versionInfo.versionCode);
                        if (downloadFileInfo != null) {

                            double finished = downloadFileInfo.getFinished();
                            double length = downloadFileInfo.getLength();
                            final double process = finished / length * 100;

                            if (isRunningBackground) {
                                remoteViews.setProgressBar(R.id.progress_bar, 100, (int) process, false);
                                if (process >= 100) {

                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    File file = null;
                                    try {
                                        file = new File(CommonUtil.getFileLoadBasePath(), versionInfo.fileName);
                                    } catch (NoSDCardException e) {
                                        e.printStackTrace();
                                    }
                                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

                                    remoteViews.setTextViewText(R.id.text1, "下载完成");
                                    notification = new Notification.Builder(context)
                                            .setSmallIcon(R.drawable.ic_launcher)
                                            .setContent(remoteViews)
                                            .setContentIntent(PendingIntent.getActivity(context, 0, intent, 0))
                                            .build();
                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    isRunningBackground = false;
                                } else {
                                    remoteViews.setTextViewText(R.id.text1, "正在下载: " + (int) process + "%");
                                }
                                mNotificationManager.notify(1, notification);
                            } else {
                                if (progressBar != null) {
                                    progressBar.setProgress((int) process);
                                }
                                if (process == 100) {
                                    //处理安装App的操作
                                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    SimpleDialogFragment prev = (SimpleDialogFragment) getSupportFragmentManager()
                                            .findFragmentByTag("downloadDialog");
                                    if (prev != null) {
                                        ft.remove(prev);
                                    }
                                    ft.commit();
                                    isRunningBackground = false;
                                    AppInstallHelper.installApk(context, versionInfo.fileName);
                                }
                            }

                            if (process >= 100) {
                                isDownloading = false;
                            }
                        }
                    }
                });
            }
        }, 0, 500);
    }


}
