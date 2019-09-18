package com.codelabs.unikomradio.mvvm.programs

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.unikomradio.MyApplication
import com.codelabs.unikomradio.NoInternet
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.data.model.Program
import com.codelabs.unikomradio.databinding.ProgramBinding
import com.codelabs.unikomradio.utilities.base.BaseFragment
import com.codelabs.unikomradio.utilities.helper.Event
import com.codelabs.unikomradio.utilities.helper.recyclerviewhelper.itemdecoration.RecyclerviewItemDecoration
import com.codelabs.unikomradio.utilities.helper.recyclerviewhelper.itemdecoration.RecyclerviewItemGridTwoHorizontalDecoration
import com.codelabs.unikomradio.utilities.services.MediaPlayerServices
import kotlinx.android.synthetic.main.no_result.view.*
import timber.log.Timber

class ProgramFragment : BaseFragment<ProgramViewModel, ProgramBinding>(R.layout.program),
    ProgramUserActionListener {
    override fun onClickItem(program: Program) {
    }

    private lateinit var topAdapter: ProgramAdapter
    private lateinit var resultAdapter: ProgramAdapter
    private lateinit var programTodayAdapter: ProgramTodayAdapter

    private lateinit var searchView: androidx.appcompat.widget.SearchView

    private var mediaPlayer: MediaPlayer? = null

    private var isSearching = false
    private var notFound = false

    private val viewModel: ProgramViewModel by viewModels {
        ProgramViewModelFactory()
    }

    override fun afterInflateView() {
        setHasOptionsMenu(true)
        mParentVM = viewModel
        mBinding.mViewModel = viewModel
        mBinding.mListener = this
        mediaPlayer = (requireActivity().application as MyApplication).mMediaPlayer
    }

    override fun setContentData() {

        val itemDecoration =
            RecyclerviewItemDecoration(
                requireContext(),
                16f
            )

        programTodayAdapter = ProgramTodayAdapter()
        mBinding.programBroadcastTodayRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mBinding.programBroadcastTodayRecyclerview.adapter = programTodayAdapter
        mBinding.programBroadcastTodayRecyclerview.addItemDecoration(itemDecoration)

        topAdapter = ProgramAdapter()
        mBinding.programProgramsRecyclerview.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        mBinding.programProgramsRecyclerview.adapter = topAdapter
        mBinding.programProgramsRecyclerview.addItemDecoration(
            RecyclerviewItemGridTwoHorizontalDecoration(
                requireContext(),
                16f
            )
        )

        resultAdapter = ProgramAdapter()
        mBinding.programResultSearch.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
        mBinding.programResultSearch.adapter = resultAdapter
        mBinding.programResultSearch.addItemDecoration(
            RecyclerviewItemGridTwoHorizontalDecoration(
                requireContext(),
                16f
            )
        )

        try {
            if (mediaPlayer?.isPlaying != null) {
                viewModel.stateStreaming(mediaPlayer!!.isPlaying)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }


    override fun onCreateObserver(viewModel: ProgramViewModel) {
        viewModel.apply {
            programs.observe(this@ProgramFragment, Observer {
                if (it.isNotEmpty()) {
                    topAdapter.submitList(it)
                } else {
                    viewModel.showMessage.value = Event("program not found")
                }
            })
        }


        viewModel.apply {
            programs.observe(this@ProgramFragment, Observer {
                val programTodayList = ArrayList<Program>()
                for (d in it) {
                    if (d.heldDay.contains('-')) {
                        val dummyDay: String = d.heldDay.replace(" ", "")
                        val dummyDayString = dummyDay.split("-")
                        Timber.i("cekidot: dummyDayString[0]: ${dummyDayString[0]}  dummyDayString[1]): ${dummyDayString[1]})")

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
                if (it.isNotEmpty() && !isSearching) {
                    programTodayAdapter.submitList(programTodayList)
                } else {
//                    viewModel.showMessage.value = Event("program not found")
                    startActivity(Intent(activity, NoInternet::class.java))
                }
            })
        }


        viewModel.apply {
            resultprograms.observe(this@ProgramFragment, Observer {
                if (isSearching) {
                    notFound = if (it.isNotEmpty()) {
                        resultAdapter.submitList(it)
                        false
                    } else {
                        mBinding.noResultLayout.noresult_label.text =
                            resources.getString(R.string.no_result, viewModel.noresultsearchtext)
                        true
                    }
                    viewOnSearching(true)
                }
            })
        }

    }

    override fun setMessageType(): String {
        return MESSAGE_TYPE_TOAST
    }

    override fun onPlayRadio() {
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

    private fun viewOnSearching(status: Boolean) {
        if (status && !notFound) {
            Timber.i("onsearching: status && !notFound")
            mBinding.programLabelBroadcast.text = "Search Result"
            mBinding.programProgramsRecyclerview.visibility = View.GONE
            mBinding.programLabelRadioProgram.visibility = View.GONE
            mBinding.programBroadcastTodayRecyclerview.visibility = View.GONE
            mBinding.programLabelBroadcast.visibility = View.VISIBLE
            mBinding.noResultLayout.visibility = View.GONE
            mBinding.programResultSearch.visibility = View.VISIBLE
        } else if (status && notFound) {
            Timber.i("onsearching: status && notFound")
            mBinding.programProgramsRecyclerview.visibility = View.GONE
            mBinding.programLabelRadioProgram.visibility = View.GONE
            mBinding.programBroadcastTodayRecyclerview.visibility = View.GONE
            mBinding.programLabelBroadcast.visibility = View.GONE
            mBinding.programResultSearch.visibility = View.GONE
            mBinding.noResultLayout.visibility = View.VISIBLE
        } else {
            Timber.i("onsearching: else")
            mBinding.programLabelBroadcast.text = "Broadcast Today"
            mBinding.programProgramsRecyclerview.visibility = View.VISIBLE
            mBinding.programLabelRadioProgram.visibility = View.VISIBLE
            mBinding.programBroadcastTodayRecyclerview.visibility = View.VISIBLE
            mBinding.programLabelBroadcast.visibility = View.VISIBLE
            mBinding.programResultSearch.visibility = View.GONE
            mBinding.noResultLayout.visibility = View.GONE
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {

        val manager = activity?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        searchView = menu.findItem(R.id.search)?.actionView as androidx.appcompat.widget.SearchView
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                isSearching = true
                viewModel.searchPrograms(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                isSearching = true
                return false
            }
        })

        searchView.setOnCloseListener {
            searchView.clearFocus()
            searchView.setQuery("", false)
            searchView.onActionViewCollapsed()
            viewModel.start()
            isSearching = false
            viewOnSearching(false)
            true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }


}

