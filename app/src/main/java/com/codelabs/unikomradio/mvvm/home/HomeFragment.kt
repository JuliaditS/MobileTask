package com.codelabs.unikomradio.mvvm.home

import android.graphics.Point
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
import com.codelabs.unikomradio.utilities.helper.OnSeeAllClickedListener
import com.codelabs.unikomradio.utilities.helper.recyclerviewhelper.itemdecoration.CirclePagerIndicatorDecoration
import com.codelabs.unikomradio.utilities.helper.recyclerviewhelper.itemdecoration.RecyclerviewItemDecoration
import com.codelabs.unikomradio.utilities.helper.recyclerviewhelper.itemdecoration.RecyclerviewItemGridTwoHorizontalDecoration
import com.codelabs.unikomradio.utilities.helper.recyclerviewhelper.snaphelper.SnapHelper
import timber.log.Timber


class HomeFragment : BaseFragment<HomeViewModel, HomeBinding>(R.layout.home), HomeUserActionListener {
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var programAdapter: ProgramTodayAdapter
    private lateinit var topChartAdapter: StreamingTopchartsAdapter
    private lateinit var crewAdapter: CrewHomeAdapter
    private lateinit var newsAdapter: NewsAdapter

    private lateinit var onSeeAllClickedListener: OnSeeAllClickedListener

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

        initAllRecylerView()

        activity?.let {
            if (it !is OnSeeAllClickedListener) throw IllegalArgumentException("Activity should implement OnSeeAllClickListener")

            onSeeAllClickedListener = activity as OnSeeAllClickedListener
        }

        if (mediaPlayer?.isPlaying != null) {
            viewModel.stateStreaming(mediaPlayer!!.isPlaying)
        }
    }

    override fun onPlayRadio() {
    }

    private fun initAllRecylerView() {
        val itemDecoration =
            RecyclerviewItemDecoration(
                requireContext(),
                16f
            )
        val itemDecorationGrid =
            RecyclerviewItemGridTwoHorizontalDecoration(
                requireContext(),
                16f
            )
        setBannerView(itemDecoration)
        setProgramsView(itemDecoration)
        setTopchartsView(itemDecoration)
        setCrewsView(itemDecoration)
        setNewsView(itemDecorationGrid)
    }

    private fun setBannerView(itemDecoration: RecyclerviewItemDecoration) {
        mBinding.homeBannerRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        mBinding.homeBannerRecyclerview.adapter = bannerAdapter
        SnapHelper().attachToRecyclerView(mBinding.homeBannerRecyclerview)

        val itemDecoration2 =
            CirclePagerIndicatorDecoration(
                requireContext(),
                12f
            )
        itemDecoration2.firstItemSpacing(16f)
        mBinding.homeBannerRecyclerview.addItemDecoration(itemDecoration2)
    }

    private fun setProgramsView(itemDecoration: RecyclerviewItemDecoration) {
        programAdapter = ProgramTodayAdapter()
        mBinding.homeBroadcastTodayRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        mBinding.homeBroadcastTodayRecyclerview.adapter = programAdapter
        mBinding.homeBroadcastTodayRecyclerview.addItemDecoration(itemDecoration)

    }

    private fun setTopchartsView(itemDecoration: RecyclerviewItemDecoration) {
        topChartAdapter = StreamingTopchartsAdapter()
        mBinding.homeTopchartRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mBinding.homeTopchartRecyclerview.adapter = topChartAdapter
    }

    private fun setCrewsView(itemDecoration: RecyclerviewItemDecoration) {
        crewAdapter = CrewHomeAdapter()
        mBinding.homeOnairtroopsRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        mBinding.homeOnairtroopsRecyclerview.adapter = crewAdapter
        mBinding.homeOnairtroopsRecyclerview.addItemDecoration(itemDecoration)
    }

    private fun setNewsView(itemDecoration: RecyclerviewItemGridTwoHorizontalDecoration) {
        newsAdapter = NewsAdapter()
        mBinding.homeNewsRecyclerview.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.HORIZONTAL, false)
        mBinding.homeNewsRecyclerview.adapter = newsAdapter

    }


    override fun onCreateObserver(viewModel: HomeViewModel) {
        viewModel.apply {
            banner.observe(this@HomeFragment, Observer {
                if (it.isNotEmpty()) {
                    bannerAdapter.submitList(it)
                } else {
                    viewModel.showMessage.value = Event("banner not found")
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
                    viewModel.showMessage.value = Event("topcharts not found")
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
                    newsAdapter.submitList(it)
                } else {
                    viewModel.showMessage.value = Event("news not found")
                }
            })
        }
    }

    override fun onClickBroadcastSeeAll() {
        onSeeAllClickedListener.onBroadcastSeeAllClicked()
    }

    override fun onClickTopchartSeeAll() {
        onSeeAllClickedListener.onTopchartSeeAllClicked()
    }

    override fun onAirTroopsSeeAll() {
        onSeeAllClickedListener.onAirtroppsSeeAllClicked()
    }

    override fun onNewsSeeAll() {
        onSeeAllClickedListener.onNewsSeeAllClicked()
    }

    override fun setMessageType(): String {
        return MESSAGE_TYPE_TOAST
    }

}
