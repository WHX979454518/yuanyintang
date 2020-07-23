package com.mxkj.yuanyintang.mainui.myself.bean;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiuJie on 2017/10/17.
 */

public class CityBean implements IPickerViewData {











    /**
     * code : 200
     * msg : success
     * data : [{"id":2,"name":"天津市","pid":0,"code":"120000","type":1,"on_line":0,"lat":"39.14393","lng":"117.210813","pingyin":"Tianjin Shi","_child":[{"id":34,"name":"北京市","pid":1,"code":"110100","type":2,"on_line":0,"lat":"39.929986","lng":"116.395645","pingyin":"","_child":[{"id":368,"name":"山南地区","pid":33,"code":"542200","type":2,"on_line":0,"lat":"29.229027","lng":"91.750644","pingyin":"Shannan Diqu","_child":[{"id":3761,"name":"花地玛堂","pid":367,"code":"","type":3,"on_line":1,"lat":"","lng":"","pingyin":""}]}]}]}]
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

    @Override
    public String getPickerViewText() {
        return null;
    }



    public static class DataBean {
        /**
         * id : 2
         * name : 天津市
         * pid : 0
         * code : 120000
         * type : 1
         * on_line : 0
         * lat : 39.14393
         * lng : 117.210813
         * pingyin : Tianjin Shi
         * _child : [{"id":34,"name":"北京市","pid":1,"code":"110100","type":2,"on_line":0,"lat":"39.929986","lng":"116.395645","pingyin":"","_child":[{"id":368,"name":"山南地区","pid":33,"code":"542200","type":2,"on_line":0,"lat":"29.229027","lng":"91.750644","pingyin":"Shannan Diqu","_child":[{"id":3761,"name":"花地玛堂","pid":367,"code":"","type":3,"on_line":1,"lat":"","lng":"","pingyin":""}]}]}]
         */

        private int id;
        private String name;
        private int pid;
        private String code;
        private int type;
        private int on_line;
        private String lat;
        private String lng;
        private String pingyin;
        private List<ChildBeanXX> _child;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getOn_line() {
            return on_line;
        }

        public void setOn_line(int on_line) {
            this.on_line = on_line;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getPingyin() {
            return pingyin;
        }

        public void setPingyin(String pingyin) {
            this.pingyin = pingyin;
        }

        public List<ChildBeanXX> get_child() {
            return _child;
        }

        public void set_child(List<ChildBeanXX> _child) {
            this._child = _child;
        }

        public static class ChildBeanXX {
            /**
             * id : 34
             * name : 北京市
             * pid : 1
             * code : 110100
             * type : 2
             * on_line : 0
             * lat : 39.929986
             * lng : 116.395645
             * pingyin :
             * _child : [{"id":368,"name":"山南地区","pid":33,"code":"542200","type":2,"on_line":0,"lat":"29.229027","lng":"91.750644","pingyin":"Shannan Diqu","_child":[{"id":3761,"name":"花地玛堂","pid":367,"code":"","type":3,"on_line":1,"lat":"","lng":"","pingyin":""}]}]
             */

            private int id;
            private String name;
            private int pid;
            private String code;
            private int type;
            private int on_line;
            private String lat;
            private String lng;
            private String pingyin;
            private List<ChildBeanX> _child;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getPid() {
                return pid;
            }

            public void setPid(int pid) {
                this.pid = pid;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getOn_line() {
                return on_line;
            }

            public void setOn_line(int on_line) {
                this.on_line = on_line;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }

            public String getPingyin() {
                return pingyin;
            }

            public void setPingyin(String pingyin) {
                this.pingyin = pingyin;
            }

            public List<ChildBeanX> get_child() {
                return _child;
            }

            public void set_child(List<ChildBeanX> _child) {
                this._child = _child;
            }

            public static class ChildBeanX {
                /**
                 * id : 368
                 * name : 山南地区
                 * pid : 33
                 * code : 542200
                 * type : 2
                 * on_line : 0
                 * lat : 29.229027
                 * lng : 91.750644
                 * pingyin : Shannan Diqu
                 * _child : [{"id":3761,"name":"花地玛堂","pid":367,"code":"","type":3,"on_line":1,"lat":"","lng":"","pingyin":""}]
                 */

                private int id;
                private String name;
                private int pid;
                private String code;
                private int type;
                private int on_line;
                private String lat;
                private String lng;
                private String pingyin;
                private List<ChildBean> _child;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getPid() {
                    return pid;
                }

                public void setPid(int pid) {
                    this.pid = pid;
                }

                public String getCode() {
                    return code;
                }

                public void setCode(String code) {
                    this.code = code;
                }

                public int getType() {
                    return type;
                }

                public void setType(int type) {
                    this.type = type;
                }

                public int getOn_line() {
                    return on_line;
                }

                public void setOn_line(int on_line) {
                    this.on_line = on_line;
                }

                public String getLat() {
                    return lat;
                }

                public void setLat(String lat) {
                    this.lat = lat;
                }

                public String getLng() {
                    return lng;
                }

                public void setLng(String lng) {
                    this.lng = lng;
                }

                public String getPingyin() {
                    return pingyin;
                }

                public void setPingyin(String pingyin) {
                    this.pingyin = pingyin;
                }

                public List<ChildBean> get_child() {
                    return _child;
                }

                public void set_child(List<ChildBean> _child) {
                    this._child = _child;
                }

                public static class ChildBean {
                    /**
                     * id : 3761
                     * name : 花地玛堂
                     * pid : 367
                     * code :
                     * type : 3
                     * on_line : 1
                     * lat :
                     * lng :
                     * pingyin :
                     */

                    private int id;
                    private String name;
                    private int pid;
                    private String code;
                    private int type;
                    private int on_line;
                    private String lat;
                    private String lng;
                    private String pingyin;

                    public int getId() {
                        return id;
                    }

                    public void setId(int id) {
                        this.id = id;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public int getPid() {
                        return pid;
                    }

                    public void setPid(int pid) {
                        this.pid = pid;
                    }

                    public String getCode() {
                        return code;
                    }

                    public void setCode(String code) {
                        this.code = code;
                    }

                    public int getType() {
                        return type;
                    }

                    public void setType(int type) {
                        this.type = type;
                    }

                    public int getOn_line() {
                        return on_line;
                    }

                    public void setOn_line(int on_line) {
                        this.on_line = on_line;
                    }

                    public String getLat() {
                        return lat;
                    }

                    public void setLat(String lat) {
                        this.lat = lat;
                    }

                    public String getLng() {
                        return lng;
                    }

                    public void setLng(String lng) {
                        this.lng = lng;
                    }

                    public String getPingyin() {
                        return pingyin;
                    }

                    public void setPingyin(String pingyin) {
                        this.pingyin = pingyin;
                    }
                }
            }

        }
    }

}
