package com.mxkj.yuanyintang.mainui.home.adapter;//package com.mxkj.yuanyintang.mainui.home.adapter;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.jakewharton.rxbinding2.view.RxView;
//import com.mxkj.yuanyintang.R;
//import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode;
//import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
//import com.mxkj.yuanyintang.mainui.home.activity.MusicIanDetailsActivity;
//import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
//import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
//import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
//import com.mxkj.yuanyintang.mainui.home.utils.GridSpacingItemDecoration;
//import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew;
//import com.mxkj.yuanyintang.mainui.search.SearchResultActivity;
//import com.mxkj.yuanyintang.extraui.activity.PicturePagerDetailsActivity;
//import com.mxkj.yuanyintang.extraui.bean.PictureDataBean;
//import com.mxkj.yuanyintang.utils.app.CommonUtils;
//import com.mxkj.yuanyintang.utils.string.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.annotations.NonNull;
//import io.reactivex.functions.Consumer;
//
//import static com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.SEARCH_WORDS;
//
///**
// * Created by LiuJie on 2017/9/26.
// */
//
//public class MusicDetailsPondAdapter extends BaseQuickAdapter<MusicIndex.ItemInfoListBean.TopicBean, BaseViewHolder> {
//
//    private ArrayList<String> imgList;
//
//    public MusicDetailsPondAdapter(List<MusicIndex.ItemInfoListBean.TopicBean> data) {
//        super(R.layout.home_item_pond, data);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, final MusicIndex.ItemInfoListBean.TopicBean item, final int p) {
////        helper.setText(R.id.tv_title, StringUtils.isEmpty(item.getTitle()));
////        helper.setText(R.id.tv_nickname, StringUtils.isEmpty(item.getNickname()));
////        helper.setText(R.id.tv_read_num, StringUtils.setNum(item.getHits()));
////        ImageLoader.with(mContext)
////                .override(30, 30)
////                .getSize(400,400)
////                .url(item.getHead_link())
////                .asCircle()
////                .scale(ScaleMode.CENTER_CROP)
////                .into(helper.getView(R.id.cimg_head));
////        RecyclerView recyclerView = helper.getView(R.id.recyclerView);
////        if (null != item.getImglist_info()) {
////            if (item.getImglist_info().size() == 0) {
////                helper.setVisible(R.id.iv_alone_img, false);
////                helper.setVisible(R.id.recyclerView, false);
////            } else if (item.getImglist_info().size() == 1) {
////                helper.setVisible(R.id.iv_alone_img, true);
////                helper.setVisible(R.id.recyclerView, false);
////                ImageLoader.with(mContext)
////                        .override(160, 160)
////                        .getSize(400,400)
////                        .url(item.getImglist_info().get(0).getLink())
////                        .asSquare()
////                        .into(helper.<ImageView>getView(R.id.iv_alone_img));
////            } else if (item.getImglist_info().size() >= 2) {
////                helper.setVisible(R.id.iv_alone_img, false);
////                helper.setVisible(R.id.recyclerView, true);
////                if (recyclerView.getAdapter() == null) {
////                    recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
////                    recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, CommonUtils.INSTANCE.dip2px(mContext, 5), 0, true));
////
////                    List<MusicIndex.ItemInfoListBean.TopicBean.ImglistInfoBean> imglist_info = item.getImglist_info();
////                    imgList = new ArrayList<>();
////                    if (imglist_info != null) {
////                        for (int i = 0; i < imglist_info.size(); i++) {
////                            imgList.add(imglist_info.get(i).getLink());
////                        }
////                    }
////                    PondPhotoAdapter pondPhotoAdapter = new PondPhotoAdapter(imgList);
////                    recyclerView.setAdapter(pondPhotoAdapter);
////                    pondPhotoAdapter.setOnItemClickListener(new OnItemClickListener() {
////                        @Override
////                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
////                            Bundle bundle = new Bundle();
////                            PictureDataBean pictureDataBean = new PictureDataBean()
////                                    .setId(item.getId() + "")
////                                    .setCommentNum(item.getThcount())
////                                    .setPhotoList(imgList)
////                                    .setTitle(item.getTitle())
////                                    .setNickname(item.getNickname())
////                                    .setPosition(position)
////                                    .setHits(item.getHits())
////                                    .setType("music");
////                            bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean);
////                            Intent intent = new Intent(mContext, PicturePagerDetailsActivity.class);
////                            intent.putExtras(bundle);
////                            mContext.startActivity(intent);
////                        }
////                    });
////                } else {
////                    recyclerView.getAdapter().notifyDataSetChanged();
////                }
////            }
////        } else {
////            helper.setVisible(R.id.iv_alone_img, false);
////            helper.setVisible(R.id.recyclerView, false);
////        }
////        helper.setVisible(R.id.tv_label_one, false);
////        helper.setVisible(R.id.tv_label_two, false);
////        helper.setVisible(R.id.tv_label_three, false);
////        if (item.getHashtag().size() == 1) {
////            helper.setVisible(R.id.tv_label_one, true);
////            helper.setText(R.id.tv_label_one, StringUtils.isEmpty(item.getHashtag().get(0).getTitle()));
////        } else if (item.getHashtag().size() == 2) {
////            helper.setVisible(R.id.tv_label_one, true);
////            helper.setVisible(R.id.tv_label_two, true);
////            helper.setText(R.id.tv_label_one, StringUtils.isEmpty(item.getHashtag().get(0).getTitle()));
////            helper.setText(R.id.tv_label_two, StringUtils.isEmpty(item.getHashtag().get(1).getTitle()));
////        } else if (item.getHashtag().size() == 3) {
////            helper.setVisible(R.id.tv_label_one, true);
////            helper.setVisible(R.id.tv_label_two, true);
////            helper.setVisible(R.id.tv_label_three, true);
////            helper.setText(R.id.tv_label_one, StringUtils.isEmpty(item.getHashtag().get(0).getTitle()));
////            helper.setText(R.id.tv_label_two, StringUtils.isEmpty(item.getHashtag().get(1).getTitle()));
////            helper.setText(R.id.tv_label_three, StringUtils.isEmpty(item.getHashtag().get(2).getTitle()));
////        }
////        RxView.clicks(helper.getView(R.id.layout_click))
////                .throttleFirst(1, TimeUnit.SECONDS)
////                .subscribe(new Consumer<Object>() {
////                    @Override
////                    public void accept(@NonNull Object o) throws Exception {
////                        Bundle bundle = new Bundle();
////                        bundle.putInt(PondDetialActivityNew.PID, item.getId());
////                        goActivity(PondDetialActivityNew.class, bundle);
////                    }
////                });
////        RxView.clicks(helper.getView(R.id.iv_alone_img))
////                .throttleFirst(1, TimeUnit.SECONDS)
////                .subscribe(new Consumer<Object>() {
////                    @Override
////                    public void accept(@NonNull Object o) throws Exception {
////                        Bundle bundle = new Bundle();
////                        PictureDataBean pictureDataBean = new PictureDataBean()
////                                .setId(item.getId() + "")
////                                .setCommentNum(item.getThcount())
////                                .setPhotoList(imgList)
////                                .setTitle(item.getTitle())
////                                .setNickname(item.getNickname())
////                                .setPosition(0)
////                                .setType("pond")
////                                .setHits(item.getHits());
////                        bundle.putSerializable(PicturePagerDetailsActivity.PICTURE_DATA, pictureDataBean);
////                        goActivity(PicturePagerDetailsActivity.class, bundle);
////                    }
////                });
////
////        RxView.clicks(helper.getView(R.id.tv_label_one))
////                .throttleFirst(1, TimeUnit.SECONDS)
////                .subscribe(new Consumer<Object>() {
////                    @Override
////                    public void accept(@NonNull Object o) throws Exception {
////                        Intent intent = new Intent(mContext, SearchResultActivity.class);
////                        Bundle bundle = new Bundle();
////                        bundle.putString(SEARCH_WORDS, item.getHashtag().get(0).getTitle());
////                        intent.putExtras(bundle);
////                        mContext.startActivity(intent);
////                    }
////                });
////
////        RxView.clicks(helper.getView(R.id.tv_label_two))
////                .throttleFirst(1, TimeUnit.SECONDS)
////                .subscribe(new Consumer<Object>() {
////                    @Override
////                    public void accept(@NonNull Object o) throws Exception {
////                        Intent intent = new Intent(mContext, SearchResultActivity.class);
////                        Bundle bundle = new Bundle();
////                        bundle.putString(SEARCH_WORDS, item.getHashtag().get(2).getTitle());
////                        intent.putExtras(bundle);
////                        mContext.startActivity(intent);
////                    }
////                });
////
////        RxView.clicks(helper.getView(R.id.tv_label_three))
////                .throttleFirst(1, TimeUnit.SECONDS)
////                .subscribe(new Consumer<Object>() {
////                    @Override
////                    public void accept(@NonNull Object o) throws Exception {
////                        Intent intent = new Intent(mContext, SearchResultActivity.class);
////                        Bundle bundle = new Bundle();
////                        bundle.putString(SEARCH_WORDS, item.getHashtag().get(2).getTitle());
////                        intent.putExtras(bundle);
////                        mContext.startActivity(intent);
////                    }
////                });
////        RxView.clicks(helper.getView(R.id.cimg_head))
////                .throttleFirst(1, TimeUnit.SECONDS)
////                .subscribe(new Consumer<Object>() {
////                    @Override
////                    public void accept(@NonNull Object o) throws Exception {
////                        Intent intent = new Intent(mContext, MusicIanDetailsActivity.class);
////                        Bundle bundle = new Bundle();
////                        bundle.putString(MusicIanDetailsActivity.MUSICIAN_ID, item.getUid() + "");
////                        intent.putExtras(bundle);
////                        mContext.startActivity(intent);
////                    }
////                });
////    }
////
////    class PondPhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
////
////        public PondPhotoAdapter(List<String> data) {
////            super(R.layout.homerecycler_pond_item_photo, data);
////        }
////
////        @Override
////        protected void convert(BaseViewHolder helper, String item, int position) {
////            ImageLoader.with(mContext)
////                    .getSize(400,400)
////                    .url(item)
////                    .override(150, 150)
////                    .asSquare()
////                    .into(helper.<ImageView>getView(R.id.iv_img));
////            if (position == 2) {
////                helper.setVisible(R.id.tv_more, true);
////                if (getData().size() - 2 > 0) {
////                    helper.setText(R.id.tv_more, "查看更多\n" + (getData().size() - 2) + "张");
////                } else {
////                    helper.setText(R.id.tv_more, "查看更多");
////                }
////            } else {
////                helper.setVisible(R.id.tv_more, false);
////            }
////        }
////
////        @Override
////        public int getItemCount() {
////            return 3;
////        }
////    }
////
////
////    /**
////     * 跳转页面
////     *
////     * @param mClass 目标页面
////     */
////    protected void goActivity(Class<?> mClass) {
////        goActivity(mClass, null);
////    }
////
////    /**
////     * 跳转页面带参数
////     *
////     * @param mClass 目标页面
////     * @param bundle 参数
////     */
////    protected void goActivity(Class<?> mClass, Bundle bundle) {
////        Intent intent = new Intent(mContext, mClass);
////        if (bundle != null) {
////            intent.putExtras(bundle);
////        }
////        mContext.startActivity(intent);
//    }
//
//
//}
//
