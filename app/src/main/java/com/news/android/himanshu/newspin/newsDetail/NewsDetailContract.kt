package com.news.android.himanshu.newspin.newsDetail

import com.news.android.himanshu.newspin.BasePresenter
import com.news.android.himanshu.newspin.BaseView
import com.news.android.himanshu.newspin.data.News

interface NewsDetailContract {
    interface View: BaseView {
        fun showArticleDetail(news:News)
    }

    interface Presenter: BasePresenter<View> {
        fun getArticleById(newsId:String)
        fun updateBookmarkNews(news: News, position: Int)
    }
}