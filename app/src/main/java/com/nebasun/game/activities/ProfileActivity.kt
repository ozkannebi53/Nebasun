package com.nebasun.game.activities

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nebasun.game.R
import com.nebasun.game.databinding.ActivityProfileBinding
import com.nebasun.game.databinding.DialogChestBinding
import com.nebasun.game.models.RewardType
import com.nebasun.game.utils.ChestManager
import com.nebasun.game.utils.GamePreferences

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var prefs: GamePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = GamePreferences(this)
        loadProfile()
        setupButtons()
    }

    override fun onResume() {
        super.onResume()
        loadProfile()
    }

    private fun loadProfile() {
        val profile = prefs.loadProfile()

        binding.tvProfileName.text = profile.name.ifEmpty { "Oyuncu" }
        binding.tvProfileLevel.text = "Seviye ${profile.playerLevel}"
        binding.tvTotalXP.text = profile.totalXp.toString()
        binding.tvTotalCoins.text = profile.coins.toString()
        binding.tvDiamonds.text = profile.diamonds.toString()

        if (profile.completedLevels.isEmpty()) {
            binding.tvCompletedLevels.text = "Henüz bölüm tamamlanmadı."
        } else {
            binding.tvCompletedLevels.text = profile.completedLevels
                .sorted()
                .joinToString(", ") { "Bölüm $it ✅" }
        }

        val avatar = profile.unlockedAvatars.lastOrNull() ?: "👤"
        binding.tvProfileAvatar.text = avatar
    }

    private fun setupButtons() {
        binding.btnOpenChest.setOnClickListener {
            val profile = prefs.loadProfile()
            if (profile.totalXp >= ChestManager.CHEST_COST_XP) {
                profile.totalXp -= ChestManager.CHEST_COST_XP
                val reward = ChestManager.openChest()

                when (reward.type) {
                    RewardType.COINS -> profile.coins += reward.amount
                    RewardType.DIAMONDS -> profile.diamonds += reward.amount
                    RewardType.AVATAR -> {
                        if (!profile.unlockedAvatars.contains(reward.avatarEmoji)) {
                            profile.unlockedAvatars.add(reward.avatarEmoji)
                        }
                    }
                }

                prefs.saveProfile(profile)
                showChestDialog(reward.emoji, reward.displayText)
                loadProfile()
            } else {
                Toast.makeText(
                    this,
                    "⚠️ Kutu açmak için ${ChestManager.CHEST_COST_XP} XP gerekli!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showChestDialog(emoji: String, rewardText: String) {
        val dialogBinding = DialogChestBinding.inflate(LayoutInflater.from(this))
        dialogBinding.tvChestReward.text = emoji
        dialogBinding.tvChestRewardDesc.text = rewardText

        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .create()

        val popAnim = AnimationUtils.loadAnimation(this, R.anim.pop)
        dialogBinding.tvChestReward.startAnimation(popAnim)

        dialogBinding.btnCloseChest.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
