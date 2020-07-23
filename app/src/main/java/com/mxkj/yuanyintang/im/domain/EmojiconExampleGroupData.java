package com.mxkj.yuanyintang.im.domain;

import com.mxkj.yuanyintang.R;

import java.util.Arrays;

public class EmojiconExampleGroupData {

    private static int[] icons = new int[]{
            R.drawable.icon_001_cover,
            R.drawable.icon_002_cover,
            R.drawable.icon_003_cover,
            R.drawable.icon_004_cover,
            R.drawable.icon_005_cover,
            R.drawable.icon_006_cover,
            R.drawable.icon_007_cover,
            R.drawable.icon_008_cover,
            R.drawable.icon_009_cover,
            R.drawable.icon_010_cover,
            R.drawable.icon_011_cover,
            R.drawable.icon_012_cover,
            R.drawable.icon_013_cover,
            R.drawable.icon_014_cover,
            R.drawable.icon_015_cover,
            R.drawable.icon_016_cover,
            R.drawable.icon_017_cover,
            R.drawable.icon_018_cover,
            R.drawable.icon_019_cover,
    };

    private static int[] bigIcons = new int[]{
            R.drawable.icon_001,
            R.drawable.icon_002,
            R.drawable.icon_003,
            R.drawable.icon_004,
            R.drawable.icon_005,
            R.drawable.icon_006,
            R.drawable.icon_007,
            R.drawable.icon_008,
            R.drawable.icon_009,
            R.drawable.icon_010,
            R.drawable.icon_011_cover,
            R.drawable.icon_012_cover,
            R.drawable.icon_013_cover,
            R.drawable.icon_014_cover,
            R.drawable.icon_015_cover,
            R.drawable.icon_016_cover,
            R.drawable.icon_017_cover,
            R.drawable.icon_018_cover,
            R.drawable.icon_019_cover,
    };
    private static String[] bigStr = new String[]{
            "爱你",
            "得意",
            "哭",
            "困",
            "来",
            "没求aa",
            "生气",
            "陶醉",
            "投降",
            "拽",
            "暗中观察",
            "抱大腿",
            "抱我",
            "废人了",
            "打电话",
            "给你心心",
            "乖巧",
            "害怕",
            "呐花花",
    };

    private static final EaseEmojiconGroupEntity DATA = createData();

    private static EaseEmojiconGroupEntity createData() {
        EaseEmojiconGroupEntity emojiconGroupEntity = new EaseEmojiconGroupEntity();
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for (int i = 0; i < icons.length; i++) {
            datas[i] = new EaseEmojicon(icons[i], null, EaseEmojicon.Type.BIG_EXPRESSION);
            datas[i].setBigIcon(bigIcons[i]);
            datas[i].setName(bigStr[i]);
            datas[i].setIdentityCode("em" + (1000 + i + 1));
        }
        emojiconGroupEntity.setEmojiconList(Arrays.asList(datas));
        emojiconGroupEntity.setIcon(icons[0]);
        emojiconGroupEntity.setType(EaseEmojicon.Type.BIG_EXPRESSION);
        return emojiconGroupEntity;
    }


    public static EaseEmojiconGroupEntity getData() {
        return DATA;
    }
}
