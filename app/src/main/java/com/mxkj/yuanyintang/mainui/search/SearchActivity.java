package com.mxkj.yuanyintang.mainui.search;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.adapter.BaseBindingAdapter;
import com.mxkj.yuanyintang.database.DBManager;
import com.mxkj.yuanyintang.databinding.ItemKeyWordsBinding;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.dynamic.widget.NoScrollListview;
import com.mxkj.yuanyintang.mainui.pond.widget.FlowLayout;
import com.mxkj.yuanyintang.utils.net.NetConnectedUtils;
import com.mxkj.yuanyintang.widget.SearTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

import static com.mxkj.yuanyintang.mainui.home.fragment.HomeFragment.SEARCH_WORDS;

public class SearchActivity extends StandardUiActivity {
    @BindView(R.id.et_search_info)
    EditText etSearchInfo;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    @BindView(R.id.search_hotWorld)
    FlowLayout searchHotWorld;
    @BindView(R.id.clear_history)
    ImageView clearHistory;
    @BindView(R.id.lv_sousuojilu)
    NoScrollListview lvSousuojilu;
    @BindView(R.id.lv_keyword)
    ListView listView;
    @BindView(R.id.layout_search)
    LinearLayout layout_search;
    @BindView(R.id.tv_search_content)
    TextView tv_search_content;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @NonNull
    private List<HotSearch.DataBean> types = new ArrayList<>();//大家都在搜
    @NonNull
    private List<String> searchList = new ArrayList<>();//搜索历史
    private SearchHisAdapter adpterSe;
    private DBManager dbManager;

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_search_myradiobutton;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
        dbManager = new DBManager(getApplicationContext());

    }

    @Override
    protected void initData() {
        String searchWord = getIntent().getStringExtra(SEARCH_WORDS);
        if (searchWord != null) {
            etSearchInfo.setHint(searchWord);
        }
        types.clear();
        NetWork.INSTANCE.getHotSearchWords(SearchActivity.this, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                types.clear();
                try {
                    JSONObject object = new JSONObject(resultData);
                    if (!object.opt("data").equals("")) {
                        HotSearch hotSearch = JSON.parseObject(resultData, HotSearch.class);
                        List<HotSearch.DataBean> data = hotSearch.getData();
                        types.addAll(data);
                        searchHotWorld.removeAllViews();
                        for (int i = 0; i < data.size(); i++) {
                            HotSearch.DataBean dataBean = data.get(i);
                            String title = null;
                            if (dataBean != null) {
                                title = dataBean.getTitle();
                            }
                            SearTextView tv = (SearTextView) LayoutInflater.from(SearchActivity.this).inflate(
                                    R.layout.search_label_tv, searchHotWorld, false);
                            //点击事件
                            final int finalI = i;
                            tv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                                    intent.putExtra(SEARCH_WORDS, types.get(finalI).getTitle());
                                    SearchActivity.this.startActivity(intent);
                                    dbManager.addSearchHistory(types.get(finalI).getTitle());
                                }
                            });
                            if (title != null) {
                                tv.setText(data.get(i).getTitle());
                            }
                            searchHotWorld.addView(tv);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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

    @Override
    protected void initEvent() {
        adpterSe = new SearchHisAdapter(SearchActivity.this, searchList);
        lvSousuojilu.setAdapter(adpterSe);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Drawable rDrawable = null;
                Drawable lDrawable = getResources().getDrawable(R.drawable.pond_search);
                /// 这一步必须要做,否则不会显示.
                lDrawable.setBounds(0, 0, lDrawable.getMinimumWidth(), lDrawable.getMinimumHeight());
                if (editable.length() == 0) {
                    etSearchInfo.setCompoundDrawables(lDrawable, null, null, null);
                } else {
                    rDrawable = getResources().getDrawable(R.drawable.dele_text);
                    /// 这一步必须要做,否则不会显示.
                    rDrawable.setBounds(0, 0, rDrawable.getMinimumWidth(), rDrawable.getMinimumHeight());
                    etSearchInfo.setCompoundDrawables(lDrawable, null, rDrawable, null);
                }


                if (etSearchInfo.getText().length() > 0) {
                    tvFinish.setText("搜索");
                    layout_search.setVisibility(View.VISIBLE);
                    tv_search_content.setText("搜索“" + editable.toString() + "”");
                    NetWork.INSTANCE.searchPopWord(SearchActivity.this, "1", etSearchInfo.getText().toString(), 1, new NetWork.TokenCallBack() {
                        @Override
                        public void doNext(String resultData, Headers headers) {
                            PopWordBean popWordBean = JSON.parseObject(resultData, PopWordBean.class);
                            final List<PopWordBean.DataBean> data = popWordBean.getData();
                            BaseBindingAdapter searPopAdapter = new BaseBindingAdapter<PopWordBean.DataBean, ItemKeyWordsBinding>(SearchActivity.this, data, R.layout.item_key_words) {
                                @Override
                                public void bindObj(ItemKeyWordsBinding itemKeyWordsBinding, PopWordBean.DataBean dataBean) {
                                    itemKeyWordsBinding.setObj(dataBean);
                                }

                                @Override
                                public void operateView(View view, PopWordBean.DataBean dataBean) {
                                    super.operateView(view, dataBean);
                                    SearTextView textView = view.findViewById(R.id.tv);
                                    textView.setSpecifiedTextsColor(dataBean.getTitle(), etSearchInfo.getText().toString(), Color.parseColor("#ff6699"));
                                }
                            };
                            listView.setAdapter(searPopAdapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
//                                    intent.putExtra("title", etSearchInfo.getText().toString().trim());
                                    intent.putExtra(SEARCH_WORDS, data.get(i).getTitle());
                                    startActivity(intent);
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
                }else{
                    layout_search.setVisibility(View.GONE);
                }

            }
        };
        etSearchInfo.addTextChangedListener(textWatcher);
        //使用键盘的回车当搜所
        etSearchInfo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, @NonNull KeyEvent event) {
                if (NetConnectedUtils.isNetConnected(getApplicationContext())) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        if (!TextUtils.isEmpty(etSearchInfo.getText().toString().trim())) {
                            dbManager.addSearchHistory(etSearchInfo.getText().toString().trim());
                            Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                            intent.putExtra(SEARCH_WORDS, etSearchInfo.getText().toString().trim());
                            startActivity(intent);
                        } else {
//                            ToastUtil.showToast(getApplicationContext(), "你还没有输入搜索内容哦~~");
                        }
                    }
                }
                return false;
            }
        });
        etSearchInfo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Drawable drawableRight = etSearchInfo.getCompoundDrawables()[2];
                        if (drawableRight != null && motionEvent.getRawX() >= (etSearchInfo.getRight() - drawableRight.getBounds().width())) {
                            etSearchInfo.setText("");
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> strings = dbManager.querySearchhistory(2);
        searchList.clear();
        if (strings!=null) {
            searchList.addAll(strings);
        }
        Collections.reverse(searchList);
        adpterSe.notifyDataSetChanged();
    }

    @OnClick({R.id.tv_finish, R.id.clear_history, R.id.rl_back, R.id.layout_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_finish:
                if (!TextUtils.isEmpty(etSearchInfo.getText().toString().trim())) {
                    if (NetConnectedUtils.isNetConnected(getApplicationContext())) {
                        dbManager.addSearchHistory(etSearchInfo.getText().toString().trim());
                        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                        intent.putExtra(SEARCH_WORDS, etSearchInfo.getText().toString().trim());
                        startActivity(intent);
                    } else {
                        setSnackBar("请检查网络连接", "", R.drawable.icon_fails);
                    }
                } else {
//                    finish();
                    setSnackBar("输入搜索内容", "", R.drawable.icon_tips_bad);
                }
                break;
            case R.id.clear_history:
                dbManager.deleteAllInfo(2);//2代表删搜索历史
                searchList.clear();
                adpterSe.notifyDataSetChanged();
                break;
            case R.id.layout_search:
                layout_search.setVisibility(View.GONE);
                break;
        }
    }
}
