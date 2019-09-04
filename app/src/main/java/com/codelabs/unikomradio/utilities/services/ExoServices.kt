package com.codelabs.unikomradio.utilities.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Binder
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.mvvm.ExoPlayer
import com.codelabs.unikomradio.utilities.MEDIA_SESSION_TAG
import com.codelabs.unikomradio.utilities.PLAYBACK_CHANNEL_ID
import com.codelabs.unikomradio.utilities.PLAYBACK_NOTIFICATION_ID
import com.codelabs.unikomradio.utilities.helper.Preferences
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Util
import timber.log.Timber

class ExoServices : Service() {
    lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    private fun initStreamingMusic() {
        @C.AudioUsage val usage = Util.getAudioUsageForStreamType(C.STREAM_TYPE_MUSIC)
        @C.AudioContentType val contentType =
            Util.getAudioContentTypeForStreamType(C.STREAM_TYPE_MUSIC)
        val audioAttributes =
            com.google.android.exoplayer2.audio.AudioAttributes.Builder().setUsage(usage)
                .setContentType(contentType)
                .build()

        val bandwidthMeter = DefaultBandwidthMeter()
        val extractorsFactory = DefaultExtractorsFactory()
//        val trackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)

        val dateSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, packageName),
            bandwidthMeter as TransferListener
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

        exoPlayer = ExoPlayer(this).exoPlayer
        exoPlayer.prepare(mediaSource)
        exoPlayer.audioAttributes = audioAttributes
        exoPlayer.volume = 1f

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this,
            PLAYBACK_CHANNEL_ID,
            R.string.playback_channel_name,
            PLAYBACK_NOTIFICATION_ID,
            object : PlayerNotificationManager.MediaDescriptionAdapter {
                override fun getCurrentContentTitle(player: Player): String {
                    return "Unikom Radio"
                }

                override fun createCurrentContentIntent(player: Player): PendingIntent? {
                    return null
                }

                override fun getCurrentContentText(player: Player): String? {
                    return ""
                }

                override fun getCurrentLargeIcon(
                    player: Player,
                    callback: PlayerNotificationManager.BitmapCallback
                ): Bitmap? {
                    return BitmapFactory.decodeResource(this@ExoServices.resources, R.drawable.thumbnailradio)
                }

            }
        )

        playerNotificationManager.setNotificationListener(object :
            PlayerNotificationManager.NotificationListener {
            override fun onNotificationStarted(notificationId: Int, notification: Notification) {
                startForeground(notificationId, notification)
            }

            override fun onNotificationCancelled(notificationId: Int) {
                stopSelf()
            }
        })
        playerNotificationManager.setPlayer(exoPlayer)

        mediaSession = MediaSessionCompat(this, MEDIA_SESSION_TAG)
        mediaSession.isActive = true
        playerNotificationManager.setMediaSessionToken(mediaSession.sessionToken)

        mediaSessionConnector = MediaSessionConnector(mediaSession)
        mediaSessionConnector.setQueueNavigator(object : TimelineQueueNavigator(mediaSession) {
            override fun getMediaDescription(
                player: Player,
                windowIndex: Int
            ): MediaDescriptionCompat {
                return getMediaDescription()
            }
        })
        mediaSessionConnector.setPlayer(exoPlayer, null)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_INIT -> {
                Timber.i("EXOSERVICES:  ACTION INIT")
                initStreamingMusic()
            }
            ACTION_PLAY -> {
                Timber.i("EXOSERVICES:  ACTION PLAY")
                playRadio()
            }
            ACTION_PAUSE -> {
                Timber.i("EXOSERVICES:  ACTION PAUSE")
                pauseRadio()
            }
            ACTION_MUTE -> {
                Timber.i("EXOSERVICES:  ACTION MUTE")
                muteRadio()
            }
            ACTION_UNMUTE -> {
                Timber.i("EXOSERVICES:  ACTION UNMUTE")
                unMuteRadio() }

            else -> {
                Timber.i("EXOSERVICES:  ACTION ELSE")
                initStreamingMusic()
            }
        }

        return START_STICKY
    }

    private fun playRadio() {
        exoPlayer.playWhenReady = true
    }

    private fun pauseRadio() {
        exoPlayer.playWhenReady = false
    }

    private fun muteRadio() {
        exoPlayer.volume = 0f
    }

    private fun unMuteRadio() {
        exoPlayer.volume = 1f
    }


    override fun onDestroy() {
        super.onDestroy()
        Preferences(this).setPlaying(false)
        mediaSession.release()
        mediaSessionConnector.setPlayer(null, null)
        playerNotificationManager.setPlayer(null)
        exoPlayer.release()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val ACTION_INIT: String = "com.codelabs.unikomradio.action.INITIALIZE"
        const val ACTION_PLAY: String = "com.codelabs.unikomradio.action.PLAY"
        const val ACTION_PAUSE: String = "com.codelabs.unikomradio.action.PAUSE"
        const val ACTION_MUTE: String = "com.codelabs.unikomradio.action.MUTE"
        const val ACTION_UNMUTE: String = "com.codelabs.unikomradio.action.UNMUTE"
    }

    inner class ExoBinder : Binder(){
        fun getService(): ExoServices = this@ExoServices
    }

    fun getMediaDescription(): MediaDescriptionCompat {
        val extras = Bundle()
        val bitmap = BitmapFactory.decodeResource(this@ExoServices.resources, R.drawable.thumbnailradio)
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, bitmap)
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON, bitmap)
        return MediaDescriptionCompat.Builder()
            .setMediaId("21")
            .setIconBitmap(bitmap)
            .setTitle("unikomradio")
            .setDescription("unikomradiodescription")
            .setExtras(extras)
            .build()
    }
}
