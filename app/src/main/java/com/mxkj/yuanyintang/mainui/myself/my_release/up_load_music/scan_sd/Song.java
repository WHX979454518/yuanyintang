package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "yyt_scan_song")//声明表名
public class Song {
    public static final String KEY_SONG_ID = "songid";
    public static final String KEY_ALBUM_ID = "albumid";
    public static final String KEY_ALBUM_NAME = "albumname";
    public static final String KEY_ALBUM_DATA = "albumdata";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_MUSIC_NAME = "musicname";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_ARTIST_ID = "artist_id";
    public static final String KEY_DATA = "data";
    public static final String KEY_FOLDER = "folder";
    public static final String KEY_SIZE = "size";
    public static final String KEY_FAVORITE = "favorite";
    public static final String KEY_LRC = "lrc";
    public static final String KEY_ISLOCAL = "islocal";
    public static final String KEY_SORT = "sort";

    @DatabaseField(generatedId = true)//generatedId   id为主键且自动生成
    private int id;

    @DatabaseField(columnName = "isChecked")
    public int isChecked;
    /**
     * 歌手
     */
    @DatabaseField(columnName = "singer")//columnName   数据中的列名
    private String singer = "";
    /**
     * 歌曲名
     */
    @DatabaseField(columnName = "songName")//columnName   数据中的列名

    private String songName;

    /**
     * 码率
     */
    @DatabaseField(columnName = "bitrate")//columnName   数据中的列名

    private String bitrate;
    /**
     * 歌曲的地址
     */
    @DatabaseField(columnName = "songPath")//columnName   数据中的列名

    private String path;
    /**
     * 歌曲长度
     */
    @DatabaseField(columnName = "songDur")//columnName   数据中的列名

    private long duration;
    /**
     * 歌曲的大小
     */
    @DatabaseField(columnName = "songFileSize")//columnName   数据中的列名

    private long size;

    @DatabaseField(columnName = "songSort")//columnName   数据中的列名
    private String sort;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    @Override
    public String toString() {
        return "Song{" +
                "singer='" + singer + '\'' +
                ", songName='" + songName + '\'' +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                '}';
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//
//    }

//    public static final Creator<Song> CREATOR = new Creator<Song>() {
//        @Override
//        public Song createFromParcel(Parcel source) {
//            Song music = new Song();
//            Bundle bundle = new Bundle();
//            bundle = source.readBundle();
//            music.duration = bundle.getInt(KEY_DURATION);
//            music.songName = bundle.getString(KEY_MUSIC_NAME);
//            music.path = bundle.getString(KEY_FOLDER);
//            music.size = bundle.getInt(KEY_SIZE);
//            music.sort = bundle.getString(KEY_SORT);
//            return music;
//        }
//
//        @Override
//        public Song[] newArray(int size) {
//            return new Song[size];
//        }
//    };


}
