package com.mxkj.yuanyintang.database.cachemusiciansql;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by LiuJie on 2017/12/5.
 */
@DatabaseTable(tableName = "yyt_cache_musician")//声明表名
public class CacheMusicIanDataBean {

    @DatabaseField(generatedId = true)//generatedId   id为主键且自动生成
    private int id;
    @DatabaseField(columnName = "data")//columnName   数据中的列名
    private String data;
    @DatabaseField(columnName = "key")//columnName   数据中的列名
    private String key;
    @DatabaseField(columnName = "uptime")
    private Long uptime;
    @DatabaseField(columnName = "endtime")
    private Long endtime;

    public CacheMusicIanDataBean() {

    }

    public int getId() {
        return id;
    }

    public CacheMusicIanDataBean setId(int id) {
        this.id = id;
        return this;
    }

    public String getData() {
        return data;
    }

    public CacheMusicIanDataBean setData(String data) {
        this.data = data;
        return this;
    }

    public Long getUptime() {
        return uptime;
    }

    public CacheMusicIanDataBean setUptime(Long uptime) {
        this.uptime = uptime;
        return this;
    }

    public Long getEndtime() {
        return endtime;
    }

    public CacheMusicIanDataBean setEndtime(Long endtime) {
        this.endtime = endtime;
        return this;
    }

    public String getKey() {
        return key;
    }

    public CacheMusicIanDataBean setKey(String key) {
        this.key = key;
        return this;
    }
}
