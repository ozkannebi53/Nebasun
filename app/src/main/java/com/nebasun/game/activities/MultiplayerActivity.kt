package com.nebasun.game.activities

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nebasun.game.R
import com.nebasun.game.databinding.ActivityMultiplayerBinding
import com.nebasun.game.utils.EmojiUtils
import com.nebasun.game.utils.GamePreferences
import com.nebasun.game.utils.WordLoader

class MultiplayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMultiplayerBinding
    private lateinit var prefs: GamePreferences

    private var player1Score = 0
    private var player2Score = 0
    private var timer: CountDownTimer? = null
    private var timeLeft = 60
    private var isGameOver = false

    private val foundWords = mutableSetOf<String>()
    private val aiFoundWords = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = GamePreferences(this)
        setupMultiplayer()
    }

    private fun setupMultiplayer() {
        val profile = prefs.loadProfile()
        binding.tvPlayer1Name.text = profile.name.ifEmpty { "Sen" }

        val levels = WordLoader.loadLevels(this)
        val level = levels.random()
        binding.letterCircleViewMP.setLetters(level.letters)

        binding.letterCircleViewMP.onLetterSelected = { letter ->
            val current = binding.tvCurrentWordMP.text.toString()
            binding.tvCurrentWordMP.text = current + letter
        }

        binding.letterCircleViewMP.onWordFormed = { word ->
            binding.tvCurrentWordMP.text = ""
            if (!isGameOver) {
                if (word in level.words && word !in foundWords) {
                    foundWords.add(word)
                    player1Score += word.length * 10
                    binding.tvPlayer1Score.text = player1Score.toString()
                    val anim = AnimationUtils.loadAnimation(this, R.anim.pop)
                    binding.tvPlayer1Score.startAnimation(anim)
                    showFeedback(EmojiUtils.randomHappyEmoji())
                } else if (word !in level.words) {
                    showFeedback(EmojiUtils.randomSadEmoji())
                    val shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake)
                    binding.letterCircleViewMP.startAnimation(shakeAnim)
                }
            }
            binding.letterCircleViewMP.clearSelection()
        }

        startTimer()
        simulateAI(level.words)
    }

    private fun showFeedback(emoji: String) {
        binding.tvFeedbackMP.text = emoji
        binding.tvFeedbackMP.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(this, R.anim.pop)
        binding.tvFeedbackMP.startAnimation(anim)
        android.os.Handler(mainLooper).postDelayed({
            binding.tvFeedbackMP.visibility = View.GONE
        }, 600)
    }

    private fun startTimer() {
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = (millisUntilFinished / 1000).toInt()
                binding.tvTimer.text = "⏱ $timeLeft"
                if (timeLeft <= 10) {
                    binding.tvTimer.setTextColor(getColor(R.color.error_red))
                }
            }

            override fun onFinish() {
                binding.tvTimer.text = "⏱ 0"
                if (!isGameOver) {
                    isGameOver = true
                    showResult()
                }
            }
        }.start()
    }

    private fun simulateAI(words: List<String>) {
        // AI difficulty: finds words at varying speeds
        val shuffledWords = words.shuffled()
        var delay = 4000L
        for (word in shuffledWords) {
            android.os.Handler(mainLooper).postDelayed({
                if (!isGameOver && !aiFoundWords.contains(word)) {
                    aiFoundWords.add(word)
                    player2Score += word.length * 10
                    binding.tvPlayer2Score.text = player2Score.toString()
                    val anim = AnimationUtils.loadAnimation(this, R.anim.pop)
                    binding.tvPlayer2Score.startAnimation(anim)
                }
            }, delay)
            delay += (2500..7000).random()
        }
    }

    private fun showResult() {
        timer?.cancel()
        val (title, message, emoji) = when {
            player1Score > player2Score -> Triple(
                "Kazandın! 🏆",
                "Tebrikler! Yapay zekayı yendin!\n\nSenin Puanın: $player1Score\nRakip Puanı: $player2Score",
                "🥇"
            )
            player1Score < player2Score -> Triple(
                "Kaybettin 😢",
                "Yapay zeka kazandı. Tekrar dene!\n\nSenin Puanın: $player1Score\nRakip Puanı: $player2Score",
                "😔"
            )
            else -> Triple(
                "Berabere! 🤝",
                "İkisi de aynı puanı aldı!\n\nPuan: $player1Score",
                "🤝"
            )
        }

        AlertDialog.Builder(this)
            .setTitle("$emoji $title")
            .setMessage(message)
            .setPositiveButton("Tekrar Oyna") { _, _ ->
                finish()
                startActivity(intent)
            }
            .setNegativeButton("Ana Menü") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
