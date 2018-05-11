package com.news.android.himanshu.newspin.bookmarkNews

import android.app.ActivityOptions
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.news.android.himanshu.newspin.Injection
import com.news.android.himanshu.newspin.R
import com.news.android.himanshu.newspin.adapter.NewsListAdapter
import com.news.android.himanshu.newspin.adapter.OnNewsItemClick
import com.news.android.himanshu.newspin.data.News
import com.news.android.himanshu.newspin.newsDetail.NewsDetailActivity
import com.news.android.himanshu.newspin.util.gone
import com.news.android.himanshu.newspin.util.inflate
import com.news.android.himanshu.newspin.util.visible
import kotlinx.android.synthetic.main.fragment_bookmark_news.*


class BookmarkNewsFragment : Fragment(),BookmarkNewsContract.View, OnNewsItemClick {

    private var mListener: OnBookmarkNewsFragmentInteractionListener? = null

    var mPresenter: BookmarkNewsContract.Presenter? = null
    lateinit var newsAdapter:NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { mPresenter = BookmarkNewsPresenter(Injection.provideNewsRepository(it.applicationContext)) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = container?.inflate(R.layout.fragment_bookmark_news)
        mPresenter?.attachView(this)
        newsAdapter = NewsListAdapter(arrayListOf(),this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter?.getBookmarkedArticles()
    }

    override fun showLoader() {
        loader.visible()
        errorTv.gone()
        bookmarkNewsListRv.gone()
    }

    override fun showArticles(articleList: ArrayList<News>) {
        newsAdapter.setData(articleList)
        bookmarkNewsListRv.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = newsAdapter
        }
        loader.gone()
        errorTv.gone()
        bookmarkNewsListRv.visible()
    }

    fun refreshBookmarkNews(){
        mPresenter?.getBookmarkedArticles()
    }

    override fun showError() {
        loader.gone()
        errorTv.visible()
        bookmarkNewsListRv.gone()
    }

    override fun onNewsListEmpty() {
        loader.gone()
        errorTv.visible()
        bookmarkNewsListRv.gone()
    }

    override fun onBookmarkIvClick(news: News, position: Int) {
        mPresenter?.updateBookmarkStatus(news,position)
    }

    override fun removeNewsItem(news: News,position:Int) {
        newsAdapter.removeNewsItem(position)
        mListener?.unBookmarkNews(news.id,news.category?:"General")
    }

    override fun onItemClick(newsId: String) {
        context?.let { startActivity(NewsDetailActivity.createIntent(it,newsId), ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()) }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnBookmarkNewsFragmentInteractionListener) {
            mListener = context
            mListener?.onBookmarkNewsFragmentAttach(this)
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnBookmarkNewsFragmentInteractionListener {
        fun onBookmarkNewsFragmentAttach(fragment: BookmarkNewsFragment)
        fun unBookmarkNews(newsId: String, category: String)
    }

    companion object {

        fun newInstance(): BookmarkNewsFragment {
            return BookmarkNewsFragment()
        }

        @JvmField
        val TAG = BookmarkNewsFragment::class.java.simpleName
    }
}
