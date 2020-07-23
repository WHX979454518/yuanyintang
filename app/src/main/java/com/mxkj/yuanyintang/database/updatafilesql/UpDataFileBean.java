package com.mxkj.yuanyintang.database.updatafilesql;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by LiuJie on 2017/10/11.
 */
@DatabaseTable(tableName = "yyt_updatafile")//声明表名
public class UpDataFileBean {
 @DatabaseField(generatedId = true)//generatedId   id为主键且自动生成
 private int id;
 //columnName   数据中的列名
 @DatabaseField(columnName = "contents")
 private String contents;
 @DatabaseField(columnName = "up_time")
 private int up_time;
 @DatabaseField(columnName = "path")
 private String path;
 @DatabaseField(columnName = "url")
 private String url;
 @DatabaseField(columnName = "musicName")
 private String music_name;
 @DatabaseField(columnName = "nickname")
 private String nickname;
 @DatabaseField(columnName = "img_url")
 private String img_url;
 @DatabaseField(columnName = "tastId")
 private int tastId;
 @DatabaseField(columnName = "musicId")
 private String music_id;
 @DatabaseField(columnName = "ext")
 private String ext;
 @DatabaseField(columnName = "fileName")
 private String fileName;
 @DatabaseField(columnName = "uid")
 private int uid;
 @DatabaseField(columnName = "song_id")
 private int song_id;
 @DatabaseField(columnName = "songName")
 private String songName;
 @DatabaseField(columnName = "collection")
 private int collection;
 @DatabaseField(columnName = "playTime")
 private String playTime;
 @DatabaseField(columnName = "success")
 private Boolean success;

 public Boolean getSuccess() {
  if (null == success) {
   return false;
  }
  return success;
 }

 public void setSuccess(Boolean success) {
  this.success = success;
 }

 public UpDataFileBean() {

 }

 public String getSongName() {
  return songName;
 }

 public void setSongName(String songName) {
  this.songName = songName;
 }

 public String getPlayTime() {
  return playTime;
 }

 public void setPlayTime(String playTime) {
  this.playTime = playTime;
 }

 public String getFileName() {
  return fileName;
 }

 public void setFileName(String fileName) {
  this.fileName = fileName;
 }

 public String getMusic_id() {
  return music_id;
 }

 public void setMusic_id(String music_id) {
  this.music_id = music_id;
 }

 public UpDataFileBean(String contents, int up_time) {
  this.contents = contents;
  this.up_time = up_time;
 }
 public UpDataFileBean(int id, String music_name) {
  this.id = id;
  this.music_name = music_name;
 }
 public int getTastId() {
  return tastId;
 }

 public void setTastId(int tastId) {
  this.tastId = tastId;
 }

 public int getId() {
  return id;
 }

 public void setId(int id) {
  this.id = id;
 }

 public String getContents() {
  return contents;
 }

 public void setContents(String contents) {
  this.contents = contents;
 }

 public int getUpTime() {
  return up_time;
 }

 public void setUpTime(int up_time) {
  this.up_time = up_time;
 }

 public String getPath() {
  return path;
 }

 public void setPath(String path) {
  this.path = path;
 }

 public String getUrl() {
  return url;
 }

 public void setUrl(String url) {
  this.url = url;
 }

 public String getMusic_name() {
  return music_name;
 }

 public void setMusic_name(String music_name) {
  this.music_name = music_name;
 }

 public String getNickname() {
  return nickname;
 }

 public void setNickname(String nickname) {
  this.nickname = nickname;
 }

 public String getImg_url() {
  return img_url;
 }

 public void setImg_url(String img_url) {
  this.img_url = img_url;
 }

 public int getUid() {
  return uid;
 }

 public void setUid(int uid) {
  this.uid = uid;
 }

 public int getSong_id() {
  return song_id;
 }

 public void setSong_id(int song_id) {
  this.song_id = song_id;
 }

 public int getCollection() {
  return collection;
 }

 public void setCollection(int collection) {
  this.collection = collection;
 }

 public String getExt() {
  return ext;
 }

 public void setExt(String ext) {
  this.ext = ext;
 }
}