package com.mxkj.yuanyintang.extraui.dialog;

import java.io.Serializable;

public class ActivityBean implements Serializable {
    private String title;
    private String topicContent;
    private String sinaDescription;
    private String activityAlias;
    private String activityType;
    private String imgpic;
    private String url;
    private String nickname;
    private String sub_title;

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", topicContent='" + topicContent + '\'' +
                ", sinaDescription='" + sinaDescription + '\'' +
                ", activityAlias='" + activityAlias + '\'' +
                ", activityType='" + activityType + '\'' +
                ", imgpic='" + imgpic + '\'' +
                ", url='" + url + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sub_title='" + sub_title + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    public String getSinaDescription() {
        return sinaDescription;
    }

    public void setSinaDescription(String sinaDescription) {
        this.sinaDescription = sinaDescription;
    }

    public String getActivityAlias() {
        return activityAlias;
    }

    public void setActivityAlias(String activityAlias) {
        this.activityAlias = activityAlias;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getImgpic() {
        return imgpic;
    }

    public void setImgpic(String imgpic) {
        this.imgpic = imgpic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }
}
