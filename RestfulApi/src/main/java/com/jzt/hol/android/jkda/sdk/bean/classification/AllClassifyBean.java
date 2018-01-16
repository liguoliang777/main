package com.jzt.hol.android.jkda.sdk.bean.classification;

import java.util.List;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class AllClassifyBean {

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
        private List<GameCountyListBean> gameCountyList;
        private List<GameStyleListBean> gameStyleList;
        private List<GameCategoryListBean> gameCategoryList;
        private List<GameManufacturerListBean> gameManufacturerList;

        public List<GameCountyListBean> getGameCountyList() {
            return gameCountyList;
        }

        public void setGameCountyList(List<GameCountyListBean> gameCountyList) {
            this.gameCountyList = gameCountyList;
        }

        public List<GameStyleListBean> getGameStyleList() {
            return gameStyleList;
        }

        public void setGameStyleList(List<GameStyleListBean> gameStyleList) {
            this.gameStyleList = gameStyleList;
        }

        public List<GameCategoryListBean> getGameCategoryList() {
            return gameCategoryList;
        }

        public void setGameCategoryList(List<GameCategoryListBean> gameCategoryList) {
            this.gameCategoryList = gameCategoryList;
        }

        public List<GameManufacturerListBean> getGameManufacturerList() {
            return gameManufacturerList;
        }

        public void setGameManufacturerList(List<GameManufacturerListBean> gameManufacturerList) {
            this.gameManufacturerList = gameManufacturerList;
        }

        public static class GameCountyListBean {
            /**
             * id : 47
             * cName : 大陆
             * cParentId : 47
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

        public static class GameStyleListBean {
            /**
             * id : 1
             * cName : 原生手柄
             * cParentId : 1
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

        public static class GameCategoryListBean {
            /**
             * id : 7
             * cName : 角色
             * cParentId : 7
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

        public static class GameManufacturerListBean {
            /**
             * id : 25
             * cName : EA
             * cParentId : 25
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
    }
}
