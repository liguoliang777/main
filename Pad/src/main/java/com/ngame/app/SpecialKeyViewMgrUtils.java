package com.ngame.app;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lx.pad.R;
import com.lx.pad.util.LLog;
import com.ngame.DabaBase.KeyEnum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class SpecialKeyViewMgrUtils extends BaseKeyViewFragment implements View.OnTouchListener {
    ImageView m_ivKeyR1;
    ImageView m_ivKeyR2;
    ImageView m_ivKeyL1;
    ImageView m_ivKeyL2;
    ImageView m_ivKeyBack;
    ImageView m_ivKeyStart;
    ImageView m_ivKeyR;
    ImageView m_ivKeyDirection;
    List<KeyEnum> m_list = new ArrayList<KeyEnum>();

    @Override
    protected View makeView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.item_keyboard_2, viewGroup, false);
    }

    @Override
    protected void initView(View view) {
        m_ivKeyR1 = view.findViewById(R.id.iv_key_r1);
        m_ivKeyR2 = view.findViewById(R.id.iv_key_r2);
        m_ivKeyL1 = view.findViewById(R.id.iv_key_l1);
        m_ivKeyL2 = view.findViewById(R.id.iv_key_l2);
        m_ivKeyBack = view.findViewById(R.id.iv_key_back);
        m_ivKeyStart = view.findViewById(R.id.iv_key_start);
        m_ivKeyR = view.findViewById(R.id.iv_key_r);
        m_ivKeyDirection = view.findViewById(R.id.iv_key_direction);

        m_ivKeyR1.setOnTouchListener(this);
        m_ivKeyR2.setOnTouchListener(this);
        m_ivKeyL1.setOnTouchListener(this);
        m_ivKeyL2.setOnTouchListener(this);
        m_ivKeyBack.setOnTouchListener(this);
        m_ivKeyStart.setOnTouchListener(this);
        m_ivKeyR.setOnTouchListener(this);
        m_ivKeyDirection.setOnTouchListener(this);

        setAllKeyDisImg();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int i = v.getId();
        if (i == R.id.iv_key_r1 || i == R.id.iv_key_r2 || i == R.id.iv_key_l1 || i == R.id
                .iv_key_l2 || i == R.id.iv_key_back || i == R.id.iv_key_start || i == R.id
                .iv_key_r || i == R.id.iv_key_direction) {
        } else {
            return false;
        }
        return viewMove(v, event);
    }

    @Override
    public void setImgPressState(ImageView view) {
        setPressImgView(view);
    }

    public final void setKeyDisImg(KeyEnum keyEnum){
        switch(IndexMappingSpecialKeyEnum.m_keyEnumAry[keyEnum.ordinal()]){
            case 1:{
                m_ivKeyL1.setImageResource(R.mipmap.ic_key_l1_dis);
                m_ivKeyL1.setEnabled(false);
                break;
            }
            case 2:{
                m_ivKeyR1.setImageResource(R.mipmap.ic_key_r1_dis);
                m_ivKeyR1.setEnabled(false);
                break;
            }
            case 3:{
                m_ivKeyL2.setImageResource(R.mipmap.ic_key_l2_dis);
                m_ivKeyL2.setEnabled(false);
                break;
            }
            case 4:{
                m_ivKeyR2.setImageResource(R.mipmap.ic_key_r2_dis);
                m_ivKeyR2.setEnabled(false);
                break;
            }
            case 5:{
                m_ivKeyStart.setImageResource(R.mipmap.ic_key_start_dis);
                m_ivKeyStart.setEnabled(false);
                break;
            }
            case 6:{
                m_ivKeyBack.setImageResource(R.mipmap.ic_key_back_dis);
                m_ivKeyBack.setEnabled(false);
                break;
            }
            case 7:{
                m_ivKeyR.setImageResource(R.mipmap.ic_key_r_dis);
                m_ivKeyR.setEnabled(false);
                break;
            }
            case 8:{
                m_ivKeyDirection.setImageResource(R.mipmap.ic_key_direction_dis);
                m_ivKeyDirection.setEnabled(false);
                break;
            }
        }
    }

    public final void setAllKeyDisImg(){
        if(m_list == null){
            LLog.d("SpecialKeyViewMgrUtils->setAllKeyDisImg m_list is null");
            return ;
        }

        if(m_list.size() > 0){
            Iterator<KeyEnum> iter = m_list.iterator();
            while(iter.hasNext()){
                setKeyDisImg(iter.next());
            }
        }
    }

}
