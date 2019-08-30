package com.codelabs.unikomradio.mvvm.settings

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.mvvm.main.MainActivity
import com.codelabs.unikomradio.utilities.helper.Preferences
import kotlinx.android.synthetic.main.about.view.*
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    private lateinit var dialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.title = getString(R.string.settings)
        supportActionBar?.elevation = 0f
        settings_light_mode_switch.isChecked = Preferences(this).isLightMode()

        settings_light_mode_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            Preferences(this).setLightMode(isChecked)
            this.startActivity(Intent(this, SettingsActivity::class.java))
            this.finishAffinity()
        }

        if (Preferences(this).isLightMode()) {
            settings_state_lightmode.text = "On"
        } else {
            settings_state_lightmode.text = "Off"
        }

        if (Preferences(this).isLightMode()) {
            settings_layout.setBackgroundColor(resources.getColor(android.R.color.white))
        } else {
            settings_notification_label.setTextColor(resources.getColor(android.R.color.white))
            settings_light_mode_label.setTextColor(resources.getColor(android.R.color.white))
            settings_about.setTextColor(resources.getColor(android.R.color.white))
            settings_about_divider.setBackgroundColor(Color.parseColor("#364466"))
            settings_lightmode_divider.setBackgroundColor(Color.parseColor("#364466"))
            settings_notification_divider.setBackgroundColor(Color.parseColor("#364466"))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        settings_about.setOnClickListener {
            DialogForm()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        if (Preferences(this).isLightMode()) {
            theme.applyStyle(R.style.AppTheme_Light, true)
        } else {
            theme.applyStyle(R.style.AppTheme_Dark, true)
        }
        return super.getTheme()
    }

    fun DialogForm() {
        dialog = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.about, null)
        if (Preferences(this).isLightMode()) {
            view.about_logo_codelabs.setImageDrawable(getDrawable(R.drawable.logocodelabsblack))
        } else {
            view.background = ColorDrawable(resources.getColor(R.color.colorPrimary))
        }
        dialog.setView(view)
        dialog.show()

    }
}
