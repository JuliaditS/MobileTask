package ac.id.unikom.codelabs.radio.utilities.helper

import android.content.Context
import ac.id.unikom.codelabs.radio.R

class ThemeMode {
    fun setThemeModeOn(context: Context) {
        if (Preferences(context).isLightMode()) {
            context.setTheme(R.style.FeedActivityThemeLight)
        } else {
            context.setTheme(R.style.FeedActivityThemeDark)
        }
    }
}