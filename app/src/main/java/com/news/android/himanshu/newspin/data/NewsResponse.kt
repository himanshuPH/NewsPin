package com.news.android.himanshu.newspin.data

class NewsResponse {
    lateinit var status: String
    var totalResults: Int=0
    lateinit var articles: ArrayList<News>
}