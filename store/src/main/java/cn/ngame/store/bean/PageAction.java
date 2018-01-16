package cn.ngame.store.bean;

/**
 * Created by jztdzsw on 15-8-14.
 */
public class PageAction {
    private int currentPage;
    private int pageSize;
    private int total;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
