package com.codelabs.unikomradio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.andremion.music.MusicCoverView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mCoverView: MusicCoverView = findViewById(R.id.cover)


    }
}
