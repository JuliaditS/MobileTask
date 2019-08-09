package com.codelabs.unikomradio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andremion.music.MusicCoverView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        supportActionBar?.title = "Discover Music"
    }
}
