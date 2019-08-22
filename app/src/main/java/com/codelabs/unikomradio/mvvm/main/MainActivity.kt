package com.codelabs.unikomradio.mvvm.main

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
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
import com.codelabs.unikomradio.utilities.base.BaseActivity
import com.codelabs.unikomradio.utilities.services.MediaPlayerServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber


class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main), MainUserActionListener {
    private var mediaPlayer: MediaPlayer? = null


    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory()
    }

    override fun setMessageType(): String {
        return MESSAGE_TYPE_TOAST
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
        mediaPlayer = (this.application as MyApplication).mMediaPlayer

        mBinding.mainBottomnavigationview.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

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

    override fun onPlayRadio() {
        Timber.i("DIKLIK DI MAIN")
        val intent = Intent(this, MediaPlayerServices::class.java)
        if (!isMyServiceRunning(MediaPlayerServices::class.java)) {
            intent.action = MediaPlayerServices.ACTION_PLAY
            this.startService(intent)
            viewModel.playStreaming()
            Timber.i("DIKLIK DI MAIN dongo")

        } else {
            Timber.i("ternyata "+mediaPlayer?.isPlaying.toString())
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                viewModel.stopStreaming()
                Timber.i("DIKLIK DI babi")

            } else {
                mediaPlayer?.start()
                viewModel.playStreaming()
                Timber.i("DIKLIK DI bangsat")
            }
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

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}
