package cn.ngame.store.widget.mulpicture;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.FileUtil;
import cn.ngame.store.gamehub.bean.PictureBean;
import cn.ngame.store.gamehub.view.CommonBaseActivity;
import cn.ngame.store.util.StringUtil;
import cn.ngame.store.util.ToastUtil;


/*
 * 列出本地相册中的图片
 * 本地相册
 */
public class MulPictureActivity extends CommonBaseActivity implements View.OnClickListener {
    private LinearLayout ll_back;
    private TextView tv_title;

    private GridView gridView; //显示图片区域
    private MulPictureAdapter adapter; //适配器
    private RelativeLayout showSelectRelativeLayout; //下面显示区域
    private LinearLayout selectPictureLinearLayout;
    private Button sendButton;

    private Map<Integer, ImageView> hashImage = new HashMap<Integer, ImageView>();

    private List<PictureBean> selectList = new ArrayList<PictureBean>();   // 存放当前选中的图片
    private List<PictureBean> list = new ArrayList<PictureBean>(); // 所有图片的路径
    private boolean isShowCamera = true;
    //相机拍照
    private String path;
    private Uri photoUri;
    private String currentUploadFileName;
    private PictureBean pictureBean;
    //其它页面带过来需要带回去的数据
    private int imageNum; //当前可以选择的图片的总数
    private int imageAllNum = 0;
    //如果点击的是拍照，则先前的选择操作取消
    private List<PictureBean> allPicturesTemp;
    private List<PictureBean> picturesTemp;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private int questionId; //发送追问问题
    private int inquiryerId;//问诊人id

    public static final int CAMERA_PERMISSION = 21; //拍照权限
    public static final int SDCARD_WRITER = 22; //sdcard读写权限
    public static final int SDCARD_READ = 23; //sdcard读写权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mul_picture);
        // mStickyLV = getAllPictureUrl();
        getBundle();
        initViews();
    }

    //获得界面传递的参数
    public void getBundle() {
        super.getBundle();
        if (bundle != null) {
//            allPicturesTemp = allPictures;
            picturesTemp = pictures;
            if (pictures != null && pictures.size() > 0) {
                selectList.addAll(pictures);
            }
            imageNum = bundle.getInt("imageNum", 0);
            imageAllNum = bundle.getInt("imageAllNum");
            if (imageAllNum == 0) {
                imageNum = 9;
            } else {
                imageNum = imageAllNum;
            }
            questionId = bundle.getInt("questionId");
            inquiryerId = bundle.getInt("inquiryerId");
        }
    }

    //设置界面传递的参数
    public Bundle setBundle() {
        super.setBundle();
        return bundle;
    }

    //加载控件
    private void initViews() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_back.setVisibility(View.VISIBLE);
        ll_back.setOnClickListener(this);
        tv_title.setText("我的相册");

        gridView = (GridView) findViewById(R.id.gv_mul_picture);
        showSelectRelativeLayout = (RelativeLayout) findViewById(R.id.rl_select_picture_layout);
        selectPictureLinearLayout = (LinearLayout) findViewById(R.id.ll_selected_picture_layout);
        selectPictureLinearLayout.setVisibility(View.GONE);
        sendButton = (Button) findViewById(R.id.btn_mul_picture_submit);
        sendButton.setOnClickListener(this);
//        gridView.setOnScrollListener(new PauseOnScrollListener(imageLoader, true, true));// 两个分别表示拖动下拉条和滑动过程中暂停加载

    }

    private void setAdapter() {
        adapter = new MulPictureAdapter(this, list, onItemClickClass, isShowCamera, selectList);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSubmitValue();
        setAdapter();
        if (list == null || list.size() == 0) {
            getAllImageUrls();
        }
    }

    public void getAllImageUrls() {
        if (!FileUtil.isSDCardEnable()) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        } else {
//            dialog = new AppLoadingDialog(this, "加载中...", 2);
//            dialog.setCancelable(true);
//            dialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    list = new ArrayList<PictureBean>();
                    ContentResolver cr = getContentResolver();
                    String[] pro = {MediaStore.Images.Media._ID, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.DATA};
                    Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, pro, MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_ADDED + " DESC");
                    int size = cursor.getCount();
                    while (cursor.moveToNext()) {
                        PictureBean pictureBean = new PictureBean();
                        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        pictureBean.setLocalURL(path);
                        list.add(pictureBean);
                    }
                    // 通知Handler扫描图片完成
                    mHandler.sendEmptyMessage(0x110);
                }
            }).start();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
//              clearDatas();
                this.finish();
                break;
            case R.id.btn_mul_picture_submit:
                sendfiles();
                break;
        }
    }

    // 调用相机拍照
    public void camera() {
        if (FileUtil.isSDCardEnable()) {
            try {
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                String sdcardPathDir = CommonUtil.getImageBasePath();
                File file = null;
                File fileDir = new File(sdcardPathDir);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                file = new File(sdcardPathDir + "Temp_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                if (file != null) {
                    path = file.getPath();
                    photoUri = Uri.fromFile(file);
                    openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(openCameraIntent, 1);
                }
            } catch (Exception e) {
                ToastUtil.show(this, "内存不足");
            }
        }
    }

    //处理选中图片后返回的操作
    MulPictureAdapter.OnItemClickClass onItemClickClass = new MulPictureAdapter.OnItemClickClass() {
        @Override
        public void OnItemClick(View v, int Position, CheckBox checkBox, View imageOverLay) {
            if (Position == 0) {
                MPermissions.requestPermissions(MulPictureActivity.this, CAMERA_PERMISSION, Manifest.permission.CAMERA);
                //camera();
            } else {
                PictureBean bean = isShowCamera == true ? list.get(Position - 1) : list.get(Position);
                if (checkBox.isChecked()) { //取消选中
                    checkBox.setChecked(false);
                    imageOverLay.setVisibility(View.GONE);
//                  selectPictureLinearLayout.removeView(hashImage.get(Position));
                    for (int i = 0; i < selectList.size(); i++) {
                        if (selectList.get(i).getLocalURL().equals(bean.getLocalURL())) {
                            selectList.remove(i);
                            break;
                        }
                    }
                    // selectList.remove(bean); 从快速查询页把已选择的对象带过来时，remove不掉
                    removePicture(bean);
                    setSubmitValue();
                } else { //选中
                    try {
                        //selectPictureLinearLayout.getChildCount()
                        if (selectList.size() < imageNum) { // 最多上传 张图片
                            //判断所选的图片是否正常,是否损坏
                            Bitmap bitmap = FileUtil.getImageThumbnail(bean.getLocalURL(), 5, 5);
                            if (bitmap == null) {
                                ToastUtil.show(MulPictureActivity.this, "图片" + bean.getLocalURL() + "异常无法上传");
                            } else if (FileUtil.getFileSize(bean.getLocalURL(), FileUtil.SIZETYPE_KB) < 3) {
                                ToastUtil.show(MulPictureActivity.this, "图片清晰度不够");
                            } else {
                                bitmap.recycle();
                                checkBox.setChecked(true);
                                imageOverLay.setVisibility(View.VISIBLE);
                                selectList.add(bean);
                                addPicture(bean);
                                setSubmitValue();
                                // ImageView imageView = iconImage(bean, Position, checkBox, imageOverLay);
                                //if (imageView != null) {
                                // hashImage.put(Position, imageView);
                                // selectList.add(bean);
                                // addPicture(bean);
                                // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                // selectPictureLinearLayout.addView(imageView);
                                // setSubmitValue();
                                // }
                            }
                        } else {
                            ToastUtil.show(MulPictureActivity.this,  "最多只能选择" + imageNum + "张照片");
                        }
                    } catch (Exception e) {
                        ToastUtil.show(MulPictureActivity.this, "异常信息");
                    }

                }

            }
        }
    };

    /**
     * 设置数据
     */
//    public void getTestDatas() {
//        allPictures = new ArrayList<PictureBean>();
//        for (int i = 0; i < 10; i++) {
//            PictureBean bean = new PictureBean();
//            allPictures.add(bean);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: // 调用相机拍照
                if (resultCode == RESULT_OK) {
                    pictureBean = new PictureBean();
                    pictureBean.setLocalURL(path);
                    FileUtil.correctCameraPictureAngle(path);// 纠正部分手机拍照后自动旋转的问题
                    refreshImagePhoto(pictureBean.getLocalURL());// 拍照后刷新，使拍照的图片显示到相册中
//                    pictureBean.setPictureId(FileTools.getLocalImageId(getContentResolver(), path));
                    selectList = new ArrayList<PictureBean>();
                    selectList.add(pictureBean);
//                    allPictures = allPicturesTemp;
                    pictures = picturesTemp;
                    addPicture(pictureBean);
                    sendfiles();
                }
                break;
            default:
                break;
        }
    }

    // 在相册中快速显示图片
    public void refreshImagePhoto(String fileUrl) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (!sdCardExist) {
            return;
        }
        // 将新增加的图片加到系统数据库中去，便于在相册中快速显示图片
        try {
            ContentValues values = new ContentValues();
            values.put("datetaken", new Date().toString());
            values.put("mime_type", "image/png");
            values.put("_data", fileUrl);
            ContentResolver cr = getApplication().getContentResolver();
            cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        } catch (Exception e) {
            // e.printStackTrace();
        }
    }

    //点击提交按钮
    public void sendfiles() {
        intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(setBundle());
        if (getIntent().hasExtra("drugId"))
            intent.putExtra("drugId", imageNum+"");
        setResult(selectList.size(), intent);
        this.finish();
    }


    //显示发送按钮文字
    public void setSubmitValue() {
        sendButton.setText("发送(" + (selectList != null ? selectList.size() : "0") + "/" + imageNum + ")");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("type", android.R.attr.type);
//        outState.putSerializable("allPictures", (Serializable) allPictures);
        outState.putSerializable("pictures", (Serializable) pictures);
        outState.putSerializable("mStickyLV", (Serializable) list);
        outState.putSerializable("selectList", (Serializable) selectList);
        outState.putSerializable("allPicturesTemp", (Serializable) allPicturesTemp);
        outState.putSerializable("picturesTemp", (Serializable) picturesTemp);
        outState.putInt("imageNum", imageNum);
        outState.putString("path", path != null ? path : "");
        outState.putBoolean("isShowCamera", isShowCamera);
        outState.putString("currentUploadFileName", currentUploadFileName);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        if (dialog != null) {
//            dialog.dismiss();
//        }
        if (savedInstanceState != null) {
//            allPictures = (List<PictureBean>) savedInstanceState.getSerializable("allPictures");
            pictures = (List<PictureBean>) savedInstanceState.getSerializable("pictures");
            allPicturesTemp = (List<PictureBean>) savedInstanceState.getSerializable("allPicturesTemp");
            picturesTemp = (List<PictureBean>) savedInstanceState.getSerializable("picturesTemp");
            selectList = (List<PictureBean>) savedInstanceState.getSerializable("selectList");
            list = (List<PictureBean>) savedInstanceState.getSerializable("mStickyLV");

            imageNum = savedInstanceState.getInt("imageNum");
            path = savedInstanceState.getString("path");
            isShowCamera = savedInstanceState.getBoolean("isShowCamera");
            currentUploadFileName = savedInstanceState.getString("currentUploadFileName");

            if (!StringUtil.isTextEmpty(path)) {
                File file = new File(path);
                if (file.exists()) {
                    path = file.getPath();
                    photoUri = Uri.fromFile(file);
                }
            }
        }
    }

    public void addPicture(PictureBean pictureBean) {
//        allPictures.add(pictureBean);
        pictures.add(pictureBean);
    }

    public void removePicture(PictureBean pictureBean) {

        for (int i = 0; i < pictures.size(); i++) {
            if (pictureBean.getLocalURL().equals(pictures.get(i).getLocalURL())) {
                pictures.remove(i);
                break;
            }
        }

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
//            if (dialog != null) {
//                dialog.dismiss();
//            }
            setAdapter();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearDatas();
    }

    // 清除数据，避免内存溢出，特别是引用的图片控件
    public void clearDatas() {
        adapter = null;
        gridView = null;
        hashImage = null;
        list = null;
        selectList = null;
        path = null;
        photoUri = null;
        currentUploadFileName = null;
        pictureBean = null;
    }

    //暂时同意权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //同意权限
    @PermissionGrant(CAMERA_PERMISSION)
    public void requestCameraSuccess() {
        MPermissions.requestPermissions(this, SDCARD_WRITER, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    //拒绝权限
    @PermissionDenied(CAMERA_PERMISSION)
    public void requestCameraFailed() {
        ToastUtil.show(this, "用户拒绝拍照功能!");
    }

    //同意权限
    @PermissionGrant(SDCARD_WRITER)
    public void requestSdcardWriteSuccess() {
        camera();
    }

    //拒绝权限
    @PermissionDenied(SDCARD_WRITER)
    public void requestSdcardWriteFailed() {
        ToastUtil.show(this, "用户拒绝读写手机存储功能!");
    }
}
