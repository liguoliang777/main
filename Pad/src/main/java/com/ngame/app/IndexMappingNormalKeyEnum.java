package com.ngame.app;

import com.ngame.DabaBase.KeyEnum;

/**
 * Created by Administrator on 2017/12/4.
 */

public class IndexMappingNormalKeyEnum {
    static int[] m_keyEnumAry = new int[KeyEnum.values().length];

    static
    {
        m_keyEnumAry[KeyEnum.A.ordinal()] = 1;
        m_keyEnumAry[KeyEnum.B.ordinal()] = 2;
        m_keyEnumAry[KeyEnum.X.ordinal()] = 3;
        m_keyEnumAry[KeyEnum.Y.ordinal()] = 4;
        m_keyEnumAry[KeyEnum.UP.ordinal()] = 5;
        m_keyEnumAry[KeyEnum.DOWN.ordinal()] = 6;
        m_keyEnumAry[KeyEnum.LEFT.ordinal()] = 7;
        m_keyEnumAry[KeyEnum.RIGHT.ordinal()] = 8;
    }
}
