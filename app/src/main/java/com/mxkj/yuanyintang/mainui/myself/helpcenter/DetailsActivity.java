package com.mxkj.yuanyintang.mainui.myself.helpcenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.adapter.BaseBindingAdapter;
import com.mxkj.yuanyintang.databinding.ItemHelpSearchListsBinding;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.HelperListBean;
import com.mxkj.yuanyintang.net.NetWork;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class DetailsActivity extends StandardUiActivity {
    @BindView(R.id.et_search)
    TextView etSearch;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    @BindView(R.id.lv_hot_search_list)
    ListView lvHotSearchList;
    @BindView(R.id.tv_search_word)
    TextView tvSearchWord;
    @BindView(R.id.tv_call_robot)
    TextView tvCallRobot;
    @BindView(R.id.layout_not_data)
    LinearLayout layoutNotData;
    @BindView(R.id.show_list)
    RelativeLayout show_list;
    @BindView(R.id.search_more)
    LinearLayout search_more;

    private int page=1;
    List<HelperListBean.DataBean> dataList = new ArrayList<>();
    BaseBindingAdapter<HelperListBean.DataBean, ItemHelpSearchListsBinding> adapter;
    int isBack;


    private String titile;
    private String class_id;
    DetailsActivityAdapter textActivityAdapter;
    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_aboutyuan;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            titile = bundle.getString("title");
            class_id = bundle.getString("class_id");
            setTitleText(titile);
        }
    }

    @Override
    protected void initData() {
        NetWork.INSTANCE.getHelpProblemListDetails(this, class_id, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                HelperListBean helperListBean = JSON.parseObject(resultData, HelperListBean.class);
                if (helperListBean != null) {
                    List<HelperListBean.DataBean> data = helperListBean.getData();
                    if (data != null && data.size() > 0) {
                        if (page == 1) {
                            dataList.clear();
                        }
                        dataList.addAll(data);
//                        adapter.setList(dataList);
                        textActivityAdapter = new DetailsActivityAdapter(dataList,DetailsActivity.this);
                        lvHotSearchList.setAdapter(textActivityAdapter);
                    } else if (page == 1) {
                        isNoDataView();
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
    }

    @Override
    protected void initEvent() {
        adapter = new BaseBindingAdapter<HelperListBean.DataBean, ItemHelpSearchListsBinding>(DetailsActivity.this, dataList, R.layout.item_help_search_lists) {
            @Override
            public void bindObj(ItemHelpSearchListsBinding itemBinding, HelperListBean.DataBean dataBean) {
                itemBinding.setObj(dataBean);
            }

            @Override
            public void operateView(View view, HelperListBean.DataBean dataBean) {
                super.operateView(view, dataBean);
//                showSearchWord(view, dataBean);

            }
        };
        lvHotSearchList.setAdapter(adapter);
//        lvHotSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                HelperListBean.DataBean dataBean = dataList.get(i);
//
//                NetWork.INSTANCE.getHelpProblemListDetailsDetails(DetailsActivity.this, dataBean.getId()+"", new NetWork.TokenCallBack() {
//                    @Override
//                    public void doNext(String resultData, Headers headers) {
//                        WebSharUrlBean webSharUrlBean = JSON.parseObject(resultData, WebSharUrlBean.class);
//                        Intent intent = new Intent(DetailsActivity.this, CommonWebview.class);
//                        intent.putExtra("url", webSharUrlBean.getData().getInfo().getShare_url());
//                        intent.putExtra("title", webSharUrlBean.getData().getInfo().getTitle());
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void doError(String msg) {
//
//                    }
//
//                    @Override
//                    public void doResult() {
//
//                    }
//                });
//            }
//        });

    }

    @OnClick({R.id.rl_search, R.id.tv_call_robot,R.id.search_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_search:
                if (!TextUtils.isEmpty(etSearch.getText())) {
                    setTitleText("搜索");
                    tvSearchWord.setText(etSearch.getText().toString());
                    dataList.clear();
                    layoutNotData.setVisibility(View.GONE);
                    initSearchData();
                }
                break;
            case R.id.tv_call_robot:
                startActivity(new Intent(this, ChatRobotActivity.class));

                break;
            case R.id.search_more:
                startActivity(new Intent(this, HotQuestionsActivity.class));
                break;
        }
    }

    private void initSearchData() {
        page=1;
        InputMethodManager mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputManager.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        NetWork.INSTANCE.searchHelp(DetailsActivity.this, etSearch.getText().toString(), new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                HelperListBean helperListBean = JSON.parseObject(resultData, HelperListBean.class);
                if (helperListBean != null) {
                    List<HelperListBean.DataBean> data = helperListBean.getData();
                    if (data != null && data.size() > 0) {
                        if (page == 1) {
                            dataList.clear();
                        }
                        dataList.addAll(data);
                    } else if (page == 1) {
                        isNoDataView();
                    }
                    adapter.setList(dataList);
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

    private void isNoDataView() {
        if (dataList.size() > 0) {
            layoutNotData.setVisibility(View.GONE);
            show_list.setVisibility(View.VISIBLE);
        } else {
            layoutNotData.setVisibility(View.VISIBLE);
            show_list.setVisibility(View.GONE);
        }
    }

//    @Override
//    public void onBackPressed() {
////        page = 1;
////        if (TextUtils.isEmpty(etSearch.getText())) {
////            finish();
////        } else {
////            etSearch.getText().clear();
////            layoutNotData.setVisibility(View.GONE);
////            initData();
////        }
//    }
}
