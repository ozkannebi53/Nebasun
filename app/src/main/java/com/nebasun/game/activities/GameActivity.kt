package com.nebasun.game.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nebasun.game.R
import com.nebasun.game.databinding.ActivityGameBinding
import com.nebasun.game.databinding.DialogLevelCompleteBinding
import com.nebasun.game.models.Level
import com.nebasun.game.utils.EmojiUtils
import com.nebasun.game.utils.GamePreferences
import com.nebasun.game.utils.WordLoader

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var prefs: GamePreferences
    private lateinit var currentLevel: Level
    private val foundWords = mutableSetOf<String>()
    private val answerBoxRows = mutableMapOf<String, LinearLayout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = GamePreferences(this)

        val levelId = intent.getIntExtra("LEVEL_ID", 1)
        val levels = WordLoader.loadLevels(this)
        currentLevel = levels.find { it.id == levelId } ?: levels.first()

        setupGame()
    }

    private fun setupGame() {
        val profile = prefs.loadProfile()
        binding.tvLevel.text = "Bölüm ${currentLevel.id}"
        binding.tvCoins.text = "🪙 ${profile.coins}"
        binding.tvXP.text = "⭐ ${profile.totalXp} XP"

        // Setup letter circle
        binding.letterCircleView.setLetters(currentLevel.letters)

        // Setup answer boxes
        setupAnswerBoxes()

        // Handle word formed
        binding.letterCircleView.onWordFormed = { word ->
            checkWord(word)
        }

        binding.letterCircleView.onLetterSelected = { letter ->
            val current = binding.tvCurrentWord.text.toString()
            binding.tvCurrentWord.text = current + letter
        }
    }

    private fun setupAnswerBoxes() {
        binding.answerBoxesLayout.removeAllViews()
        answerBoxRows.clear()

        for (word in currentLevel.words) {
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).also { it.setMargins(0, 8, 0, 8) }
            }

            for (char in word) {
                val box = TextView(this).apply {
                    text = "_"
                    textSize = 20f
                    setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                    background = ContextCompat.getDrawable(context, R.drawable.rounded_word_bg)
                    setPadding(16, 12, 16, 12)
                    layoutParams = LinearLayout.LayoutParams(52, 52).also {
                        it.setMargins(4, 0, 4, 0)
                    }
                    gravity = android.view.Gravity.CENTER
                }
                row.addView(box)
            }

            answerBoxRows[word] = row
            binding.answerBoxesLayout.addView(row)
        }
    }

    private fun checkWord(word: String) {
        binding.tvCurrentWord.text = ""

        when {
            word in currentLevel.words && word !in foundWords -> {
                foundWords.add(word)
                showCorrectFeedback()
                fillWordBoxes(word)

                if (foundWords.containsAll(currentLevel.words.toSet())) {
                    android.os.Handler(mainLooper).postDelayed({
                        showLevelCompleteDialog()
                    }, 800)
                }
            }
            word in currentLevel.bonusWords && word !in foundWords -> {
                foundWords.add(word)
                showBonusFeedback(word)
            }
            word in foundWords -> {
                showAlreadyFoundFeedback()
            }
            else -> {
                showWrongFeedback()
            }
        }

        binding.letterCircleView.clearSelection()
    }

    private fun fillWordBoxes(word: String) {
        val row = answerBoxRows[word] ?: return
        for (i in word.indices) {
            val box = row.getChildAt(i) as? TextView ?: continue
            box.text = word[i].toString()
            box.setTextColor(ContextCompat.getColor(this, R.color.success_green))
            val anim = AnimationUtils.loadAnimation(this, R.anim.pop)
            box.startAnimation(anim)
        }
    }

    private fun showCorrectFeedback() {
        val emoji = EmojiUtils.randomHappyEmoji()
        binding.tvFeedback.text = emoji
        binding.tvFeedback.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(this, R.anim.pop)
        binding.tvFeedback.startAnimation(anim)
        android.os.Handler(mainLooper).postDelayed({
            binding.tvFeedback.visibility = View.GONE
        }, 800)
    }

    private fun showBonusFeedback(word: String) {
        binding.tvFeedback.text = "✨ BONUS!"
        binding.tvFeedback.visibility = View.VISIBLE
        val anim = AnimationUtils.loadAnimation(this, R.anim.pop)
        binding.tvFeedback.startAnimation(anim)
        android.os.Handler(mainLooper).postDelayed({
            binding.tvFeedback.visibility = View.GONE
        }, 1000)
    }

    private fun showWrongFeedback() {
        val emoji = EmojiUtils.randomSadEmoji()
        binding.tvFeedback.text = emoji
        binding.tvFeedback.visibility = View.VISIBLE

        val shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake)
        binding.tvFeedback.startAnimation(shakeAnim)
        binding.letterCircleView.startAnimation(shakeAnim)

        android.os.Handler(mainLooper).postDelayed({
            binding.tvFeedback.visibility = View.GONE
        }, 700)
    }

    private fun showAlreadyFoundFeedback() {
        binding.tvFeedback.text = "✅"
        binding.tvFeedback.visibility = View.VISIBLE
        android.os.Handler(mainLooper).postDelayed({
            binding.tvFeedback.visibility = View.GONE
        }, 600)
    }

    private fun showLevelCompleteDialog() {
        val dialogBinding = DialogLevelCompleteBinding.inflate(LayoutInflater.from(this))
        dialogBinding.tvCompleteReward.text = "⭐ +${currentLevel.rewardXp} XP  🪙 +${currentLevel.rewardCoins}"

        // Animate confetti emoji
        val popAnim = AnimationUtils.loadAnimation(this, R.anim.pop)
        dialogBinding.tvConfettiEmoji.startAnimation(popAnim)

        // Update profile
        val profile = prefs.loadProfile()
        profile.totalXp += currentLevel.rewardXp
        profile.coins += currentLevel.rewardCoins
        if (!profile.completedLevels.contains(currentLevel.id)) {
            profile.completedLevels.add(currentLevel.id)
        }
        prefs.saveProfile(profile)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()

        dialogBinding.btnNextLevel.setOnClickListener {
            dialog.dismiss()
            val levels = WordLoader.loadLevels(this)
            val nextLevel = levels.find { it.id == currentLevel.id + 1 }
            if (nextLevel != null) {
                val intent = android.content.Intent(this, GameActivity::class.java)
                intent.putExtra("LEVEL_ID", nextLevel.id)
                startActivity(intent)
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                finish()
            } else {
                android.widget.Toast.makeText(this, "🎊 Tüm bölümleri tamamladın!", android.widget.Toast.LENGTH_LONG).show()
                finish()
            }
        }

        dialogBinding.btnBackToMenu.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }
}
