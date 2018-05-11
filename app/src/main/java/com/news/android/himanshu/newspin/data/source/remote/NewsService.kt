package com.news.android.himanshu.newspin.data.source.remote

import com.news.android.himanshu.newspin.data.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    //for now country india
    @GET("top-headlines")
    fun getNews(@Query("category") category: String,
                    @Query("country") country: String = "in",
                    @Query("pageSize") pageSize:Int=15): Call<NewsResponse>
}