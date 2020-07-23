package com.mxkj.yuanyintang.mainui.emotionkeyboard.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.adapter.EmotionGridViewAdapter;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.adapter.EmotionPagerAdapter;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.emotionkeyboardview.EmojiIndicatorView;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.DisplayUtils;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.EmotionUtils;
import com.mxkj.yuanyintang.mainui.emotionkeyboard.utils.GlobalOnItemClickManagerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:可替换的模板表情，gridview实现
 */
public class EmotiomComplateFragment extends BaseFragment {
    private EmotionPagerAdapter emotionPagerGvAdapter;
    private ViewPager vp_complate_emotion_layout;
    private EmojiIndicatorView ll_point_group;//表情面板对应的点列表
    private String emotion_map_type;

    /**
     * 创建与Fragment对象关联的View视图时调用
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_complate_emotion, container, false);
        Log.e(TAG, "表情====onCreateView: " );
        initView(rootView);
        initListener();
        return rootView;
    }

    /**
     * 初始化view控件
     */
    protected void initView(View rootView) {
        Log.e(TAG, "表情====initView: " );

        vp_complate_emotion_layout = (ViewPager) rootView.findViewById(R.id.vp_complate_emotion_layout);
        ll_point_group = (EmojiIndicatorView) rootView.findViewById(R.id.ll_point_group);
        //获取map的类型
        emotion_map_type = args.getString(FragmentFactory.EMOTION_MAP_TYPE);
        initEmotion();
    }

    /**
     * 初始化监听器
     */
    protected void initListener() {
        vp_complate_emotion_layout.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPagerPos = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ll_point_group.playByStartPointToNext(oldPagerPos, position);
                oldPagerPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private static final String TAG = "EmotiomComplateFragment";

    /**
     * 初始化表情面板
     * 思路：获取表情的总数，按每行存放7个表情，动态计算出每个表情所占的宽度大小（包含间距），
     * 而每个表情的高与宽应该是相等的，这里我们约定只存放3行
     * 每个面板最多存放7*3=21个表情，再减去一个删除键，即每个面板包含20个表情
     * 根据表情总数，循环创建多个容量为20的List，存放表情，对于大小不满20进行特殊
     * 处理即可。
     */
    private void initEmotion() {
        Log.e(TAG, "表情====initEmotion: " );

        // 获取屏幕宽度
        int screenWidth = DisplayUtils.getScreenWidthPixels(getActivity());
        // item的间距
        int spacing = DisplayUtils.dp2px(getActivity(), 10);
        // 动态计算item的宽度和高度
//        int itemWidth = (screenWidth - spacing * 7) / 6;
        int itemWidth = DisplayUtils.dp2px(getActivity(), 50);
        //动态计算gridview的总高度
        int gvHeight = itemWidth * 2 + spacing * 3;
//        int gvHeight = DisplayUtils.dp2px(getActivity(), 150);
        List<GridView> emotionViews = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>();
        // 遍历所有的表情的key
        Log.e(TAG, "initEmotion: ----------->" + (null == EmotionUtils.getEmojiMap(emotion_map_type,getActivity())));
        if (null == EmotionUtils.getEmojiMap(emotion_map_type,getActivity())) {
            return;
        }
        for (String emojiName : EmotionUtils.getEmojiMap(emotion_map_type,getActivity()).keySet()) {
            emotionNames.add(emojiName);
            // 每20个表情作为一组,同时添加到ViewPager对应的view集合中
            if (emotionNames.size() == 11) {
                GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
                emotionViews.add(gv);
                // 添加完一组表情,重新创建一个表情名字集合
                emotionNames = new ArrayList<>();
            }
        }

        // 判断最后是否有不足11个表情的剩余情况
        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
            emotionViews.add(gv);
        }

        //初始化指示器
        ll_point_group.initIndicator(emotionViews.size());
        // 将多个GridView添加显示到ViewPager中
        emotionPagerGvAdapter = new EmotionPagerAdapter(emotionViews);
        vp_complate_emotion_layout.setAdapter(emotionPagerGvAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
        vp_complate_emotion_layout.setLayoutParams(params);
    }

    /**
     * 创建显示表情的GridView
     */
    private GridView createEmotionGridView(final List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
        // 创建GridView
        final GridView gv = new GridView(getActivity());
        //设置点击背景透明
        gv.setSelector(android.R.color.transparent);
        //设置6列
        gv.setNumColumns(6);
//        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding);
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);
        // 给GridView设置表情图片
        EmotionGridViewAdapter adapter = new EmotionGridViewAdapter(getActivity(), emotionNames, itemWidth, emotion_map_type);
        gv.setAdapter(adapter);
        //设置全局点击事件
        gv.setOnItemClickListener(GlobalOnItemClickManagerUtils.getInstance(getActivity()).getOnItemClickListener(emotionNames));
        return gv;
    }
}
