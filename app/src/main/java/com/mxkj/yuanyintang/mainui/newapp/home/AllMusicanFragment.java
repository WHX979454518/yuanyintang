package com.mxkj.yuanyintang.mainui.newapp.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.j256.ormlite.stmt.query.In;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.MainApplication;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.home.adapter.MusicListMusicIanAdapter;
import com.mxkj.yuanyintang.mainui.home.bean.MusicListMusicIanBean;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.entity.MultiItemEntity;
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity;
import com.mxkj.yuanyintang.mainui.home.music_charts.adapter.DoughnutsAdapter;
import com.mxkj.yuanyintang.mainui.home.music_charts.bean.ChartsListBean;
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber;
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity.CHART_ID;
import static com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity.CHART_TIME_TYPE;

public class AllMusicanFragment extends RxFragment {
    private int id;
    private String type;
    private View rootView;

    private int page = 1;
    private MusicListMusicIanAdapter musicListMusicIanAdapter  = null;
    private ArrayList<MusicListMusicIanBean.DataBean> musicListMusicIanBeanList= new ArrayList<>();
    private RecyclerView recyclerview;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.allmusicanfragment, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initEvent();
        return rootView;
    }

    private void initEvent() {
        id = getArguments().getInt(CHART_ID, 0);
        type = getArguments().getString(CHART_TIME_TYPE, null);
        recyclerview = rootView.findViewById(R.id.recyclerview);
        netPublicMusicIan(id,type);

    }

    private void netData(int tag,String tagStr) {
        if (id == 0) {
            return;
        }
        NetWork.INSTANCE.getPublicMusicIan(id,type,1, getActivity(), "", new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                Log.e("ffff",""+resultData.toString());

            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }
    private void netPublicMusicIan(int tag,String tagStr) {
        NetWork.INSTANCE.getPublicMusicIan(tag,  tagStr,page, getActivity(), "", new NetWork.TokenCallBack() {
            @Override
            public void doResult() {

            }

            @Override
            public void doError(@NotNull String msg) {

            }

            @Override
            public void doNext(@NotNull String resultData, @org.jetbrains.annotations.Nullable Headers headers) {
                MusicListMusicIanBean musicListMusicIanBean = new MusicListMusicIanBean();
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(resultData);
                    if(jObj.optJSONArray("data") != null){
                        JSONArray dataArray = jObj.optJSONArray("data");
                        musicListMusicIanBean.setData(parserData(dataArray));
                    }
                    recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if (musicListMusicIanAdapter == null) {
                        musicListMusicIanAdapter = new MusicListMusicIanAdapter(musicListMusicIanBeanList);
                        recyclerview.setAdapter(musicListMusicIanAdapter);
                    } else {
                        musicListMusicIanAdapter.setNewData(musicListMusicIanBeanList);
                    }

                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private List<MusicListMusicIanBean.DataBean> parserData(JSONArray dataArray) {
        if (page == 1) {
            musicListMusicIanBeanList.clear();
        }
        for(int i=0;i<dataArray.length();i++){
            JSONObject jObj = dataArray.optJSONObject(i);
            MusicListMusicIanBean.DataBean dataBean =new MusicListMusicIanBean.DataBean();
            dataBean.setCheck(false);
            dataBean.setDay(jObj.optInt("sex"));
            dataBean.setFans_num(jObj.optInt("fans_num"));
            dataBean.setId(jObj.optInt("id"));
            dataBean.setNickname(jObj.optString("nickname"));
            dataBean.setHead_link(jObj.optString("head_link"));
            dataBean.setIs_relation(jObj.optInt("is_relation"));
            dataBean.setIs_music(jObj.optInt("is_musice"));
            JSONObject musicObject = null;
            if(jObj.optJSONObject("music") != null){
                musicObject = jObj.optJSONObject("music");
            }else {
//                val musicObject = if (jObj.optJSONObject("music") == null) org.json.JSONObject() else jObj.optJSONObject("music")
            }
            MusicListMusicIanBean.DataBean.MusicBean musicBean = new MusicListMusicIanBean.DataBean.MusicBean();
            if (null == musicObject|| musicObject.equals("{}")) {
                musicBean.setTitle("");
                musicBean.setItem_id(-1);
            } else {
                musicBean.setTitle(musicObject.optString("title"));
                musicBean.setItem_id(musicObject.optInt("item_id"));
            }
            dataBean.setMusic(musicBean);
            musicListMusicIanBeanList.add(dataBean);
//            musicListMusicIanAdapter.setNewData(musicListMusicIanBeanList);

        }
        return musicListMusicIanBeanList;
    }

}
