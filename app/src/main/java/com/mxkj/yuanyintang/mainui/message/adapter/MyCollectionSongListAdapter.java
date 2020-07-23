package com.mxkj.yuanyintang.mainui.message.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.myself.bean.MySongListBean;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;

/**
 * Created by LiuJie on 2017/10/16.
 */

public class MyCollectionSongListAdapter extends BaseQuickAdapter<MySongListBean.DataBeanX.DataBean, BaseViewHolder> {

    private Boolean isMultiSelect = false;
    private String view_type;

    public MyCollectionSongListAdapter(List<MySongListBean.DataBeanX.DataBean> data, Boolean isMultiSelect, String view_type) {
        super(R.layout.message_item_select_song, data);
        this.isMultiSelect = isMultiSelect;
        this.view_type = view_type;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MySongListBean.DataBeanX.DataBean item, final int position) {
        helper.setText(R.id.tv_name, StringUtils.isEmpty(item.getTitle()));
        if (item.getTotal() > 199) {
            helper.setBackgroundColor(R.id.layout_click, ContextCompat.getColor(mContext, R.color.bg_color));
            helper.setVisible(R.id.check_song, false);
        } else {
            helper.setBackgroundColor(R.id.layout_click, ContextCompat.getColor(mContext, R.color.transparent));
            helper.setVisible(R.id.check_song, true);
        }
        if (TextUtils.isEmpty(view_type)) {
            helper.setText(R.id.tv_play_num, StringUtils.setNum(item.getCounts()));
            helper.setText(R.id.tv_music_num, StringUtils.setNum(item.getTotal()));
        } else {
            helper.setVisible(R.id.tv_song_num, true);
            helper.setVisible(R.id.tv_play_num, false);
            helper.setVisible(R.id.tv_music_num, false);
            helper.setText(R.id.tv_song_num, StringUtils.setNum(item.getTotal()) + "首");
        }
        if (item.getImgpic_info()!=null&&!TextUtils.isEmpty(item.getImgpic_info().getLink())) {
            ImageLoader.with(mContext).url(item.getImgpic_info().getLink())
//                    .asCircle()
//                    .scale(ScaleMode.CENTER_CROP)
                    .into(helper.getView(R.id.civ_headimg));
        } else {
            ImageLoader.with(mContext).res(R.mipmap.img_defaut_head)
//                    .asCircle()
//                    .scale(ScaleMode.CENTER_CROP)
                    .into(helper.getView(R.id.civ_headimg));
        }
        if (isMultiSelect) {
            helper.setOnCheckedChangeListener(R.id.check_song, new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    item.setCheck(b);
                }
            });
        } else {
            helper.setChecked(R.id.check_song, item.getCheck());
            helper.getView(R.id.check_song).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != checkedSongListener) {
                        checkedSongListener.onChecked(position);
                    }
                }
            });
        }
    }

    /**
     * 跳转页面
     *
     * @param mClass 目标页面
     */
    protected void goActivity(Class<?> mClass) {
        goActivity(mClass, null);
    }

    /**
     * 跳转页面带参数
     *
     * @param mClass 目标页面
     * @param bundle 参数
     */
    protected void goActivity(Class<?> mClass, Bundle bundle) {
        Intent intent = new Intent(mContext, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }

    public interface ClickCheckedSongListener {
        void onChecked(int position);
    }

    private ClickCheckedSongListener checkedSongListener;

    public void setCheckedSongListener(ClickCheckedSongListener checkedSongListener) {
        this.checkedSongListener = checkedSongListener;
    }

}
