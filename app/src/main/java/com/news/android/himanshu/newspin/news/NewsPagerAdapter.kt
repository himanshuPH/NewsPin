package com.news.android.himanshu.newspin.news

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup
import com.news.android.himanshu.newspin.news.ArticleFragment

class NewsPagerAdapter(fm: FragmentManager, val newsCategoryList: List<String>) : FragmentStatePagerAdapter(fm) {
//    var newsCategoryList = ArrayList<String>()
    var registeredFragments = SparseArray<Fragment>()


    override fun getItem(position: Int): Fragment {
        return ArticleFragment.newInstance(newsCategoryList[position])
    }

    override fun getCount(): Int {
        try {
            return newsCategoryList.size
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    override fun getPageTitle(position: Int): CharSequence = newsCategoryList[position]


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        registeredFragments.put(position, fragment)
        return fragment
    }

    fun getRegisteredFragment(position: Int): Fragment {
        return registeredFragments.get(position)
    }
}