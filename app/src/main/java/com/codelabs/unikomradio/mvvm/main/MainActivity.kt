package com.codelabs.unikomradio.mvvm.main

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.airbnb.paris.extensions.style
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.databinding.ActivityMainBinding
import com.codelabs.unikomradio.mvvm.StateListener
import com.codelabs.unikomradio.mvvm.crew.CrewFragment
import com.codelabs.unikomradio.mvvm.home.HomeFragment
import com.codelabs.unikomradio.mvvm.news.NewsFragment
import com.codelabs.unikomradio.mvvm.programs.ProgramFragment
import com.codelabs.unikomradio.mvvm.streaming.StreamingFragment
import com.codelabs.unikomradio.mvvm.streaming.streaming_topcharts.StreamingTopchartsActivity
import com.codelabs.unikomradio.utilities.base.BaseActivity
import com.codelabs.unikomradio.utilities.helper.OnSeeAllClickedListener
import com.codelabs.unikomradio.utilities.helper.Preferences
import com.codelabs.unikomradio.utilities.helper.ThemeMode
import com.codelabs.unikomradio.utilities.services.ExoPlayerServices
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main),
    MainUserActionListener,
    OnSeeAllClickedListener, StateListener {

    private var isLightMode = false
    var playingState: Boolean? = false

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeMode().setThemeModeOn(this)

        mParentVM = viewModel
        mBinding.mViewModel = viewModel
        mBinding.mListener = this
        supportActionBar?.elevation = 0f

        //For initialize firstload
        loadFragment(HomeFragment())
        supportActionBar?.title = getString(R.string.title_home)

        mBinding.mainBottomnavigationview.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )

        isLightMode = Preferences(this).isLightMode()

        if (!isLightMode) {
            mBinding.mainBottomnavigationview.itemBackgroundResource = R.color.colorSecondary
            mBinding.mainBottomnavigationview.background =
                ColorDrawable(getColor(R.color.colorAccent))
            mBinding.mainPlayradioTitle.background = ColorDrawable(getColor(R.color.colorSecondary))
            mBinding.mainPlayradioDescription.background =
                ColorDrawable(getColor(R.color.colorSecondary))
        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.main_home_item -> {
                    fragment = HomeFragment()
                    supportActionBar?.title = getString(R.string.title_home)
                    mBinding.mainPlayradioLayout.visibility = View.VISIBLE
                }

                R.id.main_programs_item -> {
                    fragment = ProgramFragment()
                    supportActionBar?.title = getString(R.string.title_program)
                    mBinding.mainPlayradioLayout.visibility = View.VISIBLE
                }

                R.id.main_streaming_play_item -> {
                    fragment = StreamingFragment()
                    supportActionBar?.title = getString(R.string.title_radio)
                    mBinding.mainPlayradioLayout.visibility = View.GONE
                }

                R.id.main_crew_item -> {
                    fragment = CrewFragment()
                    supportActionBar?.title = getString(R.string.title_crew)
                    mBinding.mainPlayradioLayout.visibility = View.VISIBLE
                }

                R.id.main_news_item -> {
                    supportActionBar?.title = getString(R.string.title_news)
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
                    if (isLightMode) {
                        mBinding.mainPlayradioPlayButton.setImageResource(R.mipmap.pause_light)
                    }
                } else {
                    mBinding.mainPlayradioPlayButton.setImageResource(R.mipmap.playbutton)
                    if (isLightMode) {
                        mBinding.mainPlayradioPlayButton.setImageResource(R.drawable.icon_play_light)
                    }
                }
            })
        }
    }

    @SuppressLint("ResourceType")
    override fun setContentData() {
        if (Preferences(this).isLightMode()) {
            mBinding.mainPlayradioLayout.setBackgroundColor(getColor(android.R.color.white))
            mBinding.mainPlayradioDescription.setTextColor(getColor(R.color.colorAccentLight))
            mBinding.mainPlayradioTitle.setTextColor(getColor(android.R.color.black))
            mBinding.mainBottomnavigationview.style(R.style.BottomNavigationView)
            mBinding.mainBottomnavigationview.itemIconTintList =
                getColorStateList(R.drawable.nav_item_color_state_light)
            mBinding.mainBottomnavigationview.itemTextAppearanceActive =
                R.style.BottomNavigationView_Light_Active
        } else {
            mBinding.mainPlayradioTitle.setTextColor(getColor(android.R.color.white))
            mBinding.mainBottomnavigationview.style(R.style.BottomNavigationView)
            mBinding.mainBottomnavigationview.itemIconTintList =
                getColorStateList(R.drawable.nav_item_color_state)
            mBinding.mainBottomnavigationview.itemTextColor =
                getColorStateList(R.drawable.nav_item_color_state)
            mBinding.mainBottomnavigationview.itemTextAppearanceActive =
                R.style.BottomNavigationView_Dark_Active
            mBinding.mainPlayradioLayout.background =
                ColorDrawable(getColor(R.color.colorSecondary))
        }
    }


    override fun onPlayRadio() {
        val intent = Intent(this, ExoPlayerServices::class.java)
        playingState = Preferences(this).isPlaying()

        if (!isMyServiceRunning(ExoPlayerServices::class.java)) {
            intent.action = ExoPlayerServices.ACTION_INIT
            this.startService(intent)
            viewModel.playStreaming()
            intent.action = ExoPlayerServices.ACTION_PLAY
        } else {
            if (viewModel.isPlaying.value == true) {
                viewModel.stopStreaming()
                intent.action = ExoPlayerServices.ACTION_PAUSE
            } else {
                viewModel.playStreaming()
                intent.action = ExoPlayerServices.ACTION_PLAY
            }
        }

        this.startService(intent)

        playingState = viewModel.isPlaying.value
        playingState?.let { Preferences(this).setPlaying(it) }
    }

    override fun setStatePlayingRadio(state: Boolean?) {
        playingState = state
        playingState?.let { viewModel.stateStreaming(it) }
    }

    override fun isPlayingRadio(): Boolean? {
        return playingState
    }

    @Suppress("DEPRECATION")
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

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        if (Preferences(this).isLightMode()) {
            theme.applyStyle(R.style.AppTheme_Light, true)
        } else {
            theme.applyStyle(R.style.AppTheme_Dark, true)
        }
        return super.getTheme()
    }
}
