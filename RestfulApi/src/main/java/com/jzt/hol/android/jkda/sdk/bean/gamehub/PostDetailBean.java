package com.jzt.hol.android.jkda.sdk.bean.gamehub;

import java.util.List;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class PostDetailBean {
    /**
     * code : 0
     * msg : null
     * data : {"id":1,"postRoleName":"官方推荐8","postRoleHeadPhoto":"http://oss.ngame.cn/upload/1512790187629.png",
     * "postCategoryName":"网易","postTitle":"测试测试2","postContent":"测试测试2","watchNum":0,"pointNum":100,
     * "updateTime":1512377882000,"postImageList":null,"showPostCategory":{"id":21,"postCategoryName":"网易",
     * "postCategoryCount":0,"postCategoryUrl":null,"showPostCategoryList":null}}
     * map : null
     * innerResult : null
     */

    private int code;
    private Object msg;
    private DataBean data;
    private Object map;
    private Object innerResult;

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
         * id : 7
         * postRoleName : 技术小达人
         * postRoleHeadPhoto : http://oss.ngame.cn/upload/1513060555531.jpg
         * orderNO : 0
         * postTitle : 荒野行动操作指南
         * postContent : 基础设置

         准心形状有三个准心，准心的话个人比较倾向第一种，当然还是看个人的感觉，可以切到游戏里尝试一下。

         辅助瞄准

         命中反馈，和左右探头射击这是要点的，前两个增加打人时的一个反馈，左右探头射击，可以让你在车外进行射击，至于高频率模式则要看你手机，有些开着，会流畅，有些反而会卡，引人而异，所以自己选择。

         左手开火，这个我是点总是开启，原因就一点，右手瞄准人，左手打，这样打人舒服，一般惯用右手的人，都可以点上，惯用左手的就相反，陀螺仪和拾取列表看个人喜好。

         基础设置

         准心形状有三个准心，准心的话个人比较倾向第一种，当然还是看个人的感觉，可以切到游戏里尝试一下。

         辅助瞄准

         命中反馈，和左右探头射击这是要点的，前两个增加打人时的一个反馈，左右探头射击，可以让你在车外进行射击，至于高频率模式则要看你手机，有些开着，会流畅，有些反而会卡，引人而异，所以自己选择。

         左手开火，这个我是点总是开启，原因就一点，右手瞄准人，左手打，这样打人舒服，一般惯用右手的人，都可以点上，惯用左手的就相反，陀螺仪和拾取列表看个人喜好。
         * watchNum : 3
         * pointNum : 0
         * isPoint : 0
         * updateTime : 1513060817000
         * postImageList : [{"id":9,"postImageAddress":"http://oss.ngame.cn/upload/1513060779252.jpg","postId":7,
         * "postOrderNo":1,"isDelete":null},{"id":10,"postImageAddress":"http://oss.ngame.cn/upload/1513060787241.jpg",
         * "postId":7,"postOrderNo":2,"isDelete":null}]
         * showPostCategory : {"id":30,"postCategoryName":"荒野行动","postCategoryCount":0,"postCategoryUrl":null,"showPostCategoryList":null}
         */

        private int id;
        private String postRoleName;
        private String postRoleHeadPhoto;
        private int orderNO;
        private String postTitle;
        private String postContent;
        private int watchNum;
        private int pointNum;
        private int isPoint;
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

        public int getOrderNO() {
            return orderNO;
        }

        public void setOrderNO(int orderNO) {
            this.orderNO = orderNO;
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

        public int getIsPoint() {
            return isPoint;
        }

        public void setIsPoint(int isPoint) {
            this.isPoint = isPoint;
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
             * id : 30
             * postCategoryName : 荒野行动
             * postCategoryCount : 0
             * postCategoryUrl : null
             * showPostCategoryList : null
             */

            private int id;
            private String postCategoryName;
            private int postCategoryCount;
            private String postCategoryUrl;
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

            public String getPostCategoryUrl() {
                return postCategoryUrl;
            }

            public void setPostCategoryUrl(String postCategoryUrl) {
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
             * id : 9
             * postImageAddress : http://oss.ngame.cn/upload/1513060779252.jpg
             * postId : 7
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
