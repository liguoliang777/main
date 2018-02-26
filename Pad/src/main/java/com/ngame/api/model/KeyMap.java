package com.ngame.api.model;

/**
 * Created by Administrator on 2017/12/5.
 */

public class KeyMap {
    private String btn;
    private int flags;
    private int frequency;
    private int mode;
    private int r;
    private int step;
    private int x;
    private int y;

    public KeyMap(){super();}
    public String getBtn(){return btn;}
    public int getFlags(){return flags;}
    public int getFrequency(){return frequency;}
    public int getMode(){return mode;}
    public int getR(){return r;}
    public int getStep(){return step;}
    public int getX(){return x;}
    public int getY(){return y;}
    public void setBtn(String s){btn = s;}
    public void setFlags(int n){flags = n;}
    public void setFrequency(int n){frequency = n;}
    public void setMode(int n){mode = n;}
    public void setR(int n){r = n;}
    public void setStep(int n){step = n;}
    public void setX(int x){this.x = x;}
    public void setY(int y){this.y = y;}

    public String toString(){
        String result = "{\n" +
                "btn: " + btn + "\n" +
                "frequency: " + frequency + "\n" +
                "mode: " + mode + "\n" +
                "r: " + r + "\n" +
                "step: " + step + "\n" +
                "x: " + x + "\n" +
                "y: " + y + "\n" +
                "flags: " + flags + "\n" +
                "}\n";
        return result;
    }
}
