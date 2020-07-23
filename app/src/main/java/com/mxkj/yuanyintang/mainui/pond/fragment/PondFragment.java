//package com.mxkj.yuanyintang.mainui.pond.fragment;
//
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.annotation.RequiresApi;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSON;
//import com.bumptech.glide.Glide;
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.lzy.okgo.model.HttpParams;
//import com.melnykov.fab.FloatingActionButton;
//import com.mxkj.yuanyintang.base.MainApplication;
//import com.mxkj.yuanyintang.R;
//import com.mxkj.yuanyintang.base.activity.HomeActivity;
//import com.mxkj.yuanyintang.mainui.home.activity.LikesMusicActivity;
//import com.mxkj.yuanyintang.mainui.home.activity.SongSheetDetailsActivity;
//import com.mxkj.yuanyintang.mainui.home.bean.HomeIndex;
//import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
//import com.mxkj.yuanyintang.mainui.pond.TagDetialPondList;
//import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew;
//import com.mxkj.yuanyintang.mainui.pond.activity.PondTagListActivity;
//import com.mxkj.yuanyintang.mainui.pond.activity.PublishPond;
//import com.mxkj.yuanyintang.mainui.pond.adapter.PondRecyclerAdapterNew;
//import com.mxkj.yuanyintang.utils.photopicker.widget.NoScrollRecyclerView;
//import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
//import com.mxkj.yuanyintang.net.NetWork;
//import com.mxkj.yuanyintang.mainui.web.CommonWebview;
//import com.mxkj.yuanyintang.mainui.dynamic.InterfaceRefreshLoadMore;
//import com.mxkj.yuanyintang.mainui.home.activity.MusicDetailsActivity;
//import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
//import com.mxkj.yuanyintang.mainui.home.utils.FeedRootRecyclerView;
//import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration;
//import com.mxkj.yuanyintang.mainui.message.activity.MessageCenterActivity;
//import com.mxkj.yuanyintang.mainui.pond.adapter.HotTagAdapter;
//import com.mxkj.yuanyintang.mainui.pond.bean.PondBeanNew;
//import com.mxkj.yuanyintang.mainui.pond.bean.PondTagBanner;
//import com.mxkj.yuanyintang.mainui.search.SearchActivity;
//import com.mxkj.yuanyintang.utils.app.CommonUtils;
//import com.mxkj.yuanyintang.utils.image.imageloader.utils.ImageUtil;
//import com.mxkj.yuanyintang.utils.layoutmanager.FullyGridLayoutManager;
//import com.mxkj.yuanyintang.utils.rxbus.RxBus;
//import com.mxkj.yuanyintang.utils.uiutils.Srceen;
//import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
//import com.mxkj.yuanyintang.utils.string.StringUtils;
//import com.mxkj.yuanyintang.utils.constant.Constants;
//import com.mxkj.yuanyintang.utils.file.CacheUtils;
//import com.mxkj.yuanyintang.utils.layoutmanager.decoration.SpacesItemDecoration;
//import com.mxkj.yuanyintang.utils.rxbus.event.SearchRecommendEvent;
//import com.mxkj.yuanyintang.widget.bannerLayout.BannerLayout;
//import com.mxkj.yuanyintang.widget.bannerLayout.RecyclingUnlimitedPagerAdapter;
//import com.mxkj.yuanyintang.widget.bannerLayout.util.ViewHolder;
//import com.mxkj.yuanyintang.widget.superSwipeRefreshLayout.VpSuperSwipeRefreshLayout;
//import com.trello.rxlifecycle2.components.support.RxFragment;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
//import io.reactivex.schedulers.Schedulers;
//import okhttp3.Headers;
//
//import static com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.SHOW_SYS_MSG;
//
//public class PondFragment extends RxFragment {
//    private static final String TAG = "PondFragment";
//    @BindView(R.id.recyclerView)
//    FeedRootRecyclerView mRecyclerview;
//    @BindView(R.id.v_statusbar)
//    View vStatusbar;
//    @BindView(R.id.iv_msg)
//    ImageView ivMsg;
//    @BindView(R.id.rl_msg_center)
//    RelativeLayout rlMsgCenter;
//    @BindView(R.id.layout_top_search)
//    LinearLayout layoutTopSearch;
//    @BindView(R.id.flab)
//    FloatingActionButton flab;
//    @BindView(R.id.tv_hot_search)
//    TextView tv_hot_search;
//    @BindView(R.id.tv_msg_count)
//    TextView tv_msg_count;
//    @BindView(R.id.layout_head_search)
//    View headSearch;
//    boolean isAddMsg;
//    private View rootView;
//    private PondRecyclerAdapterNew pondRecyclerAdapter;
//    private LinearLayout mLayoutTopSearch;
//    int mDistance = 0;
//    List<PondBeanNew.DataBean.DataListBean> dataBeanList = new ArrayList<>();
//    int page = 1;
//    private List<PondTagBanner.DataBean.ShufflingBean> mAdDatas = new ArrayList<>();
//    private List<String> bannerStr = new ArrayList<>();
//    private Unbinder unbinder;
//    private RecyclingUnlimitedPagerAdapter<PondTagBanner.DataBean.ShufflingBean> unlimitedPagerAdapter;
//    @BindView(R.id.swipe_refresh)
//    VpSuperSwipeRefreshLayout swipeRefreshLayout;
//    InterfaceRefreshLoadMore interfaceRefreshLoadMore;
//    private View bannerView;
//    private View tagheadView;
//    private HotTagAdapter hotTagAdapter;
//    private View sysMsgHeadView;
//    private Disposable disposable;
//    private NoScrollRecyclerView recyclerViewTag;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.fragment_pond, container, false);
//        }
//        unbinder = ButterKnife.bind(this, rootView);
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(rootView);
//        }
//        sysMsgEvent();
//        return rootView;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (null == rootView) {
//            return;
//        }
//        initView();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    private void initView() {
//        mLayoutTopSearch = rootView.findViewById(R.id.layout_top_search);
//        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            mLayoutTopSearch.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.gradient_search_bg));
//            mLayoutTopSearch.getBackground().setAlpha(255);
//            final int height = Srceen.getScreen(getActivity())[1] / 2;
//            mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    mDistance += dy;
//                    float percent = mDistance * 1f / height;//百分比
//                    int alpha = (int) (percent * 255);
//                    if (alpha >= 255) {
//                        mLayoutTopSearch.getBackground().setAlpha(255);
//                    } else {
//                        mLayoutTopSearch.getBackground().setAlpha(alpha);
//                    }
//                }
//            });
//            StatusBarUtil.baseTransparentStatusBar(getActivity());
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, StatusBarUtil.getStatusBarHeight(getActivity()));
//            vStatusbar.setLayoutParams(params);
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//                vStatusbar.setVisibility(View.GONE);
//            }
//
//        }
//
//        interfaceRefreshLoadMore = new InterfaceRefreshLoadMore(swipeRefreshLayout, getActivity(), new InterfaceRefreshLoadMore.RefreshLoadMoreCallback() {
//            @Override
//            public void onRefresh() {
//                if (null != mLayoutTopSearch) {
//                    mLayoutTopSearch.setVisibility(View.GONE);
//                }
//                page = 1;
//                isAddMsg = false;
//                initData();
//            }
//
//            @Override
//            public void onLoadMore() {
//                page++;
//                initPondData();
//            }
//
//            @Override
//            public void onPushDistance(int distance) {
//
//            }
//
//            @Override
//            public void onPullDistance(int distance) {
//                if (null != headSearch) {
//                    headSearch.setVisibility(distance > 0 ? View.GONE : View.VISIBLE);
//                }
//            }
//        });
//        flab.setColorPressedResId(R.color.base_red);
//        flab.setColorRippleResId(R.color.base_red);
//        flab.setColorNormalResId(R.color.transparent);
//        flab.setShadow(false);
//        flab.attachToRecyclerView(mRecyclerview);
//        flab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (CacheUtils.INSTANCE.getBoolean(getActivity(), Constants.User.IS_LOGIN) == true) {
//                    getActivity().startActivity(new Intent(getActivity(), PublishPond.class));
//                } else {
//                    getActivity().startActivity(new Intent(getActivity(), LoginRegMainPage.class));
//                }
//            }
//        });
//        setMsgCount(((HomeActivity) getActivity()).getMsgUnReadNum());
//        initData();
//    }
//
//    public void setMsgCount(int msgCount) {
//        if (null != tv_msg_count) {
//            if (!CacheUtils.INSTANCE.getBoolean(getActivity(), Constants.User.IS_LOGIN)) {
//                tv_msg_count.setVisibility(View.GONE);
//                return;
//            }
//            if (msgCount <= 0) {
//                tv_msg_count.setVisibility(View.GONE);
//            } else {
//                tv_msg_count.setVisibility(View.VISIBLE);
//                tv_msg_count.setText(msgCount > 9 ? "9+" : msgCount + "");
//            }
//
//        }
//    }
//
//    private void initData() {
//        initAdDatas();
//        if (null != ((HomeActivity) getActivity()).getSearchRecommendEvent()) {
//            SearchRecommendEvent searchRecommend = ((HomeActivity) getActivity()).getSearchRecommendEvent();
//            if (null != tv_hot_search) {
//                tv_hot_search.setText(StringUtils.isEmpty(searchRecommend.getTitle()));
//            }
//        }
//    }
//
//    private void initPondData() {
//        HttpParams params = new HttpParams();
//        params.put("p", page);
//        params.put("row", "30");
//        params.put("order", "update_time-desc");
////        NetWork.INSTANCE.getPond(page == 1 ? true : false, page, getActivity(), params, new NetWork.TokenCallBack() {
////            @Override
////            public void doNext(String resultData, Headers headers) {
////                interfaceRefreshLoadMore.setStopRefreshing();
////                PondBeanNew PondBeanNew = JSON.parseObject(resultData, PondBeanNew.class);
////                jsonPondData(PondBeanNew);
////            }
////
////            @Override
////            public void doError(String msg) {
////                if (null != mLayoutTopSearch) {
////                    mLayoutTopSearch.setVisibility(View.VISIBLE);
////                }
////                interfaceRefreshLoadMore.setStopRefreshing();
////            }
////
////            @Override
////            public void doResult() {
////
////            }
////        });
//    }
//
//    private void jsonPondData(PondBeanNew PondBeanNew) {
//        if (null != mLayoutTopSearch) {
//            mLayoutTopSearch.setVisibility(View.VISIBLE);
//        }
//        if (null == swipeRefreshLayout) {
//            return;
//        }
//        List<PondBeanNew.DataBean.DataListBean> data = PondBeanNew.getData().getData_list();
//        if (data != null) {
//            if (data.size() > 0) {
//                if (page == 1) {
//                    dataBeanList.clear();
//                }
//                dataBeanList.addAll(data);
//                if (null != ((HomeActivity) getActivity()).getSearchRecommendEvent()) {
//                    SearchRecommendEvent searchRecommend = ((HomeActivity) getActivity()).getSearchRecommendEvent();
//                    if (null != tv_hot_search) {
//                        tv_hot_search.setText(StringUtils.isEmpty(searchRecommend.getTitle()));
//                    }
//                }
//            }
//
//        }
//        if (mRecyclerview.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (mRecyclerview.isComputingLayout() == false)) {
//            pondRecyclerAdapter.setNewData(dataBeanList);
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        interfaceRefreshLoadMore.resetRefreshView();
//        unbinder.unbind();
//        RxBus.getDefault().remove(disposable);
//    }
//
//    @OnClick({R.id.rl_msg_center, R.id.layout_top_search})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.rl_msg_center:
//                getActivity().startActivity(new Intent(getActivity(), MessageCenterActivity.class));
//                break;
//            case R.id.layout_top_search:
//                startActivity(new Intent(getActivity(), SearchActivity.class));
//                break;
//        }
//    }
//
//    public void addBanner() {
//        final BannerLayout bannerLayout = bannerView.findViewById(R.id.banner);
//        final LinearLayout ll_banner = bannerView.findViewById(R.id.ll_banner);
//        int anInt = CacheUtils.INSTANCE.getInt(MainApplication.Companion.getApplication(), Constants.Other.WIDTH, 0);
//        final int width = ImageUtil.dip2px(anInt);
//        final int height = (int) (width / 1.8);
//        Log.e(TAG, "addBanner: " + width + "----" + height);
//        unlimitedPagerAdapter = new RecyclingUnlimitedPagerAdapter<PondTagBanner.DataBean.ShufflingBean>(bannerLayout.getAutoScrollViewPager()
//                , getActivity()
//                , mAdDatas
//                , R.layout.item_banner_imgae) {
//            @Override
//            protected void onBind(int position, final PondTagBanner.DataBean.ShufflingBean dataBean, final ViewHolder holder) {
//                if (dataBean.getImgpic_info() != null) {
//                    ll_banner.setLayoutParams(new LinearLayout.LayoutParams(width, height));
////                    ImageLoader.with(MainApplication.Companion.getApplication()).getSize(width, height).url(dataBean.getImgpic_info().getLink()).into(holder.<ImageView>bind(R.id.img));
//                    String bannerUrl;
//                    if (dataBean.getImgpic_info().getLink().contains("?")) {
//                        bannerUrl = dataBean.getImgpic_info().getLink() + "&w=" + width + "&h=" + height;
//                    } else {
//                        bannerUrl = dataBean.getImgpic_info().getLink() + "?log_at=3&w=" + width + "&h=" + height;
//                    }
//                    Glide.with(getActivity())
//                            .load(bannerUrl)
//                            .into(holder.<ImageView>bind(R.id.img));
//                }
//
//                holder.<ImageView>bind(R.id.img).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (dataBean.getUrl().length() > 0) {
//                            if (dataBean.getAlias().equals("web")) {
//                                String url = dataBean.getUrl();
//                                Log.e(TAG, "onItemClick: " + url);
//                                Intent intent = new Intent(getActivity(), CommonWebview.class);
//                                intent.putExtra("url", url);
//                                intent.putExtra("title", dataBean.getTitle());
//                                if (!TextUtils.isEmpty(dataBean.getContent())) {
//                                    intent.putExtra("content", dataBean.getContent());
//                                }
//                                if (dataBean.getImgpic_info() != null && !TextUtils.isEmpty(dataBean.getImgpic_info().getLink())) {
//                                    intent.putExtra("img", dataBean.getImgpic_info().getLink());
//                                }
//                                getActivity().startActivity(intent);
//                            } else if (dataBean.getAlias().equals("activity")) {
//                                String url = dataBean.getUrl();
//                                Intent intent = new Intent(getActivity(), CommonWebview.class);
//                                intent.putExtra("url", url);
//                                intent.putExtra("activity", "activity");
//                                intent.putExtra("title", dataBean.getTitle());
//                                intent.putExtra("content", dataBean.getContent());
//                                if (dataBean.getImgpic_info() != null) {
//                                    intent.putExtra("img", dataBean.getImgpic_info().getLink());
//                                }
//                                getActivity().startActivity(intent);
//                            } else if (dataBean.getAlias().equals("topic")) {
//                                String url = dataBean.getUrl();
//                                Intent intent = new Intent(getActivity(), PondDetialActivityNew.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(url));
//                                intent.putExtras(bundle);
//                                getActivity().startActivity(intent);
//                            } else if (dataBean.getAlias().equals("music")) {
//                                Intent intent = new Intent(getActivity(), MusicDetailsActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString(MusicDetailsActivity.MUSIC_ID, dataBean.getUrl() + "");
//                                intent.putExtras(bundle);
//                                getActivity().startActivity(intent);
//                            } else if (dataBean.getAlias().equals("musician")) {
//                                Intent intent = new Intent(getActivity(), MusicIanDetailsActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, dataBean.getUrl() + "");
//                                intent.putExtras(bundle);
//                                getActivity().startActivity(intent);
//                            } else if (dataBean.getAlias().equals("song")) {
//                                Intent intent = new Intent(getActivity(), SongSheetDetailsActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, dataBean.getUrl() + "");
//                                intent.putExtras(bundle);
//                                getActivity().startActivity(intent);
//                            }
//                        }
//                    }
//                });
//            }
//        };
//        bannerLayout.setAdapter(unlimitedPagerAdapter);
//        bannerLayout.showIndicator(true);
//        bannerLayout.startAutoScroll();
//    }
//
//    /**
//     * 获取banner数据
//     */
//    private void initAdDatas() {
//        NetWork.INSTANCE.getPondBanner(getActivity(), null, new NetWork.TokenCallBack() {
//            @Override
//            public void doNext(String resultData, Headers headers) {
//                Log.e(TAG, "getPondBanner: " + resultData);
//                PondTagBanner pondTagBanner = JSON.parseObject(resultData, PondTagBanner.class);
//                if (pondTagBanner != null) {
//                    PondTagBanner.DataBean data = pondTagBanner.getData();
//                    if (data != null) {
//                        PondTagBanner.DataBean.TagBeanX tag = data.getTag();
//                        if (tag != null) {
//                            if (null == mRecyclerview.getAdapter()) {
//                                mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//                                mRecyclerview.addItemDecoration(new MyRecyclerDetorration(getActivity(), LinearLayoutManager.VERTICAL, CommonUtils.INSTANCE.dip2px(getActivity(), 5), R.color.bg_color, true));
//                                pondRecyclerAdapter = new PondRecyclerAdapterNew(dataBeanList, tag, true);
//                                mRecyclerview.setAdapter(pondRecyclerAdapter);
//                                if (mRecyclerview.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (mRecyclerview.isComputingLayout() == false)) {
//                                    bannerView = LayoutInflater.from(getActivity()).inflate(R.layout.homerecycle_item_top_banner, null);
//                                    pondRecyclerAdapter.addHeaderView(bannerView, 0);
//                                    sysMsgHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.homerecycle_item_system_msg, null);
//                                    pondRecyclerAdapter.addHeaderView(sysMsgHeadView, 1);
//                                    tagheadView = LayoutInflater.from(getActivity()).inflate(R.layout.pond_hot_tag_recycler, null, false);
//                                    pondRecyclerAdapter.addHeaderView(tagheadView, 2);
//                                    sysMsgHeadView.setVisibility(View.GONE);
//                                }
//                                pondRecyclerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                                        Intent intent = new Intent(getActivity(), PondDetialActivityNew.class);
//                                        intent.putExtra("pid", dataBeanList.get(position).getId());
//                                        Log.e(TAG, "onItemClick: " + dataBeanList.get(position).getId());
//                                        getActivity().startActivity(intent);
//                                    }
//                                });
//                            }
//                            initPondData();
//                            //标签
//                            List<PondTagBanner.DataBean.TagBeanX.TagBean> tagList = tag.getTag();
//                            if (tagList != null && tagList.size() > 0) {
//                                setPondTagView(tag);
//                            }
//                            //轮播图
//                            if (null != data && null != data.getShuffling() && data.getShuffling().size() > 0) {
//                                jsonBannerData(data);
//                            }
//                        }
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
//    private void jsonBannerData(PondTagBanner.DataBean bannerBean) {
//        mAdDatas = bannerBean.getShuffling();
//        if (mAdDatas != null && mAdDatas.size() > 0) {
//            bannerStr.clear();
//            for (int i = 0; i < mAdDatas.size(); i++) {
//                if (mAdDatas.get(i).getImgpic_info() != null) {
//                    String address = mAdDatas.get(i).getImgpic_info().getLink();
//                    bannerStr.add(address);
//                }
//            }
//            addBanner();
//        }
//    }
//
//    /**
//     * 添加标签布局到header 2
//     */
//    private void setPondTagView(final PondTagBanner.DataBean.TagBeanX data) {
//        final List<PondTagBanner.DataBean.TagBeanX.TagBean> tagList = data.getTag();
//        recyclerViewTag = tagheadView.findViewById(R.id.hot_tag_recycler);
//        ImageView img_hotTag = tagheadView.findViewById(R.id.img_hotTag);
//        if (data != null && data.getTag_img() != null && data.getTag_img().getImgpic_info() != null && data.getTag_img().getImgpic_info().getLink() != null) {
//            ImageLoader.with(getActivity())
//                    .url(data.getTag_img().getImgpic_info().getLink())
//                    .into(img_hotTag);
//        }
//        if (hotTagAdapter == null) {
//            hotTagAdapter = new HotTagAdapter(tagList);
//            FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(getActivity(), 3);
//            recyclerViewTag.setLayoutManager(fullyGridLayoutManager);
//            recyclerViewTag.addItemDecoration(new SpacesItemDecoration(CommonUtils.INSTANCE.dip2px(getActivity(), 10), CommonUtils.INSTANCE.dip2px(getActivity(), 10)));
//            recyclerViewTag.setAdapter(hotTagAdapter);
//            hotTagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    Intent intent = new Intent(getActivity(), TagDetialPondList.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(TagDetialPondList.TAG_ID, tagList.get(position).getId());
//                    bundle.putString(TagDetialPondList.TAG_TITLE, tagList.get(position).getTitle());
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            });
//            View moreTag = tagheadView.findViewById(R.id.more_tag);
//            moreTag.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(), PondTagListActivity.class);
//                    intent.putExtra("tagType", "pond");
//                    getActivity().startActivity(intent);
//                }
//            });
//        }
//    }
//
//    private void sysMsgEvent() {
//        disposable = RxBus.getDefault().toObservable(HomeIndex.ItemInfoListBean.SystemMsgBean.class)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<HomeIndex.ItemInfoListBean.SystemMsgBean>() {
//                    @Override
//                    public void accept(final HomeIndex.ItemInfoListBean.SystemMsgBean systemMsgBean) throws Exception {
//                        Log.e(TAG, "accept:池塘收到消息");
//                        if (CacheUtils.INSTANCE.getBoolean(getActivity(), SHOW_SYS_MSG, true) == true && isAddMsg == false) {
//                            if (getActivity() != null) {
//                                Log.e(TAG, "accept:池塘添加消息header");
//                                isAddMsg = true;
//                                sysMsgHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.homerecycle_item_system_msg, null);
//                                if (pondRecyclerAdapter != null) {
//                                    Log.e(TAG, "accept: 池塘adapter");
////                                    sysMsgHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.homerecycle_item_system_msg, null);
////                                    pondRecyclerAdapter.addHeaderView(sysMsgHeadView, 1);
//                                    sysMsgHeadView.setVisibility(View.VISIBLE);
//                                    pondRecyclerAdapter.notifyDataSetChanged();
//                                    TextView tv_system_msg = sysMsgHeadView.findViewById(R.id.tv_system_msg);
//                                    ImageView hide_sys_msg = sysMsgHeadView.findViewById(R.id.hide_sys_msg);
//                                    if (systemMsgBean != null) {
//                                        tv_system_msg.setText(systemMsgBean.getText());
//                                    }
//                                    hide_sys_msg.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            CacheUtils.INSTANCE.setBoolean(getActivity(), SHOW_SYS_MSG, false);
//                                            sysMsgHeadView.setVisibility(View.GONE);
//                                            RxBus.getDefault().post(systemMsgBean);
//                                        }
//                                    });
//                                    sysMsgHeadView.setClickable(true);
//                                    sysMsgHeadView.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            msgClickGo(systemMsgBean.getType(), systemMsgBean.getUrl(), systemMsgBean.getId() + "");
//                                        }
//                                    });
//                                }
//                            }
//                        } else {
//                            sysMsgHeadView.setVisibility(View.GONE);
//                        }
//                    }
//                });
//        RxBus.getDefault().add(disposable);
//    }
//
//
//    private void msgClickGo(String type, String url, String id) {
//        if (type != null && type.equals("page")) {
//            Bundle bundle = new Bundle();
//            switch (url) {
//                case "home":
//                    break;
//                case "topicDetails":
//                    if (!TextUtils.isEmpty(id)) {
//                        bundle.putInt(PondDetialActivityNew.PID, Integer.parseInt(id));
//                        goActivity(PondDetialActivityNew.class, bundle);
//                    }
//                    break;
//                case "musicDetails":
//                    bundle.putString(MusicDetailsActivity.MUSIC_ID, id);
//                    goActivity(MusicDetailsActivity.class, bundle);
//                    break;
//                case "musicianDetailHome":
//                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id);
//                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 0);
//                    goActivity(MusicIanDetailsActivity.class, bundle);
//                    break;
//                case "musicianDetailMusic":
//                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id);
//                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 1);
//                    goActivity(MusicIanDetailsActivity.class, bundle);
//                    break;
//                case "musicianDetailSongSheet":
//                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id);
//                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 2);
//                    goActivity(MusicIanDetailsActivity.class, bundle);
//                    break;
//                case "musicianDetailTopic":
//                    bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, id);
//                    bundle.putInt(MusicIanDetailsActivity.MUSICIAN_CURRENT_ITEM, 3);
//                    goActivity(MusicIanDetailsActivity.class, bundle);
//                    break;
//                case "songSheetDetails":
//                    bundle.putString(SongSheetDetailsActivity.SONG_SHEET_ID, id);
//                    goActivity(SongSheetDetailsActivity.class, bundle);
//                    break;
//                case "likesSongSheetDetails":
//                    bundle.putString(LikesMusicActivity.MUSICIAN_ID, id);
//                    goActivity(LikesMusicActivity.class, bundle);
//                    break;
//            }
//        } else if (type != null && type.equals("activity")) {
//            Intent intent = new Intent(getActivity(), CommonWebview.class);
//            intent.putExtra("url", url);
//            intent.putExtra("activity", "activity");
//            getActivity().startActivity(intent);
//        }
//    }
//
//    public void goActivity(Class<?> mClass, Bundle bundle) {
//        Intent intent = new Intent(getActivity(), mClass);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        startActivity(intent);
//    }
//
//
//}