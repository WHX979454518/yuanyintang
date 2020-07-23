package com.mxkj.yuanyintang.base.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout

import com.mxkj.yuanyintang.R
import com.mxkj.yuanyintang.musicplayer.activity.PlayerActivity
import com.mxkj.yuanyintang.utils.rxbus.RxBus
import com.mxkj.yuanyintang.utils.rxbus.RxBusSubscriber
import com.mxkj.yuanyintang.utils.rxbus.event.FloatLogoEvent
import com.mxkj.yuanyintang.widget.floatmenu.FloatLogoMenu

import io.reactivex.disposables.Disposable

abstract class StandardUiActivity : TitleBarActivity() {
    abstract val isVisibilityBottomPlayer: Boolean
    //    TODO 这个是一个悬浮按钮，全局显示播放器的圆形专辑图，点击直接打开播放器 这个版本产品去掉了,所以注释了
//    internal var mFloatMenu: FloatLogoMenu? = null
    private var mainView: FrameLayout? = null
//    private var mFloatLogoEvent: Disposable? = null

    protected abstract fun setLayoutId(): Int

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun initEvent()
    override fun setContentLayout(): View {
        val view = View.inflate(this, R.layout.activity_standard_ui, null)
        mainView = view.findViewById(R.id.mainView)
        val layoutView = View.inflate(this, setLayoutId(), null)
        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        mainView!!.addView(layoutView, layoutParams)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
//        mFloatLogoEvent = RxBus.getDefault().toObservable(FloatLogoEvent::class.java)
//                .subscribeWith(object : RxBusSubscriber<FloatLogoEvent>() {
//                    @Throws(Exception::class)
//                    override fun onEvent(floatLogoEvent: FloatLogoEvent) {
//                        if (null != mFloatMenu) {
////                            mFloatMenu!!.setFloatLogo(floatLogoEvent.url)
//                        }
//                    }
//                })
//        RxBus.getDefault().add(mFloatLogoEvent)
        initView()
        initEvent()
        initData()
    }

    override fun onResume() {
        super.onResume()
        if (!isVisibilityBottomPlayer) {
//            if (mFloatMenu == null) {
//                mFloatMenu = FloatLogoMenu.Builder()
//                        .withActivity(this)
//                        .withContext(this)
//                        .logo(BitmapFactory.decodeResource(resources, R.drawable.tab_music_player))
//                        .drawCicleMenuBg(true)
//                        .defaultLocation(FloatLogoMenu.RIGHT)
//                        .show()
//                mFloatMenu!!.setOnFloatMenuOpenListener { goActivity(PlayerActivity::class.java) }
//            }
        }
    }

    override fun onDestroy() {
        destroyFloat()
        super.onDestroy()
//        RxBus.getDefault().remove(mFloatLogoEvent)
    }

    fun destroyFloat() {
//        if (mFloatMenu != null) {
//            mFloatMenu!!.destoryFloat()
//        }
//        mFloatMenu = null
    }

}
