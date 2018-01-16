package com.jzt.hol.android.jkda.sdk.bean.recommend;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class RecommendListBean implements Serializable{


    /**
     * code : 0
     * msg : null
     * data : [{"id":9,"gameId":147,"gameName":"穿越火线:枪战王者","gameEnglishName":null,"gameLogo":"http://oss.ngame
     * .cn/upload/1484287375245.png","recommender":"编辑推荐","gameRecommendImg":"http://oss.ngame.cn/gameRecommend/1499846075605
     * .png","recommend":"这个游戏很好玩有意思这个游戏很好玩有意思这个游戏很好玩有意思这个游戏很好玩有意思这个游戏很好玩有意思","appTypeId":0,"createTime":1499845997000,
     * "updateTime":1499845997000},{"id":7,"gameId":136,"gameName":"世界2:魔物狩猎","gameEnglishName":null,"gameLogo":"http://oss
     * .ngame.cn/upload/1466577288833.png","recommender":"编辑推荐","gameRecommendImg":"http://oss.ngame
     * .cn/gameRecommend/1499843230158.png","recommend":"这是一个好玩的游戏哟！！！","appTypeId":0,"createTime":1499843152000,
     * "updateTime":1499843152000}]
     * map : null
     * innerResult : null
     * page : 1
     * pageSize : 10
     * totals : 2
     */

    private int code;
    private Object msg;
    private Object map;
    private Object innerResult;
    private int page;
    private int pageSize;
    private int totals;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Object getMap() {
        return map;
    }

    public void setMap(Object map) {
        this.map = map;
    }

    public Object getInnerResult() {
        return innerResult;
    }

    public void setInnerResult(Object innerResult) {
        this.innerResult = innerResult;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotals() {
        return totals;
    }

    public void setTotals(int totals) {
        this.totals = totals;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 9
         * gameId : 147
         * gameName : 穿越火线:枪战王者
         * gameEnglishName : null
         * gameLogo : http://oss.ngame.cn/upload/1484287375245.png
         * recommender : 编辑推荐
         * gameRecommendImg : http://oss.ngame.cn/gameRecommend/1499846075605.png
         * recommend : 这个游戏很好玩有意思这个游戏很好玩有意思这个游戏很好玩有意思这个游戏很好玩有意思这个游戏很好玩有意思
         * appTypeId : 0
         * createTime : 1499845997000
         * updateTime : 1499845997000
         */

        private int id;
        private long gameId;
        private String gameName;
        private Object gameEnglishName;
        private String gameLogo;
        private String recommender;
        private String gameRecommendImg;
        private String recommend;
        private int appTypeId;
        private long createTime;
        private long updateTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getGameId() {
            return gameId;
        }

        public void setGameId(int gameId) {
            this.gameId = gameId;
        }

        public String getGameName() {
            return gameName;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public Object getGameEnglishName() {
            return gameEnglishName;
        }

        public void setGameEnglishName(Object gameEnglishName) {
            this.gameEnglishName = gameEnglishName;
        }

        public String getGameLogo() {
            return gameLogo;
        }

        public void setGameLogo(String gameLogo) {
            this.gameLogo = gameLogo;
        }

        public String getRecommender() {
            return recommender;
        }

        public void setRecommender(String recommender) {
            this.recommender = recommender;
        }

        public String getGameRecommendImg() {
            return gameRecommendImg;
        }

        public void setGameRecommendImg(String gameRecommendImg) {
            this.gameRecommendImg = gameRecommendImg;
        }

        public String getRecommend() {
            return recommend;
        }

        public void setRecommend(String recommend) {
            this.recommend = recommend;
        }

        public int getAppTypeId() {
            return appTypeId;
        }

        public void setAppTypeId(int appTypeId) {
            this.appTypeId = appTypeId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
