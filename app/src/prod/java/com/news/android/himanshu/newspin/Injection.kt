package com.news.android.himanshu.newspin

import android.content.Context
import com.news.android.himanshu.newspin.data.source.NewsRepository
import com.news.android.himanshu.newspin.data.source.local.NewsDatabase
import com.news.android.himanshu.newspin.data.source.local.NewsLocalDataSource
import com.news.android.himanshu.newspin.data.source.remote.NewsRemoteDataSource
import com.news.android.himanshu.newspin.data.source.remote.RestProvider
import com.news.android.himanshu.newspin.util.AppExecutors

object Injection {
    fun provideNewsRepository(context: Context): NewsRepository {
        val database = NewsDatabase.getInstance(context)
        return NewsRepository.getInstance(NewsRemoteDataSource.getInstance(RestProvider.getNewsService()),
                NewsLocalDataSource.getInstance(AppExecutors(), database.newsDao()))
    }
}
