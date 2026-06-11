package com.nebasun.game.models

data class PlayerProfile(
    var name: String = "Oyuncu",
    var totalXp: Int = 0,
    var coins: Int = 0,
    var diamonds: Int = 0,
    var currentLevel: Int = 1,
    var completedLevels: MutableList<Int> = mutableListOf(),
    var unlockedAvatars: MutableList<String> = mutableListOf("👤")
) {
    val playerLevel: Int
        get() = (totalXp / 500) + 1
}
