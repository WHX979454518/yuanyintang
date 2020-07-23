package com.mxkj.yuanyintang.mainui.pond.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.im.ui.EaseChatFragment;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.database.DBManager;
import com.mxkj.yuanyintang.base.bean.MusicInfo;
import com.mxkj.yuanyintang.mainui.myself.bean.PlayerHistoryBean;
import com.mxkj.yuanyintang.mainui.search.MusicSearchResultBean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
import com.mxkj.yuanyintang.mainui.pond.adapter.SingleCheckListAdapter;
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils;
import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.SuperSwipeRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class AddMusicNew extends StandardUiActivity {
    public static final int REQUEST_CODE_SELECTED_MUSIC_BEAN = 811;
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.view_title)
    TextView viewTitle;
    @BindView(R.id.publish)
    TextView publish;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_selectmusic_name)
    TextView tvSelectmusicName;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_zjbf)
    TextView tvZjbf;
    @BindView(R.id.lv_musiclist)
    ListView lvMusiclist;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.ll_selected)
    LinearLayout ll_selected;
    @BindView(R.id.layout_prompt)
    RelativeLayout layout_prompt;
    private List<MusicInfo.DataBean> searchResult = new ArrayList<>();//搜索结果集合
    private boolean isSearchView;
    private SingleCheckListAdapter adapter;
    private MusicInfo.DataBean selectedBean;
    InterfaceRefreshLoadMore interfaceRefreshLoadMore;
    @BindView(R.id.swipe_refresh)
    SuperSwipeRefreshLayout swipeRefresh;
    private String type;
    private String title;
    private int page = 1;


    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_add_music;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);
        type = getIntent().getStringExtra("type");
        swipeRefresh.setEnabled(false);

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
                Drawable rDrawable = null;
                Drawable lDrawable = getResources().getDrawable(R.drawable.pond_search);
                lDrawable.setBounds(0, 0, lDrawable.getMinimumWidth(), lDrawable.getMinimumHeight());
                if (editable.length() == 0) {
                    swipeRefresh.setEnabled(false);
                    isSearchView = true;
//                    layout_prompt.setVisibility(View.VISIBLE);
                    if (searchResult == null) {
                        searchResult = new ArrayList<>();
                    }
                    adapter.setEntities(searchResult);
                    adapter.setMaxCount(50);
                    etSearch.setCompoundDrawables(lDrawable, null, null, null);
                } else {
                    title = editable.toString();
                    swipeRefresh.setEnabled(true);
//                    layout_prompt.setVisibility(View.GONE);
                    searchResult.clear();
                    adapter.notifyDataSetChanged();
                    search(editable.toString());
                    adapter.setMaxCount(0);
                    rDrawable = getResources().getDrawable(R.drawable.icon_clear);
                    /// 这一步必须要做,否则不会显示.
                    rDrawable.setBounds(0, 0, rDrawable.getMinimumWidth(), rDrawable.getMinimumHeight());
                    etSearch.setCompoundDrawables(lDrawable, null, rDrawable, null);
                }
            }
        });
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Drawable drawableRight = etSearch.getCompoundDrawables()[2];
                        if (drawableRight != null && motionEvent.getRawX() >= (etSearch.getRight() - drawableRight.getBounds().width())) {
                            etSearch.setText("");
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
        adapter = new SingleCheckListAdapter();
        lvMusiclist.setAdapter(adapter);
        lvMusiclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBean = adapter.getItem(i);
                adapter.updateStatus(selectedBean);
                ll_selected.setVisibility(View.VISIBLE);
                tvSelectmusicName.setVisibility(View.VISIBLE);
                tvSelectmusicName.setText(selectedBean.getTitle());
                tvSelect.setText("已选择  ");
            }
        });

        initHistory();
        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(swipeRefresh, this, new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
            @Override
            public void onRefresh() {
                page = 1;
                search(title);
            }

            @Override
            public void onLoadMore() {
                page++;
                search(title);
            }

            @Override
            public void onPushDistance(int distance) {

            }

            @Override
            public void onPullDistance(int distance) {

            }

        });
    }

    private void initHistory() {
//      显示最近100条
        NetWork.INSTANCE.getMusicHistory(this, 100, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(resultData);
                JSONArray array = jsonObject.getJSONArray("data");
                List<MusicInfo.DataBean> data = JSON.parseArray(array.toJSONString(), MusicInfo.DataBean.class);
                if (data != null && data.size() > 0) {
                    searchResult.addAll(data);
                    adapter.setEntities(searchResult);
                }
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {
                interfaceRefreshLoadMore.setStopRefreshing();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.finish, R.id.publish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                if (isSearchView == true) {
                    etSearch.getText().clear();
                    isSearchView = false;
                } else {
                    finish();
                }
                break;
            case R.id.publish:
                Intent intent = new Intent();
                if (TextUtils.isEmpty(type)) {
                    intent.putExtra("selectedBean", selectedBean);
                    setResult(REQUEST_CODE_SELECTED_MUSIC_BEAN, intent);
                    finish();
                } else {
                    if (null == selectedBean) {
                        return;
                    }
                    switch (type) {
                        case "chat":
                            Bundle bundle = new Bundle();
                            bundle.putString(EaseChatFragment._ID, selectedBean.getId() + "");
                            bundle.putString(EaseChatFragment.TITLE, selectedBean.getTitle());
                            bundle.putString(EaseChatFragment.NICKNAME, selectedBean.getNickname());
                            try {
                                bundle.putString(EaseChatFragment.IMGPIC_LINK, selectedBean.getImgpic_info().getLink());
                            } catch (RuntimeException e) {
                            }

                            intent.putExtras(bundle);
                            setResult(RESULT_OK, intent);
                            finish();
                            break;
                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isSearchView) {
                etSearch.getText().clear();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void search(String title) {
        adapter.setSpecialText(title);
        isSearchView = true;
        if (NetConnectedUtils.isNetConnected(this)) {
            HttpParams params = new HttpParams();
            params.put("type", "1");
            params.put("p", page + "");
            params.put("row", 60 + "");
            if (title != null) {
                params.put("keyword", title);
            }
            NetWork.INSTANCE.SearchMusic(AddMusicNew.this, params, new NetWork.TokenCallBack() {
                @Override
                public void doNext(String resultData, Headers headers) {
                    interfaceRefreshLoadMore.setStopRefreshing();
                    try {
                        JSONObject jsonObject = new JSONObject(resultData);
                        if (!jsonObject.opt("data").equals("")) {
                            List<MusicSearchResultBean.DataBeanX.DataBean> data = JSON.parseObject(resultData, MusicSearchResultBean.class).getData().getData();
                            if (data.size() > 0) {
                                for (int i = 0; i < data.size(); i++) {
                                    MusicSearchResultBean.DataBeanX.DataBean dataBean1 = data.get(i);
                                    MusicInfo.DataBean dataBean = JSON.parseObject(JSON.toJSONString(dataBean1), MusicInfo.DataBean.class);
                                    searchResult.add(dataBean);
                                }
                                searchResult.addAll(searchResult);
                                adapter.setEntities(searchResult);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void doError(String msg) {
                    interfaceRefreshLoadMore.setStopRefreshing();
                }

                @Override
                public void doResult() {

                }
            });
        } else {
//            ToastUtil.showToast(this, "没有网络连接");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        interfaceRefreshLoadMore.resetRefreshView();
    }
}
