package cn.ngame.store.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 热门推荐信息
 * Created by zeng on 2016/6/15.
 */
public class NecessaryListInfo implements Serializable {

    /**
     * id : 6
     * parentId : 0
     * toolName : 腾讯
     * installDesc : null
     * toolLogo : null
     * toolURL : null
     * md5 : null
     * uploadId : null
     * fileName : null
     * fileSize : 0
     * downloadCount : 0
     * appTypeId : null
     * orderNo : 0
     * isDelete : 0
     * createTime : null
     * updateTime : null
     * toolVersion : null
     * packages : null
     * auxiliaryTools : [{"id":2,"parentId":6,"toolName":"破解工具","installDesc":"范德萨范德萨","toolLogo":"http://oss.ngame
     * .cn/auxToolLogo/1494494984130.png","toolURL":"http://oss.ngame.cn/log4j-1.2.17.jar",
     * "md5":"04a41f0a068986f0f73485cf507c0f40","uploadId":null,"fileName":"log4j-1.2.17.jar","fileSize":489884,
     * "downloadCount":0,"appTypeId":null,"orderNo":0,"isDelete":0,"createTime":null,"updateTime":null,"toolVersion":"1.0.0",
     * "packages":"ferwe","auxiliaryTools":null}]
     */

    private int id;
    private int parentId;
    private String toolName;
    private Object installDesc;
    private Object toolLogo;
    private Object toolURL;
    private Object md5;
    private Object uploadId;
    private Object fileName;
    private int fileSize;
    private int downloadCount;
    private Object appTypeId;
    private int orderNo;
    private int isDelete;
    private Object createTime;
    private Object updateTime;
    private Object toolVersion;
    private Object packages;
    private List<AuxiliaryToolsBean> auxiliaryTools;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public Object getInstallDesc() {
        return installDesc;
    }

    public void setInstallDesc(Object installDesc) {
        this.installDesc = installDesc;
    }

    public Object getToolLogo() {
        return toolLogo;
    }

    public void setToolLogo(Object toolLogo) {
        this.toolLogo = toolLogo;
    }

    public Object getToolURL() {
        return toolURL;
    }

    public void setToolURL(Object toolURL) {
        this.toolURL = toolURL;
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

    public Object getFileName() {
        return fileName;
    }

    public void setFileName(Object fileName) {
        this.fileName = fileName;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Object getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(Object appTypeId) {
        this.appTypeId = appTypeId;
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

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public Object getToolVersion() {
        return toolVersion;
    }

    public void setToolVersion(Object toolVersion) {
        this.toolVersion = toolVersion;
    }

    public Object getPackages() {
        return packages;
    }

    public void setPackages(Object packages) {
        this.packages = packages;
    }

    public List<AuxiliaryToolsBean> getAuxiliaryTools() {
        return auxiliaryTools;
    }

    public void setAuxiliaryTools(List<AuxiliaryToolsBean> auxiliaryTools) {
        this.auxiliaryTools = auxiliaryTools;
    }

    public static class AuxiliaryToolsBean {
        /**
         * id : 2
         * parentId : 6
         * toolName : 破解工具
         * installDesc : 范德萨范德萨
         * toolLogo : http://oss.ngame.cn/auxToolLogo/1494494984130.png
         * toolURL : http://oss.ngame.cn/log4j-1.2.17.jar
         * md5 : 04a41f0a068986f0f73485cf507c0f40
         * uploadId : null
         * fileName : log4j-1.2.17.jar
         * fileSize : 489884
         * downloadCount : 0
         * appTypeId : null
         * orderNo : 0
         * isDelete : 0
         * createTime : null
         * updateTime : null
         * toolVersion : 1.0.0
         * packages : ferwe
         * auxiliaryTools : null
         */

        private int id;
        private int parentId;
        private String toolName;
        private String installDesc;
        private String toolLogo;
        private String toolURL;
        private String md5;
        private Object uploadId;
        private String fileName;
        private int fileSize;
        private int downloadCount;
        private Object appTypeId;
        private int orderNo;
        private int isDelete;
        private Object createTime;
        private Object updateTime;
        private String toolVersion;
        private String packages;
        private Object auxiliaryTools;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getToolName() {
            return toolName;
        }

        public void setToolName(String toolName) {
            this.toolName = toolName;
        }

        public String getInstallDesc() {
            return installDesc;
        }

        public void setInstallDesc(String installDesc) {
            this.installDesc = installDesc;
        }

        public String getToolLogo() {
            return toolLogo;
        }

        public void setToolLogo(String toolLogo) {
            this.toolLogo = toolLogo;
        }

        public String getToolURL() {
            return toolURL;
        }

        public void setToolURL(String toolURL) {
            this.toolURL = toolURL;
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

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public int getFileSize() {
            return fileSize;
        }

        public void setFileSize(int fileSize) {
            this.fileSize = fileSize;
        }

        public int getDownloadCount() {
            return downloadCount;
        }

        public void setDownloadCount(int downloadCount) {
            this.downloadCount = downloadCount;
        }

        public Object getAppTypeId() {
            return appTypeId;
        }

        public void setAppTypeId(Object appTypeId) {
            this.appTypeId = appTypeId;
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

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public String getToolVersion() {
            return toolVersion;
        }

        public void setToolVersion(String toolVersion) {
            this.toolVersion = toolVersion;
        }

        public String getPackages() {
            return packages;
        }

        public void setPackages(String packages) {
            this.packages = packages;
        }

        public Object getAuxiliaryTools() {
            return auxiliaryTools;
        }

        public void setAuxiliaryTools(Object auxiliaryTools) {
            this.auxiliaryTools = auxiliaryTools;
        }
    }
}
