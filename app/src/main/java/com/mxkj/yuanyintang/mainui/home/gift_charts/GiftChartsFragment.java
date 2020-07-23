package com.mxkj.yuanyintang.mainui.home.gift_charts;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.BaseActivity;
import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
import com.mxkj.yuanyintang.mainui.newapp.home.HomeBean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.CHARTS_TYPE;
import static com.mxkj.yuanyintang.mainui.home.gift_charts.GiftChartsActivity.EXPEN_CLASS_ID;

@SuppressLint("ValidFragment")
public class GiftChartsFragment extends Fragment {
    @BindView(R.id.recycler_gift_charts)
    RecyclerView recyclerGiftCharts;
//    @BindView(R.id.notoing_img)
//    ImageView notoing_img;
    @BindView(R.id.notoing_ll)
    LinearLayout notoing_ll;
    @BindView(R.id.nooting_tv)
    TextView nooting_tv;
    Unbinder unbinder;
    private View rootView;
    private View headView;
    private int page = 1;
    List<GiftChartsBean.DataBean.DataListBean> headerList = new ArrayList<>();
    List<GiftChartsBean.DataBean.DataListBean> botomList = new ArrayList<>();
    private int class_id;
    private int time_type = 2;//点击radiobutton切换  2、4
    private GiftChartsAdapter giftChartsAdapter;
    private int charts_type;
    private RadioGroup radioGrop;
    private CircleImageView img_head2, img_head1, img_head3;
    private TextView nick_3, nick_2, nick_1, score_1, score_2, score_3, tv_type1, tv_type2, tv_type3;
    private ImageView iv_order_statu1, iv_order_statu2, iv_order_statu3;
    private GiftChartsBean.DataBean.ToggleInfoBean toggle_info;
    public GiftChartsBean.DataBean.ShareInfoBean share_info;
    private RelativeLayout rl_to_user3, rl_to_user2, rl_to_user1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.gift_charts_fragment_layout, null);
        unbinder = ButterKnife.bind(this, rootView);
        Bundle arguments = getArguments();
//        这里取值不区分收益和打赏。
        class_id = arguments.getInt(EXPEN_CLASS_ID, 0);
        charts_type = arguments.getInt(CHARTS_TYPE, 0);
        initHeadView();
        initEvent();
        return rootView;
    }

    private void initHeadView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.gift_charts_newheader, null);
        radioGrop = headView.findViewById(R.id.radiogrop);
        radioGrop.setVisibility(View.GONE);
        time_type = 2;
        initData();
        img_head1 = headView.findViewById(R.id.img_head1);
        img_head2 = headView.findViewById(R.id.img_head2);
        img_head3 = headView.findViewById(R.id.img_head3);


        tv_type1 = headView.findViewById(R.id.tv_type1);
        tv_type2 = headView.findViewById(R.id.tv_type2);
        tv_type3 = headView.findViewById(R.id.tv_type3);

        nick_2 = headView.findViewById(R.id.nick_2);
        nick_1 = headView.findViewById(R.id.nick_1);
        nick_3 = headView.findViewById(R.id.nick_3);


        rl_to_user3 = headView.findViewById(R.id.rl_to_user3);
        rl_to_user2 = headView.findViewById(R.id.rl_to_user2);
        rl_to_user1 = headView.findViewById(R.id.rl_to_user1);

        score_1 = headView.findViewById(R.id.score_1);
        score_2 = headView.findViewById(R.id.score_2);
        score_3 = headView.findViewById(R.id.score_3);

        iv_order_statu1 = headView.findViewById(R.id.iv_order_statu1);
        iv_order_statu2 = headView.findViewById(R.id.iv_order_statu2);
        iv_order_statu3 = headView.findViewById(R.id.iv_order_statu3);

    }

    private static final String TAG = "WorldFragment";

    private void initData() {
        HttpParams params = new HttpParams();
        params.put("type", time_type + "");
        params.put("class_id", class_id + "");
        NetWork.INSTANCE.getChartsList(getActivity(), page, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                ((BaseActivity) getActivity()).hideLoadingView();
                Log.e(TAG, "doNext: " + resultData);
                GiftChartsBean chartsListBean = JSON.parseObject(resultData, GiftChartsBean.class);
                GiftChartsBean.DataBean data = chartsListBean.getData();
                if (data == null) return;
                toggle_info = data.getToggle_info();
                  /*分享信息*/
                share_info = data.getShare_info();
                List<GiftChartsBean.DataBean.DataListBean> datas = data.getData_list();
                if (datas != null && datas.size() > 0) {
                    headerList.clear();
                    botomList.clear();
                    if (datas.size() >= 3) {
                        for (int i = 0; i < datas.size(); i++) {
                            if (i < 3) {
                                headerList.add(datas.get(i));
                            } else {
                                botomList.add(datas.get(i));
                            }
                        }
                        initHeadData();
                        initMyScore(data.getMy_score());
                    } else {
                        botomList.addAll(datas);
                    }
//                    giftChartsAdapter.setNewData(botomList);
                    initAdapter();
                }else {
                    isNotDataView();
                }
            }

            @Override
            public void doError(String msg) {
                ((BaseActivity) getActivity()).hideLoadingView();
            }

            @Override
            public void doResult() {

            }
        });
    }

    private void isNotDataView() {
//        superSwipeRefreshLayout.setLoadMore(false);
        if (null != notoing_ll) {
            if (botomList.size() > 0) {
                notoing_ll.setVisibility(View.GONE);
                nooting_tv.setVisibility(View.VISIBLE);
            } else {
                notoing_ll.setVisibility(View.VISIBLE);
                nooting_tv.setVisibility(View.GONE);
            }
        }
    }

    //
    private void initMyScore(GiftChartsBean.DataBean.MyScoreBean my_score) {
        if (my_score == null) {
            return;
        }
        int member_id = my_score.getMember_id();
        if (member_id == 0) return;
        GiftChartsBean.DataBean.DataListBean myBean = new GiftChartsBean.DataBean.DataListBean();
        HomeBean.ImgpicInfoBean headInfoBean = new HomeBean.ImgpicInfoBean();
        if (my_score.getHead_link() != null) {
            headInfoBean.setLink(my_score.getHead_link());
            myBean.setHead_info(headInfoBean);
        }
        myBean.setIs_music(my_score.getIs_music());
        myBean.setScore(my_score.getScore());
        myBean.setTotal_desc(my_score.getTotal_desc());
        myBean.setNickname(my_score.getNickname());
        myBean.setGrow_status(my_score.getGrow_status());
        myBean.setMyscore(true);
        Log.e(TAG, "initMyScore: " + myBean.toString());
        botomList.add(myBean);
    }

    private void initHeadData() {
        if(charts_type == 3){
            tv_type1.setText("贡献");
        }else {
            tv_type1.setText("收益");
        }
        if(charts_type == 3){
            tv_type2.setText("贡献");
        }else {
            tv_type2.setText("收益");
        }
        if(charts_type == 3){
            tv_type3.setText("贡献");
        }else {
            tv_type3.setText("收益");
        }
//        tv_type1.setText(charts_type == 4 ? "收益" : "收益");
//        tv_type2.setText(charts_type == 4 ? "收益" : "收益");
//        tv_type3.setText(charts_type == 4 ? "收益" : "收益");

        if (headerList.get(0).getHead_info() != null) {
            ImageLoader.with(getActivity()).getSize(100, 100).url(headerList.get(0).getHead_info().getLink()).into(img_head1);
        }
        if (headerList.get(1).getHead_info() != null) {
            ImageLoader.with(getActivity()).getSize(100, 100).url(headerList.get(1).getHead_info().getLink()).into(img_head2);
        }
        if (headerList.get(2).getHead_info() != null) {
            ImageLoader.with(getActivity()).getSize(100, 100).url(headerList.get(2).getHead_info().getLink()).into(img_head3);
        }


        rl_to_user1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, headerList.get(0).getMember_id() + "");
                goActivity(MusicIanDetailsActivity.class, bundle);
            }
        });
        rl_to_user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, headerList.get(1).getMember_id() + "");
                goActivity(MusicIanDetailsActivity.class, bundle);
            }
        });
        rl_to_user3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, headerList.get(2).getMember_id() + "");
                goActivity(MusicIanDetailsActivity.class, bundle);
            }
        });


        nick_1.setText(StringUtils.isEmpty(headerList.get(0).getNickname()));
        nick_2.setText(StringUtils.isEmpty(headerList.get(1).getNickname()));
        nick_3.setText(StringUtils.isEmpty(headerList.get(2).getNickname()));

        score_1.setText(headerList.get(0).getTotal_desc() + "");
        score_2.setText(headerList.get(1).getTotal_desc() + "");
        score_3.setText(headerList.get(2).getTotal_desc() + "");


        nick_1.setTextColor(headerList.get(0).getIs_self() == 1 ? Color.parseColor("#00c9cc") : Color.parseColor("#1a1717"));
        nick_2.setTextColor(headerList.get(1).getIs_self() == 1 ? Color.parseColor("#00c9cc") : Color.parseColor("#1a1717"));
        nick_3.setTextColor(headerList.get(2).getIs_self() == 1 ? Color.parseColor("#00c9cc") : Color.parseColor("#1a1717"));


        iv_order_statu1.setVisibility(time_type == 2 ? View.VISIBLE : View.INVISIBLE);
        iv_order_statu2.setVisibility(time_type == 2 ? View.VISIBLE : View.INVISIBLE);
        iv_order_statu3.setVisibility(time_type == 2 ? View.VISIBLE : View.INVISIBLE);

        if (headerList.get(0).getGrow_status() == 1) {
            iv_order_statu1.setImageResource(R.drawable.chart_order_up);
        } else if (headerList.get(0).getGrow_status() == -1) {
            iv_order_statu1.setImageResource(R.drawable.chart_order_down);
        } else {
            iv_order_statu1.setImageResource(R.drawable.charts_order_normal);
        }
        if (headerList.get(1).getGrow_status() == 1) {
            iv_order_statu2.setImageResource(R.drawable.chart_order_up);
        } else if (headerList.get(1).getGrow_status() == -1) {
            iv_order_statu2.setImageResource(R.drawable.chart_order_down);
        } else {
            iv_order_statu2.setImageResource(R.drawable.charts_order_normal);
        }

        if (headerList.get(2).getGrow_status() == 1) {
            iv_order_statu3.setImageResource(R.drawable.chart_order_up);
        } else if (headerList.get(2).getGrow_status() == -1) {
            iv_order_statu3.setImageResource(R.drawable.chart_order_down);
        } else {
            iv_order_statu3.setImageResource(R.drawable.charts_order_normal);
        }
    }

    private void initEvent() {
        initData();
    }

    private void initAdapter() {
        recyclerGiftCharts.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (giftChartsAdapter != null) {
            giftChartsAdapter.removeAllHeaderView();
            giftChartsAdapter = null;
        }
        giftChartsAdapter = new GiftChartsAdapter(botomList, charts_type, time_type);
        giftChartsAdapter.addHeaderView(headView);
        recyclerGiftCharts.setAdapter(giftChartsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void goActivity(Class<?> mClass, Bundle bundle) {
        Intent intent = new Intent(getActivity(), mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().startActivity(intent);
    }
}
