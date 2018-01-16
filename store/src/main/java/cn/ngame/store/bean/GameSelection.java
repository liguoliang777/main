package cn.ngame.store.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zeng on 2016/6/15.
 */
public class GameSelection implements Serializable {

    public List<GameInfo> touchScreenSelectList;
    public List<GameInfo> handleSelectList;
    public List<GameInfo> VRSelectList;
}
