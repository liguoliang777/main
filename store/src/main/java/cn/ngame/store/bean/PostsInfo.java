package cn.ngame.store.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 游戏软件的实体类
 * Created by zeng on 2016/5/16.
 */
public class PostsInfo implements Serializable {

    /**
     * code : 0
     * msg : null
     * data : [{"id":18,"postCategoryName":"厂商圈子","postCategoryCount":0,"postCategoryUrl":null,
     * "showPostCategoryList":[{"id":20,"postCategoryName":"腾讯","postCategoryCount":0,"postCategoryUrl":null,
     * "showPostCategoryList":null},{"id":21,"postCategoryName":"网易","postCategoryCount":0,"postCategoryUrl":null,
     * "showPostCategoryList":null}]},{"id":19,"postCategoryName":"游戏圈子","postCategoryCount":0,"postCategoryUrl":null,
     * "showPostCategoryList":[{"id":22,"postCategoryName":"王者荣耀","postCategoryCount":0,"postCategoryUrl":null,
     * "showPostCategoryList":null},{"id":23,"postCategoryName":"绝地求生","postCategoryCount":0,"postCategoryUrl":null,
     * "showPostCategoryList":null},{"id":30,"postCategoryName":"荒野行动","postCategoryCount":0,"postCategoryUrl":null,
     * "showPostCategoryList":null}]}]
     * map : null
     * innerResult : null
     */

    private int code;
    private Object msg;
    private Object map;
    private Object innerResult;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 18
         * postCategoryName : 厂商圈子
         * postCategoryCount : 0
         * postCategoryUrl : null
         * showPostCategoryList : [{"id":20,"postCategoryName":"腾讯","postCategoryCount":0,"postCategoryUrl":null,
         * "showPostCategoryList":null},{"id":21,"postCategoryName":"网易","postCategoryCount":0,"postCategoryUrl":null,
         * "showPostCategoryList":null}]
         */

        private int id;
        private String postCategoryName;
        private int postCategoryCount;
        private Object postCategoryUrl;
        private List<ShowPostCategoryListBean> showPostCategoryList;

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

        public List<ShowPostCategoryListBean> getShowPostCategoryList() {
            return showPostCategoryList;
        }

        public void setShowPostCategoryList(List<ShowPostCategoryListBean> showPostCategoryList) {
            this.showPostCategoryList = showPostCategoryList;
        }

        public static class ShowPostCategoryListBean {
            /**
             * id : 20
             * postCategoryName : 腾讯
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
    }
}
