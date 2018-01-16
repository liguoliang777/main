package cn.ngame.store.gamehub.bean;

import java.io.Serializable;

/**
 * Created by jztdzsw on 15-8-16.
 */
public class PictureBean implements Serializable{
    private String localURL; //图片本地地址

    public String getLocalURL() {
        return localURL;
    }

    public void setLocalURL(String localURL) {
        this.localURL = localURL;
    }
}
