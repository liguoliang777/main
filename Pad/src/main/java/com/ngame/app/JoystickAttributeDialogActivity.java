package com.ngame.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lx.pad.R;
import com.lx.pad.util.LLog;
import com.ngame.DabaBase.KeyEnum;
import com.ngame.Utils.KeyMgrUtils;

/**
 * Created by Administrator on 2017/12/7.
 */

public class JoystickAttributeDialogActivity extends BaseFragmentActivity {
    public TextView m_tvTitle;
    private TextView m_tvRockMode;
    private TextView m_tvSlideMode;
    private TextView m_tvStep;
    private TextView m_tvFrequency;
    private TextView m_tvDesc;
    private RadioButton m_radioBtnFinal;
    private RadioButton m_radioBtnDynamic;
    private RadioGroup m_radioGroupCheck;
    private View m_viewLine;
    private TextView m_tvStepTitle;
    private View m_tvPnlStepAtt;
    private TextView m_tvFrequencyTitle;
    private View m_viewPnlFrequencyAtt;
    private KeyEnum m_keyEnum;
    private int m_nStep;
    private int m_nFrequency;
    private int m_mode;
    private boolean m_bFinalMode;
    private boolean m_isChanged;

    public JoystickAttributeDialogActivity() {
        super();
        m_bFinalMode = true;
        m_isChanged = false;
    }

    public static Intent makeIntent(Context context, KeyEnum keyEnum) {
        Intent intent = new Intent();
        intent.setPackage(context.getPackageName());
        intent.setAction(JoystickAttributeDialogActivity.class.getName());
        LLog.d("JoystickAttributeDialogActivity->makeIntent intent:" +
                JoystickAttributeDialogActivity.class.getName());
        intent.putExtra("btn", keyEnum.name());
        return intent;
    }

    protected int getLayoutId() {
        return R.layout.dialog_joystick_attributes_edit;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LLog.d("JoystickAttributeDialogActivity->onCreate");
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        getWindow().setDimAmount(0f);
        setContentView(getLayoutId());
        m_keyEnum = KeyEnum.valueOf(getIntent().getStringExtra("btn"));
        m_tvTitle = findViewById(R.id.tv_title);
        m_tvRockMode = findViewById(R.id.tv_rock_mode);
        m_tvRockMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeSelect(v);
            }
        });
        m_tvSlideMode = findViewById(R.id.tv_slide_mode);
        m_tvSlideMode.setOnClickListener(new View.OnClickListener() {
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
        m_tvStep = findViewById(R.id.tv_step);
        m_tvFrequency = findViewById(R.id.tv_frequency);
        m_tvDesc = findViewById(R.id.tv_desc);
        m_radioBtnFinal = findViewById(R.id.rb_final);
        m_radioBtnDynamic = findViewById(R.id.rb_dynamic);
        m_radioGroupCheck = findViewById(R.id.rg_check);
        m_radioGroupCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                m_isChanged = true;
                if (checkedId == R.id.rb_final) {
                    m_bFinalMode = true;
                } else {
                    m_bFinalMode = false;
                }

                switch (m_mode) {
                    case KeyMgrUtils.MOTION_MODE_FINAL:
                    case KeyMgrUtils.MOTION_MODE_DYNAMIC: {
                        m_mode = m_bFinalMode ? KeyMgrUtils.MOTION_MODE_FINAL : KeyMgrUtils
                                .MOTION_MODE_DYNAMIC;
                        break;
                    }
                }
            }
        });
        m_viewLine = findViewById(R.id.line);
        m_tvStepTitle = findViewById(R.id.tv_step_title);
        m_tvPnlStepAtt = findViewById(R.id.pnl_step_att);
        findViewById(R.id.iv_step_increase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeSelect(v);
            }
        });
        findViewById(R.id.iv_step_decrease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeSelect(v);
            }
        });
        m_tvFrequencyTitle = findViewById(R.id.tv_frequency_title);
        m_viewPnlFrequencyAtt = findViewById(R.id.pnl_frequency_att);
        findViewById(R.id.iv_frequency_increase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeSelect(v);
            }
        });
        findViewById(R.id.iv_frequency_decrease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeSelect(v);
            }
        });
        m_tvTitle.setText(getString(R.string.key_map_attributes_config, new Object[]{m_keyEnum
                .keyName.toUpperCase()}));
        m_mode = KeyMgrUtils.sGetKeyInfoMode(m_keyEnum);
        m_nStep = KeyMgrUtils.sGetKeyInfoStep(m_keyEnum);
        m_nFrequency = KeyMgrUtils.sGetKeyInfoFrequency(m_keyEnum);
        updateView();
    }

    private void modeViewUpdate(boolean rockFixed) {
        if (rockFixed) {
            m_tvRockMode.setSelected(true);
            m_tvSlideMode.setSelected(false);
            m_radioBtnFinal.setText("固定");
            m_radioBtnDynamic.setText("动态");
            m_radioGroupCheck.setVisibility(View.GONE);
            m_tvStepTitle.setVisibility(View.GONE);
            m_tvPnlStepAtt.setVisibility(View.GONE);
            m_viewPnlFrequencyAtt.setVisibility(View.GONE);
            m_tvFrequencyTitle.setVisibility(View.GONE);
            m_viewLine.setVisibility(View.GONE);
        } else {
            m_tvRockMode.setSelected(false);
            m_tvSlideMode.setSelected(true);
            m_radioBtnFinal.setText("区域");
            m_radioBtnDynamic.setText("全屏");
            m_radioGroupCheck.setVisibility(View.VISIBLE);
            m_tvStepTitle.setVisibility(View.VISIBLE);
            m_tvPnlStepAtt.setVisibility(View.VISIBLE);
            m_viewPnlFrequencyAtt.setVisibility(View.VISIBLE);
            m_tvFrequencyTitle.setVisibility(View.VISIBLE);
            m_viewLine.setVisibility(View.VISIBLE);
        }
    }

    private void updateView() {
        m_tvStep.setText(String.valueOf(m_nStep));
        m_tvFrequency.setText(String.valueOf(m_nFrequency));
        switch (m_mode) {
            case KeyMgrUtils.MOTION_MODE_NONE:
            case KeyMgrUtils.MOTION_MODE_NORMAL: {    // 显示普通按键操作信息
                modeViewUpdate(true);
                break;
            }
            case KeyMgrUtils.MOTION_MODE_FINAL: {    // 显示指向性技能操作信息  //区域
                modeViewUpdate(false);
                m_radioBtnFinal.setChecked(true);
                m_bFinalMode = true;
                break;
            }
            case KeyMgrUtils.MOTION_MODE_DYNAMIC: {    // 显示指向性技能操作信息  //全屏
                modeViewUpdate(false);
                m_radioBtnDynamic.setChecked(true);
                m_bFinalMode = false;
                break;
            }
        }
    }

    private void confirm() {
        KeyMgrUtils.sReplaceHashMapKeyEnumMode(m_keyEnum, m_mode);
        KeyMgrUtils.sReplaceHashMapKeyEnumStep(m_keyEnum, m_nStep);
        KeyMgrUtils.sReplaceHashMapKeyEnumFrequency(m_keyEnum, m_nFrequency);
    }

    private void cancelDlg() {
        AlertDialog.Builder artDlg = new AlertDialog.Builder(this, R.style
                .Theme_AppCompat_Light_Dialog);
        artDlg.setMessage("您修改了部分参数，是否保存？");
        artDlg.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                confirm();
                JoystickAttributeDialogActivity.this.finish();
            }
        });
        artDlg.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                JoystickAttributeDialogActivity.this.finish();
            }
        });
        artDlg.create().show();
    }

    private void modeSelect(View view) {
        LLog.d("JoystickAttributeDialogActivity->ModeSelect");
        int id = view.getId();
        if (id == R.id.tv_rock_mode) {
            m_mode = KeyMgrUtils.MOTION_MODE_NONE;
            m_isChanged = true;
        } else if (id == R.id.tv_slide_mode) {
            m_mode = m_bFinalMode ? KeyMgrUtils.MOTION_MODE_FINAL : KeyMgrUtils.MOTION_MODE_DYNAMIC;
            m_isChanged = true;
        } else if (id == R.id.iv_step_increase) {
            m_nStep++;
            m_isChanged = true;
        } else if (id == R.id.iv_step_decrease) {
            m_nStep--;
            if (m_nStep <= 0) {
                m_nStep = 1;
            }
            m_isChanged = true;
        } else if (id == R.id.iv_frequency_increase) {
            m_nFrequency++;
            m_isChanged = true;
        } else if (id == R.id.iv_frequency_decrease) {
            m_nFrequency--;
            if (m_nFrequency < 15) {
                m_nFrequency = 15;
            }
            m_isChanged = true;
        } else if (id == R.id.tv_confirm) {
            if (m_isChanged) {
                confirm();
            }
            finish();
        } else if (id == R.id.tv_cancel) {
            if (m_isChanged) {
                cancelDlg();
            } else {
                finish();
            }
        }
        updateView();
    }
}
