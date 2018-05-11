package com.news.android.himanshu.newspin.util

import android.content.Context
import android.net.ConnectivityManager
import com.news.android.himanshu.newspin.App


fun isNetConnected() : Boolean{
    val cm = App.getInstance().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}