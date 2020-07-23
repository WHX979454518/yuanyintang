package com.mxkj.yuanyintang.musicplayer.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.database.DBManager;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.musicplayer.adapter.MusicListAdapter;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayList;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.utils.app.CommonUtils;

import java.text.ParseException;

import static com.mxkj.yuanyintang.musicplayer.service.MediaService.bean;
import static com.mxkj.yuanyintang.musicplayer.service.MediaService.playList;

public class PopPlayList extends DialogFragment implements View.OnClickListener {
    @Nullable
    Window window;
    View mFilterView;
    @NonNull
    private ImageView hide;
    private TextView tv_total_num;
    private TextView clearList;
    private SwipeMenuListView lv_playlist;
    private MusicListAdapter adapter;
    //private CommonDialog commonDialog;
    private int position_curr_song;
    private TextView tv_song_totalTime;
    int totalTime = 0;

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, R.anim.slide_in_bottom);
    }

    @SuppressLint("RtlHardcoded")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //去出标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //放置位置
        window = getDialog().getWindow();
        assert window != null;
        window.setGravity(Gravity.BOTTOM | Gravity.RIGHT | Gravity.LEFT);
        //设置布局
        mFilterView = inflater.inflate(R.layout.popupwindow_view, ((ViewGroup) window.findViewById(android.R.id.content)), false);//需要用android.R.id.content这个view
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        initView();
        WindowManager.LayoutParams mWindowAttributes = window.getAttributes();
        mWindowAttributes.height = CommonUtils.INSTANCE.dip2px(getActivity(), 300);
        return mFilterView;
    }

    private void initView() {
        playList.clear();
        playList = PlayList.getList(getActivity());
        hide = mFilterView.findViewById(R.id.hide);
        tv_song_totalTime = mFilterView.findViewById(R.id.tv_song_totalTime);
        hide.setOnClickListener(this);
        tv_total_num = mFilterView.findViewById(R.id.tv_total_num);
        clearList = mFilterView.findViewById(R.id.clearList);
        clearList.setOnClickListener(this);
        lv_playlist = mFilterView.findViewById(R.id.lv_list);
        setSwipMenue();
        if (playList != null && bean != null) {
            for (int i = 0; i < playList.size(); i++) {
                if (playList.get(i).getId() == bean.getId()) {
                    playList.get(i).setCurrentPlaying(true);
                    position_curr_song = i;
                } else {
                    playList.get(i).setCurrentPlaying(false);
                }
                String playtime = playList.get(i).getPlaytime();
                if (playtime != null && playtime.length() > 0) {
                    char c = playtime.charAt(0);
                    char c2 = playtime.charAt(1);
                    int c1 = c;
                    if (c1 != 0) {
                        totalTime += c1;
                    } else {
                        totalTime += c2;
                    }
                }
            }
            if (playList.size() * 4 > 60) {
                tv_song_totalTime.setText("共计" + playList.size() + "首歌，" + (playList.size() * 4 / 60) + "小时" + (totalTime % 60) + "分钟");
            } else {
                tv_song_totalTime.setText("共计" + playList.size() + "首歌，" + playList.size() * 4.0 + "分钟");
            }
            tv_total_num.setText("播放列表（" + playList.size() + "首）");
            adapter = new MusicListAdapter(playList, getActivity());
            lv_playlist.setAdapter(adapter);
            if (position_curr_song > 2) {
                lv_playlist.setSelection(position_curr_song - 2);
            } else {
                lv_playlist.setSelection(0);
            }
            lv_playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                    Intent intent = new Intent(getActivity(), MediaService.class);
                    intent.putExtra("bean", playList.get(position));
                    getActivity().startService(intent);
                    for (int i = 0; i < playList.size(); i++) {
                        MusicInfo.DataBean dataBean = playList.get(i);
                        if (i == position) {
                            dataBean.setCurrentPlaying(true);
                        } else {
                            dataBean.setCurrentPlaying(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }

    private void setSwipMenue() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleItem = new SwipeMenuItem(
                        getActivity());
                deleItem.setBackground(new ColorDrawable(Color.parseColor("#ff6699")));
                deleItem.setWidth(CommonUtils.INSTANCE.dip2px(getActivity(), 60));
                deleItem.setTitle("删除");
                deleItem.setTitleSize(12);
                deleItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleItem);
            }
        };
        lv_playlist.setMenuCreator(creator);
        lv_playlist.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        PlayList.deleCurrMusic(getActivity(), playList.get(position));
                        tv_total_num.setText("当前共 " + playList.size() + " 首歌");
                        if (playList.size() * 4 > 60) {
                            tv_song_totalTime.setText("共计" + playList.size() + "首歌，" + (playList.size() * 4 / 60) + "小时" + (totalTime % 60) + "分钟");
                        } else {
                            tv_song_totalTime.setText("共计" + playList.size() + "首歌，" + playList.size() * 4 + "分钟");
                        }
                        adapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(@NonNull View v) {
        switch (v.getId()) {
            case R.id.hide://收起
                dismiss();
                break;
            case R.id.clearList:
                playList.clear();
//                adapter.notifyDataSetChanged();
                DBManager dbManager = new DBManager(getActivity());
                if(null!=dbManager){
                    dbManager.deleteAllInfo(0);
                }

                tv_total_num.setText("当前共0首歌");
                tv_song_totalTime.setText("共计0首歌，0分钟");
                break;
        }
    }
}
