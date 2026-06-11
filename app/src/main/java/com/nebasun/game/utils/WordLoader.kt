package com.nebasun.game.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nebasun.game.models.Level
import org.json.JSONObject

object WordLoader {

    private var levelsCache: List<Level>? = null
    private var aiDataCache: JSONObject? = null

    fun loadLevels(context: Context): List<Level> {
        if (levelsCache != null) return levelsCache!!

        val json = context.assets.open("words.json").bufferedReader().use { it.readText() }
        val root = JSONObject(json)
        val levelsArray = root.getJSONArray("levels")
        val levels = mutableListOf<Level>()

        for (i in 0 until levelsArray.length()) {
            val obj = levelsArray.getJSONObject(i)
            val id = obj.getInt("id")
            val lettersArr = obj.getJSONArray("letters")
            val wordsArr = obj.getJSONArray("words")
            val bonusArr = if (obj.has("bonus_words")) obj.getJSONArray("bonus_words") else null
            val rewardXp = if (obj.has("reward_xp")) obj.getInt("reward_xp") else 100
            val rewardCoins = if (obj.has("reward_coins")) obj.getInt("reward_coins") else 50

            val letters = (0 until lettersArr.length()).map { lettersArr.getString(it) }
            val words = (0 until wordsArr.length()).map { wordsArr.getString(it) }
            val bonus = if (bonusArr != null) (0 until bonusArr.length()).map { bonusArr.getString(it) } else emptyList()

            levels.add(Level(id, letters, words, bonus, rewardXp, rewardCoins))
        }

        levelsCache = levels
        return levels
    }

    fun loadAIData(context: Context): JSONObject {
        if (aiDataCache != null) return aiDataCache!!
        val json = context.assets.open("words.json").bufferedReader().use { it.readText() }
        val root = JSONObject(json)
        aiDataCache = root.getJSONObject("ai_data")
        return aiDataCache!!
    }

    fun getRandomQuote(context: Context): String {
        val aiData = loadAIData(context)
        val quotes = aiData.getJSONArray("quotes_of_the_day")
        return quotes.getString((0 until quotes.length()).random())
    }

    fun getRandomProverbs(context: Context, count: Int = 2): List<String> {
        val aiData = loadAIData(context)
        val proverbs = aiData.getJSONArray("proverbs")
        val idioms = aiData.getJSONArray("idioms")
        val all = mutableListOf<String>()
        for (i in 0 until proverbs.length()) all.add("📖 " + proverbs.getString(i))
        for (i in 0 until idioms.length()) all.add("💬 " + idioms.getString(i))
        return all.shuffled().take(count)
    }
}
