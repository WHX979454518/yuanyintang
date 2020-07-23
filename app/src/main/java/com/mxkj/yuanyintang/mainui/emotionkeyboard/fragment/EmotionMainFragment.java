package com.mxkj.yuanyintang.mainui.emotionkeyboard.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.comment.SoftKeyboardStateHelper;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.adapter.HorizontalRecyclerviewAdapter;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.adapter.NoHorizontalScrollerVPAdapter;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmotionKeyboard;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.NoHorizontalScrollerViewPager;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.model.ImageModel;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.EmotionUtils;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.GlobalOnItemClickManagerUtils;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.SharedPreferencedUtils;

import java.util.ArrayList;
import java.util.List;

public class EmotionMainFragment extends BaseFragment {
    //是否绑定当前Bar的编辑框的flag
    public static final String BIND_TO_EDITTEXT = "bind_to_edittext";
    //是否隐藏bar上的编辑框和发生按钮
    public static final String HIDE_BAR_EDITTEXT_AND_BTN = "hide bar's editText and btn";
    //当前被选中底部tab
    private static final String CURRENT_POSITION_FLAG = "CURRENT_POSITION_FLAG";
    private int CurrentPosition = 0;
    //底部水平tab
    private RecyclerView recyclerview_horizontal;
    private HorizontalRecyclerviewAdapter horizontalRecyclerviewAdapter;
    //表情面板
    public EmotionKeyboard mEmotionKeyboard;
    public EditText bar_edit_text;
    private ImageView bar_image_add_btn;
    private TextView bar_btn_send;
    private LinearLayout rl_editbar_bg;

    //需要绑定的内容view
    private View contentView;

    //不可横向滚动的ViewPager
    private NoHorizontalScrollerViewPager viewPager;

    //是否绑定当前Bar的编辑框,默认true,即绑定。
    //false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
    private boolean isBindToBarEditText = true;

    //是否隐藏bar上的编辑框和发生按钮,默认不隐藏
    private boolean isHidenBarEditTextAndBtn = false;

    List<Fragment> fragments = new ArrayList<>();
    private SoftKeyboardStateHelper softKeyboardStateHelper;
    private LinearLayout ll_emotion_layout;
    private View include_emotion_view;


    /**
     * 创建与Fragment对象关联的View视图时调用
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_emotion, container, false);
        isHidenBarEditTextAndBtn = args.getBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN);
        //获取判断绑定对象的参数
        isBindToBarEditText = args.getBoolean(EmotionMainFragment.BIND_TO_EDITTEXT);
        initView(rootView);

        View viewById = rootView.findViewById(R.id.ll_emotion_layout);
        mEmotionKeyboard = EmotionKeyboard.with(getActivity())
                .setEmotionView(viewById)//绑定表情面板
                .bindToContent(contentView)//绑定内容view
                .bindToEditText(!isBindToBarEditText ? ((EditText) contentView) : ((EditText) rootView.findViewById(R.id.bar_edit_text)))//判断绑定那种EditView
                .bindToEmotionButton(rootView.findViewById(R.id.emotion_button))//绑定表情按钮
                .bindToSendButton(rootView.findViewById(R.id.bar_btn_send))//绑定发送按钮
                .build();
        mEmotionKeyboard.sendComment(args);
        initListener();
        initDatas();
        //创建全局监听
        GlobalOnItemClickManagerUtils globalOnItemClickManager = GlobalOnItemClickManagerUtils.getInstance(getActivity());
        if (isBindToBarEditText) {
            //绑定当前Bar的编辑框
            globalOnItemClickManager.attachToEditText(bar_edit_text);

        } else {
            // false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
            globalOnItemClickManager.attachToEditText((EditText) contentView);
            mEmotionKeyboard.bindToEditText((EditText) contentView);
        }
        return rootView;
    }

    /**
     * 绑定内容view
     *
     * @param contentView
     * @return
     */
    public void bindToContentView(View contentView) {
        this.contentView = contentView;
    }

    private static final String TAG = "EmotionMainFragment";

    /**
     * 初始化view控件
     */
    protected void initView(View rootView) {
        viewPager = (NoHorizontalScrollerViewPager) rootView.findViewById(R.id.vp_emotionview_layout);
        recyclerview_horizontal = (RecyclerView) rootView.findViewById(R.id.recyclerview_h);
        bar_edit_text = (EditText) rootView.findViewById(R.id.bar_edit_text);
        bar_image_add_btn = (ImageView) rootView.findViewById(R.id.bar_image_add_btn);
        bar_btn_send = (TextView) rootView.findViewById(R.id.bar_btn_send);
        rl_editbar_bg = (LinearLayout) rootView.findViewById(R.id.rl_editbar_bg);
        ll_emotion_layout = rootView.findViewById(R.id.ll_emotion_layout);
        include_emotion_view = rootView.findViewById(R.id.include_emotion_view);

        bar_edit_text.setFocusable(true);
        bar_edit_text.setFocusableInTouchMode(true);
        bar_edit_text.requestFocus();
        bar_edit_text.postDelayed(new Runnable() {
            @Override
            public void run() {
                bar_edit_text.requestFocus();
            }
        }, 200);
        softKeyboardStateHelper = new SoftKeyboardStateHelper(rootView.findViewById(R.id.ll_emoji));
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                mEmotionKeyboard.hideEmotionLayout(true);
                ll_emotion_layout.setVisibility(View.GONE);
            }

            @Override
            public void onSoftKeyboardClosed() {

            }
        });
        if (isHidenBarEditTextAndBtn) {//隐藏
            include_emotion_view.setVisibility(View.GONE);
        } else {
            include_emotion_view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化监听器
     */
    protected void initListener() {
    }

    protected void initDatas() {
        replaceFragment();
        List<ImageModel> list = new ArrayList<>();
//        for (int i=0 ; i<fragments.size(); i++){
//            if(i==0){
//                ImageModel model1=new ImageModel();
//                model1.icon= getResources().getDrawable(R.drawable.ic_emotion);
//                model1.flag="经典笑脸";
//                model1.isSelected=true;
//                list.add(model1);
//            }else {
//                ImageModel model = new ImageModel();
//                model.icon = getResources().getDrawable(R.drawable.ic_plus);
//                model.flag = "其他笑脸" + i;
//                model.isSelected = false;
//                list.add(model);
//            }
//        }

        //记录底部默认选中第一个
        CurrentPosition = 0;
        SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);
        //底部tab
        horizontalRecyclerviewAdapter = new HorizontalRecyclerviewAdapter(getActivity(), list);
        recyclerview_horizontal.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        recyclerview_horizontal.setAdapter(horizontalRecyclerviewAdapter);
        recyclerview_horizontal.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        //初始化recyclerview_horizontal监听器
        horizontalRecyclerviewAdapter.setOnClickItemListener(new HorizontalRecyclerviewAdapter.OnClickItemListener() {
            @Override
            public void onItemClick(View view, int position, List<ImageModel> datas) {
                //获取先前被点击tab
                int oldPosition = SharedPreferencedUtils.getInteger(getActivity(), CURRENT_POSITION_FLAG, 0);
                //修改背景颜色的标记
                datas.get(oldPosition).isSelected = false;
                //记录当前被选中tab下标
                CurrentPosition = position;
                datas.get(CurrentPosition).isSelected = true;
                SharedPreferencedUtils.setInteger(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);
                //通知更新，这里我们选择性更新就行了
                horizontalRecyclerviewAdapter.notifyItemChanged(oldPosition);
                horizontalRecyclerviewAdapter.notifyItemChanged(CurrentPosition);
                //viewpager界面切换
                viewPager.setCurrentItem(position, false);
            }

            @Override
            public void onItemLongClick(View view, int position, List<ImageModel> datas) {
            }
        });
    }

    private void replaceFragment() {
        //创建fragment的工厂类
        FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();
        //创建修改实例
        EmotiomComplateFragment f1 = (EmotiomComplateFragment) factory.getFragment(EmotionUtils.EMOTION_CLASSIC_TYPE);
        fragments.add(f1);
        NoHorizontalScrollerVPAdapter adapter = new NoHorizontalScrollerVPAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    /**
     * 是否拦截返回键操作，如果此时表情布局未隐藏，先隐藏表情布局
     *
     * @return true则隐藏表情布局，拦截返回键操作
     * false 则不拦截返回键操作
     */
    public boolean isInterceptBackPress() {
        return mEmotionKeyboard.interceptBackPress();
    }
}


