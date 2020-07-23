package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music;

/**
 * Created by zuixia on 2018/9/4.
 */

public class ProBean {
    int pro;
    long currSize;
    long totalSize;
    String biaoshi;

    public String getBiaoshi() {
        return biaoshi;
    }

    public ProBean(int pro, long currSize, long totalSize, String biaoshi) {
        this.pro = pro;
        this.currSize = currSize;
        this.totalSize = totalSize;
        this.biaoshi = biaoshi;
    }

    public void setBiaoshi(String biaoshi) {
        this.biaoshi = biaoshi;
    }

    public ProBean(int pro, long currSize, long totalSize) {
        this.pro = pro;
        this.currSize = currSize;
        this.totalSize = totalSize;
    }

    public long getCurrSize() {
        return currSize;
    }

    public void setCurrSize(long currSize) {
        this.currSize = currSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }


    public int getPro() {
        return pro;
    }

    public void setPro(int pro) {
        this.pro = pro;
    }
}
