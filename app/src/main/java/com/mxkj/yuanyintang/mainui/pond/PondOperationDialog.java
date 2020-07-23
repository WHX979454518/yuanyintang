package com.mxkj.yuanyintang.mainui.pond;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.bean.UserInfo;
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment;
import com.mxkj.yuanyintang.mainui.login_regist.LoginRegMainPage;
import com.mxkj.yuanyintang.mainui.login_regist.QuickLoginActivityNew;
import com.mxkj.yuanyintang.mainui.pond.activity.PondDetialActivityNew;
import com.mxkj.yuanyintang.net.ApiAddress;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.utils.MyRecyclerDetorration;
import com.mxkj.yuanyintang.mainui.myself.UserInfoUtil;
import com.mxkj.yuanyintang.mainui.pond.bean.PondDetialBean;
import com.mxkj.yuanyintang.extraui.adapter.TitleOperationAdapter;
import com.mxkj.yuanyintang.extraui.bean.MusicBean;
import com.mxkj.yuanyintang.extraui.bean.TitleOperationBean;
import com.mxkj.yuanyintang.extraui.dialog.ReportOperationDialog;
import com.mxkj.yuanyintang.extraui.dialog.ShareBottomDialog;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

/**
 * Created by LiuJie on 2017/10/19.
 */
@SuppressLint("ValidFragment")
public class PondOperationDialog extends BaseDialogFragment {
    private Context context;
    private PondDetialBean.DataBean pondDetialBean;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;
    List<TitleOperationBean> titleOperationBeanList = new ArrayList<>();

    public PondOperationDialog(Context context, PondDetialBean.DataBean pondDetialBean) {
        this.pondDetialBean = pondDetialBean;
        this.context = context;
    }

    public PondOperationDialog() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.dialog_title_operation;
    }

    @Override
    protected Boolean isBack() {
        return false;
    }

    @Override
    protected int style() {
        return 0;
    }

    @Override
    protected void initView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addItemDecoration(new MyRecyclerDetorration(getActivity(), LinearLayoutManager.VERTICAL, CommonUtils.INSTANCE.dip2px(getActivity(), 0.6f), R.color.dividing_line_color,true));
        titleOperationBeanList.add(new TitleOperationBean("分享"));
        Log.e("TAG", pondDetialBean.getUid() + "");
        UserInfoUtil.getUserInfoByJson(CacheUtils.INSTANCE.getString(context, Constants.User.USER_JSON), new UserInfoUtil.UserCallBack() {
            @Override
            public void doNext(UserInfo infoBean) {
                if (infoBean != null && infoBean.getData() != null && infoBean.getData().getId() == pondDetialBean.getUid()) {
                    titleOperationBeanList.add(new TitleOperationBean("删除"));
                } else {
                    titleOperationBeanList.add(new TitleOperationBean("举报"));
                }
            }
        });

        TitleOperationAdapter titleOperationAdapter = new TitleOperationAdapter(titleOperationBeanList, getActivity());
        recyclerview.setAdapter(titleOperationAdapter);
        titleOperationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                dismiss();
                if (null == pondDetialBean) {
                    return;
                }
                switch (titleOperationBeanList.get(position).getTitle()) {
                    case "删除":
                        NetWork.INSTANCE.delePond(context, pondDetialBean.getId(), new NetWork.TokenCallBack() {
                            @Override
                            public void doNext(String resultData, Headers headers) {
                                ((PondDetialActivityNew) context).finish();
                            }

                            @Override
                            public void doError(String msg) {

                            }

                            @Override
                            public void doResult() {

                            }
                        });
                        break;
                    case "举报":
                        MusicBean musicBean = new MusicBean();
                        musicBean.setReportType(4).setReportId(pondDetialBean.getId());
                        ReportOperationDialog reportOperationDialog = new ReportOperationDialog(musicBean);
                        reportOperationDialog.show(getActivity().getSupportFragmentManager(), "mReportOperationDialog");
                        break;

                    case "分享":
                        if (CacheUtils.INSTANCE.getBoolean(context,Constants.User.IS_LOGIN, false) == true && pondDetialBean != null) {
                            MusicBean musicBean2 = new MusicBean();
                            MusicBean.ShareDataBean shareDataBean = new MusicBean.ShareDataBean();
                            shareDataBean.setType("pond");
                            shareDataBean.setNickname(pondDetialBean.getNickname());
                            shareDataBean.setTopicContent(pondDetialBean.getContent());
                            shareDataBean.setTitle(pondDetialBean.getTitle() + "");
                            shareDataBean.setMuisic_id(pondDetialBean.getId());
                            if (pondDetialBean.getImglist_info() != null && pondDetialBean.getImglist_info().size() > 0) {
                                shareDataBean.setWebImgUrl(pondDetialBean.getImglist_info().get(0).getLink());
                                shareDataBean.setImage_link(pondDetialBean.getImglist_info().get(0).getLink());
                            }
                            String shareUrl = ApiAddress.INSTANCE.getH5_BASE_URL() + "topic/detail?id=" + pondDetialBean.getId();
                            shareDataBean.setShareUrl(shareUrl);
                            musicBean2.setShareDataBean(shareDataBean);
                            ShareBottomDialog shareBottomDialog = new ShareBottomDialog(context, musicBean2);
                            shareBottomDialog.show();
                        } else {
                            goActivity(QuickLoginActivityNew.class);
                        }

                        break;
                }
            }
        });

        RxView.clicks(btn_cancel)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        dismiss();
                    }
                });
    }
}
