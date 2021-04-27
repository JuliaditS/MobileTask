package ac.id.unikom.codelabs.radio.mvvm.main

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import ac.id.unikom.codelabs.radio.R
import ac.id.unikom.codelabs.radio.databinding.ActivityMainBinding
import ac.id.unikom.codelabs.radio.mvvm.StateListener
import ac.id.unikom.codelabs.radio.mvvm.crew.CrewFragment
import ac.id.unikom.codelabs.radio.mvvm.home.HomeFragment
import ac.id.unikom.codelabs.radio.mvvm.news.NewsFragment
import ac.id.unikom.codelabs.radio.mvvm.programs.ProgramFragment
import ac.id.unikom.codelabs.radio.mvvm.programs.specifyToday
import ac.id.unikom.codelabs.radio.mvvm.streaming.StreamingFragment
import ac.id.unikom.codelabs.radio.mvvm.streaming.streaming_topcharts.StreamingTopchartsActivity
import ac.id.unikom.codelabs.radio.utilities.base.BaseActivity
import ac.id.unikom.codelabs.radio.utilities.helper.OnSeeAllClickedListener
import ac.id.unikom.codelabs.radio.utilities.helper.Preferences
import ac.id.unikom.codelabs.radio.utilities.helper.ThemeMode
import ac.id.unikom.codelabs.radio.utilities.services.ExoPlayerServices
import androidx.activity.viewModels
import com.airbnb.paris.extensions.style
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

            todayProgram.observe(this@MainActivity, Observer {
                if (it != null) {
                    if (specifyToday.isToday(it.heldDay)) {
                        mBinding.mainPlayradioPlayImageThumbnail.setImageURI(it.imageUrl)
                    } else {
                        val dummyDay: String = it.heldDay.replace(" ", "")
                        val dummyDayString = dummyDay.split("-")
                        if (specifyToday.isToday(dummyDayString[0], dummyDayString[1])) {

                            if ((it.startAt.toDouble() <= specifyToday.getHourSpecify()) && (it.endAt.toDouble() >= specifyToday.getHourSpecify())) {
                                mBinding.mainPlayradioPlayImageThumbnail.setImageURI(it.imageUrl)
                            }
                        }
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
