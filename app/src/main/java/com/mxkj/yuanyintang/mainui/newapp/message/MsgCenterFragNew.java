package com.mxkj.yuanyintang.mainui.newapp.message;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.flyco.dialog.widget.popup.BasePopup;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.jakewharton.rxbinding2.view.RxView;
import com.luck.picture.lib.tools.Constant;
import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.base.dialog.BaseConfirmDialog;
import com.mxkj.yuanyintang.base.dialog.BaseThreeConfirmDialog;
import com.mxkj.yuanyintang.mainui.message.activity.CommentsListActivity;
import com.mxkj.yuanyintang.mainui.message.activity.MessageCenterActivity;
import com.mxkj.yuanyintang.mainui.message.activity.MessageSettingActivity;
import com.mxkj.yuanyintang.mainui.message.activity.SystemMessageListActivity;
import com.mxkj.yuanyintang.mainui.message.adapter.MessageCenterAdapter;
import com.mxkj.yuanyintang.mainui.message.adapter.MessageCenterAdapterNew;
import com.mxkj.yuanyintang.mainui.message.bean.MessageEMMDataBean;
import com.mxkj.yuanyintang.mainui.message.bean.MsgListean;
import com.mxkj.yuanyintang.mainui.myself.activity.SuggestActivity;
import com.mxkj.yuanyintang.mainui.newapp.message.GiftListActivity;
import com.mxkj.yuanyintang.net.NetWork;
import com.mxkj.yuanyintang.utils.app.CommonUtils;
import com.mxkj.yuanyintang.utils.rxbus.RxBus;
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber;
import com.mxkj.yuanyintang.utils.rxbus.event.EMECodeEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.umeng.analytics.MobclickAgent;
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
import butterknife.Unbinder;
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
public class MsgCenterFragNew extends RxFragment {
    @BindView(R.id.recyclerView)
    SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.iv_msg_set)
    ImageView iv_msg_set;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rl_toolbar;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    @BindView(R.id.layout_not_data_view)
    LinearLayout layout_not_data_view;
    @BindView(R.id.layout_not_data_view_bg)
    LinearLayout layout_not_data_view_bg;
    private View rootView;
    private Unbinder unbinder;
    MessageCenterAdapter messageCenterAdapter;
    private List<EMConversation> emconversationList = new ArrayList<>();
    private List<EMConversation> notSticktionList = new ArrayList<>();
    private Disposable mEMEEvent;
    public static final String TITLE = "_title";
    public static final String TYPE = "_type";
    private float textSize;

    TextView tv_msg_count_show,tv_msg_count,tv_msg_count_sysmsg;

    boolean notDisturbflag;

    private String top = "置顶";
    private int count = 1;

    String sysitemid="";
    String commentsitemid="";
    //长按删除操作是通过适配器吧potion穿过去，在通过handle传过来做删除等操作
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    Bundle bundle = msg.getData();
                    final int po = bundle.getInt("position");
                    Log.i("rrrr",""+po);

                    BaseThreeConfirmDialog.Companion.newInstance().title("").content("").confirmText(top).notText("消息免打扰").moveText("移除").cancleText("取消").showDialog(getActivity(),
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
                                    BaseConfirmDialog.Companion.newInstance().title("提示").content("是否移除当前聊天窗口？").confirmText("移除").cancleText("取消").showDialog(getActivity(), new BaseConfirmDialog.onBtClick() {

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
                case 11:
                    Bundle bundle1 = msg.getData();
                    final int mesagenumber = bundle1.getInt("mesagenumber");
                    commentsitemid = bundle1.getString("commentsitemid");
                    Log.i("rrrr",""+mesagenumber);
                    if(emconversationList.size() > 0){
                        if(mesagenumber <= 0){
                            tv_msg_count.setVisibility(View.GONE);
                        }else {
                            tv_msg_count.setVisibility(View.VISIBLE);
                            tv_msg_count.setText(mesagenumber > 99 ? "99+" : mesagenumber + "");
                        }

                    }else {

                    }
                    break;
                case 12:
                    Bundle bundle2 = msg.getData();
                    final int syamesagenumber = bundle2.getInt("syamesagenumber");
                    sysitemid = bundle2.getString("itemid");
                    Log.i("rrrr",""+syamesagenumber);
                    if(emconversationList.size() > 0){
                        if(syamesagenumber <= 0){
                            tv_msg_count_sysmsg.setVisibility(View.GONE);
                        }else {
                            tv_msg_count_sysmsg.setVisibility(View.VISIBLE);
                            tv_msg_count_sysmsg.setText(syamesagenumber > 99 ? "99+" : syamesagenumber + "");
                        }

                    }else {

                    }
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_msg, container, false);
        }
        textSize = CommonUtils.INSTANCE.dip2px(getActivity(), 13);
        unbinder = ButterKnife.bind(this, rootView);
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initView();
        initEvents();
        initRecyclerView();

        return rootView;
    }

    private void initRecyclerView() {
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.msg_header, null);
        //评论右上角的评论数量
        tv_msg_count_show = headView.findViewById(R.id.tv_msg_count_show);
        tv_msg_count = headView.findViewById(R.id.tv_msg_count);
        tv_msg_count_sysmsg = headView.findViewById(R.id.tv_msg_count_sysmsg);

        recyclerView.setLayoutManager(new RecyclerViewNoBugLinearLayoutManager(getActivity()));
        if (recyclerView.getAdapter() == null) {
            messageCenterAdapter = new MessageCenterAdapter(emconversationList, getActivity(),myHandler);
            messageCenterAdapter.addHeaderView(headView);
            recyclerView.setSwipeMenuCreator(swipeMenuCreator);
            recyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
                @Override
                public void onItemClick(SwipeMenuBridge menuBridge) {
                    menuBridge.closeMenu();
                    int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
                    int adapterPosition = menuBridge.getAdapterPosition() - messageCenterAdapter.getHeaderLayoutCount(); // 菜单在RecyclerView的Item中的Position。
                    if (emconversationList != null && emconversationList.size() > adapterPosition && emconversationList.get(adapterPosition).getLastMessage().getBooleanAttribute("item_type", false)) {
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
            recyclerView.setAdapter(messageCenterAdapter);
        } else {
            messageCenterAdapter.setNewData(emconversationList);
        }
        refreshEmData();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ViewGroup.LayoutParams layoutParams = rl_toolbar.getLayoutParams();
                Log.e("TAG", "onScrolledT: " + textSize);
                int height = rl_toolbar.getHeight();
                if (dy > 0) {
                    if (height > CommonUtils.INSTANCE.dip2px(getActivity(), 40)) {
                        layoutParams.height = --height;
                    }
                    if (textSize > CommonUtils.INSTANCE.dip2px(getActivity(), 9)) {
                        tv_title.setTextSize(--textSize);
                    }
                } else {
                    LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int firstVisibleItemPosition = l.findFirstVisibleItemPosition();
                    if (firstVisibleItemPosition != 0) return;
                    if (height < CommonUtils.INSTANCE.dip2px(getActivity(), 58)) {
                        layoutParams.height = ++height;
                    }
                    if (textSize < CommonUtils.INSTANCE.dip2px(getActivity(), 12)) {
                        tv_title.setTextSize(++textSize);
                    }
                }
                rl_toolbar.setLayoutParams(layoutParams);
            }
        });

        RxView.clicks(headView.findViewById(R.id.llComment)).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                MobclickAgent.onEvent(getActivity(),"msg_comment");
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(commentsitemid);;
                if (conversation != null) {
                    conversation.markAllMessagesAsRead();
                    tv_msg_count.setVisibility(View.GONE);
                }
                startActivity(new Intent(getActivity(), CommentsListActivity.class));
            }
        });
        RxView.clicks(headView.findViewById(R.id.llSysMsg)).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                MobclickAgent.onEvent(getActivity(),"msg_system");
                Log.e("",""+sysitemid);
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(sysitemid);;
                if (conversation != null) {
                    conversation.markAllMessagesAsRead();
                    tv_msg_count_sysmsg.setVisibility(View.GONE);
                }
                startActivity(new Intent(getActivity(), SystemMessageListActivity.class));
            }
        });
        RxView.clicks(headView.findViewById(R.id.llGift)).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                MobclickAgent.onEvent(getActivity(),"msg_gift");
                startActivity(new Intent(getActivity(), GiftListActivity.class));
            }
        });
    }

    private void refreshEmData() {
        emconversationList.clear();
        notSticktionList.clear();
        notSticktionList.addAll(loadConversationList());
        notSticktionList.addAll(sysloadConversationList());

        emconversationList.addAll(loadConversationList());
        sortSTickList(loadConversationList());
        sortSTickList(sysloadConversationList());
    }

    private void notDisturb(int adapterPosition) {
        EMConversation emms = emconversationList.get(adapterPosition);
        if (emms.getLastMessage().getBooleanAttribute("isNotDisturb", false)) {
            emms.getLastMessage().setAttribute("isNotDisturb", false);
            notDisturbflag = false;
        } else {
            emms.getLastMessage().setAttribute("isNotDisturb", true);
            notDisturbflag = true;
        }
//        refreshEmData();
        emconversationList.clear();
        notSticktionList.clear();
        notSticktionList.addAll(loadConversationList());
        emconversationList.addAll(loadConversationList());
        sortSTickList(loadConversationList());
        EMClient.getInstance().chatManager().importMessages(emms.getAllMessages());
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
//        refreshEmData();
        emconversationList.clear();
        notSticktionList.clear();
        notSticktionList.addAll(loadConversationList());
        emconversationList.addAll(loadConversationList());
        sortSTickList(loadConversationList());

    }
    private void del(int adapterPosition) {
        if (TextUtils.isEmpty(emconversationList.get(adapterPosition).getLastMessage().getUserName())) {
            return;
        }
        EMClient.getInstance().chatManager().deleteConversation(emconversationList.get(adapterPosition).getLastMessage().getUserName(), true);
        emconversationList.remove(adapterPosition);
        messageCenterAdapter.setNewData(emconversationList);
        isNotDataView();
        notDisturbflag = false;
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
                        return ((int) (emConversation.getLastMessage().getLongAttribute("stickTime", 0L) - t1.getLastMessage().getLongAttribute("stickTime", 0L)));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<EMConversation>>() {
                    @Override
                    public void accept(@NonNull List<EMConversation> emConversations) throws Exception {
                        emconversationList.clear();
                        emconversationList.addAll(emConversations);
                        emconversationList.addAll(notSticktionList);
                        recyclerView.removeAllViews();

                        messageCenterAdapter.setNewData(emconversationList);
                        isNotDataView();
                    }
                });
    }
    private void isNotDataView() {
        if (emconversationList.size() > 0 && emconversationList.size() == 1 && notDisturbflag ==true) {
//            layout_not_data_view.setVisibility(View.VISIBLE);
//            layout_not_data_view_bg.setVisibility(View.VISIBLE);
//            tv_no_data.setText("还没有聊天，或许你应该更主动点~");
        } else if(emconversationList.size() > 0 && emconversationList.size() == 1){
            layout_not_data_view.setVisibility(View.VISIBLE);
            layout_not_data_view_bg.setVisibility(View.VISIBLE);
            tv_no_data.setText("还没有聊天，或许你应该更主动点~");
        }
        else if(emconversationList.size() > 0){
            layout_not_data_view.setVisibility(View.GONE);
            layout_not_data_view_bg.setVisibility(View.GONE);
        }else {
//            TODO
            layout_not_data_view.setVisibility(View.VISIBLE);
            layout_not_data_view_bg.setVisibility(View.VISIBLE);
            tv_no_data.setText("还没有聊天，或许你应该更主动点~");
            EMClient.getInstance().chatManager().markAllConversationsAsRead();
        }
    }
    private void initEvents() {
        mEMEEvent = RxBus.getDefault().toObservable(EMECodeEvent.class)
                .subscribeWith(new RxBusSubscriber<EMECodeEvent>() {
                    @Override
                    protected void onEvent(EMECodeEvent emeCodeEvent) throws Exception {
                        Log.e("TAG", "onEvent: " + emeCodeEvent.getCode());
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
//                                if (!TextUtils.equals("4", type)) {
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            RxBus.getDefault().post(new SelectTabChangeEvent(1));
//                                        }
//                                    }, 500);
//                                }
//                                finish();
                                break;
                        }
                    }
                });
        RxBus.getDefault().add(mEMEEvent);
    }

    private void initView() {
        tv_title.setTextSize(textSize);

        RxView.clicks(iv_msg_set)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        MobclickAgent.onEvent(getActivity(),"msg_more");
                        recyclerView.smoothCloseMenu();
                        SimpleCustomPop mQuickCustomPopup = new SimpleCustomPop(getActivity());
                        mQuickCustomPopup.anchorView(iv_msg_set)
                                .offset(0, 0)
                                .gravity(Gravity.BOTTOM)
                                .showAnim(null)
                                .dismissAnim(null)
                                .dimEnabled(true)
                                .show();
                    }
                });

    }

    /**
     * 获取消息列表
     */
    protected List<EMConversation> loadConversationList() {
        EMMessage msg1 = createReceivedTextMsg("msgBox");
        EMClient.getInstance().chatManager().saveMessage(msg1);

        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            if (!sortItem.second.getLastMessage().getBooleanAttribute("isNotDisturb", false)) {
                list.add(sortItem.second);
            }
        }
        return list;
    }
    private int disturbUnreadCount = 0;
    private int disturbUnreadUnreadListCount = 0;
    protected List<EMConversation> sysloadConversationList() {
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
                        if (TextUtils.isEmpty("4")) {
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
                            getActivity().startActivity(new Intent(getActivity(), MessageSettingActivity.class));
                            dismiss();
                        }
                    });
        }
    }

    //创建一条消息盒子
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        RxBus.getDefault().remove(mEMEEvent);
    }

    public class RecyclerViewNoBugLinearLayoutManager extends LinearLayoutManager {
        public RecyclerViewNoBugLinearLayoutManager(Context context) {
            super(context);
        }

        public RecyclerViewNoBugLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public RecyclerViewNoBugLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                //try catch一下
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }
    }

    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dimen_60);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            if (emconversationList.size() > viewType && emconversationList.get(viewType) != null && emconversationList.get(viewType).getLastMessage() != null) {
                if (emconversationList.get(viewType).getLastMessage().getBooleanAttribute("isStick", false)) {
                    SwipeMenuItem sticItem = new SwipeMenuItem(getActivity())
                            .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey_cc_text))
                            .setText("取消置顶")
                            .setTextColor(Color.WHITE)
                            .setTextSize(12)
                            .setWidth(width)
                            .setHeight(height);
                    swipeRightMenu.addMenuItem(sticItem);
                } else {
                    SwipeMenuItem sticItem = new SwipeMenuItem(getActivity())
                            .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.grey_cc_text))
                            .setText("  置顶")
                            .setTextColor(Color.WHITE)
                            .setTextSize(12)
                            .setWidth(width)
                            .setHeight(height);
                    swipeRightMenu.addMenuItem(sticItem);
                }
//                if (!emconversationList.get(viewType).getLastMessage().getBooleanAttribute("isMsgBox",false)) {
                if (emconversationList.get(viewType).getLastMessage().getBooleanAttribute("isNotDisturb", false)) {
                    SwipeMenuItem notDisturbItem = new SwipeMenuItem(getActivity())
                            .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blue_c4))
                            .setText("  取消免打扰")
                            .setTextColor(Color.WHITE)
                            .setTextSize(12)
                            .setWidth(getResources().getDimensionPixelSize(R.dimen.dimen_96))
                            .setHeight(height);
                    swipeRightMenu.addMenuItem(notDisturbItem);
                } else {
                    SwipeMenuItem notDisturbItem = new SwipeMenuItem(getActivity())
                            .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blue_c4))
                            .setText("  消息免打扰")
                            .setTextColor(Color.WHITE)
                            .setTextSize(12)
                            .setWidth(getResources().getDimensionPixelSize(R.dimen.dimen_96))
                            .setHeight(height);
                    swipeRightMenu.addMenuItem(notDisturbItem);
                }
//                }

                if (TextUtils.isEmpty(emconversationList.get(viewType).getLastMessage().getStringAttribute("item_type", ""))) {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity())
                            .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.base_red))
                            .setText("  移除")
                            .setTextColor(Color.WHITE)
                            .setTextSize(12)
                            .setWidth(width)
                            .setHeight(height);
                    swipeRightMenu.addMenuItem(deleteItem);
                }
//            else if (TextUtils.equals("4", emconversationList.get(viewType).getLastMessage().getStringAttribute("item_type", ""))) {
                else {
                    SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity())
                            .setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.base_red))
                            .setText("  移除")
                            .setTextColor(Color.WHITE)
                            .setTextSize(12)
                            .setWidth(width)
                            .setHeight(height);
                    swipeRightMenu.addMenuItem(deleteItem);
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        refreshEmData();
    }
}
