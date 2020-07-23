package com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.scan_sd;

import java.util.Comparator;

public class MusicComparator implements Comparator<Song> {

    @Override
    public int compare(Song m1, Song m2) {
        String py1 = m1.getSort();
        String py2 = m2.getSort();
        // 判断是否为空""
        if (isEmpty(py1) && isEmpty(py2))
            return 0;
        if (isEmpty(py1))
            return -1;
        if (isEmpty(py2))
            return 1;
        return py1.compareTo(py2);
    }

    private boolean isEmpty(String str) {
        if (str!=null) {
            return "".equals(str.trim());
        }
        return false;
    }
}