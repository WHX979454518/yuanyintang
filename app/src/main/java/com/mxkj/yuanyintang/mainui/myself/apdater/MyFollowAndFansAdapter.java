package com.mxkj.yuanyintang.mainui.myself.apdater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.jakewharton.rxbinding2.view.RxView;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.BaseActivity;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.mainui.myself.bean.MyFollowAndFansBean;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.Toast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

/**
 * Created by LiuJie on 2017/10/9.
 */

public class MyFollowAndFansAdapter extends BaseQuickAdapter<MyFollowAndFansBean.DataBean, BaseViewHolder> {

    List<MyFollowAndFansBean.DataBean> dataList = new ArrayList<>();

    public MyFollowAndFansAdapter(List<MyFollowAndFansBean.DataBean> data) {
        super(R.layout.item_my_follow_and_fans);
        this.dataList = data;
        setNewData(dataList);
    }


    @Override
    protected void convert(BaseViewHolder helper, final MyFollowAndFansBean.DataBean item, final int position) {
        ImageLoader.with(mContext)
                .getSize(200,200)

                .override(50, 50)
                .url(item.getHead_link())
                .into(helper.<CircleImageView>getView(R.id.civ_headimg));
        helper.setText(R.id.tv_name, StringUtils.isEmpty(item.getNickname()));
        helper.setText(R.id.tv_sign, StringUtils.isEmpty(item.getSignature()));
        if (TextUtils.equals("1", item.getSex())) {
            helper.<ImageView>getView(R.id.iv_sex).setImageResource(R.drawable.icon_male);
            helper.setBackgroundRes(R.id.iv_sex, R.drawable.oval_3dp_blue_60_bg);
            helper.setVisible(R.id.iv_sex, true);
        } else if (TextUtils.equals("0", item.getSex())) {
            helper.<ImageView>getView(R.id.iv_sex).setImageResource(R.drawable.icon_female);
            helper.setBackgroundRes(R.id.iv_sex, R.drawable.oval_3dp_pink_99_bg);
            helper.setVisible(R.id.iv_sex, true);
        } else {
            helper.setVisible(R.id.iv_sex, false);
        }

        if (item.getIs_music() == 3) {
            helper.setVisible(R.id.iv_is_vip, true);
        } else {
            helper.setVisible(R.id.iv_is_vip, false);
        }
        switch (item.getIs_relation()) {
            case 0:
                helper.setText(R.id.tv_follow, "+关注");
                helper.<TextView>getView(R.id.tv_follow).setTextColor(ContextCompat.getColor(mContext, R.color.base_red));
                helper.<TextView>getView(R.id.tv_follow).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_25dp_transparent_bg_red_66_line));
                break;
            case 1:
                helper.setText(R.id.tv_follow, "已关注");
                helper.<TextView>getView(R.id.tv_follow).setTextColor(ContextCompat.getColor(mContext, R.color.grey_a6_text));
                helper.<TextView>getView(R.id.tv_follow).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_25dp_transparent_bg_eb_line));
                break;
            case 2:
                helper.setText(R.id.tv_follow, "互相关注");
                helper.<TextView>getView(R.id.tv_follow).setTextColor(ContextCompat.getColor(mContext, R.color.grey_a6_text));
                helper.<TextView>getView(R.id.tv_follow).setBackground(ContextCompat.getDrawable(mContext, R.drawable.oval_25dp_transparent_bg_eb_line));
                break;
        }
        RxView.clicks(helper.getView(R.id.tv_follow))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (item.getIs_relation() == 1) {
                            final MaterialDialog dialog = new MaterialDialog(mContext);
                            dialog.content(
                                    "是否取消关注？")//
                                    .btnText("取消", "确定")//
                                    .titleTextSize(16)
                                    .titleTextColor(ContextCompat.getColor(mContext, R.color.color_14_text))
                                    .contentTextColor(ContextCompat.getColor(mContext, R.color.color_66_text))
                                    .contentTextSize(14)
                                    .btnTextSize(14)
                                    .btnTextColor(ContextCompat.getColor(mContext, R.color.base_red)
                                            , ContextCompat.getColor(mContext, R.color.base_red))
                                    .showAnim(null)//
                                    .dismissAnim(null)//
                                    .show();

                            dialog.setOnBtnClickL(
                                    new OnBtnClickL() {//left btn click listener
                                        @Override
                                        public void onBtnClick() {
                                            dialog.dismiss();
                                        }
                                    },
                                    new OnBtnClickL() {//right btn click listener
                                        @Override
                                        public void onBtnClick() {
                                            follow(item, position);
                                            dialog.dismiss();
                                        }
                                    }
                            );
                        } else {
                            follow(item, position);

                        }
                    }
                });
        RxView.clicks(helper.getView(R.id.layout_click))
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Intent intent = new Intent(mContext, MusicIanDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, item.getId() + "");
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });
    }

    private void follow(MyFollowAndFansBean.DataBean item, final int position) {
        HttpParams params = new HttpParams();
        params.put("id", item.getId() + "");
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).showLoadingView();
        }
        NetWork.INSTANCE.follow(mContext, params, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                try {
                    org.json.JSONObject object = new org.json.JSONObject(resultData);
                    org.json.JSONObject dataObject = object.optJSONObject("data") == null ? new org.json.JSONObject() : object.optJSONObject("data");
                    int code = dataObject.optInt("code");
                    dataList.get(position).setIs_relation(code);
                    notifyItemChanged(position);
                    if (null != mContext) {
                        if (code == 0) {
                            Toast.create(mContext).show("取消关注成功");
                        } else {
                            Toast.create(mContext).show("关注成功");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mContext instanceof BaseActivity) {
                    ((BaseActivity) mContext).hideLoadingView();
                }
            }

            @Override
            public void doError(String msg) {
                if (mContext instanceof BaseActivity) {
                    ((BaseActivity) mContext).hideLoadingView();
                }
            }

            @Override
            public void doResult() {

            }
        });
    }


}
