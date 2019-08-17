package com.codelabs.unikomradio.utilities.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import android.widget.Toast
import com.codelabs.unikomradio.MyApplication

class MediaPlayerServices : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private val url = "http://hits.unikom.ac.id:9996/;listen.pls?sid=1"

    private var wifiLock: WifiManager.WifiLock? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock")

        mediaPlayer = (application as MyApplication).mMediaPlayer
        mediaPlayer?.apply {
            reset()
            val audioAttributes = AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                .build()
            setAudioAttributes(audioAttributes)
            setOnPreparedListener(this@MediaPlayerServices)
            setDataSource(url)
            prepareAsync()
//            setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        }

        wifiLock?.acquire()

        return START_STICKY_COMPATIBILITY
    }

    /** Called when MediaPlayer is ready */
    override fun onPrepared(mediaPlayer: MediaPlayer) {
        Log.i("Bacot", "Starting")
        mediaPlayer.start()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onError(player: MediaPlayer?, p1: Int, p2: Int): Boolean {
        Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show()
        player?.release()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
//        wifiLock?.release()
    }

    companion object {
        const val ACTION_PLAY: String = "com.codelabs.unikomradio.action.PLAY"
    }
}