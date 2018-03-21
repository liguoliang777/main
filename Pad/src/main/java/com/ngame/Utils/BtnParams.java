package com.ngame.Utils;

/**
 * Created by Administrator on 2017/12/4.
 */

public class BtnParams {
    public int m_x;
    public int m_y;
    public int m_r;
    public int m_mode;
    public int m_step;
    public int m_frequency;
    public int m_flags;

    public BtnParams() {
        super();
    }

    public final String toString() {
        return "BtnParams(x: " + this.m_x + ", y: " + this.m_y + ", r: " + this.m_r + ", mode: "
                + this.m_mode + ", step: " + this.m_step + ", frequency: " + this.m_frequency + ")";
    }
}
