package com.news.android.himanshu.newspin.data.source

import com.news.android.himanshu.newspin.data.News

interface NewsDataSource {

    suspend fun getNewsById(id: String): Result<News>

    suspend fun getNewsByCategory(category: String): Result<ArrayList<News>>

    suspend fun getBookmarkedNews(): Result<ArrayList<News>>

    suspend fun saveNews(news: News)

    suspend fun updateBookmarkStatus(news: News)

    suspend fun deleteNews(category: String,isBookmarked: Boolean)

    suspend fun deleteAllNews()

}