package com.mxkj.yuanyintang.mainui.myself.apdater;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.myself.bean.MyPondBean;
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew;
import com.mxkj.yuanyintang.mainui.pond.activity.PublishPond;
import com.mxkj.yuanyintang.mainui.pond.TagDetialPondList;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.widget.MultiLineRadioGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2017/10/10.
 */

public class MyPondAdapter extends BaseQuickAdapter<MyPondBean.DataBean.PondBean, BaseViewHolder> {

    private Context context;

    public MyPondAdapter(List<MyPondBean.DataBean.PondBean> data, Context context) {
        super(R.layout.item_my_pond, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MyPondBean.DataBean.PondBean item, int position) {
        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_time, StringUtils.isEmpty(item.getUpdate_time()));
        // 池塘状态（0关闭，1审核中，2已审核，3草稿箱，-1审核失败）
        if (item.getStatus() == -1) {
            helper.setText(R.id.tv_label, "未上线");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(context, R.drawable.oval_3dp_orange_f3_bg_orange_8a_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(context, R.color.orange_8a));
        } else if (item.getStatus() == 0) {
            helper.setText(R.id.tv_label, "未上线");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(context, R.drawable.oval_3dp_orange_f3_bg_orange_8a_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(context, R.color.orange_8a));
        } else if (item.getStatus() == 1) {
            helper.setText(R.id.tv_label, "审核中");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(context, R.drawable.oval_3dp_blue_f9_bg_blue_c4_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(context, R.color.blue_c4));
        } else if (item.getStatus() == 2) {
            helper.setText(R.id.tv_label, "已上线");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(context, R.drawable.oval_3dp_green_fa_bg_green_d1_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(context, R.color.green_d1));
        } else if (item.getStatus() == 3) {
            helper.setText(R.id.tv_label, "草稿");
            helper.getView(R.id.tv_label).setBackground(ContextCompat.getDrawable(context, R.drawable.oval_3dp_green_fa_bg_green_cd_line));
            helper.<TextView>getView(R.id.tv_label).setTextColor(ContextCompat.getColor(context, R.color.green_cd));
        }
        if (null != item.getHashtag() && item.getHashtag().size() > 0) {
            final List<String> strings = new ArrayList<>();
            strings.clear();
            for (int i = 0; i < item.getHashtag().size(); i++) {
                strings.add(item.getHashtag().get(i).getTitle());
            }
            helper.<MultiLineRadioGroup>getView(R.id.pond_tag).addAll(strings);
            helper.<MultiLineRadioGroup>getView(R.id.pond_tag).setOnCheckChangedListener(new MultiLineRadioGroup.OnCheckedChangedListener() {
                @Override
                public void onItemChecked(MultiLineRadioGroup group, int position, boolean checked) {
                    Intent intent = new Intent(mContext, TagDetialPondList.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(TagDetialPondList.TAG_ID, item.getHashtag().get(position).getId());
                    bundle.putString(TagDetialPondList.TAG_TITLE, item.getHashtag().get(position).getTitle());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }

        RxView.clicks(helper.getView(R.id.layout_click))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (item.getStatus() == 2) {
                            Intent intent = new Intent(mContext, PondDetialActivityNew.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt(PondDetialActivityNew.PID, item.getId());
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, PublishPond.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(PublishPond.DATA, item);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                        }
                    }
                });
    }
}
