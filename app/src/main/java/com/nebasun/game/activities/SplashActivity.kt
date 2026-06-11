package com.nebasun.game.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.nebasun.game.R
import com.nebasun.game.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animate title
        val popAnim = AnimationUtils.loadAnimation(this, R.anim.pop)
        binding.tvSplashTitle.startAnimation(popAnim)

        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.tvSplashSub.startAnimation(fadeIn)

        // Navigate to main after delay
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, 2000)
    }
}
