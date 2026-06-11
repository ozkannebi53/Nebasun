package com.nebasun.game.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nebasun.game.adapters.LevelAdapter
import com.nebasun.game.databinding.ActivityLevelSelectBinding
import com.nebasun.game.utils.GamePreferences
import com.nebasun.game.utils.WordLoader

class LevelSelectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLevelSelectBinding
    private lateinit var prefs: GamePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = GamePreferences(this)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val levels = WordLoader.loadLevels(this)
        val profile = prefs.loadProfile()

        val adapter = LevelAdapter(levels, profile.completedLevels) { level ->
            // Check if previous level is completed (sequential unlock)
            if (level.id == 1 || profile.completedLevels.contains(level.id - 1)) {
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra("LEVEL_ID", level.id)
                startActivity(intent)
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            } else {
                Toast.makeText(
                    this,
                    "⛔ Önce Bölüm ${level.id - 1}'i tamamlamalısın!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.rvLevels.layoutManager = LinearLayoutManager(this)
        binding.rvLevels.adapter = adapter
    }
}
