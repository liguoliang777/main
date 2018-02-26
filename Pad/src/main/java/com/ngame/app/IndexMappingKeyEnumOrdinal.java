package com.ngame.app;

import com.ngame.DabaBase.KeyEnum;


/**
 * Created by Administrator on 2017/12/7.
 */

public final class IndexMappingKeyEnumOrdinal {
    static final int[] keyIndexAry = new int[KeyEnum.values().length];
    static{
        keyIndexAry[KeyEnum.Q.ordinal()] = 1;
        keyIndexAry[KeyEnum.A.ordinal()] = 2;
        keyIndexAry[KeyEnum.B.ordinal()] = 3;
        keyIndexAry[KeyEnum.X.ordinal()] = 4;
        keyIndexAry[KeyEnum.Y.ordinal()] = 5;
        keyIndexAry[KeyEnum.L1.ordinal()] = 6;
        keyIndexAry[KeyEnum.R1.ordinal()] = 7;
        keyIndexAry[KeyEnum.L2.ordinal()] = 8;
        keyIndexAry[KeyEnum.R2.ordinal()] = 9;
        keyIndexAry[KeyEnum.L.ordinal()] = 10;
        keyIndexAry[KeyEnum.R.ordinal()] = 11;
        keyIndexAry[KeyEnum.F.ordinal()] = 12;
        keyIndexAry[KeyEnum.UP.ordinal()] = 13;
        keyIndexAry[KeyEnum.DOWN.ordinal()] = 14;
        keyIndexAry[KeyEnum.LEFT.ordinal()] = 15;
        keyIndexAry[KeyEnum.RIGHT.ordinal()] = 16;
        keyIndexAry[KeyEnum.THUMBL.ordinal()] = 17;
        keyIndexAry[KeyEnum.THUMBR.ordinal()] = 18;
        keyIndexAry[KeyEnum.START.ordinal()] = 19;
        keyIndexAry[KeyEnum.BACK.ordinal()] = 20;
    }
}
