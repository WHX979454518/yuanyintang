package com.mxkj.yuanyintang.mainui.home.bean;

import java.util.List;

/**
 * Created by LiuJie on 2017/10/13.
 */

public class UpDataBean {

    /**
     * code : 200
     * msg : 有新版升级！
     * data : [{"id":7,"version":"1.0.1","create_time":1500878445,"type":1,"remark":"更新","is_updata":1,"url":"www.baidu.com","file_video_link":""}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 7
         * version : 1.0.1
         * create_time : 1500878445
         * type : 1
         * remark : 更新
         * is_updata : 1
         * url : www.baidu.com
         * file_video_link :
         */

        private int id;
        private String version;
        private int create_time;
        private int type;
        private String remark;
        private int is_updata;
        private String url;
        private String file_video_link;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getIs_updata() {
            return is_updata;
        }

        public void setIs_updata(int is_updata) {
            this.is_updata = is_updata;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFile_video_link() {
            return file_video_link;
        }

        public void setFile_video_link(String file_video_link) {
            this.file_video_link = file_video_link;
        }
    }
}
