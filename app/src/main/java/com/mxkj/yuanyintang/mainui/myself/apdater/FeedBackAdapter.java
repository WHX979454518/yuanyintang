package com.mxkj.yuanyintang.mainui.myself.apdater;

import android.widget.CheckBox;

import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseQuickAdapter;
import com.mxkj.yuanyintang.mainui.home.homebaseadapter.BaseViewHolder;
import com.mxkj.yuanyintang.extraui.bean.ReportOperationBean;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by LiuJie on 2017/11/8.
 */

public class FeedBackAdapter extends BaseQuickAdapter<ReportOperationBean.DataBean, BaseViewHolder> {

    public FeedBackAdapter(List<ReportOperationBean.DataBean> data) {
        super(R.layout.item_feedback, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ReportOperationBean.DataBean item, final int position) {
        helper.<CheckBox>getView(R.id.check_song).setChecked(item.getCheck());
        helper.<CheckBox>getView(R.id.check_song).setText(StringUtils.isEmpty(item.getTitle()));
        RxView.clicks(helper.getView(R.id.layout_click))
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (null != setOnSelectCheckListener) {
                            setOnSelectCheckListener.onSelectCheck(position);
                        }
                    }
                });
    }

    public interface setOnSelectCheckListener {
        void onSelectCheck(int position);
    }

    private setOnSelectCheckListener setOnSelectCheckListener;

    public void setSetOnSelectCheckListener(FeedBackAdapter.setOnSelectCheckListener setOnSelectCheckListener) {
        this.setOnSelectCheckListener = setOnSelectCheckListener;
    }
}
