@file:JvmName("MathUtils")
package com.news.android.himanshu.newspin.util

import java.util.*

fun constrain(min: Float, max: Float, v: Float) = v.coerceIn(min, max)

fun isTimeGt1Hr(timestamp:Long) :Boolean {
    return Math.abs(Date().time - timestamp) >  60 * 60 *1000
}