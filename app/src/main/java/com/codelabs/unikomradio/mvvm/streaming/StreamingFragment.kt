package com.codelabs.unikomradio.mvvm.streaming


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.databinding.StreamingBinding
import com.codelabs.unikomradio.utilities.base.BaseFragment
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory


class StreamingFragment : BaseFragment<StreamingViewModel,StreamingBinding>(R.layout.streaming) {


    private lateinit var exoPlayer: ExoPlayer
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.streaming, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initializePlayer()
    }

    private fun initializePlayer() {
//        val player: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
//            DefaultRenderersFactory(context),
//            DefaultTrackSelector(), DefaultLoadControl()
//        )
//
//        streaming_exo_playerview.player = player
//
//        player.playWhenReady = true
//        player.seekTo(0, 0)
    }

    fun play(url: String) {
        //1
        val userAgent = "http://hits.unikom.ac.id:9996/;listen.pls?sid=1"
        //2
        val mediaSource = ExtractorMediaSource
            .Factory(DefaultDataSourceFactory(context, userAgent))
            .setExtractorsFactory(DefaultExtractorsFactory())
            .createMediaSource(Uri.parse(url))
        //3
        exoPlayer.prepare(mediaSource)
        //4
        exoPlayer.playWhenReady = true
    }

    override fun onCreateObserver(viewModel: StreamingViewModel) {
    }

    override fun setContentData() {
    }

    override fun setMessageType(): String {
        return ""
    }

    override fun afterInflateView() {
    }
}

