package com.jzt.hol.android.jkda.sdk.bean.manager;

import java.io.Serializable;
import java.util.List;

/**
 * 游戏列表
 * Created by gp on 2017/3/18 0018.
 */

public class LikeListBean implements Serializable{


    /**
     * code : 0
     * msg : id表示游戏id，gameName表示游戏名称，gameLogo表示游戏logo，gameLink表示游戏下载链接，scoreLevel表示游戏评分，cName表示游戏分类汇总
     * data : {"gameList":[{"id":146,"cName":"MOBA","gameName":"王者荣耀","gameLogo":"http://oss.ngame.cn/upload/1466667978284
     * .png","scoreLevel":5,"gameLink":"http://oss.ngame.cn/games/hbwy/%E7%8E%8B%E8%80%85%E8%8D%A3%E8%80%80_20170209.apk",
     * "gameSize":785263045,"versionCode":17011509,"packages":"com.tencent.tmgp.sgame","filename":"1469244345731.apk",
     * "md5":"18C7EFBC3105B7B844F728BC969A4C30","versionName":"1.17.1.15","updateTime":1500446138000},{"id":591,"cName":"角色,解谜,
     * 益智,冒险,动作,MZ","gameName":"影之刃2","gameLogo":"http://oss.ngame.cn/upload/1483083079423.png","scoreLevel":5,
     * "gameLink":"http://oss.ngame.cn/games/hbwy/%E5%BD%B1%E4%B9%8B%E5%88%832.apk","gameSize":399191285,"versionCode":72,
     * "packages":"m.Sgame.yzr2","filename":"yxr21230.apk","md5":"66994DA662F3D73F1229551E9059A853","versionName":"1.0.18",
     * "updateTime":1484905733000},{"id":672,"cName":"动作,腾讯,角色,云适配手柄,二次元,冒险,大陆","gameName":"龙之谷","gameLogo":"","scoreLevel":5,
     * "gameLink":"","gameSize":0,"versionCode":0,"packages":"","filename":"","md5":"","versionName":"",
     * "updateTime":1491726447000}],"totals":14}
     * map : null
     * innerResult : null
     */

    private int code;
    private String msg;
    private DataBean data;
    private Object map;
    private Object innerResult;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        /**
         * gameList : [{"id":146,"cName":"MOBA","gameName":"王者荣耀","gameLogo":"http://oss.ngame.cn/upload/1466667978284.png",
         * "scoreLevel":5,"gameLink":"http://oss.ngame.cn/games/hbwy/%E7%8E%8B%E8%80%85%E8%8D%A3%E8%80%80_20170209.apk",
         * "gameSize":785263045,"versionCode":17011509,"packages":"com.tencent.tmgp.sgame","filename":"1469244345731.apk",
         * "md5":"18C7EFBC3105B7B844F728BC969A4C30","versionName":"1.17.1.15","updateTime":1500446138000},{"id":591,
         * "cName":"角色,解谜,益智,冒险,动作,MZ","gameName":"影之刃2","gameLogo":"http://oss.ngame.cn/upload/1483083079423.png",
         * "scoreLevel":5,"gameLink":"http://oss.ngame.cn/games/hbwy/%E5%BD%B1%E4%B9%8B%E5%88%832.apk","gameSize":399191285,
         * "versionCode":72,"packages":"m.Sgame.yzr2","filename":"yxr21230.apk","md5":"66994DA662F3D73F1229551E9059A853",
         * "versionName":"1.0.18","updateTime":1484905733000},{"id":672,"cName":"动作,腾讯,角色,云适配手柄,二次元,冒险,大陆","gameName":"龙之谷",
         * "gameLogo":"","scoreLevel":5,"gameLink":"","gameSize":0,"versionCode":0,"packages":"","filename":"","md5":"",
         * "versionName":"","updateTime":1491726447000}]
         * totals : 14
         */

        private int totals;
        private List<GameListBean> gameList;

        public int getTotals() {
            return totals;
        }

        public void setTotals(int totals) {
            this.totals = totals;
        }

        public List<GameListBean> getGameList() {
            return gameList;
        }

        public void setGameList(List<GameListBean> gameList) {
            this.gameList = gameList;
        }

        public static class GameListBean {
            /**
             * id : 146
             * cName : MOBA
             * gameName : 王者荣耀
             * gameLogo : http://oss.ngame.cn/upload/1466667978284.png
             * scoreLevel : 5
             * gameLink : http://oss.ngame.cn/games/hbwy/%E7%8E%8B%E8%80%85%E8%8D%A3%E8%80%80_20170209.apk
             * gameSize : 785263045
             * versionCode : 17011509
             * packages : com.tencent.tmgp.sgame
             * filename : 1469244345731.apk
             * md5 : 18C7EFBC3105B7B844F728BC969A4C30
             * versionName : 1.17.1.15
             * updateTime : 1500446138000
             */

            private long id;
            private String cName;
            private String gameName;
            private String gameLogo;
            private int scoreLevel;
            private String gameLink;
            private int gameSize;
            private int versionCode;
            private String packages;
            private String filename;
            private String md5;
            private String versionName;
            private long updateTime;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getCName() {
                return cName;
            }

            public void setCName(String cName) {
                this.cName = cName;
            }

            public String getGameName() {
                return gameName;
            }

            public void setGameName(String gameName) {
                this.gameName = gameName;
            }

            public String getGameLogo() {
                return gameLogo;
            }

            public void setGameLogo(String gameLogo) {
                this.gameLogo = gameLogo;
            }

            public int getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(int scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public String getGameLink() {
                return gameLink;
            }

            public void setGameLink(String gameLink) {
                this.gameLink = gameLink;
            }

            public int getGameSize() {
                return gameSize;
            }

            public void setGameSize(int gameSize) {
                this.gameSize = gameSize;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public String getPackages() {
                return packages;
            }

            public void setPackages(String packages) {
                this.packages = packages;
            }

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getMd5() {
                return md5;
            }

            public void setMd5(String md5) {
                this.md5 = md5;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
