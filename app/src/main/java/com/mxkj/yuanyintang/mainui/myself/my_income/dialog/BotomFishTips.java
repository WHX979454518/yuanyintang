package com.mxkj.yuanyintang.mainui.myself.my_income.dialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.dialog.BaseDialogFragment;
import com.mxkj.yuanyintang.mainui.web.CommonWebview;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * Created by zuixia on 2018/5/14.
 */
@SuppressLint("ValidFragment")
public class BotomFishTips extends BaseDialogFragment {
    private String url, desc,title;
    @BindView(R.id.tv_go_help_center)
    TextView tvGoHelpCenter;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    Unbinder unbinder;
    private int type;//0小鱼干、1甜甜圈

    @SuppressLint("ValidFragment")
    public BotomFishTips(int type, String desc, String url) {
        this.url = url;
        this.type = type;
        this.desc = desc;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.botom_fish_dialog;
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
        if (desc != null) {
            tvContent.setText(desc);
        }
        if (type == 1) {
            title = "甜甜圈使用攻略";
            tvTitle.setText("甜甜圈是什么");
            tvGoHelpCenter.setText("查看甜甜圈使用攻略>");
        } else {//小鱼干
            title = "小鱼干使用攻略";
            tvTitle.setText("小鱼干是什么");
            tvGoHelpCenter.setText("查看小鱼干使用攻略>");
        }
        tvGoHelpCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CommonWebview.class);
                intent.putExtra("url", StringUtils.isEmpty(url));
                intent.putExtra("title", title);
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}


