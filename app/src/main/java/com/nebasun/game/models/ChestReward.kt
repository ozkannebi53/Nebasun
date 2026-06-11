package com.nebasun.game.models

enum class RewardType { COINS, DIAMONDS, AVATAR }

data class ChestReward(
    val type: RewardType,
    val amount: Int = 0,
    val avatarEmoji: String = "",
    val displayText: String,
    val emoji: String
)
