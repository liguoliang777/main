package cn.ngame.store.activity.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.admin.SystemMsgBean;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;
import cn.ngame.store.adapter.SystemInfoAdapter;
import cn.ngame.store.core.db.DatabaseManager;

/**
 * 系统消息
 * Created by gp on 2017/4/15 0015.
 */

public class SystemInfoActivity extends BaseFgActivity implements View.OnClickListener {

    private LinearLayout ll_back;
    private TextView tv_title, tv_rightTxt;
    private ListView listView;
    private LinearLayout ll_null, ll_bottom;
    private TextView tv_select, tv_delete;
    SystemInfoAdapter adapter;
    List<String> selectList; //选中的item
    boolean deleteFlag;
    private DatabaseManager dbManager;
    SystemMsgBean msgbean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_info_activity);
        init();
    }

    private void init() {
        dbManager = DatabaseManager.getInstance(this);

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_rightTxt = (TextView) findViewById(R.id.tv_rightTxt);
        tv_title.setText("系统消息");
        listView = (ListView) findViewById(R.id.listView);
        ll_null = (LinearLayout) findViewById(R.id.ll_null);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tv_select = (TextView) findViewById(R.id.tv_select);
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        ll_back.setOnClickListener(this);
        tv_rightTxt.setOnClickListener(this);
        tv_select.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (deleteFlag) {
                    if (selectList.contains(position + "")) { //点击添加item，存在就取消选中
                        selectList.remove(position + "");
                    } else {
                        selectList.add(position + "");
                    }
                    adapter.setSelectIds(selectList);
                    adapter.notifyDataSetChanged();
                    if (selectList.size() > 0) { //下方删除按钮
                        tv_delete.setTextColor(getResources().getColor(R.color.mainColor));
                        tv_delete.setEnabled(true);
                    } else {
                        tv_delete.setTextColor(getResources().getColor(R.color.color999999));
                        tv_delete.setEnabled(false);
                    }
                    if (selectList.size() == msgbean.getData().size()) {
                        tv_select.setText("取消全选");
                        tv_select.setTextColor(getResources().getColor(R.color.orange_2));
                    } else {
                        tv_select.setText("全选");
                        tv_select.setTextColor(getResources().getColor(R.color.mainColor));
                    }
                } else {
                    //跳转详情
                    Intent i = new Intent();
                    i.setClass(SystemInfoActivity.this, QuestionDetailActivity.class);
                    i.putExtra("url", msgbean.getData().get(position).getUrl());
                    i.putExtra("id", msgbean.getData().get(position).getId());
                    i.putExtra("type", "systemMsg");
                    startActivity(i);
                }
            }
        });
        msgbean = (SystemMsgBean) getIntent().getSerializableExtra("msgbean");
        if (msgbean != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < msgbean.getData().size(); i++) {
                        //添加系统消息
                        dbManager.addSystemMsg(msgbean.getData().get(i).getId());
                    }
                }
            }).start();
        } else {
            listView.setVisibility(View.GONE);
            ll_null.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectList = new ArrayList<>();
        if (msgbean != null) {
            if (adapter == null) {
                adapter = new SystemInfoAdapter(SystemInfoActivity.this, msgbean.getData(), dbManager.getUnReadSystem(), 0);
                listView.setAdapter(adapter);
            } else {
                adapter.setList(msgbean.getData(), dbManager.getUnReadSystem(), 0);
            }
        }
    }

//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(0, R.anim.slide_out_right); //搜索页面退出动画
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                this.finish();
                break;
            case R.id.tv_rightTxt:
//                if (deleteFlag) {
//                    adapter.setList(mStickyLV, 0);
//                    deleteFlag = false;
//                    tv_rightTxt.setText("删除");
//                    ll_bottom.setVisibility(View.GONE);
//                } else {
//                    adapter.setList(mStickyLV, 1);
//                    deleteFlag = true;
//                    tv_rightTxt.setText("取消");
//                    ll_bottom.setVisibility(View.VISIBLE);
////                    tv_select.setText("全选");
////                    tv_select.setTextColor(getResources().getColor(R.color.colorPrimary));
//                    if (selectList.size() > 0) { //下方删除按钮
//                        tv_delete.setTextColor(getResources().getColor(R.color.colorPrimary));
//                        tv_delete.setEnabled(true);
//                    } else {
//                        tv_delete.setTextColor(getResources().getColor(R.color.font_black_9));
//                        tv_delete.setEnabled(false);
//                    }
//                }
                break;
            case R.id.tv_select:
//                if (selectList.size() != msgbean.getData().size()) {
//                    //全选
//                    selectList = new ArrayList<>();
//                    for (int i = 0; i < msgbean.getData().size(); i++) {
//                        selectList.add(msgbean.getData().get(i));
//                    }
//                    tv_select.setText("取消全选");
//                    tv_select.setTextColor(getResources().getColor(R.color.orange_2));
//                } else {
//                    //取消全选
//                    selectList = new ArrayList<>();
//                    tv_select.setText("全选");
//                    tv_select.setTextColor(getResources().getColor(R.color.colorPrimary));
//                }
//                adapter.setSelectIds(selectList);
//                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_delete:

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
    }
}
