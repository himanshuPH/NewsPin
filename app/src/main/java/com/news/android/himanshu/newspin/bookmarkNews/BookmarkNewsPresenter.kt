package com.news.android.himanshu.newspin.bookmarkNews

import com.news.android.himanshu.newspin.data.News
import com.news.android.himanshu.newspin.data.source.NewsRepository
import com.news.android.himanshu.newspin.data.source.Result
import com.news.android.himanshu.newspin.util.launchSilent
import kotlinx.coroutines.experimental.android.UI
import kotlin.coroutines.experimental.CoroutineContext


class BookmarkNewsPresenter(private val newsRepository: NewsRepository,
                            private val uiContext: CoroutineContext = UI):BookmarkNewsContract.Presenter {

    var mView: BookmarkNewsContract.View? = null
    override fun attachView(view: BookmarkNewsContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun getBookmarkedArticles() = launchSilent(uiContext) {
        mView?.showLoader()
        val bookmarkedNewsResult = newsRepository.getBookmarkedNews()
        when(bookmarkedNewsResult){
            is Result.Success -> mView?.showArticles(bookmarkedNewsResult.data)
            is Result.Error -> mView?.showError()
        }
    }

    override fun updateBookmarkStatus(news: News,position:Int) = launchSilent(uiContext) {
        newsRepository.updateBookmarkStatus(news)
        mView?.removeNewsItem(news,position)
    }
}