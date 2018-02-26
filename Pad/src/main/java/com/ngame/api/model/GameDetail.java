package com.ngame.api.model;

import java.security.KeyManagementException;
import java.util.List;

/**
 * Created by Administrator on 2017/12/5.
 */

public class GameDetail {
    private int ctime;
    private int down_count;
    private String game_name;
    private String game_package;
    private String icon_src;
    private int id;
    private int ipm_mode;
    private List<KeyMap> key_map;
    private int order;
    private String source;
    private int status;

    public GameDetail(){super();}
    public int getCtime(){return ctime;}
    public int getDown_count(){return down_count;}
    public String getGame_name(){return game_name;}
    public String getGame_package(){return game_package;}
    public String getIcon_src(){return icon_src;}
    public int getId(){return id;}
    public int getIpm_mode(){return ipm_mode;}
    public List<KeyMap> getKey_map(){return key_map;}
    public int getOrder(){return order;}
    public String getSource(){return source;}
    public int getStatus(){return status;}
    public void setCtime(int n){ctime = n;}
    public void setDown_count(int n){down_count = n;}
    public void setGame_name(String s){game_name = s;}
    public void setGame_package(String s){game_package = s;}
    public void setIcon_src(String s){icon_src = s;}
    public void setId(int n){id = n;}
    public void setIpm_mode(int n){ipm_mode = n;}
    public void setKey_map(List<KeyMap> list){key_map = list;}
    public void setOrder(int n){order = n;}
    public void setSource(String s){source = s;}
    public void getStatus(int n){status = n;}

}
