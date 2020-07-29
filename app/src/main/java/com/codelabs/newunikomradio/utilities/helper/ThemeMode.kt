package com.codelabs.newunikomradio.utilities.helper

import android.content.Context
import com.codelabs.newunikomradio.R

class ThemeMode {
    fun setThemeModeOn(context: Context) {
        if (Preferences(context).isLightMode()) {
            context.setTheme(R.style.FeedActivityThemeLight)
        } else {
            context.setTheme(R.style.FeedActivityThemeDark)
        }
    }
}