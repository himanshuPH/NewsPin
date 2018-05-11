package com.news.android.himanshu.newspin.data.source.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.news.android.himanshu.newspin.data.News

@Dao
interface NewsDao {

    @Query("SELECT * from news WHERE id=:newsID")
    fun getNewsById(newsID: String): News?

    @Query("SELECT * from news WHERE category=:category")
    fun getNewsByCategory(category: String): List<News>

    @Query("SELECT * from news WHERE isBookmarked =:isBookmarked ")
    fun getBookmarkedNews(isBookmarked: Boolean): List<News>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(news: News)

    @Query("UPDATE news SET isBookmarked =:isBookmarked WHERE id =:newsId")
    fun updateBookmark(newsId: String, isBookmarked: Boolean)

    @Query("DELETE FROM news")
    fun deleteAllNews()

    @Query("DELETE FROM news WHERE category =:category AND isBookmarked =:isBookmarked")
    fun deleteNews(category: String,isBookmarked: Boolean)

}