package com.codelabs.newunikomradio.mvvm

import android.content.Context
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter

class ExoPlayer(context: Context) {
    var exoPlayer: SimpleExoPlayer

    init {
        val bandwidthMeter = DefaultBandwidthMeter()
        val trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        exoPlayer =
            ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector(trackSelectionFactory))
    }

    companion object {
        @Volatile
        private var instance: ExoPlayer? = null

        fun getInstance(context: Context) {
            instance ?: synchronized(this) {
                instance ?: ExoPlayer(context).also { instance = it }
            }
        }
    }
}