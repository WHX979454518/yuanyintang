package com.mxkj.yuanyintang.mainui.myself.my_release;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.mainui.myself.my_release.view.AddPartnerViewGroup;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mxkj.yuanyintang.mainui.myself.my_release.up_load_music.UploadMusicActivity.PARTNER_JSON;

public class BindPartnerActivity extends StandardUiActivity {
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.tv_sure_right)
    TextView tvSureRight;
    @BindView(R.id.tv_add_job_type)
    TextView tv_add_job_type;
    @BindView(R.id.ll_main_view)
    LinearLayout llMainView;
    public static final int ADD_PARTNER_REQUEST_CODE = 0x0101;
    public static final String ADD_PARTNER_BEAN = "ADD_PARTNER_BEAN";
    private int currAddPosition = -1;//传回来的用户 需要添加的位置

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_bind_partner;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);
        initPartnerView();

    }

    /**
     * 数据回显
     */
    private void initPartnerView() {
        String stringExtra = getIntent().getStringExtra(PARTNER_JSON);
        if (!TextUtils.isEmpty(stringExtra)) {
            Log.e(TAG, "initPartnerView: " + stringExtra);
            UploadJsonBean uploadJsonBean = JSON.parseObject(stringExtra, UploadJsonBean.class);
            List<UploadJsonBean.MemberBeanX> memberList = uploadJsonBean.getMember();
            for (int i = 0; i < memberList.size(); i++) {
                tvAddPartnerView("");
                View childAt = llMainView.getChildAt(i);
                EditText et_work_type = childAt.findViewById(R.id.et_work_type);
                AddPartnerViewGroup partner_view_group = childAt.findViewById(R.id.partner_view_group);
                et_work_type.setText(memberList.get(i).getMusic_type());
                List<UploadJsonBean.MemberBeanX.MemberBean> members = memberList.get(i).getMember();
                for (int j = 0; j < members.size(); j++) {
                    MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean = new MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean();
                    memberBean.setNickname(members.get(j).getNickname());
                    memberBean.setUid(members.get(j).getUid());
                    partner_view_group.insert(memberBean, new AddPartnerViewGroup.InsertCallback() {
                        @Override
                        public void onInsert(boolean canAdd) {
                            if (!canAdd){
                                setSnackBar("你已添加该小伙伴","",R.drawable.icon_tips_bad);
                            }
                        }
                    });
                }
            }
        } else {
            tvAddPartnerView("1");//默认添加一
            tvAddPartnerView("2");//默认添加一
            tvAddPartnerView("3");//默认添加一
            tvAddPartnerView("4");//默认添加一
            tvAddPartnerView("5");//默认添加一


        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {

    }

    @OnClick({R.id.finish, R.id.tv_sure_right, R.id.tv_add_job_type})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.tv_sure_right:
                sureBindPartner();
                break;
            case R.id.tv_add_job_type:
                tvAddPartnerView("");
                break;
        }
    }

    private void sureBindPartner() {
        int childCount = llMainView.getChildCount();
        Log.e(TAG, "sureBindPartner: "+childCount );
        UploadJsonBean uploadJsonBean = new UploadJsonBean();
        List<UploadJsonBean.MemberBeanX> memberBeanXES = new ArrayList<>();
        for (int i = 0; i < childCount; i++) {
            Log.e(TAG, "sureBindPartner:遍历--- "+i );

//            if (i == childCount - 1) break;
            View childAt = llMainView.getChildAt(i);
            EditText et_work_type = childAt.findViewById(R.id.et_work_type);
            AddPartnerViewGroup partner_view_group = childAt.findViewById(R.id.partner_view_group);
            UploadJsonBean.MemberBeanX memberBeanX = new UploadJsonBean.MemberBeanX();
            if (!TextUtils.isEmpty(et_work_type.getText())) {
                memberBeanX.setMusic_type(et_work_type.getText().toString());
            }
            List<MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean> dataList = partner_view_group.getDataList();
            ArrayList<UploadJsonBean.MemberBeanX.MemberBean> memberBeans = new ArrayList<>();
            for (int j = 0; j < dataList.size(); j++) {
                UploadJsonBean.MemberBeanX.MemberBean memberBean = new UploadJsonBean.MemberBeanX.MemberBean();
                if (!dataList.get(j).getNickname().equals("+添加用户")) {
                    memberBean.setNickname(dataList.get(j).getNickname());
                    memberBean.setUid(dataList.get(j).getUid());
                    memberBean.setMusic_type(memberBeanX.getMusic_type());
                    memberBeans.add(memberBean);
                }
            }
            memberBeanX.setMember(memberBeans);
            memberBeanXES.add(memberBeanX);
        }
        Log.e(TAG, "sureBindPartner-----memberBeanXES:--size--- "+memberBeanXES.size());

        if (memberBeanXES.size() != 0) {
            uploadJsonBean.setMember(memberBeanXES);
            String jsonString = JSON.toJSONString(uploadJsonBean);
            Intent intent = new Intent();
            intent.putExtra(PARTNER_JSON, jsonString);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PARTNER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean = (MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean) data.getSerializableExtra(ADD_PARTNER_BEAN);
            addChildPartnerview(memberBean);
        }
    }

    private void addChildPartnerview(MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean) {
        if (memberBean == null || currAddPosition == -1) return;
        View view = llMainView.getChildAt(currAddPosition);
        AddPartnerViewGroup group = view.findViewById(R.id.partner_view_group);
        group.insert(memberBean, new AddPartnerViewGroup.InsertCallback() {
            @Override
            public void onInsert(boolean canAdd) {
                if (!canAdd){
                    setSnackBar("你已添加该小伙伴","",R.drawable.icon_tips_bad);
                }
            }
        });
        currAddPosition = -1;
    }

    private void tvAddPartnerView(String biaoshi) {
        final View view = LayoutInflater.from(this).inflate(R.layout.add_partner_layout, null);
        int childCount = llMainView.getChildCount();
        if (childCount < 10) {
            llMainView.addView(view);
            Log.e(TAG, "tvAddPartnerView: " + llMainView.getChildCount());
            for (int i = 0; i < llMainView.getChildCount(); i++) {
                final View childAt = llMainView.getChildAt(i);
                ImageView delFor = childAt.findViewById(R.id.img_del_partner_item);
                final TextView tv_name = view.findViewById(R.id.tv_name);
                final TextView et_work_type = view.findViewById(R.id.et_work_type);
                final ImageView img_del_partner_item = view.findViewById(R.id.img_del_partner_item);
                if(biaoshi.equals("1")){
                    et_work_type.setText("策划");
                    et_work_type.setFocusable(false);
                    img_del_partner_item.setVisibility(View.GONE);
                }else if(biaoshi.equals("2")){
                    et_work_type.setText("唱见");
                    et_work_type.setFocusable(false);
                    img_del_partner_item.setVisibility(View.GONE);
                }else if(biaoshi.equals("3")){
                    et_work_type.setText("作词");
                    et_work_type.setFocusable(false);
                    img_del_partner_item.setVisibility(View.GONE);
                }else if(biaoshi.equals("4")){
                    et_work_type.setText("作曲");
                    et_work_type.setFocusable(false);
                    img_del_partner_item.setVisibility(View.GONE);
                }else if(biaoshi.equals("5")){
                    et_work_type.setText("后期");
                    et_work_type.setFocusable(false);
                    img_del_partner_item.setVisibility(View.GONE);
                }

                final int finalI = i;
                tv_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tv_name.getText().toString().trim().equals("+添加用户")) {
                            currAddPosition = finalI;
                            startActivityIfNeeded(new Intent(BindPartnerActivity.this, PartnerListActivity.class), ADD_PARTNER_REQUEST_CODE);
                        }
                    }
                });
                if (delFor != null) {
                    if (llMainView.getChildCount() == 1) {
                        delFor.setVisibility(View.INVISIBLE);
                    } else {
                        delFor.setVisibility(View.VISIBLE);
                    }
                    delFor.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View mview) {
                            BaseConfirmDialog.Companion.newInstance().title("删除工种").content("确定要删除该工种？").confirmText("删除").cancleText("手滑了").showDialog(BindPartnerActivity.this, new BaseConfirmDialog.onBtClick() {
                                @Override
                                public void onConfirm() {
                                    Log.e(TAG, "onConfirm: "+finalI);
                                    llMainView.removeView(childAt);
                                    for (int i = 0; i < llMainView.getChildCount(); i++) {
                                        View childAt = llMainView.getChildAt(i);
                                        ImageView img_del_partner_item = childAt.findViewById(R.id.img_del_partner_item);
                                        if (img_del_partner_item != null) {
                                            if (llMainView.getChildCount() == 1) {
                                                img_del_partner_item.setVisibility(View.INVISIBLE);
                                            } else {
                                                img_del_partner_item.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancle() {

                                }
                            });
                        }
                    });
                }
            }
        } else {
            setSnackBar("最多添加10个工种哦", "", R.drawable.icon_success);
        }
    }

    /**
     * 上传歌曲的时候需要传给服务器的json串
     */
    public static class UploadJsonBean implements Serializable {

        private List<MemberBeanX> member;

        public List<MemberBeanX> getMember() {
            return member;
        }

        public void setMember(List<MemberBeanX> member) {
            this.member = member;
        }

        public static class MemberBeanX implements Serializable {
            private String music_type;
            private List<MemberBean> member;

            public String getMusic_type() {
                return music_type;
            }

            public void setMusic_type(String music_type) {
                this.music_type = music_type;
            }

            public List<MemberBean> getMember() {
                return member;
            }

            public void setMember(List<MemberBean> member) {
                this.member = member;
            }

            public static class MemberBean implements Serializable {
                /**
                 * nickname :
                 * uid : 0
                 */

                private String nickname;
                private String music_type;
                private int uid;

                public String getMusic_type() {
                    return music_type;
                }

                public void setMusic_type(String music_type) {
                    this.music_type = music_type;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public int getUid() {
                    return uid;
                }

                public void setUid(int uid) {
                    this.uid = uid;
                }
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
