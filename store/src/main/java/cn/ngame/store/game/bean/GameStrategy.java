package cn.ngame.store.game.bean;

import java.io.Serializable;

/**
 * @author zeng
 * @since 2016/12/15
 */
public class GameStrategy implements Serializable {


    /**
     * id : 725
     * strategyTitle : 游戏手柄支持
     * strategyContent : 支持。本游戏是经过云映射处理过的游戏版本；
     * gameId : 146
     * isDelete : 0
     */

    private int id;
    private String strategyTitle;
    private String strategyContent;
    private int gameId;
    private int isDelete;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrategyTitle() {
        return strategyTitle;
    }

    public void setStrategyTitle(String strategyTitle) {
        this.strategyTitle = strategyTitle;
    }

    public String getStrategyContent() {
        return strategyContent;
    }

    public void setStrategyContent(String strategyContent) {
        this.strategyContent = strategyContent;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
}
