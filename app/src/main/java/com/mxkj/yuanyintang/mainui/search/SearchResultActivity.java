package com.mxkj.yuanyintang.mainui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.SousuoSlidingTabLayout;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.adapter.BaseBindingAdapter;
import com.mxkj.yuanyintang.database.DBManager;
import com.mxkj.yuanyintang.databinding.ItemKeyWordsBinding;
import com.mxkj.yuanyintang.mainui.search.fragment.MusicSearchList;
import com.mxkj.yuanyintang.mainui.search.fragment.PondSearchList;
import com.mxkj.yuanyintang.mainui.search.fragment.SheetSearchList;
import com.mxkj.yuanyintang.mainui.search.fragment.UserSearchList;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils;
import com.mxkj.yuanyintang.utils.string.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.SEARCH_WORDS;

public class SearchResultActivity extends StandardUiActivity {
    public static final String TO_SHEET_RESULT = "sheet_search_result";
    @BindView(R.id.et_search_info)
    EditText etSearchInfo;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.tab_dynamic)
    SousuoSlidingTabLayout tabDynamic;
    @BindView(R.id.viewpager_result)
    ViewPager viewpagerResult;
    @BindView(R.id.lv_keyword)
    ListView lvKeyword;
    @BindView(R.id.rl_mainview)
    RelativeLayout rlMainview;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titles = new String[4];
    private MusicSearchList musicResultFrag;
    private SheetSearchList sheetSearchList;
    private UserSearchList userSearchList;
    private PondSearchList pondSearchList;
    private String title;
    private BaseBindingAdapter<PopWordBean.DataBean, ItemKeyWordsBinding> searPopAdapter;
    private int position;

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_search_reault;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        hideTitle(true);
        title = StringUtils.isEmpty(getIntent().getStringExtra(SEARCH_WORDS));
        if (title != null) {
            etSearchInfo.setText(title);
            tvFinish.setText("搜索");
            etSearchInfo.setCursorVisible(false);
            etSearchInfo.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    etSearchInfo.setCursorVisible(true);
                    return false;
                }
            });
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        if (title != null) {
            titles[0] = ("单曲");
            titles[1] = ("歌单");
            titles[2] = ("用户");
            titles[3] = ("池塘");
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            musicResultFrag = new MusicSearchList();
            musicResultFrag.setArguments(bundle);
            sheetSearchList = new SheetSearchList();
            sheetSearchList.setArguments(bundle);
            userSearchList = new UserSearchList();
            userSearchList.setArguments(bundle);
            pondSearchList = new PondSearchList();
            pondSearchList.setArguments(bundle);
            fragments.clear();
            //音乐
            fragments.add(musicResultFrag);
            //歌单
            fragments.add(sheetSearchList);
            //用户
            fragments.add(userSearchList);
            //池塘
            fragments.add(pondSearchList);

            tabDynamic.setViewPager(viewpagerResult, titles, this, fragments);
            tabDynamic.updateTabSelection(0);
            if (getIntent().getStringExtra(TO_SHEET_RESULT) != null) {
                viewpagerResult.setCurrentItem(1, false);
            }
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvFinish.setText("搜索");
                lvKeyword.setVisibility(View.VISIBLE);
                title = etSearchInfo.getText().toString();
                NetWork.INSTANCE.searchPopWord(SearchResultActivity.this, "1", title, 1, new NetWork.TokenCallBack() {
                    @Override
                    public void doNext(String resultData, Headers headers) {
                        Log.i(TAG, "doNext: " + resultData);
                        PopWordBean popWordBean = JSON.parseObject(resultData, PopWordBean.class);
                        final List<PopWordBean.DataBean> data = popWordBean.getData();
                        searPopAdapter = new BaseBindingAdapter<PopWordBean.DataBean, ItemKeyWordsBinding>(SearchResultActivity.this, data, R.layout.item_key_words) {
                            @Override
                            public void bindObj(ItemKeyWordsBinding itemKeyWordsBinding, PopWordBean.DataBean dataBean) {
                                itemKeyWordsBinding.setObj(dataBean);
                            }
                        };
                        lvKeyword.setAdapter(searPopAdapter);
                        lvKeyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
//                                    intent.putExtra("title", etSearchInfo.getText().toString().trim());
                                intent.putExtra(SEARCH_WORDS, data.get(i).getTitle());
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void doError(String msg) {

                    }

                    @Override
                    public void doResult() {

                    }
                });

//                } else {
//                    tvFinish.setText("取消");
//                    lvKeyword.setVisibility(View.GONE);
//                    rlMainview.setVisibility(View.VISIBLE);
//                }
            }
        };
        etSearchInfo.addTextChangedListener(textWatcher);

        //使用键盘的回车当搜所
        etSearchInfo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, @NonNull KeyEvent event) {
                if (NetConnectedUtils.isNetConnected(getApplicationContext())) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchResultActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        if (!TextUtils.isEmpty(etSearchInfo.getText().toString().trim())) {
                            DBManager dbManager = new DBManager(getApplicationContext());
                            dbManager.addSearchHistory(etSearchInfo.getText().toString().trim());
                            title = etSearchInfo.getText().toString();
                            Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
                            intent.putExtra(SEARCH_WORDS, etSearchInfo.getText().toString().trim());
                            startActivity(intent);
                            finish();
                        } else {
//                            ToastUtil.showToast(getApplicationContext(), "你还没有输入搜索内容哦~~");
                        }
                    }
                }
                return false;
            }
        });
    }

    public void setCurrPage(int pos) {
        position = pos;
        viewpagerResult.setCurrentItem(pos);
        tabDynamic.updateTabSelection(pos);
    }

    public void setMusicCounts(int music) {
        if (titles[0] == null) return;
        titles[0] = "单曲 " + setUpperLimitNumber(music);
        tabDynamic.notifyDataSetChanged();
        tabDynamic.updateTabSelection(position);

    }

    public void setSheetCounts(int sheet) {
        if (titles[1] == null) return;
        titles[1] = "歌单 " + setUpperLimitNumber(sheet);
        tabDynamic.notifyDataSetChanged();
        tabDynamic.updateTabSelection(position);

    }

    public void setUserCounts(int user) {
        if (titles[2] == null) return;
        titles[2] = "用户 " + setUpperLimitNumber(user);
        tabDynamic.notifyDataSetChanged();
        tabDynamic.updateTabSelection(position);

    }

    public void setPondCounts(int pond) {
        if (titles[3] == null) return;
        titles[3] = "池塘 " + setUpperLimitNumber(pond);
        tabDynamic.notifyDataSetChanged();
        tabDynamic.updateTabSelection(position);

    }

    private String setUpperLimitNumber(int num) {
        if (num > 99) {
            return 99 + "+";
        }
        return num + "";
    }

    @OnClick({R.id.et_search_info, R.id.tv_finish, R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_search_info:
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_finish:
                if (!TextUtils.isEmpty(etSearchInfo.getText().toString().trim())) {
                    if (NetConnectedUtils.isNetConnected(getApplicationContext())) {
                        title = etSearchInfo.getText().toString();
                        Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
                        intent.putExtra(SEARCH_WORDS, etSearchInfo.getText().toString().trim());
                        startActivity(intent);
                        finish();
                    }
                } else {
//                    finish();
                    setSnackBar("输入搜索内容", "", R.drawable.icon_tips_bad);

                }
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(0, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!TextUtils.isEmpty(etSearchInfo.getText().toString().trim())) {
            if (NetConnectedUtils.isNetConnected(getApplicationContext())) {
                title = etSearchInfo.getText().toString();
                Intent intent = new Intent(SearchResultActivity.this, SearchResultActivity.class);
                intent.putExtra(SEARCH_WORDS, etSearchInfo.getText().toString().trim());
                startActivity(intent);
                finish();
            }
        } else {
            setSnackBar("输入搜索内容", "", R.drawable.icon_tips_bad);

        }
    }
}
