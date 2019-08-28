package com.codelabs.unikomradio.utilities.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class Preferences(private val context: Context) {

    private var sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = sharedPreferences.edit()

    fun setLightMode(state: Boolean) {
        editor.putBoolean(LIGHT_MODE, state)
        editor.apply()
    }

    public fun isLightMode(): Boolean {
        return sharedPreferences.getBoolean(LIGHT_MODE, false)
    }

    companion object {
        const val LIGHT_MODE = "light_mode"
    }
}