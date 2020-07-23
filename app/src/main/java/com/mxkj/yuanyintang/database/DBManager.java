package com.mxkj.yuanyintang.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.widget.emoji.FaceBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.mxkj.yuanyintang.musicplayer.service.MediaService.playList;
/**
 * created by LiuJie -2017/9/23
 */
public class DBManager {
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        try {
            helper = new DatabaseHelper(context);
            db = helper.getWritableDatabase();
            db.setLocale(Locale.CHINA);
        } catch (RuntimeException e) {
            Log.e("TAG", "DBManagerException-----: " + e);
        }
    }

    /**
     * 添加音乐到当前列表
     */
    public void addToCurrentList(@NonNull final MusicInfo.DataBean listBean) {
        if (db == null) return;
        try {
            db.beginTransaction();
            db.delete(DatabaseHelper.CURRENT_PLAY_LIST, "MUSIC_ID = ?",
                    new String[]{String.valueOf(listBean.getId())});
            db.execSQL("INSERT INTO " + DatabaseHelper.CURRENT_PLAY_LIST
                            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                    new Object[]{
                            listBean.getId(),
                            listBean.getTitle(),
                            listBean.getVideo(),
                            listBean.getCounts(),
                            listBean.getShares(),
                            listBean.getCollection(),
                            listBean.getDownloads(),
                            listBean.getUp_time(),
                            listBean.getNickname(),
                            listBean.getCatename(),
                            listBean.getStatus(),
                            listBean.getCategory(),
                            listBean.getComment(),
                            listBean.getCollects(),
                            listBean.getTag(),
                            listBean.getUid(),
                            listBean.getCreate_time(),
                            listBean.getImgpic(),
                            listBean.getLyrics(),
                            listBean.getIntro(),
                            listBean.getTag_xs(),
                            listBean.getTag_yz(),
                            listBean.getTag_fg(),
                            listBean.getTag_zt(),
                            listBean.getTag_sx(),
                            listBean.getVideo_info() == null ? "" : listBean.getVideo_info().getLink(),
                            listBean.getImgpic_info() == null ? "" : listBean.getImgpic_info().getLink(),
                            listBean.getIsdown(),
                            listBean.getSong_title(),
                            listBean.getSong_id()
                    });
            db.setTransactionSuccessful();
            List<MusicInfo.DataBean> query = query(1);
            if (null != playList) {
                playList.clear();
                playList.addAll(query);
            }
        } catch (RuntimeException exception) {
            deleteAllInfo(1);
        } finally {
            if (db != null&&db.inTransaction()) {
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 添加搜索历史
     */
    public void addSearchHistory(String name) {
        if (db == null) return;

        db.beginTransaction();
        try {

            db.delete(DatabaseHelper.SEARCH_HISTORY, " songname= ?",
                    new String[]{name});

            db.execSQL("INSERT INTO " + DatabaseHelper.SEARCH_HISTORY
                    + " VALUES(null, ?)", new Object[]{name});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 添加json缓存
     */
    public void setJsonCache(String params, String jsonStr) {
        if (db == null) return;
        db.beginTransaction();
        try {
            db.delete(DatabaseHelper.JSON_CACHE, " params= ?",
                    new String[]{params});
            db.execSQL("INSERT INTO " + DatabaseHelper.JSON_CACHE
                    + " VALUES(?,?,?)", new Object[]{params, jsonStr});
            db.setTransactionSuccessful();
        } catch (RuntimeException e) {
            Log.e("TAG", "setJsonCache: " + e);
        } finally {
            db.endTransaction();
        }
    }


    public String getJsonCache(String params) {
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT * FROM " + DatabaseHelper.JSON_CACHE + " where params='" + params + "'", null);
            if (c.moveToFirst()) {
                String jsonStr = c.getString(c.getColumnIndex("jsonStr"));
                return jsonStr;
            }
        } catch (Exception e) {

        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    /**
     * 存储表情数据
     */
    public void saveEmoji(List<FaceBean.DataBean> faceBeanList) {
        if (db == null) return;
        for (int i = 0; i < faceBeanList.size(); i++) {
            db.beginTransaction();
            try {
                db.delete(DatabaseHelper.EMOJI, " id= ?",
                        new String[]{String.valueOf(faceBeanList.get(i).getId())});

                db.execSQL("INSERT INTO " + DatabaseHelper.EMOJI
                        + " VALUES(?,?,?)", new Object[]{String.valueOf(faceBeanList.get(i).getId()), faceBeanList.get(i).getEmoji(), faceBeanList.get(i).getImgpic_link()});
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }

    @NonNull
    public List<FaceBean.DataBean> queryEmoji() {
        Cursor c = null;
        try {
            ArrayList<FaceBean.DataBean> faceLists = new ArrayList<>();
            c = queryTheCursor(3);
            while (c.moveToNext()) {
                FaceBean.DataBean face = new FaceBean.DataBean();
                face.setId(c.getInt(c.getColumnIndex("id")));
                face.setEmoji(c.getString(c.getColumnIndex("emoji")));
                String imgpic_link = c.getString(c.getColumnIndex("imgpic_link"));
//            FaceBean.DataBean.ImgpicInfoBean imgpicInfoBean = new FaceBean.DataBean.ImgpicInfoBean();
//            imgpicInfoBean.setLink(imgpic_link);
                face.setImgpic_link(imgpic_link);
                faceLists.add(face);
            }
            return faceLists;
        } catch (Exception e) {

        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    public void deleteMusic(@NonNull MusicInfo.DataBean listBean) {
        if (db == null) return;

        db.delete(DatabaseHelper.CURRENT_PLAY_LIST, "MUSIC_ID = ?",
                new String[]{String.valueOf(listBean.getId())});
    }

    /**
     * 删除某条搜索历史
     */
    public void delSrchHistrory(String name) {
        if (db == null) return;

        db.delete(DatabaseHelper.SEARCH_HISTORY, "songname = ?", new String[]{name});
    }

    @NonNull
    public List<MusicInfo.DataBean> query(int type) {
        Cursor c = null;
        try {
            /**
             * 这里只查询常用的字段，其他字段视情况添加
             * */
            ArrayList<MusicInfo.DataBean> playLists = new ArrayList<>();
            c = queryTheCursor(type);
            while (c.moveToNext()) {
                MusicInfo.DataBean listBean = new MusicInfo.DataBean();
                listBean.setId(c.getInt(c.getColumnIndex("MUSIC_ID")));
                listBean.setTitle(c.getString(c.getColumnIndex("TITLE")));
                listBean.setNickname(c.getString(c.getColumnIndex("NICKNAME")));
                listBean.setCollection(c.getInt(c.getColumnIndex("COLLECTION")));
                listBean.setComment(c.getInt(c.getColumnIndex("COMMENT")));
                listBean.setUid(c.getInt(c.getColumnIndex("UID")));
                listBean.setLyrics(c.getString(c.getColumnIndex("LYRICS")));
                MusicInfo.DataBean.VideoInfoBean videoInfoBean = new MusicInfo.DataBean.VideoInfoBean();
                videoInfoBean.setLink(c.getString(c.getColumnIndex("VIDEO_LINK")));
                listBean.setVideo_info(videoInfoBean);
                String imgpic_link = c.getString(c.getColumnIndex("IMGPIC_LINK"));
                MusicInfo.DataBean.ImgpicInfoBean imgpicInfoBean = new MusicInfo.DataBean.ImgpicInfoBean();
                imgpicInfoBean.setLink(imgpic_link);
                listBean.setImgpic_info(imgpicInfoBean);
                playLists.add(listBean);
            }
            return playLists;
        } catch (Exception e) {

        } finally {
            if (c != null) {
                c.close();
            }

        }
        return new ArrayList();
    }

    @NonNull
    public List<String> querySearchhistory(int type) {
        Cursor c = null;
        try {
            c = queryTheCursor(2);
            List<String> songs = new ArrayList<>();
            while (c.moveToNext()) {
                songs.add(c.getString(c.getColumnIndex("songname")));
            }
            return songs;
        } catch (Exception e) {

        } finally {
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    /**
     * 更新歌曲收藏信息
     */
    @NonNull
    public void upDateMusicInfo(MusicInfo.DataBean dataBean) {
        if (db == null) return;
        db.execSQL("UPDATE " + DatabaseHelper.CURRENT_PLAY_LIST + " SET COLLECTION =" + dataBean.getCollection() + " WHERE MUSIC_ID=" + dataBean.getId());
    }


    @Nullable
    public Cursor queryTheCursor(int type) {//1为列表，2为搜索历史,
        Cursor c = null;
        if (type == 1) {
            c = db.rawQuery("SELECT * FROM " + DatabaseHelper.CURRENT_PLAY_LIST,
                    null);
        } else if (type == 2) {
            c = db.rawQuery("SELECT * FROM " + DatabaseHelper.SEARCH_HISTORY,
                    null);
        } else if (type == 3) {
            c = db.rawQuery("SELECT * FROM " + DatabaseHelper.EMOJI,
                    null);
        }
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }

    public void deleteAllInfo(int type) {
        if (type == 0) {//播放列表
            Log.e("DBM", "deleteAllInfo: ");
            db.execSQL("delete  from " + DatabaseHelper.CURRENT_PLAY_LIST);
        } else if (type == 1) {//搜索历史
            db.execSQL("delete from " + DatabaseHelper.SEARCH_HISTORY);
        } else {//表情
            db.execSQL("delete from " + DatabaseHelper.EMOJI);
        }
    }
}