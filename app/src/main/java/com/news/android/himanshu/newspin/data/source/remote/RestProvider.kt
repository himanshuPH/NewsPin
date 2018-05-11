package com.news.android.himanshu.newspin.data.source.remote

import com.news.android.himanshu.newspin.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestProvider {
    private fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            client.addInterceptor(HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        client.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Api-Key",BuildConfig.NEWS_READER_API_KEY)
                    .method(original.method(), original.body())
                    .build()
            chain.proceed(request)
        }
        client.connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
        return client.build()
    }

    fun getNewsService(): NewsService {
        return provideRetrofit().create(NewsService::class.java!!)
    }
}