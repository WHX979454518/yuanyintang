package com.mxkj.yuanyintang.mainui.home.music_charts.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.music_charts.activity.ChartsListsActivity;
import com.mxkj.yuanyintang.mainui.home.music_charts.bean.ChartsListBean;
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew;
import com.mxkj.yuanyintang.mainui.myself.doughnut.MyDoughnutActivityNew;
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity;
import com.mxkj.yuanyintang.musicplayer.play_control.PlayCtrlUtil;
import com.mxkj.yuanyintang.musicplayer.service.MediaService;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.PlayerMusicRefreshDataEvent;
import com.mxkj.yuanyintang.utils.string.StringUtils;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.musicplayer.service.MediaService.bean;

/**
 * Created by LiuJie on 2017/9/29.
 */

public class DoughnutsAdapter extends BaseMultiItemQuickAdapter<ChartsListBean.DataBean.DataListBean, BaseViewHolder> {

    public String typeStr;//日周月123
    private DiaLogBuilder supportDialog;
    int buttonId;
    private int my_support_count;
    List<RewardBean.DataBean.RewardListBean> reward_list;

    private int count = 1;

    private int type;

    public DoughnutsAdapter(List<ChartsListBean.DataBean.DataListBean> data, int type, int typeId) {
        super(data);
        this.type=type;
        addItemType(1, R.layout.item_music_doughnut_charts);
        switch (type) {
            case 1:
//                typeStr = "日甜甜圈:";
                typeStr = "本日打赏:";
                break;
            case 2:
                typeStr = "本周打赏:";

                break;
            case 3:
                typeStr = "本月打赏:";
                break;
        }
    }

    private String titile;
    public DoughnutsAdapter(List<ChartsListBean.DataBean.DataListBean> data, int type, int typeId, String titile) {
        super(data);
        this.type=type;
        this.titile=titile;
        addItemType(1, R.layout.item_music_doughnut_charts);
        switch (type) {
            case 1:
//                typeStr = "日甜甜圈:";
                typeStr = "本日打赏:";
                break;
            case 2:
                typeStr = "本周打赏:";

                break;
            case 3:
                typeStr = "本月打赏:";
                break;
        }
    }
    @Override
    protected void convert(final BaseViewHolder helper, final ChartsListBean.DataBean.DataListBean item) {
        if (item == null) {
            return;
        }
        if (null != bean) {
            if (bean.getId() == item.getMusic_id()) {
                if (MediaService.getMediaPlayer().isPlaying()) {
                    helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_true));
                } else {
                    helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false));
                }
            } else {
                helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false));
            }
        } else {
            helper.setImageDrawable(R.id.iv_player, ContextCompat.getDrawable(mContext, R.drawable.icon_music_player_white_normal_false));
        }
        RxView.clicks(helper.getView(R.id.iv_player))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (bean != null && bean.getId() == item.getMusic_id()) {
                            mContext.sendBroadcast(new Intent(MediaService.ACTION_PAUSE));
                        } else {
                            bean = null;
                            RxBus.getDefault().post(new PlayerMusicRefreshDataEvent());
                            PlayCtrlUtil.INSTANCE.play(mContext, item.getMusic_id(), 0);
                        }
                    }
                });
        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.getTitle()));

        if (getHeaderLayoutCount()!=0){//有头部的前三名，则列表的第一个从第四名开始
            item.setTag(helper.getPosition()+4);
        }else{
            item.setTag(helper.getPosition());
        }
        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.getTitle()));
        helper.setText(R.id.tv_day_dough, typeStr + item.getCoin());
        if (getHeaderLayoutCount()>0) {
            helper.setText(R.id.tv_order, String.valueOf(helper.getPosition() + 3));
        }else{
            helper.setText(R.id.tv_order, String.valueOf(helper.getPosition()+1));
        }
        helper.setText(R.id.tv_nickname, StringUtils.isEmpty(item.getNickname()));
        ImageLoader.with(mContext).getSize(65, 65).override(65, 65).rectRoundCorner(2)
                .url(item.getImgpic_link()).into(helper.getView(R.id.iv_music_bg));
        if (item.getReward_counts() == 0) {
            helper.setText(R.id.tv_user_couts, "快来给TA冲榜吧");
        } else {
            helper.setText(R.id.tv_user_couts, "冲榜用户:" + item.getReward_counts() + "");
        }
        if(item.getItemType() == 2){
            helper.setVisible(R.id.support_him, true);
        }else {
            helper.setVisible(R.id.support_him, false);
        }

        if(titile.equals("音乐打赏榜") || titile == "音乐打赏榜"){
            helper.setVisible(R.id.support_him, true);
        }else {
            helper.setVisible(R.id.support_him, false);
        }
        helper.setOnClickListener(R.id.support_him, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CacheUtils.INSTANCE.getBoolean(mContext, Constants.User.IS_LOGIN)) {
//                    supportHim(item);
                    ((ChartsListsActivity) mContext).showBotomGiftDialog(item.getMusic_id());
                } else {
                    mContext.startActivity(new Intent(mContext, QuickLoginActivityNew.class));
                }
            }
        });


        TextView tv_title = helper.getView(R.id.tv_title);
        TextView tv_day_dough = helper.getView(R.id.tv_day_dough);
        if (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying()) {
            if(bean != null && bean.getId() == item.getMusic_id()){
                item.setPlaying(true);
                tv_title.setTextColor(Color.parseColor("#ff6699"));
                tv_day_dough.setTextColor(Color.parseColor("#ff6699"));
            }
        }
        if(item.isPlaying()){
            tv_title.setTextColor(Color.parseColor("#ff6699"));
            tv_day_dough.setTextColor(Color.parseColor("#ff6699"));
        }else {
            tv_title.setTextColor(Color.parseColor("#2b2b2b"));
            tv_day_dough.setTextColor(Color.parseColor("#9da2a6"));
        }


        RxView.clicks(helper.getView(R.id.ll_to_detial))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
//                        Intent intent = new Intent(mContext, MusicDetailsActivity.class);
//                        intent.putExtra(MUSIC_ID, item.getMusic_id() + "");
//                        mContext.startActivity(intent);
                        if(count == 2 && item.isPlaying()){
                            Intent intent = new Intent(mContext,PlayerActivity.class);
                            mContext.startActivity(intent);
                        }
//                        else if(count == 1 && (MediaService.mediaPlayer != null && MediaService.mediaPlayer.isPlaying())){
//                            Intent intent = new Intent(mContext,PlayerActivity.class);
//                            PlayCtrlUtil.INSTANCE.play(mContext,item.getMusic_id(),0);
//                            mContext.startActivity(intent);
//                        }
                        else {
                            PlayCtrlUtil.INSTANCE.play(mContext,item.getMusic_id(),0);
                            count = 2;
                        }
                    }
                });
    }

    //旧版
    public void supportHim(final ChartsListBean.DataBean.DataListBean item) {
        View view = View.inflate(mContext, R.layout.dialog_support, null);
        supportDialog = new DiaLogBuilder(mContext)
                .setContentView(view)
                .setAniMo(R.anim.popup_in)
                .setFullScreen()
                .setCanceledOnTouchOutside(false)
                .setGrvier(Gravity.CENTER);

        ImageView iv_close = view.findViewById(R.id.iv_close);
        final RadioGroup radioGroup = view.findViewById(R.id.radiogrop);
        final TextView tv_count = view.findViewById(R.id.tv_count);//剩余次数
        TextView tv_go = view.findViewById(R.id.tv_go);
        final TextView tv_num = view.findViewById(R.id.tv_num);
        TextView tv_get_more = view.findViewById(R.id.tv_get_more);
        final RadioButton rb_one = view.findViewById(R.id.rb_one);
        RadioButton rb_two = view.findViewById(R.id.rb_two);
        RadioButton rb_three = view.findViewById(R.id.rb_three);

        NetWork.INSTANCE.getrewardinfo(mContext, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                RewardBean rewardBean = JSON.parseObject(resultData, RewardBean.class);
                RewardBean.DataBean data = rewardBean.getData();
                if (data == null) {
                    return;
                }
                tv_count.setText("你今天还可以冲榜" + data.getToday_limit() + "次");
                tv_num.setText("当前甜甜圈剩余" + data.getMy_coin() + "个");
                reward_list = data.getReward_list();
                if (reward_list != null && reward_list.size() == 3) {
                    for (int i = 0; i < 3; i++) {
                        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                        radioButton.setChecked(reward_list.get(i).is_selected == 1 ? true : false);
                        radioButton.setText(reward_list.get(i).getTitle());
                    }
                }
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (reward_list != null && reward_list.size() > 2) {
                    switch (i) {
                        case R.id.rb_one:
                            buttonId = reward_list.get(0).getId();
                            my_support_count = reward_list.get(0).getCoin_num();
                            break;
                        case R.id.rb_two:
                            buttonId = reward_list.get(1).getId();
                            my_support_count = reward_list.get(1).getCoin_num();
                            break;
                        case R.id.rb_three:
                            my_support_count = reward_list.get(2).getCoin_num();
                            buttonId = reward_list.get(2).getId();
                            break;
                    }
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportDialog.setDismiss();
            }
        });
        tv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Log.e(TAG, "onClick: ");
//                HttpParams params = new HttpParams();
//                params.put("music_id", item.getMusic_id() + "");
//                params.put("reward_id", buttonId + "");
//                NetWork.goReward(mContext, params, new NetWork.TokenCallBack() {
//                    @Override
//                    public void doNext(String resultData, Headers headers) {
//                        supportDialog.setDismiss();
//                        if (mContext instanceof BaseActivity) {
////                            TODO  返回Ttq_total_desc()
//                            ((BaseActivity) mContext).setSnackBar("冲榜成功~~", "", R.drawable.icon_success);
//                            /**
//                             * 发送事件去刷新页面（排序要改变）
//                             * */
//                            RxBus.getDefault().post(new PlayerMusicRefreshDataEvent());
//                        }
//                    }
//
//                    @Override
//                    public void doError(String msg) {
//                        supportDialog.setDismiss();
//
//                    }
//
//                    @Override
//                    public void doResult() {
//
//                    }
//                });
            }
        });

        tv_get_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, MyDoughnutActivityNew.class));
            }
        });
        supportDialog.show();
    }

    public static class RewardBean {

        private int code;
        private String msg;
        private DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * today_limit : 5
             * my_coin : 200
             * reward_list : [{"id":1,"coin_num":1,"title":"1个","is_selected":0},{"id":2,"coin_num":2,"title":"2个","is_selected":1},{"id":3,"coin_num":5,"title":"5个","is_selected":0}]
             */

            private int today_limit;
            private int my_coin;
            private List<RewardListBean> reward_list;

            public int getToday_limit() {
                return today_limit;
            }

            public void setToday_limit(int today_limit) {
                this.today_limit = today_limit;
            }

            public int getMy_coin() {
                return my_coin;
            }

            public void setMy_coin(int my_coin) {
                this.my_coin = my_coin;
            }

            public List<RewardListBean> getReward_list() {
                return reward_list;
            }

            public void setReward_list(List<RewardListBean> reward_list) {
                this.reward_list = reward_list;
            }

            public static class RewardListBean {
                /**
                 * id : 1
                 * coin_num : 1
                 * title : 1个
                 * is_selected : 0
                 */

                private int id;
                private int coin_num;
                private String title;
                public int is_selected;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getCoin_num() {
                    return coin_num;
                }

                public void setCoin_num(int coin_num) {
                    this.coin_num = coin_num;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public int getIs_selected() {
                    return is_selected;
                }

                public void setIs_selected(int is_selected) {
                    this.is_selected = is_selected;
                }
            }
        }
    }


}
