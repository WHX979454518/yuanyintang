package com.mxkj.yuanyintang.musicplayer.playcache.sourcestorage;


import com.mxkj.yuanyintang.musicplayer.playcache.SourceInfo;

/**
 * Storage for {@link SourceInfo}.
 *
 * @author Alexey Danilov (danikula@gmail.com).
 */
public interface SourceInfoStorage {

    SourceInfo get(String url);

    void put(String url, SourceInfo sourceInfo);

    void release();
}
