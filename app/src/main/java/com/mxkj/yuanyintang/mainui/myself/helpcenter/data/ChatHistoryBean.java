package com.mxkj.yuanyintang.mainui.myself.helpcenter.data;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zuixia on 2018/4/23.
 */


/**
 * 本来用ormLite做聊天记录保存的，产品说退出app清除聊天记录，所以数据量不会太多，简单点用Sp保存了
 */
@DatabaseTable(tableName = "yxy_chat_history_bean")//声明表名
public class ChatHistoryBean implements MultiItemEntity, Serializable {

    @DatabaseField(generatedId = true)
    private int _id;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @DatabaseField(columnName = "isSelfMsg")
    private int isSelfMsg;

    @DatabaseField(columnName = "type")
    private int type;

    @DatabaseField(columnName = "msgText")
    private String msgText;//text字段为一条文本
    @DatabaseField(columnName = "msgUrl")
    private String msgUrl;//text字段为一个url

    @ForeignCollectionField(eager = true)
    private List<MutiTextBean> list;//text字段为一个数组
// @ForeignCollectionField(eager = true)
//    private Collection<MutiTextBean> list;//text字段为一个数组

    @Override
    public int getItemType() {
        return type;
    }

    @DatabaseTable(tableName = "yxy_chat_muti_text")//声明表名
    public static class MutiTextBean implements Serializable {
        @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
        public ChatHistoryBean chatHistoryBean;

        public ChatHistoryBean getChatHistoryBean() {
            return chatHistoryBean;
        }

        public void setChatHistoryBean(ChatHistoryBean chatHistoryBean) {
            this.chatHistoryBean = chatHistoryBean;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @DatabaseField(columnName = "article")
        private String article;

         @DatabaseField(columnName = "info")
        private String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @DatabaseField(columnName = "name")
        private String name;

        @DatabaseField(columnName = "source")

        private String source;
        @DatabaseField(columnName = "icon")

        private String icon;
        @DatabaseField(columnName = "detailurl")

        private String detailurl;
        @DatabaseField(columnName = "icon_link")

        private String icon_link;
        @DatabaseField(columnName = "share_url")

        private String share_url;
        @DatabaseField(columnName = "id")

        private int id;
        @DatabaseField(columnName = "pid")

        private int pid;
        @DatabaseField(columnName = "title")

        private String title;
        @DatabaseField(columnName = "type")

        private int type;
        @DatabaseField(columnName = "description")

        private String description;



        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
        }

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getDetailurl() {
            return detailurl;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }

        public String getIcon_link() {
            return icon_link;
        }

        public void setIcon_link(String icon_link) {
            this.icon_link = icon_link;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public int getIsSelfMsg() {
        return isSelfMsg;
    }

    public void setIsSelfMsg(int isSelfMsg) {
        this.isSelfMsg = isSelfMsg;
    }


    public int getMsgType() {
        return type;
    }

    public void setMsgType(int msgType) {
        this.type = msgType;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getMsgUrl() {
        return msgUrl;
    }

    public void setMsgUrl(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public List<MutiTextBean> getList() {
        return list;
    }

    public void setList(List<MutiTextBean> list) {
        this.list = list;
    }


}
