package com.mxkj.yuanyintang.mainui.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;

import java.util.ArrayList;
import java.util.List;

public class MultiLinePartnerGroup extends ViewGroup implements OnClickListener {
    public final int LEFT = 1;
    public final int CENTER = 0;
    public final int RIGHT = 2;
    private int mX, mY;
    private List<PartnerView> viewList;
    private int childMarginHorizontal = 0;
    private int childMarginVertical = 0;
    private int childResId = 0;
    private int childCount = 0;
    private int mLastCheckedPosition = -1;
    private int rowNumber = 0;
    private int gravity = LEFT;
    private OnClickedListener listener;
    private List<MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean> childValues;
    private boolean forceLayout;

    public MultiLinePartnerGroup(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewList = new ArrayList<>();
        childValues = new ArrayList<>();
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.MultiLineRadioGroup);
        childMarginHorizontal = arr.getDimensionPixelSize(R.styleable.MultiLineRadioGroup_child_margin_horizontal, 15);
        childMarginVertical = arr.getDimensionPixelSize(R.styleable.MultiLineRadioGroup_child_margin_vertical, 5);
        childResId = arr.getResourceId(R.styleable.MultiLineRadioGroup_child_layout, 0);
        gravity = arr.getInt(R.styleable.MultiLineRadioGroup_gravity, LEFT);
        if (childResId == 0) {
            throw new RuntimeException(
                    "The attr 'child_layout' must be specified!");
        }
        arr.recycle();
    }

    public MultiLinePartnerGroup(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MultiLinePartnerGroup(@NonNull Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childCount = getChildCount();
        int flagX = 0, flagY = 0, sheight = 0;
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                measureChild(v, widthMeasureSpec, heightMeasureSpec);
                int w = v.getMeasuredWidth() + childMarginHorizontal * 2
                        + flagX + getPaddingLeft() + getPaddingRight();
                if (w > getMeasuredWidth()) {
                    flagY++;
                    flagX = 0;
                }
                sheight = v.getMeasuredHeight();
                flagX += v.getMeasuredWidth() + childMarginHorizontal * 2;
            }
            rowNumber = flagY;
        }
        int height = (flagY + 1) * (sheight + childMarginVertical)
                + childMarginVertical + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(getMeasuredWidth(), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed && !forceLayout) {
            Log.d("tag", "onLayout:unChanged");
            return;
        }
        childCount = getChildCount();
        int[] sX = new int[rowNumber + 1];
        if (childCount > 0) {
            if (gravity != LEFT) {
                for (int i = 0; i < childCount; i++) {
                    View v = getChildAt(i);
                    int w = v.getMeasuredWidth() + childMarginHorizontal * 2
                            + mX + getPaddingLeft() + getPaddingRight();
                    if (w > getWidth()) {
                        if (gravity == CENTER) {
                            sX[mY] = (getWidth() - mX) / 2;
                        } else { // right
                            sX[mY] = (getWidth() - mX);
                        }
                        mY++;
                        mX = 0;
                    }
                    mX += v.getMeasuredWidth() + childMarginHorizontal * 2;
                    if (i == childCount - 1) {
                        if (gravity == CENTER) {
                            sX[mY] = (getWidth() - mX) / 2;
                        } else { // right
                            sX[mY] = (getWidth() - mX);
                        }
                    }
                }
                mX = mY = 0;
            }
            for (int i = 0; i < childCount; i++) {
                View v = getChildAt(i);
                int w = v.getMeasuredWidth() + childMarginHorizontal * 2 + mX
                        + getPaddingLeft() + getPaddingRight();
                if (w > getWidth()) {
                    mY++;
                    mX = 0;
                }
                int startX = mX + childMarginHorizontal + getPaddingLeft()
                        + sX[mY];
                int startY = mY * v.getMeasuredHeight() + (mY + 1)
                        * childMarginVertical;
                v.layout(startX, startY, startX + v.getMeasuredWidth(), startY
                        + v.getMeasuredHeight());
                mX += v.getMeasuredWidth() + childMarginHorizontal * 2;
            }
        }
        mX = mY = 0;
        forceLayout = false;
    }

    @Override
    public void onClick(@NonNull View v) {
        try {
            if (listener != null) {
                int i = (Integer) v.getTag();
                listener.onItemClicked(this,i);
            }
        } catch (Exception e) {
        }
    }

    public void setOnCheckChangedListener(OnClickedListener l) {
        this.listener = l;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        forceLayout = true;
        requestLayout();
    }

    public boolean remove(int position) {
        if (position >= 0 && position < viewList.size()) {
            PartnerView partnerView = viewList.remove(position);
            removeView(partnerView);
            childValues.remove(partnerView);
            childCount--;
            forceLayout = true;
            if (position <= mLastCheckedPosition) { // before LastCheck
                if (mLastCheckedPosition == position) {
                    mLastCheckedPosition = -1;
                } else {
                    mLastCheckedPosition--;
                }
            }
            for (int i = position; i < viewList.size(); i++) {
                viewList.get(i).setTag(i);
            }
            postInvalidate();
            return true;
        }
        return false;
    }

    public boolean insert(int position, MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean) {
        if (position < 0 || position > viewList.size()) {
            return false;
        }
        PartnerView partnerView = getChild();
        partnerView.setData(memberBean, position);
        partnerView.setOnClickListener(this);
        viewList.add(position, partnerView);
        addView(partnerView, position);
        childValues.add(position, memberBean);
        childCount++;
        forceLayout = true;
        if (position <= mLastCheckedPosition) { // before LastCheck
            mLastCheckedPosition++;
        }
        for (int i = position; i < viewList.size(); i++) {
            viewList.get(i).setTag(i);
        }
        postInvalidate();
        return true;
    }

    @NonNull
    private PartnerView getChild() {
        View v = LayoutInflater.from(getContext()).inflate(childResId, this,
                false);
        if (!(v instanceof PartnerView)) {
            throw new RuntimeException(
                    "The attr child_layout's root must be a CheckBox!");
        }
        return (PartnerView) v;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (null != viewList && viewList.size() > 0) {
            for (View v : viewList) {
                v.setEnabled(enabled);
            }
        }
    }

    public interface OnClickedListener {
        void onItemClicked(MultiLinePartnerGroup group, int position);
    }
}