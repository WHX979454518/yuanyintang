package com.mxkj.yuanyintang.base.activity

import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.mxkj.yuanyintang.widget.bannerLayout.BaseRecyclingPagerAdapter
import com.mxkj.yuanyintang.widget.largeImage.LargeImageView
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.mxkj.yuanyintang.net.NetWork
import com.mxkj.yuanyintang.utils.file.CacheUtils
import com.tbruyelle.rxpermissions2.Permission
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Consumer
import okhttp3.Headers


class StartActivity : BaseActivity() {
    private val imageInteger = intArrayOf(R.drawable.bg_start_one, R.drawable.bg_start_two, R.drawable.bg_start_three, R.drawable.bg_start_four)
    private lateinit var view_start: View
    private lateinit var tv_jump_over: TextView
    private lateinit var indicatorViews: Array<ImageView?>
    private lateinit var layout_bottom: LinearLayout
    private var position = -1
    private val adapter = object : BaseRecyclingPagerAdapter() {
        override fun getView(position: Int, convertView: View?, container: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_start, container, false)
            }
            val largeImageView = convertView as LargeImageView?
            largeImageView!!.setImage(imageInteger[position])
            return largeImageView
        }

        override fun getCount(): Int {
            return imageInteger.size
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        StatusBarUtil.baseTransparentStatusBar(this)
        setContentView(R.layout.activity_start)
        val viewPager = findViewById(R.id.mImageViewPager) as ViewPager
        view_start = findViewById(R.id.view_start)
        view_start!!.visibility = View.GONE
        tv_jump_over = findViewById(R.id.tv_jump_over) as TextView
        layout_bottom = findViewById(R.id.layout_bottom) as LinearLayout
        viewPager.adapter = adapter
        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(p: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(p: Int) {
                position = p
                view_start!!.visibility = if (position + 1 == imageInteger.size) View.VISIBLE else View.GONE
                layout_bottom!!.visibility = if (position + 1 == imageInteger.size) View.INVISIBLE else View.VISIBLE
                setImageBackground(p)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        createIndicatorLayout()
        RxView.clicks(view_start!!)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    goActivity(HomeActivity::class.java)
                    finish()
                }
        RxView.clicks(tv_jump_over!!)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    goActivity(HomeActivity::class.java)
                    finish()
                }
    }

    private fun dip2px(dip: Int): Int {
        val scale = resources.displayMetrics.density
        return (dip * scale + 0.5f).toInt()
    }

    private fun createIndicatorLayout() {
        addIndicator()
    }

    private fun addIndicator() {
        if (adapter != null) {
            val count = adapter.count
            indicatorViews = arrayOfNulls(4)
            for (i in 0 until count) {
                val view = ImageView(this)
                indicatorViews[i] = view
                val params = LinearLayout.LayoutParams(dip2px(12), dip2px(12))
                params.rightMargin = dip2px(8)
                view.layoutParams = params
                view.scaleType = ImageView.ScaleType.CENTER_CROP
                view.setBackgroundResource(R.drawable.page_start_indicator)
                if (i == 0) {
                    view.isSelected = true
                }
                layout_bottom!!.addView(view)
            }
        }
    }

    private fun setImageBackground(selectItems: Int) {
        for (i in indicatorViews.indices) {
            indicatorViews!![i]!!.isSelected = i == selectItems!!
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        layout_bottom!!.removeAllViews()
    }
}
