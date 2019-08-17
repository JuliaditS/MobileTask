package com.codelabs.unikomradio.mvvm.streaming

import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.media.MediaPlayer
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.codelabs.unikomradio.MyApplication
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.databinding.StreamingBinding
import com.codelabs.unikomradio.mvvm.streaming.streaming_topcharts.StreamingTopchartsActivity
import com.codelabs.unikomradio.utilities.base.BaseFragment
import com.codelabs.unikomradio.utilities.services.MediaPlayerServices


class StreamingFragment : BaseFragment<StreamingViewModel, StreamingBinding>(R.layout.streaming),
    StreamingUserActionListener {

    private var mediaPlayer: MediaPlayer? = null
    private val viewModel: StreamingViewModel by viewModels {
        StreamingViewModelFactory()
    }

    override fun onCreateObserver(viewModel: StreamingViewModel) {
        viewModel.apply {
            isPlaying.observe(viewLifecycleOwner, Observer<Boolean> {
                if (it) {
                    mBinding.streamingPlayStreaming.setImageResource(R.mipmap.pause)
                } else {
                    mBinding.streamingPlayStreaming.setImageResource(R.mipmap.playbutton)
                }
            })
        }
    }

    override fun setContentData() {
        activity?.actionBar?.title = "Live Streaming Audio"
    }

    override fun setMessageType(): String {
        return ""
    }

    override fun afterInflateView() {
        mParentVM = viewModel
        mBinding.mListener = this
        mBinding.mViewModel = viewModel
        mediaPlayer = (requireActivity().application as MyApplication).mMediaPlayer
        mBinding.streamingSongThumbnail.setImageResource(R.drawable.thumbnailradio_rounder_corners)
    }

    override fun onPlayMusicClick() {
        val intent = Intent(activity, MediaPlayerServices::class.java)
        if (!isMyServiceRunning(MediaPlayerServices::class.java)) {
            intent.action = MediaPlayerServices.ACTION_PLAY
            activity?.startService(intent)
            viewModel.playStreaming()
        } else {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
                viewModel.stopStreaming()
            } else {
                mediaPlayer?.start()
                viewModel.playStreaming()
            }
        }
    }

    override fun onTopChartClick() {
        startActivity(Intent(requireContext(),StreamingTopchartsActivity::class.java))
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {

        val manager = activity?.getSystemService(ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onMuteClick() {
        viewModel.muteStreaming()
        if (viewModel.isMute.value == true) {
            mediaPlayer?.setVolume(0f, 0f)
            mBinding.streamingSoundActive.setImageResource(R.mipmap.volumemute)
        } else {
            mediaPlayer?.setVolume(1f, 1f)
            mBinding.streamingSoundActive.setImageResource(R.mipmap.volume)
        }
    }
}

