package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * 用户的实体类
 * Created by zeng on 2016/6/12.
 */
public class User implements Serializable {


    /**
     * id : 2
     * userCode : UC1500108412881
     * nickName : 4444
     * loginName : 18621767596
     * password : 60851f6e7795ca38b6c3be80e5dd951d
     * type : 1
     * status : 0
     * mobile : 18621767596
     * gender : 男
     * headPhoto : http://oss.ngame.cn/upload/userHead/1500454650621.png
     * appTypeId : 2
     * isDelete : 0
     * createTime : 1500108331000
     * updateTime : 1500364165000
     * token : null
     */

    public int id;
    public String userCode;
    public String nickName;
    public String loginName;
    public String password;
    public int type;
    public int status;
    public String mobile;
    public String gender;
    public String headPhoto;
    public int appTypeId;
    public int isDelete;
    public long createTime;
    public long updateTime;
    public String token;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userCode='" + userCode + '\'' +
                ", nickName='" + nickName + '\'' +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", mobile='" + mobile + '\'' +
                ", gender='" + gender + '\'' +
                ", headPhoto='" + headPhoto + '\'' +
                ", appTypeId=" + appTypeId +
                ", isDelete=" + isDelete +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", token='" + token + '\'' +
                '}';
    }
}
