package com.mxkj.yuanyintang.widget.bannerLayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mxkj.yuanyintang.R;
import com.mxkj.yuanyintang.widget.bannerLayout.autoscoll.AutoScrollViewPager;


/**
 * banner
 * Created by liaoxiang on 16/3/17.
 */
public class HomeTopBannerLayout extends FrameLayout implements ViewPager.OnPageChangeListener {
    public ClickViewPager autoScrollViewPager;
    private LinearLayout indicator;
    private ImageView[] indicatorViews;
    private String indicatorBgColor = "#00000000";
    private int indicatorHeight = 16;//默认高度，单位dip
    private float aspect = -1f;
    private int interval = 5 * 1000;//5秒
    private boolean showIndicator = true;
    private PagerAdapter mPagerAdapter;
    private DataSetObserver mDataSetObserver;

    public HomeTopBannerLayout(Context context) {
        super(context);
        init(context, null);
    }

    public HomeTopBannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public HomeTopBannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HomeTopBannerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {
        //如果pageAdapter调用notifyDataSetChanged();则重新绘制圆点
        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                showIndicator(showIndicator);
            }
        };
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BannerLayout);
            aspect = a.getFloat(R.styleable.BannerLayout_aspect, aspect);
            a.recycle();
        }
        autoScrollViewPager = new ClickViewPager(getContext());
        autoScrollViewPager.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        autoScrollViewPager.setInterval(interval);
        autoScrollViewPager.setBorderAnimation(false);
        autoScrollViewPager.setStopScrollWhenTouch(true);
        autoScrollViewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
        autoScrollViewPager.setOffscreenPageLimit(6);
        //noinspection deprecation
        autoScrollViewPager.setOnPageChangeListener(this);
        autoScrollViewPager.setBackgroundColor(Color.GRAY);
        autoScrollViewPager.setOnItemClickListener(new ClickViewPager.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (null != bannerItemListener) {
                    bannerItemListener.onItemClick(position);
                }
            }
        });
        addView(autoScrollViewPager);
        //触摸\滑动不轮播
        autoScrollViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下不轮播
                        stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //移动不轮播
                        stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_UP:
                        //抬起轮播
                        startAutoScroll();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        //取消操作,轮播
                        stopAutoScroll();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (aspect > 0) {
            setMeasuredDimension(sizeWidth, (int) (sizeWidth / aspect));
            measureChildren(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setMode(int mode) {
        autoScrollViewPager.setSlideBorderMode(mode);
    }

    public void setCurrentItem(int position) {
        autoScrollViewPager.setCurrentItem(position, false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        System.err.println("--onLayout--");
        if (aspect > 0) {
            if (autoScrollViewPager.getLayoutParams() != null && autoScrollViewPager.getLayoutParams().height <= 0) {
                int width = getMeasuredWidth();
                //重设高度
                autoScrollViewPager.getLayoutParams().height = (int) (width / aspect);
            }
        }
    }

    /**
     * 设置数据adapter
     *
     * @param pagerAdapter pagerAdapter
     */
    public void setAdapter(PagerAdapter pagerAdapter) {
        if (mPagerAdapter != null) {
            mPagerAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mPagerAdapter = pagerAdapter;
        if (pagerAdapter != null) {
            mPagerAdapter.registerDataSetObserver(mDataSetObserver);
            autoScrollViewPager.setAdapter(mPagerAdapter);
            showIndicator(showIndicator);
        }
    }

    /**
     * 开始自动滚动
     */
    public void startAutoScroll() {
        autoScrollViewPager.startAutoScroll();
    }

    /**
     * 停止自动滚动
     */
    public void stopAutoScroll() {
        autoScrollViewPager.stopAutoScroll();
    }

    public void showIndicator(boolean show) {
        showIndicator = show;
        if (show) {
            if (indicator != null) {
                removeView(indicator);
                indicator = null;
            }
            createIndicatorLayout();
        } else {
            if (indicator != null) {
                removeView(indicator);
                indicator = null;
            }
        }
    }

    private void createIndicatorLayout() {
        indicator = new LinearLayout(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, dip2px(indicatorHeight));
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.bottomMargin = 20;
        params.rightMargin = 60;
        indicator.setPadding(10,10,10,10);
        indicator.setLayoutParams(params);
        indicator.setGravity(Gravity.CENTER);
        indicator.setOrientation(LinearLayout.HORIZONTAL);
//        indicator.setBackgroundColor(Color.parseColor(indicatorBgColor));
        indicator.setBackgroundResource(R.drawable.home_banner_indi_bck);
        addIndicator();
        //添加小圆点
        addView(indicator);
    }

    private void addIndicator() {
        PagerAdapter pagerAdapter = autoScrollViewPager.getAdapter();
        if (pagerAdapter != null) {
            //TODO 除数修改的话需要修改ClickViewPager的数值
            int count = pagerAdapter.getCount() / 30;
//            System.err.println("---->"+count);
            indicatorViews = new ImageView[count];
            for (int i = 0; i < count; i++) {
                ImageView view = new ImageView(getContext());
                indicatorViews[i] = view;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(5), dip2px(5));
                params.rightMargin = dip2px(5);
                params.gravity = Gravity.CENTER;
                view.setLayoutParams(params);
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                view.setBackgroundResource(R.drawable.page_indicator);
                if (i == 0) {
                    view.setSelected(true);
                }
                indicator.addView(view);
            }
        }
    }

    private int dip2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems 选中项
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < indicatorViews.length; i++) {
            if (i == selectItems) {
                indicatorViews[i].setSelected(true);
            } else {
                indicatorViews[i].setSelected(false);
            }
        }
    }

    private int position = -1;
    private static final String TAG = "BannerLayout";

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (null != mPagerAdapter && mPagerAdapter.getCount() > 0) {
            if (mPagerAdapter instanceof RecyclingUnlimitedPagerAdapter) {
                position %= mPagerAdapter.getCount() / 30;
            }
            if (indicator != null) {
                setImageBackground(position);
            }
            if (null != bannerItemListener) {
                bannerItemListener.onPageSelected(position);
            }
            this.position = position;
        }
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface BannerItemListener {
        void onItemClick(int position);

        void onPageSelected(int position);
    }

    public BannerItemListener bannerItemListener;

    public void setBannerItemListener(BannerItemListener bannerItemListener) {
        this.bannerItemListener = bannerItemListener;
    }

    public ClickViewPager getAutoScrollViewPager() {
        return autoScrollViewPager;
    }


}
