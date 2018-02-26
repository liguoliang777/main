package com.ngame.app;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.lx.pad.R;
import com.ngame.DabaBase.KeyEnum;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 */

public class NormalKeyViewMgrUtils extends BaseKeyViewFragment implements View.OnTouchListener {
    ImageView m_ivKeyA;
    ImageView m_ivKeyB;
    ImageView m_ivKeyX;
    ImageView m_ivKeyY;
    ImageView m_ivKeyUp;
    ImageView m_ivKeyDown;
    ImageView m_ivKeyLeft;
    ImageView m_ivKeyRight;
    List<KeyEnum> m_list = new ArrayList<KeyEnum>();


    public NormalKeyViewMgrUtils() {
        super();
    }

    @Override
    protected View makeView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.item_keyboard_1, viewGroup, false);
    }

    @Override
    protected void initView(View view) {
        m_ivKeyA = view.findViewById(R.id.iv_key_a);
        m_ivKeyB = view.findViewById(R.id.iv_key_b);
        m_ivKeyX = view.findViewById(R.id.iv_key_x);
        m_ivKeyY = view.findViewById(R.id.iv_key_y);
        m_ivKeyUp = view.findViewById(R.id.iv_key_up);
        m_ivKeyDown = view.findViewById(R.id.iv_key_down);
        m_ivKeyLeft = view.findViewById(R.id.iv_key_left);
        m_ivKeyRight = view.findViewById(R.id.iv_key_right);

        m_ivKeyA.setOnTouchListener(this);
        m_ivKeyB.setOnTouchListener(this);
        m_ivKeyX.setOnTouchListener(this);
        m_ivKeyY.setOnTouchListener(this);
        m_ivKeyUp.setOnTouchListener(this);
        m_ivKeyDown.setOnTouchListener(this);
        m_ivKeyLeft.setOnTouchListener(this);
        m_ivKeyRight.setOnTouchListener(this);

        setAllKeyDisImg();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int i = v.getId();
        if (i == R.id.iv_key_a || i == R.id.iv_key_b || i == R.id.iv_key_x || i == R.id.iv_key_y
                || i == R.id.iv_key_up || i == R.id.iv_key_down || i == R.id.iv_key_left || i == R.id.iv_key_right) {
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
        switch(IndexMappingNormalKeyEnum.m_keyEnumAry[keyEnum.ordinal()]){
            case 1:{
                m_ivKeyA.setImageResource(R.mipmap.ic_key_a_dis);
                m_ivKeyA.setEnabled(false);
                break;
            }
            case 2:{
                m_ivKeyB.setImageResource(R.mipmap.ic_key_b_dis);
                m_ivKeyB.setEnabled(false);
                break;
            }
            case 3:{
                m_ivKeyX.setImageResource(R.mipmap.ic_key_x_dis);
                m_ivKeyX.setEnabled(false);
                break;
            }
            case 4:{
                m_ivKeyY.setImageResource(R.mipmap.ic_key_y_dis);
                m_ivKeyY.setEnabled(false);
                break;
            }
            case 5:{
                m_ivKeyUp.setImageResource(R.mipmap.ic_key_up_dis);
                m_ivKeyUp.setEnabled(false);
                break;
            }
            case 6:{
                m_ivKeyDown.setImageResource(R.mipmap.ic_key_down_dis);
                m_ivKeyDown.setEnabled(false);
                break;
            }
            case 7:{
                m_ivKeyLeft.setImageResource(R.mipmap.ic_key_left_dis);
                m_ivKeyLeft.setEnabled(false);
                break;
            }
            case 8:{
                m_ivKeyRight.setImageResource(R.mipmap.ic_key_right_dis);
                m_ivKeyRight.setEnabled(false);
                break;
            }
        }
    }

    public final void setAllKeyDisImg(){
        if(m_list.size() > 0){
            Iterator<KeyEnum> iter = m_list.iterator();
            while(iter.hasNext()){
                setKeyDisImg(iter.next());
            }
        }
    }

}
