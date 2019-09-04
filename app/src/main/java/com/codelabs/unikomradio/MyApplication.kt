package com.codelabs.unikomradio

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.codelabs.unikomradio.utilities.helper.Preferences
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.exoplayer2.SimpleExoPlayer
import timber.log.Timber


class MyApplication : Application() {

    private var isNightModeEnabled = false

    var mMediaPlayer: MediaPlayer? = null
    lateinit var exoPlayer: SimpleExoPlayer

    private fun initMediaPlayer() {
//        mMediaPlayer = MediaPlayer()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Fresco.initialize(this)

        this.isNightModeEnabled = Preferences(this).isLightMode()

    }

    companion object {
        lateinit var instance: MyApplication

        fun getContext(): Context = instance.applicationContext
    }


}

