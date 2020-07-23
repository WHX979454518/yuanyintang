//package com.mxkj.yuanyintang.ui.pond;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.lzy.okgo.model.HttpParams;
//import com.mxkj.yuanyintang.BaseActivity;
//import com.mxkj.yuanyintang.R;
//import com.mxkj.yuanyintang.net.NetWork;
//import com.mxkj.yuanyintang.ui.home.utils.FeedRootRecyclerView;
//import com.mxkj.yuanyintang.ui.home.utils.MyRecyclerDetorration;
//import com.mxkj.yuanyintang.ui.pond.adapter.PondRecyclerAdapter;
//import com.mxkj.yuanyintang.ui.pond.bean.PondBean;
//import com.mxkj.yuanyintang.ui.pond.bean.PondHotTagBean;
//import com.mxkj.yuanyintang.utils.app.CommonUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class TagDetialPond extends BaseActivity {
//    @BindView(R.id.finish)
//    ImageView finish;
//    @BindView(R.id.view_title)
//    TextView viewTitle;
//    @BindView(R.id.menu_right_top)
//    ImageView menuRightTop;
//    @BindView(R.id.rl_title)
//    RelativeLayout rlTitle;
//    @BindView(R.id.recyclerview)
//    FeedRootRecyclerView mRecyclerview;
//    private PondHotTagBean.DataBean tagBean;
//    List<PondBean.DataBean> dataBeanList = new ArrayList<>();
//    private PondRecyclerAdapter pondRecyclerAdapter;
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_tag_detial_pond;
//    }
//
//    @Override
//    public boolean isVisibilityBottomPlayer() {
//        return false;
//    }
//
//    @Override
//    protected void initView() {
//        ButterKnife.bind(this);
//    }
//
//    @Override
//    protected void initEvent() {
//        Intent intent = getIntent();
//        if (intent != null && intent.getSerializableExtra("tagBean") != null) {
//            tagBean = (PondHotTagBean.DataBean) intent.getSerializableExtra("tagBean");
//            viewTitle.setText(tagBean.getTitle());
//        }
//        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerview.addItemDecoration(new MyRecyclerDetorration(this, LinearLayoutManager.VERTICAL, CommonUtils.dip2px(this, 5), R.color.white));
//        pondRecyclerAdapter = new PondRecyclerAdapter(dataBeanList);
//        mRecyclerview.setAdapter(pondRecyclerAdapter);
//
//    }
//
//    @Override
//    protected void initData() {
//        initPondList(1);
//    }
//
//    private void initPondList(final int page) {
//        if (tagBean != null) {
//            HttpParams params = new HttpParams();
//            params.put("tag", tagBean.getId());
//            params.put("p", page + "");
//            NetWork.getPond(page, this, params, new NetWork.TokenCallBack() {
//                @Override
//                public void doNext(String resultData) {
//                    Log.e(TAG, "doNext: " + resultData);
//                    PondBean pondBean = JSON.parseObject(resultData, PondBean.class);
//                    List<PondBean.DataBean> data = pondBean.getData();
//                    if (page == 1) {
//                        dataBeanList.clear();
//                    }
//                    dataBeanList.addAll(data);
//                    Log.e(TAG, "doNext: " + dataBeanList.size());
//                    pondRecyclerAdapter.setNewData(dataBeanList);
//                }
//
//                @Override
//                public void doError(String msg) {
//
//                }
//            });
//        }
//    }
//
//    @OnClick({R.id.finish, R.id.menu_right_top})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.finish:
//                finish();
//                break;
//            case R.id.menu_right_top:
//
//                break;
//        }
//    }
//}
