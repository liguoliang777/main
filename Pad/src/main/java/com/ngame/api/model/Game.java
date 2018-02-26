package com.ngame.api.model;

/**
 * Created by Administrator on 2017/12/4.
 */

public class Game {
    private int down_count;
    private String game_name;
    private String game_package;
    private String icon_src;
    private int id;
    private String source;

    public Game(){
        super();
    }

    public int getDown_count(){return down_count;}
    public String getGame_name(){return game_name;}
    public String getGame_package(){return game_package;}
    public String getIcon_src(){return icon_src;}
    public int getId(){return id;}
    public String getSource(){return source;}
    public void setDown_count(int count){down_count = count;}
    public void setGame_name(String name){game_name = name;}
    public void setGame_package(String pkgName){game_package = pkgName;}
    public void setIcon_src(String src){icon_src = src;}
    public void setId(int id){this.id = id;}
    public void setSource(String source){this.source = source;}

    public String toString(){
        String result = "{\n" +
                "down_count: " + down_count + "\n" +
                "game_name: " + game_name + "\n" +
                "game_package: " + game_package + "\n" +
                "icon_src: " + icon_src + "\n" +
                "id: " + id + "\n" +
                "source: " + source + "\n" +
                "}\n";
        return result;
    }
}
