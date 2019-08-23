//package com.codelabs.unikomradio.utilities.services
//
//import android.app.Service
//import android.content.Intent
//import android.media.MediaPlayer
//import android.net.Uri
//import android.os.Handler
//import android.os.IBinder
//import android.widget.Toast
//import com.google.android.exoplayer2.*
//import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
//import com.google.android.exoplayer2.source.ExtractorMediaSource
//import com.google.android.exoplayer2.source.TrackGroupArray
//import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
//import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
//import com.google.android.exoplayer2.trackselection.TrackSelectionArray
//import com.google.android.exoplayer2.upstream.DataSource
//import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
//import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
//import com.google.android.exoplayer2.upstream.TransferListener
//import com.google.android.exoplayer2.util.Util
//import timber.log.Timber
//
//class ExoPlayerServices : Service(), ExoPlayerStreaming.EventListener, MediaPlayer.OnErrorListener {
//    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
//    }
//
//    override fun onSeekProcessed() {
//    }
//
//    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
//    }
//
//    override fun onPlayerError(error: ExoPlaybackException?) {
//        Timber.i(error?.printStackTrace().toString())
//    }
//
//    override fun onLoadingChanged(isLoading: Boolean) {
//    }
//
//    override fun onPositionDiscontinuity(reason: Int) {
//    }
//
//    override fun onRepeatModeChanged(repeatMode: Int) {
//    }
//
//    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
//    }
//
//    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
//    }
//
//    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//    }
//
//    private val url = "http://hits.unikom.ac.id:9996/;listen.pls?sid=1"
//
//    private lateinit var exoPlayer: SimpleExoPlayer
//    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//
//        @C.AudioUsage val usage = Util.getAudioUsageForStreamType(C.STREAM_TYPE_MUSIC)
//        @C.AudioContentType val contentType = Util.getAudioContentTypeForStreamType(C.STREAM_TYPE_MUSIC)
//        val audioAttributes =
//            com.google.android.exoplayer2.audio.AudioAttributes.Builder().setUsage(usage).setContentType(contentType)
//                .build()
//
//        val bandwidthMeter = DefaultBandwidthMeter()
//        val extractorsFactory = DefaultExtractorsFactory()
//        val trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
//
//        val dateSourceFactory = DefaultDataSourceFactory(
//            this,
//            Util.getUserAgent(this, packageName),
//            bandwidthMeter as TransferListener<in DataSource>
//        )
//        val mediaSource = ExtractorMediaSource(
//            Uri.parse("http://hits.unikom.ac.id:9996/;listen.pls?sid=1"),
//            dateSourceFactory,
//            extractorsFactory,
//            Handler(),
//            Throwable::printStackTrace
//        )    // replace Uri with your song url
//
//        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector(trackSelectionFactory))
//        exoPlayer.prepare(mediaSource)
//        exoPlayer.audioAttributes = audioAttributes
//        exoPlayer.playWhenReady = true
//        exoPlayer.volume = 1f
//        return START_STICKY
//    }
//
//    override fun onBind(p0: Intent?): IBinder? {
//        return null
//    }
//
//    override fun onError(player: MediaPlayer?, p1: Int, p2: Int): Boolean {
//        Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show()
//        player?.release()
//        return true
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        exoPlayer.release()
//    }
//
//    companion object {
//        const val ACTION_PLAY: String = "com.codelabs.unikomradio.action.PLAY"
//    }
//}