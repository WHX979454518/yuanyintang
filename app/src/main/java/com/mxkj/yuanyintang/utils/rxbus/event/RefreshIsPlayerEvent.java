package com.mxkj.yuanyintang.utils.rxbus.event;

/**
 * 更新播放按钮
 * Created by LiuJie on 2017/10/26.
 */

public class RefreshIsPlayerEvent {

    private String type;
    private int position;
    private Boolean player;

    public RefreshIsPlayerEvent(String type, int position, Boolean player) {
        this.type = type;
        this.position = position;
        this.player = player;
    }

    public Boolean getPlayer() {
        return player;
    }

    public void setPlayer(Boolean player) {
        this.player = player;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
