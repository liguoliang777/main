package cn.ngame.store.base.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.bean.VersionInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.GameFileStatus;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.exception.NoSDCardException;
import cn.ngame.store.fragment.SimpleDialogFragment;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.AppInstallHelper;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.view.SimpleTitleBar;

/**
 * 显示 关于信息的 界面
 * Created by zeng on 2016/5/23.
 */
public class AboutActivity extends BaseFgActivity {

    private static final String TAG = AboutActivity.class.getSimpleName();
    private Context context;

    private ProgressBar progressBar;

    private RemoteViews remoteViews = null;
    private Notification notification = null;
    private NotificationManager mNotificationManager = null;

    private VersionInfo versionInfo = null;

    private boolean isRunningBackground = false;
    private boolean isDownloading = false;
    private boolean isChecking = false;

    private TextView tv_version;


    private Timer timer = new Timer();
    private Handler handler = new Handler();

    private IFileLoad fileLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_about);
        context = getApplicationContext();
        SimpleTitleBar title_bar = (SimpleTitleBar) findViewById(R.id.title_bar);
        title_bar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_version = (TextView) findViewById(R.id.text1);
        tv_version.setText(CommonUtil.getVersionName(this));

        fileLoad = FileLoadManager.getInstance(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SimpleDialogFragment prev = (SimpleDialogFragment) getSupportFragmentManager().findFragmentByTag("progressDialog");
        if (prev != null) {
            prev.dismiss();
            ft.remove(prev);
        }
        ft.commit();
    }

    /**
     * 处理检测更新按钮点击事件
     *
     * @param view 被点击的按钮
     */
    public void onUpdateClick(View view) {

        if (isChecking) {
            return;
        }

        if (isDownloading) {
            if (isRunningBackground) {
                Toast.makeText(AboutActivity.this, "正在后台为您更新！", Toast.LENGTH_SHORT).show();
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
                    if (localVersion < versionInfo.versionCode) {
                        showUpdateDialog();
                        CommonUtil.verifyStoragePermissions(AboutActivity.this); //申请读写SD卡权限
                    } else {
                        Toast.makeText(context, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "检测失败：服务端异常！", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "HTTP请求成功：服务端返回错误！");
                }
                isChecking = false;
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Toast.makeText(context, "检测失败，请检查网络连接!", Toast.LENGTH_SHORT).show();
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
        dialogFragment.setTitle("更新");
        dialogFragment.setDialogWidth(250);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_update, null);
        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_title.setText("发现新版本：" + versionInfo.versionName);
        TextView tv_summary = (TextView) contentView.findViewById(R.id.tv_summary);
        tv_summary.setText(versionInfo.content);

        dialogFragment.setContentView(contentView);

        dialogFragment.setPositiveButton(R.string.update_later, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
            }
        });
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
        dialogFragment.setTitle("更新");
        dialogFragment.setDialogWidth(250);

        LayoutInflater inflater = getLayoutInflater();
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_download, null);
        progressBar = (ProgressBar) contentView.findViewById(R.id.progress_bar);

        dialogFragment.setContentView(contentView);

        dialogFragment.setPositiveButton(R.string.update_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();

                fileLoad.delete(versionInfo.url);

                if (mNotificationManager != null) {
                    mNotificationManager.cancel(1);
                }
                isDownloading = false;
            }
        });

        dialogFragment.setNegativeButton(R.string.update_background, new View.OnClickListener() {
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
        });
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
                                            .setContentIntent(PendingIntent.getActivity(AboutActivity.this, 0, intent, 0))
                                            .build();
                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    isRunningBackground = false;
                                } else {
                                    remoteViews.setTextViewText(R.id.text1, "正在下载: " + (int) process + "%");
                                }
                                mNotificationManager.notify(1, notification);
                            } else {

                                Log.d(TAG, "--------------------------->>>> APP更新进度　" + (int) process);
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
