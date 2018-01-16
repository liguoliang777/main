package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * 游戏分类
 * Created by zeng on 2016/5/31.
 */
public class Category implements Serializable{

    public int id;
    public String typeName;

    public Category() {
    }

    public Category(int id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (id != category.id) return false;
        return typeName.equals(category.typeName);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + typeName.hashCode();
        return result;
    }
}
