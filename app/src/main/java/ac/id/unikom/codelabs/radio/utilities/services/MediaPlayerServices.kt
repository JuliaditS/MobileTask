package ac.id.unikom.codelabs.radio.utilities.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.IBinder
import android.widget.Toast

class MediaPlayerServices : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private val url = "http://hits.unikom.ac.id:9996/;listen.pls?sid=1"

    private var wifiLock: WifiManager.WifiLock? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this,"ANJING",Toast.LENGTH_SHORT).show()
//        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//        wifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock")
//
//        mediaPlayer = (application as MyApplication).mMediaPlayer
//        val audioAttributes = AudioAttributes.Builder()
//            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
//            .build()
//
//        try {
//            mediaPlayer?.apply {
//                isLooping = true
//                setAudioAttributes(audioAttributes)
//                setOnPreparedListener(this@MediaPlayerServices)
//                setDataSource(url)
//                prepareAsync()
//                setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
//                wifiLock?.acquire()
//            }
//        } catch (e: IllegalArgumentException){
//            e.printStackTrace()
//        } catch (e: IllegalStateException){
//            e.printStackTrace()
//        } catch (e: IOException){
//            e.printStackTrace()
//        }


        return START_STICKY
    }

    /** Called when MediaPlayer is ready */
    override fun onPrepared(mediaPlayer: MediaPlayer) {
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
        wifiLock?.release()
        mediaPlayer = null
    }

    companion object {
        const val ACTION_PLAY: String = "com.codelabs.unikomradio.action.PLAY"
    }
}