package com.ngame.app;

import com.ngame.DabaBase.KeyEnum;

/**
 * Created by Administrator on 2017/12/4.
 */

public class IndexMappingSpecialKeyEnum {
    public static int[] m_keyEnumAry = new int[KeyEnum.values().length];

    static
    {
        m_keyEnumAry[KeyEnum.L1.ordinal()] = 1;
        m_keyEnumAry[KeyEnum.R1.ordinal()] = 2;
        m_keyEnumAry[KeyEnum.L2.ordinal()] = 3;
        m_keyEnumAry[KeyEnum.R2.ordinal()] = 4;
        m_keyEnumAry[KeyEnum.START.ordinal()] = 5;
        m_keyEnumAry[KeyEnum.BACK.ordinal()] = 6;
        m_keyEnumAry[KeyEnum.R.ordinal()] = 7;
        m_keyEnumAry[KeyEnum.F.ordinal()] = 8;
    }
}
