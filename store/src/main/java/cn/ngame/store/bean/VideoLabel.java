package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * Created by zeng on 2016/6/13.
 */
public class VideoLabel implements Serializable {

    public long id;
    public String labelName;

    public VideoLabel() {
    }

    public VideoLabel(long id, String labelName) {
        this.id = id;
        this.labelName = labelName;
    }
}
