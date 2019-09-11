package com.codelabs.unikomradio

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.codelabs.unikomradio.mvvm.main.MainActivity
import com.codelabs.unikomradio.utilities.helper.Preferences

class NoInternet : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.no_internet_connection)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f
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
}