package com.mxkj.yuanyintang.mainui.message.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.comment.CommentActivity;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.mainui.home.activity.MyCollectionSongActivity;
import com.mxkj.yuanyintang.mainui.home.data.Constant;
import com.mxkj.yuanyintang.mainui.message.bean.MsgListean;
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity;
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew;
import com.mxkj.yuanyintang.mainui.pond.activity.PondReplyDetialActivity;
import com.mxkj.yuanyintang.mainui.web.CommonWebview;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.utils.app.TimeUtils;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;

import static com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM;
import static com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.MUSICIAN_ID;
import static com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity.SONG_SHEET_ID;
import static com.mxkj.yuanyintang.mainui.pond.activity.PondReplyDetialActivity.POND_COMMENTID;

/**
 * Created by LiuJie on 2018/3/8.
 */

public class BroadcastMsgListListAdapter extends BaseMultiItemQuickAdapter<MsgListean.DataBeanX.DataBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BroadcastMsgListListAdapter(List<MsgListean.DataBeanX.DataBean> data) {
        super(data);
        addItemType(Constant.MsgCenterItem.BroadMsgItemType.HAVE_IMG, R.layout.msg_center_broadmsg_have_img);
        addItemType(Constant.MsgCenterItem.BroadMsgItemType.NO_IMG, R.layout.msg_center_broadmsg_no_img);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MsgListean.DataBeanX.DataBean item) {
        if (item.getItemType() != 0 && item.getType() == 1) {
            helper.setText(R.id.creat_time, TimeUtils.timestampToDateChn(item.getCreate_time()));
            helper.setText(R.id.title, item.getTitle());
            helper.setText(R.id.content, item.getBody());
            if (item.getImgpic_info() != null && !TextUtils.isEmpty(item.getImgpic_info().getLink())) {
                ImageLoader.with(mContext).url(item.getImgpic_info().getLink()).into(helper.getView(R.id.img_pic));
            }
            final String urlmodel = item.getUrlmodel();
            final String app_url = item.getApp_url();
            final String title = item.getTitle();
            final String body = item.getBody();
            final int from_id = item.getFrom_id();

            if (TextUtils.isEmpty(urlmodel) || TextUtils.isEmpty(app_url)) {
                helper.getView(R.id.ll_go).setVisibility(View.GONE);
            } else {

                helper.getView(R.id.ll_go).setVisibility(View.VISIBLE);
            }


            helper.setOnClickListener(R.id.ll_to_detial, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (urlmodel) {
                        case "activity":
                            Intent intent = new Intent(mContext, CommonWebview.class);
                            intent.putExtra("url", app_url);
                            intent.putExtra("activity", "activity");
                            intent.putExtra("title", StringUtils.isEmpty(title));
                            intent.putExtra("content", StringUtils.isEmpty(body));
                            mContext.startActivity(intent);
                            break;
                        case "musicDetails":
                            toMusic(from_id);
                            break;
                        case "songSheetDetails":
                            toAlbum(from_id);
                            break;
                        case "musicianDetailHome":
                            toUser(from_id, 0);
                            break;
                        case "musicianDetailMusic":
                            toUser(from_id, 1);
                            break;
                        case "musicianDetailSongSheet":
                            toUser(from_id, 2);
                            break;
                        case "musicianDetailTopic":
                            toUser(from_id, 3);
                            break;
                        case "likesSongSheetDetails":
                            Intent intentCollect = new Intent(mContext, MyCollectionSongActivity.class);
                            intentCollect.putExtra(MUSICIAN_ID, item.getFrom_uid() + "");
                            mContext.startActivity(intentCollect);
                            break;
                        case "topicDetails":
                            toPond(from_id);
                            break;

                    }
                }
            });
        }

    }

    //跳转用户
    private void toUser(int pageIndex, int uid) {
        Intent intent = new Intent(mContext, MusicIanDetailsActivity.class);
        intent.putExtra(MUSICIAN_ID, uid + "");
        intent.putExtra(MUSICIAN_CURRENT_ITEM, pageIndex);
        mContext.startActivity(intent);
    }

    //跳转音乐
    private void toMusic(int musicId) {
//        Intent intent = new Intent(mContext, MusicDetailsActivity.class);
//        intent.putExtra(MUSIC_ID, musicId + "");
//        mContext.startActivity(intent);
        PlayCtrlUtil.INSTANCE.play(mContext,musicId,0);
    }

    //跳转歌单
    private void toAlbum(int albumId) {
        Intent intent_detial = new Intent(mContext, SongSheetDetailsActivity.class);
        intent_detial.putExtra(SONG_SHEET_ID, albumId + "");
        mContext.startActivity(intent_detial);

    }

    //跳转池塘
    private void toPond(int pondId) {
        Intent intent = new Intent(mContext, PondDetialActivityNew.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PondDetialActivityNew.PID, pondId);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    //跳转评论（歌单、歌曲）
    private void toComment(int itemId, int type) {
        Intent intent = new Intent(mContext, CommentActivity.class);
        intent.putExtra(CommentActivity.TYPE, type);
        intent.putExtra(CommentActivity.ITEM_ID, itemId);
        mContext.startActivity(intent);
    }

    //跳转评论（池塘）
    private void toPondComment(int topicReplyId) {
        Intent intent = new Intent(mContext, PondReplyDetialActivity.class);
        intent.putExtra(POND_COMMENTID, topicReplyId);
        mContext.startActivity(intent);
    }

}
