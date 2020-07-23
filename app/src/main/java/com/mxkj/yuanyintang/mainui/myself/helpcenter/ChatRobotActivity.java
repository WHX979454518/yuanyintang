package com.mxkj.yuanyintang.mainui.myself.helpcenter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jakewharton.rxbinding2.view.RxView;
import com.lzy.okgo.model.HttpParams;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.mainui.myself.helpcenter.data.ChatHistoryBean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.constant.Constants;
import com.mxkj.yuanyintang.utils.file.CacheUtils;
import com.mxkj.yuanyintang.utils.layoutmanager.FullyLinearLayoutManager;
import com.mxkj.yuanyintang.utils.uiutils.SoftKeyBoardListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import okhttp3.Headers;

public class ChatRobotActivity extends StandardUiActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.et_question)
    EditText etQuestion;
    @BindView(R.id.tv_send)
    TextView tvSend;
    private List<ChatHistoryBean> chatHistoryLists = new ArrayList<>();
    private ChartRobotAdapter chartRobotAdapter;
    private String chatHistoryStr;
    android.os.Handler handler = new android.os.Handler();

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_chat_robot;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setTitleText("源小伊");
    }

    @Override
    protected void initData() {
        showChatHistory();
    }

    @Override
    protected void initEvent() {
        chartRobotAdapter = new ChartRobotAdapter(chatHistoryLists);
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        layoutManager.setAutoMeasureEnabled(true);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(chartRobotAdapter);
        RxView.clicks(tvSend).throttleFirst(2, TimeUnit.SECONDS).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (!TextUtils.isEmpty(etQuestion.getText())) {
                    askRobot();
                }
                etQuestion.getText().clear();

            }
        });

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recycler.smoothScrollToPosition(0);
                    }
                }, 100);
            }

            @Override
            public void keyBoardHide(int height) {

            }
        });


    }

    private void showChatHistory() {
        String chatString = CacheUtils.INSTANCE.getString(this, Constants.Other.YXY_CHAT_HISTOTY);
        if (TextUtils.isEmpty(chatString)) {
            askRobot();
            return;
        }
        try {
            List<ChatHistoryBean> list = (List<ChatHistoryBean>) CacheUtils.INSTANCE.String2SceneList(chatString);
            if (list != null && list.size() > 0) {
                chatHistoryLists.addAll(list);
                notifyAdapter();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void askRobot() {
        if (!TextUtils.isEmpty(etQuestion.getText())) {
            ChatHistoryBean chatHistoryBean = new ChatHistoryBean();
            chatHistoryBean.setMsgType(8);
            chatHistoryBean.setMsgText(etQuestion.getText().toString());
            chatHistoryLists.add(0, chatHistoryBean);
            if (chatHistoryLists.size() > 100) {
                chatHistoryLists.remove(chatHistoryLists.size() - 1);
            }
            notifyAdapter();
        }
        HttpParams params = new HttpParams("keyword", TextUtils.isEmpty(etQuestion.getText().toString()) ? "" : etQuestion.getText().toString());
        NetWork.INSTANCE.askRobot(this, params, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                initChatData(resultData);
            }

            @Override
            public void doError(String msg) {

            }

            @Override
            public void doResult() {

            }
        });
    }

    private void initChatData(String resultData) {
//        创建一条聊天记录
        ChatHistoryBean chatHistoryBean = new ChatHistoryBean();
        JSONObject resultObj = JSON.parseObject(resultData);
        JSONObject dataObj = resultObj.getJSONObject("data");
        Integer type = dataObj.getInteger("type");
        chatHistoryBean.setMsgType(type);
        /** 1 帮助中心内容
         2 文本内容
         3 链接
         4 新闻类
         5 菜谱类
         6 儿歌类
         7 诗词类*/
        switch (type) {
            case 1:
                JSONArray jsonArray = dataObj.getJSONArray("text");
                String jsonString = jsonArray.toJSONString();
                List<ChatHistoryBean.MutiTextBean> mutiTextBeans = JSON.parseArray(jsonString, ChatHistoryBean.MutiTextBean.class);
                chatHistoryBean.setList(mutiTextBeans);
                break;
            case 2:
                chatHistoryBean.setMsgText(dataObj.getString("text"));
                break;
            case 3:
                chatHistoryBean.setMsgText(dataObj.getString("text"));
                break;
            case 4://新闻
                JSONArray jsonArrayArtical = dataObj.getJSONArray("text");
                String jsonArticalStr = jsonArrayArtical.toJSONString();
                List<ChatHistoryBean.MutiTextBean> articalBeans = JSON.parseArray(jsonArticalStr, ChatHistoryBean.MutiTextBean.class);
                chatHistoryBean.setList(articalBeans);
                break;
            case 5://菜谱。。。。。。。
                JSONArray jsonArrayMenu = dataObj.getJSONArray("text");
                String jsonMenuStr = jsonArrayMenu.toJSONString();
                List<ChatHistoryBean.MutiTextBean> menuBeans = JSON.parseArray(jsonMenuStr, ChatHistoryBean.MutiTextBean.class);
                chatHistoryBean.setList(menuBeans);
                break;
            case 6:

                break;
            case 7:

                break;

        }

        if (chatHistoryLists.size() > 100) {
            chatHistoryLists.remove(chatHistoryLists.size() - 1);
        }
        chatHistoryLists.add(0, chatHistoryBean);
        notifyAdapter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            chatHistoryStr = CacheUtils.INSTANCE.SceneList2String(chatHistoryLists);
            if (chatHistoryStr == null) {
                return;
            }
            CacheUtils.INSTANCE.setString(this, Constants.Other.YXY_CHAT_HISTOTY, chatHistoryStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void notifyAdapter() {
        chartRobotAdapter.setNewData(chatHistoryLists);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recycler.smoothScrollToPosition(0);
            }
        }, 100);
    }
}
