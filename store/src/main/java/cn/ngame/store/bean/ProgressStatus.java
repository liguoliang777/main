package cn.ngame.store.bean;

import java.io.Serializable;

/**
 * 用于保存ListView 的下载状态
 * Created by zeng on 2016/6/4.
 */
public class ProgressStatus implements Serializable {

    public int position;
    public int process;
    public int status;

    public ProgressStatus() {
    }

    public ProgressStatus(int status) {
        this.status = status;
    }

    public ProgressStatus(int status, int process) {
        this.process = process;
        this.status = status;
    }

    public ProgressStatus(int position, int status, int process) {
        this.position = position;
        this.status = status;
        this.process = process;
    }
}
