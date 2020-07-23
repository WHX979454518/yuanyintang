package com.mxkj.yuanyintang.mainui.home.bean;

/**
 * Created by LiuJie on 2017/9/22.
 */

public class MusicListTabBean {

    /**
     * id : 89
     * title : 洛天依
     */

    private int id;
    private String title;
    private boolean check;


    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
