package com.mxkj.yuanyintang.mainui.myself.activity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.pond.bean.PondHotTagBean;
import com.mxkj.yuanyintang.mainui.pond.widget.FlowLayout;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class ChooseMusicTag extends StandardUiActivity {
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.view_title)
    TextView viewTitle;
    @BindView(R.id.publish)
    TextView publish;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.pond_tag_hot)
    FlowLayout pondTagHot;
    @BindView(R.id.pond_tag_diy)
    FlowLayout pondTagDiy;
    @BindView(R.id.et_tag_name)
    EditText etTagName;
    @BindView(R.id.add_diyTag)
    TextView addDiyTag;
    @BindView(R.id.tvSelectedNum)
    TextView tvSelectedNum;
    private List<PondHotTagBean.DataBean> selectedTagList = new ArrayList<>();//已选中的
    private int page = 1;
    private int maxTagNum = 9;
    private int cate_id;//分类id

    @Override
    public int setLayoutId() {
        return R.layout.activity_choose_music_tag;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
        cate_id = getIntent().getIntExtra("cateId", 0);
        int num = getIntent().getIntExtra("num", -1);
        if (num > 0) {
            maxTagNum = num;
        }
    }

    @Override
    protected void initEvent() {
        try {
            String selectTagStr = CacheUtils.INSTANCE.getString(ChooseMusicTag.this,"selectMusicTag", "");
            List list = CacheUtils.INSTANCE.String2SceneList(selectTagStr);
            selectedTagList.addAll(list);
            for (int i = 0; i < selectedTagList.size(); i++) {
                if (selectedTagList.get(i).getId() == 0) {
                    final LinearLayout ll = (LinearLayout) LayoutInflater.from(this).inflate(
                            R.layout.choosepond_tag_diy, pondTagHot, false);
                    TextView tv_tag = ll.findViewById(R.id.tv_tag);
                    RelativeLayout dele_diytag = ll.findViewById(R.id.dele_diytag);
                    tv_tag.setText(selectedTagList.get(i).getTitle());
                    dele_diytag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pondTagDiy.removeView(ll);
                        }
                    });
                    pondTagDiy.addView(ll);
                }
            }
            tvSelectedNum.setText("(" + selectedTagList.size() + "/" + maxTagNum + ")");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
        initHotTagData(page);
    }

    private void initHotTagData(final int page) {
        NetWork.INSTANCE.getPublicTag(1, cate_id, page, this, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                PondHotTagBean pondHotTagBean = JSON.parseObject(resultData, PondHotTagBean.class);
                List<PondHotTagBean.DataBean> data = pondHotTagBean.getData();
                if (data != null && data.size() > 0) {
                    initHotFlowView(data);
                } else {
//                    loadMore.setText("没有更多啦");
                }
            }

            @Override
            public void doError(String msg) {
                Log.e(TAG, "doError: " + msg);
            }

            @Override
            public void doResult() {

            }
        });
    }

    private void initHotFlowView(final List<PondHotTagBean.DataBean> data) {
        pondTagHot.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            final CheckBox ckTag = (CheckBox) LayoutInflater.from(this).inflate(
                    R.layout.choose_pond_tag_hottag, pondTagHot, false);
            //点击事件
            final int finalI = i;
            ckTag.setText(data.get(i).getTitle());
            for (int j = 0; j < selectedTagList.size(); j++) {
                if (selectedTagList.get(j).getTitle().equals(data.get(i).getTitle())) {
                    ckTag.setChecked(true);
                }
            }
            ckTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (selectedTagList.size() < maxTagNum) {
                            selectedTagList.add(data.get(finalI));
                        } else {
                            ckTag.setChecked(false);
                        }
                    } else {
                        for (int i = 0; i < selectedTagList.size(); i++) {
                            if (selectedTagList.get(i).getTitle().equals(data.get(finalI).getTitle())) {
                                selectedTagList.remove(i);
                            }
                        }
                    }
                    tvSelectedNum.setText("(" + selectedTagList.size() + "/" + maxTagNum + ")");

                }
            });
            pondTagHot.addView(ckTag);
        }


    }

    @OnClick({R.id.finish, R.id.publish, R.id.add_diyTag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                CacheUtils.INSTANCE.setString(ChooseMusicTag.this,"selectMusicTag", "");
                finish();
                break;
            case R.id.publish:
                if (selectedTagList.size() > 0) {
                    String sceneList2String = null;
                    try {
                        sceneList2String = CacheUtils.INSTANCE.SceneList2String(selectedTagList);
                        CacheUtils.INSTANCE.setString(ChooseMusicTag.this,"selectMusicTag", sceneList2String);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                }

                break;
            case R.id.add_diyTag:
                if (etTagName.getText() != null) {
                    final LinearLayout ll = (LinearLayout) LayoutInflater.from(this).inflate(
                            R.layout.choosepond_tag_diy, pondTagHot, false);
                    TextView tv_tag = ll.findViewById(R.id.tv_tag);
                    RelativeLayout dele_diytag = ll.findViewById(R.id.dele_diytag);
                    tv_tag.setText(etTagName.getText().toString().trim());
                    dele_diytag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pondTagDiy.removeView(ll);
                        }
                    });
                    PondHotTagBean.DataBean bean = new PondHotTagBean.DataBean();
                    bean.setId(0);
                    bean.setTitle(etTagName.getText().toString().trim());
                    if (selectedTagList.size() < maxTagNum) {
                        selectedTagList.add(bean);
                        pondTagDiy.addView(ll);
                    } else {
//                        dialogForUser();
                    }
                    tvSelectedNum.setText("(" + selectedTagList.size() + "/9");
                }
                break;
        }
    }
}
