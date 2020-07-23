package com.mxkj.yuanyintang.mainui.home.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.HomeActivity;
import com.mxkj.yuanyintang.base.activity.BaseActivity;
import com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity;
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity;
import com.mxkj.yuanyintang.mainui.home.music_charts.bean.ChartsHomeBean;
import com.mxkj.yuanyintang.mainui.web.CommonWebview;
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.utils.constant.Constants;

import static com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.CHARTS_TYPE;
import static com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.EXPEN_CLASS_ID;
import static com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.INCOME_CLASS_ID;

/**
 * Created by LiuJie on 2017/12/12.
 */

public class HtmlAgencyActivity extends BaseActivity {

    private String type;
    private String url;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html_agency);
        htmlInteractiveApp();
        goActivity(HomeActivity.class);
        finish();
    }

    private void htmlInteractiveApp() {
        final Uri uriData = getIntent().getData();
        if (null != uriData) {
            type = uriData.getQueryParameter("yytType");
            url = uriData.getQueryParameter("url");
            id = uriData.getQueryParameter("id");

            if (TextUtils.isEmpty(id)) {
                id = "0";
            }
            if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(url)) {
                Log.e(TAG, "htmlInteractiveApp: ===" + type + "----" + url + "----" + id);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (type.equals("page")) {
                            Bundle bundle = new Bundle();
                            switch (url) {
                                case "home"://首页（启动app）
                                    break;
                                case "top"://榜单
                                    try {
                                        String classId = uriData.getQueryParameter("class");
                                        String toggleId = uriData.getQueryParameter("toggle_id");
                                        String chartType = uriData.getQueryParameter("billboard_type");
                                        Integer intchartType = Integer.valueOf(chartType);
                                        if (intchartType==3||intchartType==4){
                                            Intent intent = new Intent(HtmlAgencyActivity.this, GiftChartsActivity.class);
                                            if (intchartType == 3) {
                                                intent.putExtra(EXPEN_CLASS_ID, Integer.valueOf(classId));
                                                intent.putExtra(INCOME_CLASS_ID, Integer.valueOf(toggleId));
                                                intent.putExtra(CHARTS_TYPE, 3);
                                            } else if (intchartType == 4) {
                                                intent.putExtra(INCOME_CLASS_ID, Integer.valueOf(classId));
                                                intent.putExtra(EXPEN_CLASS_ID, Integer.valueOf(toggleId));
                                                intent.putExtra(CHARTS_TYPE, 4);
                                            }
                                            startActivity(intent);
                                        }else{
                                            bundle = new Bundle();
                                            ChartsHomeBean.DataBean dataBean = new ChartsHomeBean.DataBean();
//                                  dataBean.setTitle(item.getTitle());
                                            dataBean.setType(Integer.valueOf(chartType));
                                            dataBean.setId(Integer.valueOf(classId));
//                                    if (item.getImgpic_info() != null) {
//                                        dataBean.setImgpic_link(item.getImgpic_info().getLink());
//                                    }
                                            bundle.putSerializable(Constants.Other.CHARTS_BEAN, dataBean);
                                            goActivity(ChartsListsActivity.class, bundle);
                                        }
                                    } catch (RuntimeException e) {
                                        Log.e(TAG, "RuntimeException====: " + e);
                                    }

                                    break;
                                case "topicDetails"://池塘详情
                                    if (!TextUtils.isEmpty(id)) {
                                        bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(id));
                                        goActivity(PondDetialActivityNew.class, bundle);
                                    }
                                    break;
                                case "musicDetails"://音乐详情
//                                    bundle.putString(MusicDetailsActivity.MUSIC_ID, id);
//                                    goActivity(MusicDetailsActivity.class, bundle);
                                    PlayCtrlUtil.INSTANCE.play(HtmlAgencyActivity.this,Integer.valueOf(id),0);
                                    break;
                                case "musicianDetailHome"://用户详情
                                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id);
                                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 0);
                                    goActivity(MusicIanDetailsActivity.class, bundle);
                                    break;
                                case "musicianDetailMusic"://用户详情（音乐页面）
                                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id);
                                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 1);
                                    goActivity(MusicIanDetailsActivity.class, bundle);
                                    break;
                                case "musicianDetailSongSheet":
                                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id);
                                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 2);
                                    goActivity(MusicIanDetailsActivity.class, bundle);
                                    break;
                                case "musicianDetailTopic":
                                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id);
                                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 3);
                                    goActivity(MusicIanDetailsActivity.class, bundle);
                                    break;
                                case "songSheetDetails"://歌单详情
                                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, id);
                                    goActivity(SongSheetDetailsActivity.class, bundle);
                                    break;
                                case "likesSongSheetDetails":
                                    bundle.putString(LikesMusicActivity.MUSICIAN_ID, id);
                                    goActivity(LikesMusicActivity.class, bundle);
                                    break;
                            }
                        } else if (type.equals("activity")) {
                            Intent intent = new Intent(HtmlAgencyActivity.this, CommonWebview.class);
                            intent.putExtra("url", url);
                            intent.putExtra("activity", "activity");
                            startActivity(intent);
                        }
                    }
                }, 500);
            } else {
                startActivity(new Intent(HtmlAgencyActivity.this, HomeActivity.class));
            }
        }
    }
}
