package com.nebasun.game.models

data class Level(
    val id: Int,
    val letters: List<String>,
    val words: List<String>,
    val bonusWords: List<String> = emptyList(),
    val rewardXp: Int = 100,
    val rewardCoins: Int = 50
)
