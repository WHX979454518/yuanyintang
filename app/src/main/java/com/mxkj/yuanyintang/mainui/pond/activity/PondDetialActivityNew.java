package com.mxkj.yuanyintang.mainui.pond.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding2.view.RxView;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.extraui.AgreeAnimationUtil;
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.mainui.home.bean.SongListDetails;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew;
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo;
import com.mxkj.yuanyintang.mainui.pond.PondOperationDialog;
import com.mxkj.yuanyintang.mainui.pond.TagDetialPondList;
import com.mxkj.yuanyintang.mainui.web.CommonWebview;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.net.ApiAddress;
import com.mxkj.yuanyintang.utils.RichTextutils.ImageTextUtil;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.photopicker.widget.NoScrollRecyclerView;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.mainui.pond.adapter.AllPondCommentAdapter;
import com.mxkj.yuanyintang.mainui.pond.adapter.PondDetialImgListAdapter;
import com.mxkj.yuanyintang.mainui.pond.adapter.SimilerPondAdapter;
import com.mxkj.yuanyintang.mainui.pond.adapter.VoteAdapter_PondDetial;
import com.mxkj.yuanyintang.mainui.pond.bean.PondCommentBean;
import com.mxkj.yuanyintang.mainui.pond.bean.PondDetialBean;
import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.bean.PictureDataBean;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.layoutmanager.decoration.SpacesItemDecoration;
import com.mxkj.yuanyintang.utils.threadpool.ThreadPoolManager;
import com.mxkj.yuanyintang.video.MvVideoActivitykt;
import com.mxkj.yuanyintang.widget.MultiLineRadioGroup;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.VpSuperSwipeRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_PID;
import static com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_SUCCESS;
import static com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.COMMENT_SUCCESS_JSON;
import static com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard.IS_POND;
import static com.mxkj.yuanyintang.musicplayer.service.MediaService.ACTION_PAUSE;

public class PondDetialActivityNew extends StandardUiActivity {
    @BindView(R.id.recycler_pond_allcomment)
    RecyclerView recyclerPondAllcomment;
    @BindView(R.id.img_agree)
    ImageView img_agree;
    @BindView(R.id.ani_img_agree)
    ImageView ani_img_agree;
    @BindView(R.id.swipe_refresh)
    VpSuperSwipeRefreshLayout swipeRefresh;
    @BindView(R.id.back_botom)
    ImageView backBotom;
    @BindView(R.id.tv_replyPond)
    TextView tvReplyPond;
    @BindView(R.id.share_pond)
    TextView sharePond;
    @BindView(R.id.comments_pond_rl)
    RelativeLayout comments_pond_rl;
    @BindView(R.id.pond_comments_num)
    TextView pond_comments_num;
    @BindView(R.id.ll_agree)
    LinearLayout ll_agree;
    @BindView(R.id.tv_agree_num)
    TextView tv_agree_num;
    private int pid;
    private PondDetialBean.DataBean pondDetialBean;
    private View headerBaseInfo;
    private View headerPondVote;
    private View headerPondSimilar;
    private View headerPondHotComment;
    public static final String PID = "pid";
    public static final String POND_DETIAL = "pond_detial";
    private int POND_REPLY = 10088;
    private InterfaceRefreshLoadMore interfaceRefreshLoadMore;
    private List<PondCommentBean.DataBean> commenList = new ArrayList<>();//默认评论列表
    private List<PondCommentBean.DataBean> hot_ommenList = new ArrayList<>();//热门评论
    private AllPondCommentAdapter allCommentAdapter;
    private int page = 1;
    private NoScrollRecyclerView lvImgList;
    private AllPondCommentAdapter hotCommentAdapter;
    private List<PondDetialBean.DataBean.VoteBean> voteList;
    private VoteAdapter_PondDetial voteAdapter_pondDetial;
    public static final String POND_COMMENT_ACTION = "update_pond_comment";
    public static final String POND_COMMENT_BEAN = "pond_comment_bean";
    private UpdateCommentReceiver pondReceiver;
    private IntentFilter pondFilter;
    private int eventPosition;
    private String commentType = "";

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_pond_detial_new;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("详情");
        initAdapter();
        headerBaseInfo = LayoutInflater.from(this).inflate(R.layout.pond_detial_header1, null);
        headerPondVote = LayoutInflater.from(this).inflate(R.layout.pond_detial_header4, null);
        headerPondSimilar = LayoutInflater.from(this).inflate(R.layout.pond_detial_header5, null);
        headerPondHotComment = LayoutInflater.from(this).inflate(R.layout.pond_detial_header6, null);
        allCommentAdapter.addHeaderView(headerBaseInfo, 0);
        allCommentAdapter.addHeaderView(headerPondVote, 3);
        allCommentAdapter.addHeaderView(headerPondSimilar, 4);
        allCommentAdapter.addHeaderView(headerPondHotComment, 5);
        View headerPondAllCommentTitle = LayoutInflater.from(this).inflate(R.layout.pond_detial_header7, null);
        allCommentAdapter.addHeaderView(headerPondAllCommentTitle, 6);
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });

        Drawable lDrawable = getResources().getDrawable(R.drawable.more_charts_member);
        lDrawable.setBounds(0, 0, lDrawable.getMinimumWidth(), lDrawable.getMinimumHeight());
        setRightButton("", lDrawable, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PondOperationDialog dynamicOperationDialog = new PondOperationDialog(PondDetialActivityNew.this, pondDetialBean);
                dynamicOperationDialog.show(getSupportFragmentManager(), "mDynamicDialog");

            }
        });
        comments_pond_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到第一条的位置
                recyclerPondAllcomment.scrollToPosition(1);
            }
        });


        ll_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpParams params = new HttpParams();
                params.put("type", "5");
                params.put("item_id", pid + "");
                AgreeAnimationUtil.INSTANCE.setAnim(PondDetialActivityNew.this, ani_img_agree, img_agree, AgreeAnimationUtil.COMMENT_AGREE);
                NetWork.INSTANCE.agree(PondDetialActivityNew.this, params, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(@NotNull String resultData, @Nullable Headers headers) {
                        JSONObject jsonObject = JSON.parseObject(resultData);
                        int code = jsonObject.getInteger("code");
                        if (code == 200) {
                            if (pondDetialBean.getIs_agree() == 0) {
                                img_agree.setImageResource(R.drawable.icon_agreed);
                                tv_agree_num.setText((pondDetialBean.getAgrees() + 1) + "");
                                pondDetialBean.setIs_agree(1);
                            }
                        }
                    }

                    @Override
                    public void doError(@NotNull String msg) {

                    }

                    @Override
                    public void doResult() {

                    }
                });

            }
        });

    }

    private void initAdapter() {
        if (recyclerPondAllcomment.getAdapter() == null) {
            allCommentAdapter = new AllPondCommentAdapter(commenList, getSupportFragmentManager(), "all");
            recyclerPondAllcomment.setLayoutManager(new LinearLayoutManager(this));
            recyclerPondAllcomment.setAdapter(allCommentAdapter);
            recyclerPondAllcomment.setItemViewCacheSize(100);
        }
        allCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                eventPosition = position;
                commentType = "all";
                Log.e(TAG, "onItemClick: ");
                PondCommentBean.DataBean dataBean = commenList.get(position);
                if (dataBean != null) {
                    Intent intent = new Intent(PondDetialActivityNew.this, PondReplyDetialActivity.class);
                    intent.putExtra("pond_commentBean", dataBean);
                    intent.putExtra(PondReplyDetialActivity.POND_COMMENTID, dataBean.getId());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initData() {
        initBaseInfoData();
        getHotCommenList(1, "thcount-desc");
        getAllCommenList(page, "update_time-desc");

    }

    @Override
    protected void initEvent() {
        registePondCommentReceiver();
//        showLoadingView();
        pid = getIntent().getIntExtra(PID, 0);
        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(swipeRefresh, this, new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
            @Override
            public void onRefresh() {
                page = 1;
                initData();
                swipeRefresh.setLoadMore(true);
            }

            @Override
            public void onLoadMore() {
                page++;
                getAllCommenList(page, null);
            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPullDistance(int distance) {

            }
        });
    }

    private void registePondCommentReceiver() {
        pondFilter = new IntentFilter();
        pondFilter.addAction(COMMENT_SUCCESS);
        pondFilter.addAction(POND_COMMENT_ACTION);
        pondReceiver = new UpdateCommentReceiver();
        registerReceiver(pondReceiver, pondFilter);
    }

    /**
     * 池塘主要信息
     */

    private void initBaseInfoData() {
        NetWork.INSTANCE.getPondDetialInfo(PondDetialActivityNew.this, pid, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                Log.e(TAG, "池塘详情: " + resultData);
                hideLoadingView();
                PondDetialBean pondDetialBeans = JSON.parseObject(resultData, PondDetialBean.class);
                pondDetialBean = pondDetialBeans.getData();
                initBaseInfoEvent();
                initTag();
                initImgListEvent();
                initSimilarPond();
                initVote();
            }

            @Override
            public void doError(String msg) {
                hideLoadingView();
                setSnackBar(StringUtils.isEmpty(msg), "", R.drawable.icon_fails);

            }

            @Override
            public void doResult() {

            }
        });
    }

    private void initBaseInfoEvent() {
        if (pondDetialBean != null && pondDetialBean.getIs_agree() == 1) {
            img_agree.setImageResource(R.drawable.icon_agreed);
            if(pondDetialBean.getThcount() != 0){
                pond_comments_num.setVisibility(View.VISIBLE);
                pond_comments_num.setText(pondDetialBean.getThcount()+"");
            }
            tv_agree_num.setText((pondDetialBean.getAgrees()) + "");

        }else if(pondDetialBean != null && pondDetialBean.getAgrees()>0){
            if(pondDetialBean.getThcount() != 0){
                pond_comments_num.setVisibility(View.VISIBLE);
                pond_comments_num.setText(pondDetialBean.getThcount()+"");
            }
            tv_agree_num.setText((pondDetialBean.getAgrees()) + "");
        }
        final TextView tvFollow = headerBaseInfo.findViewById(R.id.tv_follow);
        TextView username = headerBaseInfo.findViewById(R.id.username);
        TextView tvFaraway = headerBaseInfo.findViewById(R.id.tvFaraway);
        TextView tvLocation = headerBaseInfo.findViewById(R.id.tvLocation);
        TextView createTime = headerBaseInfo.findViewById(R.id.create_time);
        final TextView itemType = headerBaseInfo.findViewById(R.id.item_type);
        TextView tvSongName = headerBaseInfo.findViewById(R.id.tv_songName);
        TextView content = headerBaseInfo.findViewById(R.id.content);
        TextView singerSong = headerBaseInfo.findViewById(R.id.singer_song);
        ImageView imgIcon = headerBaseInfo.findViewById(R.id.img_icon);
        ImageView vRz = headerBaseInfo.findViewById(R.id.v_rz);
        final ImageView imgPlaymusic = headerBaseInfo.findViewById(R.id.img_playmusic);
        final ImageView imgSong = headerBaseInfo.findViewById(R.id.img_song);
        LinearLayout llFollow = headerBaseInfo.findViewById(R.id.ll_follow);
        LinearLayout llMusic = headerBaseInfo.findViewById(R.id.ll_music);

        LinearLayout show_ll = headerBaseInfo.findViewById(R.id.show_ll);
        LinearLayout hide_ll = headerBaseInfo.findViewById(R.id.hide_ll);
        ImageView img_playmusic2 = headerBaseInfo.findViewById(R.id.img_playmusic2);
        TextView itemType2 = headerBaseInfo.findViewById(R.id.item_type2);
        TextView tvSongName2 = headerBaseInfo.findViewById(R.id.tv_songName2);
        TextView singerSong2 = headerBaseInfo.findViewById(R.id.singer_song2);

        if (pondDetialBean != null) {
            if (TextUtils.isEmpty(pondDetialBean.getPlace_desc())) {
                tvLocation.setVisibility(View.GONE);
            } else {
                tvLocation.setText(pondDetialBean.getPlace_desc());
            }
            if (TextUtils.isEmpty(pondDetialBean.getDistance())) {
                tvFaraway.setVisibility(View.GONE);
            } else {
                tvFaraway.setText(pondDetialBean.getDistance());
            }
            username.setText(StringUtils.isEmpty(pondDetialBean.getNickname()));
            createTime.setText(pondDetialBean.getCreate_time() + "");

            if (pondDetialBean.getIs_music() == 3) {
                vRz.setVisibility(View.VISIBLE);
            } else {
                vRz.setVisibility(View.GONE);
            }

            if((pondDetialBean.getItem_info().getStatus() == 1 && pondDetialBean.getItem_info().getIs_private() == 1) || pondDetialBean.getItem_info().getStatus() == 0) {
                show_ll.setVisibility(View.GONE);
                hide_ll.setVisibility(View.VISIBLE);
//                img_playmusic2.setVisibility(View.GONE);
            }else {
                show_ll.setVisibility(View.VISIBLE);
                hide_ll.setVisibility(View.GONE);
            }



            //关注  状态判断
            if (pondDetialBean.getRelation() == 1) {
                tvFollow.setText("已关注");
                tvFollow.setTextColor(Color.parseColor("#ff6699"));
                tvFollow.setBackgroundResource(R.drawable.home_follow_false);
            } else if (pondDetialBean.getRelation() == 0) {
                tvFollow.setText("+关注");
                tvFollow.setTextColor(Color.parseColor("#ffffff"));
                tvFollow.setBackgroundResource(R.drawable.home_follow_true);
            } else if (pondDetialBean.getRelation() == 2) {
                tvFollow.setText("已关注");
                tvFollow.setTextColor(Color.parseColor("#ff6699"));
                tvFollow.setBackgroundResource(R.drawable.home_follow_false);
            }
            ImageLoader.with(this)
                    .getSize(200, 200)

                    .override(30, 30)
                    .url(pondDetialBean.getHead_link())
                    .asCircle()
                    .scale(ScaleMode.CENTER_CROP)
                    .into(imgIcon);

            RxView.clicks(imgIcon).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(@NonNull Object o) throws Exception {
                    Intent intent = new Intent(PondDetialActivityNew.this, MusicIanDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, pondDetialBean.getUid() + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            RxView.clicks(username).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(@NonNull Object o) throws Exception {
                    Intent intent = new Intent(PondDetialActivityNew.this, MusicIanDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, pondDetialBean.getUid() + "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            RxView.clicks(llFollow).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                @Override
                public void accept(@NonNull Object o) throws Exception {
                    if (CacheUtils.INSTANCE.getBoolean(PondDetialActivityNew.this, Constants.User.IS_LOGIN) == true) {
                        if (pondDetialBean.getRelation() == 0) {
                            follows(pondDetialBean, tvFollow);
                        } else {
                            MaterialDialog("是否取消关注？", null, null, new MaterialDialogBtnClickListener() {
                                @Override
                                public void onBtnClick(int code) {
                                    if (code == 1) {
                                        follows(pondDetialBean, tvFollow);
                                    }
                                }
                            });
                        }
                    } else {
                        startActivity(new Intent(PondDetialActivityNew.this, QuickLoginActivityNew.class));
                    }
                }
            });
            final PondDetialBean.DataBean.ItemInfoBean item_info = pondDetialBean.getItem_info();
            if ((item_info != null && item_info.getId() != 0)||item_info.getItem_type() == 6) {
                llMusic.setVisibility(View.VISIBLE);
                tvSongName.setText(item_info.getTitle());
                singerSong.setText(item_info.getNickname());
                tvSongName2.setText(item_info.getTitle());
                singerSong2.setText(item_info.getNickname());

                Log.e("ffff",""+pondDetialBean.getItem_type());
                switch (pondDetialBean.getItem_type()) {
                    case 1://单曲
                        itemType.setText("单曲");
                        itemType2.setText("单曲");
                        imgPlaymusic.setVisibility(View.VISIBLE);
                        RxView.clicks(imgPlaymusic).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(@NonNull Object o) throws Exception {
//                                if (MediaService.mediaPlayer != null && MediaService.bean != null&&MediaService.bean.getId() == item_info.getId()) {
//                                    item_info.setPlaying(!MediaService.mediaPlayer.isPlaying());
//                                    if(item_info.isPlaying()==true){
//                                        imgPlaymusic.setImageResource(R.drawable.icon_pond_play_true);
//                                        sendBroadcast(new Intent(ACTION_PAUSE));
//                                    }else {
//                                        imgPlaymusic.setImageResource(R.drawable.icon_pond_play_false);
//                                        sendBroadcast(new Intent(ACTION_PAUSE));
//                                    }
//                                } else {
//                                    PlayCtrlUtil.INSTANCE.play(PondDetialActivityNew.this, pondDetialBean.getMusic_id(), 0);
//                                    item_info.setPlaying(true);
//                                    imgPlaymusic.setImageResource(R.drawable.icon_pond_play_true);
//                                }
                                if (MediaService.mediaPlayer != null && MediaService.bean != null && MediaService.bean.getId() == item_info.getId()) {
                                    item_info.setPlaying(!MediaService.mediaPlayer.isPlaying());
                                    sendBroadcast(new Intent(ACTION_PAUSE));
                                    if (item_info.isPlaying()==true) {
                                        imgPlaymusic.setImageResource(R.drawable.icon_pond_play_true);
                                    } else {
                                        imgPlaymusic.setImageResource(R.drawable.icon_pond_play_false);
                                    }

                                } else {
                                    imgPlaymusic.setImageResource(R.drawable.icon_pond_play_true);
                                    item_info.isPlaying();
                                    PlayCtrlUtil.INSTANCE.play(PondDetialActivityNew.this, item_info.getId(), 0);
                                }

                            }
                        });

                        break;
                    case 2://歌单
                        itemType.setText("歌单");
                        itemType2.setText("歌单");
                        imgPlaymusic.setVisibility(View.GONE);

                        break;
                    case 3://池塘
                        itemType.setText("池塘");
                        itemType2.setText("池塘");
                        imgPlaymusic.setVisibility(View.GONE);
                        break;
                    case 4://MV
                        itemType.setText("MV");
                        itemType2.setText("MV");
                        imgPlaymusic.setVisibility(View.GONE);
                        break;
                    case 6://链接
                        show_ll.setVisibility(View.VISIBLE);
                        hide_ll.setVisibility(View.GONE);
                        itemType.setText("链接");
                        itemType2.setText("链接");
                        llMusic.setVisibility(View.VISIBLE);
                        imgPlaymusic.setVisibility(View.GONE);
                        tvSongName.setText(item_info.getTitle());
                        singerSong.setText(item_info.getSub_title());
                        Glide.with(PondDetialActivityNew.this).load(item_info.getImgpic()).into(imgSong);
//                        if(null!=item_info.getImgpic()&&!item_info.getImgpic().equals("")){
//                            Glide.with(PondDetialActivityNew.this).load(item_info.getImgpic()).into(imgSong);
//                        }else {
////                            imgSong.setBackgroundResource(R.drawable.nothing);
//                        }
                        break;
                }

                RxView.clicks(llMusic).throttleFirst(3, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if(itemType.getText().toString().equals("链接") || itemType.getText().toString() == "链接"){
                            if (null!=item_info.getUrl()||item_info.getUrl() != "") {
                                Intent intentweb = new Intent(PondDetialActivityNew.this, CommonWebview.class);
                                intentweb.putExtra("url", item_info.getUrl());
                                intentweb.putExtra("activity", "activity");
                                intentweb.putExtra("title", item_info.getTitle());
                                intentweb.putExtra("content", pondDetialBean.getContent());
                                intentweb.putExtra("img", item_info.getImgpic());
                                startActivity(intentweb);
                            } else {

                            }
                        }
                        if (pondDetialBean.getItem_info().getStatus() == 0) {

                            if(itemType.getText().toString().equals("单曲")){
                                Toast toast = new Toast(PondDetialActivityNew.this);
                                View view = LayoutInflater.from(PondDetialActivityNew.this).inflate(R.layout.customtoast, null);
                                toast.setView(view);
                                toast.setGravity(Gravity.FILL_HORIZONTAL, 0, -500);
                                toast.show();
                            }else {
                                if (TextUtils.isEmpty(item_info.getId()+"")) {
                                    return;
                                }
                                NetWork.INSTANCE.getSongSheetDetails("songDetails_"+item_info.getId(),PondDetialActivityNew.this, item_info.getId()+"", new NetWork.TokenCallBack() {
                                    @Override
                                    public void doNext(String resultData, Headers headers) {
                                    }

                                    @Override
                                    public void doError(String msg) {

                                    }

                                    @Override
                                    public void doResult() {

                                    }
                                });

                            }


                        }else{
                            switch (pondDetialBean.getItem_type()) {
                                case 1:
                                    PlayCtrlUtil.INSTANCE.play(PondDetialActivityNew.this, pondDetialBean.getMusic_id(), 0);
                                    break;
                                case 2:
                                    Intent intent = new Intent(PondDetialActivityNew.this, SongSheetDetailsActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, item_info.getId() + "");
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    break;
                                case 3:
                                    Intent intentp = new Intent(PondDetialActivityNew.this, PondDetialActivityNew.class);
                                    Bundle bundlep = new Bundle();
                                    bundlep.putInt(PondDetialActivityNew.PID, item_info.getId());
                                    intentp.putExtras(bundlep);
                                    startActivity(intentp);
                                    break;
                                case 4:
                                    if(item_info.getStatus() == 0){
                                        Toast toast = new Toast(PondDetialActivityNew.this);
                                        View view = LayoutInflater.from(PondDetialActivityNew.this).inflate(R.layout.customtoast, null);
                                        toast.setView(view);
                                        toast.setGravity(Gravity.TOP, 0, 30);
                                        toast.show();
                                    }else{
                                        toMV(item_info.getId(),item_info.getMv_info().getLink(),item_info.getUid(),item_info.gettitle(),item_info.getNickname()
                                                ,item_info.getImgpic_link());
                                    }
                                    break;
                                case 6:
                                    if (null!=item_info.getUrl()||item_info.getUrl() != "") {
                                        Intent intentweb = new Intent(PondDetialActivityNew.this, CommonWebview.class);
                                        intentweb.putExtra("url", item_info.getUrl());
                                        intentweb.putExtra("activity", "activity");
                                        intentweb.putExtra("title", item_info.getTitle());
                                        intentweb.putExtra("content", pondDetialBean.getContent());
                                        intentweb.putExtra("img", item_info.getImgpic());
                                        startActivity(intentweb);
                                    } else {

                                    }
                                    break;
                            }
                        }
//                        TODO

                    }
                });

                try {
                    ImageLoader.with(this)
                            .getSize(200, 200)

                            .override(55, 55)
                            .url(pondDetialBean.getItem_info().getImgpic_link())
//                            .url(pondDetialBean.getImgpic_info().getLink())
                            .scale(ScaleMode.CENTER_CROP)
                            .into(imgSong);

                } catch (RuntimeException e) {
                }


            } else {
                llMusic.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(pondDetialBean.getContent())) {
                if (pondDetialBean.getContent().contains("<img") || pondDetialBean.getContent().contains("<br>")) {
                    ImageTextUtil.setImageText(content, pondDetialBean.getContent());
                } else {
                    content.setText(pondDetialBean.getContent());
                }
            }
        }
    }

    private void initImgListEvent() {
        if (pondDetialBean != null) {
            lvImgList = headerBaseInfo.findViewById(R.id.pond_detial_img);
            final List<String> imglist_link = new ArrayList<>();
            List<PondDetialBean.DataBean.ImglistInfoBean> imglist_info = pondDetialBean.getImglist_info();
            if (imglist_info != null) {
                for (int i = 0; i < imglist_info.size(); i++) {
                    imglist_link.add(imglist_info.get(i).getLink());
                }
                if (imglist_link.size() > 0) {
                    PondDetialImgListAdapter adapter = new PondDetialImgListAdapter(R.layout.pong_reply_img_item, imglist_link, imglist_info);
                    lvImgList.setLayoutManager(new LinearLayoutManager(this));
                    lvImgList.setAdapter(adapter);
                    if (lvImgList.getRecycledViewPool() != null) {
                        lvImgList.getRecycledViewPool().setMaxRecycledViews(0, 10);
                    }
                    adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            Bundle bundle = new Bundle();
                            PictureDataBean pictureDataBean = new PictureDataBean()
                                    .setId(pondDetialBean.getId() + "")
                                    .setCommentNum(pondDetialBean.getHits())
                                    .setPhotoList(imglist_link)
                                    .setTitle(pondDetialBean.getTitle())
                                    .setNickname(pondDetialBean.getNickname())
                                    .setType("pond")
                                    .setPosition(position)
                                    .setHits(pondDetialBean.getHits());
                            bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean);
                            Intent intent = new Intent(PondDetialActivityNew.this, PicturePagerDetailsActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            }
        }
    }

    private void initHotComment() {
        NoScrollRecyclerView recy_hot_comment = headerPondHotComment.findViewById(R.id.recycler_hotComment);
        if (recy_hot_comment.getAdapter() == null) {
            hotCommentAdapter = new AllPondCommentAdapter(hot_ommenList, getSupportFragmentManager(), "hot");
            recy_hot_comment.setLayoutManager(new LinearLayoutManager(this));
            recy_hot_comment.setAdapter(hotCommentAdapter);
            hotCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Log.e(TAG, "onItemClick: ");
                    eventPosition = position;
                    commentType = "hot";
                    PondCommentBean.DataBean dataBean = hot_ommenList.get(position);
                    if (dataBean != null) {
                        Intent intent = new Intent(PondDetialActivityNew.this, PondReplyDetialActivity.class);
                        intent.putExtra("pond_commentBean", dataBean);
                        intent.putExtra(PondReplyDetialActivity.POND_COMMENTID, dataBean.getId());
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void initSimilarPond() {
        RecyclerView recycler_similerPond = headerPondSimilar.findViewById(R.id.recycler_similerPond);
        RelativeLayout rl_recommdPond = headerPondSimilar.findViewById(R.id.rl_recommdPond);
        if (pondDetialBean.getRecommend() != null && pondDetialBean.getRecommend().size() > 0) {
            SimilerPondAdapter similerPondAdapter = new SimilerPondAdapter(pondDetialBean.getRecommend());
            if (recycler_similerPond.getAdapter() == null) {
                recycler_similerPond.setLayoutManager(new LinearLayoutManager(PondDetialActivityNew.this, LinearLayoutManager.HORIZONTAL, false));
                recycler_similerPond.addItemDecoration(new SpacesItemDecoration(CommonUtils.INSTANCE.dip2px(PondDetialActivityNew.this, 10), CommonUtils.INSTANCE.dip2px(PondDetialActivityNew.this, 10)));
                recycler_similerPond.setAdapter(similerPondAdapter);
            }
        } else {
            rl_recommdPond.setVisibility(View.GONE);
        }
    }

    private void initVote() {
        ListView lv_vote = headerPondVote.findViewById(R.id.lv_vote);
        final EditText et_vote = headerPondVote.findViewById(R.id.et_vote);
        final TextView tv_send_vote = headerPondVote.findViewById(R.id.tv_send_vote);
        TextView tv_vote_title = headerPondVote.findViewById(R.id.tv_vote_title);
        LinearLayout ll_voteView = headerPondVote.findViewById(R.id.ll_voteView);
        voteList = pondDetialBean.getVote();
        if (pondDetialBean.getVote() != null && pondDetialBean.getVote().size() > 0) {
            ll_voteView.setVisibility(View.VISIBLE);
            voteAdapter_pondDetial = new VoteAdapter_PondDetial(PondDetialActivityNew.this, voteList, pondDetialBean);
            lv_vote.setAdapter(voteAdapter_pondDetial);
            int sumvotenum = pondDetialBean.getSumvotenum();//投票总数
            final int is_vote = pondDetialBean.getIs_vote();
            String is_vote_id = pondDetialBean.getIs_vote_id();//投票记录id
            int hide = pondDetialBean.getHide();//投票可见
            tv_vote_title.setText(pondDetialBean.getQuestion_name() + "");
            Log.e(TAG, "initVoteInfoHeadView:sumvotenum== " + sumvotenum + "");
            if (is_vote == 1) {
                tv_send_vote.setText("已投票");
                tv_send_vote.setClickable(false);
                et_vote.setVisibility(View.GONE);
            } else {
                Log.e(TAG, "投票多选单选: " + pondDetialBean.getVotetype());
                lv_vote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (pondDetialBean.getVotetype() > 1) {//多选
                            int cout = 0;
                            if (cout < 4) {
                                voteList.get(i).setChecked(true);
                                voteList.get(i).setVotenum(voteList.get(i).getVotenum() + 1);
                                pondDetialBean.setVote(voteList);
                                cout++;
                            } else {
                                if (voteList.get(i).isChecked() == true) {
                                    voteList.get(i).setChecked(false);
                                    if (voteList.get(i).getVotenum() > 0) {
                                        voteList.get(i).setVotenum(voteList.get(i).getVotenum() - 1);
                                    }
                                    pondDetialBean.setVote(voteList);
                                    cout--;
                                }
                            }
                        } else {//单选
                            for (int j = 0; j < voteList.size(); j++) {
                                voteList.get(j).setChecked(false);
                            }
                            voteList.get(i).setChecked(true);
                        }
                        voteAdapter_pondDetial.notifyDataSetChanged();
                    }
                });

            /*提交投票*/
                tv_send_vote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (CacheUtils.INSTANCE.getBoolean(PondDetialActivityNew.this, Constants.User.IS_LOGIN, false)) {
                            HttpParams params = new HttpParams();
                            params.put("pid", pondDetialBean.getId() + "");
                            String viteIds = "";
                            for (int i = 0; i < voteList.size(); i++) {
                                if (voteList.get(i).isChecked() == true) {
                                    if (pondDetialBean.getVotetype() > 1) {
                                        viteIds += voteList.get(i).getId() + ",";
                                    } else {
                                        viteIds = voteList.get(i).getId() + "";
                                    }
                                }
                            }
                            params.put("ids", viteIds);
                            Log.e(TAG, "onClick: params===" + params);
                            if (!TextUtils.isEmpty(et_vote.getText())) {
                                HttpParams param = new HttpParams();
                                param.put("pid", pid + "");
                                param.put("content", et_vote.getText().toString());
                                NetWork.INSTANCE.replyTopic(PondDetialActivityNew.this, param, new NetWork.TokenCallBack() {
                                    @Override
                                    public void doNext(String resultData, Headers headers) {
                                        hideLoadingView();
                                        getAllCommenList(1, null);
                                    }

                                    @Override
                                    public void doError(String msg) {
                                        Log.e(TAG, "doError: " + msg);
                                        hideLoadingView();
                                    }

                                    @Override
                                    public void doResult() {

                                    }
                                });
                                params.put("content", "");
                                NetWork.INSTANCE.pondVote(PondDetialActivityNew.this, params, new NetWork.TokenCallBack() {
                                    @Override
                                    public void doNext(String resultData, Headers headers) {
                                        et_vote.getText().clear();
                                        et_vote.setFocusable(false);
                                        et_vote.setFocusableInTouchMode(false);
                                        JSONObject jsonObject = JSON.parseObject(resultData);
                                        Integer code = jsonObject.getInteger("code");
                                        if (code == 200) {
                                            pondDetialBean.setIs_vote(1);
                                            pondDetialBean.setSumvotenum(pondDetialBean.getSumvotenum() + 1);
                                            tv_send_vote.setText("已投票");
                                            tv_send_vote.setClickable(false);
                                            et_vote.setVisibility(View.GONE);
                                            setSnackBar("投票成功", "", R.drawable.icon_success);
                                            voteAdapter_pondDetial.notifyDataSetChanged();

                                        }
                                    }

                                    @Override
                                    public void doError(String msg) {

                                    }

                                    @Override
                                    public void doResult() {

                                    }
                                });
                            } else {
                                setSnackBar("输入你的看法吧~", "", R.drawable.icon_tips_bad);
                            }
                        } else {
                            startActivity(new Intent(PondDetialActivityNew.this, QuickLoginActivityNew.class));
                        }
                    }
                });
            }
        }
    }

    private void initTag() {
        final List<PondDetialBean.DataBean.TagBean> tag = pondDetialBean.getTag();
        if (tag != null && tag.size() > 0) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < tag.size(); i++) {
                list.add(tag.get(i).getTitle());
            }
            MultiLineRadioGroup tagView = headerBaseInfo.findViewById(R.id.pond_tag);
            tagView.removeAllViews();
            tagView.addAll(list);
            tagView.setOnCheckChangedListener(new MultiLineRadioGroup.OnCheckedChangedListener() {
                @Override
                public void onItemChecked(MultiLineRadioGroup group, int position, boolean checked) {
                    Log.e(TAG, "onItemChecked: ");
                    Intent intent = new Intent(PondDetialActivityNew.this, TagDetialPondList.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(TagDetialPondList.TAG_ID, tag.get(position).getId());
                    bundle.putString(TagDetialPondList.TAG_TITLE, tag.get(position).getTitle());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

    }

    private void getAllCommenList(final int page, final String order) {
        pid = getIntent().getIntExtra("pid", 0);
        HttpParams params = new HttpParams();
        params.put("pid", pid + "");
        params.put("p", page + "");
        if (order != null) {
            params.put("order", order);
        }
        NetWork.INSTANCE.getAllPondComment(page == 1 ? true : false, this, pid, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                interfaceRefreshLoadMore.setStopRefreshing();
                PondCommentBean pondCommentBean = JSON.parseObject(resultData, PondCommentBean.class);
                List<PondCommentBean.DataBean> data = pondCommentBean.getData();
                if (data != null && data.size() > 0) {
                    if (page == 1) {
                        commenList.clear();
                    }
                    commenList.addAll(data);
                    if (recyclerPondAllcomment.getScrollState() == RecyclerView.SCROLL_STATE_IDLE && (recyclerPondAllcomment.isComputingLayout() == false)) {
                        allCommentAdapter.setNewData(commenList);
                    }
                } else {
                    if (page == 1) {
                        if (allCommentAdapter.getFooterLayoutCount() == 0) {
                            View inflate = LayoutInflater.from(PondDetialActivityNew.this).inflate(R.layout.no_comment_layout, null);
                            allCommentAdapter.addFooterView(inflate);
                        }
                    } else {
                        if (allCommentAdapter.getFooterLayoutCount() == 0) {
                            View inflate = LayoutInflater.from(PondDetialActivityNew.this).inflate(R.layout.no_more_data_text, null);
//                            allCommentAdapter.addFooterView(inflate);
                            allCommentAdapter.addFooterView(inflate);
                            swipeRefresh.setLoadMore(false);
                        }
                    }
                }
            }

            @Override
            public void doError(String msg) {
                interfaceRefreshLoadMore.setStopRefreshing();
            }

            @Override
            public void doResult() {

            }
        });
    }

    private void getHotCommenList(final int page, final String order) {
        pid = getIntent().getIntExtra("pid", 0);
        HttpParams params = new HttpParams();
        params.put("pid", pid + "");
        params.put("p", page + "");
        params.put("row", 3);
        params.put("order", order);
        NetWork.INSTANCE.getAllPondComment(page == 1 ? true : false, this, pid, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                interfaceRefreshLoadMore.setStopRefreshing();
                PondCommentBean pondCommentBean = JSON.parseObject(resultData, PondCommentBean.class);
                List<PondCommentBean.DataBean> data = pondCommentBean.getData();
                if (data != null && data.size() > 0) {
                    headerPondHotComment.setVisibility(View.VISIBLE);
                    hot_ommenList.clear();
                    if (hotCommentAdapter != null) {
                        hotCommentAdapter.notifyDataSetChanged();
                    }
                    hot_ommenList.addAll(data);
                    try {
                        initHotComment();
                    } catch (RuntimeException e) {
                    }
                } else {
                    headerPondHotComment.setVisibility(View.GONE);
                }
            }

            @Override
            public void doError(String msg) {
                interfaceRefreshLoadMore.setStopRefreshing();

            }

            @Override
            public void doResult() {

            }
        });
    }

    @OnClick({R.id.back_botom, R.id.tv_replyPond, R.id.share_pond})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_botom:
                finish();
                break;
            case R.id.tv_replyPond:
                if (CacheUtils.INSTANCE.getBoolean(PondDetialActivityNew.this, Constants.User.IS_LOGIN, false)) {
                    Intent intent = new Intent(this, ReplyPond.class);
                    intent.putExtra("pondId", pid + "");
                    startActivityForResult(intent, POND_REPLY);
                } else {
                    startActivity(new Intent(this, QuickLoginActivityNew.class));
                }
                break;
            case R.id.share_pond:
                if (NetConnectedUtils.isNetConnected(getApplicationContext())) {
                    if (CacheUtils.INSTANCE.getBoolean(PondDetialActivityNew.this, Constants.User.IS_LOGIN, false) == true && pondDetialBean != null) {
                        MusicBean musicBean = new MusicBean();
                        MusicBean.ShareDataBean shareDataBean = new MusicBean.ShareDataBean();
                        shareDataBean.setType("pond");

                        shareDataBean.setNickname(pondDetialBean.getNickname());
                        shareDataBean.setTopicContent(pondDetialBean.getContent());
                        shareDataBean.setTitle(pondDetialBean.getTitle() + "");
                        shareDataBean.setMuisic_id(pondDetialBean.getId());
                        if (pondDetialBean.getImglist_info() != null && pondDetialBean.getImglist_info().size() > 0) {
                            shareDataBean.setWebImgUrl(pondDetialBean.getImglist_info().get(0).getLink());
                            shareDataBean.setImage_link(pondDetialBean.getImglist_info().get(0).getLink());
                        }
                        String shareUrl = ApiAddress.INSTANCE.getH5_BASE_URL() + "topic/detail?id=" + pondDetialBean.getId();
                        shareDataBean.setShareUrl(shareUrl);
                        musicBean.setShareDataBean(shareDataBean);
                        ShareBottomDialog shareBottomDialog = new ShareBottomDialog(this, musicBean);
                        shareBottomDialog.show();
                    } else {
                        goActivity(QuickLoginActivityNew.class);
                    }
                }
                break;
        }
    }

    private void follows(final PondDetialBean.DataBean data, final TextView tv_follow) {
        showLoadingView();
        HttpParams params = new HttpParams();
        params.put("id", data.getUid() + "");
        NetWork.INSTANCE.follow(PondDetialActivityNew.this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                JSONObject object = JSON.parseObject(resultData);
                int code = object.getInteger("code");
                if (code == 200) {
                    if (data.getRelation() == 0) {
                        tv_follow.setText("已关注");
                        tv_follow.setTextColor(Color.parseColor("#ff6699"));
                        tv_follow.setBackgroundResource(R.drawable.home_follow_false);
                        data.setRelation(1);
                    } else if (data.getRelation() == 1) {
                        tv_follow.setText("+关注");
                        tv_follow.setTextColor(Color.parseColor("#ffffff"));
                        tv_follow.setBackgroundResource(R.drawable.home_follow_true);
                        data.setRelation(0);
                    } else if (data.getRelation() == 2) {
                        tv_follow.setText("+关注");
                        tv_follow.setTextColor(Color.parseColor("#ffffff"));
                        tv_follow.setBackgroundResource(R.drawable.home_follow_true);
                        data.setRelation(0);
                    }
                }
                hideLoadingView();
            }

            @Override
            public void doError(String msg) {
                hideLoadingView();
            }

            @Override
            public void doResult() {

            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pondReceiver);
        interfaceRefreshLoadMore.resetRefreshView();
        System.gc();
    }

    public void setEventPosition(int position, String commentType) {
        eventPosition = position;
        this.commentType = commentType;
    }

    private class UpdateCommentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MobclickAgent.onEvent(PondDetialActivityNew.this,"pond_comment");
            if (intent != null && intent.getAction() != null) {
                allCommentAdapter.removeAllFooterView();
                switch (intent.getAction()) {
                    case POND_COMMENT_ACTION:
                        setSnackBar("回复成功", "", R.drawable.icon_success);
                        Log.e(TAG, "池塘评论更新了。。: ");
                        PondCommentBean.DataBean pondDataBean = (PondCommentBean.DataBean) intent.getSerializableExtra(POND_COMMENT_BEAN);
                        commenList.add(0, pondDataBean);
                        allCommentAdapter.setNewData(commenList);
                        break;
                    case COMMENT_SUCCESS://池塘评论（pid为0时为评论的回复,否则为子评论的回复）
                        allCommentAdapter.removeAllFooterView();
                        String stringExtra = intent.getStringExtra(COMMENT_SUCCESS_JSON);
                        JSONObject jsonObject = JSON.parseObject(stringExtra);
                        String dataStr = jsonObject.getString("data");
                        int pid = intent.getIntExtra(COMMENT_PID, 0);
                        boolean booleanExtra = intent.getBooleanExtra(IS_POND, false);
                        if (booleanExtra == true) {
                            switch (commentType) {
                                case "hot":
                                    Log.e(TAG, "onReceive: 热门评论更新" + stringExtra);
                                    PondCommentBean.DataBean dataBean = hot_ommenList.get(eventPosition);
                                    List<PondCommentBean.DataBean.ComListsBean> com_lists = dataBean.getCom_lists();
                                    PondCommentBean.DataBean.ComListsBean comListsBean = JSON.parseObject(dataStr, PondCommentBean.DataBean.ComListsBean.class);
                                    com_lists.add(0, comListsBean);
                                    dataBean.setCom_lists(com_lists);
                                    hot_ommenList.set(0, dataBean);
                                    hotCommentAdapter.notifyItemChanged(eventPosition);
                                    setSnackBar("回复成功", "", R.drawable.icon_success);
                                    break;

                                case "all":
                                    Log.e(TAG, "onReceive: 全部评论更新" + stringExtra);
                                    Log.e(TAG, "eventPosition==: " + eventPosition);
                                    if (eventPosition > 0) {
                                        --eventPosition;//减去header占的一个位置
                                    }
                                    PondCommentBean.DataBean dataBeanAll = commenList.get(eventPosition);
                                    List<PondCommentBean.DataBean.ComListsBean> com_listsAll = dataBeanAll.getCom_lists();
                                    if (com_listsAll == null) {
                                        com_listsAll = new ArrayList<>();
                                    }
                                    PondCommentBean.DataBean.ComListsBean comListsBeanAll = JSON.parseObject(dataStr, PondCommentBean.DataBean.ComListsBean.class);
                                    com_listsAll.add(0, comListsBeanAll);
                                    dataBeanAll.setCom_lists(com_listsAll);
                                    commenList.set(0, dataBeanAll);
                                    allCommentAdapter.notifyItemChanged(eventPosition + 1);
                                    setSnackBar("回复成功", "", R.drawable.icon_success);
                                    break;
                            }
                        }
                        break;
                }
            }
        }
    }
    //跳转MV
    private void toMV(int id ,String mvurl,int uid,String title,String nickname,String imgpic_link) {
        Intent intent = new Intent(PondDetialActivityNew.this, MvVideoActivitykt.class);
        Bundle bundle = new Bundle();
        bundle.putInt("mv", id);
        bundle.putString("mvurl", mvurl);
        bundle.putInt("uid", uid);
        bundle.putString("title", title);
        bundle.putString("nickname", nickname);
        bundle.putString("imgpic_link", imgpic_link);
        bundle.putString("bioashi", 1+ "");
        intent.putExtra("mvdate",bundle);
        startActivity(intent);
    }
}
