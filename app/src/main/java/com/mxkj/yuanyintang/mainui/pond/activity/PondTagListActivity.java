package com.mxkj.yuanyintang.mainui.pond.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.pond.TagDetialPondList;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.pond.bean.PondHotTagBean;
import com.mxkj.yuanyintang.mainui.pond.widget.FlowLayout;
import com.mxkj.yuanyintang.widget.SearTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class PondTagListActivity extends StandardUiActivity {

    @BindView(R.id.pond_tag_hot)
    FlowLayout pondTagHot;
    @BindView(R.id.pond_tag_all)
    FlowLayout pondTagAll;
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.view_title)
    TextView viewTitle;
    @BindView(R.id.clear_text)
    ImageView clear_text;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.load_more)
    TextView loadMore;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.ll_hotTag)
    LinearLayout llHotTag;
    private int page = 1;
    private String keyWord;

    @Override
    public int setLayoutId() {
        return R.layout.activity_base_tag_list;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
        viewTitle.setText("标签列表");
    }

    @Override
    protected void initEvent() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                pondTagAll.removeAllViews();
                keyWord = editable.toString();
                loadMore.setText("点我加载更多");
                if (editable.length() > 0) {
                    clear_text.setVisibility(View.VISIBLE);
                    page = 1;
                    llHotTag.setVisibility(View.GONE);
                    initTag(1, "times-desc", keyWord);
                } else {
                    clear_text.setVisibility(View.GONE);
                    keyWord = null;
                    initTag(1, "times-desc", null);
                    llHotTag.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void initData() {
        initTag(1, "hot-desc", null);
        initTag(1, "times-desc", null);
    }

    private void initTag(final int page, final String order, final String keyWord) {
        HttpParams params = new HttpParams();
        if (keyWord != null) {
            params.put("keyword", keyWord + "");
        }
        params.put("p", page + "");
        params.put("row", 40 + "");
        params.put("order", order);
        Log.e(TAG, "initTag: " + params);

        NetWork.INSTANCE.getPondHotTag(this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                PondHotTagBean pondHotTagBean = JSON.parseObject(resultData, PondHotTagBean.class);
                List<PondHotTagBean.DataBean> data = pondHotTagBean.getData();
                if (data != null && data.size() > 0) {
                    initFlowView(data, order);
                } else {
                    loadMore.setText("没有更多啦");
                }
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    private void initFlowView(final List<PondHotTagBean.DataBean> data, String order) {
        for (int i = 0; i < data.size(); i++) {
            SearTextView tv = (SearTextView) LayoutInflater.from(PondTagListActivity.this).inflate(
                    R.layout.search_label_tv, pondTagAll, false);
            //点击事件
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PondTagListActivity.this, TagDetialPondList.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(TagDetialPondList.TAG_ID, data.get(finalI).getId());
                    bundle.putString(TagDetialPondList.TAG_TITLE, data.get(finalI).getTitle());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            if (keyWord == null) {
                tv.setText(data.get(i).getTitle());
            } else {
                if (page == 1) {
                    pondTagAll.removeAllViews();
                }
                tv.setSpecifiedTextsColor(data.get(i).getTitle(), keyWord, Color.parseColor("#ff6699"));
            }
            if (order.equals("hot-desc")) {
                pondTagHot.addView(tv);
            } else if (order.equals("times-desc")) {
                pondTagAll.addView(tv);
            }
        }


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @OnClick({R.id.finish, R.id.load_more, R.id.clear_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.load_more:
                page++;
                initTag(page, "times-desc", keyWord);
                break;
            case R.id.clear_text:
                etSearch.getText().clear();
                break;
        }
    }

}
