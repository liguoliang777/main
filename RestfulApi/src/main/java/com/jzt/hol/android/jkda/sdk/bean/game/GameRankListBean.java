package com.jzt.hol.android.jkda.sdk.bean.game;

import java.io.Serializable;
import java.util.List;

/**
 * 游戏列表
 * Created by gp on 2017/3/18 0018.
 */

public class GameRankListBean implements Serializable{

    private int code;
    private String msg;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
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
         * id : 638
         * gameName : 龙之谷
         * gameLogo : http://oss.ngame.cn/upload/1488424068534.png
         * gameVersion :
         * gameLink : http://oss.ngame.cn/games/hbwy/%E9%BE%99%E4%B9%8B%E8%B0%B7_may_20170308.apk
         * gameSize : 664302434
         * gameDesc : 《龙之谷》手游是一款由腾讯运营，以端游《龙之谷》为故事背景制作的3DARPG手游。游戏原汁原味复刻了端游的剧情、世界观，带你重回记忆中的阿尔特里亚，重现最激爽的3D无锁定战斗，给你畅快的指尖战斗体验！和小伙伴一起组队屠龙，更有竞技场可以一决高下，社交、公会系统让你再续前缘，重新找回当年的好友！
         《龙之谷》手游是一款由腾讯运营，以端游《龙之谷》为故事背景制作的3DARPG手游。游戏原汁原味复刻了端游的剧情、世界观，带你重回记忆中的阿尔特里亚，重现最激爽的3D无锁定战斗，给你畅快的指尖战斗体验！和小伙伴一起组队屠龙，更有竞技场可以一决高下，社交、公会系统让你再续前缘，重新找回当年的好友！

         * gameInfo : null
         * gameSelected : 0
         * packages : com.tencent.tmgp.dragonnest
         * updateTime : 1489027090000
         * orderNo : 0
         * filename : 龙之谷_20170227_killer.apk
         * md5 : C54BAAB333320E78B2366218007812A9
         * uploadId : null
         * scoreLevel : 5
         * downloadCount : 2920
         * versionCode : 170225
         * versionName : 1.11.0
         * percentage : 2.3
         * isDelete : 0
         * createTime : 1488423678000
         * commentPeople : 9
         * appTypeId : 0
         * isHand : 1
         * isVR : 0
         * isHeadControl : 0
         * isTouchScreen : 1
         * simpleLabel :
         * urlSchema :
         * iosCompany : 1
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

        private long id;
        private String gameName;
        private String gameLogo;
        private String gameVersion;
        private String gameLink;
        private String imgLink;
        private int gameSize;
        private String gameDesc;
        private Object gameInfo;
        private int gameSelected;
        private String packages;
        private long updateTime;
        private int orderNo;
        private String filename;
        private String md5;
        private Object uploadId;
        private int scoreLevel;
        private int downloadCount;
        private int versionCode;
        private String versionName;
        private float percentage;
        private int isDelete;
        private long createTime;
        private int commentPeople;
        private int appTypeId;
        private int isHand;
        private int isVR;
        private int isHeadControl;
        private int isTouchScreen;
        private String simpleLabel;
        private String urlSchema;
        private int iosCompany;
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

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
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

        public String getGameVersion() {
            return gameVersion;
        }

        public void setGameVersion(String gameVersion) {
            this.gameVersion = gameVersion;
        }

        public String getGameLink() {
            return gameLink;
        }
        public String getImgLink() {
            return imgLink;
        }

        public void setImgLink(String imgLink) {
            this.imgLink = imgLink;
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

        public String getGameDesc() {
            return gameDesc;
        }

        public void setGameDesc(String gameDesc) {
            this.gameDesc = gameDesc;
        }

        public Object getGameInfo() {
            return gameInfo;
        }

        public void setGameInfo(Object gameInfo) {
            this.gameInfo = gameInfo;
        }

        public int getGameSelected() {
            return gameSelected;
        }

        public void setGameSelected(int gameSelected) {
            this.gameSelected = gameSelected;
        }

        public String getPackages() {
            return packages;
        }

        public void setPackages(String packages) {
            this.packages = packages;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(int orderNo) {
            this.orderNo = orderNo;
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

        public Object getUploadId() {
            return uploadId;
        }

        public void setUploadId(Object uploadId) {
            this.uploadId = uploadId;
        }

        public int getScoreLevel() {
            return scoreLevel;
        }

        public void setScoreLevel(int scoreLevel) {
            this.scoreLevel = scoreLevel;
        }

        public int getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(int downloadCount) {
            this.downloadCount = downloadCount;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public float getPercentage() {
            return percentage;
        }

        public void setPercentage(float percentage) {
            this.percentage = percentage;
        }

        public int getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(int isDelete) {
            this.isDelete = isDelete;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getCommentPeople() {
            return commentPeople;
        }

        public void setCommentPeople(int commentPeople) {
            this.commentPeople = commentPeople;
        }

        public int getAppTypeId() {
            return appTypeId;
        }

        public void setAppTypeId(int appTypeId) {
            this.appTypeId = appTypeId;
        }

        public int getIsHand() {
            return isHand;
        }

        public void setIsHand(int isHand) {
            this.isHand = isHand;
        }

        public int getIsVR() {
            return isVR;
        }

        public void setIsVR(int isVR) {
            this.isVR = isVR;
        }

        public int getIsHeadControl() {
            return isHeadControl;
        }

        public void setIsHeadControl(int isHeadControl) {
            this.isHeadControl = isHeadControl;
        }

        public int getIsTouchScreen() {
            return isTouchScreen;
        }

        public void setIsTouchScreen(int isTouchScreen) {
            this.isTouchScreen = isTouchScreen;
        }

        public String getSimpleLabel() {
            return simpleLabel;
        }

        public void setSimpleLabel(String simpleLabel) {
            this.simpleLabel = simpleLabel;
        }

        public String getUrlSchema() {
            return urlSchema;
        }

        public void setUrlSchema(String urlSchema) {
            this.urlSchema = urlSchema;
        }

        public int getIosCompany() {
            return iosCompany;
        }

        public void setIosCompany(int iosCompany) {
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

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", gameName='" + gameName + '\'' +
                    ", gameLogo='" + gameLogo + '\'' +
                    ", gameVersion='" + gameVersion + '\'' +
                    ", gameLink='" + gameLink + '\'' +
                    ", gameSize=" + gameSize +
                    ", gameDesc='" + gameDesc + '\'' +
                    ", gameInfo=" + gameInfo +
                    ", gameSelected=" + gameSelected +
                    ", packages='" + packages + '\'' +
                    ", updateTime=" + updateTime +
                    ", orderNo=" + orderNo +
                    ", filename='" + filename + '\'' +
                    ", md5='" + md5 + '\'' +
                    ", uploadId=" + uploadId +
                    ", scoreLevel=" + scoreLevel +
                    ", downloadCount=" + downloadCount +
                    ", versionCode=" + versionCode +
                    ", versionName='" + versionName + '\'' +
                    ", percentage=" + percentage +
                    ", isDelete=" + isDelete +
                    ", createTime=" + createTime +
                    ", commentPeople=" + commentPeople +
                    ", appTypeId=" + appTypeId +
                    ", isHand=" + isHand +
                    ", isVR=" + isVR +
                    ", isHeadControl=" + isHeadControl +
                    ", isTouchScreen=" + isTouchScreen +
                    ", simpleLabel='" + simpleLabel + '\'' +
                    ", urlSchema='" + urlSchema + '\'' +
                    ", iosCompany=" + iosCompany +
                    ", gameAgentList=" + gameAgentList +
                    ", gameDetailsImages=" + gameDetailsImages +
                    ", gameKeyMapsImages=" + gameKeyMapsImages +
                    ", questionResults=" + questionResults +
                    ", smallOrderNo=" + smallOrderNo +
                    ", smallOrderNo1=" + smallOrderNo1 +
                    ", smallOrderNo2=" + smallOrderNo2 +
                    ", smallOrderNo3=" + smallOrderNo3 +
                    ", gameKeyDescList=" + gameKeyDescList +
                    ", operation=" + operation +
                    ", gameTypeList=" + gameTypeList +
                    ", gameStrategy=" + gameStrategy +
                    '}';
        }
    }
}
