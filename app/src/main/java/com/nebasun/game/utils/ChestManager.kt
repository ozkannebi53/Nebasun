package com.nebasun.game.utils

import com.nebasun.game.models.ChestReward
import com.nebasun.game.models.RewardType

object ChestManager {

    val CHEST_COST_XP = 100

    private val avatarPool = listOf(
        "🦁", "🐯", "🦊", "🐺", "🦝", "🐻", "🐼", "🦄", "🐲", "🦅",
        "🦋", "🐬", "🦈", "🦁", "🐉", "⚡", "🌊", "🔥", "🌙", "⭐"
    )

    fun openChest(): ChestReward {
        val rand = (1..100).random()
        return when {
            rand <= 50 -> {
                // 50% chance: coins
                val amount = (10..100).random()
                ChestReward(
                    type = RewardType.COINS,
                    amount = amount,
                    displayText = "+$amount Altın",
                    emoji = "🪙"
                )
            }
            rand <= 80 -> {
                // 30% chance: diamonds
                val amount = (1..10).random()
                ChestReward(
                    type = RewardType.DIAMONDS,
                    amount = amount,
                    displayText = "+$amount Elmas",
                    emoji = "💎"
                )
            }
            else -> {
                // 20% chance: avatar
                val avatar = avatarPool.random()
                ChestReward(
                    type = RewardType.AVATAR,
                    avatarEmoji = avatar,
                    displayText = "Yeni Avatar: $avatar",
                    emoji = avatar
                )
            }
        }
    }
}
