package com.mxkj.yuanyintang.mainui.pond.widget;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.myself.activity.EditPersonalProfileActivity;
import com.mxkj.yuanyintang.mainui.myself.apdater.MyCollectionAdapter;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.DetailsActivity;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.DetailsActivityAdapter;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.HotQuestionsActivityGridviewAdapter;
import com.mxkj.yuanyintang.mainui.pond.adapter.ChoosePondTagAdapter;
import com.mxkj.yuanyintang.mainui.pond.adapter.ChoosePondTagGridviewAdapter;
import com.mxkj.yuanyintang.mainui.pond.bean.ChoosePlayListTagBean;
import com.mxkj.yuanyintang.mainui.search.SearchActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.mainui.pond.bean.PondHotTagBean;
import com.mxkj.yuanyintang.utils.uiutils.SetGridViewViewHeightBasedOnChildren;
import com.mxkj.yuanyintang.utils.uiutils.Toast;
import com.mxkj.yuanyintang.utils.file.CacheUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;

public class ChoosePondTag extends StandardUiActivity {
    @BindView(R.id.finish)
    TextView finish;
    @BindView(R.id.view_title)
    TextView viewTitle;
    @BindView(R.id.publish)
    TextView publish;
    //    @BindView(R.id.view_title)
//    TextView view_title;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.pond_tag_hot)
    FlowLayout pondTagHot;
    @BindView(R.id.pond_tag_diy)
    FlowLayout pondTagDiy;
    @BindView(R.id.et_tag_name)
    EditText etTagName;
    @BindView(R.id.add_diyTag)
    TextView addDiyTag;
    @BindView(R.id.tvSelectedNum)
    TextView tvSelectedNum;
    @BindView(R.id.playlist_name)
    ListView playlist_name;
    @BindView(R.id.playlist_name_list)
    GridView playlist_name_list;
    @BindView(R.id.choosetag_ed_tv)
    EditText choosetag_ed_tv;

    private List<PondHotTagBean.DataBean> selectedTagList = new ArrayList<>();//已选中的
    private int page = 1;
    private int maxTagNum = 3;
    private String type;

    Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//接收到完整的包裹
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    boolean flag = bundle.getBoolean("flag");
                    String tag = bundle.getString("tag");
                    int potion = bundle.getInt("potion");
//                    List list = Collections.singletonList(tag);
                    List<PondHotTagBean.DataBean> list = new ArrayList<>();//吧已选中的加入到新建的集合
                    PondHotTagBean.DataBean dataBean = new PondHotTagBean.DataBean();
                    dataBean.setTitle(tag);
                    list.add(dataBean);

                    if (flag) {
                        if (selectedTagList.size() < maxTagNum) {
                            selectedTagList.addAll(list);
                        } else {
//                            CheckBox ckTag= (CheckBox) playlist_name.getChildAt(potion).findViewById(R.id.ck_tag);
//                            ckTag.setChecked(false);
                            setSnackBar("最多选择" + maxTagNum + "个标签", "", R.drawable.icon_tips_bad);
                        }
                    } else {
                        for (int i = 0; i < selectedTagList.size(); i++) {
                            if(selectedTagList.get(i).getTitle().equals(tag)){
                                selectedTagList.remove(i);
                            }
                        }
                    }

                    ChoosePondTagGridviewAdapter choosePondTagGridviewAdapter = new ChoosePondTagGridviewAdapter(ChoosePondTag.this, selectedTagList);
                    playlist_name_list.setAdapter(choosePondTagGridviewAdapter);
                    SetGridViewViewHeightBasedOnChildren.setListViewHeightBasedOnChildren(playlist_name_list);
                    choosePondTagGridviewAdapter.notifyDataSetChanged() ;

                    tvSelectedNum.setText("(" + selectedTagList.size() + "/" + maxTagNum + ")");
                    break;
            }


        }
    };


    @Override
    public int setLayoutId() {
        return R.layout.activity_choose_pond_tag;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        hideTitle(true);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        String title = getIntent().getStringExtra("title");
        int num = getIntent().getIntExtra("num", -1);
        if (num > 0) {
            maxTagNum = num;
        }
        tvSelectedNum.setText("(0/" + maxTagNum + ")");


        choosetag_ed_tv.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //是否是回车键
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ChoosePondTag.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //搜索
                    search();
                    choosetag_ed_tv.setText("");
                }
                return false;
            }
        });
    }

    private void search(){
        List<PondHotTagBean.DataBean> list = new ArrayList<>();//吧已选中的加入到新建的集合
        PondHotTagBean.DataBean dataBean = new PondHotTagBean.DataBean();
        dataBean.setTitle(choosetag_ed_tv.getText().toString());
        list.add(dataBean);
        if (selectedTagList.size() < maxTagNum) {
            selectedTagList.addAll(list);
        } else {
//                            CheckBox ckTag= (CheckBox) playlist_name.getChildAt(potion).findViewById(R.id.ck_tag);
//                            ckTag.setChecked(false);
            setSnackBar("最多选择" + maxTagNum + "个标签", "", R.drawable.icon_tips_bad);
        }
        Log.e("jjjjjj",""+choosetag_ed_tv.getText().toString());
        for (int i = 0; i < selectedTagList.size(); i++) {
            Log.e("jjjjjj", "" + selectedTagList.get(i).getTitle());
        }
        ChoosePondTagGridviewAdapter choosePondTagGridviewAdapter = new ChoosePondTagGridviewAdapter(ChoosePondTag.this, selectedTagList);
        playlist_name_list.setAdapter(choosePondTagGridviewAdapter);
        SetGridViewViewHeightBasedOnChildren.setListViewHeightBasedOnChildren(playlist_name_list);
        choosePondTagGridviewAdapter.notifyDataSetChanged() ;
        tvSelectedNum.setText("(" + selectedTagList.size() + "/" + maxTagNum + ")");
    };

    @Override
    protected void initEvent() {
        try {
            String selectTagStr = CacheUtils.INSTANCE.getString(ChoosePondTag.this,"selectTopicTag", "");
            List list = CacheUtils.INSTANCE.String2SceneList(selectTagStr);
            selectedTagList.addAll(list);
            for (int i = 0; i < selectedTagList.size(); i++) {
                if (selectedTagList.get(i).getId() == 0) {
                    final LinearLayout ll = (LinearLayout) LayoutInflater.from(ChoosePondTag.this).inflate(
                            R.layout.choosepond_tag_diy, pondTagHot, false);
                    final TextView tv_tag = ll.findViewById(R.id.tv_tag);
                    RelativeLayout dele_diytag = ll.findViewById(R.id.dele_diytag);
                    tv_tag.setText(selectedTagList.get(i).getTitle());
                    dele_diytag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (int i = 0; i < selectedTagList.size(); i++) {
                                Log.e(TAG, "onClick: " + selectedTagList.get(i).getTitle());
                                Log.e(TAG, "onClick: " + tv_tag.getText().toString().trim());

                                if (selectedTagList.get(i).getTitle().equals(tv_tag.getText().toString().trim())) {
                                    selectedTagList.remove(i);
                                    tvSelectedNum.setText("(" + selectedTagList.size() + "/" + maxTagNum + ")");
                                    pondTagDiy.removeView(ll);

                                }
                            }
                        }
                    });
                    pondTagDiy.addView(ll);
                }
            }
            tvSelectedNum.setText("(" + selectedTagList.size() + "/" + maxTagNum + ")");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
        initHotTagData(page);
        ChoosePondTagGridviewAdapter choosePondTagGridviewAdapter = new ChoosePondTagGridviewAdapter(ChoosePondTag.this, selectedTagList);
        playlist_name_list.setAdapter(choosePondTagGridviewAdapter);
        SetGridViewViewHeightBasedOnChildren.setListViewHeightBasedOnChildren(playlist_name_list);
        choosePondTagGridviewAdapter.notifyDataSetChanged() ;
    }

    private void initHotTagData(final int page) {
        //这里是请求的原来的老数据，吧布局影藏了，没发请求
//        HttpParams params = new HttpParams();
//        params.put("p", page + "");
//        params.put("row", 20 + "");
//        Log.e(TAG, "initTag: " + params);
//        NetWork.INSTANCE.getPondHotTag(this, params, new NetWork.TokenCallBack() {
//
//            @Override
//            public void doNext(String resultData, Headers headers) {
//                PondHotTagBean pondHotTagBean = JSON.parseObject(resultData, PondHotTagBean.class);
//                List<PondHotTagBean.DataBean> data = pondHotTagBean.getData();
//                if (data != null && data.size() > 0) {
//                    initHotFlowView(data);
//                } else {
////                    loadMore.setText("没有更多啦");
//                }
//            }
//
//            @Override
//            public void doError(String msg) {
//                Log.e(TAG, "doError: " + msg);
//            }
//
//            @Override
//            public void doResult() {
//
//            }
//        });
        NetWork.INSTANCE.getPlayListTag(this, null, new NetWork.TokenCallBack() {

            @Override
            public void doNext(String resultData, Headers headers) {
                ChoosePlayListTagBean choosePlayListTagBean = JSON.parseObject(resultData, ChoosePlayListTagBean.class);
                List<ChoosePlayListTagBean.DataBean> data = choosePlayListTagBean.getData();
                if (data != null && data.size() > 0) {
                    initPlaylistFlowView(data);
                } else {
//                    loadMore.setText("没有更多啦");
                }
            }

            @Override
            public void doError(String msg) {
                Log.e(TAG, "doError: " + msg);
            }

            @Override
            public void doResult() {

            }
        });
    }

    private void initHotFlowView(final List<PondHotTagBean.DataBean> data) {
        pondTagHot.removeAllViews();
        for (int i = 0; i < data.size(); i++) {
            final CheckBox ckTag = (CheckBox) LayoutInflater.from(ChoosePondTag.this).inflate(
                    R.layout.choose_pond_tag_hottag, pondTagHot, false);
            //点击事件
            final int finalI = i;
            ckTag.setText(data.get(i).getTitle());
            for (int j = 0; j < selectedTagList.size(); j++) {
                if (selectedTagList.get(j).getTitle().equals(data.get(i).getTitle())) {
                    ckTag.setChecked(true);
                }
            }
            ckTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if (selectedTagList.size() < maxTagNum) {
                            selectedTagList.add(data.get(finalI));
                        } else {
                            ckTag.setChecked(false);
                            setSnackBar("最多选择" + maxTagNum + "个标签", "", R.drawable.icon_tips_bad);
                        }
                    } else {
                        for (int i = 0; i < selectedTagList.size(); i++) {
                            if (selectedTagList.get(i).getTitle().equals(data.get(finalI).getTitle())) {
                                selectedTagList.remove(i);
                            }
                        }
                    }
                    tvSelectedNum.setText("(" + selectedTagList.size() + "/" + maxTagNum + ")");
                }
            });
            pondTagHot.addView(ckTag);
        }
    }

    //新的歌单标签（里面有分类）
    private void initPlaylistFlowView(final List<ChoosePlayListTagBean.DataBean> data) {
        List<ChoosePlayListTagBean .DataBean> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            //这里先把分类的名字显示出来。在操作分类中的标签
            dataList.add(data.get(i));
        }
        ChoosePondTagAdapter choosePondTagAdapter = new ChoosePondTagAdapter(dataList,ChoosePondTag.this,myhandler);
        playlist_name.setAdapter(choosePondTagAdapter);
    }

    @OnClick({R.id.finish, R.id.publish, R.id.add_diyTag})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                CacheUtils.INSTANCE.setString(ChoosePondTag.this,"selectTopicTag", "");
                finish();
                break;
            case R.id.publish:
                String sceneList2String = null;
                try {
                    sceneList2String = CacheUtils.INSTANCE.SceneList2String(selectedTagList);
                    CacheUtils.INSTANCE.setString(ChoosePondTag.this,"selectTopicTag", sceneList2String);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(type)) {
                    Intent intent = new Intent();
                    intent.putExtra("data", (Serializable) selectedTagList);
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;
            case R.id.add_diyTag:
                if (!TextUtils.isEmpty(etTagName.getText())) {
                    final LinearLayout ll = (LinearLayout) LayoutInflater.from(ChoosePondTag.this).inflate(
                            R.layout.choosepond_tag_diy, pondTagHot, false);
                    final TextView tv_tag = ll.findViewById(R.id.tv_tag);
                    RelativeLayout dele_diytag = ll.findViewById(R.id.dele_diytag);
                    tv_tag.setText(etTagName.getText().toString().trim());
                    dele_diytag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (int i = 0; i < selectedTagList.size(); i++) {
                                Log.e(TAG, "onClick: " + selectedTagList.get(i).getTitle());
                                Log.e(TAG, "onClick: " + tv_tag.getText().toString().trim());
                                if (selectedTagList.get(i).getTitle().equals(tv_tag.getText().toString().trim())) {
                                    selectedTagList.remove(i);
                                    tvSelectedNum.setText("(" + selectedTagList.size() + "/" + maxTagNum + ")");
                                    pondTagDiy.removeView(ll);
                                }
                            }
                        }
                    });
                    PondHotTagBean.DataBean bean = new PondHotTagBean.DataBean();
                    bean.setId(0);
                    bean.setTitle(etTagName.getText().toString().trim());
                    if (selectedTagList.size() < maxTagNum) {
                        selectedTagList.add(bean);
                        pondTagDiy.addView(ll);
                        setSnackBar("添加成功", "", R.drawable.icon_success);
                    } else {
                        setSnackBar("标签个数已达上限", "", R.drawable.icon_tips_bad);
                    }
                    tvSelectedNum.setText("(" + selectedTagList.size() + "/" + maxTagNum + ")");
                    etTagName.getText().clear();
                } else {
                    Toast.create(ChoosePondTag.this).show("请输入自定义标签");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != pondTagHot) {
            pondTagHot.removeAllViews();
        }
    }
}
