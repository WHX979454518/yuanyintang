package com.mxkj.yuanyintang.mainui.pond;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.pond.bean.VoteBean;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoteActivity extends StandardUiActivity implements SwitchButton.OnCheckedChangeListener {

    @BindView(R.id.headerViewGroup)
    FrameLayout headerViewGroup;
    @BindView(R.id.et_vote_title)
    EditText etVoteTitle;
    @BindView(R.id.ll_vote_item)
    LinearLayout llVoteItem;
    @BindView(R.id.add_vote_item)
    TextView addVoteItem;
    @BindView(R.id.switch_votet_muti)
    SwitchButton switchVotetMuti;
    @BindView(R.id.switch_votetype)
    SwitchButton switchVotetype;
    @BindView(R.id.tv_downVote)
    TextView tvDownVote;
    @BindView(R.id.tv_voteNum)
    TextView tvVoteNum;
    @BindView(R.id.tv_addVote)
    TextView tvAddVote;
    @BindView(R.id.ll_voteNum)
    LinearLayout llVoteNum;
    private VoteBean voteBean;
    private List<VoteBean.VoteItem> voteItemList = new ArrayList<>();
    int hide;
    int voteType;
    private List<VoteBean.VoteItem> itemList;

    @Override
    public int setLayoutId() {
        return R.layout.activity_vote;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("投票");
        setRightButtonText("确定");
        getNavigationBar().getRightButton().setTextColor(Color.parseColor("#ff4c55"));
        setLeftButtonImageView(ContextCompat.getDrawable(this, R.drawable.login_close));
    }

    @Override
    protected void initEvent() {
        switchVotetMuti.setOnCheckedChangeListener(this);
        switchVotetype.setOnCheckedChangeListener(this);
        VoteBean voteBean = (VoteBean) getIntent().getSerializableExtra("voteBean");
        if (voteBean != null) {
            String question_name = voteBean.getQuestion_name();

            if (voteBean.getVotetype() > 1) {
                llVoteNum.setVisibility(View.VISIBLE);
                tvVoteNum.setText(voteBean.getVotetype() + "");
            }
            etVoteTitle.setText(TextUtils.isEmpty(question_name) ? "" : question_name);
            int hide = voteBean.getHide();
            switchVotetype.setChecked(hide == 1 ? true : false);
            int votetype = voteBean.getVotetype();
            switchVotetMuti.setChecked(votetype > 1 ? true : false);
            itemList = voteBean.getItemList();
            llVoteItem.removeAllViews();
            setVoteItemView();
        }
    }

    private void setVoteItemView() {
        for (int i = 0; i < itemList.size(); i++) {
            VoteBean.VoteItem voteItem = itemList.get(i);
            final View voteItemLayout = LayoutInflater.from(this).inflate(R.layout.pond_vote_item, null);
            EditText editText = voteItemLayout.findViewById(R.id.et_voteItemName);
            ImageView deleVoteItem = voteItemLayout.findViewById(R.id.img_deleVoteItem);
            editText.setText(voteItem.getOptionname());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(CommonUtils.INSTANCE.dip2px(this, 10), CommonUtils.INSTANCE.dip2px(this, 10), CommonUtils.INSTANCE.dip2px(this, 10), CommonUtils.INSTANCE.dip2px(this, 10));
            voteItemLayout.setLayoutParams(lp);
            TextView item_position = voteItemLayout.findViewById(R.id.item_position);
            item_position.setText("选项" + (i + 1) + ":");
            llVoteItem.addView(voteItemLayout);
            final int finalI = i;
            deleVoteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemList.size() > finalI) {
                        itemList.remove(finalI);
                    }
                    llVoteItem.removeView(voteItemLayout);
                    for (int i = 0; i < llVoteItem.getChildCount(); i++) {
                        View childAt = llVoteItem.getChildAt(i);
                        TextView item_position = childAt.findViewById(R.id.item_position);
                        item_position.setText("选项" + (i + 1) + ":");
                    }
                }
            });
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.add_vote_item, R.id.tv_downVote, R.id.tv_addVote, R.id.leftButton, R.id.rightButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_vote_item:
                if (llVoteItem.getChildCount() < 10) {
                    addVoteItem();
                } else {
                    setSnackBar("选项不能超过10个哦~~", "", R.drawable.icon_tips_bad);
                }
                break;
            case R.id.leftButton:
                finish();
                break;
            case R.id.rightButton:
                voteBean = new VoteBean();
                if (!TextUtils.isEmpty(etVoteTitle.getText())) {
                    voteBean.setQuestion_name(etVoteTitle.getText().toString());
                    voteBean.setHide(hide);
                    voteBean.setVotetype(Integer.valueOf(tvVoteNum.getText().toString()));
                    getVoteItemList();
                } else {
                    setSnackBar("你还没有输入投票标题哦", "", R.drawable.icon_tips_bad);
                }
                break;
            case R.id.tv_downVote:
                if (Integer.valueOf(tvVoteNum.getText().toString()) > 0) {
                    tvVoteNum.setText((llVoteItem.getChildCount() - 1) + "");
                } else {
                }
                break;
            case R.id.tv_addVote:
                if (Integer.valueOf(tvVoteNum.getText().toString()) < llVoteItem.getChildCount()) {
                    tvVoteNum.setText((Integer.valueOf(tvVoteNum.getText().toString()) + 1) + "");
                } else {
                    setSnackBar("最大票数不能小于选项数哦~~", "", R.drawable.icon_tips_bad);
                }
                break;
        }
    }

    private void addVoteItem() {
        final View voteItemLayout = LayoutInflater.from(this).inflate(R.layout.pond_vote_item, null);
        voteItemLayout.setPadding(CommonUtils.INSTANCE.dip2px(this, 10), CommonUtils.INSTANCE.dip2px(this, 10), CommonUtils.INSTANCE.dip2px(this, 10), CommonUtils.INSTANCE.dip2px(this, 10));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(CommonUtils.INSTANCE.dip2px(this, 10), CommonUtils.INSTANCE.dip2px(this, 10), CommonUtils.INSTANCE.dip2px(this, 10), CommonUtils.INSTANCE.dip2px(this, 10));
        voteItemLayout.setLayoutParams(lp);
        TextView item_position = voteItemLayout.findViewById(R.id.item_position);
        item_position.setText("选项" + (llVoteItem.getChildCount() + 1) + ":");
        llVoteItem.addView(voteItemLayout);
        ImageView deleVoteItem = voteItemLayout.findViewById(R.id.img_deleVoteItem);
        deleVoteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llVoteItem.removeView(voteItemLayout);
                for (int i = 0; i < llVoteItem.getChildCount(); i++) {
                    View childAt = llVoteItem.getChildAt(i);
                    TextView item_position = childAt.findViewById(R.id.item_position);
                    item_position.setText("选项" + (i + 1) + ":");

                }
            }
        });


    }

    private void getVoteItemList() {
        voteItemList.clear();
        int childCount = llVoteItem.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = llVoteItem.getChildAt(i);
            EditText editText = childAt.findViewById(R.id.et_voteItemName);
            VoteBean.VoteItem voteItemBean = new VoteBean.VoteItem();
            voteItemBean.setId(0);
            if (editText.getText() != null) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    setSnackBar("投票选项内容不能为空哦~", "", R.drawable.icon_tips_bad);
                    break;
                } else {
                    voteItemBean.setOptionname(editText.getText().toString());
                    voteItemList.add(voteItemBean);
                }
            }
        }
        voteBean.setItemList(voteItemList);
        Intent intent = new Intent();
        intent.putExtra("voteBean", voteBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.switch_votet_muti://多选
                if (isChecked) {
                    voteType = 4;
                    llVoteNum.setVisibility(View.VISIBLE);
                } else {
                    voteType = 1;
                    llVoteNum.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.switch_votetype:
                if (isChecked) {
                    hide = 1;
                } else {
                    hide = 0;
                }
                break;

        }

    }
}
