package com.news.android.himanshu.newspin.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "news")
data class News constructor(@PrimaryKey @ColumnInfo(name = "id") var id: String="", @SerializedName("source") @Embedded(prefix = "source_") val source: NewsSource?=null,
                                          val author:String?=null,val title:String? =null,val description:String?=null,
                                          val url : String?=null,val urlToImage: String?=null,val publishedAt:String="",var category:String?,var isBookmarked:Boolean=false){
}