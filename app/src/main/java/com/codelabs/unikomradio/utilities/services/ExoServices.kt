package com.codelabs.unikomradio.utilities.services

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Util
import timber.log.Timber

class ExoServices : Service() {
    lateinit var exoPlayer: SimpleExoPlayer

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this,"ADA",Toast.LENGTH_SHORT).show()
        if (intent?.action  == ACTION_PLAY){
            Timber.i("IYA INI CUY")
        } else {
            Timber.i("TIDAK CUY")
        }
//        @C.AudioUsage val usage = Util.getAudioUsageForStreamType(C.STREAM_TYPE_MUSIC)
//        @C.AudioContentType val contentType =
//            Util.getAudioContentTypeForStreamType(C.STREAM_TYPE_MUSIC)
//        val audioAttributes =
//            com.google.android.exoplayer2.audio.AudioAttributes.Builder().setUsage(usage)
//                .setContentType(contentType)
//                .build()
//
//        val bandwidthMeter = DefaultBandwidthMeter()
//        val extractorsFactory = DefaultExtractorsFactory()
//        val trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
//
//        val dateSourceFactory = DefaultDataSourceFactory(
//            this,
//            Util.getUserAgent(this, packageName),
//            bandwidthMeter as TransferListener
//        )
//        val mediaSource = ExtractorMediaSource(
//            Uri.parse("http://hits.unikom.ac.id:9996/;listen.pls?sid=1"),
//            dateSourceFactory,
//            extractorsFactory,
//            Handler(),
//            ExtractorMediaSource.EventListener {
//                it.printStackTrace()
//            }
//        )    // replace Uri with your song url
//
//        exoPlayer =
//            ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector(trackSelectionFactory))
//        exoPlayer.prepare(mediaSource)
//        exoPlayer.audioAttributes = audioAttributes
//        exoPlayer.volume = 1f
//        exoPlayer.playWhenReady = true

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val ACTION_PLAY: String = "com.codelabs.unikomradio.action.PLAY"
    }
}
