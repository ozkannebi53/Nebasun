package com.nebasun.game.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nebasun.game.databinding.ItemLevelBinding
import com.nebasun.game.models.Level

class LevelAdapter(
    private val levels: List<Level>,
    private val completedLevels: List<Int>,
    private val onLevelClick: (Level) -> Unit
) : RecyclerView.Adapter<LevelAdapter.LevelViewHolder>() {

    inner class LevelViewHolder(private val binding: ItemLevelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(level: Level) {
            binding.tvLevelNumber.text = level.id.toString()
            binding.tvLevelTitle.text = "Bölüm ${level.id}"
            binding.tvLevelReward.text = "⭐ ${level.rewardXp} XP  🪙 ${level.rewardCoins}"

            val isCompleted = completedLevels.contains(level.id)
            val isUnlocked = level.id == 1 || completedLevels.contains(level.id - 1)

            binding.tvLevelStatus.text = when {
                isCompleted -> "✅"
                isUnlocked -> "🔓"
                else -> "🔒"
            }

            // Visual feedback for locked levels
            binding.root.alpha = if (isUnlocked) 1.0f else 0.5f

            binding.root.setOnClickListener {
                onLevelClick(level)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        val binding = ItemLevelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LevelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        holder.bind(levels[position])
    }

    override fun getItemCount() = levels.size
}
