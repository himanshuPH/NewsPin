package com.news.android.himanshu.newspin.news

import com.news.android.himanshu.newspin.data.News
import com.news.android.himanshu.newspin.data.source.NewsRepository
import com.news.android.himanshu.newspin.data.source.Result
import com.news.android.himanshu.newspin.news.ArticleContract
import com.news.android.himanshu.newspin.util.launchSilent
import kotlinx.coroutines.experimental.android.UI
import kotlin.coroutines.experimental.CoroutineContext


class ArticlePresenter(private val newsRepository: NewsRepository,
                       private val uiContext: CoroutineContext = UI): ArticleContract.Presenter {

    var mView: ArticleContract.View? = null

    override fun attachView(view: ArticleContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun getArticlesByCategory(articleCategory: String) = launchSilent(uiContext) {
        mView?.showLoader()
        val articlesResult = newsRepository.getNewsByCategory(articleCategory)
        when(articlesResult){
            is Result.Success -> mView?.showArticles(articlesResult.data)
            is Result.Error -> mView?.showError()
        }
    }

    override fun updateBookmarkNews(news: News, position: Int) = launchSilent(uiContext) {
        newsRepository.updateBookmarkStatus(news)
        mView?.changeBookmarkStatus(position)
    }
}