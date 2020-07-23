package com.mxkj.yuanyintang.mainui.home.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIanSongSheetBean;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.mainui.home.bean.SongListDetails;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.mainui.myself.activity.EditSongActivity;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.HelperListBean;
import com.mxkj.yuanyintang.mainui.newapp.pond.PondInfo;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.threadpool.ThreadPoolManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.proguard.P;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Headers;

public class MusicianSongsAdapter extends BaseQuickAdapter<MusicIanSongSheetBean.DataBean, BaseViewHolder> {

    private String type = "";
    private Handler mhandler;


    WindowManager wm;
    ProgressBar progressbar;
    View inflate;
    Dialog dialog;

    RelativeLayout player_all_music,editor_song_list,share_song_list,public_song_list,delete_song_list,cancel;
    LinearLayout l1,l2,l3,l4;
    TextView song_name;


    public MusicianSongsAdapter(List<MusicIanSongSheetBean.DataBean> data,String type) {
        super(R.layout.musician_sheet_item_list, data);
        this.type = type;
    }

    public MusicianSongsAdapter(List<MusicIanSongSheetBean.DataBean> data,String type,Handler mhandler) {
        super(R.layout.musician_sheet_item_list, data);
        this.type = type;
        this.mhandler = mhandler;
    }

    public void setType(String type){
        this.type = type;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MusicIanSongSheetBean.DataBean item, final int position) {

        helper.setText(R.id.tv_music_name, item.getTitle());
        helper.setText(R.id.tv_num, item.getTotal() + "首,播放" + item.getCounts() + "次");

        //判断是否是自己的歌单，是则显示(現在是自己收藏的歌单和别人的歌单都不会显示这几个小圆点，吧这些去掉只有别人的歌单在显示删除和分享等)
        UserInfoUtil.getUserInfoById(0, mContext, new UserInfoUtil.UserCallBack() {
            @Override
            public void doNext(UserInfo infoBean) {
                if (infoBean != null && infoBean.getData() != null) {
                    String usersnickname = infoBean.getData().getNickname();
                    if(!usersnickname.equals(item.getNickname())){
                        helper.getView(R.id.music_more).setVisibility(View.GONE);
                    }else {

                    }
                }
            }
        });


        helper.setOnClickListener(R.id.music_more, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
                //填充对话框的布局
                inflate = LayoutInflater.from(mContext).inflate(R.layout.my_new_song_list_dialog, null);
                //初始化控件
                player_all_music = inflate.findViewById(R.id.player_all_music);
                editor_song_list = inflate.findViewById(R.id.editor_song_list);
                share_song_list = inflate.findViewById(R.id.share_song_list);
                public_song_list = inflate.findViewById(R.id.public_song_list);
                delete_song_list = inflate.findViewById(R.id.delete_song_list);
                cancel = inflate.findViewById(R.id.cancel);

                l1 = inflate.findViewById(R.id.l1);
                l2 = inflate.findViewById(R.id.l2);
                l3 = inflate.findViewById(R.id.l3);
                l4 = inflate.findViewById(R.id.l4);
                song_name = inflate.findViewById(R.id.song_name);
                //将布局设置给Dialog
                dialog.setContentView(inflate);
                //获取当前Activity所在的窗体
                Window dialogWindow1 = dialog.getWindow();
                //设置Dialog从窗体底部弹出
                dialogWindow1.setGravity(Gravity.BOTTOM);
                //获得窗体的属性
                WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();
                lp1.y = 2;//设置Dialog距离底部的距离
                wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display d1 = wm.getDefaultDisplay(); // 获取屏幕宽、高度
                WindowManager.LayoutParams p1 = dialogWindow1.getAttributes(); // 获取对话框当前的参数值
                p1.height = (int) (d1.getHeight() * 0.45); // 高度设置为屏幕的0.6，根据实际情况调整
                p1.width = (int) (d1.getWidth() * 1); // 宽度设置为屏幕的0.65，根据实际情况调整
                //    将属性设置给窗体
                dialogWindow1.setAttributes(lp1);
                dialog.show();//显示对话框



                if(item.getType()==1){
                    editor_song_list.setVisibility(View.GONE);
                    public_song_list.setVisibility(View.GONE);
                    delete_song_list.setVisibility(View.GONE);
                    l1.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                    l4.setVisibility(View.GONE);
                }else {

                }

                if(item.getIs_private()==1){
                    public_song_list.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.VISIBLE);
                }else {

                }

                //判斷是我创建的歌单还是我收藏的歌单
                if(TextUtils.equals("2", type)){
                    editor_song_list.setVisibility(View.GONE);
                    l1.setVisibility(View.GONE);
                }else {

                }


                song_name.setText(item.getTitle());
                player_all_music.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MobclickAgent.onEvent(mContext, "album_detail_play_all");
                        PlayCtrlUtil.INSTANCE.playSheet(mContext, item.getId()+"");
//                        if (isPlaying) {
//                            imgAllPlaying.setImageResource(R.drawable.song_detail_play_false);
//                            isPlaying = false;
//                        } else {
//                            imgAllPlaying.setImageResource(R.drawable.song_detail_play_true);
//                            isPlaying = true;
//                        }
                    }
                });
                editor_song_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        intent.setClass(mContext, EditSongActivity.class);
                        bundle.putString("biaoshi", 2+"");
                        bundle.putSerializable("data", item);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                        dialog.cancel();
                    }
                });
                share_song_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MobclickAgent.onEvent(mContext, "album_detail_more");
                        if (null == item) {
                            return;
                        }
                        MusicBean.ShareDataBean shareDataBean = new MusicBean.ShareDataBean();
                        shareDataBean.setType("album");
                        shareDataBean.setNickname(item.getNickname());
                        shareDataBean.setTopicContent(item.getNickname());
                        shareDataBean.setTitle(item.getTitle() + "");
                        shareDataBean.setMuisic_id(item.getId());
                        try {
                            shareDataBean.setWebImgUrl(item.getImgpic_info().getLink());
                        } catch (RuntimeException e) {
                        }

                        String shareUrl = "https://www.yuanyintang.com/mlist/" + item.getId();//第三方分享的歌单链接
                        shareDataBean.setShareUrl(shareUrl);
                        MusicBean musicBean = new MusicBean()
                                .setMusic_id(item.getId() + "")
                                .setMusician_name(item.getNickname())
                                .setMusic_name(item.getTitle())
                                .setSongName(item.getTitle())
                                .setUid(item.getUid())
                                //.setCommentNum(item.getComment())
                                .setImgpic(item.getImgpic())
                                //.setSignature(item.getSignature())
                                .setSong_id(item.getId())
                                //.setReportType(2).setCanEdit(canEdit)
                                .setReportId(item.getId())
                                .setShareDataBean(shareDataBean)//歌单分享bean
                                .setType(2);
                        try {
                            String link = item.getImgpic_info().getLink();
                            MusicBean.ImgpicInfoBean imgpicInfoBean = new MusicBean.ImgpicInfoBean();
                            imgpicInfoBean.setLink(link);
                            musicBean.setImgpic_info(imgpicInfoBean);

                        } catch (RuntimeException e) {
                        }
                        ShareBottomDialog shareBottomDialog = new ShareBottomDialog(mContext, musicBean);
                        dialog.cancel();
                        shareBottomDialog.show();
                    }
                });
                public_song_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        NetWork.INSTANCE.publicSongList(mContext, item.getId()+"",new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
                                Message message = Message.obtain();
                                message.what = 1;
                                Bundle bundle = new Bundle();
                                bundle.putInt("position", position);
                                message.setData(bundle);
                                mhandler.sendMessage(message);
                            }

                            @Override
                            public void doError(String msg) { }

                            @Override
                            public void doResult() {}
                        });
                        dialog.cancel();
                    }
                });
                delete_song_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BaseConfirmDialog.Companion.newInstance()
                                .confirmText("删除")
                                .cancleText("取消")
                                .title("提示")
                                .content("确定要删除歌单吗？")
                                .isShowYxy(false)
                                .showDialog(mContext, new BaseConfirmDialog.onBtClick() {
                                    @Override
                                    public void onConfirm() {

                                        //判斷是我创建的歌单还是我收藏的歌单
                                        if(TextUtils.equals("2", type)){
                                            NetWork.INSTANCE.getSongCollect(mContext, item.getId()+"",new NetWork.TokenCallBack() {
                                                @Override
                                                public void doNext(String resultData, Headers headers) {
                                                    Message message = Message.obtain();
                                                    message.what = 1;
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("position", position);
                                                    message.setData(bundle);
                                                    mhandler.sendMessage(message);
                                                }

                                                @Override
                                                public void doError(String msg) {

                                                }

                                                @Override
                                                public void doResult() {

                                                }
                                            });
                                        }else {
                                            NetWork.INSTANCE.delCollectionSong(mContext, item.getId()+"",new NetWork.TokenCallBack() {
                                                @Override
                                                public void doNext(String resultData, Headers headers) {
                                                    Message message = Message.obtain();
                                                    message.what = 1;
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt("position", position);
                                                    message.setData(bundle);
                                                    mhandler.sendMessage(message);
                                                }

                                                @Override
                                                public void doError(String msg) {

                                                }

                                                @Override
                                                public void doResult() {

                                                }
                                            });
                                        }

                                    }

                                    @Override
                                    public void onCancle() {

                                    }
                                });
                        dialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });

            }
        });

        Log.e("hhhhh",""+item.getStatus());
        if((type.equals("2") || type == "2") && item.getStatus() ==0){
            helper.getView(R.id.private_img).setVisibility(View.VISIBLE);
            helper.setTextColor(R.id.tv_music_name, R.color.base_red);
            helper.setTextColor(R.id.tv_num, R.color.base_red);
        }else  if((type.equals("2") || type == "2") && item.getIs_private()==1){
            helper.getView(R.id.private_img).setVisibility(View.VISIBLE);
//            ImageLoader.with(mContext).res(R.mipmap.img_mine_privacy_song).into(helper.getView(R.id.private_img));
            helper.setTextColor(R.id.tv_num, R.color.base_red);
            helper.setTextColor(R.id.tv_music_name, R.color.base_red);
        }else if(item.getIs_private()==1){
            helper.getView(R.id.private_img).setVisibility(View.VISIBLE);
            ImageLoader.with(mContext).res(R.mipmap.img_mine_privacy_song).into(helper.getView(R.id.private_img));
            helper.setTextColor(R.id.tv_music_name, R.color.base_red);
            helper.setTextColor(R.id.tv_num, R.color.base_red);
        }else {
            helper.getView(R.id.private_img).setVisibility(View.GONE);
        }
//        if (item.getStatu()==1){
//            helper.setTextColor(R.id.tv_music_name, R.color.base_red);
//            helper.setTextColor(R.id.tv_num, R.color.base_red);
//        }

        if (item.getImgpic_info() != null) {
            ImageLoader.with(mContext).url(item.getImgpic_info().getLink()).placeHolder(R.drawable.img_mine_ilike_moren3).getSize(200, 200).into(helper.getView(R.id.cimg_cover));
        }else{
            ImageLoader.with(mContext).res(R.drawable.img_mine_ilike_moren3).into(helper.getView(R.id.cimg_cover));
        }
        if(item.getType()==1){
            helper.getView(R.id.cimg_cover_love).setVisibility(View.VISIBLE);
            ImageLoader.with(mContext).res(R.drawable.icon_index_likesong).into(helper.getView(R.id.cimg_cover_love));
        }


        helper.setOnClickListener(R.id.llItem, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((type.equals("2") || type == "2")&&item.getStatus()==0||(type.equals("2") || type == "2") && item.getIs_private()==1){
                    NetWork.INSTANCE.getSongSheetDetails("songDetails_"+item.getId(),mContext, ""+item.getId(), new NetWork.TokenCallBack() {
                        @Override
                        public void doNext(String resultData, Headers headers) {
                            //                    Message message = Message.obtain();
                            //                    message.what = 10;
                            //                    Bundle bundle = new Bundle();
                            //                    bundle.putInt("position", resultData);
                            //                    message.setData(bundle);
                            //                    mhandler.sendMessage(message);
                        }

                        @Override
                        public void doError(String msg) {

                        }

                        @Override
                        public void doResult() {

                        }
                    });
                }else {
                    MobclickAgent.onEvent(mContext,"musician_go_album");
                    Intent intent = new Intent(mContext, SongSheetDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, item.getId() + "");
                    bundle.putString("type",type);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }

            }
        });



    }
}
