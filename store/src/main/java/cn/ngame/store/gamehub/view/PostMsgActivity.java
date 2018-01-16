package cn.ngame.store.gamehub.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.NormalDataBean;
import com.jzt.hol.android.jkda.sdk.bean.gamehub.PostMsgBodyBean;
import com.jzt.hol.android.jkda.sdk.rx.ObserverWrapper;
import com.jzt.hol.android.jkda.sdk.services.gamehub.PostMsgClient;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.bean.UpLoadBean;
import cn.ngame.store.core.utils.APIErrorUtils;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.SPUtils;
import cn.ngame.store.core.utils.SystemUtil;
import cn.ngame.store.core.utils.unitChangeUtil;
import cn.ngame.store.gamehub.bean.PictureBean;
import cn.ngame.store.user.view.LoginActivity;
import cn.ngame.store.util.ConvUtil;
import cn.ngame.store.util.StringUtil;
import cn.ngame.store.util.ToastUtil;
import cn.ngame.store.widget.BaseGridView;
import cn.ngame.store.widget.UploadFileHttp;
import cn.ngame.store.widget.mulpicture.MulPictureActivity;

/**
 * 发帖
 * Created by gp on 2017/2/15 0015.
 */

public class PostMsgActivity extends CommonBaseActivity implements View.OnClickListener {

    private int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    private LinearLayout ll_back;
    private TextView tv_title, tv_rightTxt;
    private TextView tv_choose, tv_type, tv_choose_game;
    private EditText et_title, et_content;
    int type;
    String gameValue;
    private String strFile2 = "";
    private String title;
    private String content;
    private HashMap<String, File> files; //图片集合

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_msg_activity);
        init();
    }

    private void init() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_rightTxt = (TextView) findViewById(R.id.tv_rightTxt);
        tv_title.setText("发帖");
        tv_rightTxt.setText("发送");
        tv_choose = (TextView) findViewById(R.id.tv_choose);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_choose_game = (TextView) findViewById(R.id.tv_choose_game);
        iv_upload = (ImageView) findViewById(R.id.iv_upload);
        gridView = (BaseGridView) findViewById(R.id.horizontal_gridview);
        tv_info = (TextView) findViewById(R.id.tv_info);
        ll_back.setOnClickListener(this);
        tv_rightTxt.setOnClickListener(this);
        tv_choose.setOnClickListener(this);
        tv_type.setOnClickListener(this);
        tv_choose_game.setOnClickListener(this);
        iv_upload.setOnClickListener(this);
        tv_info.setText("添加图片说明（最多9张）");
    }

    @Override
    protected void onResume() {
        super.onResume();
        type = (int) SPUtils.get(this, "choose_module", 0);
        gameValue = (String) SPUtils.get(this, "choose_game", "选择游戏 >>");
        tv_title.setText("发帖");
        tv_rightTxt.setText("发送");
        et_title.setVisibility(View.VISIBLE); //显示编辑框标题
        setEditTextHeight(unitChangeUtil.dp2px(this, 120)); //编辑内容高度
        switch (type) {
            case 0:
                tv_choose.setVisibility(View.VISIBLE);
                tv_type.setVisibility(View.GONE);
                break;
            case 1:
                tv_choose.setVisibility(View.GONE);
                tv_type.setVisibility(View.VISIBLE);
                tv_type.setText("攻略");
                tv_choose_game.setVisibility(View.GONE);
                break;
            case 2:
                tv_choose.setVisibility(View.GONE);
                tv_type.setVisibility(View.VISIBLE);
                tv_type.setText("求助");
                tv_choose_game.setVisibility(View.GONE);
                break;
            case 3:
                tv_choose.setVisibility(View.GONE);
                tv_type.setVisibility(View.VISIBLE);
                tv_type.setText("投票");
                tv_choose_game.setVisibility(View.VISIBLE);
                tv_choose_game.setText(gameValue);
                tv_title.setText("投票");
                tv_rightTxt.setText("发布");
                et_title.setVisibility(View.GONE); //隐藏标题
                setEditTextHeight(unitChangeUtil.dp2px(this, 187)); //编辑内容高度
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right); //搜索页面退出动画
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                this.finish();
                break;
            case R.id.tv_rightTxt:
                // 防止连续点击
                long currentTime = Calendar.getInstance().getTimeInMillis();
                if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                    lastClickTime = currentTime;
                    checkInfo();
                }
                break;
            case R.id.tv_choose:
            case R.id.tv_type:
                startActivity(new Intent(this, ChooseModuleActivity.class));
                break;
            case R.id.tv_choose_game:
                startActivity(new Intent(this, ChooseGameActivity.class));
                break;
            case R.id.iv_upload:
                int choose = 9 - pictures.size();
                if (choose > 0) {
                    int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        intent = new Intent(this, MulPictureActivity.class);
                        bundle = setBundle();
                        bundle.putInt("imageNum", choose);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 101);
                    } else {
                        MPermissions.requestPermissions(this, MulPictureActivity.SDCARD_READ, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                } else {
                    ToastUtil.show(this, "最多只能选择9张照片");
                }
                break;
        }
    }

    private void checkInfo() {
        title = et_title.getText().toString().trim();
        content = et_content.getText().toString().trim();
        if (type == 0) {
            ToastUtil.show(this, "请选择板块");
            return;
        }
        if (type == 3) {
            if (gameValue.equals("选择游戏 >>")) {
                ToastUtil.show(this, "请选择游戏");
                return;
            }
            title = gameValue;
        } else {
            if (StringUtil.isEmpty(title)) {
                ToastUtil.show(this, "标题不能为空");
                return;
            }
            if (title.length() > 30) {
                ToastUtil.show(this, "标题长度1-30");
                return;
            }
        }
        if (StringUtil.isEmpty(content)) {
            ToastUtil.show(this, "正文不能为空");
            return;
        }
        if (SystemUtil.checkNet(this)) {
            submitMsg();
        } else {
            ToastUtil.show(this, "没有网络");
        }
    }

    /**
     * 图片上传+提交数据
     */
    public void submitMsg() {
        DialogHelper.showWaiting(getSupportFragmentManager(), "发送中...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    files = new HashMap<String, File>();
                    if (pictures != null) {
                        for (PictureBean pictureBean : pictures) {
                            File file = new File(pictureBean.getLocalURL());
//                            strFile2 = ((strFile2 + ",").equals(",") ? "" : (strFile2 + ",")) + ImageUtil.getImageStr(file);
                            files.put(file.getName(), file);
                        }
                    }
                    UpLoadBean result = UploadFileHttp.INSTANCE.uploadFile(Constant.WEB_SITE + Constant.URL_UP_LOAD_FILE, files);
                    if (result == null) {
                        return;
                    }
                    if (result.getCode() == 0) {
                        sendHandle(result.getData(), 1);
                    } else {
                        DialogHelper.hideWaiting(getSupportFragmentManager());
                        sendHandle("", 0);
                    }
                } catch (Exception e) {
                    DialogHelper.hideWaiting(getSupportFragmentManager());
                }
            }
        }).start();
    }

    public void sendHandle(String message, int success) {
        Looper.prepare();
        Message msg = new Message();
        msg.what = success;
        msg.obj = message;
        handler.sendMessage(msg);
        Looper.loop();
    }

    /**
     * 更新ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    submitBack(ConvUtil.NS(msg.obj));
                    break;
                case 0:
                    DialogHelper.hideWaiting(getSupportFragmentManager());
                    ToastUtil.show(PostMsgActivity.this, ConvUtil.NS(msg.obj));
                    break;
            }
            pictures.clear();
        }
    };

    /**
     * 发帖
     */
    private void submitBack(String imgs) {
        PostMsgBodyBean bodyBean = new PostMsgBodyBean();
        bodyBean.setPostTagId(type);
        bodyBean.setPostTitle(title);
        bodyBean.setPostContent(content);
        bodyBean.setPostImage(imgs);
        bodyBean.setToken(StoreApplication.token);
        new PostMsgClient(this, bodyBean).observable()
//                .compose(this.<DiscountListBean>bindToLifecycle())
                .subscribe(new ObserverWrapper<NormalDataBean>() {
                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show(PostMsgActivity.this, APIErrorUtils.getMessage(e));
                        DialogHelper.hideWaiting(getSupportFragmentManager());
                    }

                    @Override
                    public void onNext(NormalDataBean result) {
                        DialogHelper.hideWaiting(getSupportFragmentManager());
                        if (result != null && result.getCode() == 0) {
                            ToastUtil.show(PostMsgActivity.this, "发送成功！");
                            PostMsgActivity.this.finish();
                        } else {
                            if (result.getMsg().equals("token信息已过期")) {
                                startActivity(new Intent(PostMsgActivity.this, LoginActivity.class));
                            } else {
                                ToastUtil.show(PostMsgActivity.this, result.getMsg());
                            }
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (arg0 == 101) {
            if (arg1 == 9) {
                iv_upload.setVisibility(View.GONE);
                tv_info.setVisibility(View.GONE);
            } else if (arg1 > 0) {
                tv_info.setVisibility(View.GONE);
            } else {
                iv_upload.setVisibility(View.VISIBLE);
//                tv_info.setVisibility(View.VISIBLE);
            }
            setIntent(arg2);
            getBundle();
            setGridView();
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    public void setEditTextHeight(int height) {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) et_content.getLayoutParams();
        linearParams.height = height;
        et_content.setLayoutParams(linearParams);
    }

    //读取手机存储
    @PermissionGrant(MulPictureActivity.SDCARD_READ)
    public void requestSdcardReadSuccess() {
        int choose = 9 - pictures.size();
        intent = new Intent(this, MulPictureActivity.class);
        bundle = setBundle();
        bundle.putInt("imageNum", choose);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(bundle);
        startActivityForResult(intent, 101);
    }

    //读取手机存储
    @PermissionDenied(MulPictureActivity.SDCARD_READ)
    public void requestSdcardReadFailed() {
        ToastUtil.show(this, "用户拒绝读写手机存储!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPUtils.remove(this, "choose_module");
        SPUtils.remove(this, "choose_game");
    }
}
