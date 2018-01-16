package cn.ngame.store.user.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.User;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.FileUtil;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.core.utils.UrlConstant;
import cn.ngame.store.exception.NoSDCardException;
import cn.ngame.store.fragment.OneBtDialogFragment;
import cn.ngame.store.util.ToastUtil;


/**
 * 用户中心界面
 * Created by zeng on 2016/5/26.
 */
public class UserCenterActivity extends BaseFgActivity {

    public static final String TAG = UserCenterActivity.class.getSimpleName();
    private UserCenterActivity content;
    private String pwd;

    private ImageView img_photo;
    private TextView tv_account;
    private EditText tv_nickname;

    private String nickName;

    private SharedPreferences preferences;
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 1458;

    private String mCurrentPhotoPath;

    private File mTempDir;
    private User user;
    private String imgStrPost = "";
    private String avatarUrl;
    private String LOGIN_TYPE;
    private String IMG_TYPE = "-1";//0表示用户有图片地址  1 表示用户选的本地，base64字符串。
    private SharedPreferences.Editor editor;
    private ArrayAdapter<String> mAdapter;
    private Dialog defAvatarDialog;
    private FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        this.setContentView(R.layout.activity_user_center);
        content = UserCenterActivity.this;
        fm = getSupportFragmentManager();
        preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        editor = preferences.edit();
        findViewById(R.id.left_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView centerTv = (TextView) findViewById(R.id.center_tv);
        centerTv.setText("个人资料设置");
        LOGIN_TYPE = StoreApplication.loginType;

        Button changePwdBt = (Button) findViewById(R.id.change_pwd_bt);
        if (Constant.PHONE.equals(LOGIN_TYPE)) {
            changePwdBt.setVisibility(View.VISIBLE);
            changePwdBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserCenterActivity.this.content, ChangePwdActivity.class);
                    intent.putExtra(KeyConstant.IS_FROM_USER_CENTER, true);
                    startActivity(intent);
                    UserCenterActivity.this.content.finish();
                }
            });
        } else {
            changePwdBt.setVisibility(View.INVISIBLE);
        }
        try {
            mTempDir = new File(CommonUtil.getImageBasePath());
        } catch (NoSDCardException e) {
            e.printStackTrace();
        }
        if (mTempDir != null && !mTempDir.exists()) {
            mTempDir.mkdirs();
        }
        img_photo = (ImageView) findViewById(R.id.img_photo);
        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_nickname = (EditText) findViewById(R.id.tv_nickname);

        img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改头像
                showChangeAvatarDialog();

            }
        });
       /* user = StoreApplication.user;*/
        pwd = StoreApplication.passWord;
        imgStrPost = StoreApplication.userHeadUrl;
        nickName = StoreApplication.nickName;
        setUserInfo();
     /*   if (pwd == null) {
            //getUserInfo();
            android.util.Log.d(TAG, "user == null");
            LoginHelper loginHelper = new LoginHelper(UserCenterActivity.this);
            loginHelper.reLogin();
            //setUserInfo();
        } else {
            setUserInfo();
        }*/


        //*/重新登录
     /*   LoginHelper loginHelper = new LoginHelper(this);
        loginHelper.reLogin();*/
        TextView titleRightBt = (TextView) findViewById(R.id.title_right_tv);
        titleRightBt.setVisibility(View.VISIBLE);
        titleRightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickNameStr = tv_nickname.getText().toString();
                if (nickNameStr.length() == 0) {
                    ToastUtil.show(UserCenterActivity.this.content, "昵称为空哦！");
                    return;
                }
                if (nickNameStr.equals(nickName) && "-1".equals(IMG_TYPE)) {
                    ToastUtil.show(UserCenterActivity.this.content, "您未修改任何资料哦");
                    //content.finish();
                } else {
                    nickName = nickNameStr;
                    uploadImage();
                }
            }
        });

        android.util.Log.d(TAG, "userTOKEN:" + StoreApplication.token);
        android.util.Log.d(TAG, "userCode:" + StoreApplication.userCode);

        //默认头像地址
        for (int i = 1; i < 21; i++) {
            if (i < 10) {
                mUrlList.add(UrlConstant.RECOMMED_URL_START + "0" + i + ".png");
            } else {
                mUrlList.add(UrlConstant.RECOMMED_URL_START + i + ".png");
            }
        }
        defAvatarDialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog_recommend_avatar, null);
        GridView gridView = (GridView) inflate.findViewById(R.id.recommend_grid_view);
        gridView.setAdapter(new AvatarAdapter());
        defAvatarDialog.setContentView(inflate);//将布局设置给Dialog
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imgStrPost = mUrlList.get(position);
                imageLoader.displayImage(imgStrPost, img_photo);
                IMG_TYPE = "0";
            }
        });
    }

    //修改头像
    public void showChangeAvatarDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog_change_avatar, null);

        View.OnClickListener mDialogClickLstener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                int id = v.getId();
                if (id == R.id.choose_local_tv) {//本地相册
                    Crop.pickImage(UserCenterActivity.this);
                } else if (id == R.id.choose_camera_tv) {//相机
                    getImageFromCamera();
                } else if (id == R.id.choose_recomend_tv) {
                    //选择推荐头像
                    setDialogWindow(defAvatarDialog);
                }
            }
        };
        inflate.findViewById(R.id.choose_local_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_recomend_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_camera_tv).setOnClickListener(mDialogClickLstener);
        inflate.findViewById(R.id.choose_cancel_tv).setOnClickListener(mDialogClickLstener);

        dialog.setContentView(inflate);//将布局设置给Dialog
        setDialogWindow(dialog);
    }

    private List<String> mUrlList = new ArrayList<>();


    //默认头像适配器
    public class AvatarAdapter extends BaseAdapter {
        public AvatarAdapter() {
            super();
        }

        @Override
        public int getCount() {
            return mUrlList.size();
        }

        @Override
        public Object getItem(int position) {
            return mUrlList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            AvatarAdapter.ViewHolder holder;
            if (convertView == null) {
                holder = new AvatarAdapter.ViewHolder();
                convertView = View.inflate(parent.getContext(), R.layout.gridview_image_view_item, null);
                holder.mIconIv = (SimpleDraweeView) convertView.findViewById(R.id.recommend_icon_gv_iv);
                convertView.setTag(holder);
            } else {
                holder = (AvatarAdapter.ViewHolder) convertView.getTag();
            }
            holder.mIconIv.setImageURI(mUrlList.get(position));
            return convertView;
        }

        class ViewHolder {
            private SimpleDraweeView mIconIv;
        }
    }

    //退出登录
    public void showLogoutDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog_From_Bottom_Style);
        //填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_dialog_logout, null);

        inflate.findViewById(R.id.logout_yes_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                logoutClearData();
                startActivity(new Intent(content, LoginActivity.class));
                UserCenterActivity.this.finish();
            }
        });
        inflate.findViewById(R.id.logout_cancel_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
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

    protected void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = "Temp_camera" + String.valueOf(System.currentTimeMillis());
        File cropFile = new File(mTempDir, fileName);
        Uri fileUri = Uri.fromFile(cropFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file

        mCurrentPhotoPath = fileUri.getPath();
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Crop.REQUEST_PICK) {
                beginCrop(result.getData());
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, result);
            } else if (requestCode == REQUEST_CODE_CAPTURE_CAMERA) {
                if (mCurrentPhotoPath != null) {
                    beginCrop(Uri.fromFile(new File(mCurrentPhotoPath)));
                }
            }
        }
    }

    private void beginCrop(Uri source) {
        String fileName = "Temp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File cropFile = new File(mTempDir, fileName);
        Uri outputUri = Uri.fromFile(cropFile);
        new Crop(source).output(outputUri).asSquare().start(this);
    }

    DisplayImageOptions roundOptions = FileUtil.getRoundOptions(R.color.transparent, 360);

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = Crop.getOutput(result);
            //StoreService.uploadImage(file);
            avatarUrl = uri.toString();
            imageLoader.displayImage(avatarUrl, img_photo, roundOptions);
            // 上传图片
            String path = uri.getPath();
            File file = new File(path);
            imgStrPost = ImageUtil.getImageStr(file);
            IMG_TYPE = "1";
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadImage() {
        DialogHelper.showWaiting(fm, "加载中...");
        String url = Constant.WEB_SITE + Constant.URL_MODIFY_USER_DATA;
        Response.Listener<JsonResult<User>> successListener =
                new Response.Listener<JsonResult<User>>() {
                    @Override
                    public void onResponse(JsonResult<User> result) {
                        int code = result.code;
                        if (code == 0) {
                            ToastUtil.show(content, "资料修改成功!");
                            User user = result.data;
                            editor.putString(Constant.CONFIG_TOKEN, user.token);
                            editor.putString(Constant.CONFIG_USER_HEAD, user.headPhoto);
                            editor.putString(Constant.CONFIG_NICK_NAME, nickName);
                            editor.putBoolean(KeyConstant.AVATAR_HAS_CHANGED, true);
                            editor.putString(Constant.CONFIG_USER_CODE, user.userCode);//userCode
                            editor.apply();

                            StoreApplication.token = user.token;
                            StoreApplication.userHeadUrl = user.headPhoto;
                            StoreApplication.nickName = nickName;
                            StoreApplication.userCode = user.userCode;
                            UserCenterActivity.this.finish();
                        } else if (code >= -4 && code <= -1) {
                            android.util.Log.d(TAG, "返回: " + code + result.msg);
                            if (content != null && !content.isFinishing()) {
                                showReLoginDialog();
                            }
                            //需要重新登录
                            logoutClearData();
                            //UserCenterActivity.this.finish();
                        } else {
                            Toast.makeText(UserCenterActivity.this, "修改失败！", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "HTTP请求成功：修改失败！" + code + result.msg);
                        }
                        //隐藏提示框
                        DialogHelper.hideWaiting(fm);
                    }
                };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                DialogHelper.hideWaiting(fm);
                Toast.makeText(UserCenterActivity.this, "修改失败，网络连接异常！", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！" + volleyError.getMessage());
            }
        };

        Request<JsonResult<User>> versionRequest = new GsonRequest<JsonResult<User>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<User>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(KeyConstant.USER_CODE, StoreApplication.userCode);
                params.put(KeyConstant.PICTURE_STR, imgStrPost);
                params.put(KeyConstant.GENDER, "男");
                params.put(KeyConstant.TYPE, IMG_TYPE);//type 0表示用户选的新图片，base64字符串。      1表示用户有图片地址
                params.put(KeyConstant.NICK_NAME, nickName);
                params.put(KeyConstant.TOKEN, StoreApplication.token);

                return params;
            }
        };
   /*     versionRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/

        StoreApplication.requestQueue.add(versionRequest);
    }

    /**
     * 显示结果对话框
     */
    private void showReLoginDialog() {
        final OneBtDialogFragment dialogFragment = new OneBtDialogFragment();
        dialogFragment.setTitle(R.string.relogin_msg);
        dialogFragment.setDialogWidth(content.getResources().getDimensionPixelSize(R.dimen.unlogin_dialog_width));
        dialogFragment.setNegativeButton(R.string.login_now, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
                content.startActivity(new Intent(content, LoginActivity.class));
                content.finish();
            }
        });
        dialogFragment.show(fm, "successDialog");
    }

    ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * 处理退出操作
     */
    public void onLogoutClick(View view) {
        showLogoutDialog();
    }

    //退出登录
    private void logoutClearData() {
        SharedPreferences preferences = getSharedPreferences(Constant.CONFIG_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constant.CONFIG_USER_PWD, "");
        editor.putString(Constant.CONFIG_LOGIN_TYPE, Constant.PHONE);
        editor.putBoolean(KeyConstant.AVATAR_HAS_CHANGED, true);
        editor.apply();

        StoreApplication.userHeadUrl = "";
        StoreApplication.loginType = Constant.PHONE;
        StoreApplication.nickName = "";
        StoreApplication.userCode = "";
        StoreApplication.userName = "";
        StoreApplication.passWord = "";
        StoreApplication.token = null;
        StoreApplication.user = null;
    }


    private void setUserInfo() {
        DisplayImageOptions roundOptions = FileUtil.getRoundOptions(R.drawable.ic_def_logo_188_188, 360);
        imageLoader.displayImage(StoreApplication.userHeadUrl, img_photo, roundOptions);
        tv_nickname.setText(nickName);
        tv_nickname.setSelection(nickName.length());
        tv_account.setText(StoreApplication.userName);
    }

/* private void showChangeNicknameDialog() {

        final FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("progressDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();

        dialogFragment.setTitle("昵称修改");
        dialogFragment.setDialogWidth(250);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_edit, null);
        final EditText editText = (EditText) contentView.findViewById(R.id.et_content);
        dialogFragment.setContentView(contentView);

        dialogFragment.setPositiveButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
            }
        });

        dialogFragment.setNegativeButton(R.string.sure, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickName = editText.getText().toString();
                if (nickName.length() > 0) {
                    if (nickName.length() > 13) {
                        Toast.makeText(UserCenterActivity.this, "亲，您的昵称太长了！", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (!TextUtil.isLegal(nickName.trim(), "[A-Za-z0-9\\u4e00-\\u9fa5_]+")) {
                        Toast.makeText(UserCenterActivity.this, "亲，只允许中文，英文，数字等字符哦！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dialogFragment.dismiss();
                    changeNickname();
                } else {
                    Toast.makeText(UserCenterActivity.this, "昵称不能为空哦！", Toast.LENGTH_SHORT).show();
                }


            }
        });
        dialogFragment.show(ft, "progressDialog");

    }*/
    /**
     * 修改昵称
     */
   /* private void changeNickname() {

        final String url = Constant.WEB_SITE + Constant.URL_CHANGE_NICKNAME;
        Response.Listener<JsonResult> successListener = new Response.Listener<JsonResult>() {
            @Override
            public void onResponse(JsonResult result) {
                if (result == null) {
                    Toast.makeText(UserCenterActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0) {
                    if (nickName != null) {
                        nickName = nickName.length() > 10 ? nickName.substring(0, 10) : nickName;
                    }
                    tv_nickname.setText(nickName);
                    if (user != null) {
                        user.nickName = nickName;
                    }
                    if (user != null) {
                        user.nickName = nickName;
                        StoreApplication.nickName = nickName;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Constant.CONFIG_NICK_NAME, nickName);
                        editor.apply();
                    }
                    Toast.makeText(UserCenterActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
                    Toast.makeText(UserCenterActivity.this, result.msg, Toast.LENGTH_SHORT).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                ToastUtil.show(UserCenterActivity.this, "修改失败，请检查网络连接!");
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult> versionRequest = new GsonRequest<JsonResult>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("token", StoreApplication.token);
                params.put("userCode", StoreApplication.userCode);
                params.put("newNickName", nickName);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }*/

    /*   *//**
     * 获取用户信息
     *//*
    private void getUserInfo() {
        final String url = Constant.WEB_SITE + Constant.URL_USER_INFO;
        Response.Listener<JsonResult<User>> successListener = new Response.Listener<JsonResult<User>>() {
            @Override
            public void onResponse(JsonResult<User> result) {
                if (result == null) {
                    Toast.makeText(UserCenterActivity.this, "服务端异常", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.code == 0 && result.data != null) {
                    user = result.data;
                    user = user;
                    String userHeadPhoto = user.headPhoto;
                    if (userHeadPhoto != null && StoreApplication.userHeadUrl != null &&
                            !StoreApplication.userHeadUrl.equals(userHeadPhoto)) {
                        StoreApplication.userHeadUrl = userHeadPhoto;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Constant.CONFIG_USER_HEAD, userHeadPhoto);
                        editor.apply();
                    } else {
                        StoreApplication.userHeadUrl = userHeadPhoto;
                    }

                    nickName = user.nickName;
                    if (nickName != null) {
                        nickName = nickName.length() > 10 ? nickName.substring(0, 10) : nickName;
                    }
                    tv_nickname.setText(nickName);
                    tv_account.setText(user.mobile);
                    imageLoader.displayImage(StoreApplication.userHeadUrl, img_photo, roundOptions);

                } else {
                    Log.d(TAG, "HTTP请求成功：服务端返回错误: " + result.msg);
                    Toast.makeText(UserCenterActivity.this, "服务端异常，请重新登录！", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Toast.makeText(UserCenterActivity.this, "加载用户信息，请检查网络连接!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult<User>> versionRequest = new GsonRequest<JsonResult<User>>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult<User>>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("token", StoreApplication.token);
                return params;
            }
        };
        StoreApplication.requestQueue.add(versionRequest);
    }*/

}
