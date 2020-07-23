package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd.pinyin.Pinyin;

import java.io.File;
import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;

public class MusicLoader {
    private boolean isScan = true;
    private int totalFilesNum;//文件总数
    private int scanedFileNum;//已扫描文件数
    private boolean isScanShortSong;

    public boolean isScan() {
        return isScan;
    }

    ArrayList<Song> songArrayList = new ArrayList<>();


    public void setScan(boolean scan) {
        isScan = scan;
    }

    public static MusicLoader newInstance() {
        return new MusicLoader();
    }

    //刷新列表
    @AfterPermissionGranted(1)
    public ArrayList<Song> refreshFileList(boolean cancelled, Context context, String strPath, boolean isScanShortSong, ScanProgressCallback callback) {
        this.isScanShortSong = isScanShortSong;
        String filename;//文件名
        String suf;//文件后缀
        File dir = new File(strPath);//文件夹dir
        File[] files = dir.listFiles();//文件夹下的所有文件或文件夹
        if (files == null) {
            return songArrayList;
        }
        for (int i = 0; i < files.length; i++) {
            if (cancelled) {
                break;
            }
            scanedFileNum++;

            if (files[i].isDirectory()) {
                totalFilesNum += files.length;
                refreshFileList(cancelled, context, files[i].getAbsolutePath(), isScanShortSong, callback);//递归文件夹！！！
            } else {
                String path = files[i].getAbsolutePath().toLowerCase();
//                if (callback != null) {
//                    callback.setPro(path, songArrayList);
//                }
                filename = files[i].getName();
                int j = filename.lastIndexOf(".");
                suf = filename.substring(j + 1);//得到文件后缀
                if (suf.equalsIgnoreCase("mp3")) {
                    Song songInfo = getSongInfo(files[i]);
                    if (songInfo != null) {
                        SongDao songDao = new SongDao(context);
                        if (isScanShortSong == false) {
                            if (songInfo.getDuration() > 60) {
                                songDao.add(songInfo);
                                songArrayList.add(songInfo);
                            }
                        } else {
                            songDao.add(songInfo);
                            songArrayList.add(songInfo);
                        }
                    }
                }
            }
        }
        return songArrayList;
    }


    public static Song getSongInfo(File file) {
        if (file == null) {
            return null;
        }
        String absolutePath = file.getAbsolutePath();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(absolutePath);
        } catch (RuntimeException e) {
            return null;
        }

        String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        String mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
        String artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        long duration = getRingDuring(file);
        String bitrate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE); // bit/s api >= 14
        String date = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);

        Song song = new Song();
        song.setDuration(duration);
        song.setPath(absolutePath);
        song.setSinger(artist);
        song.setSize(file.length());
        song.setSongName(file.getName());
        song.setBitrate(bitrate);

        song.setSort(Pinyin.toPinyin(file.getName().charAt(0)).substring(0, 1).toUpperCase());
        return song;
    }

    /*
     * 获取时长
     * */
    private static long getRingDuring(File file) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        try {
            mmr.setDataSource(file.getAbsolutePath());
            long fileSize = file.length();
            long bitRate = Long.parseLong(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
            long duration = (fileSize * 8) / (bitRate);//单位，秒
            return duration;
        } catch (Exception e) {
            return 70;
        }
    }


    public interface ScanProgressCallback {
        void setPro(String path, ArrayList<Song> size);
    }


}
