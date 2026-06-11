package com.nebasun.game.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.nebasun.game.R
import com.nebasun.game.ai.AkrepZeka
import com.nebasun.game.databinding.ActivityMainBinding
import com.nebasun.game.databinding.DialogAiAssistantBinding
import com.nebasun.game.utils.GamePreferences
import com.nebasun.game.utils.WordLoader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: GamePreferences
    private lateinit var akrepZeka: AkrepZeka

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = GamePreferences(this)
        akrepZeka = AkrepZeka(this)

        setupUI()
        loadQuote()
    }

    override fun onResume() {
        super.onResume()
        // Reload player name if saved
        val savedName = prefs.loadPlayerName()
        if (savedName.isNotEmpty()) {
            binding.etPlayerName.setText(savedName)
        }
    }

    private fun setupUI() {
        // Animate title
        val popAnim = AnimationUtils.loadAnimation(this, R.anim.pop)
        binding.tvAppTitle.startAnimation(popAnim)

        binding.btnPlay.setOnClickListener {
            savePlayerName()
            val intent = Intent(this, LevelSelectActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        binding.btnMultiplayer.setOnClickListener {
            savePlayerName()
            val intent = Intent(this, MultiplayerActivity::class.java)
            startActivity(intent)
        }

        binding.btnProfile.setOnClickListener {
            savePlayerName()
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnAI.setOnClickListener {
            showAIAssistantDialog()
        }
    }

    private fun savePlayerName() {
        val name = binding.etPlayerName.text?.toString()?.trim()
        if (!name.isNullOrEmpty()) {
            prefs.savePlayerName(name)
            val profile = prefs.loadProfile()
            profile.name = name
            prefs.saveProfile(profile)
        }
    }

    private fun loadQuote() {
        try {
            val quote = akrepZeka.getDailyQuote()
            binding.tvQuote.text = quote
        } catch (e: Exception) {
            binding.tvQuote.text = "Başarı bir yolculuktur, bir varış noktası değil."
        }
    }

    private fun showAIAssistantDialog() {
        val dialogBinding = DialogAiAssistantBinding.inflate(LayoutInflater.from(this))

        // Load quote and proverbs
        dialogBinding.tvAIQuote.text = akrepZeka.getDailyQuote()
        val proverbsAndIdioms = akrepZeka.getRandomProverbsAndIdioms()
        dialogBinding.tvAIProverbs.text = proverbsAndIdioms.joinToString("\n\n")

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.btnAskAI.setOnClickListener {
            val question = dialogBinding.etAIInput.text?.toString()?.trim() ?: ""
            if (question.isNotEmpty()) {
                val response = akrepZeka.answerQuestion(question)
                dialogBinding.tvAIResponse.text = response
                dialogBinding.tvAIResponse.visibility = android.view.View.VISIBLE
                val anim = AnimationUtils.loadAnimation(this, R.anim.fade_in)
                dialogBinding.tvAIResponse.startAnimation(anim)
            }
        }

        dialog.show()
    }
}
