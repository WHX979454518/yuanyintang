package com.mxkj.yuanyintang.mainui.myself.my_release.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.mainui.home.bean.MusicIndex;

import java.util.ArrayList;
import java.util.List;

public class AddPartnerViewGroup extends ViewGroup implements OnClickListener {
    public final int LEFT = 1;
    public final int CENTER = 0;
    public final int RIGHT = 2;
    private int mX, mY;
    private List<AddPartnerView> viewList;
    private int childMarginHorizontal = 0;
    private int childMarginVertical = 0;
    private int childResId = 0;
    private int childCount = 0;
    private int rowNumber = 0;
    private int gravity = LEFT;
    private OnClickedListener listener;
    private List<MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean> childValues;
    private boolean forceLayout;//标记布局发生了改变（决定走不走onLayout方法）

    public AddPartnerViewGroup(@NonNull final Context context, AttributeSet attrs, int defStyleAttr) {
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
                    "bug！bug！");
        }

        if (childCount > 0) {
            boolean hasValues = childValues != null;
            for (int i = 0; i < childCount; i++) {
                View child = getChild();
                if (child instanceof AddPartnerView) {
                    AddPartnerView partnerView = (AddPartnerView) child;
                    viewList.add(partnerView);
                    addView(partnerView);
                    if (hasValues && i < childValues.size()) {
                        partnerView.setData(childValues.get(i));
                    }
                    partnerView.setTag(i);
                    partnerView.setOnClickListener(this);
                }
            }
        }
        MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean = new MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean();
        memberBean.setNickname("+添加用户");
        insert(memberBean, null);
        arr.recycle();
    }

    public AddPartnerViewGroup(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddPartnerViewGroup(@NonNull Context context) {
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
        int height = (flagY + 1) * (sheight + childMarginVertical) + childMarginVertical * 2 + getPaddingBottom() + getPaddingTop();
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
                listener.onItemClicked(this, i);
            }
//           TODO
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
            AddPartnerView partnerView = viewList.remove(position);
            removeView(partnerView);
            childValues.remove(partnerView);
            childCount--;
            forceLayout = true;
            for (int i = position; i < viewList.size(); i++) {
                viewList.get(i).setTag(i);
            }
            postInvalidate();
            return true;
        }
        return false;
    }

    public boolean insert(final MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean memberBean, InsertCallback callback) {
        final View child = getChild();
        boolean canAdd = true;
        if (child instanceof AddPartnerView) {
            if (childValues.size() > 20) {
                return true;
            }
            for (int i = 0; i < childValues.size(); i++) {
                if (childValues.get(i).getNickname().equals(memberBean.getNickname())) {
                    canAdd = false;//用户昵称是唯一的，暂时遍历已经添加的用户名，一旦存在就不添加
                    if (callback != null) {
                        callback.onInsert(canAdd);
                    }
                    break;
                }
            }
            if (canAdd) {
                final AddPartnerView partnerView = (AddPartnerView) child;
                partnerView.setData(memberBean);
                viewList.add(partnerView);
                addView(partnerView, getChildCount() > 0 ? getChildCount() - 1 : 0);
                childValues.add(memberBean);
                childCount++;
                forceLayout = true;
                ImageView iv_del_item = partnerView.findViewById(R.id.iv_del_item);
                iv_del_item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        forceLayout = true;
                        removeView(partnerView);
                        viewList.remove(partnerView);
                        childValues.remove(memberBean);
                        childCount--;
                    }
                });
                postInvalidate();
            }

        }
        return true;
    }

    @NonNull
    private View getChild() {
        View v = LayoutInflater.from(getContext()).inflate(childResId, this, false);
        return v;
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
        void onItemClicked(AddPartnerViewGroup group, int position);
    }


    public interface InsertCallback {
        void onInsert(boolean canAdd);
    }

    public List<MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean> getDataList() {
        return childValues == null ? new ArrayList<MusicIndex.ItemInfoListBean.MemberBeanX.MemberBean>() : childValues;
    }
}