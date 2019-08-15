package com.codelabs.unikomradio.mvvm.streaming


import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.databinding.StreamingBinding
import com.codelabs.unikomradio.utilities.base.BaseFragment


class StreamingFragment : BaseFragment<StreamingViewModel, StreamingBinding>(R.layout.streaming),
    StreamingUserActionListener {

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
    }

    override fun setMessageType(): String {
        return ""
    }

    override fun afterInflateView() {
        mParentVM = viewModel
        mBinding.mListener = this
        mBinding.mViewModel = viewModel
    }

    override fun onPlayMusicClick() {
        if (viewModel.mediaPlayer?.isPlaying != null) {
            if (viewModel.mediaPlayer?.isPlaying == true) {
                viewModel.stopStreaming()
            } else {
                viewModel.playStreaming()
            }
        } else {
            viewModel.playStreaming()
        }
    }

    override fun onTopChartClick() {
    }

    override fun onMuteClick() {
        viewModel.muteStreaming(context)
        if (viewModel.isMute.value == true){
            mBinding.streamingSoundActive.setImageResource(R.mipmap.volumemute)
        } else {
            mBinding.streamingSoundActive.setImageResource(R.mipmap.volume)
        }
    }
}

