package com.news.android.himanshu.newspin.splash

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.news.android.himanshu.newspin.MainActivity
import com.news.android.himanshu.newspin.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splash_screen)
            val ivSet = AnimatorInflater.loadAnimator(this, R.animator.logo_animation) as AnimatorSet
            ivSet.setTarget(splashIv)
            val tvSet = AnimatorInflater.loadAnimator(this, R.animator.logo_animation) as AnimatorSet
            tvSet.setTarget(splashTv)
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(ivSet, tvSet)
            animatorSet.duration = 500
            animatorSet.start()
            animatorSet.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {

                }

                override fun onAnimationEnd(animation: Animator) {
                    proceedForMainScreen()
                }

                override fun onAnimationCancel(animation: Animator) {
                    proceedForMainScreen()
                }

                override fun onAnimationRepeat(animation: Animator) {

                }
            })
        } catch (e: Exception) {

            Crashlytics.logException(e)
            proceedForMainScreen()

        }
    }

    private fun proceedForMainScreen() {
        val intent = Intent(this@SplashScreen, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this@SplashScreen.startActivity(intent)
        this@SplashScreen.finish()
    }
}
