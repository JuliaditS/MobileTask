package com.codelabs.unikomradio.mvvm.programs

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codelabs.unikomradio.MyApplication
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.data.model.Program
import com.codelabs.unikomradio.databinding.ProgramBinding
import com.codelabs.unikomradio.utilities.base.BaseFragment
import com.codelabs.unikomradio.utilities.helper.Event
import com.codelabs.unikomradio.utilities.services.MediaPlayerServices
import timber.log.Timber

class ProgramFragment : BaseFragment<ProgramViewModel, ProgramBinding>(R.layout.program), ProgramUserActionListener {
    override fun onClickItem(program: Program) {
    }

    private lateinit var topAdapter: ProgramAdapter
    private lateinit var bottomAdapter: ProgramTodayAdapter

    private var mediaPlayer: MediaPlayer? = null

    private val viewModel: ProgramViewModel by viewModels {
        ProgramViewModelFactory()
    }

    override fun afterInflateView() {
        mParentVM = viewModel
        mBinding.mViewModel = viewModel
        mBinding.mListener = this
        mediaPlayer = (requireActivity().application as MyApplication).mMediaPlayer
    }

    override fun setContentData() {
        topAdapter = ProgramAdapter()
        mBinding.programProgramsRecyclerview.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mBinding.programProgramsRecyclerview.adapter = topAdapter


        bottomAdapter = ProgramTodayAdapter()
        mBinding.programBroadcastTodayRecyclerview.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        mBinding.programBroadcastTodayRecyclerview.adapter = bottomAdapter

        if (mediaPlayer?.isPlaying != null) {
            viewModel.stateStreaming(mediaPlayer!!.isPlaying)
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
                if (it.isNotEmpty()) {
                    bottomAdapter.submitList(it)
                } else {
                    viewModel.showMessage.value = Event("program not found")
                }
            })
        }

        viewModel.apply {
            isPlaying.observe(viewLifecycleOwner, Observer<Boolean> {
                if (it) {
                    mBinding.programPlayradioPlayButton.setImageResource(R.mipmap.pause)
                } else {
                    mBinding.programPlayradioPlayButton.setImageResource(R.mipmap.playbutton)
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


    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {

        val manager = activity?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

}
