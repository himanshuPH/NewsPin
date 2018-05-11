package com.news.android.himanshu.newspin.data.source.local

import com.news.android.himanshu.newspin.data.News
import com.news.android.himanshu.newspin.data.source.LocalDataNotFoundException
import com.news.android.himanshu.newspin.data.source.NewsDataSource
import com.news.android.himanshu.newspin.data.source.Result
import com.news.android.himanshu.newspin.util.AppExecutors
import kotlinx.coroutines.experimental.withContext

class NewsLocalDataSource private constructor(val appExecutors: AppExecutors,
                                              val newsDao: NewsDao) : NewsDataSource {


    override suspend fun getNewsById(id: String): Result<News> = withContext(appExecutors.ioContext) {
        val news = newsDao.getNewsById(id)
        if (news != null) Result.Success(news) else Result.Error(LocalDataNotFoundException())
    }

    override suspend fun getNewsByCategory(category: String): Result<ArrayList<News>> = withContext(appExecutors.ioContext) {
        val news = ArrayList(newsDao.getNewsByCategory(category))
        if (news.isNotEmpty())
            Result.Success(news)
        else
            Result.Error(LocalDataNotFoundException())
    }

    override suspend fun saveNews(news: News) = withContext(appExecutors.ioContext) {
        newsDao.insertNews(news)
    }

    override suspend fun updateBookmarkStatus(news: News) = withContext(appExecutors.ioContext) {
        newsDao.updateBookmark(news.id, news.isBookmarked)
    }

    override suspend fun deleteNews(category: String,isBookmarked: Boolean) = withContext(appExecutors.ioContext) {
        newsDao.deleteNews(category,isBookmarked)
    }

    override suspend fun deleteAllNews() = withContext(appExecutors.ioContext){
        newsDao.deleteAllNews()
    }

    override suspend fun getBookmarkedNews(): Result<ArrayList<News>> = withContext(appExecutors.ioContext) {
        val news = ArrayList(newsDao.getBookmarkedNews(true))
        if (news.isNotEmpty())
            Result.Success(news)
        else
            Result.Error(LocalDataNotFoundException())
    }

    companion object {
        private var INSTANCE: NewsLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, newsDao: NewsDao): NewsLocalDataSource {
            if (INSTANCE == null) {
                synchronized(NewsLocalDataSource::javaClass) {
                    INSTANCE = NewsLocalDataSource(appExecutors, newsDao)
                }
            }
            return INSTANCE!!
        }
    }
}