package com.news.android.himanshu.newspin.news

import com.news.android.himanshu.newspin.BasePresenter
import com.news.android.himanshu.newspin.BaseView
import com.news.android.himanshu.newspin.data.News

interface ArticleContract {

    interface View: BaseView {
        fun showLoader()
        fun showArticles(articleList:ArrayList<News>)
        fun changeBookmarkStatus(position: Int)
        fun showError()
    }

    interface Presenter: BasePresenter<View> {
        fun getArticlesByCategory(articleCategory:String)
        fun updateBookmarkNews(news: News, position: Int)
    }
}