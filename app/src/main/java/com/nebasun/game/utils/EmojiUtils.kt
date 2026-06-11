package com.nebasun.game.utils

object EmojiUtils {

    val sadEmojis = listOf("😢", "😞", "😔", "😟", "🙁", "😕", "😣", "😖", "😩", "😫")
    val happyEmojis = listOf("🎉", "🥳", "😄", "🌟", "✨", "🎊", "👏", "🔥", "💪", "🏆")
    val chestEmojis = listOf("🎁", "📦", "🎀", "🎰", "💝")

    fun randomSadEmoji(): String = sadEmojis.random()
    fun randomHappyEmoji(): String = happyEmojis.random()
    fun randomChestEmoji(): String = chestEmojis.random()
}
