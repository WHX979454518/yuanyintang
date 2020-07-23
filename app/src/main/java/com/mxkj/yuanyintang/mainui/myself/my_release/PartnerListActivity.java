package com.mxkj.yuanyintang.mainui.myself.my_release;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.adapter.BaseBindingAdapter;
import com.mxkj.yuanyintang.databinding.ItemHistoryPartnerBinding;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;
import com.mxkj.yuanyintang.mainui.myself.settings.activity.InviteOthersActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader;
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils;
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil;
import com.mxkj.yuanyintang.widget.SearTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.myself.my_release.BindPartnerActivity.ADD_PARTNER_BEAN;

public class PartnerListActivity extends StandardUiActivity {

    BaseBindingAdapter<MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean, ItemHistoryPartnerBinding> adapter;
    @BindView(R.id.finish)
    ImageView finish;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.lv_partner)
    ListView lvPartner;
    @BindView(R.id.clear_txt)
    ImageView clear_txt;
    @BindView(R.id.tv_sure_right)
    TextView tvSureRight;
    @BindView(R.id.ll_dataView)
    LinearLayout llDataView;
    @BindView(R.id.tv_invite)
    TextView tvInvite;
    @BindView(R.id.tv_just_add)
    TextView tvJustAdd;
    @BindView(R.id.ll_nodataView)
    LinearLayout llNodataView;
    @BindView(R.id.tv_no_data_desc)
    TextView tvNoDataDesc;
    private List<MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean> dataList = new ArrayList<>();

    private int page = 1;

    @Override

    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        StatusBarUtil.baseTransparentStatusBar(this);
        return R.layout.activity_partner_list;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);

    }

    @Override
    protected void initData() {
        HttpParams params = new HttpParams();
        params.put("p", page);
        NetWork.INSTANCE.getPartnerList(this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                parseData(resultData);
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    private void parseData(String resultData) {
        TempBean tempBean = JSON.parseObject(resultData, TempBean.class);
        List<TempBean.DataBean> data = tempBean.getData();
        if (data != null && data.size() > 0) {
            llDataView.setVisibility(View.VISIBLE);
            llNodataView.setVisibility(View.GONE);

            if (page == 1) {
                dataList.clear();
            }
            for (int i = 0; i < data.size(); i++) {
                MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean = new MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean();
                memberBean.setUid(data.get(i).getId());
                memberBean.setNickname(data.get(i).getNickname());
                if (data.get(i).getHead_info()!=null){
                    memberBean.setHead_link(data.get(i).getHead_info().getLink());
                }
                dataList.add(memberBean);
            }
            adapter.setList(dataList);
        } else {
            if (page == 1) {
                llDataView.setVisibility(View.GONE);
                llNodataView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void initEvent() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() > 0) {
                    tvTips.setText("添加\"" + editable.toString() + "\"这个小伙伴");
                    search(editable.toString());
                    clear_txt.setVisibility(View.VISIBLE);
                    tvJustAdd.setText("直接添加"+editable.toString()+"这个小伙伴");

                    tvNoDataDesc.setText("这个朋友还没有注册过源音塘哦");
                    tvJustAdd.setText("直接添加\""+editable.toString()+"\"这个小伙伴");
                    tvInvite.setVisibility(View.VISIBLE);
                    tvJustAdd.setVisibility(View.VISIBLE);
                } else {
                    tvTips.setText("最近添加的小伙伴");
                    clear_txt.setVisibility(View.GONE);
                    tvNoDataDesc.setText("还没有关联过小伙伴哦");
                    tvInvite.setVisibility(View.GONE);
                    tvJustAdd.setVisibility(View.GONE);
                }
            }
        });
        adapter = new BaseBindingAdapter<MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean, ItemHistoryPartnerBinding>(this, dataList, R.layout.item_history_partner) {
            @Override
            public void bindObj(ItemHistoryPartnerBinding itemHistoryPartnerBinding, MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean) {

            }

            @Override
            public void operateView(View view, MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean) {
                super.operateView(view, memberBean);
                CircleImageView iv_head = view.findViewById(R.id.iv_head);
                SearTextView searTextView = view.findViewById(R.id.tv_nickname);
                if (memberBean.getHead_link() != null) {
                    ImageLoader.with(PartnerListActivity.this).getSize(100, 100).setUrl(memberBean.getHead_info()).into(iv_head);
                }
                if (!TextUtils.isEmpty(etSearch.getText())) {
                    searTextView.setSpecifiedTextsColor(memberBean.getNickname(), etSearch.getText().toString(), Color.parseColor("#ff6699"));
                }else{
                    searTextView.setText(memberBean.getNickname());
                }
            }
        };
        lvPartner.setAdapter(adapter);
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, @NonNull KeyEvent event) {
                if (NetConnectedUtils.isNetConnected(getApplicationContext())) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(PartnerListActivity.this.getCurrentFocus()
                                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        if (!TextUtils.isEmpty(etSearch.getText())) {
                            search(etSearch.getText().toString());
                        }
                    }
                }
                return false;
            }
        });
        lvPartner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setActivityResult(dataList.get(i));
            }
        });
    }

    private void setActivityResult(MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean) {
        Intent intent = new Intent();
        intent.putExtra(ADD_PARTNER_BEAN, memberBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void search(String nicknme) {
        page = 1;
        HttpParams params = new HttpParams();
        params.put("p", page + "");
        params.put("nickname", nicknme);
        NetWork.INSTANCE.searchPartner(this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                parseData(resultData);
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    @OnClick({R.id.finish, R.id.tv_invite,R.id.clear_txt, R.id.tv_just_add, R.id.tv_tips})
    public void onClick(View view) {
        MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean = new MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean();
        switch (view.getId()) {
            case R.id.finish:
                finish();
                break;
            case R.id.clear_txt:
                etSearch.getText().clear();
                break;
            case R.id.tv_tips:
                if (TextUtils.isEmpty(etSearch.getText())) return;
                memberBean.setNickname(etSearch.getText().toString().trim());
                setActivityResult(memberBean);
                finish();
                break;

            case R.id.tv_invite:
//                TODO
                goActivity(InviteOthersActivity.class);
                break;
            case R.id.tv_just_add:
                if (TextUtils.isEmpty(etSearch.getText())) return;
                memberBean.setNickname(etSearch.getText().toString().trim());
                setActivityResult(memberBean);
                finish();
                break;
        }
    }

    private static class TempBean {

        /**
         * code : 200
         * msg : success
         * data : [{"id":22649,"nickname":"1N6Fs"}]
         */

        private int code;
        private String msg;
        private List<DataBean> data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 22649
             * nickname : 1N6Fs
             */



            private int id;
            private String nickname;
            /**
             * head : 9f409438fc333bb7ff82cb21043bca8fdeaa08ba
             * head_link : http://testapi.imxkj.com//image/9f409438fc333bb7ff82cb21043bca8fdeaa08ba/3
             * head_info : {"ext":"jpg","w":"1000","h":"1000","size":"1013241","is_long":"0","md5":"9f409438fc333bb7ff82cb21043bca8fdeaa08ba","link":"http://testapi.imxkj.com//image/9f409438fc333bb7ff82cb21043bca8fdeaa08ba/3"}
             */

            private String head;
            private String head_link;
            private HeadInfoBean head_info;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getHead_link() {
                return head_link;
            }

            public void setHead_link(String head_link) {
                this.head_link = head_link;
            }

            public HeadInfoBean getHead_info() {
                return head_info;
            }

            public void setHead_info(HeadInfoBean head_info) {
                this.head_info = head_info;
            }

            public static class HeadInfoBean {
                /**
                 * ext : jpg
                 * w : 1000
                 * h : 1000
                 * size : 1013241
                 * is_long : 0
                 * md5 : 9f409438fc333bb7ff82cb21043bca8fdeaa08ba
                 * link : http://testapi.imxkj.com//image/9f409438fc333bb7ff82cb21043bca8fdeaa08ba/3
                 */

                private String ext;
                private String w;
                private String h;
                private String size;
                private String is_long;
                private String md5;
                private String link;

                public String getExt() {
                    return ext;
                }

                public void setExt(String ext) {
                    this.ext = ext;
                }

                public String getW() {
                    return w;
                }

                public void setW(String w) {
                    this.w = w;
                }

                public String getH() {
                    return h;
                }

                public void setH(String h) {
                    this.h = h;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }

                public String getIs_long() {
                    return is_long;
                }

                public void setIs_long(String is_long) {
                    this.is_long = is_long;
                }

                public String getMd5() {
                    return md5;
                }

                public void setMd5(String md5) {
                    this.md5 = md5;
                }

                public String getLink() {
                    return link;
                }

                public void setLink(String link) {
                    this.link = link;
                }
            }
        }
    }
}
