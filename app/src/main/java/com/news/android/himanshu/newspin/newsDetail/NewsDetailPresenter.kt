package com.news.android.himanshu.newspin.newsDetail

import com.news.android.himanshu.newspin.data.News
import com.news.android.himanshu.newspin.data.source.NewsRepository
import com.news.android.himanshu.newspin.data.source.Result
import com.news.android.himanshu.newspin.util.launchSilent
import kotlinx.coroutines.experimental.android.UI
import kotlin.coroutines.experimental.CoroutineContext

class NewsDetailPresenter(private val newsRepository: NewsRepository,
                          private val uiContext: CoroutineContext = UI): NewsDetailContract.Presenter {

    var mView: NewsDetailContract.View? = null

    override fun attachView(view: NewsDetailContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun getArticleById(newsId: String) = launchSilent(uiContext) {
        val result = newsRepository.getNewsById(newsId)
        if (result is Result.Success) {
            mView?.showArticleDetail(result.data)
        }
    }

    override fun updateBookmarkNews(news: News, position: Int) {
        
    }
}