package com.mxkj.yuanyintang.utils.rxbus.event;

/**
 * Created by LiuJie on 2017/10/30.
 */

public class MyCollectionRefreshEvent {
    private Boolean isAllSelect;
    private int code;

    public MyCollectionRefreshEvent(Boolean isAllSelect, int code) {
        this.isAllSelect = isAllSelect;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Boolean getAllSelect() {
        return isAllSelect;
    }

    public void setAllSelect(Boolean allSelect) {
        isAllSelect = allSelect;
    }
}
