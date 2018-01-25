package cn.ngame.store.activity.manager;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.adapter.InstalledGameAdapter;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.AppInstallHelper;
import cn.ngame.store.core.utils.FileUtil;
import cn.ngame.store.view.ActionItem;
import cn.ngame.store.view.QuickAction;

/**
 * 下载更新fragment (懒加载-当滑动到当前fragment时，才去加载。而不是进入到activity时，加载所有fragment)
 * Created by gp on 2017/3/3 0003.
 */
@SuppressLint({"ValidFragment", "WrongConstant"})
public class ManagerFragment extends Fragment {
    final static String TAG = ManagerFragment.class.getSimpleName();
    ListView listView;
    public static int PAGE_SIZE = 10;
    protected QuickAction mItemClickQuickAction;
    private IFileLoad fileLoad;
    private InstalledGameAdapter alreadyLvAdapter;
    private FragmentActivity content;
    private boolean mHidden = false;
    private PackageInfo mfileUnstalledInfo;
    private PackageManager packageManager;
    private String pkgNameListStr = "";
    private List<FileLoadInfo> openFileInfoList;
    private List<PackageInfo> packageInfos = new ArrayList<>();
    private PackageInfo packageInfo = new PackageInfo();
    private ApplicationInfo applicationInfo;
    private TextView emptyTv;
    private int oldLength;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        View view = inflater.inflate(R.layout.fragment_manager, container, false);
        listView = (ListView) view.findViewById(R.id.manager_lv);
        emptyTv = (TextView) view.findViewById(R.id.manager_empty_tv);
        emptyTv.setText("列表为空~");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: ");
        content = getActivity();
        packageManager = content.getPackageManager();

        initPop();
        initListView();
    }


    public void initListView() {
        alreadyLvAdapter = new InstalledGameAdapter(content, content.getSupportFragmentManager(),
                mItemClickQuickAction);
        listView.setAdapter(alreadyLvAdapter);
        fileLoad = FileLoadManager.getInstance(content);
    }

    private List<PackageInfo> localAppList = new ArrayList<>();

    private JSONArray jsonArray = new JSONArray();


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        //获取本地
        try {
            pkgNameListStr = FileUtil.readFile();
            if (null != pkgNameListStr) {
                jsonArray = new JSONArray(pkgNameListStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        oldLength = jsonArray.length();
        //获取数据库 =>  添加
        openFileInfoList = fileLoad.getOpenFileInfo();
        for (FileLoadInfo openFileInfo : openFileInfoList) {
            String gamePackageName = openFileInfo.getPackageName();
            if (pkgNameListStr == null || !pkgNameListStr.contains(gamePackageName)) {
                jsonArray.put(gamePackageName);
            }
        }
        if (jsonArray.length() > oldLength) {
            FileUtil.writeFile2SDCard(jsonArray.toString());
            pkgNameListStr = FileUtil.readFile();
        }
        if (null != alreadyLvAdapter) {
            alreadyLvAdapter.setDate(getLocalApp());
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(TAG, "onHiddenChanged: ");
    }

    private List<PackageInfo> getLocalApp() {
        packageInfos = packageManager.getInstalledPackages(0);
        localAppList.clear();
        if (pkgNameListStr != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                packageInfo = packageInfos.get(i);
                applicationInfo = packageInfo.applicationInfo;
                //非系统应用
                if (null != applicationInfo) {
                    if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) <= 0) {
                        String packageName = applicationInfo.packageName;
                        //如果包名
                        if (pkgNameListStr.contains(packageName) && !"cn.ngame.store".equals
                                (packageName)) {
                            localAppList.add(packageInfo);
                        }
                    }
                }
            }
            if (localAppList == null || localAppList.size() == 0) {
                emptyTv.setVisibility(View.VISIBLE);
            } else {
                emptyTv.setVisibility(View.GONE);
            }
        } else {
            emptyTv.setVisibility(View.VISIBLE);
        }
        return localAppList;
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void initPop() {
        // 设置Action
        mItemClickQuickAction = new QuickAction(content, QuickAction.VERTICAL);
        ActionItem pointItem = new ActionItem(0, "卸载", null);
        mItemClickQuickAction.addActionItem(pointItem);
        mItemClickQuickAction.setOnActionItemClickListener(new QuickAction
                .OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                if (pos == 0) {
                    //删除文件下载任务
                    mfileUnstalledInfo = alreadyLvAdapter.getItemInfo();
                    //卸载
                    String packageName = mfileUnstalledInfo.applicationInfo.packageName;

                    AppInstallHelper.unstallApp(content, packageName);

                    if (null == packageName) {
                        return;
                    }
                    //删除安装包和正在下载的文件
                    List<FileLoadInfo> loadingFileInfo = fileLoad.getOpenFileInfo();
                    for (FileLoadInfo fileLoadInfo : loadingFileInfo) {
                        if (packageName.equals(fileLoadInfo.getPackageName())) {
                            fileLoad.delete(fileLoadInfo.getUrl());
                        }

                    }
                    mItemClickQuickAction.dismiss();
                    source.dismiss();
                }

            }
        });
    }
}
