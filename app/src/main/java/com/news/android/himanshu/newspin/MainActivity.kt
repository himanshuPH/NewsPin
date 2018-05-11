package com.news.android.himanshu.newspin

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.news.android.himanshu.newspin.bookmarkNews.BookmarkNewsFragment
import com.news.android.himanshu.newspin.news.ArticleFragment
import com.news.android.himanshu.newspin.news.NewsSectionFragment
import com.roughike.bottombar.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnTabSelectListener, NewsSectionFragment.OnNewsSectionFragmentInteractionListener, BookmarkNewsFragment.OnBookmarkNewsFragmentInteractionListener, ArticleFragment.OnArticleFragmentInteractionListener {
    var newsSectionFragment: NewsSectionFragment? = null
    var bookmarkNewsFragment: BookmarkNewsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//            setSupportActionBar(toolbar)
        bottomNavigation.setDefaultTab(R.id.news)
        bottomNavigation.setOnTabSelectListener(this)
    }

    override fun onTabSelected(tabId: Int) {
        onFragmentChanged(supportFragmentManager, tabId)
    }

    override fun changeBottomTabColor(color: Int) {
        bottomNavigation.setActiveTabColor(color)
    }

    override fun onBookmarkNewsFragmentAttach(fragment: BookmarkNewsFragment) {
        bookmarkNewsFragment = fragment
    }

    override fun refreshBookmarksNews() {
        bookmarkNewsFragment?.refreshBookmarkNews()
    }

    override fun unBookmarkNews(newsId: String, category: String) {
        newsSectionFragment?.refreshBookmarkNewsByCategory(newsId,category)
    }

    override fun onNewsSectionFragmentAttach(fragment: NewsSectionFragment) {
        newsSectionFragment=fragment
    }

    fun onFragmentChanged(fragmentManager: FragmentManager, tabId: Int) {
        try {
            val currentVisible = getVisibleFragment(fragmentManager)
            val newsSectionView = getFragmentByTag(fragmentManager, NewsSectionFragment.TAG) as NewsSectionFragment?
            val bookmarkNewsView = getFragmentByTag(fragmentManager, BookmarkNewsFragment.TAG) as BookmarkNewsFragment?

            when (tabId) {
                R.id.news -> {
                    if (newsSectionView == null)
                        onAddAndHide(fragmentManager, NewsSectionFragment.newInstance(), currentVisible)
                    else
                        onShowHideFragment(fragmentManager, newsSectionView, currentVisible)
                }
                R.id.boomarks -> {
                    if (bookmarkNewsView == null)
                        onAddAndHide(fragmentManager, BookmarkNewsFragment.newInstance(), currentVisible)
                    else
                        onShowHideFragment(fragmentManager, bookmarkNewsView, currentVisible)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getFragmentByTag(fragmentManager: FragmentManager, tag: String): Fragment? {
        return fragmentManager.findFragmentByTag(tag)
    }

    fun getVisibleFragment(manager: FragmentManager): Fragment? {
        val fragments = manager.fragments
        if (fragments != null && !fragments.isEmpty()) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible) {
                    return fragment
                }
            }
        }
        return null
    }

    fun onShowHideFragment(fragmentManager: FragmentManager, toShow: Fragment?, toHide: Fragment?) {
        if (toHide == null) {
            fragmentManager
                    .beginTransaction()
                    .show(toShow)
                    .commit()
            toShow?.onHiddenChanged(false)
        } else {
            toHide.onHiddenChanged(true)
            fragmentManager
                    .beginTransaction()
                    .hide(toHide)
                    .show(toShow)
                    .commit()
            toShow?.onHiddenChanged(false)
        }
    }

    fun onAddAndHide(fragmentManager: FragmentManager, toAdd: Fragment, toHide: Fragment?) {

        if (toHide == null) {
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container, toAdd, toAdd.javaClass.simpleName)
                    .commit()
            toAdd.onHiddenChanged(false)
        } else {
            toHide.onHiddenChanged(true)
            fragmentManager
                    .beginTransaction()
                    .hide(toHide)
                    .add(R.id.container, toAdd, toAdd.javaClass.simpleName)
                    .commit()
            toAdd.onHiddenChanged(false)
        }

    }
}
