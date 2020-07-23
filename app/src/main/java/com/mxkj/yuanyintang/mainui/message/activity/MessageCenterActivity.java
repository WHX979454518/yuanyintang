package com.mxkj.yuanyintang.mainui.message.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.dialog.widget.popup.BasePopup;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.jakewharton.rxbinding2.view.RxView;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.activity.StandardUiActivity;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.base.dialog.BaseThreeConfirmDialog;
import com.mxkj.yuanyintang.mainui.message.adapter.MessageCenterAdapter;
import com.mxkj.yuanyintang.mainui.message.bean.MessageEMMDataBean;
import com.mxkj.yuanyintang.mainui.message.bean.MsgListean;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber;
import com.mxkj.yuanyintang.utils.rxbus.event.EMECodeEvent;
import com.mxkj.yuanyintang.utils.rxbus.event.SelectTabChangeEvent;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;

/**
 * Created by LiuJie on 2017/10/21.
 * （后台接口修改）新增公告消息，
 * 评论消息列表页面重写，
 * 系统消息、公告消息列表页面重写
 */

public class MessageCenterActivity extends StandardUiActivity {
    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.layout_not_data_view)
    LinearLayout layout_not_data_view;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    MessageCenterAdapter messageCenterAdapter;

    private List<EMConversation> emconversationList = new ArrayList<>();
    private List<EMConversation> notSticktionList = new ArrayList<>();
    private Disposable mEMEEvent;
    private int disturbUnreadCount = 0;
    private int disturbUnreadUnreadListCount = 0;
    public static final String TITLE = "_title";
    public static final String TYPE = "_type";
    private String type = "";//TODO by LiuJie:目前状态是4，只用于判断消息盒子页面,1是公告消息
    private Boolean isMsgBoxNotDisturb = false;// TODO by LiuJie:是否在消息盒子操作过免打扰


    private String top = "置顶";
    private int count = 1;
    //长按删除操作是通过适配器吧potion穿过去，在通过handle传过来做删除等操作
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    Bundle bundle = msg.getData();
                    final int po = bundle.getInt("position");
                    Log.i("rrrr",""+po);

                    BaseThreeConfirmDialog.Companion.newInstance().title("").content("").confirmText(top).notText("取消免打扰").moveText("移除").cancleText("取消").showDialog(MessageCenterActivity.this,
                            new BaseThreeConfirmDialog.onBtClick() {
                                @Override
                                public void onTop() {
                                    stick(po);
                                    count++;
                                    for(int i=0;i<100;i++){
                                        if(count%2==0){
                                            top = "取消置顶";
                                        }
                                        if(count%2!=0){
                                            top = "置顶";
                                        }
                                    }
                                }

                                @Override
                                public void onNotdisturb() {
                                    notDisturb(po);
                                }

                                @Override
                                public void onConfirm() {
                                    BaseConfirmDialog.Companion.newInstance().title("提示").content("是否移除当前聊天窗口？").confirmText("移除").cancleText("取消").showDialog(MessageCenterActivity.this, new BaseConfirmDialog.onBtClick() {

                                        @Override
                                        public void onConfirm() {
                                            //Toast.makeText(getActivity(),"移除",Toast.LENGTH_SHORT).show();
                                            del(po);
                                        }

                                        @Override
                                        public void onCancle() {
                                            //Toast.makeText(getActivity(),"取消",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onCancle() {
                                    //Toast.makeText(getActivity(),"4",Toast.LENGTH_SHORT).show();
                                }
                            });
                    break;
            }
        }
    };



    @Override
    public int setLayoutId() {
        return R.layout.activity_message_center;
    }

    @Override
    public boolean isVisibilityBottomPlayer() {
        return false;
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        type = getIntent().getStringExtra(TYPE);
        String title = getIntent().getStringExtra(TITLE);
        initEmMsgData();
        if (TextUtils.isEmpty(title)) {
            setTitleText("消息中心");
        } else {
            setTitleText(title);
        }
        setRightButtonImageView(ContextCompat.getDrawable(this, R.drawable.icon_more_black));
        tv_no_data.setText("还没有聊天，或许你应该更主动点~");
        RxView.clicks(getNavigationBar().getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        onBackPressed();
                    }
                });
        RxView.clicks(getNavigationBar().getRightButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        recyclerView.smoothCloseMenu();
                        SimpleCustomPop mQuickCustomPopup = new SimpleCustomPop(MessageCenterActivity.this);
                        mQuickCustomPopup.anchorView(getNavigationBar().getRightButton())
                                .offset(0, 0)
                                .gravity(Gravity.BOTTOM)
                                .showAnim(null)
                                .dismissAnim(null)
                                .dimEnabled(true)
                                .show();
                    }
                });
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshEmData();
        messageCenterAdapter = new MessageCenterAdapter(emconversationList, this,myHandler) {
            @Override
            public int getItemViewType(int position) {
                return position;
            }
        };
        recyclerView.setAdapter(messageCenterAdapter);
        recyclerView.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        recyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                int adapterPosition = menuBridge.getAdapterPosition(); // 菜单在RecyclerView的Item中的Position。
                if (TextUtils.equals("4", emconversationList.get(adapterPosition).getLastMessage().getStringAttribute("item_type", ""))) {
                    switch (menuPosition) {
                        case 0://置顶
                            stick(adapterPosition);
                            break;
                        case 1://删除
                            del(adapterPosition);
                            break;
                    }
                } else {
                    switch (menuPosition) {
                        case 0://置顶
                            stick(adapterPosition);
                            break;
                        case 1://免打扰
                            notDisturb(adapterPosition);
                            break;
                        case 2:
                            del(adapterPosition);
                            break;
                    }
                }
            }
        });
    }

    private void notDisturb(int adapterPosition) {
        EMConversation emms = emconversationList.get(adapterPosition);
        if (emms.getLastMessage().getBooleanAttribute("isNotDisturb", false)) {
            isMsgBoxNotDisturb = true;
            emms.getLastMessage().setAttribute("isNotDisturb", false);
        } else {
            emms.getLastMessage().setAttribute("isNotDisturb", true);
        }
        EMClient.getInstance().chatManager().importMessages(emms.getAllMessages());
        refreshEmData();
    }

    private void stick(int adapterPosition) {
        EMConversation emms = emconversationList.get(adapterPosition);
        if (emms.getLastMessage().getBooleanAttribute("isStick", false)) {
            emms.getLastMessage().setAttribute("isStick", false);
        } else {
            emms.getLastMessage().setAttribute("stickTime", System.currentTimeMillis());
            emms.getLastMessage().setAttribute("isStick", true);
        }
        EMClient.getInstance().chatManager().importMessages(emms.getAllMessages());
        refreshEmData();
    }

    private void del(int adapterPosition) {
        if (TextUtils.isEmpty(emconversationList.get(adapterPosition).getLastMessage().getUserName())) {
            return;
        }
        EMClient.getInstance().chatManager().deleteConversation(emconversationList.get(adapterPosition).getLastMessage().getUserName(), true);
        messageCenterAdapter.notifyItemRemoved(adapterPosition);
        emconversationList.remove(adapterPosition);
        isNotDataView();
    }
    private void refreshEmData() {
        emconversationList.clear();
        notSticktionList.clear();
        notSticktionList.addAll(loadConversationList());
        sortSTickList(loadConversationList());
    }

    private void sortSTickList(final List<EMConversation> emConversationList) {
        for (int i = 0; i < notSticktionList.size(); i++) {
            if (notSticktionList.get(i).getLastMessage().getBooleanAttribute("isStick", false)) {
                notSticktionList.remove(i);
                i--;
            }
        }
        Observable.fromIterable(emConversationList)
                .filter(new Predicate<EMConversation>() {
                    @Override
                    public boolean test(@NonNull EMConversation emConversation) throws Exception {
                        return emConversation.getLastMessage().getBooleanAttribute("isStick", false);
                    }
                })
                .toSortedList(new Comparator<EMConversation>() {
                    @Override
                    public int compare(EMConversation emConversation, EMConversation t1) {
                        return (int) (emConversation.getLastMessage().getLongAttribute("stickTime", 0L) - t1.getLastMessage().getLongAttribute("stickTime", 0L));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<EMConversation>>() {
                    @Override
                    public void accept(@NonNull List<EMConversation> emConversations) throws Exception {
                        emconversationList.addAll(emConversations);
                        emconversationList.addAll(notSticktionList);
                        recyclerView.removeAllViews();
                        messageCenterAdapter.notifyDataSetChanged();
                        isNotDataView();
                    }
                });
    }

    private void isNotDataView() {
        if (emconversationList.size() > 0) {
            layout_not_data_view.setVisibility(View.GONE);
        } else {
            layout_not_data_view.setVisibility(View.VISIBLE);
            EMClient.getInstance().chatManager().markAllConversationsAsRead();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        mEMEEvent = RxBus.getDefault().toObservable(EMECodeEvent.class)
                .subscribeWith(new RxBusSubscriber<EMECodeEvent>() {
                    @Override
                    protected void onEvent(EMECodeEvent emeCodeEvent) throws Exception {
                        switch (emeCodeEvent.getCode()) {
                            case 100:
                                refreshEmData();
                                break;
                            case 1:
                                if (null != recyclerView) {
                                    recyclerView.removeAllViews();
                                    refreshEmData();
                                }
                                break;
                            case 997:
                                if (!TextUtils.equals("4", type)) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            RxBus.getDefault().post(new SelectTabChangeEvent(1));
                                        }
                                    }, 500);
                                }
                                finish();
                                break;
                        }
                    }
                });
        RxBus.getDefault().add(mEMEEvent);
    }

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        disturbUnreadCount = 0;
        disturbUnreadUnreadListCount = 0;//未阅读的会话条数
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    if (!TextUtils.equals("2", conversation.getLastMessage().getStringAttribute("item_type", ""))) {
                        if (TextUtils.isEmpty(type)) {
                            if (!conversation.getLastMessage().getBooleanAttribute("isNotDisturb", false)) {
                                sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                            } else {
                                if (conversation.getUnreadMsgCount() > 0) {
                                    disturbUnreadUnreadListCount++;
                                    disturbUnreadCount += conversation.getUnreadMsgCount();
                                }
                            }
                        } else {
                            if (conversation.getLastMessage().getBooleanAttribute("isNotDisturb", false)) {
                                sortList.add(new Pair(conversation.getLastMessage().getMsgTime(), conversation));
                            }
                        }
                    }
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        if (conversations.containsKey("msgBox")) {
            Observable.fromArray(list)
                    .flatMap(new Function<List<EMConversation>, ObservableSource<EMConversation>>() {
                        @Override
                        public ObservableSource<EMConversation> apply(@NonNull List<EMConversation> emConversationList) throws Exception {
                            return Observable.fromIterable(emConversationList);
                        }
                    })
                    .doOnNext(new Consumer<EMConversation>() {
                        @Override
                        public void accept(@NonNull EMConversation emConversation) throws Exception {
                        }
                    })
                    .filter(new Predicate<EMConversation>() {
                        @Override
                        public boolean test(@NonNull EMConversation emConversation) throws Exception {
                            return TextUtils.equals("msgBox", emConversation.conversationId());
                        }
                    })
                    .observeOn(Schedulers.io())
                    .subscribe(new Consumer<EMConversation>() {
                        @Override
                        public void accept(@NonNull EMConversation emConversation) throws Exception {
                            emConversation.markAllMessagesAsRead();
                            emConversation.getLastMessage().setAttribute("isNotDisturbUnreadCount", disturbUnreadCount);
                            emConversation.getLastMessage().setAttribute("content", "[有" + disturbUnreadUnreadListCount + "条会话未阅读]");
                            EMClient.getInstance().chatManager().importMessages(emConversation.getAllMessages());
                        }
                    });
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {
                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();
    }

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {

        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dimen_60);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            if (emconversationList.get(viewType).getLastMessage().getBooleanAttribute("isStick", false)) {
                SwipeMenuItem sticItem = new SwipeMenuItem(MessageCenterActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(MessageCenterActivity.this, R.color.grey_cc_text))
                        .setText("取消置顶") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setTextSize(12)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(sticItem);
            } else {
                SwipeMenuItem sticItem = new SwipeMenuItem(MessageCenterActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(MessageCenterActivity.this, R.color.grey_cc_text))
                        .setText("  置顶") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setTextSize(12)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(sticItem);
            }

            if (!TextUtils.equals("4", emconversationList.get(viewType).getLastMessage().getStringAttribute("item_type", ""))) {
                if (emconversationList.get(viewType).getLastMessage().getBooleanAttribute("isNotDisturb", false)) {
                    SwipeMenuItem notDisturbItem = new SwipeMenuItem(MessageCenterActivity.this)
                            .setBackgroundColor(ContextCompat.getColor(MessageCenterActivity.this, R.color.blue_c4))
                            .setText("  取消免打扰")
                            .setTextColor(Color.WHITE)
                            .setTextSize(12)
                            .setWidth(getResources().getDimensionPixelSize(R.dimen.dimen_96))
                            .setHeight(height);
                    swipeRightMenu.addMenuItem(notDisturbItem);
                } else {
                    SwipeMenuItem notDisturbItem = new SwipeMenuItem(MessageCenterActivity.this)
                            .setBackgroundColor(ContextCompat.getColor(MessageCenterActivity.this, R.color.blue_c4))
                            .setText("  消息免打扰")
                            .setTextColor(Color.WHITE)
                            .setTextSize(12)
                            .setWidth(getResources().getDimensionPixelSize(R.dimen.dimen_96))
                            .setHeight(height);
                    swipeRightMenu.addMenuItem(notDisturbItem);
                }
            }

            if (TextUtils.isEmpty(emconversationList.get(viewType).getLastMessage().getStringAttribute("item_type", ""))) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(MessageCenterActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(MessageCenterActivity.this, R.color.base_red))
                        .setText("  移除")
                        .setTextColor(Color.WHITE)
                        .setTextSize(12)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);
            }
//            else if (TextUtils.equals("4", emconversationList.get(viewType).getLastMessage().getStringAttribute("item_type", ""))) {
            else {
                SwipeMenuItem deleteItem = new SwipeMenuItem(MessageCenterActivity.this)
                        .setBackgroundColor(ContextCompat.getColor(MessageCenterActivity.this, R.color.base_red))
                        .setText("  移除")
                        .setTextColor(Color.WHITE)
                        .setTextSize(12)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getDefault().remove(mEMEEvent);
    }

    class SimpleCustomPop extends BasePopup<SimpleCustomPop> {
        @BindView(R.id.tv_all_read)
        TextView tv_all_read;
        @BindView(R.id.tv_msg_setting)
        TextView tv_msg_setting;

        public SimpleCustomPop(Context context) {
            super(context);
            setCanceledOnTouchOutside(true);
        }

        @Override
        public View onCreatePopupView() {
            View inflate = View.inflate(mContext, R.layout.pop_layout_msg_sort, null);
            ButterKnife.bind(this, inflate);
            return inflate;
        }

        @Override
        public void setUiBeforShow() {
            RxView.clicks(tv_all_read)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            //所有未读消息数清零
                            EMClient.getInstance().chatManager().markAllConversationsAsRead();
                            refreshEmData();
                            dismiss();
                        }
                    });
            RxView.clicks(tv_msg_setting)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(@NonNull Object o) throws Exception {
                            goActivity(MessageSettingActivity.class);
                            dismiss();
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RxBus.getDefault().post(new SelectTabChangeEvent(1));
                }
            }, 500);
            finish();
        }
    }


    private void initEmMsgData() {
        initMsgData(1);
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        if (!conversations.containsKey("msgBox")) {
            EMMessage msg1 = createReceivedTextMsg("msgBox");
            msg1.setAttribute("isMsgBox", true);
            EMClient.getInstance().chatManager().saveMessage(msg1);
        }
        // 如果登录过，APP 长期在后台再进的时候也可能会导致加载到内存的群组和会话为空，可以在主页面的 oncreate 里也加上这两句代码，当然，更好的办法应该是放在程序的开屏页
        //如果不提前载入，会导致首次点击消息中心进入消息中心后页面空白（环信消息记录必须在进入聊天界面之前加入到内存中）
        EMClient.getInstance().chatManager().loadAllConversations();
        EMClient.getInstance().groupManager().loadAllGroups();

    }

    /**
     * 1,公告消息，2CommentsListActivity，3评论消息(环信的聊天记录需要提前加载进内存，否则第一次进入再加载会出现空白，详情看环信文档)
     */
    public void initMsgData(final int type) {
        NetWork.INSTANCE.getMsgList(this, 1, type, new NetWork.TokenCallBack() {
            @Override
            public void doNext(String resultData, Headers headers) {
                try {
                    MsgListean msgListean = JSON.parseObject(resultData, MsgListean.class);
                    MsgListean.DataBeanX data = msgListean.getData();
                    if (data != null) {
                        List<MsgListean.DataBeanX.DataBean> msgList = data.getData();
                        MsgListean.DataBeanX.CountBean count = data.getCount();
                        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();

                        if (type == 1 && !conversations.containsKey("msgBroadcast")) {//公告消息
                            if (msgList != null && msgList.size() > 0) {
                                MessageEMMDataBean messageDataBean = new MessageEMMDataBean();
                                messageDataBean.setContent(msgList.get(0).getBody());
                                messageDataBean.setCreate_time(msgList.get(0).getCreate_time());
                                MessageEMMDataBean.DataBean dataBean = new MessageEMMDataBean.DataBean();
                                dataBean.setId(msgList.get(0).getItem_id());
                                dataBean.setTitle(msgList.get(0).getTitle());
                                dataBean.setUid(msgList.get(0).getFrom_uid());
                                messageDataBean.setData(dataBean);
                                messageDataBean.setItem_id(String.valueOf(msgList.get(0).getItem_id()));
                                messageDataBean.setObj_type(String.valueOf(msgList.get(0).getType()));
                                String s = JSON.toJSONString(messageDataBean);
                                EMMessage msgBroadcast = createReceivedBroadcast("msgBroadcast", msgList.get(0).getCreate_time(), s);
                                EMClient.getInstance().chatManager().saveMessage(msgBroadcast);

                                EMClient.getInstance().chatManager().loadAllConversations();
                                EMClient.getInstance().groupManager().loadAllGroups();
                            }
                        }
                    }
                } catch (RuntimeException e) {
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

    //预先创建一条消息盒子
    private EMMessage createReceivedTextMsg(String from) {
        EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
        EMTextMessageBody body = new EMTextMessageBody("receive text msg " + System.currentTimeMillis());
        msg.addBody(body);
        msg.setFrom(from);
        msg.setTo("msgBox");
        msg.setAttribute("item_type", "4");
        msg.setAttribute("isMsgBox", true);
        msg.setAttribute("data", "");
        msg.setMsgTime(System.currentTimeMillis());
        return msg;
    }

    //预先创建一条公告消息
    private EMMessage createReceivedBroadcast(String from, long create_time, String content) {
        EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
        EMTextMessageBody body = new EMTextMessageBody("receive Broadcast msg " + System.currentTimeMillis());
        msg.addBody(body);
        msg.setFrom(from);
        msg.setTo("msgBroadcast");
        msg.setAttribute("item_type", "msgBroadcast");
        msg.setAttribute("isBroadcast", true);
        msg.setAttribute("isNotDisturb", false);
        msg.setAttribute("data", content);
        msg.setMsgTime(create_time);
        return msg;
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (TextUtils.equals("4", type) && isMsgBoxNotDisturb) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RxBus.getDefault().post(new EMECodeEvent(1));
                }
            }, 400);
        }
        RxBus.getDefault().post(new EMECodeEvent());
        finish();
    }

}
