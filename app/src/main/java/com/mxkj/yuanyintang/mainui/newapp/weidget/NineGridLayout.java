package com.mxkj.yuanyintang.mainui.newapp.weidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.utils.app.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public abstract class NineGridLayout extends ViewGroup {
    private static final float DEFUALT_SPACING = 3f;
    private static final int MAX_COUNT = 3;
    protected Context mContext;
    private float mSpacing = DEFUALT_SPACING;
    private int mColumns;
    private int mRows;
    private int mTotalWidth;
    private int mSingleWidth;

    private boolean mIsShowAll = false;
    private boolean mIsFirst = true;
    private List<String> mUrlList = new ArrayList<>();

    public NineGridLayout(Context context) {
        super(context);
        init(context);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridLayout);

        mSpacing = typedArray.getDimension(R.styleable.NineGridLayout_sapcing, DEFUALT_SPACING);
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        if (getListSize(mUrlList) == 0) {
            setVisibility(GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mTotalWidth = right - left;
        mSingleWidth = (int) ((mTotalWidth - mSpacing * (3 - 1)) / 3);
        if (mIsFirst) {
            notifyDataSetChanged();
            mIsFirst = false;
        }
    }

    /**
     * 设置间隔
     *
     * @param spacing
     */
    public void setSpacing(float spacing) {
        mSpacing = spacing;
    }

    /**
     * 设置是否显示所有图片（超过最大数时）
     *
     * @param isShowAll
     */
    public void setIsShowAll(boolean isShowAll) {
        mIsShowAll = isShowAll;
    }

    public void setUrlList(List<String> urlList) {
        if (getListSize(urlList) == 0) {
            setVisibility(GONE);
            return;
        }
        setVisibility(VISIBLE);

        mUrlList.clear();
        mUrlList.addAll(urlList);

        if (!mIsFirst) {
            notifyDataSetChanged();
        }
    }

    public void notifyDataSetChanged() {
        post(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        });
    }

    private void refresh() {
        removeAllViews();
        int size = getListSize(mUrlList);
        if (size > 0) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
        generateChildrenLayout(size);
        layoutParams();

        for (int i = 0; i < size; i++) {
            String url = mUrlList.get(i);
            RoundCornorImageView imageView;
            if (!mIsShowAll) {
                if (i < MAX_COUNT - 1) {
                    imageView = createImageView(i, url);
                    layoutImageView(imageView, i, url, false);
                } else { //第3张时
                    if (size <= MAX_COUNT) {//刚好第3张
                        imageView = createImageView(i, url);
                        layoutImageView(imageView, i, url, false);

                    } else {//超过3张
                        imageView = createImageView(i, url);
                        layoutImageView(imageView, i, url, true);
                        break;
                    }
                }
            } else {
                imageView = createImageView(i, url);
                layoutImageView(imageView, i, url, false);
            }
        }
    }

    private void layoutParams() {
        //根据子view数量确定高度
//        TODO  产品说一行最多显示3个图，所以也用不到计算了

        LayoutParams params = getLayoutParams();
        int listSize = getListSize(mUrlList);
        int singleHeight = 0;
        if (listSize == 1) {
            singleHeight = CommonUtils.INSTANCE.dip2px(getContext(), 112);//原来是224
            params.height = (int) (singleHeight + mSpacing);
        } else if (listSize == 2) {
            singleHeight = CommonUtils.INSTANCE.dip2px(getContext(), 168);//原来是168
            params.height = (int) (singleHeight + mSpacing);
        }
//        else if (listSize == 4) {
//            singleHeight = CommonUtils.INSTANCE.dip2px(getContext(), 168);
//            params.height = (int) (singleHeight * 2 + mSpacing);
//        }
        else {
            singleHeight = CommonUtils.INSTANCE.dip2px(getContext(), 112);//原来是112
//            params.height = (int) (singleHeight * mRows + mSpacing * (mRows - 1));
            params.height = singleHeight;
        }
        setLayoutParams(params);
    }

    private RoundCornorImageView createImageView(final int i, final String url) {
        RoundCornorImageView imageView = new RoundCornorImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickImage(i, url, mUrlList);
            }
        });
        return imageView;
    }

    /**
     * @param imageView
     * @param url
     * @param showNumFlag 是否在最大值的图片上显示还有未显示的图片张数
     */
    private void layoutImageView(RoundCornorImageView imageView, int i, String url, boolean showNumFlag) {
        int listSize = getListSize(mUrlList);
        int singleWidth = 0;
        int singleHeight = 0;
        if (listSize == 1) {
//            singleWidth = CommonUtils.INSTANCE.dip2px(getContext(), 283);//原来是283
            singleWidth = (int) ((mTotalWidth - mSpacing * (3 - 1)) / 3);
            singleHeight = CommonUtils.INSTANCE.dip2px(getContext(), 112);//原来是224
        } else if (listSize == 2) {
            singleWidth = (int) ((mTotalWidth - mSpacing * (2 - 1)) / 2);
            singleHeight = CommonUtils.INSTANCE.dip2px(getContext(), 168);//原来是168
        } else {
            singleWidth = (int) ((mTotalWidth - mSpacing * (3 - 1)) / 3);
            singleHeight = CommonUtils.INSTANCE.dip2px(getContext(), 112);//原来是112
        }
        int[] position = findPosition(i);
        int left = (int) ((singleWidth + mSpacing) * position[1]);
        int top = (int) ((singleHeight + mSpacing) * position[0]);
        int right = left + singleWidth;
        int bottom = top + singleHeight;

        imageView.layout(left, top, right, bottom);

        addView(imageView);
        if (showNumFlag) {
            int overCount = getListSize(mUrlList) - MAX_COUNT;
            if (overCount > 0) {
                float textSize = 30;
                View view = LayoutInflater.from(getContext()).inflate(R.layout.tv_more, null);
                TextView textView = view.findViewById(R.id.textView);
                textView.setText("+" + String.valueOf(overCount));
                textView.setTextColor(Color.WHITE);
                textView.setPadding(0, singleHeight / 2 - getFontHeight(textSize), 0, 0);
                textView.setTextSize(textSize);
//                Drawable drawable = getResources().getDrawable(R.drawable.icon_picture_more);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                textView.setCompoundDrawables(drawable, null, null, null);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundColor(Color.BLACK);
                textView.getBackground().setAlpha(50);

                textView.layout(left, top, right, bottom);
                addView(textView);
            }
        }
        displayImage(imageView, url);
    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mColumns; j++) {
                if ((i * mColumns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    /**
     * 根据图片个数确定行列数量
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= 2) {
            mRows = 1;
            mColumns = length;
        }
        if (length == 3) {
            mRows = 1;
            mColumns = length;
        } else if (length <= 6) {
            mRows = 2;
            mColumns = 3;
            if (length == 4) {
                mColumns = 3;
            }
        } else {
            mColumns = 3;
            if (mIsShowAll) {
                mRows = length / 3;
                int b = length % 3;
                if (b > 0) {
                    mRows++;
                }
            } else {
                mRows = 3;
            }
        }
        if (mColumns > 3) {
            mColumns = 3;
        }

    }

    protected void setOneImageLayoutParams(RoundCornorImageView imageView, int width, int height) {
        imageView.setLayoutParams(new LayoutParams(width, height));
        imageView.layout(0, 0, width, height);

        LayoutParams params = getLayoutParams();
//        params.width = width;
        params.height = height;
        setLayoutParams(params);
    }

    private int getListSize(List<String> list) {
        if (list == null || list.size() == 0) {
            return 0;
        }
        return list.size();
    }

    private int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    /**
     * @param imageView
     * @param url
     * @param parentWidth 父控件宽度
     * @return true 代表按照九宫格默认大小显示，false 代表按照自定义宽高显示
     */
    protected abstract boolean displayOneImage(RoundCornorImageView imageView, String url, int parentWidth);

    protected abstract void displayImage(RoundCornorImageView imageView, String url);

    protected abstract void onClickImage(int position, String url, List<String> urlList);
}