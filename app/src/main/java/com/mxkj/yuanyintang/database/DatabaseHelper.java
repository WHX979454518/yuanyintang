package com.mxkj.yuanyintang.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String CURRENT_PLAY_LIST = "songlists";
    public static final String SEARCH_HISTORY = "searchHistory";
    public static final String JSON_CACHE = "jsoncache";
    public static final String EMOJI = "emoji";

    public DatabaseHelper(Context context) {
        super(context, CURRENT_PLAY_LIST, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        String execPlayList = "CREATE TABLE " + CURRENT_PLAY_LIST + "(" +
                "MUSIC_ID INTEGER," +
                "TITLE TEXT," +
                "VIDEO TEXT," +
                "COUNTS INTEGER," +
                "SHARES INTEGER," +
                "COLLECTION INTEGER," +
                "DOWNLOADS INTEGER," +
                "UP_TIME INTEGER," +
                "NICKNAME TEXT," +
                "CATENAME TEXT," +
                "STATUS INTEGER," +
                "CATEGORY INTEGER," +
                "COMMENT INTEGER," +
                "COLLECTS INTEGER," +
                "TAG TEXT," +
                "UID INTEGER," +
                "CREATE_TIME INTEGER," +
                "IMGPIC TEXT," +
                "LYRICS TEXT," +
                "INTRO TEXT," +
                "TAG_XS INTEGER," +
                "TAG_YZ INTEGER," +
                "TAG_FG INTEGER," +
                "TAG_ZT INTEGER," +
                "TAG_SX INTEGER," +
                "VIDEO_LINK TEXT," +
                "IMGPIC_LINK TEXT," +
                "ISDOWN INTEGER," +
                "SONGNAME TEXT," +
                "SONG_ID INTEGER" +
                ")";

        String execHistory = "CREATE TABLE " + SEARCH_HISTORY + "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,songname TEXT)";
        String jsonCache = "CREATE TABLE " + JSON_CACHE + "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,params TEXT,jsonStr TEXT)";
        String emoji = "CREATE TABLE " + EMOJI + "(id INTEGER,emoji TEXT,imgpic_link TEXT)";
        db.execSQL(execPlayList);
        db.execSQL(execHistory);
        db.execSQL(emoji);
        db.execSQL(jsonCache);

    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        // 调用时间：如果DATABASE_VERSION值被改为别的数,系统发现现有数据库版本不同,即会调用onUpgrade
        db.execSQL("DROP TABLE IF EXISTS " + CURRENT_PLAY_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + SEARCH_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + EMOJI);
        db.execSQL("DROP TABLE IF EXISTS " + JSON_CACHE);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

}