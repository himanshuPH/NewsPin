package com.news.android.himanshu.newspin.news

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
import com.news.android.himanshu.newspin.news.ArticleContract
import com.news.android.himanshu.newspin.newsDetail.NewsDetailActivity
import com.news.android.himanshu.newspin.util.gone
import com.news.android.himanshu.newspin.util.inflate
import com.news.android.himanshu.newspin.util.visible
import kotlinx.android.synthetic.main.fragment_article.*


class ArticleFragment : Fragment(), ArticleContract.View, OnNewsItemClick {
    lateinit var newsAdapter:NewsListAdapter
    private var mListener: OnArticleFragmentInteractionListener? = null
    var newsCategory = "Science"
    var mPresenter: ArticleContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            newsCategory = it.getString(NEWS_CATEGORY, "Science")
        }
        context?.let { mPresenter = ArticlePresenter(Injection.provideNewsRepository(it.applicationContext)) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = container?.inflate(R.layout.fragment_article)
        mPresenter?.attachView(this)
        newsAdapter = NewsListAdapter(arrayListOf(),this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter?.getArticlesByCategory(newsCategory)
    }

    override fun onNewsListEmpty() {

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnArticleFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onBookmarkIvClick(news: News, position: Int) {
        mPresenter?.updateBookmarkNews(news, position)
    }

    override fun changeBookmarkStatus(position: Int) {
        newsAdapter.notifyItemChanged(position)
        mListener?.refreshBookmarksNews()
    }

    fun refreshBookmarkStatus(newsId: String){
        newsAdapter.refreshBookmarkStatus(newsId)
    }

    override fun onItemClick(newsId: String) {
        context?.let { startActivity(NewsDetailActivity.createIntent(it,newsId),ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()) }
    }

    override fun showLoader() {
        loader.visible()
        errorTv.gone()
        newsListRv.gone()
    }

    override fun showArticles(articleList: ArrayList<News>) {
        newsAdapter.setData(articleList)
        newsListRv.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = newsAdapter
        }
        loader.gone()
        errorTv.gone()
        newsListRv.visible()
    }

    override fun showError() {
        loader.gone()
        errorTv.visible()
        newsListRv.gone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter?.detachView()
    }

    interface OnArticleFragmentInteractionListener {
        fun refreshBookmarksNews()
    }

    companion object {
        val NEWS_CATEGORY = "newsCategory"

        fun newInstance(newsCategory: String): ArticleFragment {
            val fragment = ArticleFragment()
            val args = Bundle()
            args.putString(NEWS_CATEGORY, newsCategory)
            fragment.arguments = args
            return fragment
        }
    }
}
