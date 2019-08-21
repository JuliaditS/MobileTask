package com.codelabs.unikomradio.mvvm.home

import android.media.MediaPlayer
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.unikomradio.MyApplication
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.databinding.HomeBinding
import com.codelabs.unikomradio.mvvm.news.NewsAdapter
import com.codelabs.unikomradio.mvvm.programs.ProgramTodayAdapter
import com.codelabs.unikomradio.mvvm.streaming.streaming_topcharts.StreamingTopchartsAdapter
import com.codelabs.unikomradio.utilities.base.BaseFragment
import com.codelabs.unikomradio.utilities.helper.Event
import timber.log.Timber


class HomeFragment : BaseFragment<HomeViewModel, HomeBinding>(R.layout.home), HomeUserActionListener {


    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var programAdapter: ProgramTodayAdapter
    private lateinit var topChartAdapter: StreamingTopchartsAdapter
    private lateinit var crewAdapter: CrewHomeAdapter
    private lateinit var newsAdapter: NewsAdapter

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

        mBinding.homeBannerRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        mBinding.homeBannerRecyclerview.adapter = bannerAdapter


        programAdapter = ProgramTodayAdapter()
        mBinding.homeBroadcastTodayRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        mBinding.homeBroadcastTodayRecyclerview.adapter = programAdapter

        topChartAdapter = StreamingTopchartsAdapter()
        mBinding.homeTopchartRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mBinding.homeTopchartRecyclerview.adapter = topChartAdapter

        crewAdapter = CrewHomeAdapter()
        mBinding.homeOnairtroopsRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        mBinding.homeOnairtroopsRecyclerview.adapter = crewAdapter

        newsAdapter = NewsAdapter()
        mBinding.homeNewsRecyclerview.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        mBinding.homeNewsRecyclerview.adapter = newsAdapter

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

        viewModel.apply {
            programs.observe(this@HomeFragment, Observer {
                if (it.isNotEmpty()) {
                    programAdapter.submitList(it)
                } else {
                    viewModel.showMessage.value = Event("program not found")
                }
            })
        }

        viewModel.apply {
            topcharts.observe(this@HomeFragment, Observer {
                if (it.isNotEmpty()) {
                    topChartAdapter.submitList(it)
                } else {
                    viewModel.showMessage.value = Event("program not found")
                }
            })
        }

        viewModel.apply {
            crews.observe(this@HomeFragment, Observer {
                if (it.isNotEmpty()) {
                    crewAdapter.submitList(it)
                } else {
                    viewModel.showMessage.value = Event("crew not found")
                }
            })
        }

        viewModel.apply {
            news.observe(this@HomeFragment, Observer {
                if (it.isNotEmpty()) {
                    Timber.i("ada datanya goblok " + it.toString())
                    newsAdapter.submitList(it)
                } else {
                    viewModel.showMessage.value = Event("news not found")
                }
            })
        }
    }

    override fun setMessageType(): String {
        return MESSAGE_TYPE_TOAST
    }


}
