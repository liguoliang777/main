package cn.ngame.store.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zeng on 2016/6/23.
 */
public class SearchWrapper implements Serializable{

    public List<VideoInfo> videoList;
    public List<GameInfo> gameList;
}
