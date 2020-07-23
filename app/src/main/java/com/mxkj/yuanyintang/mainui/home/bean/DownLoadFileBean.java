package com.mxkj.yuanyintang.mainui.home.bean;

/**
 * Created by LiuJie on 2017/10/18.
 */

public class DownLoadFileBean {

    public static class DownLoadFileBitBean {
        /**
         * code : 200
         * msg : 成功
         * data : {"sd_sdl":true,"high_sdl":true}
         */

        private int code;
        private String msg;
        private DataBean data;

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

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * sd_sdl : true
             * high_sdl : true
             */

            private boolean sd_sdl;
            private boolean high_sdl;

            public boolean isSd_sdl() {
                return sd_sdl;
            }

            public void setSd_sdl(boolean sd_sdl) {
                this.sd_sdl = sd_sdl;
            }

            public boolean isHigh_sdl() {
                return high_sdl;
            }

            public void setHigh_sdl(boolean high_sdl) {
                this.high_sdl = high_sdl;
            }

        }
    }

    public static class DownLoadKeyBean {

        /**
         * code : 200
         * msg : 成功
         * data : {"filename":"新贵妃.mp3","ext":"mp3","key":"6b7fu_4whejQuYzNakQQ2S3-AiC-OsY5_3_B7vwCQSLGzfl90p7EzgkwSjt546RNnNGVQ28ylvVvHtJZc9T6emuxrFIGxr0mD-UjDMmLnCEq9OoqgTiKrMpInD2UFB7RrtSGsyzkvK53bdwWGuYJ1MgHnrKSwUdfy7kh7GaMM0MlXdnclibQK50MR2ZUMeK7FccciSe0RsiczUnlJL8"}
         */

        private int code;
        private String msg;
        private DataBean data;

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

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * filename : 新贵妃.mp3
             * ext : mp3
             * key : 6b7fu_4whejQuYzNakQQ2S3-AiC-OsY5_3_B7vwCQSLGzfl90p7EzgkwSjt546RNnNGVQ28ylvVvHtJZc9T6emuxrFIGxr0mD-UjDMmLnCEq9OoqgTiKrMpInD2UFB7RrtSGsyzkvK53bdwWGuYJ1MgHnrKSwUdfy7kh7GaMM0MlXdnclibQK50MR2ZUMeK7FccciSe0RsiczUnlJL8
             */

            private String filename;
            private String ext;
            private String key;

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getExt() {
                return ext;
            }

            public void setExt(String ext) {
                this.ext = ext;
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }
        }
    }
}
