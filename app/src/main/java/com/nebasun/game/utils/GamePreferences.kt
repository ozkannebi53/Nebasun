package com.nebasun.game.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.nebasun.game.models.PlayerProfile

class GamePreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "nebasun_prefs"
        private const val KEY_PROFILE = "player_profile"
        private const val KEY_PLAYER_NAME = "player_name"
    }

    fun saveProfile(profile: PlayerProfile) {
        prefs.edit().putString(KEY_PROFILE, gson.toJson(profile)).apply()
    }

    fun loadProfile(): PlayerProfile {
        val json = prefs.getString(KEY_PROFILE, null)
        return if (json != null) {
            gson.fromJson(json, PlayerProfile::class.java)
        } else {
            PlayerProfile()
        }
    }

    fun savePlayerName(name: String) {
        prefs.edit().putString(KEY_PLAYER_NAME, name).apply()
    }

    fun loadPlayerName(): String {
        return prefs.getString(KEY_PLAYER_NAME, "") ?: ""
    }
}
