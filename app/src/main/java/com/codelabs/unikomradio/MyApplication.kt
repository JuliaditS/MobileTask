package com.codelabs.unikomradio

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.PowerManager
import android.widget.Toast
import java.io.IOException
import timber.log.Timber



class MyApplication : Application(){
    var mMediaPlayer: MediaPlayer? = null

    private fun initMediaPlayer(){
        mMediaPlayer = MediaPlayer()
    }

    override fun onCreate() {
        super.onCreate()
        instance  = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        try {
            initMediaPlayer()
        } catch (e: IOException){
            e.printStackTrace()
            Toast.makeText(this,"Check internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        lateinit var instance : MyApplication

        fun getContext(): Context = instance.applicationContext
    }

}