package com.codelabs.unikomradio.mvvm.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.mvvm.crew.CrewFragment
import com.codelabs.unikomradio.mvvm.home.HomeFragment
import com.codelabs.unikomradio.mvvm.programs.ProgramFragment
import com.codelabs.unikomradio.mvvm.streaming.StreamingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var prevMenuItem: MenuItem? = null

        main_bottomnavigationview.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        main_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null) {
                    prevMenuItem?.isChecked = false
                } else {
                    main_bottomnavigationview.menu.getItem(0).isChecked = false
                }

                val bottomIconActive = main_bottomnavigationview.menu.getItem(position)
                bottomIconActive.isChecked = true
                prevMenuItem = bottomIconActive
            }
        })
        setupViewPager(main_viewpager)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var status = 0
        when (item.itemId) {
            R.id.main_home_item -> {
                status = 0
            }
            R.id.main_streaming_play_item -> {
                status = 1
            }
            R.id.main_programs_item -> {
                status = 2

            }
            R.id.main_crew_item -> {
                status = 3
            }
        }
        main_viewpager.currentItem = status
        true
    }

    private fun setupViewPager(viewPager: ViewPager){
        val adapter = MainAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment())
        adapter.addFragment(StreamingFragment())
        adapter.addFragment(ProgramFragment())
        adapter.addFragment(CrewFragment())
        main_viewpager.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}
