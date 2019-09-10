package com.codelabs.unikomradio.utilities.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import com.codelabs.unikomradio.BuildConfig
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.mvvm.ExoPlayer
import com.codelabs.unikomradio.utilities.*
import com.codelabs.unikomradio.utilities.helper.Preferences
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Util
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class ExoPlayerServices : Service() {
    lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var playerNotificationManager: PlayerNotificationManager
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector
    var state = true

    val db = FirebaseFirestore.getInstance()
    private val docRef = db.collection(ON_RADIO_PLAYING).document(onradioplayingdocument)


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

        val dateSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, packageName),
            bandwidthMeter as TransferListener
        )

        val mediaSource = ExtractorMediaSource(
            Uri.parse(BuildConfig.BASE_URL_UNIKOM_RADIO_STREAMING),
            dateSourceFactory,
            extractorsFactory,
            Handler(),
            ExtractorMediaSource.EventListener {
                it.printStackTrace()
            }
        )

        exoPlayer = ExoPlayer(this).exoPlayer
        docRef.set(hashMapOf(ISPLAYING to exoPlayer.playWhenReady))


//        exoPlayer.addListener(object : Player.EventListener {
//            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
//                docRef.addSnapshotListener { snapshot, exception ->
//                    if (exception != null) {
//                        exception.printStackTrace()
//                        return@addSnapshotListener
//                    }
//
//                    if (snapshot != null) {
//                        docRef.set(hashMapOf(ISPLAYING to playWhenReady))
//
//                    } else {
//                        exception?.printStackTrace()
//                        Timber.w("Current data null")
//                    }
//                }
//            }
//        })

        exoPlayer.prepare(mediaSource)
        exoPlayer.audioAttributes = audioAttributes
        exoPlayer.volume = 1f

        exoPlayer.addListener(object : Player.EventListener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
                super.onPlaybackParametersChanged(playbackParameters)
                Timber.i("anjingbabi: $playbackParameters")
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                Timber.i("anjingkuda: $playWhenReady")
                docRef.set(hashMapOf(ISPLAYING to playWhenReady))

            }
        })

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
                    return BitmapFactory.decodeResource(
                        this@ExoPlayerServices.resources,
                        R.drawable.thumbnailradio
                    )
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


        playerNotificationManager.setOngoing(true)
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
                unMuteRadio()
            }

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
        playerNotificationManager.setOngoing(false)
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


    fun getMediaDescription(): MediaDescriptionCompat {
        val extras = Bundle()
        val bitmap =
            BitmapFactory.decodeResource(
                this@ExoPlayerServices.resources,
                R.drawable.thumbnailradio
            )
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
