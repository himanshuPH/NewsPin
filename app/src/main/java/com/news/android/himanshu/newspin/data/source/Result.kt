@file:Suppress("unused")

package com.news.android.himanshu.newspin.data.source

sealed class Result<out T : Any> {

    class Success<out T : Any>(val data: T) : Result<T>()

    class Error(val exception: Throwable?) : Result<Nothing>()
}