package cn.ngame.store.bean;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class ClassifyTopBean {
    long id;
    int icon;
    String name;

    public ClassifyTopBean(String name, long id, int icon) {
        this.id = id;
        this.icon = icon;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }
}
