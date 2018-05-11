package com.news.android.himanshu.newspin.bookmarkNews

import com.news.android.himanshu.newspin.BasePresenter
import com.news.android.himanshu.newspin.BaseView
import com.news.android.himanshu.newspin.data.News


interface BookmarkNewsContract {

    interface View: BaseView {
        fun showLoader()
        fun showArticles(articleList:ArrayList<News>)
        fun removeNewsItem(news: News,position:Int)
        fun showError()
    }

    interface Presenter: BasePresenter<View> {
        fun getBookmarkedArticles()
        fun updateBookmarkStatus(news: News,position:Int)
    }
}