package cn.ngame.store.local.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.StoreApplication;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.core.utils.LoginHelper;
import cn.ngame.store.local.model.WatchRecord;
import cn.ngame.store.local.presenter.IWatchRecordPresenter;
import cn.ngame.store.local.presenter.WatchRecordPresenter;
import cn.ngame.store.video.view.VideoDetailActivity;
import cn.ngame.store.view.NoScrollListView;

/**
 * 显示观看历史的界面
 * Created by zeng on 2016/10/11.
 */
public class WatchHistoryActivity extends BaseFgActivity implements IWatchRecordView,View.OnClickListener{

    public static final String TAG = WatchHistoryActivity.class.getName();
    private NoScrollListView lv_yesterday,lv_other;
    private NoScrollListViewAdapter adapter_yesterday,adapter_other;

    private IWatchRecordPresenter presenter;

    private TextView bt_edit,bt_select_all,bt_delete;
    private LinearLayout lay_bottom;
    private boolean isEdit = false;
    private int checkAllState = 0;  //0.不操作,1.全选,2.取消全选

    private List<WatchRecord> checkedRecordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.local_activity_watch_history);

        Button bt_back = (Button) findViewById(R.id.left_but);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //重新登录
        if(StoreApplication.passWord != null && StoreApplication.passWord.length() > 0){
            LoginHelper loginHelper = new LoginHelper(this);
            loginHelper.reLoadSP();
        }else {
            StoreApplication.token = null;
        }

        bt_edit = (TextView) findViewById(R.id.right_but);
        bt_edit.setOnClickListener(this);
        bt_select_all = (TextView) findViewById(R.id.tv_select_all);
        bt_select_all.setOnClickListener(this);
        bt_delete = (TextView) findViewById(R.id.tv_delete);
        bt_delete.setOnClickListener(this);

        lay_bottom = (LinearLayout) findViewById(R.id.layout_1);

        lv_yesterday = (NoScrollListView) findViewById(R.id.lv_yesterday);
        lv_other = (NoScrollListView) findViewById(R.id.lv_other);

        adapter_yesterday = new NoScrollListViewAdapter(this);
        adapter_other = new NoScrollListViewAdapter(this);
        lv_yesterday.setAdapter(adapter_yesterday);
        lv_other.setAdapter(adapter_other);

        lv_yesterday.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = v.getId();
                WatchRecord record = (WatchRecord) adapter_yesterday.getItem(position);
                if(record != null){
                    Intent intent = new Intent(WatchHistoryActivity.this, VideoDetailActivity.class);
                    intent.putExtra("id",record.getVideoId());
                    WatchHistoryActivity.this.startActivity(intent);
                }
            }
        });

        lv_other.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = v.getId();
                WatchRecord record = (WatchRecord) adapter_other.getItem(position);
                if(record != null){
                    Intent intent = new Intent(WatchHistoryActivity.this, VideoDetailActivity.class);
                    intent.putExtra("id",record.getVideoId());
                    WatchHistoryActivity.this.startActivity(intent);
                }
            }
        });

        presenter = new WatchRecordPresenter(this,this);

        String userCode = StoreApplication.user == null ? null : StoreApplication.user.userCode;
        presenter.showWatchRecord(StoreApplication.token,userCode);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String userCode = StoreApplication.user == null ? null : StoreApplication.user.userCode;
        presenter.showWatchRecord(StoreApplication.token,userCode);
    }

    @Override
    public void showWatchRecord(List<WatchRecord> yesterdayRecord, List<WatchRecord> otherRecord) {

        adapter_yesterday.setDate(yesterdayRecord);
        /*for (WatchRecord wr : yesterdayRecord){
            Log.e(TAG,"------------>>>> "+wr.getTitle());
            Toast.makeText(this,""+wr.getTitle(),Toast.LENGTH_SHORT).show();
        }*/
        lv_yesterday.notifyDataSetChanged();
        adapter_other.setDate(otherRecord);
        lv_other.notifyDataSetChanged();

    }

    /**
     * 处理 编辑时 ITEM被选中或取消
     * @param isChecked 是否被选中
     * @param record  被选中记录
     */
    public void checkedChanged(boolean isChecked,WatchRecord record){

        if(checkedRecordList == null){
            checkedRecordList = new ArrayList<>();
        }

        if(isChecked){
            checkedRecordList.add(record);
        }else {
            checkedRecordList.remove(record);
        }
        bt_delete.setText("删除（"+checkedRecordList.size()+"）");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.right_but:

                if(isEdit){
                    isEdit = false;
                    bt_edit.setText("编辑");
                    lay_bottom.setVisibility(View.GONE);
                }else {
                    isEdit = true;
                    bt_edit.setText("取消");
                    lay_bottom.setVisibility(View.VISIBLE);
                    checkedRecordList.clear();
                    bt_delete.setText("删除（0）");
                }
                adapter_yesterday.setEdit(isEdit);
                lv_yesterday.notifyDataSetChanged();
                adapter_other.setEdit(isEdit);
                lv_other.notifyDataSetChanged();

                break;
            case R.id.tv_delete:
                if(checkedRecordList != null && checkedRecordList.size() > 0){

                    Toast.makeText(this,"正在删除记录",Toast.LENGTH_SHORT).show();
                    String userCode = StoreApplication.user == null ? null : StoreApplication.user.userCode;
                    presenter.deleteRecord(StoreApplication.token,userCode,checkedRecordList);
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    presenter.showWatchRecord(StoreApplication.token,userCode);
                    checkedRecordList.clear();
                    bt_delete.setText("删除（0 ）");

                }else {
                    Toast.makeText(this,"请选择要删除的记录",Toast.LENGTH_SHORT).show();
                }
                /*for(WatchRecord record : checkedRecordList){
                    Toast.makeText(this,""+record.getVideoId(),Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.tv_select_all:

                if(checkAllState == 0 || checkAllState == 2){
                    checkAllState = 1;
                    bt_select_all.setText("取消全选");
                    checkedRecordList.clear();

                }else if(checkAllState == 1){
                    checkAllState = 2;
                    bt_select_all.setText("全选");
                    checkedRecordList.clear();
                }

                bt_delete.setText("删除（"+checkedRecordList.size()+"）");
                adapter_yesterday.setCheckAllState(checkAllState);
                lv_yesterday.notifyDataSetChanged();
                adapter_other.setCheckAllState(checkAllState);
                lv_other.notifyDataSetChanged();

                break;
        }
    }
}
