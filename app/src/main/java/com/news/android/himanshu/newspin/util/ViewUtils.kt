package com.news.android.himanshu.newspin.util


import android.content.Context
import android.os.Build
import android.view.View

object ViewUtils {

    fun isNavBarOnBottom(context: Context): Boolean {
        val res = context.resources
        val cfg = res.configuration
        val dm = res.displayMetrics
        val canMove = dm.widthPixels != dm.heightPixels && cfg.smallestScreenWidthDp < 600
        return !canMove || dm.widthPixels < dm.heightPixels
    }

    fun setLightStatusBar(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
        }
    }

}

