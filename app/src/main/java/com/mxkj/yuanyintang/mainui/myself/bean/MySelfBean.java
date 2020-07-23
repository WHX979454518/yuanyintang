package com.mxkj.yuanyintang.mainui.myself.bean;

/**
 * Created by LiuJie on 2017/9/30.
 */

public class MySelfBean {

    private int drawable;
    private String name;
    private Boolean check = false;

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MySelfBean(int drawable, String name) {
        this.drawable = drawable;
        this.name = name;
    }

    public MySelfBean() {

    }

}
