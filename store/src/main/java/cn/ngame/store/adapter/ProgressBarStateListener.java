package cn.ngame.store.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
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

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.bean.JsonResult;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.fileload.UnZipFileTask;
import cn.ngame.store.core.net.GsonRequest;
import cn.ngame.store.core.utils.AppInstallHelper;
import cn.ngame.store.core.utils.CommonUtil;
import cn.ngame.store.core.utils.Constant;
import cn.ngame.store.core.utils.DialogHelper;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.exception.NoSDCardException;
import cn.ngame.store.fragment.OneBtDialogFragment;
import cn.ngame.store.fragment.SimpleDialogFragment;
import cn.ngame.store.view.GameLoadProgressBar;

/**
 * 处理处理游戏下载按钮点击后的相关操作
 * Created by zeng on 2016/10/26.
 */
public class ProgressBarStateListener implements GameLoadProgressBar.OnStateChangeListener {

    public static final String TAG = ProgressBarStateListener.class.getName();

    private Context context;
    private FileLoadManager manager;
    private FragmentManager fm;
    private Handler handler = new Handler();

    public ProgressBarStateListener(Context context, FragmentManager fm) {
        this.context = context;
        this.fm = fm;
        this.manager = FileLoadManager.getInstance(context);
    }

    @Override
    public void onStartDownload(FileLoadInfo info) {
        //Toast.makeText(context,"我开始下载了",Toast.LENGTH_SHORT).show();
        int res = manager.load(info.getName(), info.getUrl(), info.getMd5(), info.getPackageName(), info.getVersionCode(), info
                .getTitle(), info.getPreviewUrl(), info.getServerId(), StoreApplication.allowAnyNet);
        if (res == IFileLoad.RESULT_NO_NET) {
            Toast.makeText(context, "无网络，请检查网络连接", Toast.LENGTH_SHORT).show();
        } else if (res == IFileLoad.RESULT_PARAMES_ERROR) {
            Toast.makeText(context, "资源异常，无法下载", Toast.LENGTH_SHORT).show();
        } else if (res == IFileLoad.RESULT_4G_NET) {
            showUpdateDialog(info);
        }
        updateDownloadCount(info);
    }

    @Override
    public void updateApp(FileLoadInfo info) {

        int res = manager.load(info.getName(), info.getUrl(), info.getMd5(), info.getPackageName(), info.getVersionCode(), info
                .getTitle(), info.getPreviewUrl(), info.getServerId(), StoreApplication.allowAnyNet);
        if (res == IFileLoad.RESULT_NO_NET) {
            Toast.makeText(context, "无网络，请检查网络连接", Toast.LENGTH_SHORT).show();
        } else if (res == IFileLoad.RESULT_PARAMES_ERROR) {
            Toast.makeText(context, "资源异常，无法下载", Toast.LENGTH_SHORT).show();
        } else if (res == IFileLoad.RESULT_4G_NET) {
            showUpdateDialog(info);
        }
    }

    @Override
    public void onPauseDownload(FileLoadInfo info) {

        manager.pause(info.getUrl());
    }

    @Override
    public void onRestartDownload(FileLoadInfo info) {
        //Toast.makeText(context,"继续下载",Toast.LENGTH_SHORT).show();
        int res = manager.load(info.getName(), info.getUrl(), info.getMd5(), info.getPackageName(), info.getVersionCode(), info
                .getTitle(), info.getPreviewUrl(), info.getServerId(), StoreApplication.allowAnyNet);
        if (res == IFileLoad.RESULT_NO_NET) {
            Toast.makeText(context, "无网络，请检查网络连接", Toast.LENGTH_SHORT).show();
        } else if (res == IFileLoad.RESULT_PARAMES_ERROR) {
            Toast.makeText(context, "资源异常，无法下载", Toast.LENGTH_SHORT).show();
        } else if (res == IFileLoad.RESULT_4G_NET) {
            showUpdateDialog(info);
        }
    }

    @Override
    public void onInstallApp(FileLoadInfo info) {

        String fileName = info.getName();
        if (fileName != null && fileName.endsWith(".ngk")) {

            try {
                String fName = fileName.split("\\.")[0];

                File file = new File(CommonUtil.getFileLoadBasePath(), fileName);
                File newFile;
                if (file.exists() && file.isFile()) {
                    newFile = new File(CommonUtil.getFileLoadBasePath(), fName + ".zip");
                    file.renameTo(newFile);
                } else {
                    newFile = new File(CommonUtil.getFileLoadBasePath(), fName + ".zip");
                }

                if (!newFile.exists() || !newFile.isFile()) {
                    Toast.makeText(context, "安装文件丢失，请重新下载", Toast.LENGTH_LONG).show();
                    manager.delete(info.getUrl());
                    return;
                }

                DialogHelper.showWaiting(fm, "Waiting...");//正在解压
                new Thread(new UnZipFileTask(fm, context, info, handler)).start();

            } catch (NoSDCardException e) {
                e.printStackTrace();
            }

        } else if (fileName != null && fileName.endsWith(".apk")) {
            try {
                File file = new File(CommonUtil.getFileLoadBasePath(), fileName);
                if (!file.exists() || !file.isFile()) {
                    manager.delete(info.getUrl());
                    showApkLostDialog();
                    return;
                }
            } catch (NoSDCardException e) {
                e.printStackTrace();
            }

            AppInstallHelper.installApk(context, info.getName());
        }
    }

    @Override
    public void onOpenApp(FileLoadInfo info) {
        //Toast.makeText(context,"我要打开APP："+info.packages,Toast.LENGTH_SHORT).show();
        AppInstallHelper.openApp(context, info.getPackageName());
    }

    //安装包丢失对话框
    private void showApkLostDialog() {
        final OneBtDialogFragment dialogFragment = new OneBtDialogFragment();
        dialogFragment.setTitle(R.string.install_apk_lost_redownload);
        dialogFragment.setDialogWidth(context.getResources().getDimensionPixelSize(R.dimen.unlogin_dialog_width));
        dialogFragment.setNegativeButton(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
            }
        });
        dialogFragment.show(fm.beginTransaction(), "successDialog");
    }

    /**
     * 显示更新对话框
     */
    private void showUpdateDialog(final FileLoadInfo info) {

        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("4g_net");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final SimpleDialogFragment dialogFragment = new SimpleDialogFragment();
        dialogFragment.setCancelable(false);
        dialogFragment.setDialogWidth(250);


        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.layout_dialog_update, null);
        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);

        TextView tv_summary = (TextView) contentView.findViewById(R.id.tv_summary);
        tv_summary.setText("当前非WIFI网络，继续下载将使用运营商流量！");

        dialogFragment.setContentView(contentView);

        dialogFragment.setNegativeButton("继续下载", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();

                manager.load(info.getName(), info.getUrl(), info.getMd5(), info.getPackageName(), info.getVersionCode(), info
                        .getTitle(), info.getPreviewUrl(), info.getServerId(), true);
            }
        });
        dialogFragment.setPositiveButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFragment.dismiss();
            }
        });
        dialogFragment.show(ft, "4g_net");
    }

    /**
     * 调用接口，更新下载次数
     */
    private void updateDownloadCount(final FileLoadInfo info) {

        String url = Constant.WEB_SITE + Constant.URL_GAME_CENSUS;
        Response.Listener<JsonResult> successListener = new Response.Listener<JsonResult>() {
            @Override
            public void onResponse(JsonResult result) {

                if (result == null || result.code != 0) {
                    return;
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                Log.d(TAG, "HTTP请求失败：网络连接错误！");
            }
        };

        Request<JsonResult> request = new GsonRequest<JsonResult>(Request.Method.POST, url,
                successListener, errorListener, new TypeToken<JsonResult>() {
        }.getType()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("gameId", String.valueOf(info.getServerId()));
                return params;
            }
        };
        StoreApplication.requestQueue.add(request);

    }
}
