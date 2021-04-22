package com.example.kotlinapplication.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.example.kotlinapplication.HomeActivity
import com.example.kotlinapplication.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation)
        val middleAnimation = AnimationUtils.loadAnimation(this,R.anim.middle_animation)
        val bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animation)

        mainTitle.startAnimation(topAnimation)
        subTitle.startAnimation(bottomAnimation)
        mainImage.startAnimation(middleAnimation)

        val splashTimeOut = 4000
        val intent = Intent(this@SplashActivity,HomeActivity::class.java)
        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, splashTimeOut.toLong())

    }
}