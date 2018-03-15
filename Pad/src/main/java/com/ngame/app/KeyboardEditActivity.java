package com.ngame.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lx.pad.R;
import com.lx.pad.util.LLog;
import com.ngame.DabaBase.KeyEnum;
import com.ngame.Utils.DisplayMetricsMgr;
import com.ngame.Utils.IUpdateKeyState;
import com.ngame.Utils.KeyMgrUtils;
import com.ngame.widget.DragImageView;
import com.ngame.widget.DragImgClickInterface;
import com.ngame.widget.DragInterface;
import com.ngame.widget.ScaleInterface;
import com.ngds.pad.server.DeviceManager;

/**
 * Created by Administrator on 2017/12/2.
 */

public class KeyboardEditActivity extends BaseFragmentActivity implements ViewDragInterface, View
        .OnDragListener, DragInterface, ScaleInterface, DragImgClickInterface, View
        .OnClickListener {
    private ImageView m_curDragView;
    private static final Class[] m_sClsPageAry = new Class[]{NormalKeyViewMgrUtils.class,
            SpecialKeyViewMgrUtils.class};
    private NormalKeyViewMgrUtils m_normalKeyViewMgrUtils;
    private SpecialKeyViewMgrUtils m_specialKeyViewMgrUtils;
    View m_viewPointerLocation;
    //    private ImageView[] m_injectViewAry;
    private View m_viewMenu;
    private ImageView m_ivDel;
    private ImageView m_ivArrow;
    //    private ImageView m_ivJoystick;
    private FrameLayout m_frameLayoutMenu;
    private TextView m_tvTip;
    private ViewPager m_viewPagerKeys;
    private LinearLayout m_linearLayoutDot;
    private TextView m_tvPrevious;
    private TextView m_tvNext;
    private FrameLayout m_frameLayoutContainer;
    private ImageView[] m_ivAryDraged;  //拖出来的按钮
    private boolean m_isShowTest = false;

    private static KeyboardEditActivity context = null;

    public static final KeyboardEditActivity getInstance() {
        return context;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏标题
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);   //设置全屏
        setContentView(R.layout.activity_keyboard_config);
        m_viewPointerLocation = findViewById(R.id.view_pointer_location);
        m_ivDel = findViewById(R.id.iv_del);
        m_frameLayoutMenu = findViewById(R.id.fl_menu);
        m_tvTip = findViewById(R.id.tv_tip);
        m_viewMenu = findViewById(R.id.rl_menu);
        m_linearLayoutDot = findViewById(R.id.ll_dot);
        m_ivArrow = findViewById(R.id.iv_arrow);
        m_ivArrow.setOnClickListener(this);
        m_tvPrevious = findViewById(R.id.tv_previous);
        m_tvPrevious.setOnClickListener(this);

        context = this;

        m_tvNext = findViewById(R.id.tv_next);
        m_tvNext.setOnClickListener(this);
        m_viewPagerKeys = findViewById(R.id.vp_keys);
        m_viewPagerKeys.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        m_viewPagerKeys.setAdapter(new PagerkeyAdapter(getSupportFragmentManager()));
        m_viewPagerKeys.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: {
                        m_tvPrevious.setEnabled(false);
                        m_tvNext.setEnabled(true);
                        break;
                    }
                    case 1: {
                        m_tvPrevious.setEnabled(true);
                        m_tvNext.setEnabled(false);
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        m_frameLayoutContainer = findViewById(R.id.fl_container);
        m_frameLayoutContainer.setOnDragListener(this);

        findViewById(R.id.iv_reset).setOnClickListener(this);
        findViewById(R.id.iv_save).setOnClickListener(this);
        findViewById(R.id.iv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_sync).setOnClickListener(this);
        findViewById(R.id.tv_motion).setOnClickListener(this);

        KeyMgrUtils.sUpdateKeyEnumHashMap(this);
        updateDragKeyView();
        updateIsShowState();
        showTestView();

        if (KeyMgrUtils.sGetPrefBoolean(this, "tool_config", "key_first_guide", true)) {
            Intent intent = new Intent();
            intent.setPackage(getPackageName());
            intent.setAction(PadToolGuideActivity.class.getName());
            startActivity(intent);
        }


    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.iv_arrow) {
            if (m_viewMenu.getVisibility() == View.VISIBLE) {
                m_ivArrow.setImageResource(R.mipmap.ic_menu_open);
                m_viewMenu.setVisibility(View.GONE);
            } else {
                m_viewMenu.setVisibility(View.VISIBLE);
                m_ivArrow.setImageResource(R.mipmap.ic_menu_close);
            }
        } else if (vId == R.id.iv_reset) {
            AlertDialog.Builder artDlg = new AlertDialog.Builder(this, R.style
                    .Theme_AppCompat_Light_Dialog);
            artDlg.setMessage("即将清空所有按键设置，确认将清除当前映射配置");
            artDlg.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    KeyEnum[] keyEnumAry = KeyEnum.values();
                    int nLen = keyEnumAry.length;
                    for (int nIndex = 0; nIndex < nLen; nIndex++) {
                        KeyEnum keyEnum = keyEnumAry[nIndex];
                        enableKeyView(keyEnum, true);
                        KeyMgrUtils.sInitKeyEnum(keyEnum);
                    }
                    KeyMgrUtils.sSavePrefInfo(KeyboardEditActivity.this);
                    updateDragKeyView();
                    dialog.dismiss();
                    Toast.makeText(KeyboardEditActivity.this, "按键设置已清空", Toast.LENGTH_LONG).show();
                }
            });
            artDlg.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            artDlg.create().show();
        } else if (vId == R.id.iv_save) {
            KeyMgrUtils.sSavePrefInfo(KeyboardEditActivity.this);
            Toast.makeText(KeyboardEditActivity.this, "保存成功", Toast.LENGTH_LONG).show();
            String strPkgName = getPackageName();
            KeyMgrUtils.copyFile("/data/data/" + strPkgName + "/shared_prefs/keyboard.xml",
                    "mnt/sdcard/data/keyboard.xml");
            finish();
        } else if (vId == R.id.iv_cancel) {
            if (KeyMgrUtils.sIsChanged()) {
                showSaveDlg();
            } else {
                finish();
            }
        } else if (vId == R.id.tv_sync) {
            //云端
             AlertDialog.Builder progressDialog = new AlertDialog.Builder(this, R.style
                    .Theme_AppCompat_DayNight_Dialog);
            progressDialog.setMessage("   加载中...  ");
            progressDialog.setCancelable(true);
            final  AlertDialog alertDialog = progressDialog.create();
            alertDialog.show();
            KeyMgrUtils.sUpdateKeyEnumFromHttpServer(this, DeviceManager.getInstance(this)
                    .getGamePackageName(), new IUpdateKeyState() {
                @Override
                public void updateFail() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "同步云端键位失败", Toast
                                    .LENGTH_SHORT).show();
                        }
                    });
                    alertDialog.dismiss();
                }

                @Override
                public void updateSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            KeyEnum[] keyEnums = KeyEnum.values();
                            for (int nIndex = 0; nIndex < keyEnums.length; nIndex++) {
                                enableKeyView(keyEnums[nIndex], false);
                            }
                            updateDragKeyView();
                            m_normalKeyViewMgrUtils.setAllKeyDisImg();
                            m_specialKeyViewMgrUtils.setAllKeyDisImg();
                            Toast.makeText(context, "云端键位已同步", Toast
                                    .LENGTH_SHORT).show();
                        }
                    });
                    alertDialog.dismiss();
                }
            });

        } else if (vId == R.id.tv_motion) {
//            startActivity();
//            LLog.d("KeyboardEditActivity->viewClick R.id.tv_motion is not realize");
            startActivity(MotionAttributeDialogActivity.makeIntent(this, KeyEnum.MOTION));
        } else if (vId == R.id.tv_previous) {
            m_viewPagerKeys.setCurrentItem(0);
        } else if (vId == R.id.tv_next) {
            m_viewPagerKeys.setCurrentItem(1);
        }
    }

    @Override
    public void setCurDragView(ImageView view) {
        LLog.d("KeyboardEditActivity->setCurDragView");
        m_curDragView = view;
    }

    @Override
    public boolean isViewDraged(ImageView view) {
        LLog.d("KeyboardEditActivity->isViewDraged");
        boolean alreadyProcess = false;
        if (m_ivAryDraged[((KeyEnum) view.getTag()).ordinal()] != null) {
            Toast.makeText(this, "已经有一个该按钮", Toast.LENGTH_SHORT).show();
            alreadyProcess = false;
        } else {
            alreadyProcess = true;
        }
        return alreadyProcess;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        LLog.d("KeyboardEditActivity->onDrag action:" + event.getAction());
        switch (event.getAction()) {
            case DragEvent.ACTION_DROP: {
                KeyEnum keyEnum = (KeyEnum) m_curDragView.getTag();
                DragImageView dragImageView = new DragImageView(this);
                dragImageView.setImageDrawable(m_curDragView.getDrawable());
                dragImageView.setTag(keyEnum);
                dragImageView.setDragInterface(this);
                dragImageView.setScaleInterface(this);
                dragImageView.setDragImgClickInterface(this);
                int w = m_curDragView.getWidth() / 2;
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
                int x = (int) event.getX();
                int y = (int) event.getY();
                layoutParams.leftMargin = x - w;
                layoutParams.topMargin = y - w;
                LLog.d("KeyboardEditActivity->按键拖动:" + keyEnum.keyName + " " +
                        String.format("left:%d top:%d right:%d bottom:%d",
                                layoutParams.leftMargin, layoutParams.topMargin, layoutParams
                                        .rightMargin, layoutParams.bottomMargin));
                m_frameLayoutContainer.addView(dragImageView, layoutParams);
                m_normalKeyViewMgrUtils.setKeyDisImg(keyEnum);
                m_specialKeyViewMgrUtils.setKeyDisImg(keyEnum);
                KeyMgrUtils.sReplaceHashMapKeyEnumX(keyEnum, x);
                KeyMgrUtils.sReplaceHashMapKeyEnumY(keyEnum, y);
                KeyMgrUtils.sReplaceHashMapKeyEnumR(keyEnum, w);
                m_ivAryDraged[keyEnum.ordinal()] = dragImageView;
                break;
            }
        }
        return true;
    }

    @Override
    public void onDragFinish(ImageView v) {
        LLog.d("KeyboardEditActivity->按键拖动结束.");
        if (v.getTag() != null) {
            if (isIntersectsDelView(v)) {
                enableKeyView((KeyEnum) v.getTag(), true);
            } else {
                sReplaceKeyViewInfo(v);
            }
            m_ivDel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDragMove(ImageView v) {
        LLog.d("KeyboardEditActivity->拖动中");
        if (isIntersectsDelView(v)) {
            m_ivDel.setImageResource(R.mipmap.ic_del_foc);
        } else {
            m_ivDel.setImageResource(R.mipmap.ic_del);
        }
    }

    @Override
    public void onDragStart(ImageView v) {
        LLog.d("KeyboardEditActivity->onDragStart");
        m_ivDel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScaleFinish(ImageView v) {
        LLog.d("KeyboardEditActivity->onScaleFinish");
        if (v.getTag() != null) {
            sReplaceKeyViewInfo(v);
        }
    }

    @Override
    public void onScaleStart(ImageView v) {
        LLog.d("KeyboardEditActivity->onScaleStart");
        m_ivDel.setVisibility(View.GONE);
    }

    @Override
    public void onDragImageViewClick(ImageView v) {
        LLog.d("KeyboardEditActivity->onDragImageViewClick");
        KeyEnum keyEnum = (KeyEnum) v.getTag();
        if (keyEnum != null) {
            switch (IndexMappingKeyEnumOrdinal.keyIndexAry[keyEnum.ordinal()]) {
                case 2: //KeyEnum.A
                case 3: //KeyEnum.B
                case 4: //KeyEnum.X
                case 5: //KeyEnum.Y
                case 6: //KeyEnum.L1
                case 7: //KeyEnum.R1
                case 8: //KeyEnum.L2
                case 9: //KeyEnum.R2
                case 13:    //KeyEnum.UP
                case 14:    //KeyEnum.DOWN
                case 15:    //KeyEnum.LEFT
                case 16:    //KeyEnum.RIGHT
                case 19:    //KeyEnum.START
                case 20:    //KeyEnum.BACK
                {
                    if (keyEnum != null) {
//                        startActivity();
//                        LLog.d("KeyboardEditActivity->onDragImageViewClick startActivity
// KeyAttributeDialogActivity not realize");
                        startActivity(KeyAttributeDialogActivity.makeIntent(this, keyEnum));
                    }
                    break;
                }
                case 10:    //KeyEnum.L
                case 11:    //KeyEnum.R
                {
                    if (keyEnum != null) {
//                            startActivity();
//                            LLog.d("KeyboardEditActivity->onDragImageViewClick startActivity
// JoystickAttributeDialogActivity not realize");
                        startActivity(JoystickAttributeDialogActivity.makeIntent(this, keyEnum));
                    }
                    break;
                }
            }
        }
    }


    class PagerkeyAdapter extends FragmentPagerAdapter {
        public PagerkeyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment result = null;

            try {
                result = (Fragment) m_sClsPageAry[position].newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (position == 0) {
                result = m_normalKeyViewMgrUtils;
                if (result == null) {
                    return null;
                }
            } else if (position == 1) {
                result = m_specialKeyViewMgrUtils;
            }

            ((BaseKeyViewFragment) result).setViewDragInterface(KeyboardEditActivity.this);

            return result;
        }

        @Override
        public int getCount() {
            return m_sClsPageAry.length;
        }
    }

    public KeyboardEditActivity() {
        super();
        m_ivAryDraged = new ImageView[26];
        m_normalKeyViewMgrUtils = new NormalKeyViewMgrUtils();
        m_specialKeyViewMgrUtils = new SpecialKeyViewMgrUtils();
        m_normalKeyViewMgrUtils.m_context = this;
        m_specialKeyViewMgrUtils.m_context = this;
    }

    private static RectF sMakeViewRectF(View view) {
        return (view == null) ? null : new RectF(view.getX(), view.getY(), view.getX() + view
                .getWidth(), view.getY() + view.getHeight());
    }

    private boolean isIntersectsDelView(View view) {
        return (view == null || m_ivDel == null) ? false : RectF.intersects(sMakeViewRectF(view),
                sMakeViewRectF(m_ivDel));
    }

    private static void sReplaceKeyViewInfo(View view) {
        new Exception().printStackTrace();

        KeyEnum keyEnum = (KeyEnum) view.getTag();
        LLog.d("KeyboardEditActivity->替换按键图标 view.getLayoutParams().width:" + view
                .getLayoutParams().width);
        int r = view.getWidth() / 2;
        int x = (int) view.getX() + r;
        int y = (int) view.getY() + r;
        LLog.d("KeyboardEditActivity->替换按键图标 getX - GetY keyName:" + keyEnum.keyName + " x:" + x
                + " y:" + y + " r:" + r);
        int pos[] = new int[2];
//        view.getLocationInWindow(pos);
//        LLog.d("KeyboardEditActivity->sReplaceKeyViewInfo getLocationInWindow keyName:" +
// keyEnum.keyName + " x:" + pos[0] + " y:" + pos[1]);
        view.getLocationOnScreen(pos);
        LLog.d("KeyboardEditActivity->替换按键图标 获取再屏幕中的坐标 keyName:" + keyEnum.keyName + " x:" +
                pos[0] + " y:" + pos[1]);
        x = pos[0] + r;
        y = pos[1] + r;
        KeyMgrUtils.sReplaceHashMapKeyEnumX(keyEnum, x);
        KeyMgrUtils.sReplaceHashMapKeyEnumY(keyEnum, y);
        KeyMgrUtils.sReplaceHashMapKeyEnumR(keyEnum, r);
    }


    private void hideTestView() {
        LLog.d("KeyboardEditActivity->hideTestView");
        m_frameLayoutMenu.setVisibility(View.VISIBLE);
        m_tvTip.setVisibility(View.INVISIBLE);
        m_viewPointerLocation.setVisibility(View.INVISIBLE);
    }

    private void showTestView() {
        LLog.d("KeyboardEditActivity->showTestView not realize");
        if (m_isShowTest) {
            m_viewPointerLocation.bringToFront();
            m_frameLayoutMenu.bringToFront();
            m_frameLayoutMenu.setVisibility(View.VISIBLE);
            m_tvTip.setVisibility(View.VISIBLE);
            m_viewPointerLocation.setVisibility(View.VISIBLE);

            ImageView[] imgViewAry = m_ivAryDraged;
            int nLen = imgViewAry.length;
            boolean bKeySet = false;
            for (int nIndex = 0; nIndex < nLen; nIndex++) {
                if (imgViewAry[nIndex] != null) {
                    bKeySet = true;
                    break;
                }
            }
            if (bKeySet) {
                m_tvTip.setText("您尚未配置键位，请返回主菜单选择“按键配置”进行相关操作！");
            } else {
                m_tvTip.setText("请操作手柄进行测试");
            }
        } else {
            hideTestView();
        }
    }

    private void updateIsShowState() {
        LLog.d("KeyboardEditActivity->updateIsShowState");
        m_isShowTest = getIntent().getBooleanExtra("is_show", false);
    }

    private void showSaveDlg() {
        AlertDialog.Builder artDlg = new AlertDialog.Builder(this, R.style
                .Theme_AppCompat_Light_Dialog);
        artDlg.setMessage("您修改了部分参数，是否保存？");
        artDlg.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                KeyMgrUtils.sSavePrefInfo(KeyboardEditActivity.this);
                dialog.dismiss();
                KeyboardEditActivity.this.finish();
            }
        });
        artDlg.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                KeyMgrUtils.sUpdateKeyEnumHashMap(KeyboardEditActivity.this);
                dialog.dismiss();
                KeyboardEditActivity.this.finish();
            }
        });
        artDlg.create().show();
    }

    private void updateDragKeyView() {
        DragImageView dragImageView;
        float dpX = 40f;
        float dpY = 266f;
        KeyEnum[] keyEnums = KeyEnum.values();
        for (int nIndex = 0; nIndex < keyEnums.length; nIndex++) {
            KeyEnum keyEnum = keyEnums[nIndex];
            int x = KeyMgrUtils.sGetKeyInfoX(keyEnum);
            int y = KeyMgrUtils.sGetKeyInfoY(keyEnum);
            ImageView imgView = m_ivAryDraged[keyEnum.ordinal()];
            if (x >= 0 && y >= 0 && imgView == null) {
                dragImageView = new DragImageView(this);
                Drawable drawable = IndexMappingKeyEnumRes.sGetDrawable(keyEnum, this);
                if (drawable != null) {
                    dragImageView.setImageDrawable(drawable);
                }
                dragImageView.setTag(keyEnum);
                dragImageView.setDragInterface(this);
                dragImageView.setScaleInterface(this);
                dragImageView.setDragImgClickInterface(this);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
                layoutParams.leftMargin = x;
                layoutParams.topMargin = y;
                x = KeyMgrUtils.sGetKeyInfoR(keyEnum);
                if (x > 0) {
                    layoutParams.width = x * 2;
                    layoutParams.height = layoutParams.width;
                    layoutParams.leftMargin -= x;
                    layoutParams.topMargin -= x;
                }

                m_frameLayoutContainer.addView(dragImageView, layoutParams);
//                LLog.d("KeyboardEditActivity->updateDragKeyView " + (m_normalKeyViewMgrUtils !=
// null) + " | " + (m_normalKeyViewMgrUtils.m_list != null));
                m_normalKeyViewMgrUtils.m_list.add(keyEnum);
//                LLog.d("KeyboardEditActivity->updateDragKeyView " + (m_specialKeyViewMgrUtils
// != null) + " | " + (m_specialKeyViewMgrUtils.m_list != null));
                m_specialKeyViewMgrUtils.m_list.add(keyEnum);
                m_ivAryDraged[keyEnum.ordinal()] = dragImageView;
            }
        }

        if (m_ivAryDraged[KeyEnum.L.ordinal()] == null) {
            dragImageView = new DragImageView(this);
            dragImageView.setImageResource(R.mipmap.ic_key_l);
            dragImageView.setTag(KeyEnum.L);
            dragImageView.setDragInterface(this);
            dragImageView.setScaleInterface(this);
            dragImageView.setDragImgClickInterface(this);
            int n = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_key_l).getWidth() / 2;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            layoutParams.leftMargin = DisplayMetricsMgr.dip2px(this, dpX);
            layoutParams.topMargin = DisplayMetricsMgr.dip2px(this, dpY);
            m_frameLayoutContainer.addView(dragImageView, layoutParams);
            m_ivAryDraged[KeyEnum.L.ordinal()] = dragImageView;
            KeyMgrUtils.sReplaceHashMapKeyEnumX(KeyEnum.L, DisplayMetricsMgr.dip2px(this, dpX) + n);
            KeyMgrUtils.sReplaceHashMapKeyEnumY(KeyEnum.L, DisplayMetricsMgr.dip2px(this, dpY) + n);
            KeyMgrUtils.sReplaceHashMapKeyEnumR(KeyEnum.L, n);
            KeyMgrUtils.sSavePrefInfo(this);
        }
    }

    private void enableKeyView(KeyEnum keyEnum, boolean bInit) {
//        LLog.d("KeyboardEditActivity->enableKeyView");
        if (keyEnum != null) {
//            LLog.d("KeyboardEditActivity->enableKeyView keyEnum:" + keyEnum.keyName);
            m_frameLayoutContainer.removeView(m_ivAryDraged[keyEnum.ordinal()]);
            switch (IndexMappingNormalKeyEnum.m_keyEnumAry[keyEnum.ordinal()]) {
                case 1: {
                    m_normalKeyViewMgrUtils.m_ivKeyA.setImageResource(R.mipmap.ic_key_a);
                    m_normalKeyViewMgrUtils.m_ivKeyA.setEnabled(true);
                    break;
                }
                case 2: {
                    m_normalKeyViewMgrUtils.m_ivKeyB.setImageResource(R.mipmap.ic_key_b);
                    m_normalKeyViewMgrUtils.m_ivKeyB.setEnabled(true);
                    break;
                }
                case 3: {
                    m_normalKeyViewMgrUtils.m_ivKeyX.setImageResource(R.mipmap.ic_key_x);
                    m_normalKeyViewMgrUtils.m_ivKeyX.setEnabled(true);
                    break;
                }
                case 4: {
                    m_normalKeyViewMgrUtils.m_ivKeyY.setImageResource(R.mipmap.ic_key_y);
                    m_normalKeyViewMgrUtils.m_ivKeyY.setEnabled(true);
                    break;
                }
                case 5: {
                    m_normalKeyViewMgrUtils.m_ivKeyUp.setImageResource(R.mipmap.ic_key_up);
                    m_normalKeyViewMgrUtils.m_ivKeyUp.setEnabled(true);
                    break;
                }
                case 6: {
                    m_normalKeyViewMgrUtils.m_ivKeyDown.setImageResource(R.mipmap.ic_key_down);
                    m_normalKeyViewMgrUtils.m_ivKeyDown.setEnabled(true);
                    break;
                }
                case 7: {
                    m_normalKeyViewMgrUtils.m_ivKeyLeft.setImageResource(R.mipmap.ic_key_left);
                    m_normalKeyViewMgrUtils.m_ivKeyLeft.setEnabled(true);
                    break;
                }
                case 8: {
                    m_normalKeyViewMgrUtils.m_ivKeyRight.setImageResource(R.mipmap.ic_key_right);
                    m_normalKeyViewMgrUtils.m_ivKeyRight.setEnabled(true);
                    break;
                }
            }

            switch (IndexMappingSpecialKeyEnum.m_keyEnumAry[keyEnum.ordinal()]) {
                case 1: {
                    m_specialKeyViewMgrUtils.m_ivKeyL1.setImageResource(R.mipmap.ic_key_l1);
                    m_specialKeyViewMgrUtils.m_ivKeyL1.setEnabled(true);
                    break;
                }
                case 2: {
                    m_specialKeyViewMgrUtils.m_ivKeyR1.setImageResource(R.mipmap.ic_key_r1);
                    m_specialKeyViewMgrUtils.m_ivKeyR1.setEnabled(true);
                    break;
                }
                case 3: {
                    m_specialKeyViewMgrUtils.m_ivKeyL2.setImageResource(R.mipmap.ic_key_l2);
                    m_specialKeyViewMgrUtils.m_ivKeyL2.setEnabled(true);
                    break;
                }
                case 4: {
                    m_specialKeyViewMgrUtils.m_ivKeyR2.setImageResource(R.mipmap.ic_key_r2);
                    m_specialKeyViewMgrUtils.m_ivKeyR2.setEnabled(true);
                    break;
                }
                case 5: {
                    m_specialKeyViewMgrUtils.m_ivKeyStart.setImageResource(R.mipmap.ic_key_start);
                    m_specialKeyViewMgrUtils.m_ivKeyStart.setEnabled(true);
                    break;
                }
                case 6: {
                    m_specialKeyViewMgrUtils.m_ivKeyBack.setImageResource(R.mipmap.ic_key_back);
                    m_specialKeyViewMgrUtils.m_ivKeyBack.setEnabled(true);
                    break;
                }
                case 7: {
                    m_specialKeyViewMgrUtils.m_ivKeyR.setImageResource(R.mipmap.ic_key_r);
                    m_specialKeyViewMgrUtils.m_ivKeyR.setEnabled(true);
                    break;
                }
                case 8: {
                    m_specialKeyViewMgrUtils.m_ivKeyDirection.setImageResource(R.mipmap
                            .ic_key_direction);
                    m_specialKeyViewMgrUtils.m_ivKeyDirection.setEnabled(true);
                    break;
                }
            }

            if (bInit) {
                KeyMgrUtils.sInitKeyEnum(keyEnum);
            }

            m_ivAryDraged[keyEnum.ordinal()] = null;
        }
    }
}
