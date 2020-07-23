package com.mxkj.yuanyintang.musicplayer.lyric_remusic;

import java.util.List;

/**
 * 歌词解析器
 */
public interface ILrcParser {

    List<LrcRow> getLrcRows(String str);
}
