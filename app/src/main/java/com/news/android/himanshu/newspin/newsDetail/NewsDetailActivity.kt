package com.news.android.himanshu.newspin.newsDetail

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.text.Html
import android.transition.TransitionInflater
import android.util.TypedValue
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.news.android.himanshu.newspin.Injection
import com.news.android.himanshu.newspin.R
import com.news.android.himanshu.newspin.customTabs.CustomTabActivityHelper
import com.news.android.himanshu.newspin.data.News
import com.news.android.himanshu.newspin.elasticView.SystemChromeFader
import com.news.android.himanshu.newspin.util.AnimUtils.getFastOutSlowInInterpolator
import com.news.android.himanshu.newspin.util.ColorUtils
import com.news.android.himanshu.newspin.util.ViewUtils
import kotlinx.android.synthetic.main.activity_news_detail.*


class NewsDetailActivity : AppCompatActivity(), NewsDetailContract.View {
    var mPresenter: NewsDetailContract.Presenter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        mPresenter = NewsDetailPresenter(Injection.provideNewsRepository(applicationContext))
        mPresenter?.attachView(this)
        back.setOnClickListener { finishAfterTransition() }

        draggable_frame?.addListener(
                object : SystemChromeFader(this@NewsDetailActivity) {
                    override fun onDragDismissed() {
                        if (draggable_frame.getTranslationY() > 0) {
                            window.returnTransition = TransitionInflater.from(this@NewsDetailActivity)
                                    .inflateTransition(R.transition.about_return_downward)
                        }
                        finishAfterTransition()
                    }
                })

        mPresenter?.getArticleById(intent.getStringExtra(NEWS_ID) ?: "")

    }

    override fun showArticleDetail(news: News) {

        try {

            Glide.with(this)
                    .load(news.urlToImage ?: "")
                    .listener(requestListener)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(newsIv)
            newsHeadlineTv.text = news.title
            newsDescriptionTv.text = news.description

            shareFab.setOnClickListener {
                try {
                    val body = "${news.title}<br>via NewsPin app<br>${news.url}"
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.putExtra(Intent.EXTRA_TEXT, if (Build.VERSION.SDK_INT >= 24)
                        Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY)
                    else
                        Html.fromHtml(body)
                    )
                    shareIntent.type = "text/plain"
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    val intent = createChooser(shareIntent, "send")
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            linkFab.setOnClickListener {
                CustomTabActivityHelper.openCustomTab(
                        this@NewsDetailActivity,
                        CustomTabsIntent.Builder()
                                .setToolbarColor(ContextCompat.getColor(this@NewsDetailActivity, R.color.colorPrimary))
                                .addDefaultShareMenuItem()
                                .build(),
                        Uri.parse(news.url))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val requestListener = object : RequestListener<String, GlideDrawable> {
        override fun onException(e: Exception?, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
            return false
        }

        override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
            val bitmap = (resource?.getCurrent() as GlideBitmapDrawable).getBitmap()
                    ?: return false
            val twentyFourDip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    24f, this@NewsDetailActivity.getResources().getDisplayMetrics()).toInt()
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters()
                    .setRegion(0, 0, bitmap.getWidth() - 1, twentyFourDip)
                    .generate({ palette ->
                        val isDark: Boolean
                        val lightness = ColorUtils.isDark(palette)
                        if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                            isDark = ColorUtils.isDark(bitmap, bitmap.getWidth() / 2, 0)
                        } else {
                            isDark = lightness == ColorUtils.IS_DARK
                        }

                        if (!isDark) { // make back icon dark on light images
                            back.setColorFilter(ContextCompat.getColor(
                                    this@NewsDetailActivity, R.color.dark_icon))
                        }

                        var statusBarColor = window.statusBarColor
                        val topColor = ColorUtils.getMostPopulousSwatch(palette)

                        if (topColor != null && (isDark || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
                            statusBarColor = ColorUtils.scrimify(topColor.getRgb(),
                                    isDark, SCRIM_ADJUSTMENT)
                            // set a light status bar on M+
                            if (!isDark && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                ViewUtils.setLightStatusBar(newsIv)

                            }
                        }

                        if (statusBarColor != window.statusBarColor) {
                            val statusBarColorAnim = ValueAnimator.ofArgb(window.statusBarColor, statusBarColor)
                            statusBarColorAnim.addUpdateListener { animation -> window.statusBarColor = animation.animatedValue as Int }
                            statusBarColorAnim.duration = 1000L
                            statusBarColorAnim.interpolator = getFastOutSlowInInterpolator(this@NewsDetailActivity)
                            statusBarColorAnim.start()
                        }
                    })

            return false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }

    companion object {
        private val SCRIM_ADJUSTMENT = 0.075f
        val NEWS_ID = "news_id"

        fun createIntent(context: Context, newsID: String): Intent {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(NEWS_ID, newsID)
            return intent
        }
    }
}
