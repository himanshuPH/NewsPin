package com.news.android.himanshu.newspin.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.news.android.himanshu.newspin.R
import com.news.android.himanshu.newspin.data.News
import com.news.android.himanshu.newspin.util.inflate


class NewsListAdapter(var newsList: ArrayList<News>, val onNewsItemClick: OnNewsItemClick) : RecyclerView.Adapter<NewsListAdapter.NewsItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsItemViewHolder = NewsItemViewHolder(parent.inflate(R.layout.news_list_item))

    override fun onBindViewHolder(holder: NewsItemViewHolder, position: Int) {
        val newsItem = newsList[holder.adapterPosition]
        showCategoryImage(holder.newsIv, newsItem.urlToImage ?: "",holder.view.context)
        holder.newsHeadingTv.maxLines = if (newsItem.description.isNullOrEmpty()) 3 else 2
        holder.newsHeadingTv.text = newsItem.title?:""
        holder.newsDescTv.text = newsItem.description ?: ""
        holder.newsSourceTv.text = newsItem.source?.name ?: ""
        holder.bookmarkIv.setImageDrawable(ContextCompat.getDrawable(holder.view.context, if (newsItem.isBookmarked) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border))
        holder.bookmarkIv.setOnClickListener {
            newsItem.isBookmarked = !newsItem.isBookmarked
            onNewsItemClick.onBookmarkIvClick(newsItem, holder.adapterPosition)
        }
        holder.cardView.setOnClickListener {
            onNewsItemClick.onItemClick(newsItem.id)
        }

    }

    fun setData(newsList: ArrayList<News>){
        this.newsList = newsList
        notifyDataSetChanged()
    }

    fun refreshBookmarkStatus(newsId: String){
        val position = newsList.indexOfFirst { it.id==newsId }
        val news = newsList[position]
        newsList.set(position,news)
        notifyItemChanged(position)
    }

    fun removeNewsItem(position: Int){
        newsList.removeAt(position)
        notifyItemRemoved(position)
        if(newsList.isEmpty())
            onNewsItemClick.onNewsListEmpty()
    }

    override fun getItemCount(): Int = newsList.size

    fun showCategoryImage(newsIv: ImageView, imageUrl: String,context:Context) {
        try {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(newsIv)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    class NewsItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val cardView = view.findViewById<CardView>(R.id.cardView)
        val newsHeadingTv = view.findViewById<TextView>(R.id.newsHeadingTv)
        val newsIv = view.findViewById<ImageView>(R.id.newsIv)
        val newsDescTv = view.findViewById<TextView>(R.id.newsDescTv)
        val bookmarkIv = view.findViewById<ImageView>(R.id.bookmarkIv)
        val newsSourceTv = view.findViewById<TextView>(R.id.newsSourceTv)
    }
}

interface OnNewsItemClick {
    fun onBookmarkIvClick(news: News, position: Int)
    fun onItemClick(newsId: String)
    fun onNewsListEmpty()
}