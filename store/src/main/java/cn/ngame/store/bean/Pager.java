package cn.ngame.store.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 用于存储分页信息
 * Created by zeng on 2016/6/6.
 */
public class Pager implements Serializable {

    public int page = 1;
    public int pageSize = 8;
    public int totals = 0;
    private List<GameInfo> data;


    public List<GameInfo> getData() {
        return data;
    }
    public void setData(List<GameInfo> data) {
        if(this.data == null){
            this.data = data;
        }else {
            this.data.addAll(data);
        }

    }
}
