package ac.id.unikom.codelabs.radio.mvvm.home

import ac.id.unikom.codelabs.radio.MyApplication
import ac.id.unikom.codelabs.radio.NoInternet
import ac.id.unikom.codelabs.radio.R
import ac.id.unikom.codelabs.radio.data.model.Program
import ac.id.unikom.codelabs.radio.databinding.HomeBinding
import ac.id.unikom.codelabs.radio.mvvm.news.NewsAdapter
import ac.id.unikom.codelabs.radio.mvvm.programs.specifyToday
import ac.id.unikom.codelabs.radio.mvvm.settings.SettingsActivity
import ac.id.unikom.codelabs.radio.mvvm.streaming.streaming_topcharts.StreamingTopchartsAdapter
import ac.id.unikom.codelabs.radio.utilities.base.BaseFragment
import ac.id.unikom.codelabs.radio.utilities.helper.OnSeeAllClickedListener
import ac.id.unikom.codelabs.radio.utilities.helper.Preferences
import ac.id.unikom.codelabs.radio.utilities.helper.ThemeMode
import ac.id.unikom.codelabs.radio.utilities.helper.recyclerviewhelper.itemdecoration.CirclePagerIndicatorDecoration
import ac.id.unikom.codelabs.radio.utilities.helper.recyclerviewhelper.itemdecoration.RecyclerviewItemDecoration
import ac.id.unikom.codelabs.radio.utilities.helper.recyclerviewhelper.itemdecoration.RecyclerviewItemGridTwoHorizontalDecoration
import ac.id.unikom.codelabs.radio.utilities.helper.recyclerviewhelper.snaphelper.SnapHelper
import android.content.Intent
import android.media.MediaPlayer
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : BaseFragment<HomeViewModel, HomeBinding>(R.layout.home),
    HomeUserActionListener {
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var programAdapter: ProgramTodayAdapter
    private lateinit var topChartAdapter: StreamingTopchartsAdapter
    private lateinit var crewAdapter: CrewHomeAdapter
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var todayProgramImageUrl: String

    private lateinit var onSeeAllClickedListener: OnSeeAllClickedListener


    private var mediaPlayer: MediaPlayer? = null

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory()
    }

    override fun afterInflateView() {
        setHasOptionsMenu(true)
        ThemeMode().setThemeModeOn(requireContext())
        mParentVM = viewModel
        mBinding.mViewModel = viewModel
        mediaPlayer = (requireActivity().application as MyApplication).mMediaPlayer

    }

    override fun setContentData() {
        bannerAdapter = BannerAdapter()
        mBinding.mListener = this

        initAllRecylerView()

        activity?.let {
            require(it is OnSeeAllClickedListener) { "Activity should implement OnSeeAllClickListener" }
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
        viewModel.initContext(context)
        viewModel.apply {
            banner.observe(this@HomeFragment, Observer {
                if (it.isNotEmpty()) {
                    bannerAdapter.submitList(it)
                } else {
                    startActivity(Intent(activity, NoInternet::class.java))
                }
            })
        }

        viewModel.apply {
            programs.observe(this@HomeFragment, Observer {
                val programTodayList = ArrayList<Program>()
                for (d in it) {
                    if (d.heldDay.contains('-')) {
                        val dummyDay: String = d.heldDay.replace(" ", "")
                        val dummyDayString = dummyDay.split("-")
                        if (specifyToday.isToday(dummyDayString[0], dummyDayString[1])) {
                            programTodayList.add(d)
                        }
                    } else if (d.heldDay.contains(',')) {
                        val dumm = d.heldDay.split(',')
                        for ((i, _) in dumm.withIndex()) {
                            if (specifyToday.isToday(dumm[i])) {
                                programTodayList.add(d)
                            }
                        }
                    } else {
                        if (specifyToday.isToday(d.heldDay)) {
                            programTodayList.add(d)
                        }
                    }
                }
                if (it.isNotEmpty()) {
                    programAdapter.submitList(programTodayList)
                } else {
                    startActivity(Intent(activity, NoInternet::class.java))
                }
            })
        }

        viewModel.apply {
            topcharts.observe(this@HomeFragment, Observer {
                if (it.isNotEmpty()) {
                    topChartAdapter.submitList(
                        it.sortedBy { topChart -> topChart.currentRank }.take(3)
                    )
                } else {
                    startActivity(Intent(activity, NoInternet::class.java))
                }
            })
        }

        viewModel.apply {
            crews.observe(this@HomeFragment, Observer {
                if (it.isNotEmpty()) {
                    crewAdapter.submitList(it)
                } else {
                    startActivity(Intent(activity, NoInternet::class.java))
                }
            })
        }

        viewModel.apply {
            news.observe(this@HomeFragment, Observer {
                if (it.isNotEmpty()) {
                    newsAdapter.submitList(it)
                } else {
                    startActivity(Intent(activity, NoInternet::class.java))
                }
            })
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (Preferences(requireContext()).isLightMode()) {
            inflater.inflate(R.menu.menu_main_light, menu)
        } else {
            inflater.inflate(R.menu.menu_home, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_menu_settings) {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
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
