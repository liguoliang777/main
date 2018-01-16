package com.jzt.hol.android.jkda.sdk.bean.classification;

import java.util.List;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class ClassifiHomeBean {

    /**
     * code : 0
     * msg : onlineList表示热门网游，gunFireList表示枪战射击，parkourList表示冒险跑酷，combatList表示动作格斗roleList表示角色扮演，puzzleList表示休闲益智，raceList表示赛车竞赛，sportList表示体育球类
     * data : {"combatList":[{"id":274,"typeName":"动作1","orderNo":0,"isDelete":0,"parentId":133,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":275,"typeName":"动作2","orderNo":0,"isDelete":0,"parentId":133,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":276,"typeName":"动作3","orderNo":0,"isDelete":0,"parentId":133,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":277,"typeName":"动作4","orderNo":0,"isDelete":0,"parentId":133,"appTypeId":0,"logoUrl":"","iosCompany":1}],"puzzleList":[{"id":286,"typeName":"休闲1","orderNo":0,"isDelete":0,"parentId":136,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":287,"typeName":"休闲2","orderNo":0,"isDelete":0,"parentId":136,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":288,"typeName":"休闲3","orderNo":0,"isDelete":0,"parentId":136,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":289,"typeName":"休闲4","orderNo":0,"isDelete":0,"parentId":136,"appTypeId":0,"logoUrl":"","iosCompany":1}],"onlineList":[{"id":270,"typeName":"热门1","orderNo":0,"isDelete":0,"parentId":131,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":271,"typeName":"热门2","orderNo":0,"isDelete":0,"parentId":131,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":272,"typeName":"热门3","orderNo":0,"isDelete":0,"parentId":131,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":273,"typeName":"热门4","orderNo":0,"isDelete":0,"parentId":131,"appTypeId":0,"logoUrl":"","iosCompany":1}],"sportList":[{"id":262,"typeName":"体育1","orderNo":0,"isDelete":0,"parentId":84,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":263,"typeName":"体育2","orderNo":0,"isDelete":0,"parentId":84,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":264,"typeName":"体育3","orderNo":0,"isDelete":0,"parentId":84,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":265,"typeName":"体育4","orderNo":0,"isDelete":0,"parentId":84,"appTypeId":0,"logoUrl":"","iosCompany":1}],"raceList":[{"id":282,"typeName":"赛车1","orderNo":0,"isDelete":0,"parentId":135,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":283,"typeName":"赛车2","orderNo":0,"isDelete":0,"parentId":135,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":284,"typeName":"赛车3","orderNo":0,"isDelete":0,"parentId":135,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":285,"typeName":"赛车4","orderNo":0,"isDelete":0,"parentId":135,"appTypeId":0,"logoUrl":"","iosCompany":1}],"gunFireList":[{"id":278,"typeName":"枪战1","orderNo":0,"isDelete":0,"parentId":134,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":279,"typeName":"枪战2","orderNo":0,"isDelete":0,"parentId":134,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":280,"typeName":"枪战3","orderNo":0,"isDelete":0,"parentId":134,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":281,"typeName":"枪战4","orderNo":0,"isDelete":0,"parentId":134,"appTypeId":0,"logoUrl":"","iosCompany":1}],"roleList":[{"id":266,"typeName":"角色1","orderNo":0,"isDelete":0,"parentId":132,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":267,"typeName":"角色2","orderNo":0,"isDelete":0,"parentId":132,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":268,"typeName":"角色3","orderNo":0,"isDelete":0,"parentId":132,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":269,"typeName":"角色4","orderNo":0,"isDelete":0,"parentId":132,"appTypeId":0,"logoUrl":"","iosCompany":1}],"parkourList":[{"id":290,"typeName":"冒险1","orderNo":0,"isDelete":0,"parentId":137,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":291,"typeName":"冒险2","orderNo":0,"isDelete":0,"parentId":137,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":292,"typeName":"冒险3","orderNo":0,"isDelete":0,"parentId":137,"appTypeId":0,"logoUrl":"","iosCompany":1},{"id":293,"typeName":"冒险4","orderNo":0,"isDelete":0,"parentId":137,"appTypeId":0,"logoUrl":"","iosCompany":1}]}
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
        private List<CombatListBean> combatList;
        private List<PuzzleListBean> puzzleList;
        private List<OnlineListBean> onlineList;
        private List<SportListBean> sportList;
        private List<RaceListBean> raceList;
        private List<GunFireListBean> gunFireList;
        private List<RoleListBean> roleList;
        private List<ParkourListBean> parkourList;

        public List<CombatListBean> getCombatList() {
            return combatList;
        }

        public void setCombatList(List<CombatListBean> combatList) {
            this.combatList = combatList;
        }

        public List<PuzzleListBean> getPuzzleList() {
            return puzzleList;
        }

        public void setPuzzleList(List<PuzzleListBean> puzzleList) {
            this.puzzleList = puzzleList;
        }

        public List<OnlineListBean> getOnlineList() {
            return onlineList;
        }

        public void setOnlineList(List<OnlineListBean> onlineList) {
            this.onlineList = onlineList;
        }

        public List<SportListBean> getSportList() {
            return sportList;
        }

        public void setSportList(List<SportListBean> sportList) {
            this.sportList = sportList;
        }

        public List<RaceListBean> getRaceList() {
            return raceList;
        }

        public void setRaceList(List<RaceListBean> raceList) {
            this.raceList = raceList;
        }

        public List<GunFireListBean> getGunFireList() {
            return gunFireList;
        }

        public void setGunFireList(List<GunFireListBean> gunFireList) {
            this.gunFireList = gunFireList;
        }

        public List<RoleListBean> getRoleList() {
            return roleList;
        }

        public void setRoleList(List<RoleListBean> roleList) {
            this.roleList = roleList;
        }

        public List<ParkourListBean> getParkourList() {
            return parkourList;
        }

        public void setParkourList(List<ParkourListBean> parkourList) {
            this.parkourList = parkourList;
        }

        public static class CombatListBean {
            /**
             * id : 274
             * typeName : 动作1
             * orderNo : 0
             * isDelete : 0
             * parentId : 133
             * appTypeId : 0
             * logoUrl :
             * iosCompany : 1
             */

            private int id;
            private String typeName;
            private int orderNo;
            private int isDelete;
            private int parentId;
            private int appTypeId;
            private String logoUrl;
            private int iosCompany;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public String getLogoUrl() {
                return logoUrl;
            }

            public void setLogoUrl(String logoUrl) {
                this.logoUrl = logoUrl;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }
        }

        public static class PuzzleListBean {
            /**
             * id : 286
             * typeName : 休闲1
             * orderNo : 0
             * isDelete : 0
             * parentId : 136
             * appTypeId : 0
             * logoUrl :
             * iosCompany : 1
             */

            private int id;
            private String typeName;
            private int orderNo;
            private int isDelete;
            private int parentId;
            private int appTypeId;
            private String logoUrl;
            private int iosCompany;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public String getLogoUrl() {
                return logoUrl;
            }

            public void setLogoUrl(String logoUrl) {
                this.logoUrl = logoUrl;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }
        }

        public static class OnlineListBean {
            /**
             * id : 270
             * typeName : 热门1
             * orderNo : 0
             * isDelete : 0
             * parentId : 131
             * appTypeId : 0
             * logoUrl :
             * iosCompany : 1
             */

            private int id;
            private String typeName;
            private int orderNo;
            private int isDelete;
            private int parentId;
            private int appTypeId;
            private String logoUrl;
            private int iosCompany;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public String getLogoUrl() {
                return logoUrl;
            }

            public void setLogoUrl(String logoUrl) {
                this.logoUrl = logoUrl;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }
        }

        public static class SportListBean {
            /**
             * id : 262
             * typeName : 体育1
             * orderNo : 0
             * isDelete : 0
             * parentId : 84
             * appTypeId : 0
             * logoUrl :
             * iosCompany : 1
             */

            private int id;
            private String typeName;
            private int orderNo;
            private int isDelete;
            private int parentId;
            private int appTypeId;
            private String logoUrl;
            private int iosCompany;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public String getLogoUrl() {
                return logoUrl;
            }

            public void setLogoUrl(String logoUrl) {
                this.logoUrl = logoUrl;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }
        }

        public static class RaceListBean {
            /**
             * id : 282
             * typeName : 赛车1
             * orderNo : 0
             * isDelete : 0
             * parentId : 135
             * appTypeId : 0
             * logoUrl :
             * iosCompany : 1
             */

            private int id;
            private String typeName;
            private int orderNo;
            private int isDelete;
            private int parentId;
            private int appTypeId;
            private String logoUrl;
            private int iosCompany;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public String getLogoUrl() {
                return logoUrl;
            }

            public void setLogoUrl(String logoUrl) {
                this.logoUrl = logoUrl;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }
        }

        public static class GunFireListBean {
            /**
             * id : 278
             * typeName : 枪战1
             * orderNo : 0
             * isDelete : 0
             * parentId : 134
             * appTypeId : 0
             * logoUrl :
             * iosCompany : 1
             */

            private int id;
            private String typeName;
            private int orderNo;
            private int isDelete;
            private int parentId;
            private int appTypeId;
            private String logoUrl;
            private int iosCompany;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public String getLogoUrl() {
                return logoUrl;
            }

            public void setLogoUrl(String logoUrl) {
                this.logoUrl = logoUrl;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }
        }

        public static class RoleListBean {
            /**
             * id : 266
             * typeName : 角色1
             * orderNo : 0
             * isDelete : 0
             * parentId : 132
             * appTypeId : 0
             * logoUrl :
             * iosCompany : 1
             */

            private int id;
            private String typeName;
            private int orderNo;
            private int isDelete;
            private int parentId;
            private int appTypeId;
            private String logoUrl;
            private int iosCompany;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public String getLogoUrl() {
                return logoUrl;
            }

            public void setLogoUrl(String logoUrl) {
                this.logoUrl = logoUrl;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }
        }

        public static class ParkourListBean {
            /**
             * id : 290
             * typeName : 冒险1
             * orderNo : 0
             * isDelete : 0
             * parentId : 137
             * appTypeId : 0
             * logoUrl :
             * iosCompany : 1
             */

            private int id;
            private String typeName;
            private int orderNo;
            private int isDelete;
            private int parentId;
            private int appTypeId;
            private String logoUrl;
            private int iosCompany;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public int getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(int orderNo) {
                this.orderNo = orderNo;
            }

            public int getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(int isDelete) {
                this.isDelete = isDelete;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public String getLogoUrl() {
                return logoUrl;
            }

            public void setLogoUrl(String logoUrl) {
                this.logoUrl = logoUrl;
            }

            public int getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(int iosCompany) {
                this.iosCompany = iosCompany;
            }
        }
    }
}
