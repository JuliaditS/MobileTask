package com.codelabs.unikomradio.mvvm.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.mvvm.crew.CrewFragment
import com.codelabs.unikomradio.mvvm.home.HomeFragment
import com.codelabs.unikomradio.mvvm.news.NewsFragment
import com.codelabs.unikomradio.mvvm.programs.ProgramFragment
import com.codelabs.unikomradio.mvvm.streaming.StreamingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.elevation = 0f

        //init tampilan awal
        loadFragment(HomeFragment())
        supportActionBar?.title = "Discover Music"

        main_bottomnavigationview.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.main_home_item -> {
                fragment = HomeFragment()
                supportActionBar?.title = "Discover Music"
            }

            R.id.main_programs_item -> {
                fragment = ProgramFragment()
                supportActionBar?.title = "Programs Music"

            }

            R.id.main_streaming_play_item -> {
                fragment = StreamingFragment()
                supportActionBar?.title = "Live Streaming Radio"
            }

            R.id.main_crew_item -> {
                fragment = CrewFragment()
                supportActionBar?.title = "On Air Tropps"
            }

            R.id.main_news_item -> {
                supportActionBar?.title = "News"
                fragment = NewsFragment()
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

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}
