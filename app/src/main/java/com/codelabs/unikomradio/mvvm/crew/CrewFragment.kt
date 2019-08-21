package com.codelabs.unikomradio.mvvm.crew


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codelabs.unikomradio.MyApplication

import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.databinding.CrewBinding
import com.codelabs.unikomradio.utilities.base.BaseFragment
import com.codelabs.unikomradio.utilities.helper.Event
import com.codelabs.unikomradio.utilities.services.MediaPlayerServices

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CrewFragment : BaseFragment<CrewViewModel,CrewBinding>(R.layout.crew),CrewUserActionListener {

    private lateinit var adapter: CrewAdapter
    private var mediaPlayer: MediaPlayer? = null

    private val viewModel: CrewViewModel by viewModels {
        CrewViewModelFactory()
    }

    override fun afterInflateView() {
        mParentVM = viewModel
        mBinding.mListener = this
        mBinding.mViewModel = viewModel
        mediaPlayer = (requireActivity().application as MyApplication).mMediaPlayer

    }

    override fun setContentData() {
        adapter = CrewAdapter()
        mBinding.crewRecyclerview.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        mBinding.crewRecyclerview.adapter = adapter

        if (mediaPlayer?.isPlaying != null){
            viewModel.stateStreaming(mediaPlayer!!.isPlaying)
        }
    }

    override fun setMessageType(): String {
        return MESSAGE_TYPE_TOAST
    }

    override fun onCreateObserver(viewModel: CrewViewModel) {
        viewModel.apply {
            crews.observe(this@CrewFragment, Observer {
                if (it.isNotEmpty()){
                    adapter.submitList(it)
                } else {
                    viewModel.showMessage.value = Event("crew not found")
                }
            })
        }

        viewModel.apply {
            isPlaying.observe(viewLifecycleOwner, Observer<Boolean> {
                if (it) {
                    mBinding.crewPlayradioPlayButton.setImageResource(R.mipmap.pause)
                } else {
                    mBinding.crewPlayradioPlayButton.setImageResource(R.mipmap.playbutton)
                }
            })
        }
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
