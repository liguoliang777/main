package cn.ngame.store.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zeng on 2016/6/13.
 */
public class VideoCategory implements Serializable {

    public static final String TAG = VideoCategory.class.getSimpleName();

    public List<VideoType> videoTypes;
    public List<VideoLabel> videoLabels;

}
