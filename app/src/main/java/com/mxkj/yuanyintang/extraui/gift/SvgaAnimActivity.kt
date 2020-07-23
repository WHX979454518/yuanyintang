package com.mxkj.yuanyintang.extraui.gift

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextPaint
import android.text.TextUtils
import android.view.Window

import com.mxkj.yuanyintang.utils.uiutils.StatusBarUtil
import com.opensource.svgaplayer.SVGACallback
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity

import java.net.URL

class SvgaAnimActivity : AppCompatActivity() {

    private var aniView: SVGAImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.baseTransparentStatusBar(this)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        aniView = SVGAImageView(this)
        aniView!!.setBackgroundColor(Color.BLACK)
        aniView!!.background.alpha = 100
        loadAnimation()
        setContentView(aniView)
    }

    private fun loadAnimation() {
        val parser = SVGAParser(this)
        try {
            val url = intent.getStringExtra(GIFT_SVG)
            if (TextUtils.isEmpty(url)) {
                return
            }
            parser.parse(URL(url), object : SVGAParser.ParseCompletion {
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    val drawable = SVGADrawable(videoItem)
                    aniView!!.setImageDrawable(drawable)
                    aniView!!.startAnimation()
                }

                override fun onError() {

                }
            })
            aniView!!.callback = object : SVGACallback {
                override fun onPause() {}

                override fun onFinished() {
                    finish()
                }

                override fun onRepeat() {

                }

                override fun onStep(i: Int, v: Double) {

                }
            }
        } catch (e: Exception) {
            print(true)
        }

    }

    /**
     * 进行简单的文本替换
     *
     * @return
     */
    private fun requestDynamicItem(): SVGADynamicEntity {
        val dynamicEntity = SVGADynamicEntity()
        val textPaint = TextPaint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 28f
        dynamicEntity.setDynamicText("gift", textPaint, "banner")
        return dynamicEntity
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(0, 0)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    companion object {
        const val GIFT_SVG = "gift_svg"
    }
}
