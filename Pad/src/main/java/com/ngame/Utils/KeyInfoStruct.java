package com.ngame.Utils;

/**
 * Created by Administrator on 2017/12/4.
 */

public class KeyInfoStruct {
    public int m_action;
    public int m_keyOrdinal;
    public int m_keyPtX;
    public int m_keyPtY;

    public KeyInfoStruct(int action, int keyOrdinal, int keyPtX, int keyPtY){
        super();
        m_action = action;
        m_keyOrdinal = keyOrdinal;
        m_keyPtX = keyPtX;
        m_keyPtY = keyPtY;
    }
}
