package com.mxkj.yuanyintang.mainui.myself.doughnut;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;

/**
 * Created by zuixia on 2018/6/5.
 */

public class ExpandableLinearLayout extends LinearLayout implements View.OnClickListener {
    private static final String TAG = ExpandableLinearLayout.class.getSimpleName();
    private TextView tvTip;
    private ImageView img_tips;
    private boolean isExpand = false;//是否是展开状态，默认是隐藏
    private int defaultItemCount;//一开始展示的条目数
    private String expandText;//待展开显示的文字
    private String hideText;//待隐藏显示的文字
    private boolean useDefaultBottom;//是否使用默认的底部，默认为true使用默认的底部
    private boolean hasBottom;//是否已经有底部，默认为false，没有
    private View bottomView;
    private float fontSize;
    private int textColor;
    private int arrowResId;
    private int mPosition;

    public ExpandableLinearLayout(Context context) {
        this(context, null);
    }

    public ExpandableLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExpandableLinearLayout);
        defaultItemCount = ta.getInt(R.styleable.ExpandableLinearLayout_defaultItemCount, 2);
        expandText = ta.getString(R.styleable.ExpandableLinearLayout_expandText);
        hideText = ta.getString(R.styleable.ExpandableLinearLayout_hideText);
        fontSize = ta.getDimension(R.styleable.ExpandableLinearLayout_tipTextSize, 30);
        textColor = ta.getColor(R.styleable.ExpandableLinearLayout_tipTextColor, Color.parseColor("#ff4c55"));
        useDefaultBottom = ta.getBoolean(R.styleable.ExpandableLinearLayout_useDefaultBottom, true);
        ta.recycle();

        setOrientation(VERTICAL);
    }

    /**
     * 渲染完成时初始化默认底部view
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViews();
    }

    /**
     * 初始化底部view
     */
    private void findViews() {
        bottomView = View.inflate(getContext(), R.layout.item_ell_bottom, null);
        tvTip = (TextView) bottomView.findViewById(R.id.tv_tip);
        img_tips =  bottomView.findViewById(R.id.img_tips);
        tvTip.getPaint().setTextSize(fontSize);
        tvTip.setTextColor(textColor);
        bottomView.setOnClickListener(this);
    }


    public void addItem(View view,int position) {
        if (!useDefaultBottom){
            //如果不使用默认底部
            if (position!=-1){
                addView(view,position);
            }else{
                addView(view);
            }
            int childCount = getChildCount();
            if (childCount > defaultItemCount){
                hide();
            }
            return;
        }

        //使用默认底部
        if (!hasBottom) {
            //如果还没有底部
            addView(view);
        } else {
            int childCount = getChildCount();
            addView(view, childCount - 1);//插在底部之前
        }
        refreshUI(view);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        }
        super.setOrientation(orientation);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        justToAddBottom(childCount);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 判断是否要添加底部
     * @param childCount
     */
    private void justToAddBottom(int childCount) {
        if (childCount > defaultItemCount) {
            if (useDefaultBottom && !hasBottom) {
                //要使用默认底部,并且还没有底部
                addView(bottomView);//添加底部
                hide();
                hasBottom = true;
            }
        }
    }

    /**
     * 刷新UI
     *
     * @param view
     */
    private void refreshUI(View view) {
        int childCount = getChildCount();
        if (childCount > defaultItemCount) {
            if (childCount - defaultItemCount == 1) {
                //刚超过默认，判断是否要添加底部
                justToAddBottom(childCount);
            }
            view.setVisibility(GONE);//大于默认数目的先隐藏
        }
    }

    /**
     * 展开
     */
    private void expand() {
        for (int i = defaultItemCount; i < getChildCount(); i++) {
            //从默认显示条目位置以下的都显示出来
            View view = getChildAt(i);
            view.setVisibility(VISIBLE);
            img_tips.setImageResource(R.drawable.show_more_close);


        }
    }

    /**
     * 收起
     */
    private void hide() {
        int endIndex = useDefaultBottom ? getChildCount() - 1 : getChildCount();//如果是使用默认底部，则结束的下标是到底部之前，否则则全部子条目都隐藏
        for (int i = defaultItemCount; i < endIndex; i++) {
            //从默认显示条目位置以下的都隐藏
            View view = getChildAt(i);
            view.setVisibility(GONE);
            img_tips.setImageResource(R.drawable.show_more_open);

        }
    }


    @Override
    public void onClick(View v) {
        toggle();
    }

    public void toggle() {
        if (isExpand) {
            hide();
            tvTip.setText(expandText);
        } else {
            expand();
            tvTip.setText(hideText);
        }
        isExpand = !isExpand;

        //回调
        if (mStateListener != null){
            mStateListener.onStateChanged(isExpand);
        }
    }


    private OnStateChangeListener mStateListener;

    /**
     * 定义状态改变接口
     */
    public interface OnStateChangeListener {
        void onStateChanged(boolean isExpanded);
    }

    public void setOnStateChangeListener(OnStateChangeListener mListener) {
        this.mStateListener = mListener;
    }


    public void setOnItemClickListener(final OnItemClickListener listener){
        int endIndex = useDefaultBottom ? getChildCount() - 1 : getChildCount();//如果是使用默认底部，则结束的下标是到底部之前
        for (int i = 0; i< endIndex;i++){
            View view = getChildAt(i);
            final int position = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,position);
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}