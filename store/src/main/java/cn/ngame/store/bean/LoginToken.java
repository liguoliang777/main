package cn.ngame.store.bean;

/**
 * 用户登录的Token信息
 * Created by zeng on 2016/6/12.
 */
public class LoginToken {

    /**
     * id : null
     * userCode : UC1500019542265
     * nickName : 新用户56163
     * loginName : 13419556163
     * password : 154b998e49fee22fd86a454a0b9e511b
     * type : 1
     * status : null
     * mobile : 13419556163
     * gender : 保密
     * headPhoto : http://oss.ngame.cn/userDefaultAvatar/user1.png
     * appTypeId : 0
     * isDelete : null
     * createTime : null
     * updateTime : null
     * token : 014d4a6526b04ed49a780fcd1c60dc84
     */

    private Object id;
    private String userCode;
    private String nickName;
    private String loginName;
    private String password;
    private int type;
    private Object status;
    private String mobile;
    private String gender;
    private String headPhoto;
    private int appTypeId;
    private Object isDelete;
    private Object createTime;
    private Object updateTime;
    private String token;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public int getAppTypeId() {
        return appTypeId;
    }

    public void setAppTypeId(int appTypeId) {
        this.appTypeId = appTypeId;
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

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
