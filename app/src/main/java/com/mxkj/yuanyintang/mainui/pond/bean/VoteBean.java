package com.mxkj.yuanyintang.mainui.pond.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/11.
 */

public class VoteBean implements Serializable{
    private String question_name;
    private int votetype;
    private int hide;
    private List<VoteItem> itemList;


    @Override
    public String toString() {
        return "VoteBean{" +
                "question_name='" + question_name + '\'' +
                ", votetype=" + votetype +
                ", hide=" + hide +
                ", itemList=" + itemList +
                '}';
    }

    public String getQuestion_name() {
        return question_name;
    }

    public void setQuestion_name(String question_name) {
        this.question_name = question_name;
    }

    public int getVotetype() {
        return votetype;
    }

    public void setVotetype(int votetype) {
        this.votetype = votetype;
    }

    public int getHide() {
        return hide;
    }

    public void setHide(int hide) {
        this.hide = hide;
    }

    public List<VoteItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<VoteItem> itemList) {
        this.itemList = itemList;
    }

    public static class VoteItem implements Serializable{
        private int id;
        private String optionname;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOptionname() {
            return optionname;
        }

        public void setOptionname(String optionname) {
            this.optionname = optionname;
        }


        @Override
        public String toString() {
            return "VoteItem{" +
                    "id=" + id +
                    ", optionname='" + optionname + '\'' +
                    '}';
        }
    }

}
