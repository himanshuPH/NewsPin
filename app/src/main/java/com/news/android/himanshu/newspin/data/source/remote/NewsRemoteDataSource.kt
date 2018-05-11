package com.news.android.himanshu.newspin.data.source.remote

import com.news.android.himanshu.newspin.data.News
import com.news.android.himanshu.newspin.data.source.NewsDataSource
import com.news.android.himanshu.newspin.data.source.RemoteDataNotFoundException
import com.news.android.himanshu.newspin.data.source.Result
import com.news.android.himanshu.newspin.util.getResult

class NewsRemoteDataSource private constructor(val newsService: NewsService) : NewsDataSource {


    suspend override fun getNewsById(id: String): Result<News> {
        //no need
        return Result.Error(RemoteDataNotFoundException())
    }

    suspend override fun getNewsByCategory(category: String): Result<ArrayList<News>> {
        val newsResponseResult = newsService.getNews(category).getResult()
        return when (newsResponseResult) {
            is Result.Success -> {
                if (newsResponseResult.data.articles.isNotEmpty())
                    Result.Success(newsResponseResult.data.articles)
                else
                    Result.Error(RemoteDataNotFoundException())
            }
            is Result.Error -> {
                newsResponseResult
            }

        }
    }

    override suspend fun saveNews(news: News) {
        //No need
    }

    override suspend fun updateBookmarkStatus(news: News) {
        //No need
    }

    override suspend fun deleteNews(category: String,isBookmarked: Boolean) {
        //no need
    }

    override suspend fun deleteAllNews() {
        //no need
    }

    override suspend fun getBookmarkedNews(): Result<ArrayList<News>> {
        //no need
        return Result.Error(RemoteDataNotFoundException())
    }

    companion object {

        private lateinit var INSTANCE: NewsRemoteDataSource
        private var needsNewInstance = true

        @JvmStatic
        fun getInstance(newsService: NewsService): NewsRemoteDataSource {
            if (needsNewInstance) {
                INSTANCE = NewsRemoteDataSource(newsService)
                needsNewInstance = false
            }
            return INSTANCE
        }
    }
}