package com.mxkj.yuanyintang.mainui.message.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.im.ui.EaseChatFragment;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.message.adapter.MyCollectionSongListAdapter;
import com.mxkj.yuanyintang.mainui.myself.bean.MySongListBean;
import com.mxkj.yuanyintang.utils.uiutils.DiaLogBuilder;
import com.mxkj.yuanyintang.utils.uiutils.Toast;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.event.MyCollectionRefreshEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

/**
 * Created by Liujie on 2017/10/16.
 */

public class MyCollectionSongListActivity extends StandardUiActivity {
    public static final String IS_MULTI_SELECT = "_is_multi_select";//是否多选
    public static final String MUSIC_ID = "music_id";//音樂id
    public static final String VIEW_TYPE = "_type";

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.layout_new_song)
    LinearLayout layout_new_song;

    private int page = 1;
    private Boolean isMultiSelect = false;
    private Boolean isConfirm = false;
    private String music_id;
    private String view_type;
    MyCollectionSongListAdapter myCollectionSongListAdapter;
    List<MySongListBean.DataBeanX.DataBean> dataBeanList = new ArrayList<>();

    private DiaLogBuilder diaLogBuilder;

    @Override
    public int setLayoutId() {
        return R.layout.activity_my_collection_song_list;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        isMultiSelect = getIntent().getBooleanExtra(IS_MULTI_SELECT, false);
        music_id = getIntent().getStringExtra(MUSIC_ID);
        view_type = getIntent().getStringExtra(VIEW_TYPE);
        if (TextUtils.isEmpty(view_type)) {
            setTitleText("选择歌单");
            setLeftButtonImageView(ContextCompat.getDrawable(this, R.drawable.login_close));
        } else {
            setTitleText("添加到歌单");
            setLeftButtonImageView(null);
            getNavigationBar().getLeftButton().setText("取消");
            getNavigationBar().getLeftButton().setTextColor(ContextCompat.getColor(this, R.color.grey_a6_text));
        }
        setRightButtonText("确定");
        getNavigationBar().getRightButton().setTextColor(ContextCompat.getColor(this, R.color.base_red));
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        finish();
                    }
                });
        RxView.clicks(getNavigationBar().getRightButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
//                        多选状态，添加到多个歌单
                        if (isMultiSelect) {
                            addMusicSong();
                        } else {
//                            单选，看是要添加歌单还是选择分享
                            if (!TextUtils.isEmpty(view_type)) {
                                addMusicSong();
                                return;
                            }
//                            选择分享（聊天界面或者其他地方会用到view_type传空）
                            MySongListBean.DataBeanX.DataBean dataBean = null;
                            for (int i = 0; i < dataBeanList.size(); i++) {
                                if (dataBeanList.get(i).getCheck()) {
                                    dataBean = dataBeanList.get(i);
                                    break;
                                }
                            }
                            if (null != dataBean && null != dataBean.getImgpic_info()) {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString(EaseChatFragment._ID, dataBean.getId() + "");
                                bundle.putString(EaseChatFragment.TITLE, dataBean.getTitle());
                                bundle.putString(EaseChatFragment.NICKNAME, dataBean.getNickname());
                                try {
                                    bundle.putString(EaseChatFragment.IMGPIC_LINK, dataBean.getImgpic_info().getLink());
                                } catch (RuntimeException e) {
                                }

                                intent.putExtras(bundle);
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.create(MyCollectionSongListActivity.this).show("请选择歌单");
                            }
                        }
                    }
                });
        RxView.clicks(layout_new_song)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (null != diaLogBuilder) {
                            diaLogBuilder.show();
                        }
                    }
                });
        initRecylcerView();
        initDialogBuilder();
    }

    int count=1;
    CheckBox check_newsonglist;
    TextView private_songlist;
    private void initDialogBuilder() {
        View view = View.inflate(this, R.layout.dialog_new_song, null);
        TextView tv_cancle = view.findViewById(R.id.tv_cancle);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        final EditText et_song_content = view.findViewById(R.id.et_song_content);

        private_songlist = view.findViewById(R.id.private_songlist);
        check_newsonglist = view.findViewById(R.id.check_newsonglist);

        diaLogBuilder = new DiaLogBuilder(this)
                .setContentView(view)
                .setFullScreen()
                .setGrvier(Gravity.CENTER)
                .setCanceledOnTouchOutside(true)
                .setAniMo(R.anim.popup_in);
        RxView.clicks(tv_confirm)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (TextUtils.isEmpty(et_song_content.getText().toString())) {
                            Toast.create(MyCollectionSongListActivity.this).show("请输入歌单名字");
                            return;
                        }
                        if(check_newsonglist.isChecked()==true){
                            newSong(et_song_content.getText().toString(), 1+"");
                        }else{
                            newSong(et_song_content.getText().toString(),0+"");
                        }

                        et_song_content.setText("");
                        diaLogBuilder.setDismiss();
                    }
                });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaLogBuilder.setDismiss();
            }
        });

        check_newsonglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count==1){
                    check_newsonglist.setChecked(true);
                    count =2;
                    private_songlist.setText("仅自己可见");
                }else{
                    check_newsonglist.setChecked(false);
                    count = 1;
                    private_songlist.setText("仅自己可见");
                }
            }
        });
    }

    private void newSong(String s,String is_private) {
        NetWork.INSTANCE.getNewPlaylist(this, music_id,s,is_private,new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                Toast.create(MyCollectionSongListActivity.this).show("已加入到新歌单");
//                RxBus.getDefault().post(new MyCollectionRefreshEvent(false, 0));
                finish();
//                page = 1;
//                initData();
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    private void addMusicSong() {
        if (TextUtils.isEmpty(music_id)) {
            Toast.create(MyCollectionSongListActivity.this).show("没有音乐ID哦~");
            isConfirm = false;
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < dataBeanList.size(); i++) {
            if (dataBeanList.get(i).getCheck()) {
                sb.append(dataBeanList.get(i).getId()).append(",");
            }
        }
        if (TextUtils.isEmpty(sb)) {
            Toast.create(MyCollectionSongListActivity.this).show("请选择歌单");
            isConfirm = false;
            return;
        }
        sb.deleteCharAt(sb.length() - 1);
        if(view_type.equals("historicalrecordAddSong")||view_type=="historicalrecordAddSong"){
            NetWork.INSTANCE.historicalrecordpostAddSong(this, sb.toString(), music_id, isMultiSelect, new NetWork.TokenCallBack() {
                @Override
                public void doNext(String resultData, Headers headers) {
                    JSONObject object = JSON.parseObject(resultData);
                    String msg = object.getString("msg");
                    Toast.create(MyCollectionSongListActivity.this).show(msg);
                    RxBus.getDefault().post(new MyCollectionRefreshEvent(false, 0));
                    finish();
                }

                @Override
                public void doError(String msg) {
                    isConfirm = false;
                }

                @Override
                public void doResult() {

                }
            });
        }else {
            NetWork.INSTANCE.postAddSong(this, sb.toString(), music_id, isMultiSelect, new NetWork.TokenCallBack() {
                @Override
                public void doNext(String resultData, Headers headers) {
                    JSONObject object = JSON.parseObject(resultData);
                    String msg = object.getString("msg");
                    Toast.create(MyCollectionSongListActivity.this).show(msg);
                    RxBus.getDefault().post(new MyCollectionRefreshEvent(false, 0));
                    finish();
                }

                @Override
                public void doError(String msg) {
                    isConfirm = false;
                }

                @Override
                public void doResult() {

                }
            });
        }

    }

    private void initRecylcerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        myCollectionSongListAdapter = new MyCollectionSongListAdapter(dataBeanList, isMultiSelect, view_type);
        recyclerview.setAdapter(myCollectionSongListAdapter);
        myCollectionSongListAdapter.setCheckedSongListener(new MyCollectionSongListAdapter.ClickCheckedSongListener() {
            @Override
            public void onChecked(int position) {
                for (int i = 0; i < dataBeanList.size(); i++) {
                    if (i == position) {
                        if (dataBeanList.get(i).getCheck()) {
                            dataBeanList.get(i).setCheck(false);
                        } else {
                            dataBeanList.get(i).setCheck(true);
                        }

                    } else {
                        dataBeanList.get(i).setCheck(false);
                    }
                }
                myCollectionSongListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData() {
        NetWork.INSTANCE.getMemberSong(this, page, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                MySongListBean mySongListBean = JSON.parseObject(resultData, MySongListBean.class);
                refreshData(mySongListBean);
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    private void refreshData(MySongListBean mySongListBean) {
        if (page == 1) {
            dataBeanList.clear();
        }
        dataBeanList.addAll(mySongListBean.getData().getData());
        myCollectionSongListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initEvent() {

    }

    private void MaterialDialog(String content) {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.content(
                content)//
                .btnText("取消", "确定")//
                .titleTextSize(16)
                .titleTextColor(ContextCompat.getColor(this, R.color.color_14_text))
                .contentTextColor(ContextCompat.getColor(this, R.color.color_66_text))
                .contentTextSize(14)
                .btnTextSize(14)
                .btnTextColor(ContextCompat.getColor(this, R.color.base_red)
                        , ContextCompat.getColor(this, R.color.base_red))
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
                        dialog.dismiss();
                        if (!isConfirm) {
                            isConfirm = true;
                            addMusicSong();
                        } else {
                            Toast.create(MyCollectionSongListActivity.this).show("正在添加请稍等...");
                        }
                    }
                }
        );
    }
}
