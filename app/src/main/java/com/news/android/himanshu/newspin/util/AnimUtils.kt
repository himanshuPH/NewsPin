package com.news.android.himanshu.newspin.util

import android.content.Context
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator

object AnimUtils {

    var fastOutSlowIn: Interpolator? = null

    fun getFastOutSlowInInterpolator(context: Context): Interpolator {
        return fastOutSlowIn?:AnimationUtils.loadInterpolator(context,
                android.R.interpolator.fast_out_slow_in).apply { fastOutSlowIn=this }
    }
}