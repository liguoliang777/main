package com.jzt.hol.android.jkda.sdk.bean.gamehub;

import java.util.List;

/**
 * Created by Administrator on 2017/3/6 0006.
 */

public class GameHubMainBean {

    /**
     * code : 0
     * msg : null
     * data : [{"id":3,"userCode":"UC0000000000069","postPublisher":"新用户76920",
     * "headPhoto":"http://oss.ngame.cn/upload/userHead/1488614496280.jpg","postTitle":"我不会玩","postTagId":1,"watchNum":0,
     * "postContent":"有哦宽松估摸着下午说GPS咯哦哦","createTime":1488614643000,"updateTime":1488614643000,"appTypeId":0,"commentNum":0,
     * "postImage":"http://oss.ngame.cn/upload/postImage/1488614642772.png,http://oss.ngame.cn/upload/postImage/1488614645034
     * .png,http://oss.ngame.cn/upload/postImage/1488614647262.png","pointNum":0,"voteNum":0,"isDelete":0,"isReport":0,
     * "orderNo":0,"isPoint":null},{"id":2,"userCode":"UC0000000000045","postPublisher":"13035114192","headPhoto":"http://oss
     * .ngame.cn/upload/userHead/1474265729013.jpg","postTitle":"今日头条","postTagId":1,"watchNum":0,"postContent":"挺好的一个头条",
     * "createTime":1488595458000,"updateTime":1488595458000,"appTypeId":0,"commentNum":0,"postImage":"http://oss.ngame
     * .cn/upload/postImage/1488595457992.png","pointNum":0,"voteNum":0,"isDelete":0,"isReport":0,"orderNo":0,"isPoint":null},
     * {"id":1,"userCode":"UC0000000000045","postPublisher":"13035114192","headPhoto":"http://oss.ngame
     * .cn/upload/userHead/1474265729013.jpg","postTitle":"今日头条","postTagId":1,"watchNum":0,"postContent":"挺好的一个头条",
     * "createTime":1488593747000,"updateTime":1488593747000,"appTypeId":0,"commentNum":0,"postImage":"http://oss.ngame
     * .cn/upload/postImage/1488593746965.png","pointNum":0,"voteNum":0,"isDelete":0,"isReport":1,"orderNo":0,"isPoint":null}]
     * map : null
     * innerResult : null
     * page : 1
     * pageSize : 10
     * totals : 3
     */

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

    public static class DataBean {

        /**
         * id : 5
         * postRoleName : 官方推荐
         * postRoleHeadPhoto :
         * postCategoryName : 绝地求生
         * postTitle : 测试绝地求生
         * postContent : 测试绝地求生编辑
         * watchNum : 0
         * pointNum : 100
         * updateTime : 1512438480000
         * postImageList : [{"id":4,"postImageAddress":"http://oss.ngame.cn/upload/1512525551589.png","postId":5,
         * "postOrderNo":1,"isDelete":null}]
         * showPostCategory : {"id":23,"postCategoryName":"绝地求生","postCategoryCount":0,"postCategoryUrl":null,"showPostCategoryList":null}
         */

        private int id;
        private String postRoleName;
        private String postRoleHeadPhoto;
        private String postCategoryName;
        private String postTitle;
        private String postContent;
        private int watchNum;
        private int pointNum;
        private long updateTime;
        private ShowPostCategoryBean showPostCategory;
        private List<PostImageListBean> postImageList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPostRoleName() {
            return postRoleName;
        }

        public void setPostRoleName(String postRoleName) {
            this.postRoleName = postRoleName;
        }

        public String getPostRoleHeadPhoto() {
            return postRoleHeadPhoto;
        }

        public void setPostRoleHeadPhoto(String postRoleHeadPhoto) {
            this.postRoleHeadPhoto = postRoleHeadPhoto;
        }

        public String getPostCategoryName() {
            return postCategoryName;
        }

        public void setPostCategoryName(String postCategoryName) {
            this.postCategoryName = postCategoryName;
        }

        public String getPostTitle() {
            return postTitle;
        }

        public void setPostTitle(String postTitle) {
            this.postTitle = postTitle;
        }

        public String getPostContent() {
            return postContent;
        }

        public void setPostContent(String postContent) {
            this.postContent = postContent;
        }

        public int getWatchNum() {
            return watchNum;
        }

        public void setWatchNum(int watchNum) {
            this.watchNum = watchNum;
        }

        public int getPointNum() {
            return pointNum;
        }

        public void setPointNum(int pointNum) {
            this.pointNum = pointNum;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public ShowPostCategoryBean getShowPostCategory() {
            return showPostCategory;
        }

        public void setShowPostCategory(ShowPostCategoryBean showPostCategory) {
            this.showPostCategory = showPostCategory;
        }

        public List<PostImageListBean> getPostImageList() {
            return postImageList;
        }

        public void setPostImageList(List<PostImageListBean> postImageList) {
            this.postImageList = postImageList;
        }

        public static class ShowPostCategoryBean {
            /**
             * id : 23
             * postCategoryName : 绝地求生
             * postCategoryCount : 0
             * postCategoryUrl : null
             * showPostCategoryList : null
             */

            private int id;
            private String postCategoryName;
            private int postCategoryCount;
            private Object postCategoryUrl;
            private Object showPostCategoryList;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPostCategoryName() {
                return postCategoryName;
            }

            public void setPostCategoryName(String postCategoryName) {
                this.postCategoryName = postCategoryName;
            }

            public int getPostCategoryCount() {
                return postCategoryCount;
            }

            public void setPostCategoryCount(int postCategoryCount) {
                this.postCategoryCount = postCategoryCount;
            }

            public Object getPostCategoryUrl() {
                return postCategoryUrl;
            }

            public void setPostCategoryUrl(Object postCategoryUrl) {
                this.postCategoryUrl = postCategoryUrl;
            }

            public Object getShowPostCategoryList() {
                return showPostCategoryList;
            }

            public void setShowPostCategoryList(Object showPostCategoryList) {
                this.showPostCategoryList = showPostCategoryList;
            }
        }

        public static class PostImageListBean {
            /**
             * id : 4
             * postImageAddress : http://oss.ngame.cn/upload/1512525551589.png
             * postId : 5
             * postOrderNo : 1
             * isDelete : null
             */

            private int id;
            private String postImageAddress;
            private int postId;
            private int postOrderNo;
            private Object isDelete;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPostImageAddress() {
                return postImageAddress;
            }

            public void setPostImageAddress(String postImageAddress) {
                this.postImageAddress = postImageAddress;
            }

            public int getPostId() {
                return postId;
            }

            public void setPostId(int postId) {
                this.postId = postId;
            }

            public int getPostOrderNo() {
                return postOrderNo;
            }

            public void setPostOrderNo(int postOrderNo) {
                this.postOrderNo = postOrderNo;
            }

            public Object getIsDelete() {
                return isDelete;
            }

            public void setIsDelete(Object isDelete) {
                this.isDelete = isDelete;
            }
        }
    }
}
