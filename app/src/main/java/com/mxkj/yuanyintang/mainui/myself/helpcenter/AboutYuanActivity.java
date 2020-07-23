//package com.mxkj.yuanyintang.mainui.myself.helpcenter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.InputMethodManager;
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
//import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
//import com.mxkj.yuanyintang.mainui.myself.bean.MyPondBean;
//import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.HelperListBean;
//import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.ProblemListBean;
//import com.mxkj.yuanyintang.mainui.web.CommonWebview;
//import com.mxkj.yuanyintang.net.NetWork;
//import com.mxkj.yuanyintang.utils.net.NetConnectedUtils;
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
//public class AboutYuanActivity extends StandardUiActivity {
//    @BindView(R.id.et_search)
//    EditText etSearch;
//    @BindView(R.id.rl_search)
//    RelativeLayout rlSearch;
//    RelativeLayout rlSearch;
//    @BindView(R.id.lv_hot_search_list)
//    ListView lvHotSearchList;
//    @BindView(R.id.tv_search_word)
//    TextView tvSearchWord;
//    @BindView(R.id.tv_call_robot)
//    TextView tvCallRobot;
//    @BindView(R.id.layout_not_data)
//    LinearLayout layoutNotData;
//    @BindView(R.id.show_list)
//    RelativeLayout show_list;
////    @BindView(R.id.search_more)
////    LinearLayout search_more;
//
//    private int page=1;
//    List<HelperListBean.DataBean> dataList = new ArrayList<>();
//    BaseBindingAdapter<HelperListBean.DataBean, ItemHelpSearchListsBinding> adapter;
//    int isBack;
//
//    private String titile;
//    private String class_id;
//    @Override
//    public boolean isVisibilityBottomPlayer() {
//        return false;
//    }
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.activity_aboutyuan;
//    }
//
//    @Override
//    protected void initView() {
//        ButterKnife.bind(this);
//        Intent intent = getIntent();
//        if (intent != null && intent.getExtras() != null) {
//            Bundle bundle = intent.getExtras();
//            titile = bundle.getString("title");
//            class_id = bundle.getString("class_id");
//            setTitleText(titile);
//        }
////        search_more.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                goActivity(HotQuestionsActivity.class);
////            }
////        });
//    }
//
//    @Override
//    protected void initData() {
//        NetWork.INSTANCE.getHelpProblemListDetails(this, class_id, new NetWork.TokenCallBack() {
//            @Override
//            public void doNext(String resultData, Headers headers) {
//                Log.e("uuuuuuuu",""+resultData);
//                HelperListBean helperListBean = JSON.parseObject(resultData, HelperListBean.class);
//                if (helperListBean != null) {
//                    List<HelperListBean.DataBean> data = helperListBean.getData();
//                    if (data != null && data.size() > 0) {
//                        dataList.addAll(data);
//                        adapter.setList(dataList);
//                    }
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
//    @Override
//    protected void initEvent() {
//
//    }
//
//
//    @OnClick({R.id.rl_search, R.id.tv_call_robot})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.rl_search:
//
//                break;
//            case R.id.tv_call_robot:
//
//
//                break;
//        }
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        page = 1;
//        if (TextUtils.isEmpty(etSearch.getText())) {
//            finish();
//        } else {
//            etSearch.getText().clear();
//            layoutNotData.setVisibility(View.GONE);
//            initData();
//        }
//    }
//}
