package com.news.android.himanshu.newspin.elasticView

import android.app.Activity
import android.graphics.Color
import com.news.android.himanshu.newspin.util.ColorUtils
import com.news.android.himanshu.newspin.util.ViewUtils


open class SystemChromeFader(private val activity: Activity) : ElasticDragDismissCallback {
    private val statusBarAlpha: Int
    private val navBarAlpha: Int
    private val fadeNavBar: Boolean

    init {
        statusBarAlpha = Color.alpha(activity.window.statusBarColor)
        navBarAlpha = Color.alpha(activity.window.navigationBarColor)
        fadeNavBar = ViewUtils.isNavBarOnBottom(activity)
    }

    override fun onDrag(elasticOffset: Float, elasticOffsetPixels: Float,
                        rawOffset: Float, rawOffsetPixels: Float) {
        if (elasticOffsetPixels > 0) {
            // dragging downward, fade the status bar in proportion
            activity.window.statusBarColor = ColorUtils.modifyAlpha(activity.window
                    .statusBarColor, ((1f - rawOffset) * statusBarAlpha).toInt())
        } else if (elasticOffsetPixels == 0f) {
            // reset
            activity.window.statusBarColor = ColorUtils.modifyAlpha(
                    activity.window.statusBarColor, statusBarAlpha)
            activity.window.navigationBarColor = ColorUtils.modifyAlpha(
                    activity.window.navigationBarColor, navBarAlpha)
        } else if (fadeNavBar) {
            // dragging upward, fade the navigation bar in proportion
            activity.window.navigationBarColor = ColorUtils.modifyAlpha(activity.window.navigationBarColor,
                    ((1f - rawOffset) * navBarAlpha).toInt())
        }
    }

    override fun onDragDismissed() {
        activity.finishAfterTransition()
    }
}