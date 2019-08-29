package com.codelabs.unikomradio.mvvm.settings

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.paris.utils.getStyle
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.mvvm.main.MainActivity
import com.codelabs.unikomradio.utilities.helper.Preferences
import com.jakewharton.processphoenix.ProcessPhoenix
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settings_light_mode_switch.isChecked = Preferences(this).isLightMode()

        settings_light_mode_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            Preferences(this).setLightMode(isChecked)

            ProcessPhoenix.triggerRebirth(this, Intent(this, SettingsActivity::class.java))
        }

        if (Preferences(this).isLightMode()){
            settings_state_lightmode.text = "On"
        } else {
            settings_state_lightmode.text = "Off"
        }

        if (Preferences(this).isLightMode()){
            settings_layout.setBackgroundColor(resources.getColor(android.R.color.white))
        } else {
            settings_layout.setBackgroundColor(resources.getColor(R.color.colorSecondary))
            settingslayout.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            settings_notification_label.setTextColor(resources.getColor(android.R.color.white))
            settings_light_mode_label.setTextColor(resources.getColor(android.R.color.white))
            settings_about.setTextColor(resources.getColor(android.R.color.white))
            settings_label_general.setTextColor(resources.getColor(android.R.color.white))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
