package com.codelabs.unikomradio.mvvm.main

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.codelabs.unikomradio.MyApplication
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.databinding.ActivityMainBinding
import com.codelabs.unikomradio.mvvm.crew.CrewFragment
import com.codelabs.unikomradio.mvvm.home.HomeFragment
import com.codelabs.unikomradio.mvvm.news.NewsFragment
import com.codelabs.unikomradio.mvvm.programs.ProgramFragment
import com.codelabs.unikomradio.mvvm.streaming.StreamingFragment
import com.codelabs.unikomradio.mvvm.streaming.streaming_topcharts.StreamingTopchartsActivity
import com.codelabs.unikomradio.utilities.base.BaseActivity
import com.codelabs.unikomradio.utilities.helper.OnSeeAllClickedListener
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.EventListener
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
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main), MainUserActionListener,
    OnSeeAllClickedListener {


    private var mediaPlayer: MediaPlayer? = null
    private lateinit var exoPlayer: SimpleExoPlayer


    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mParentVM = viewModel
        mBinding.mViewModel = viewModel
        mBinding.mListener = this
        supportActionBar?.elevation = 0f
        //init tampilan awal
        loadFragment(HomeFragment())
        supportActionBar?.title = "Discover Music"
//        mediaPlayer = (this.application as MyApplication).mMediaPlayer
        exoPlayer = (this.application as MyApplication).exoPlayer
        mBinding.mainBottomnavigationview.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


//        initRadioStreaming()
    }

//    private fun initRadioStreaming() {
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
//            ExtractorMediaSource.EventListener {
//                it.printStackTrace()
//            }
//        )    // replace Uri with your song url
//
//        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, DefaultTrackSelector(trackSelectionFactory))
//        exoPlayer.prepare(mediaSource)
//        exoPlayer.audioAttributes = audioAttributes
//        exoPlayer.volume = 1f
//    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.main_home_item -> {
                fragment = HomeFragment()
                supportActionBar?.title = "Discover Music"
                mBinding.mainPlayradioLayout.visibility = View.VISIBLE
            }

            R.id.main_programs_item -> {
                fragment = ProgramFragment()
                supportActionBar?.title = "Programs Music"
                mBinding.mainPlayradioLayout.visibility = View.VISIBLE
            }

            R.id.main_streaming_play_item -> {
                fragment = StreamingFragment()
                supportActionBar?.title = "Live Streaming Radio"
                mBinding.mainPlayradioLayout.visibility = View.GONE
            }

            R.id.main_crew_item -> {
                fragment = CrewFragment()
                supportActionBar?.title = "On Air Tropps"
                mBinding.mainPlayradioLayout.visibility = View.VISIBLE
            }

            R.id.main_news_item -> {
                supportActionBar?.title = "News"
                fragment = NewsFragment()
                mBinding.mainPlayradioLayout.visibility = View.VISIBLE
            }
        }

        return@OnNavigationItemSelectedListener loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun onCreateObserver(viewModel: MainViewModel) {
        viewModel.apply {
            isPlaying.observe(this@MainActivity, Observer<Boolean> {
                if (it) {
                    mBinding.mainPlayradioPlayButton.setImageResource(R.mipmap.pause)
                } else {
                    mBinding.mainPlayradioPlayButton.setImageResource(R.mipmap.playbutton)
                }
            })
        }
    }

    override fun setContentData() {
        try {
            if (mediaPlayer?.isPlaying != null) {
                viewModel.stateStreaming(mediaPlayer!!.isPlaying)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

//    With mediaplayer
//    override fun onPlayRadio() {
//        val intent = Intent(this, MediaPlayerServices::class.java)
//        if (!isMyServiceRunning(MediaPlayerServices::class.java)) {
//            intent.action = MediaPlayerServices.ACTION_PLAY
//            this.startService(intent)
//            viewModel.playStreaming()
//
//        } else {
//            Timber.i("ternyata " + mediaPlayer?.isPlaying.toString())
//            if (mediaPlayer?.isPlaying == true) {
//                mediaPlayer?.pause()
//                viewModel.stopStreaming()
//
//            } else {
//                mediaPlayer?.start()
//                viewModel.playStreaming()
//            }
//        }
//    }

    override fun onPlayRadio() {
        try {
            if (exoPlayer.playWhenReady) {
                viewModel.stopStreaming()
                exoPlayer.playWhenReady = false
            } else {
                viewModel.playStreaming()
                exoPlayer.playWhenReady = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {

        val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onBroadcastSeeAllClicked() {
        mBinding.mainBottomnavigationview.selectedItemId = R.id.main_programs_item
    }

    override fun onTopchartSeeAllClicked() {
        startActivity(Intent(this, StreamingTopchartsActivity::class.java))
    }

    override fun onAirtroppsSeeAllClicked() {
        mBinding.mainBottomnavigationview.selectedItemId = R.id.main_crew_item
    }

    override fun onNewsSeeAllClicked() {
        mBinding.mainBottomnavigationview.selectedItemId = R.id.main_news_item
    }

    override fun setMessageType(): String {
        return MESSAGE_TYPE_TOAST
    }

    override fun onPlayRadioLayoutClick() {
        mBinding.mainBottomnavigationview.selectedItemId = R.id.main_streaming_play_item
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}
