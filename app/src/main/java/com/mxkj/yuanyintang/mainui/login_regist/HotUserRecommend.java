package com.mxkj.yuanyintang.mainui.login_regist;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.HomeActivity;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.dynamic.adapter.RecommendUserAdapter;
import com.mxkj.yuanyintang.mainui.dynamic.bean.RecommendUser;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
import com.mxkj.yuanyintang.utils.layoutmanager.decoration.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.dynamic.fragment.ObservalFragment.NEED_REFRESH;

public class HotUserRecommend extends StandardUiActivity {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_follow_all)
    TextView tvFollowAll;
    @BindView(R.id.tv_start)
    TextView tvStart;
    private RecommendUserAdapter adapter;
    private List<RecommendUser.DataBean> dataList = new ArrayList<>();
    private String ids = "";
    private boolean isToHome = false;

    @Override
    public int setLayoutId() {
        return R.layout.activity_hot_user_recommend;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        isToHome = getIntent().getBooleanExtra("isToHome", false);
        setRightButtonText("跳过");
        getNavigationBar().getRightButton().setTextColor(ContextCompat.getColor(this, R.color.grey_cc_text));
        setTitleText("推荐关注");
        setOnLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter = new RecommendUserAdapter(dataList);
        recycler.setLayoutManager(new GridLayoutManager(HotUserRecommend.this, 3));
        recycler.addItemDecoration(new SpacesItemDecoration(0, CommonUtils.INSTANCE.dip2px(HotUserRecommend.this, 20)));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        NetWork.INSTANCE.getRecommendUser(this, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                RecommendUser recommendUser = JSON.parseObject(resultData, RecommendUser.class);
                List<RecommendUser.DataBean> data = recommendUser.getData();
                if (data != null) {
                    dataList.addAll(data);
                    adapter.setNewData(dataList);
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

    }

    @OnClick({R.id.rightButton, R.id.tv_follow_all, R.id.tv_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightButton:
                if (isToHome == true) {
                    startActivity(new Intent(this, HomeActivity.class));
                }
                finish();
                break;
            case R.id.tv_follow_all:
                for (int i = 0; i < dataList.size(); i++) {
                    dataList.get(i).setChecked(true);
                }
                adapter.setNewData(dataList);
                break;
            case R.id.tv_start:
                List<RecommendUser.DataBean> data = adapter.getData();
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).isChecked() == true) {
                        if (i != data.size() - 1) {
                            ids += (data.get(i).getId() + ",");
                        } else {
                            ids += (data.get(i).getId());
                        }
                    }
                }
                followAll();
                if (isToHome == true) {
                    startActivity(new Intent(this, HomeActivity.class));
                }
                finish();
                break;
        }
    }

    private void followAll() {
        HttpParams params = new HttpParams();
        params.put("ids", ids);
        NetWork.INSTANCE.followAll(this, params, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {

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
    protected void onDestroy() {
        super.onDestroy();
        setResult(NEED_REFRESH);
    }
}
