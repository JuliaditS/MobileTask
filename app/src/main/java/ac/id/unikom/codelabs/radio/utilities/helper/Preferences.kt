package ac.id.unikom.codelabs.radio.utilities.helper

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

    fun isLightMode(): Boolean {
        return sharedPreferences.getBoolean(LIGHT_MODE, false)
    }

    fun setPlaying(state: Boolean){
        editor.putBoolean(IS_PLAYING, state)
        editor.apply()
    }

    fun isPlaying(): Boolean {
        return sharedPreferences.getBoolean(IS_PLAYING, false)
    }

    companion object {
        const val LIGHT_MODE = "light_mode"
        const val IS_PLAYING = "is_playing"
    }
}