package com.mxkj.yuanyintang.mainui.myself.apdater;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
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
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.mainui.comment.CommentActivity;
import com.mxkj.yuanyintang.mainui.home.activity.ContainSongListActivity;
import com.mxkj.yuanyintang.mainui.message.adapter.MyCollectionSongListAdapter;
import com.mxkj.yuanyintang.mainui.myself.activity.EditSongActivity;
import com.mxkj.yuanyintang.mainui.myself.bean.MyReleaseBean;
import com.mxkj.yuanyintang.mainui.myself.bean.MySongListBean;
import com.mxkj.yuanyintang.mainui.myself.my_release.MyReleaseActivity;
import com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.UploadMusicActivity;
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.video.MvVideoActivity;
import com.mxkj.yuanyintang.video.MvVideoActivitykt;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.UploadMusicActivity.DATA;

/**
 * Created by LiuJie on 2017/10/24.
 */

public class MyReleaseAdapterNew extends BaseMultiItemQuickAdapter<MyReleaseBean.DataBean, BaseViewHolder> {

    WindowManager wm;
    ProgressBar progressbar;
    View inflate;
    Dialog dialog;
    RelativeLayout player_all_music,contains_song_list,next_song_list,add_song_list,download_song_list,comments_song_list,
            share_song_list,editor_song_list,apply_for_offline,cancel,
            delete_song_list,back_song_list;

    TextView song_name,tv_canclel;

    RecyclerView recyclerviewlist;
    LinearLayout layout_new_songlist;
    ArrayList<MySongListBean.DataBeanX.DataBean> dataBeanList = new ArrayList();
    MyCollectionSongListAdapter myColloectionSongListAdapter;
    private Handler mhandler;
    public MyReleaseAdapterNew(List<MyReleaseBean.DataBean> data,Handler mhandler) {
        super(data);
        addItemType(-1,R.layout.item_my_release);
        addItemType(10,R.layout.item_my_release);
        addItemType(11,R.layout.item_my_release);
        addItemType(1,R.layout.item_my_release);
        addItemType(20,R.layout.item_my_release);
        addItemType(21,R.layout.item_my_release);
        addItemType(22,R.layout.item_my_release);
        addItemType(2,R.layout.item_my_release);
        addItemType(4,R.layout.item_my_release);

        this.mhandler = mhandler;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MyReleaseBean.DataBean item) {

        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_label, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_nickname, StringUtils.isEmpty(item.getNickname()));
        helper.setText(R.id.tv_time, StringUtils.isEmpty(item.getUpdate_time()));
        try{
            ImageLoader.with(mContext)
                    .getSize(200,200)

                    .override(50, 50)
                    .url(item.getImgpic_info().getLink())
                    .scale(ScaleMode.CENTER_CROP)
                    .rectRoundCorner(4)
                    .into(helper.getView(R.id.iv_headImg));
        }catch (RuntimeException e){}



        //全部（无参数） 1代表审核中 2代表审核通过 -1代表审核失败 0代表下线 4代表草稿
        if (item.getAdmin_status() == -1) {
            helper.setText(R.id.tv_label, "未上线");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_orange_f3_bg_orange_8a_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(mContext, R.color.orange_8a));
        } else if (item.getAdmin_status() == 0) {
            helper.setText(R.id.tv_label, "未上线");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_orange_f3_bg_orange_8a_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(mContext, R.color.orange_8a));
        } else if (item.getAdmin_status() == 2) {
            helper.setText(R.id.tv_label, "已上线");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_blue_f9_bg_blue_c4_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(mContext, R.color.blue_c4));

            //这里把数据拿过来判断有没有mv。有就吧mv的图标显示出来，没有就不显示
            if(item.getMv().equals("")){
                helper.setVisible(R.id.mv_id,false);
            }else {
                helper.setVisible(R.id.mv_id,true);
            }



        } else if (item.getAdmin_status() == 1) {
            helper.setText(R.id.tv_label, "审核中");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_green_fa_bg_green_d1_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(mContext, R.color.green_d1));
        } else if (item.getAdmin_status() == 4) {
            helper.setText(R.id.tv_label, "草稿");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_green_fa_bg_green_cd_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(mContext, R.color.green_cd));
        } else if (item.getAdmin_status() == 1&&item.getStatus()==1) {
            helper.setText(R.id.tv_label, "修改中");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_3dp_green_fa_bg_green_cd_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(mContext, R.color.green_cd));
        }
        RxView.clicks(helper.getView(R.id.layout_click)).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                if (item.getStatus() == 1) {
//                    Intent intent = new Intent(mContext, MusicDetailsActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString(MusicDetailsActivity.MUSIC_ID, item.getId() + "");
//                    intent.putExtras(bundle);
//                    mContext.startActivity(intent);
                    PlayCtrlUtil.INSTANCE.play(mContext,item.getId(),0);
                }
            }
        });

        RxView.clicks(helper.getView(R.id.mv_id)).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Intent intent = new Intent(mContext,MvVideoActivitykt.class);
                Bundle bundle = new Bundle();
                bundle.putInt("mymv", item.getId());
                bundle.putString("mvurl", item.getMv_info().get(0).getLink());
                bundle.putInt("uid", item.getUid());
                Log.i("msg",""+item.getMv_info().get(0).getLink());
                bundle.putString("title", item.getTitle());
                bundle.putString("nickname", item.getNickname());
                bundle.putString("imgpic_link", item.getImgpic_info().getLink());
                bundle.putString("bioashi",2+"");
                intent.putExtra("mvdate",bundle);
                mContext.startActivity(intent);
            }
        });

        helper.setOnClickListener(R.id.my_works_more, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
                //填充对话框的布局
                if(item.getAdmin_status() == 2){
                    inflate = LayoutInflater.from(mContext).inflate(R.layout.my_works_more_dialog, null);
                    //初始化控件


                    contains_song_list = inflate.findViewById(R.id.contains_song_list);//包含这首歌的歌单
                    next_song_list = inflate.findViewById(R.id.next_song_list);//下一首
                    add_song_list = inflate.findViewById(R.id.add_song_list);//添加到
                    download_song_list = inflate.findViewById(R.id.download_song_list);//下载
                    comments_song_list = inflate.findViewById(R.id.comments_song_list);//评论
                    share_song_list = inflate.findViewById(R.id.share_song_list);//分享

                    apply_for_offline = inflate.findViewById(R.id.apply_for_offline);//申请下线

                } else if(item.getAdmin_status() == 1){
                    inflate = LayoutInflater.from(mContext).inflate(R.layout.my_works_more_dialog_two, null);
                    //初始化控件
//                    delete_song_list = inflate.findViewById(R.id.delete_song_list);//删除
                    back_song_list  = inflate.findViewById(R.id.back_song_list);//撤回申请
                }else if (item.getAdmin_status() == 4 ||item.getAdmin_status() == 0||item.getAdmin_status() == -1){//草稿和没有上线的
                    inflate = LayoutInflater.from(mContext).inflate(R.layout.my_works_more_dialog_three, null);
                    //初始化控件
                    delete_song_list = inflate.findViewById(R.id.delete_song_list);//删除
                }
                song_name = inflate.findViewById(R.id.song_name);//歌曲名
                player_all_music = inflate.findViewById(R.id.player_all_music);//播放
                editor_song_list = inflate.findViewById(R.id.editor_song_list);//编辑
                cancel = inflate.findViewById(R.id.cancel);//取消
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
                if(item.getAdmin_status() == 2){
                    p1.height = (int) (d1.getHeight() * 0.7); // 高度设置为屏幕的0.6，根据实际情况调整
                }else if (item.getAdmin_status() == 1){
                    p1.height = (int) (d1.getHeight() * 0.3); // 高度设置为屏幕的0.6，根据实际情况调整
                }else if (item.getAdmin_status() == 4){
                    p1.height = (int) (d1.getHeight() * 0.28); // 高度设置为屏幕的0.6，根据实际情况调整
                }
                p1.width = (int) (d1.getWidth() * 1); // 宽度设置为屏幕的0.65，根据实际情况调整
                //    将属性设置给窗体
                dialogWindow1.setAttributes(lp1);
                dialog.show();//显示对话框

                song_name.setText(item.getTitle());
                if(item.getAdmin_status() == 2){
                    contains_song_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//包含这首歌的歌单
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            intent.setClass(mContext, ContainSongListActivity.class);
                            bundle.putString(ContainSongListActivity.MUSIC_ID, StringUtils.isEmpty(item.getId()+""));
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                            dialog.cancel();
                        }
                    });//包含这首歌的歌单
                    next_song_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//下一首
                            PlayCtrlUtil.nextPlayIntent(mContext, Integer.parseInt(item.getId()+""));
                            dialog.cancel();
                        }
                    });
                    add_song_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//添加到
                            //先关闭原来的弹窗在打开新的弹窗
                            dialog.cancel();
                            dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
                            //填充对话框的布局
                            inflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_newsonglist, null);
                            //初始化控件
                            recyclerviewlist = inflate.findViewById(R.id.recyclerviewlist);
                            tv_canclel = inflate.findViewById(R.id.tv_canclel);
                            layout_new_songlist = inflate.findViewById(R.id.layout_new_songlist);
                            tv_canclel.setText("确定");

                            //将布局设置给Dialog
                            dialog.setContentView(inflate);
                            //获取当前Activity所在的窗体
                            Window dialogWindow1 = dialog.getWindow();
                            //设置Dialog从窗体底部弹出
                            dialogWindow1.setGravity(Gravity.BOTTOM);
                            //获得窗体的属性
                            WindowManager.LayoutParams lp1 = dialogWindow1.getAttributes();
                            lp1.y = 5;//设置Dialog距离底部的距离
                            wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                            Display d1 = wm.getDefaultDisplay(); // 获取屏幕宽、高度
                            WindowManager.LayoutParams p1 = dialogWindow1.getAttributes(); // 获取对话框当前的参数值
                            p1.height = (int) (d1.getHeight() * 0.6); // 高度设置为屏幕的0.6，根据实际情况调整
                            p1.width = (int) (d1.getWidth() * 1); // 宽度设置为屏幕的0.65，根据实际情况调整
                            //    将属性设置给窗体
                            dialogWindow1.setAttributes(lp1);
                            dialog.show();//显示对话框

//                            NetWork.INSTANCE.getMemberSong(mContext, 1, new NetWork.TokenCallBack() {
//                                @Override
//                                public void doNext(String resultData, Headers headers) {
//                                    MySongListBean mySongListBean = JSON.parseObject(resultData, MySongListBean.class);
//                                    //refreshData(mySongListBean)
//                                    dataBeanList.addAll(mySongListBean.getData().getData());
//                                    //myColloectionSongListAdapter.notifyDataSetChanged()
//
//                                    recyclerviewlist.layoutManager = LinearLayoutManager(mContext);
//                                    myColloectionSongListAdapter = new MyCollectionSongListAdapter(dataBeanList, false, "");
//                                    recyclerviewlist.setAdapter(myColloectionSongListAdapter);
//                                    myColloectionSongListAdapter.setCheckedSongListener();
//                                    myColloectionSongListAdapter.setCheckedSongListener { position ->
//                                        for (i in dataBeanList.indices) {
//                                            if (i == position) {
//                                                dataBeanList[i].check = true
//                                            } else {
//                                                dataBeanList[i].check = false
//                                            }
//                                        }
//                                        myColloectionSongListAdapter.notifyDataSetChanged()
//                                    }
//                                }
//
//                                @Override
//                                public void doError(String msg) {
//
//                                }
//
//                                @Override
//                                public void doResult() {
//
//                                }
//                            });
                        }
                    });
                    download_song_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//下载
                            NetWork.INSTANCE.getDownBit(mContext, item.getId()+"", new NetWork.TokenCallBack() {
                                @Override
                                public void doNext(String resultData, Headers headers) {
                                    Log.e("gggg",""+resultData.toString());
                                    Message message = Message.obtain();
                                    message.what = 1;
                                    mhandler.sendMessage(message);
                                }

                                @Override
                                public void doError(String msg) {

                                }

                                @Override
                                public void doResult() {

                                }
                            });
                            dialog.cancel();
                        }
                    });
                    comments_song_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//评论
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            intent.setClass(mContext, CommentActivity.class);
                            bundle.putInt(CommentActivity.TYPE, 1);
                            bundle.putInt(CommentActivity.ITEM_ID,item.getId());
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                            dialog.cancel();
                        }
                    });//评论
                    share_song_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//分享
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
                    });//分享
                    apply_for_offline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {//申请下线
                            final BaseConfirmDialog baseConfirmDialog = BaseConfirmDialog.Companion.newInstance()
                                    .confirmText("申请下线")
                                    .cancleText("手滑了")
                                    .title("申请歌曲下线")
                                    .content("确定要申请《" + item.getTitle() + "》下线？")
                                    .tips("歌曲下线后，该音乐将无法显示")
                                    .isShowEdittext(true);
                            baseConfirmDialog.showDialog(mContext, new BaseConfirmDialog.onBtClick() {
                                @Override
                                public void onConfirm() {
                                    if (TextUtils.isEmpty(baseConfirmDialog.getEt_reason().getText())) {
//                                        setSnackBar("请输入申请下线原因", "", R.drawable.icon_fails);
                                        return;
                                    }
                                    HttpParams params = new HttpParams();
                                    params.put("id", item.getId() + "");
                                    params.put("reason", baseConfirmDialog.getEt_reason().getText().toString());
                                    //showLoadingView();
                                    NetWork.INSTANCE.applyDownLine(mContext, params, new NetWork.TokenCallBack() {
                                        @Override
                                        public void doNext(String resultData, Headers headers) {
//                                            setSnackBar("申请成功~", "", R.drawable.icon_success);
                                            Log.e("gggg",""+resultData.toString());
                                            Message message = Message.obtain();
                                            message.what = 1;
                                            mhandler.sendMessage(message);
                                            baseConfirmDialog.dismiss();
                                        }

                                        @Override
                                        public void doError(String msg) {

                                        }

                                        @Override
                                        public void doResult() {

                                        }
                                    });
                                }

                                @Override
                                public void onCancle() {

                                }
                            });
                            dialog.cancel();
                        }
                    });//申请下线
                }else if (item.getAdmin_status() == 1){//审核中的
                    back_song_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BaseConfirmDialog.Companion.newInstance()
                                    .confirmText("撤回申请")
                                    .cancleText("手滑了")
                                    .title("撤销申请")
                                    .content("确定要撤回《" + item.getTitle() + "》？")
                                    .tips("源小伊正在审核您的作品,撤销后将终止审核")
                                    .showDialog(mContext, new BaseConfirmDialog.onBtClick() {
                                        @Override
                                        public void onConfirm() {
                                            NetWork.INSTANCE.backRelease(mContext, new HttpParams("id", item.getId() + ""), new NetWork.TokenCallBack() {
                                                @Override
                                                public void doNext(String resultData, Headers headers) {
                                                    Message message = Message.obtain();
                                                    message.what = 1;
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

                                        @Override
                                        public void onCancle() {

                                        }
                                    });
                            dialog.cancel();
                        }
                    });
                }else if (item.getAdmin_status() == 4 ||item.getAdmin_status() == 0||item.getAdmin_status() == -1){//草稿和没有上线的
                    delete_song_list.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BaseConfirmDialog.Companion.newInstance()
                                    .confirmText("删除")
                                    .cancleText("手滑了")
                                    .title("删除歌曲")
                                    .content("确定要删除《" + item.getTitle() + "》？")
                                    .tips("删除后该条歌曲消失，且不可恢复")
                                    .showDialog(mContext, new BaseConfirmDialog.onBtClick() {
                                        @Override
                                        public void onConfirm() {
                                            NetWork.INSTANCE.deleRelease(mContext, new HttpParams("id", item.getId() + ""), new NetWork.TokenCallBack() {
                                                @Override
                                                public void doNext(String resultData, Headers headers) {
                                                    Log.e("gggg",""+resultData.toString());
//                                                    notifyDataSetChanged();
                                                    Message message = Message.obtain();
                                                    message.what = 1;
//                                                    Bundle bundle = new Bundle();
//                                                    bundle.putInt("position", position);
//                                                    message.setData(bundle);
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

                                        @Override
                                        public void onCancle() {

                                        }
                                    });
                            dialog.cancel();
                        }
                    });
                }

                player_all_music.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PlayCtrlUtil.INSTANCE.play(mContext,item.getId(),0);
                    }
                });
                //编辑
                editor_song_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        intent.setClass(mContext, UploadMusicActivity.class);
                        bundle.putSerializable(DATA, item.getId());
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
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



    }
}
