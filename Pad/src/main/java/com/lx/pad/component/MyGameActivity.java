package com.lx.pad.component;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lx.pad.ItemType.AppInfo;
import com.lx.pad.R;
import com.lx.pad.adapter.AppInfoAdapter;
import com.lx.pad.util.GameInfoDbMgr;
import com.lx.pad.util.PackageList;
import com.ngame.Utils.KeyMgrUtils;
import com.ngds.pad.server.DeviceManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

/**
 * 我的游戏的设置界面
 */
public class MyGameActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnFocusChangeListener{
    private List<AppInfo> appInfoList = null;
    GameInfoDbMgr gameInfoDbMgr = null;
    private GridView gridView = null;
    private AppInfoAdapter appInfoAdapter = null;
    private Button btnCancel = null;
    private Button btnRemove = null;
    private PopupWindow popupWindow = null;
    private ImageView imgViewBtnBack = null;
    private TextView textViewHeadTitleName = null;

    private boolean bUpdateAppInfo = false;
    private int posion = 0;
    private View itemView = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏标题
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //设置全屏=
        setContentView(R.layout.game_activity_view);

        gameInfoDbMgr = new GameInfoDbMgr(this);

        initViewControl();
    }
    public void initViewControl(){
        gridView = (GridView)findViewById(R.id.game_view);
        imgViewBtnBack = (ImageView)findViewById(R.id.btn_back);
        textViewHeadTitleName = (TextView)findViewById(R.id.head_title_name);
        textViewHeadTitleName.setText("我的游戏");
        imgViewBtnBack.setVisibility(View.VISIBLE);
        imgViewBtnBack.setOnClickListener(this);
    }

    private void initGridViewGameView(){
        appInfoAdapter = new AppInfoAdapter(appInfoList, this);
        gridView.setAdapter(appInfoAdapter);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
        gridView.setSelector(new ColorDrawable(0));
    }

    private void initPopupViewControls(int posion){
        this.posion = posion;
        View popupView = LayoutInflater.from(this).inflate(R.layout.popup_view, null);
        btnRemove = (Button)popupView.findViewById(R.id.btn_two);
        btnRemove.setText("移除");
        btnRemove.setFocusable(true);
        btnCancel = (Button)popupView.findViewById(R.id.btn_one);
        btnCancel.setText("取消");
        popupWindow = new PopupWindow(popupView, -1, -2, true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                MyGameActivity.this.setViewVisibility(itemView, View.INVISIBLE);
                itemView = null;
            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_bg));
        popupWindow.showAtLocation(popupView, 81, 0, 0);
        btnCancel.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
        btnCancel.setOnFocusChangeListener(this);
        btnRemove.setOnFocusChangeListener(this);
    }

    private AppInfo newAddAppInfo(){
        AppInfo appInfo = new AppInfo();
        appInfo.setAppIcon(getResources().getDrawable(R.mipmap.add));
        appInfo.setAppName(" ");
        appInfo.setPackageName(" ");
        return appInfo;
    }

    private boolean updateArrayListAppInfo(){
        boolean bSuccess = false;
        List<AppInfo> arrayListAppInfo = new ArrayList<AppInfo>();
        List<AppInfo> allpackageList = PackageList.getAllPackageInfoList(this);
        List<String> aryListGameInfo = gameInfoDbMgr.queryAppInfoFromDB();
        if(aryListGameInfo.size() != 0 && allpackageList.size() != 0){
            for(int i = 0; i < aryListGameInfo.size(); i++){
                int count = 0;
                while(count < allpackageList.size()){
                    if(allpackageList.get(count).getPackageName().equals(aryListGameInfo.get(i))){
                        arrayListAppInfo.add(allpackageList.get(count));
                        allpackageList.remove(count);
                    }else{
                        count++;
                        continue;
                    }
                    break;
                }
            }
        }


        if(arrayListAppInfo != null){
            arrayListAppInfo.add(newAddAppInfo());
            appInfoList = arrayListAppInfo;
        }else{
            appInfoList = new ArrayList<AppInfo>();
            appInfoList.add(newAddAppInfo());
        }

        if(allpackageList.size() <= 0){
            bSuccess = true;
        }

        return bSuccess;
    }

    private void setViewVisibility(View view, int visibility){
        if(view != null && view.findViewById(R.id.icon_choice) != null){
            view.findViewById(R.id.icon_choice).setVisibility(visibility);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_back) {
            finish();
        } else if (i == R.id.btn_one) {
            if (popupWindow.isShowing()) {
                return;
            } else {
                popupWindow.dismiss();
            }
        } else if (i == R.id.btn_two) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            if (posion != -1) {
                gameInfoDbMgr.delAppInfoFromDB(appInfoList.get(posion).getPackageName());
                appInfoList.remove(posion);
                appInfoAdapter.initArrayList(appInfoList);
                gridView.setAdapter(appInfoAdapter);
            }
        }
    }

    //启动游戏，并将包名传递过去
    private void startAppByPkgName(String spFileName){
        Log.d("tag", "startAppByPkgName ---------- pkg:" + spFileName);
        DeviceManager.getInstance(this).registerCallback(spFileName);
        Intent intent = getPackageManager().getLaunchIntentForPackage(spFileName);
        if(intent != null){
            startActivity(intent);
//            //从配置文件中更新按键信息
//            KeyMgrUtils.sUpdateKeyEnumHashMap(this);
            //以包名从服务器更新对应的按键信息
            KeyMgrUtils.sUpdateKeyEnumFromHttpServer(this, spFileName, null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyGameActivity.this, "按手柄i键或start+LB启动按键映射配置", Toast.LENGTH_LONG).show();
                }
            }, 3000);
        }else{
            Toast.makeText(this, "启动失败...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!appInfoList.get(position).getPackageName().equals(" ")){
            //运行选择的游戏
            startAppByPkgName(appInfoList.get(position).getPackageName());
        }else if(!bUpdateAppInfo){
            //打开添加游戏界面
            //startActivity(new Intent(this, ChoiceGameActivity.class));
        }else{
            Toast.makeText(this, "没有可选游戏", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(!appInfoList.get(position).getPackageName().equals(" ")){
            initPopupViewControls(position);
            itemView = view;
            setViewVisibility(itemView, View.VISIBLE);
        }else{
            Toast.makeText(this, "请正确操作...", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //根据焦点改变颜色
        int clr = 0;
        int i = v.getId();
        if (i == R.id.btn_one) {
            if (hasFocus) {
                //btnCancel
                clr = R.color.tools_txt_bg;
            } else {
                //btnCancel
                clr = R.color.grey;
            }
            btnCancel.setBackgroundResource(clr);
        } else if (i == R.id.btn_two) {
            if (hasFocus) {
                //btnRemove
                clr = R.color.tools_txt_bg;
            } else {
                //btnRemove
                clr = R.color.grey;
            }
            btnRemove.setBackgroundResource(clr);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bUpdateAppInfo = updateArrayListAppInfo();
        initGridViewGameView();
        if(popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }

}
