package com.jzt.hol.android.jkda.sdk.bean.gamehub;

import java.util.List;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

public class CommentBean {

    /**
     * code : 0
     * msg : null
     * data : [{"id":14,"postTagId":1,"nickName":"高攀测试账号","postTitle":"额共鸣民工","content":"我是一只小小鸟","createTime":1488964751000,"appTypeId":0},{"id":14,"postTagId":1,"nickName":"高攀测试账号","postTitle":"额共鸣民工","content":"第二次丁敏哦你明明你明明您脂溢性迷宫定哦lol哦咯JOJO额明晓溪洗澡咯咯in民间组织走去外婆和","createTime":1488964821000,"appTypeId":0},{"id":14,"postTagId":1,"nickName":"高攀测试账号","postTitle":"额共鸣民工","content":"方凯凯凯凯凯凯","createTime":1488967797000,"appTypeId":0},{"id":13,"postTagId":2,"nickName":"高攀测试账号","postTitle":"别看我只是一只羊","content":"我好难过，急需一枚妹子","createTime":1489024051000,"appTypeId":0},{"id":13,"postTagId":2,"nickName":"高攀测试账号","postTitle":"别看我只是一只羊","content":"再来一发","createTime":1489024475000,"appTypeId":0},{"id":14,"postTagId":1,"nickName":"高攀测试账号","postTitle":"额共鸣民工","content":"","createTime":1489107316000,"appTypeId":0},{"id":14,"postTagId":1,"nickName":"高攀测试账号","postTitle":"额共鸣民工","content":"","createTime":1489107316000,"appTypeId":0}]
     * map : null
     * innerResult : null
     */

    private int code;
    private String msg;
    private Object map;
    private Object innerResult;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 14
         * postTagId : 1
         * nickName : 高攀测试账号
         * postTitle : 额共鸣民工
         * content : 我是一只小小鸟
         * createTime : 1488964751000
         * appTypeId : 0
         */

        private int id;
        private int postTagId;
        private String nickName;
        private String postTitle;
        private String content;
        private long createTime;
        private int appTypeId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPostTagId() {
            return postTagId;
        }

        public void setPostTagId(int postTagId) {
            this.postTagId = postTagId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPostTitle() {
            return postTitle;
        }

        public void setPostTitle(String postTitle) {
            this.postTitle = postTitle;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getAppTypeId() {
            return appTypeId;
        }

        public void setAppTypeId(int appTypeId) {
            this.appTypeId = appTypeId;
        }
    }
}
