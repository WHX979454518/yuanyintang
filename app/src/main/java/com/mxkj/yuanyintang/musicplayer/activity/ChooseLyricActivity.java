package com.mxkj.yuanyintang.musicplayer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.musicplayer.adapter.ChooseLyricAdapter;
import com.mxkj.yuanyintang.net.ApiAddress;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.image.GetBitmapFromView;
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mxkj.yuanyintang.musicplayer.service.MediaService.bean;

public class ChooseLyricActivity extends StandardUiActivity {
    @BindView(R.id.headerViewGroup)
    FrameLayout headerViewGroup;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.lv_lyrics)
    ListView lvLyrics;
    @BindView(R.id.ll_share_lyric)
    LinearLayout llShareLyric;
    @BindView(R.id.ll_choosepic)
    LinearLayout ll_choosepic;

    private List<ChooseLyricBean> chooseLyricBeanList = new ArrayList<>();
    private List<String> lyricses = new ArrayList<>();
    private String qrcodeurl = "";
    ChooseLyricAdapter adapterType;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_choose_lyric;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        lyricses = getIntent().getStringArrayListExtra("lyrics");
        qrcodeurl = getIntent().getStringExtra("qrcodeurl");
        for (int i = 0; i < lyricses.size(); i++) {
            ChooseLyricBean chooseLyricBean = new ChooseLyricBean();
            chooseLyricBean.setText(lyricses.get(i));
            chooseLyricBeanList.add(chooseLyricBean);
        }
        setTitleText("选择歌词");
//        setRightButtonText("确定");
        hideRightButton();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        adapterType = new ChooseLyricAdapter(chooseLyricBeanList, ChooseLyricActivity.this, this);
        lvLyrics.setAdapter(adapterType);
    }

    @OnClick({R.id.leftButton, R.id.rightButton,R.id.ll_share_lyric,R.id.ll_choosepic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leftButton:
                finish();
                break;
            case R.id.rightButton:
                if (adapterType != null) {
                    List<ChooseLyricBean> dataList = adapterType.getDataList();
                    ArrayList<String> strings = new ArrayList<>();
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).isChecked() == true) {
                            strings.add(dataList.get(i).getText());
                        }
                    }
                    Intent intent = new Intent(this, ShareLyricActivity.class);
                    intent.putStringArrayListExtra("selectedLyric", strings);
                    intent.putExtra("qrcodeurl", qrcodeurl);
                    startActivity(intent);

                }
                break;
            case R.id.ll_share_lyric:
                shareLyric();
                break;
            case R.id.ll_choosepic:
                if (adapterType != null) {
                    List<ChooseLyricBean> dataList = adapterType.getDataList();
                    ArrayList<String> strings = new ArrayList<>();
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).isChecked() == true) {
                            strings.add(dataList.get(i).getText());
                        }
                    }
                    Intent intent = new Intent(this, ShareLyricActivity.class);
                    intent.putStringArrayListExtra("selectedLyric", strings);
                    intent.putExtra("qrcodeurl", qrcodeurl);
                    startActivity(intent);

                }
                break;
        }
    }

    public class ChooseLyricBean {
        private String text;
        private boolean isChecked;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }

    public void shareLyric() {
        if (adapterType != null) {
            List<ChooseLyricBean> dataList = adapterType.getDataList();
            ArrayList<String> strings = new ArrayList<>();
            String str = "";
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).isChecked() == true) {
                    strings.add(dataList.get(i).getText());
                }
            }
            for (int j=0;j<strings.size();j++) {
                str += strings.get(j)+",";
            }

        if (NetConnectedUtils.isNetConnected(getApplicationContext())) {
//            if (CacheUtils.INSTANCE.getBoolean(this, Constants.User.IS_LOGIN, false) == true && bean != null) {
                MusicBean musicBean = new MusicBean();
                MusicBean.ShareDataBean shareDataBean = new MusicBean.ShareDataBean();
//                shareDataBean.setType("music");
                shareDataBean.setType("lyrics");
                if(null!=bean.getVideo_info()){
                    shareDataBean.setVideo_link(bean.getVideo_info().getLink());
                }
            try {
                shareDataBean.setImage_link(bean.getImgpic_info().getLink());
                shareDataBean.setWebImgUrl(bean.getImgpic_info().getLink());
            } catch (RuntimeException e) {
            }
                shareDataBean.setUid(bean.getUid());
                shareDataBean.setMuisic_id(bean.getId());
                shareDataBean.setNickname(bean.getNickname());
                shareDataBean.setTitle(bean.getTitle());
                shareDataBean.setShare_link(bean.getShare_url());
                shareDataBean.setMv(bean.getMv());

                shareDataBean.setTopicContent(str);
                Log.e("ggggg",""+str.toString());
//                shareDataBean.setTitle(str);
                musicBean.setShareDataBean(shareDataBean);
                ShareBottomDialog shareBottomDialog = new ShareBottomDialog(this, musicBean);
                shareBottomDialog.show();

//            } else {
//                goActivity(LoginRegMainPage.class);
//            }
        }
        }
    }
}
