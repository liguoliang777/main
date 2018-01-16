package com.jzt.hol.android.jkda.sdk.bean.gamehub;

import java.util.List;

/**
 * Created by Administrator on 2017/3/4 0004.
 */

public class GameListBean {

    /**
     * code : 0
     * msg : networkGameList表示网络类游戏，standAloneGameList表示单机类游戏，otherGameList表示其他类游戏
     * data : {"standAloneGameList":[{"id":650,"gameName":"波斯王子：影与火1","gameLogo":null,"gameVersion":null,"gameLink":null,"gameSize":null,"gameDesc":null,"gameInfo":null,"gameSelected":null,"packages":null,"updateTime":null,"orderNo":null,"filename":null,"md5":null,"uploadId":null,"scoreLevel":null,"downloadCount":null,"versionCode":null,"versionName":null,"percentage":null,"isDelete":null,"createTime":null,"commentPeople":null,"appTypeId":0,"isHand":null,"isVR":null,"isHeadControl":null,"isTouchScreen":null,"simpleLabel":null,"urlSchema":null,"iosCompany":null,"gameAgentList":null,"gameDetailsImages":null,"gameKeyMapsImages":null,"questionResults":null,"smallOrderNo":null,"smallOrderNo1":null,"smallOrderNo2":null,"smallOrderNo3":null,"gameKeyDescList":null,"operation":null,"gameTypeList":null,"gameStrategy":null}],"networkGameList":[{"id":648,"gameName":"王者荣耀","gameLogo":null,"gameVersion":null,"gameLink":null,"gameSize":null,"gameDesc":null,"gameInfo":null,"gameSelected":null,"packages":null,"updateTime":null,"orderNo":null,"filename":null,"md5":null,"uploadId":null,"scoreLevel":null,"downloadCount":null,"versionCode":null,"versionName":null,"percentage":null,"isDelete":null,"createTime":null,"commentPeople":null,"appTypeId":0,"isHand":null,"isVR":null,"isHeadControl":null,"isTouchScreen":null,"simpleLabel":null,"urlSchema":null,"iosCompany":null,"gameAgentList":null,"gameDetailsImages":null,"gameKeyMapsImages":null,"questionResults":null,"smallOrderNo":null,"smallOrderNo1":null,"smallOrderNo2":null,"smallOrderNo3":null,"gameKeyDescList":null,"operation":null,"gameTypeList":null,"gameStrategy":null}],"otherGameList":[{"id":649,"gameName":"问道","gameLogo":null,"gameVersion":null,"gameLink":null,"gameSize":null,"gameDesc":null,"gameInfo":null,"gameSelected":null,"packages":null,"updateTime":null,"orderNo":null,"filename":null,"md5":null,"uploadId":null,"scoreLevel":null,"downloadCount":null,"versionCode":null,"versionName":null,"percentage":null,"isDelete":null,"createTime":null,"commentPeople":null,"appTypeId":0,"isHand":null,"isVR":null,"isHeadControl":null,"isTouchScreen":null,"simpleLabel":null,"urlSchema":null,"iosCompany":null,"gameAgentList":null,"gameDetailsImages":null,"gameKeyMapsImages":null,"questionResults":null,"smallOrderNo":null,"smallOrderNo1":null,"smallOrderNo2":null,"smallOrderNo3":null,"gameKeyDescList":null,"operation":null,"gameTypeList":null,"gameStrategy":null}]}
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
        private List<StandAloneGameListBean> standAloneGameList;
        private List<NetworkGameListBean> networkGameList;
        private List<OtherGameListBean> otherGameList;

        public List<StandAloneGameListBean> getStandAloneGameList() {
            return standAloneGameList;
        }

        public void setStandAloneGameList(List<StandAloneGameListBean> standAloneGameList) {
            this.standAloneGameList = standAloneGameList;
        }

        public List<NetworkGameListBean> getNetworkGameList() {
            return networkGameList;
        }

        public void setNetworkGameList(List<NetworkGameListBean> networkGameList) {
            this.networkGameList = networkGameList;
        }

        public List<OtherGameListBean> getOtherGameList() {
            return otherGameList;
        }

        public void setOtherGameList(List<OtherGameListBean> otherGameList) {
            this.otherGameList = otherGameList;
        }

        public static class StandAloneGameListBean {
            /**
             * id : 650
             * gameName : 波斯王子：影与火1
             * gameLogo : null
             * gameVersion : null
             * gameLink : null
             * gameSize : null
             * gameDesc : null
             * gameInfo : null
             * gameSelected : null
             * packages : null
             * updateTime : null
             * orderNo : null
             * filename : null
             * md5 : null
             * uploadId : null
             * scoreLevel : null
             * downloadCount : null
             * versionCode : null
             * versionName : null
             * percentage : null
             * isDelete : null
             * createTime : null
             * commentPeople : null
             * appTypeId : 0
             * isHand : null
             * isVR : null
             * isHeadControl : null
             * isTouchScreen : null
             * simpleLabel : null
             * urlSchema : null
             * iosCompany : null
             * gameAgentList : null
             * gameDetailsImages : null
             * gameKeyMapsImages : null
             * questionResults : null
             * smallOrderNo : null
             * smallOrderNo1 : null
             * smallOrderNo2 : null
             * smallOrderNo3 : null
             * gameKeyDescList : null
             * operation : null
             * gameTypeList : null
             * gameStrategy : null
             */

            private int id;
            private String gameName;
            private Object gameLogo;
            private Object gameVersion;
            private Object gameLink;
            private Object gameSize;
            private Object gameDesc;
            private Object gameInfo;
            private Object gameSelected;
            private Object packages;
            private Object updateTime;
            private Object orderNo;
            private Object filename;
            private Object md5;
            private Object uploadId;
            private Object scoreLevel;
            private Object downloadCount;
            private Object versionCode;
            private Object versionName;
            private Object percentage;
            private Object isDelete;
            private Object createTime;
            private Object commentPeople;
            private int appTypeId;
            private Object isHand;
            private Object isVR;
            private Object isHeadControl;
            private Object isTouchScreen;
            private Object simpleLabel;
            private Object urlSchema;
            private Object iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGameName() {
                return gameName;
            }

            public void setGameName(String gameName) {
                this.gameName = gameName;
            }

            public Object getGameLogo() {
                return gameLogo;
            }

            public void setGameLogo(Object gameLogo) {
                this.gameLogo = gameLogo;
            }

            public Object getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(Object gameVersion) {
                this.gameVersion = gameVersion;
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

            public Object getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(Object gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public Object getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(Object gameSelected) {
                this.gameSelected = gameSelected;
            }

            public Object getPackages() {
                return packages;
            }

            public void setPackages(Object packages) {
                this.packages = packages;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
                this.updateTime = updateTime;
            }

            public Object getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(Object orderNo) {
                this.orderNo = orderNo;
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

            public Object getUploadId() {
                return uploadId;
            }

            public void setUploadId(Object uploadId) {
                this.uploadId = uploadId;
            }

            public Object getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(Object scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public Object getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(Object downloadCount) {
                this.downloadCount = downloadCount;
            }

            public Object getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(Object versionCode) {
                this.versionCode = versionCode;
            }

            public Object getVersionName() {
                return versionName;
            }

            public void setVersionName(Object versionName) {
                this.versionName = versionName;
            }

            public Object getPercentage() {
                return percentage;
            }

            public void setPercentage(Object percentage) {
                this.percentage = percentage;
            }

            public Object getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(Object isDelete) {
                this.isDelete = isDelete;
            }

            public Object getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Object createTime) {
                this.createTime = createTime;
            }

            public Object getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(Object commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public Object getIsHand() {
                return isHand;
            }

            public void setIsHand(Object isHand) {
                this.isHand = isHand;
            }

            public Object getIsVR() {
                return isVR;
            }

            public void setIsVR(Object isVR) {
                this.isVR = isVR;
            }

            public Object getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(Object isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public Object getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(Object isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public Object getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(Object simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public Object getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(Object urlSchema) {
                this.urlSchema = urlSchema;
            }

            public Object getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(Object iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }

        public static class NetworkGameListBean {
            /**
             * id : 648
             * gameName : 王者荣耀
             * gameLogo : null
             * gameVersion : null
             * gameLink : null
             * gameSize : null
             * gameDesc : null
             * gameInfo : null
             * gameSelected : null
             * packages : null
             * updateTime : null
             * orderNo : null
             * filename : null
             * md5 : null
             * uploadId : null
             * scoreLevel : null
             * downloadCount : null
             * versionCode : null
             * versionName : null
             * percentage : null
             * isDelete : null
             * createTime : null
             * commentPeople : null
             * appTypeId : 0
             * isHand : null
             * isVR : null
             * isHeadControl : null
             * isTouchScreen : null
             * simpleLabel : null
             * urlSchema : null
             * iosCompany : null
             * gameAgentList : null
             * gameDetailsImages : null
             * gameKeyMapsImages : null
             * questionResults : null
             * smallOrderNo : null
             * smallOrderNo1 : null
             * smallOrderNo2 : null
             * smallOrderNo3 : null
             * gameKeyDescList : null
             * operation : null
             * gameTypeList : null
             * gameStrategy : null
             */

            private int id;
            private String gameName;
            private Object gameLogo;
            private Object gameVersion;
            private Object gameLink;
            private Object gameSize;
            private Object gameDesc;
            private Object gameInfo;
            private Object gameSelected;
            private Object packages;
            private Object updateTime;
            private Object orderNo;
            private Object filename;
            private Object md5;
            private Object uploadId;
            private Object scoreLevel;
            private Object downloadCount;
            private Object versionCode;
            private Object versionName;
            private Object percentage;
            private Object isDelete;
            private Object createTime;
            private Object commentPeople;
            private int appTypeId;
            private Object isHand;
            private Object isVR;
            private Object isHeadControl;
            private Object isTouchScreen;
            private Object simpleLabel;
            private Object urlSchema;
            private Object iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGameName() {
                return gameName;
            }

            public void setGameName(String gameName) {
                this.gameName = gameName;
            }

            public Object getGameLogo() {
                return gameLogo;
            }

            public void setGameLogo(Object gameLogo) {
                this.gameLogo = gameLogo;
            }

            public Object getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(Object gameVersion) {
                this.gameVersion = gameVersion;
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

            public Object getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(Object gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public Object getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(Object gameSelected) {
                this.gameSelected = gameSelected;
            }

            public Object getPackages() {
                return packages;
            }

            public void setPackages(Object packages) {
                this.packages = packages;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
                this.updateTime = updateTime;
            }

            public Object getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(Object orderNo) {
                this.orderNo = orderNo;
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

            public Object getUploadId() {
                return uploadId;
            }

            public void setUploadId(Object uploadId) {
                this.uploadId = uploadId;
            }

            public Object getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(Object scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public Object getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(Object downloadCount) {
                this.downloadCount = downloadCount;
            }

            public Object getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(Object versionCode) {
                this.versionCode = versionCode;
            }

            public Object getVersionName() {
                return versionName;
            }

            public void setVersionName(Object versionName) {
                this.versionName = versionName;
            }

            public Object getPercentage() {
                return percentage;
            }

            public void setPercentage(Object percentage) {
                this.percentage = percentage;
            }

            public Object getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(Object isDelete) {
                this.isDelete = isDelete;
            }

            public Object getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Object createTime) {
                this.createTime = createTime;
            }

            public Object getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(Object commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public Object getIsHand() {
                return isHand;
            }

            public void setIsHand(Object isHand) {
                this.isHand = isHand;
            }

            public Object getIsVR() {
                return isVR;
            }

            public void setIsVR(Object isVR) {
                this.isVR = isVR;
            }

            public Object getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(Object isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public Object getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(Object isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public Object getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(Object simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public Object getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(Object urlSchema) {
                this.urlSchema = urlSchema;
            }

            public Object getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(Object iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }

        public static class OtherGameListBean {
            /**
             * id : 649
             * gameName : 问道
             * gameLogo : null
             * gameVersion : null
             * gameLink : null
             * gameSize : null
             * gameDesc : null
             * gameInfo : null
             * gameSelected : null
             * packages : null
             * updateTime : null
             * orderNo : null
             * filename : null
             * md5 : null
             * uploadId : null
             * scoreLevel : null
             * downloadCount : null
             * versionCode : null
             * versionName : null
             * percentage : null
             * isDelete : null
             * createTime : null
             * commentPeople : null
             * appTypeId : 0
             * isHand : null
             * isVR : null
             * isHeadControl : null
             * isTouchScreen : null
             * simpleLabel : null
             * urlSchema : null
             * iosCompany : null
             * gameAgentList : null
             * gameDetailsImages : null
             * gameKeyMapsImages : null
             * questionResults : null
             * smallOrderNo : null
             * smallOrderNo1 : null
             * smallOrderNo2 : null
             * smallOrderNo3 : null
             * gameKeyDescList : null
             * operation : null
             * gameTypeList : null
             * gameStrategy : null
             */

            private int id;
            private String gameName;
            private Object gameLogo;
            private Object gameVersion;
            private Object gameLink;
            private Object gameSize;
            private Object gameDesc;
            private Object gameInfo;
            private Object gameSelected;
            private Object packages;
            private Object updateTime;
            private Object orderNo;
            private Object filename;
            private Object md5;
            private Object uploadId;
            private Object scoreLevel;
            private Object downloadCount;
            private Object versionCode;
            private Object versionName;
            private Object percentage;
            private Object isDelete;
            private Object createTime;
            private Object commentPeople;
            private int appTypeId;
            private Object isHand;
            private Object isVR;
            private Object isHeadControl;
            private Object isTouchScreen;
            private Object simpleLabel;
            private Object urlSchema;
            private Object iosCompany;
            private Object gameAgentList;
            private Object gameDetailsImages;
            private Object gameKeyMapsImages;
            private Object questionResults;
            private Object smallOrderNo;
            private Object smallOrderNo1;
            private Object smallOrderNo2;
            private Object smallOrderNo3;
            private Object gameKeyDescList;
            private Object operation;
            private Object gameTypeList;
            private Object gameStrategy;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getGameName() {
                return gameName;
            }

            public void setGameName(String gameName) {
                this.gameName = gameName;
            }

            public Object getGameLogo() {
                return gameLogo;
            }

            public void setGameLogo(Object gameLogo) {
                this.gameLogo = gameLogo;
            }

            public Object getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(Object gameVersion) {
                this.gameVersion = gameVersion;
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

            public Object getGameDesc() {
                return gameDesc;
            }

            public void setGameDesc(Object gameDesc) {
                this.gameDesc = gameDesc;
            }

            public Object getGameInfo() {
                return gameInfo;
            }

            public void setGameInfo(Object gameInfo) {
                this.gameInfo = gameInfo;
            }

            public Object getGameSelected() {
                return gameSelected;
            }

            public void setGameSelected(Object gameSelected) {
                this.gameSelected = gameSelected;
            }

            public Object getPackages() {
                return packages;
            }

            public void setPackages(Object packages) {
                this.packages = packages;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
                this.updateTime = updateTime;
            }

            public Object getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(Object orderNo) {
                this.orderNo = orderNo;
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

            public Object getUploadId() {
                return uploadId;
            }

            public void setUploadId(Object uploadId) {
                this.uploadId = uploadId;
            }

            public Object getScoreLevel() {
                return scoreLevel;
            }

            public void setScoreLevel(Object scoreLevel) {
                this.scoreLevel = scoreLevel;
            }

            public Object getDownloadCount() {
                return downloadCount;
            }

            public void setDownloadCount(Object downloadCount) {
                this.downloadCount = downloadCount;
            }

            public Object getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(Object versionCode) {
                this.versionCode = versionCode;
            }

            public Object getVersionName() {
                return versionName;
            }

            public void setVersionName(Object versionName) {
                this.versionName = versionName;
            }

            public Object getPercentage() {
                return percentage;
            }

            public void setPercentage(Object percentage) {
                this.percentage = percentage;
            }

            public Object getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(Object isDelete) {
                this.isDelete = isDelete;
            }

            public Object getCreateTime() {
                return createTime;
            }

            public void setCreateTime(Object createTime) {
                this.createTime = createTime;
            }

            public Object getCommentPeople() {
                return commentPeople;
            }

            public void setCommentPeople(Object commentPeople) {
                this.commentPeople = commentPeople;
            }

            public int getAppTypeId() {
                return appTypeId;
            }

            public void setAppTypeId(int appTypeId) {
                this.appTypeId = appTypeId;
            }

            public Object getIsHand() {
                return isHand;
            }

            public void setIsHand(Object isHand) {
                this.isHand = isHand;
            }

            public Object getIsVR() {
                return isVR;
            }

            public void setIsVR(Object isVR) {
                this.isVR = isVR;
            }

            public Object getIsHeadControl() {
                return isHeadControl;
            }

            public void setIsHeadControl(Object isHeadControl) {
                this.isHeadControl = isHeadControl;
            }

            public Object getIsTouchScreen() {
                return isTouchScreen;
            }

            public void setIsTouchScreen(Object isTouchScreen) {
                this.isTouchScreen = isTouchScreen;
            }

            public Object getSimpleLabel() {
                return simpleLabel;
            }

            public void setSimpleLabel(Object simpleLabel) {
                this.simpleLabel = simpleLabel;
            }

            public Object getUrlSchema() {
                return urlSchema;
            }

            public void setUrlSchema(Object urlSchema) {
                this.urlSchema = urlSchema;
            }

            public Object getIosCompany() {
                return iosCompany;
            }

            public void setIosCompany(Object iosCompany) {
                this.iosCompany = iosCompany;
            }

            public Object getGameAgentList() {
                return gameAgentList;
            }

            public void setGameAgentList(Object gameAgentList) {
                this.gameAgentList = gameAgentList;
            }

            public Object getGameDetailsImages() {
                return gameDetailsImages;
            }

            public void setGameDetailsImages(Object gameDetailsImages) {
                this.gameDetailsImages = gameDetailsImages;
            }

            public Object getGameKeyMapsImages() {
                return gameKeyMapsImages;
            }

            public void setGameKeyMapsImages(Object gameKeyMapsImages) {
                this.gameKeyMapsImages = gameKeyMapsImages;
            }

            public Object getQuestionResults() {
                return questionResults;
            }

            public void setQuestionResults(Object questionResults) {
                this.questionResults = questionResults;
            }

            public Object getSmallOrderNo() {
                return smallOrderNo;
            }

            public void setSmallOrderNo(Object smallOrderNo) {
                this.smallOrderNo = smallOrderNo;
            }

            public Object getSmallOrderNo1() {
                return smallOrderNo1;
            }

            public void setSmallOrderNo1(Object smallOrderNo1) {
                this.smallOrderNo1 = smallOrderNo1;
            }

            public Object getSmallOrderNo2() {
                return smallOrderNo2;
            }

            public void setSmallOrderNo2(Object smallOrderNo2) {
                this.smallOrderNo2 = smallOrderNo2;
            }

            public Object getSmallOrderNo3() {
                return smallOrderNo3;
            }

            public void setSmallOrderNo3(Object smallOrderNo3) {
                this.smallOrderNo3 = smallOrderNo3;
            }

            public Object getGameKeyDescList() {
                return gameKeyDescList;
            }

            public void setGameKeyDescList(Object gameKeyDescList) {
                this.gameKeyDescList = gameKeyDescList;
            }

            public Object getOperation() {
                return operation;
            }

            public void setOperation(Object operation) {
                this.operation = operation;
            }

            public Object getGameTypeList() {
                return gameTypeList;
            }

            public void setGameTypeList(Object gameTypeList) {
                this.gameTypeList = gameTypeList;
            }

            public Object getGameStrategy() {
                return gameStrategy;
            }

            public void setGameStrategy(Object gameStrategy) {
                this.gameStrategy = gameStrategy;
            }
        }
    }
}
