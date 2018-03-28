package com.ngame.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lx.pad.R;
import com.ngame.DabaBase.KeyEnum;
import com.ngame.Utils.KeyMgrUtils;

/**
 * Created by Administrator on 2017/12/8.
 */

public class KeyAttributeDialogActivity extends BaseFragmentActivity {
    public TextView m_tvTitle;
    private TextView m_tvKeyModeNormal;
    private TextView m_tvKeyModeSkill;
    private TextView m_tvDesc;
    private RadioButton m_radioBtnLeftJoystick;
    private RadioButton m_radioBtnRightJoystick;
    private RadioGroup m_radioGroupCheck;
    private CheckBox m_reverse;
    private View m_viewLine;
    private KeyEnum m_keyEnum;
    private int m_nKeyMode;
    private int m_keyFlags; //是否反向
    private boolean m_isLeftJoystick;   //关联左摇杆或右摇杆
    private boolean m_isChanged;

    public KeyAttributeDialogActivity() {
        super();
        m_isLeftJoystick = true;
        m_isChanged = false;
    }

    public static Intent makeIntent(Context context, KeyEnum keyEnum){
        Intent intent = new Intent();
        intent.setPackage(context.getPackageName());
        intent.setAction(KeyAttributeDialogActivity.class.getName());
        intent.putExtra("btn", keyEnum.name());
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_key_attributes_edit);
        m_keyEnum = KeyEnum.valueOf(getIntent().getStringExtra("btn"));
        m_tvTitle = findViewById(R.id.tv_title);
        m_tvKeyModeNormal = findViewById(R.id.tv_key_mode_nomal);
        m_tvKeyModeSkill = findViewById(R.id.tv_key_mode_skill);
        m_tvKeyModeNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeSelect(v);
            }
        });
        m_tvKeyModeSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeSelect(v);
            }
        });
        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeSelect(v);
            }
        });
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeSelect(v);
            }
        });
        m_tvDesc = findViewById(R.id.tv_desc);
        m_radioBtnLeftJoystick = findViewById(R.id.rb_left_joystick);
        m_radioBtnRightJoystick = findViewById(R.id.rb_right_joystick);
        m_radioGroupCheck = findViewById(R.id.rg_check);
        m_radioGroupCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                m_isChanged = true;
                if(checkedId == R.id.rb_left_joystick){
                    m_isLeftJoystick = true;
                }else{
                    m_isLeftJoystick = false;
                }
//                switch(m_nKeyMode){
//                    case 1:
//                    case 2:{
                        m_nKeyMode = m_isLeftJoystick ? KeyMgrUtils.KEYMODE_LEFT_STICK : KeyMgrUtils.KEYMODE_RIGHT_STICK;
//                        break;
//                    }
//                }
            }
        });
        m_reverse = findViewById(R.id.cb_reverse);
        m_reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skillSetting(v);
            }
        });
        m_viewLine = findViewById(R.id.line);
        m_tvTitle.setText(getString(R.string.key_map_attributes_config, new Object[]{m_keyEnum.keyName.toUpperCase()}));
        m_nKeyMode = KeyMgrUtils.sGetKeyInfoMode(m_keyEnum);
        if(m_nKeyMode == -1){
            m_nKeyMode = KeyMgrUtils.KEYMODE_NORMAL;
        }
        m_keyFlags = KeyMgrUtils.sGetKeyInfoFlags(m_keyEnum);
        if(m_keyFlags == -1){
            m_keyFlags = 0;
        }

        updateView();
    }

    private void modeSelect(View view){
        int id = view.getId();
        if (id == R.id.tv_key_mode_nomal) {
            m_nKeyMode = KeyMgrUtils.KEYMODE_NORMAL;
            m_isChanged = true;
        } else if (id == R.id.tv_key_mode_skill) {
            m_nKeyMode = m_isLeftJoystick ? KeyMgrUtils.KEYMODE_LEFT_STICK : KeyMgrUtils
                    .KEYMODE_RIGHT_STICK;
            m_isChanged = true;
        } else if (id == R.id.tv_confirm) {
            if (m_isChanged) {
                saveSetting();
            }
            finish();
        } else if (id == R.id.tv_cancel) {
            if (m_isChanged) {
                showSaveDlg();
            } else {
                finish();
            }
        }

        updateView();
    }

    // 按键中的左摇杆 右摇杆 反向修改的确认界面
    private void showSaveDlg(){
        AlertDialog.Builder artDlg = new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog);
        artDlg.setMessage("您修改了部分参数，是否保存？");
        artDlg.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                KeyMgrUtils.sReplaceHashMapKeyEnumMode(m_keyEnum, m_nKeyMode);
                KeyMgrUtils.sReplaceHashMapKeyEnumFlag(m_keyEnum, m_keyFlags);
                KeyAttributeDialogActivity.this.finish();
            }
        });
        artDlg.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                KeyAttributeDialogActivity.this.finish();
            }
        });
        artDlg.create().show();
    }

    private void saveSetting(){
        KeyMgrUtils.sReplaceHashMapKeyEnumMode(m_keyEnum, m_nKeyMode);
        KeyMgrUtils.sReplaceHashMapKeyEnumFlag(m_keyEnum, m_keyFlags);
    }

    private void showChangeDlg(){
        AlertDialog.Builder artDlg = new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog);
        artDlg.setMessage("您修改了部分参数，是否保存？");
        artDlg.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                saveSetting();
                KeyAttributeDialogActivity.this.finish();
            }
        });
        artDlg.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                KeyAttributeDialogActivity.this.finish();
            }
        });
        artDlg.create().show();
    }

    //指向性技能
    private void skillSetting(View view){
        int id = view.getId();
        if (id == R.id.tv_key_mode_nomal) {
            m_nKeyMode = KeyMgrUtils.KEYMODE_NORMAL;
            m_isChanged = true;
        } else if (id == R.id.tv_key_mode_skill) {
            if (m_isLeftJoystick) {
                m_nKeyMode = KeyMgrUtils.KEYMODE_LEFT_STICK;
            } else {
                m_nKeyMode = KeyMgrUtils.KEYMODE_RIGHT_STICK;
            }
            m_isChanged = true;
        } else if (id == R.id.tv_confirm) {
            if (m_isChanged) {
                saveSetting();
            }
            finish();
        } else if (id == R.id.tv_cancel) {
            if (m_isChanged) {
                showChangeDlg();
            } else {
                finish();
            }
        } else if (id == R.id.cb_reverse) {
            if (m_reverse.isChecked()) {
                m_keyFlags |= 1;
            } else {
                m_keyFlags &= -2;
            }
            m_isChanged = true;
        }
        updateView();
    }

    private void updateView(){
        switch(m_nKeyMode){
            case KeyMgrUtils.KEYMODE_NORMAL:{
                updateKeyModeView(true);
                break;
            }
            case KeyMgrUtils.KEYMODE_LEFT_STICK:{
                updateKeyModeView(false);
                m_radioBtnLeftJoystick.setChecked(true);
                reverseIsCheck();
                m_isLeftJoystick = true;
                break;
            }
            case KeyMgrUtils.KEYMODE_RIGHT_STICK:{
                updateKeyModeView(false);
                m_radioBtnRightJoystick.setChecked(true);
                reverseIsCheck();
                m_isLeftJoystick = false;
                break;
            }
        }
    }

    private void updateKeyModeView(boolean bNormal){
        if(bNormal){
            m_tvKeyModeNormal.setSelected(true);
            m_tvKeyModeSkill.setSelected(false);
            m_radioGroupCheck.setVisibility(View.GONE);
            m_viewLine.setVisibility(View.GONE);
            m_reverse.setVisibility(View.GONE);
        }else{
            m_tvKeyModeNormal.setSelected(false);
            m_tvKeyModeSkill.setSelected(true);
            m_radioGroupCheck.setVisibility(View.VISIBLE);
            m_viewLine.setVisibility(View.VISIBLE);
            m_reverse.setVisibility(View.VISIBLE);
        }
    }

    private void reverseIsCheck(){
        if((m_keyFlags & 1) == 1){
            m_reverse.setChecked(true);
        }else{
            m_reverse.setChecked(false);
        }
    }
}
