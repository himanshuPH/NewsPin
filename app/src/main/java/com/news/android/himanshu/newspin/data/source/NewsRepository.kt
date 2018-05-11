package com.news.android.himanshu.newspin.data.source

import android.preference.PreferenceManager.getDefaultSharedPreferences
import com.news.android.himanshu.newspin.App
import com.news.android.himanshu.newspin.data.News
import com.news.android.himanshu.newspin.util.isNetConnected
import com.news.android.himanshu.newspin.util.isTimeGt1Hr
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap
import kotlin.collections.arrayListOf
import kotlin.collections.find
import kotlin.collections.flatMap
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.set

class NewsRepository(val newsRemoteDataSource: NewsDataSource,
                     val newsLocalDataSource: NewsDataSource) : NewsDataSource {

    var cachedNews: LinkedHashMap<String, ArrayList<News>> = LinkedHashMap()

    var cacheIsDirty = false

    override suspend fun getNewsByCategory(category: String): Result<ArrayList<News>> {
        cacheIsDirty = isTimeGt1Hr(getLatestFetchTimestamp(category))
        val isNetConnected = isNetConnected()

        if (cachedNews.isNotEmpty() && !cacheIsDirty)
            Result.Success(cachedNews[category] ?: arrayListOf())

        return if (cacheIsDirty && isNetConnected)
            getNewsFromRemoteDataSource(category, true)
        else
            getNewsFromLocalDataSource(category, isNetConnected)
    }

    private suspend fun getNewsFromLocalDataSource(category: String, isNetConnected: Boolean): Result<ArrayList<News>> {
        val result = newsLocalDataSource.getNewsByCategory(category)
        return when (result) {
            is Result.Success -> {
                cacheIsDirty = false
                cachedNews.put(category, result.data)
                Result.Success(cachedNews[category] ?: arrayListOf())
            }
            is Result.Error -> {
                if (isNetConnected)
                    getNewsFromRemoteDataSource(category, false)
                else
                    Result.Error(LocalDataNotFoundException())

            }
        }
    }

    private suspend fun getNewsFromRemoteDataSource(category: String, refreshLocalData: Boolean): Result<ArrayList<News>> {
        val result = newsRemoteDataSource.getNewsByCategory(category)
        return when (result) {
            is Result.Success -> {
                setLatestFetchTimestamp(category)
                refreshCacheAndLocalDataSource(result.data, category, refreshLocalData)
                Result.Success(cachedNews[category] ?: arrayListOf())
            }
            is Result.Error -> Result.Error(RemoteDataNotFoundException())
        }

    }

    private suspend fun refreshCacheAndLocalDataSource(newsList: ArrayList<News>, category: String, refreshLocalData: Boolean) {
        cacheIsDirty = false
        if (refreshLocalData) {
            newsLocalDataSource.deleteNews(category, isBookmarked = false) //delete news which are not bookmarked
            val result = newsLocalDataSource.getNewsByCategory(category)
            when (result) {
                is Result.Success -> {
                    if (result.data.isNotEmpty())
                        newsList.addAll(0, result.data)    // add at 0 index so that distinctBy removes news with id null
                }
            }
        }

        val list = ArrayList(newsList.distinctBy { it.url })
        list.forEach {
            if (it.id == null || it.id.isEmpty()) {
                it.id = UUID.randomUUID().toString()
                it.category = category
                saveNews(it)
            }
        }
        cachedNews[category] = list
    }

    override suspend fun getNewsById(id: String): Result<News> {
        val news = cachedNews.values.flatMap { it }.find { it.id == id }
        if (news != null) {
            Result.Success(news)
        }
        return newsLocalDataSource.getNewsById(id)
    }

    override suspend fun deleteNews(category: String, isBookmarked: Boolean) {
        newsLocalDataSource.deleteNews(category, isBookmarked)
        cachedNews.remove(category)
    }


    override suspend fun saveNews(news: News) {
        newsLocalDataSource.saveNews(news)
    }

    override suspend fun updateBookmarkStatus(news: News) {
        newsLocalDataSource.updateBookmarkStatus(news)
        cachedNews[news.category]?.find { it.id == news.id }?.apply { this.isBookmarked = news.isBookmarked }
    }

    override suspend fun getBookmarkedNews(): Result<ArrayList<News>> {
        return newsLocalDataSource.getBookmarkedNews()
    }

    override suspend fun deleteAllNews() {

    }

    fun setLatestFetchTimestamp(category: String) {
        val edit = getDefaultSharedPreferences(App.getInstance()).edit()
        edit.putLong("${category}latestFetchTimestamp", Date().time)
        edit.commit()
    }

    fun getLatestFetchTimestamp(category: String): Long {
        val time = getDefaultSharedPreferences(App.getInstance()).getLong("${category}latestFetchTimestamp", 0)
        return time
    }

    companion object {

        private var INSTANCE: NewsRepository? = null

        @JvmStatic
        fun getInstance(newsRemoteDataSource: NewsDataSource,
                        newsLocalDataSource: NewsDataSource): NewsRepository {
            return INSTANCE ?: NewsRepository(newsRemoteDataSource, newsLocalDataSource)
                    .apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}