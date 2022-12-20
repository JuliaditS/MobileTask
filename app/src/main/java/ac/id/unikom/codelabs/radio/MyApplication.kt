package ac.id.unikom.codelabs.radio

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import ac.id.unikom.codelabs.radio.utilities.helper.Preferences
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.firebase.BuildConfig
import timber.log.Timber


class MyApplication : Application() {

    private var isNightModeEnabled = false

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

        Fresco.initialize(this)

        this.isNightModeEnabled = Preferences(this).isLightMode()

    }

    companion object {
        lateinit var instance: MyApplication

        fun getContext(): Context = instance.applicationContext
    }


}

