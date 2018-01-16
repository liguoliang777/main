package cn.ngame.store.activity.manager;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.adapter.DownLoadCenterAdapter;
import cn.ngame.store.base.fragment.BaseSearchFragment;
import cn.ngame.store.bean.PageAction;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.view.ActionItem;
import cn.ngame.store.view.QuickAction;


/**
 * 下载更新fragment (懒加载-当滑动到当前fragment时，才去加载。而不是进入到activity时，加载所有fragment)
 * Created by gp on 2017/3/3 0003.
 */

public class DownloadCenterFragment extends BaseSearchFragment {

    ListView listView;
    private PageAction pageAction;
    public static int PAGE_SIZE = 10;
    private int typeValue;
    private String type;
    protected QuickAction mItemClickQuickAction;
    private IFileLoad fileLoad;

    private DownLoadCenterAdapter alreadyLvAdapter;
    /**
     * 当前点击的列表 1.下载列表 2.完成列表
     */
    private int itemType;
    private int itemPosition;
    private FragmentActivity content;
    private List<FileLoadInfo> alreadyList;
    private TextView emptyTv;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_installed;
    }

    @Override
    protected void initViewsAndEvents(View view) {
        content = getActivity();
        listView = (ListView) view.findViewById(R.id.listView);
        emptyTv = (TextView) view.findViewById(R.id.empty_tv);
        emptyTv.setText("您还没有下载任务哦~");
        fileLoad = FileLoadManager.getInstance(content);
        initPop();

        alreadyLvAdapter = new DownLoadCenterAdapter(content, getSupportFragmentManager(), mItemClickQuickAction);
        listView.setAdapter(alreadyLvAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (alreadyLvAdapter != null) {
            alreadyLvAdapter.clean();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        reLoadFileInfo();
    }

    private void reLoadFileInfo() {
        alreadyList = fileLoad.getLoadedFileInfo();
        if (alreadyList == null || alreadyList.size() <= 0) {
            emptyTv.setVisibility(View.VISIBLE);
        } else {
            emptyTv.setVisibility(View.INVISIBLE);
        }
        alreadyLvAdapter.setDate(alreadyList);
    }

    private void initPop() {
        // 设置Action
        mItemClickQuickAction = new QuickAction(getActivity(), QuickAction.VERTICAL);
        ActionItem pointItem = new ActionItem(1, "删除安装包", null);
        mItemClickQuickAction.addActionItem(pointItem);

        mItemClickQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                if (pos == 0) {
                    //删除文件下载任务
                    FileLoadInfo fileInfo = (FileLoadInfo) alreadyLvAdapter.getItem(itemPosition);
                    //删除下载任务
                    fileLoad.delete(fileInfo.getUrl());
                    //取消弹出框
                    mItemClickQuickAction.dismiss();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    reLoadFileInfo();
                }

            }
        });
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected View getLoadView(View view) {
        return null;
    }
}
