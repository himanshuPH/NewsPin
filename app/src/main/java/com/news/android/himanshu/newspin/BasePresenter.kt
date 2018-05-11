package com.news.android.himanshu.newspin


interface BasePresenter<in V : BaseView> {
    fun attachView(view: V)

    fun detachView()
}