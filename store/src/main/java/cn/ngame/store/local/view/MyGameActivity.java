package cn.ngame.store.local.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.DownLoadCenterAdapter;
import cn.ngame.store.core.fileload.FileLoadInfo;
import cn.ngame.store.core.fileload.FileLoadManager;
import cn.ngame.store.core.fileload.IFileLoad;
import cn.ngame.store.core.utils.Log;
import cn.ngame.store.view.ActionItem;
import cn.ngame.store.view.NoScrollListView;
import cn.ngame.store.view.QuickAction;
import cn.ngame.store.view.SimpleTitleBar;

/**
 * 下载记录页面
 * Created by zeng on 2016/5/30.
 */
public class MyGameActivity extends BaseFgActivity {

    public static final String TAG = MyGameActivity.class.getSimpleName();
    private SimpleTitleBar titleBar;
    private NoScrollListView listView_ing;
    private NoScrollListView listView_already;

    private DownLoadCenterAdapter downLoadCenterAdapter;
    private DownLoadCenterAdapter alreadyLvAdapter;

    protected QuickAction mItemClickQuickAction;

    private IFileLoad fileLoad;

    /**
     * 当前点击的列表 1.下载列表 2.完成列表
     */
    private int itemType;
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.local_activity_my_game);

        titleBar = (SimpleTitleBar) findViewById(R.id.title_bar);
        listView_ing = (NoScrollListView) findViewById(R.id.listView_ing);
        listView_already = (NoScrollListView) findViewById(R.id.listView_already);

        downLoadCenterAdapter = new DownLoadCenterAdapter(this,getSupportFragmentManager(), mItemClickQuickAction);
        listView_ing.setAdapter(downLoadCenterAdapter);
        alreadyLvAdapter = new DownLoadCenterAdapter(this,getSupportFragmentManager(), mItemClickQuickAction);
        listView_already.setAdapter(alreadyLvAdapter);

        fileLoad = FileLoadManager.getInstance(this);

        TextView tv_clear = (TextView) findViewById(R.id.text1);
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileLoad.deleteFinishedLoadRecord();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //重新加载未下载完成的
                List<FileLoadInfo> loadedList = fileLoad.getLoadedFileInfo();
                alreadyLvAdapter.setDate(loadedList);
                listView_already.notifyDataSetChanged();
            }
        });

        initPop();
    }

    private void initPop() {

        // 设置Action
        mItemClickQuickAction = new QuickAction(this, QuickAction.VERTICAL);

        ActionItem pointItem = new ActionItem(1, "删除任务", null);
        mItemClickQuickAction.addActionItem(pointItem);

        mItemClickQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {

                Log.d(TAG,pos+"");
                if(pos == 0){
                    //删除文件下载任务
                    FileLoadInfo fileInfo = null;
                    if(itemType == 1){

                        fileInfo = (FileLoadInfo) downLoadCenterAdapter.getItem(itemPosition);

                    }else if(itemType == 2){
                        fileInfo = (FileLoadInfo) alreadyLvAdapter.getItem(itemPosition);
                    }

                    //删除下载任务
                    fileLoad.delete(fileInfo.getUrl());

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //重新加载未下载完成的
                    List<FileLoadInfo> loadingList = fileLoad.getAllFileInfo();
                    downLoadCenterAdapter.setDate(loadingList);
                    listView_ing.notifyDataSetChanged();

                    //重新加载已下载完成的
                    List<FileLoadInfo> loadedList = fileLoad.getLoadedFileInfo();
                    alreadyLvAdapter.setDate(loadedList);
                    listView_already.notifyDataSetChanged();
                }

                //取消弹出框
                mItemClickQuickAction.dismiss();
            }
        });

        listView_already.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemType = 2;
                itemPosition = v.getId();

                //显示弹出框
                mItemClickQuickAction.show(v);
            }
        });

        listView_ing.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemType = 1;
                itemPosition = v.getId();
                //显示弹出框消失
                mItemClickQuickAction.show(v);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        titleBar.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<FileLoadInfo> loadingList = fileLoad.getAllFileInfo();
        downLoadCenterAdapter.setDate(loadingList);
        listView_ing.notifyDataSetChanged();
        List<FileLoadInfo> alreadyList = fileLoad.getLoadedFileInfo();
        alreadyLvAdapter.setDate(alreadyList);
        listView_already.notifyDataSetChanged();

    }


}