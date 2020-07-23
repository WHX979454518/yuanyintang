package com.mxkj.yuanyintang.mainui.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.mainui.home.data.Constant;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseMultiItemQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.home.utils.GridSpacingItemDecoration;
import com.mxkj.yuanyintang.mainui.newapp.pond.PondAdapter;
import com.mxkj.yuanyintang.mainui.search.SearchResultActivity;
import com.mxkj.yuanyintang.utils.photopicker.widget.NoScrollRecyclerView;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.widget.ExpandableTextView;
import com.mxkj.yuanyintang.widget.MultiLineRadioGroup;

import java.util.ArrayList;
import java.util.List;

import static com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.SEARCH_WORDS;
import static com.mxkj.yuanyintang.mainui.search.SearchResultActivity.TO_SHEET_RESULT;

public class SongSheetDetailsAdapter extends BaseMultiItemQuickAdapter<MusicIndex.ItemInfoListBean, BaseViewHolder> implements BaseQuickAdapter.SpanSizeLookup, BaseQuickAdapter.OnItemChildClickListener {
    private boolean isEdit;
    private int maxHasLoadPosition;
    public NoScrollRecyclerView song_recyclerview;
    private Context context;
    private String songName;
    private FragmentManager fragmentManager;
    MusicDetailsSongAdapter musicDetailsSongAdapter;
    public SongSheetMusicListAdapter songSheetMusicAdapter;
    SongSheetRecommendListAdapter songSheetRecommendListAdapter;
    MultiLineRadioGroup multiline_group;
    private SongSheetMusicListAdapter.ClickCheckedSongListener checkedSongListener;

    public MultiLineRadioGroup getMultilineGroup() {
        return multiline_group;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public SongSheetDetailsAdapter(Context context, FragmentManager fragmentManager, boolean isEdit) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.isEdit = isEdit;
        initView();
    }


    public boolean isEdit() {
        return isEdit;
    }

    public void setEditAndNotify(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    private void initView() {
        setSpanSizeLookup(this);
//        addItemType(Constant.MUSIC_TYPE_LISTENING, R.layout.musicrecycle_item_listening);
//        addItemType(Constant.MUSIC_TYPE_GIFT_LIST, R.layout.musicrecycle_item_gift_list);
//        addItemType(Constant.MUSIC_TYPE_RELATED_PARTNER, R.layout.musicrecycle_item_partner_list);
//        addItemType(Constant.MUSIC_TYPE_SYNOPSIS, R.layout.musicrecycle_item_synopsis);
        //        addItemType(Constant.MUSIC_TYPE_MUSIC, R.layout.musicrecycle_item_music);//歌单详情歌单
//        addItemType(Constant.MUSIC_TYPE_RECOMMEND_SONG, R.layout.musicrecycle_item_recommend_song);


        addItemType(Constant.MUSIC_TYPE_SONG, R.layout.musicrecycle_item_song);//相关歌单
        addItemType(Constant.MUSIC_TYPE_POND, R.layout.musicrecycle_item_pond);
        addItemType(Constant.MUSIC_TYPE_RECOMMEND, R.layout.musicrecycle_item_recommend);//猜你喜欢
    }

    @Override
    public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
        return 1;
    }

    @Override
    protected void convert(BaseViewHolder helper, MusicIndex.ItemInfoListBean item, int position) {
        if ("music".equals(item.itemType)) {
//            bindMusicData(helper, item, position);
        } else if ("recommend".equals(item.itemType)) {
//            bindRecommendData(helper, item, position);
        } else if ("pond" == item.itemType) {
//            bindPondData(helper, item, position);
        }
    }
    @Override
    public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        return false;
    }

    public SongSheetMusicListAdapter getMusiListAdapter() {
        if (null != songSheetMusicAdapter) {
            return songSheetMusicAdapter;
        }
        return null;
    }

    public void notifyData() {
        if (null != musicDetailsSongAdapter) {
            musicDetailsSongAdapter.notifyDataSetChanged();
        }
        if (null != songSheetMusicAdapter) {
            songSheetMusicAdapter.notifyDataSetChanged();
        }
        if (null != songSheetRecommendListAdapter) {
            songSheetRecommendListAdapter.notifyDataSetChanged();
        }
    }

    public void setCheckedSongListener(SongSheetMusicListAdapter.ClickCheckedSongListener checkedSongListener) {
        this.checkedSongListener = checkedSongListener;
    }
}
