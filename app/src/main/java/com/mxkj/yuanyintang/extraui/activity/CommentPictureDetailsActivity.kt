package com.mxkj.yuanyintang.extraui.activity

import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView
import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.base.activity.StandardUiActivity
import com.mxkj.yuanyintang.utils.image.imageloader.config.ScaleMode
import com.mxkj.yuanyintang.utils.image.imageloader.loader.ImageLoader
import com.mxkj.yuanyintang.utils.photopicker.photoview.PhotoView
import com.mxkj.yuanyintang.extraui.bean.CommentPictureDataBean
import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.mxkj.yuanyintang.utils.string.StringUtils
import java.util.concurrent.TimeUnit
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_comment_picture_details.*
class CommentPictureDetailsActivity : StandardUiActivity() {
    private lateinit var commentPictureDetailsAdapter: CommentPictureDetailsAdapter
    private var pictureDataBean: CommentPictureDataBean? = null
    override val isVisibilityBottomPlayer: Boolean
        get() = false
    public override fun setLayoutId(): Int {
        return R.layout.activity_comment_picture_details
    }
    override fun initView() {
        hideTitle(true)
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.black))
        ButterKnife.bind(this)
        pictureDataBean = intent.getSerializableExtra(COMMENT_PICTURE_DATA) as CommentPictureDataBean
        navigationBar.init()
        navigationBar.setTitle("0/0")
        navigationBar.setTitleColor(ContextCompat.getColor(this, R.color.white_text))
        navigationBar.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
        navigationBar.setLeftButtonDrawable(ContextCompat.getDrawable(this, R.drawable.close_back_white))
        navigationBar.setRightButtonImageView(ContextCompat.getDrawable(this, R.drawable.icon_more_white))
        RxView.clicks(navigationBar.getLeftButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { finish() }
        RxView.clicks(navigationBar.getRightButton())
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe { finish() }
        initViewPager()

    }

    private fun initViewPager() {
        if (null == pictureDataBean) {
            return
        }
        commentPictureDetailsAdapter = CommentPictureDetailsAdapter()
        viewPager.adapter = commentPictureDetailsAdapter
        viewPager.currentItem = pictureDataBean!!.position
        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                navigationBar.setTitle((position + 1).toString() + "/" + pictureDataBean!!.dataBeanList!!.size)
                tv_name.setText(StringUtils.isEmpty(pictureDataBean!!.dataBeanList!![position].nickname))
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    override fun initData() {

    }

    override fun initEvent() {

    }

    inner class CommentPictureDetailsAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return pictureDataBean!!.dataBeanList!!.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val photoView = View.inflate(this@CommentPictureDetailsActivity, R.layout.item_photoview, null) as PhotoView
            ImageLoader.with(this@CommentPictureDetailsActivity)
                    .url(pictureDataBean!!.dataBeanList!![position].img)
                    .scale(ScaleMode.FIT_CENTER)
                    .into(photoView)
            container.addView(photoView)
            return photoView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(container.getChildAt(position))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        pictureDataBean!!.dataBeanList!!.clear()
        commentPictureDetailsAdapter.notifyDataSetChanged()
    }

    companion object {
        const val COMMENT_PICTURE_DATA = "_comment_picture_data"
    }

}
