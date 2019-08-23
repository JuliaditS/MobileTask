package com.codelabs.unikomradio.utilities.services

import android.content.Context
import android.net.Uri
import android.os.Handler
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Util

class ExoPlayerStreaming(private val context: Context) : ExoPlayer.EventListener{
    init {


        @C.AudioUsage val usage = Util.getAudioUsageForStreamType(C.STREAM_TYPE_MUSIC)
//        @C.AudioContentType val contentType = Util.getAudioContentTypeForStreamType(C.STREAM_TYPE_MUSIC)
//        val audioAttributes = AudioAttributes.Builder().setUsage(usage).setContentType(contentType).build()
//
//        val bandwidthMeter = DefaultBandwidthMeter()
//        val extractorsFactory = DefaultExtractorsFactory()
//        val trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
//        val dateSourceFactory = DefaultDataSourceFactory(
//            this,
//            Util.getUserAgent(this, this.getPackageName()),
//            bandwidthMeter as TransferListener<in DataSource>
//        )
//        val mediaSource = ExtractorMediaSource(
//            Uri.parse("http://hits.unikom.ac.id:9996/;listen.pls?sid=1"),
//            dateSourceFactory,
//            extractorsFactory,
//            Handler(),
//            Player.EventListener { it.printStackTrace() })    // replace Uri with your song url
//        val exoPlayer = ExoPlayerFactory.newSimpleInstance(context, DefaultTrackSelector(trackSelectionFactory))
//        exoPlayer.prepare(mediaSource)
//        (exoPlayer as SimpleExoPlayer).audioAttributes = audioAttributes
//        exoPlayer.setPlayWhenReady(true)
//        (exoPlayer as SimpleExoPlayer).volume = 1f
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
    }

    override fun onSeekProcessed() {
    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
    }

    override fun onPlayerError(error: ExoPlaybackException?) {
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPositionDiscontinuity(reason: Int) {
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
    }


}