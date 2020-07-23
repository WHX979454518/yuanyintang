package com.mxkj.yuanyintang.mainui.message.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.message.bean.SystemMessageListBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;

/**
 * Created by LiuJie on 2017/11/12.
 */

public class SystemMessageListDetailsActivity extends StandardUiActivity {

    @BindView(R.id.tv_contents)
    TextView tv_contents;
    @BindView(R.id.iv_img)
    ImageView imageView;

    SystemMessageListBean.DataBeanXX.DataBeanX dataBeanX;

    @Override
    public int setLayoutId() {
        return R.layout.activity_system_message_list_details;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("系统消息");
        setOnLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dataBeanX = (SystemMessageListBean.DataBeanXX.DataBeanX) getIntent().getSerializableExtra("data");
        if (null != dataBeanX) {
            tv_contents.setText(dataBeanX.getContent());
            ImageLoader.with(this)
                    .url(dataBeanX.getHead_link())
                    .into(imageView);
        }
    }

    @Override
    protected void initData() {
        if (null == dataBeanX) {
            return;
        }
        NetWork.INSTANCE.getMessageShow(this, dataBeanX.getId() + "", "1", new NetWork.TokenCallBack() {

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
    protected void initEvent() {

    }
}
