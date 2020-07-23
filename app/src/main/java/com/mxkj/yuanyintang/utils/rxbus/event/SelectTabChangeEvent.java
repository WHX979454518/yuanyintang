package com.mxkj.yuanyintang.utils.rxbus.event;

/**
 * Created by LiuJie on 2017/10/25.
 */

public class SelectTabChangeEvent {

    private int tab;//101,附近的人跳转

    public SelectTabChangeEvent(int tab) {
        this.tab = tab;
    }

    public int getTab() {
        return tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }
}
