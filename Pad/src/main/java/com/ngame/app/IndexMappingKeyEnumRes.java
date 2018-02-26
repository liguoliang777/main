package com.ngame.app;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.lx.pad.R;
import com.ngame.DabaBase.KeyEnum;

/**
 * Created by Administrator on 2017/12/7.
 */

public final class IndexMappingKeyEnumRes {
    public static Drawable sGetDrawable(KeyEnum keyEnum, Context context){
        Drawable drawable = null;
        if(keyEnum != null){
            int resId = 0;
            switch(IndexMappingKeyEnumOrdinal.keyIndexAry[keyEnum.ordinal()]){
                case 1:break;
                case 2:resId = R.mipmap.ic_key_a;break;
                case 3:resId = R.mipmap.ic_key_b;break;
                case 4:resId = R.mipmap.ic_key_x;break;
                case 5:resId = R.mipmap.ic_key_y;break;
                case 6:resId = R.mipmap.ic_key_l1;break;
                case 7:resId = R.mipmap.ic_key_r1;break;
                case 8:resId = R.mipmap.ic_key_l2;break;
                case 9:resId = R.mipmap.ic_key_r2;break;
                case 10:resId = R.mipmap.ic_key_l;break;
                case 11:resId = R.mipmap.ic_key_r;break;
                case 12:resId = R.mipmap.ic_key_direction;break;
                case 13:resId = R.mipmap.ic_key_up;break;
                case 14:resId = R.mipmap.ic_key_down;break;
                case 15:resId = R.mipmap.ic_key_left;break;
                case 16:resId = R.mipmap.ic_key_right;break;
                case 17:break;
                case 18:break;
                case 19:resId = R.mipmap.ic_key_start;break;
                case 20:resId = R.mipmap.ic_key_back;break;
            }
            if(resId > 0){
                drawable = context.getResources().getDrawable(resId);
            }
        }
        return drawable;
    }
}
