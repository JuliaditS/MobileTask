package com.codelabs.unikomradio

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.widget.Toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Util
import timber.log.Timber
import java.io.IOException


class MyApplication : Application() {
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

        initRadioStreaming()

        Fresco.initialize(this)
        try {
            initMediaPlayer()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRadioStreaming() {
        @C.AudioUsage val usage = Util.getAudioUsageForStreamType(C.STREAM_TYPE_MUSIC)
        @C.AudioContentType val contentType = Util.getAudioContentTypeForStreamType(C.STREAM_TYPE_MUSIC)
        val audioAttributes =
            com.google.android.exoplayer2.audio.AudioAttributes.Builder().setUsage(usage).setContentType(contentType)
                .build()

        val bandwidthMeter = DefaultBandwidthMeter()
        val extractorsFactory = DefaultExtractorsFactory()
        val trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)

        val dateSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, packageName),
            bandwidthMeter as TransferListener<in DataSource>
        )
        val mediaSource = ExtractorMediaSource(
            Uri.parse("http://hits.unikom.ac.id:9996/;listen.pls?sid=1"),
            dateSourceFactory,
            extractorsFactory,
            Handler(),
            ExtractorMediaSource.EventListener {
                it.printStackTrace()
            }
        )    // replace Uri with your song url

        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector(trackSelectionFactory))
        exoPlayer.prepare(mediaSource)
        exoPlayer.audioAttributes = audioAttributes
        exoPlayer.volume = 1f
    }

    companion object {
        lateinit var instance: MyApplication

        fun getContext(): Context = instance.applicationContext
    }

}