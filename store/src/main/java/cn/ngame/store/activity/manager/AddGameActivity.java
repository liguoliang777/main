package cn.ngame.store.activity.manager;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lx.pad.ItemType.AppInfo;
import com.lx.pad.adapter.ChoiceAppInfoAdapter;
import com.lx.pad.util.GameInfoDbMgr;
import com.lx.pad.util.PackageList;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.BaseFgActivity;

/**
 * Created by Administrator on 2017/11/27.
 */

public class AddGameActivity extends BaseFgActivity implements View.OnClickListener, View
        .OnFocusChangeListener, OnItemClickListener {
    private List<AppInfo> arrayListAppInfo= new ArrayList<AppInfo>();
    private List<String> dbArrayListAppInfo= new ArrayList<String>();
    private ArrayList<AppInfo> arrayListChoiceAppInfo= new ArrayList<AppInfo>();
    private GridView gameView;
    private ChoiceAppInfoAdapter choiceAppInfoAdapter;
    private View popupViewHandle;
    private Button popupViewBtnAdd;
    private Button popupViewBtnCancel;
    private GameInfoDbMgr gameInfoDbMgr;
    private ImageView btnBack;
    private TextView headTitleName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStatusBar();
        setContentView(R.layout.activity_game_add);
        gameInfoDbMgr = new GameInfoDbMgr(this);
        initView();

        updateArrayListAppInfo();
        initChoiceAdapter();
    }


    private void initView() {
        gameView = (GridView) findViewById(R.id.game_view);
        findViewById(R.id.ll_back).setOnClickListener(this);
        TextView titleTv = (TextView) findViewById(R.id.tv_title);
        titleTv.setText("选择游戏");
        popupViewHandle = findViewById(R.id.popup_view_handle);
        popupViewBtnAdd = (Button) findViewById(R.id.btn_two);
        popupViewBtnAdd.setText("添加");
        popupViewBtnAdd.setFocusable(true);
        popupViewBtnCancel = (Button) findViewById(R.id.btn_one);
        popupViewBtnCancel.setText("取消");
        popupViewBtnCancel.setOnClickListener(this);
        popupViewBtnAdd.setOnClickListener(this);
        popupViewBtnAdd.setOnFocusChangeListener(this);
        popupViewBtnCancel.setOnFocusChangeListener(this);
    }

    private void initChoiceAdapter() {
        choiceAppInfoAdapter = new ChoiceAppInfoAdapter(arrayListAppInfo, this);
        gameView.setAdapter(choiceAppInfoAdapter);
        gameView.setOnItemClickListener(this);
        gameView.setSelector(new ColorDrawable(0));
    }

    private void updateArrayListAppInfo() {
        List<AppInfo> arrayList = PackageList.getAllPackageInfoList(this);
        dbArrayListAppInfo = gameInfoDbMgr.queryAppInfoFromDB();
        if (dbArrayListAppInfo.size() != 0 && arrayList.size() != 0) {
            for (int nIndex = 0; nIndex < dbArrayListAppInfo.size(); nIndex++) {
                int count = 0;
                while (count < arrayList.size()) {
                    if (arrayList.get(count).getPackageName().equals(dbArrayListAppInfo.get
                            (nIndex))) {
                        arrayList.remove(count);
                    } else {
                        count++;
                        continue;
                    }
                    break;
                }
            }
        }
        arrayListAppInfo = arrayList;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_back) {
            arrayListChoiceAppInfo.clear();
            finish();
        } else if (i == R.id.btn_one) {
            choiceAppInfoAdapter.initAddFlagsTable();
            arrayListChoiceAppInfo.clear();
            popupViewHandle.setVisibility(View.GONE);
        } else if (i == R.id.btn_two) {  //添加
            if (arrayListChoiceAppInfo.size() <= 0) {
                return;
            }
            for (int nIndex = 0; nIndex < arrayListChoiceAppInfo.size(); nIndex++) {
                gameInfoDbMgr.insertAppInfoFromDB(arrayListChoiceAppInfo.get(nIndex));
                int index = 0;
                while (index < arrayListAppInfo.size()) {
                    if (arrayListAppInfo.get(index).getPackageName().equals
                            (arrayListChoiceAppInfo.get(nIndex).getPackageName())) {
                        arrayListAppInfo.remove(index);
                    } else {
                        index++;
                        continue;
                    }
                    break;
                }
            }
            if (arrayListAppInfo.size() > 0) {
                choiceAppInfoAdapter.initArrayListAppInfo(arrayListAppInfo);
                gameView.setAdapter(choiceAppInfoAdapter);
                popupViewHandle.setVisibility(View.GONE);
                return;
            }
            arrayListChoiceAppInfo.clear();
            finish();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int clr;
        int i = v.getId();
        if (i == R.id.btn_one) {
            if (hasFocus) {
                clr = R.color.tools_txt_bg;
            } else {
                clr = R.color.grey;
            }
            popupViewBtnCancel.setBackgroundResource(clr);
        } else if (i == R.id.btn_two) {
            if (hasFocus) {
                clr = R.color.tools_txt_bg;
            } else {
                clr = R.color.grey;
            }
            popupViewBtnAdd.setBackgroundResource(clr);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (choiceAppInfoAdapter.addGameByPosition(position)) {
            arrayListChoiceAppInfo.add(arrayListAppInfo.get(position));
        } else {
            for (int nIndex = 0; nIndex < arrayListChoiceAppInfo.size(); nIndex++) {
                if (arrayListChoiceAppInfo.get(nIndex).getPackageName().equals(arrayListAppInfo
                        .get(position).getPackageName())) {
                    arrayListChoiceAppInfo.remove(nIndex);
                }
            }
        }

        if (arrayListChoiceAppInfo.size() > 0) {
            popupViewHandle.setVisibility(View.VISIBLE);
        } else {
            popupViewHandle.setVisibility(View.GONE);
        }
    }

}
