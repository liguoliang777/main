package com.jzt.hol.android.jkda.sdk.bean.main;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class DiscoverListBean {


    /**
     * code : 0
     * msg : gameCategroyList游戏分类数据，dailyNewGamesList每日新发现，weeklyNewGamesList表示近期最热，agentGamePicList表示游戏厂商，roleGameList
     * 角色游戏列表7，actionGameList动作游戏列表8，adventureGameList冒险游戏列表9，cardGameList卡牌游戏列表10，strategyGameList
     * 策略游戏列表11，simulationGameList模拟游戏列表12， quadraticElementGameList二次元游戏列表13，fpsGameList FPS游戏列表14，racingGameList 竞速游戏列表15，
     * flyingGameList 飞行游戏列表16，mobaGameList MOBA游戏列表17，puzzleGameList 益智游戏列表18，sportsGameList 体育游戏列表19，aestheticismGameList
     * 唯美游戏列表20，survivalGameList 生存游戏列表21， puzzGameList 解谜游戏列表22， musicGameList 音乐游戏列表23，arcadeGameList 街机游戏列表24
     * data : {"gameCategroyList":[{"id":7,"cName":"角色","cParentId":"2"},{"id":8,"cName":"动作","cParentId":"2"},{"id":9,
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
         * gameCategroyList : [{"id":7,"cName":"角色","cParentId":"2"},{"id":8,"cName":"动作","cParentId":"2"},{"id":9,
         * "cName":"冒险","cParentId":"2"},{"id":10,"cName":"卡牌","cParentId":"2"},{"id":11,"cName":"策略","cParentId":"2"},
         * {"id":12,"cName":"模拟","cParentId":"2"},{"id":13,"cName":"二次元","cParentId":"2"},{"id":14,"cName":"FPS",
         * "cParentId":"2"},{"id":15,"cName":"竞速","cParentId":"2"},{"id":16,"cName":"飞行","cParentId":"2"},{"id":17,
         * "cName":"MOBA","cParentId":"2"},{"id":18,"cName":"益智","cParentId":"2"},{"id":19,"cName":"体育","cParentId":"2"},
         * {"id":20,"cName":"唯美","cParentId":"2"},{"id":21,"cName":"生存","cParentId":"2"},{"id":22,"cName":"解谜",
         * "cParentId":"2"},{"id":23,"cName":"音乐","cParentId":"2"},{"id":24,"cName":"街机","cParentId":"2"}]
         * dailyNewGamesList : {"list":[],"categoryName":"每日新发现","categoryId":-1}
         * weeklyNewGamesList : {"list":[],"categoryName":"近期最热","categoryId":-2}
         * agentGamePicList : {"list":[],"categoryName":"大厂","categoryId":-3}
         * resultList : [{"list":[{"id":591,"cName":null,"gameName":"影之刃2","gameLogo":"http://oss.ngame.cn/upload/1483083079423
         * .png","scoreLevel":null,"gameLink":null,"gameSize":null,"versionCode":null,"packages":null,"filename":null,
         * "md5":null,"versionName":null,"updateTime":null},{"id":672,"cName":null,"gameName":"龙之谷","gameLogo":"",
         * "scoreLevel":null,"gameLink":null,"gameSize":null,"versionCode":null,"packages":null,"filename":null,"md5":null,
         * "versionName":null,"updateTime":null}],"categoryName":"角色","categoryId":7},{"list":[{"id":591,"cName":null,
         * "gameName":"影之刃2","gameLogo":"http://oss.ngame.cn/upload/1483083079423.png","scoreLevel":null,"gameLink":null,
         * "gameSize":null,"versionCode":null,"packages":null,"filename":null,"md5":null,"versionName":null,"updateTime":null},
         * {"id":672,"cName":null,"gameName":"龙之谷","gameLogo":"","scoreLevel":null,"gameLink":null,"gameSize":null,
         * "versionCode":null,"packages":null,"filename":null,"md5":null,"versionName":null,"updateTime":null}],
         * "categoryName":"动作","categoryId":8},{"list":[{"id":591,"cName":null,"gameName":"影之刃2","gameLogo":"http://oss.ngame
         * .cn/upload/1483083079423.png","scoreLevel":null,"gameLink":null,"gameSize":null,"versionCode":null,"packages":null,
         * "filename":null,"md5":null,"versionName":null,"updateTime":null},{"id":671,"cName":null,"gameName":"贪吃蛇大作战【eva】",
         * "gameLogo":"http://oss.ngame.cn/upload/1476084541957.png","scoreLevel":null,"gameLink":null,"gameSize":null,
         * "versionCode":null,"packages":null,"filename":null,"md5":null,"versionName":null,"updateTime":null},{"id":672,
         * "cName":null,"gameName":"龙之谷","gameLogo":"","scoreLevel":null,"gameLink":null,"gameSize":null,"versionCode":null,
         * "packages":null,"filename":null,"md5":null,"versionName":null,"updateTime":null}],"categoryName":"冒险",
         * "categoryId":9},{"list":[],"categoryName":"卡牌","categoryId":10},{"list":[],"categoryName":"策略","categoryId":11},
         * {"list":[],"categoryName":"模拟","categoryId":12},{"list":[{"id":672,"cName":null,"gameName":"龙之谷","gameLogo":"",
         * "scoreLevel":null,"gameLink":null,"gameSize":null,"versionCode":null,"packages":null,"filename":null,"md5":null,
         * "versionName":null,"updateTime":null}],"categoryName":"二次元","categoryId":13},{"list":[],"categoryName":"FPS",
         * "categoryId":14},{"list":[],"categoryName":"竞速","categoryId":15},{"list":[],"categoryName":"飞行","categoryId":16},
         * {"list":[{"id":146,"cName":null,"gameName":"王者荣耀","gameLogo":"http://oss.ngame.cn/upload/1466667978284.png",
         * "scoreLevel":null,"gameLink":null,"gameSize":null,"versionCode":null,"packages":null,"filename":null,"md5":null,
         * "versionName":null,"updateTime":null},{"id":681,"cName":null,"gameName":"王者荣耀（eva）","gameLogo":"http://oss.ngame
         * .cn/upload/1466667978284.png","scoreLevel":null,"gameLink":null,"gameSize":null,"versionCode":null,"packages":null,
         * "filename":null,"md5":null,"versionName":null,"updateTime":null}],"categoryName":"MOBA","categoryId":17},
         * {"list":[{"id":591,"cName":null,"gameName":"影之刃2","gameLogo":"http://oss.ngame.cn/upload/1483083079423.png",
         * "scoreLevel":null,"gameLink":null,"gameSize":null,"versionCode":null,"packages":null,"filename":null,"md5":null,
         * "versionName":null,"updateTime":null},{"id":671,"cName":null,"gameName":"贪吃蛇大作战【eva】","gameLogo":"http://oss.ngame
         * .cn/upload/1476084541957.png","scoreLevel":null,"gameLink":null,"gameSize":null,"versionCode":null,"packages":null,
         * "filename":null,"md5":null,"versionName":null,"updateTime":null}],"categoryName":"益智","categoryId":18},{"list":[],
         * "categoryName":"体育","categoryId":19},{"list":[{"id":682,"cName":null,"gameName":"小小英雄","gameLogo":"http://oss.ngame
         * .cn/upload/1497769296370.jpg","scoreLevel":null,"gameLink":null,"gameSize":null,"versionCode":null,"packages":null,
         * "filename":null,"md5":null,"versionName":null,"updateTime":null}],"categoryName":"唯美","categoryId":20},
         * {"list":[{"id":671,"cName":null,"gameName":"贪吃蛇大作战【eva】","gameLogo":"http://oss.ngame.cn/upload/1476084541957.png",
         * "scoreLevel":null,"gameLink":null,"gameSize":null,"versionCode":null,"packages":null,"filename":null,"md5":null,
         * "versionName":null,"updateTime":null}],"categoryName":"生存","categoryId":21},{"list":[{"id":591,"cName":null,
         * "gameName":"影之刃2","gameLogo":"http://oss.ngame.cn/upload/1483083079423.png","scoreLevel":null,"gameLink":null,
         * "gameSize":null,"versionCode":null,"packages":null,"filename":null,"md5":null,"versionName":null,
         * "updateTime":null}],"categoryName":"解谜","categoryId":22},{"list":[],"categoryName":"音乐","categoryId":23},{"list":[],
         * "categoryName":"街机","categoryId":24}]
         */

        private DailyNewGamesListBean dailyNewGamesList;
        private WeeklyNewGamesListBean weeklyNewGamesList;
        private AgentGamePicListBean agentGamePicList;
        private List<GameCategroyListBean> gameCategroyList;
        private List<ResultListBean> resultList;

        public DailyNewGamesListBean getDailyNewGamesList() {
            return dailyNewGamesList;
        }

        public void setDailyNewGamesList(DailyNewGamesListBean dailyNewGamesList) {
            this.dailyNewGamesList = dailyNewGamesList;
        }

        public WeeklyNewGamesListBean getWeeklyNewGamesList() {
            return weeklyNewGamesList;
        }

        public void setWeeklyNewGamesList(WeeklyNewGamesListBean weeklyNewGamesList) {
            this.weeklyNewGamesList = weeklyNewGamesList;
        }

        public AgentGamePicListBean getAgentGamePicList() {
            return agentGamePicList;
        }

        public void setAgentGamePicList(AgentGamePicListBean agentGamePicList) {
            this.agentGamePicList = agentGamePicList;
        }

        public List<GameCategroyListBean> getGameCategroyList() {
            return gameCategroyList;
        }

        public void setGameCategroyList(List<GameCategroyListBean> gameCategroyList) {
            this.gameCategroyList = gameCategroyList;
        }

        public List<ResultListBean> getResultList() {
            return resultList;
        }

        public void setResultList(List<ResultListBean> resultList) {
            this.resultList = resultList;
        }

        public static class DailyNewGamesListBean {
            /**
             * list : []
             * categoryName : 每日新发现
             * categoryId : -1
             */

            private String categoryName;
            private int categoryId;
            private List<DiscoverTopBean> list;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public List<DiscoverTopBean> getList() {
                return list;
            }

            public void setList(List<DiscoverTopBean> list) {
                this.list = list;
            }
        }

        public static class WeeklyNewGamesListBean {
            /**
             * list : []
             * categoryName : 近期最热
             * categoryId : -2
             */

            private String categoryName;
            private int categoryId;
            private List<DiscoverTopBean> list;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public List<DiscoverTopBean> getList() {
                return list;
            }

            public void setList(List<DiscoverTopBean> list) {
                this.list = list;
            }
        }

        public static class AgentGamePicListBean {
            /**
             * list : []
             * categoryName : 大厂
             * categoryId : -3
             */

            private String categoryName;
            private int categoryId;
            private List<DiscoverTopBean> list;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public List<DiscoverTopBean> getList() {
                return list;
            }

            public void setList(List<DiscoverTopBean> list) {
                this.list = list;
            }
        }

        public static class GameCategroyListBean {
            /**
             * id : 7
             * cName : 角色
             * cParentId : 2
             */

            private int id;
            private String cName;
            private String cParentId;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCName() {
                return cName;
            }

            public void setCName(String cName) {
                this.cName = cName;
            }

            public String getCParentId() {
                return cParentId;
            }

            public void setCParentId(String cParentId) {
                this.cParentId = cParentId;
            }
        }

        public static class ResultListBean {
            /**
             * list : [{"id":591,"cName":null,"gameName":"影之刃2","gameLogo":"http://oss.ngame.cn/upload/1483083079423.png",
             * "scoreLevel":null,"gameLink":null,"gameSize":null,"versionCode":null,"packages":null,"filename":null,"md5":null,
             * "versionName":null,"updateTime":null},{"id":672,"cName":null,"gameName":"龙之谷","gameLogo":"","scoreLevel":null,
             * "gameLink":null,"gameSize":null,"versionCode":null,"packages":null,"filename":null,"md5":null,
             * "versionName":null,"updateTime":null}]
             * categoryName : 角色
             * categoryId : 7
             */

            private String categoryName;
            private int categoryId;
            private List<ListBean> list;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public int getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(int categoryId) {
                this.categoryId = categoryId;
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public static class ListBean {
                /**
                 * id : 591
                 * cName : null
                 * gameName : 影之刃2
                 * gameLogo : http://oss.ngame.cn/upload/1483083079423.png
                 * scoreLevel : null
                 * gameLink : null
                 * gameSize : null
                 * versionCode : null
                 * packages : null
                 * filename : null
                 * md5 : null
                 * versionName : null
                 * updateTime : null
                 */

                private int id;
                private Object cName;
                private String gameName;
                private String gameLogo;
                private Object scoreLevel;
                private Object gameLink;
                private Object gameSize;
                private Object versionCode;
                private Object packages;
                private Object filename;
                private Object md5;
                private Object versionName;
                private Object updateTime;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public Object getCName() {
                    return cName;
                }

                public void setCName(Object cName) {
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

                public Object getScoreLevel() {
                    return scoreLevel;
                }

                public void setScoreLevel(Object scoreLevel) {
                    this.scoreLevel = scoreLevel;
                }

                public Object getGameLink() {
                    return gameLink;
                }

                public void setGameLink(Object gameLink) {
                    this.gameLink = gameLink;
                }

                public Object getGameSize() {
                    return gameSize;
                }

                public void setGameSize(Object gameSize) {
                    this.gameSize = gameSize;
                }

                public Object getVersionCode() {
                    return versionCode;
                }

                public void setVersionCode(Object versionCode) {
                    this.versionCode = versionCode;
                }

                public Object getPackages() {
                    return packages;
                }

                public void setPackages(Object packages) {
                    this.packages = packages;
                }

                public Object getFilename() {
                    return filename;
                }

                public void setFilename(Object filename) {
                    this.filename = filename;
                }

                public Object getMd5() {
                    return md5;
                }

                public void setMd5(Object md5) {
                    this.md5 = md5;
                }

                public Object getVersionName() {
                    return versionName;
                }

                public void setVersionName(Object versionName) {
                    this.versionName = versionName;
                }

                public Object getUpdateTime() {
                    return updateTime;
                }

                public void setUpdateTime(Object updateTime) {
                    this.updateTime = updateTime;
                }
            }
        }
    }
}
