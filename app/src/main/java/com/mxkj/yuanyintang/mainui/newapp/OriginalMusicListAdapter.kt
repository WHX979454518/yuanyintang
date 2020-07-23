package com.mxkj.yuanyintang.mainui.newapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.bean.MusicInfo
import com.mxkj.yuanyintang.extraui.bean.MusicBean
import com.mxkj.yuanyintang.extraui.dialog.MusicOperationDialog
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.Companion.MUSICIAN_CURRENT_ITEM
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity.Companion.MUSICIAN_ID
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity
import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity.SONG_SHEET_ID
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil
import com.mxkj.yuanyintang.musicplayer.service.MediaService
import com.mxkj.yuanyintang.musicplayer.service.MediaService.bean
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.string.StringUtils
import com.mxkj.yuanyintang.video.MvVideoActivitykt
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

class OriginalMusicListAdapter(data: List<HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean>, private val fragManager: android.support.v4.app.FragmentManager) : BaseQuickAdapter<HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean, BaseViewHolder>(R.layout.original_music_item, data) {
    override fun convert(helper: BaseViewHolder, item: HomeBean.TypeMusicListBean.TypeMusicBean.MusicBean) {
        var position = helper.adapterPosition-headerLayoutCount
        val music_type = item.music_type
        if (music_type == 1) {
            helper.getView<TextView>(R.id.tv_type).visibility = VISIBLE
        } else {
            helper.getView<TextView>(R.id.tv_type).visibility = GONE
        }


//        if(item.mv_info==null&&item.mv.equals("")){
//            helper.getView<ImageView>(R.id.original_img).visibility = GONE
//        }else{
//            helper.getView<ImageView>(R.id.original_img).visibility = VISIBLE
//        }
        if (item.mv==null||item.mv.equals("")) {
            helper.getView<ImageView>(R.id.original_img).visibility = GONE
        } else {
            helper.getView<ImageView>(R.id.original_img).visibility = VISIBLE
        }
        Log.i("msgg",""+item.mv)
        RxView.clicks(helper.getView(R.id.original_img))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    toMV(item.id,item.mv_info.link,item.uid,item.title,item.nickname,item.imgpic_link);
                }

        var musicNane = helper.getView<TextView>(R.id.tv_title)
        var nickName = helper.getView<TextView>(R.id.nickName)

        ImageLoader.with(mContext).getSize(150, 150).setUrl(item.imgpic_info).into(helper.getView(R.id.iv_bcg))
        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.title))
        helper.setText(R.id.nickName, StringUtils.isEmpty(item.nickname))



        if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying) {
            if (bean != null && bean?.id == item.id) {
                item.isPlaying = true
                musicNane.setTextColor(Color.parseColor("#ff6699"))
                nickName.setTextColor(Color.parseColor("#ff6699"))
            }
        }


        if (item.isPlaying) {
            musicNane.setTextColor(Color.parseColor("#ff6699"))
            nickName.setTextColor(Color.parseColor("#ff6699"))
        } else {
            musicNane.setTextColor(Color.parseColor("#ff333333"))
            nickName.setTextColor(Color.parseColor("#9da2a6"))
        }

        helper.setText(R.id.tv_counts, item.counts.toString())

        RxView.clicks(helper.getView(R.id.ivMore))
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe {
                    val musicBean = MusicBean()
                            .setMusic_name(item.title)
                            .setCommentNum(item.comment)
                            .setUid(item.uid)
                            .setMusic_id(item.id.toString() + "")
                            .setMusician_name(item.nickname)
                            .setSong_id(item.song_id)
                            .setSongName("")
                            .setCollection(item.is_collection)
                            .setPosition(position)
                            .setType(1)
                            .setReportType(1)
                            .setReportId(item.id)
                            .setCommentType(1)
                            .setMultiSelect(false)
                            .setPlayTime(item.playtime)
                            //新添加的mv
                            .setMv(item.mv)

                    if (item.mv_info != null) {
                        val link = item.mv_info.link
                        val mvInfoBean = MusicBean.MvInfoBean()
                        mvInfoBean.link = link
                        musicBean.mvInfoBean = mvInfoBean
                    }



                    if (item.imgpic_info != null) {
                        val link = item.imgpic_info.link
                        val imgpicInfoBean = MusicBean.ImgpicInfoBean()
                        imgpicInfoBean.link = link
                        musicBean.imgpic_info = imgpicInfoBean
                    }

                    fragManager?.let {
                        val musicOperationDialog = MusicOperationDialog(
                                musicBean, it)
                        musicOperationDialog.show(it, position.toString() + "")
                        musicOperationDialog.setSetMusicOperationListener(object : MusicOperationDialog.SetMusicOperationListener {
                            override fun onCollection(collection: Int, position: Int) {
                                item.is_collection = collection
                                notifyDataSetChanged()
                                musicOperationDialog.dismiss()
                            }

                            override fun getType(type: Int) {

                            }
                        })
                    }

                }
        if (onItemClickListener == null) {
            RxView.clicks(helper.getView(R.id.llToNext))
                    .subscribe {
                        if (bean != null && bean?.id == item.id) {
                            if (MediaService.mediaPlayer != null) {
                                val playing = MediaService.mediaPlayer.isPlaying
                                data[position].isPlaying = !playing
                                mContext.sendBroadcast(Intent(MediaService.ACTION_PAUSE))
                            } else {
                                data[position].isPlaying = true
                            }
                        } else {
                            for (i in 0 until data.size) {
                                if (i == position) {
                                    data[i].isPlaying = true
                                    bean = null
                                } else {
                                    data[i].isPlaying = false
                                }
                            }
                        }
                        if (data[position] != null && data[position].video_info != null && data[position].video_info.link != null) {
                            val file = File(data[position].video_info.link)
                            if (file.exists()) {
                                bean = JSON.parseObject(JSON.toJSONString(data[position]), MusicInfo.DataBean::class.java)
                                PlayCtrlUtil.startServiceToPlay(mContext, bean)
                            } else {
                                PlayCtrlUtil.play(mContext, data[position].id, data[position].song_id)
                            }
                        }

                    }
        }
    }

    //跳转MV
    private fun toMV(id :Int,mvurl :String,uid:Int,title :String,nickname :String,imgpic_link :String) {
        val intent = Intent(mContext, MvVideoActivitykt::class.java)
        var bundle = Bundle()
        bundle.putInt("mv", id);
        bundle.putString("mvurl", mvurl);
        bundle.putInt("uid", uid);

        bundle.putString("title", title);
        bundle.putString("nickname", nickname);
        bundle.putString("imgpic_link", imgpic_link);

        bundle.putString("bioashi", 1.toString() + "")
        intent.putExtra("mvdate",bundle);
        mContext.startActivity(intent)
    }

    //跳转用户
    private fun toUser(pageIndex: Int, uid: Int) {
        val intent = Intent(mContext, MusicIanDetailsActivity::class.java)
        intent.putExtra(MUSICIAN_ID, uid.toString() + "")
        intent.putExtra(MUSICIAN_CURRENT_ITEM, pageIndex)
        mContext.startActivity(intent)
    }

    //跳转音乐
    private fun toMusic(musicId: Int) {
//        val intent = Intent(mContext, MusicDetailsActivity::class.java)
//        intent.putExtra(MUSIC_ID, musicId.toString() + "")
//        mContext.startActivity(intent)
    }

    //跳转歌单
    private fun toAlbum(albumId: Int) {
        val intent_detial = Intent(mContext, SongSheetDetailsActivity::class.java)
        intent_detial.putExtra(SONG_SHEET_ID, albumId.toString() + "")
        mContext.startActivity(intent_detial)
    }

    fun resetPlayStatus() {
        Log.e("TAG", "resetPlayStatus")
        Observable.create(ObservableOnSubscribe<List<Any>> { e ->
            for (i in 0 until data.size) {
                data[i].isPlaying = false
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe {
            setNewData(data)
        }
    }

}