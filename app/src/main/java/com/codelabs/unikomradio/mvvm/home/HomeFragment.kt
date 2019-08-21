package com.codelabs.unikomradio.mvvm.home

import android.media.MediaPlayer
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.unikomradio.MyApplication
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.databinding.HomeBinding
import com.codelabs.unikomradio.utilities.base.BaseFragment
import com.codelabs.unikomradio.utilities.helper.Event

class HomeFragment : BaseFragment<HomeViewModel, HomeBinding>(R.layout.home), HomeUserActionListener {


    private lateinit var bannerAdapter: BannerAdapter
    private var mediaPlayer: MediaPlayer? = null


    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory()
    }

    override fun afterInflateView() {
        mParentVM = viewModel
        mBinding.mViewModel = viewModel
        mediaPlayer = (requireActivity().application as MyApplication).mMediaPlayer
    }

    override fun setContentData() {
        bannerAdapter = BannerAdapter()
        mBinding.mListener = this
        mBinding.programBannerRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        mBinding.programBannerRecyclerview.adapter = bannerAdapter

        if (mediaPlayer?.isPlaying != null) {
            viewModel.stateStreaming(mediaPlayer!!.isPlaying)
        }
    }

    override fun onPlayRadio() {
    }

    override fun onCreateObserver(viewModel: HomeViewModel) {
        viewModel.apply {
            banner.observe(this@HomeFragment, Observer {
                if (it.isNotEmpty()) {
                    bannerAdapter.submitList(it)
                } else {
                    viewModel.showMessage.value = Event("program not found")
                }
            })
        }
    }

    override fun setMessageType(): String {
        return MESSAGE_TYPE_TOAST
    }


}
