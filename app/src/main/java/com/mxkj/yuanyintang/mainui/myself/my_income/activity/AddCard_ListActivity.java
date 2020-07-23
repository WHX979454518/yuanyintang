package com.mxkj.yuanyintang.mainui.myself.my_income.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.adapter.BaseBindingAdapter;
import com.mxkj.yuanyintang.databinding.ItemBankListBinding;
import com.mxkj.yuanyintang.mainui.myself.my_income.bean.BankListBean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.myself.my_income.activity.AddCreditCardActivity.CARD_BEAN;

public class AddCard_ListActivity extends StandardUiActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    ListView recycler;
    @BindView(R.id.ll_data_view)
    LinearLayout llDataView;
    BaseBindingAdapter<BankListBean.DataBean, ItemBankListBinding> adapter;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_add_card__list;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);
    }

    @Override
    protected void initData() {
        NetWork.INSTANCE.getBankList(this, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                BankListBean bankListBean = JSON.parseObject(resultData, BankListBean.class);
                final List<BankListBean.DataBean> data = bankListBean.getData();
               if (data!=null&&data.size()>0){
                   initAdapter(data);
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

    private void initAdapter(final List<BankListBean.DataBean> data) {
        adapter = new BaseBindingAdapter<BankListBean.DataBean, ItemBankListBinding>(AddCard_ListActivity.this, data, R.layout.item_bank_list) {
            @Override
            public void bindObj(ItemBankListBinding itemBankListBinding, BankListBean.DataBean bankListBean) {
                itemBankListBinding.setObj(bankListBean);
            }

            @Override
            public void operateView(View view, BankListBean.DataBean dataBean) {
                super.operateView(view, dataBean);
                ImageView img_bank = view.findViewById(R.id.img_bank);
                if (dataBean.getIcon_info() != null) {
                    ImageLoader.with(AddCard_ListActivity.this).url(dataBean.getIcon_info().getLink()).into(img_bank);
                }
            }
        };
        recycler.setAdapter(adapter);
        recycler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra(CARD_BEAN,  data.get(i));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected void initEvent() {

    }
}
