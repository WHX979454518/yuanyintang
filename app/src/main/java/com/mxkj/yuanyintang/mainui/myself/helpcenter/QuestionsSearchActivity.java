//package com.mxkj.yuanyintang.main_ui.myself.helpcenter;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.mxkj.yuanyintang.R;
//import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
//import com.mxkj.yuanyintang.base.adapter.BaseBindingAdapter;
//import com.mxkj.yuanyintang.databinding.ItemHelpSearchListsBinding;
//import com.mxkj.yuanyintang.main_ui.myself.helpcenter.data.HelperListBean;
//import com.mxkj.yuanyintang.main_ui.web.CommonWebview;
//import com.mxkj.yuanyintang.net.NetWork;
//import com.mxkj.yuanyintang.widget.SearTextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import okhttp3.Headers;
//
//public class QuestionsSearchActivity extends StandardUiActivity {
//    @BindView(R.id.et_search)
//    EditText etSearch;
//    @BindView(R.id.rl_search)
//    RelativeLayout rlSearch;
//    @BindView(R.id.lv_hot_search_list)
//    ListView lvHotSearchList;
//    @BindView(R.id.tv_search_word)
//    TextView tvSearchWord;
//    @BindView(R.id.tv_call_robot)
//    TextView tvCallRobot;
//    @BindView(R.id.layout_not_data)
//    LinearLayout layoutNotData;
//    private int page;
//    List<HelperListBean.DataBean> dataList = new ArrayList<>();
//    BaseBindingAdapter<HelperListBean.DataBean, ItemHelpSearchListsBinding> adapter;
//    int isBack;
//    private String searchWord;
//
//
//    @Override
//    public boolean isVisibilityBottomPlayer() {
//        return false;
//    }
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.activity_hot_questions;
//    }
//
//    @Override
//    protected void initView() {
//        ButterKnife.bind(this);
//        setTitleText("搜索");
//    }
//
//    @Override
//    protected void initData() {
//    }
//
//    @Override
//    protected void initEvent() {
//        adapter = new BaseBindingAdapter<HelperListBean.DataBean, ItemHelpSearchListsBinding>(QuestionsSearchActivity.this, dataList, R.layout.item_help_search_lists) {
//            @Override
//            public void bindObj(ItemHelpSearchListsBinding itemBinding, HelperListBean.DataBean dataBean) {
//                itemBinding.setObj(dataBean);
//            }
//
//            @Override
//            public void operateView(View view, HelperListBean.DataBean dataBean) {
//                super.operateView(view, dataBean);
//                showSearchWord(view, dataBean);
//
//            }
//        };
//        lvHotSearchList.setAdapter(adapter);
//        lvHotSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                HelperListBean.DataBean dataBean = dataList.get(i);
//                Intent intent = new Intent(QuestionsSearchActivity.this, CommonWebview.class);
//                intent.putExtra("url", dataBean.getShare_url() + dataBean.getId());
//                intent.putExtra("title", dataBean.getTitle());
//                startActivity(intent);
//            }
//        });
//
//        searchWord = getIntent().getStringExtra("searchWord");
//        if (searchWord != null) {
//            etSearch.setText(searchWord);
//            initSearchData();
//        }
//
//    }
//
//    /**
//     * 搜索结果标红
//     */
//    private void showSearchWord(View view, HelperListBean.DataBean dataBean) {
//        SearTextView tv_title = view.findViewById(R.id.tv_title);
//        SearTextView tv_content = view.findViewById(R.id.tv_content);
//        if (!TextUtils.isEmpty(etSearch.getText().toString())) {
//            tv_title.setSpecifiedTextsColor(dataBean.getTitle(), etSearch.getText().toString(), Color.parseColor("#ff6699"));
//            tv_content.setSpecifiedTextsColor(dataBean.getDescription(), etSearch.getText().toString(), Color.parseColor("#ff6699"));
//        } else {
//            tv_title.setText(dataBean.getTitle() + "");
//            tv_content.setText(dataBean.getDescription() + "");
//
//        }
//    }
//
//    @OnClick({R.id.rl_search, R.id.tv_call_robot})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.rl_search:
//                if (!TextUtils.isEmpty(etSearch.getText())) {
//                    setTitleText("搜索");
//                    tvSearchWord.setText(etSearch.getText().toString());
//                    dataList.clear();
//                    initSearchData();
//                }
//                break;
//            case R.id.tv_call_robot:
//
//                break;
//        }
//    }
//
//    private void initSearchData() {
//        NetWork.searchHelp(QuestionsSearchActivity.this, etSearch.getText().toString(), new NetWork.TokenCallBack() {
//            @Override
//            public void doNext(String resultData, Headers headers) {
//                HelperListBean helperListBean = JSON.parseObject(resultData, HelperListBean.class);
//                if (helperListBean != null) {
//                    List<HelperListBean.DataBean> data = helperListBean.getData();
//                    if (data != null && data.size() > 0) {
//                        if (page == 1) {
//                            dataList.clear();
//                        }
//                        dataList.addAll(data);
//                    } else if (page == 1) {
//                        isNoDataView();
//                    }
//                    adapter.setList(dataList);
//                }
//            }
//
//            @Override
//            public void doError(String msg) {
//
//            }
//
//            @Override
//            public void doResult() {
//
//            }
//        });
//    }
//
//    private void isNoDataView() {
//        if (dataList.size() > 0) {
//            layoutNotData.setVisibility(View.GONE);
//        } else {
//            layoutNotData.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        overridePendingTransition(0,0);
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(0,0);
//    }
//}
