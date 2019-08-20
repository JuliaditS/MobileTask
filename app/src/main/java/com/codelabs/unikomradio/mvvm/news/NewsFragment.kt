package com.codelabs.unikomradio.mvvm.news


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.codelabs.unikomradio.MyApplication
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.databinding.NewsBinding
import com.codelabs.unikomradio.mvvm.programs.ProgramAdapter
import com.codelabs.unikomradio.mvvm.programs.ProgramTodayAdapter
import com.codelabs.unikomradio.utilities.base.BaseFragment
import com.codelabs.unikomradio.utilities.helper.Event
import com.codelabs.unikomradio.utilities.services.MediaPlayerServices

class NewsFragment : BaseFragment<NewsViewModel, NewsBinding>(R.layout.news), NewsUserActionListener {


    private lateinit var adapter: NewsAdapter
    private var mediaPlayer: MediaPlayer? = null


    private val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory()
    }

    override fun afterInflateView() {
        mParentVM = viewModel
        mBinding.mViewModel = viewModel
        mediaPlayer = (requireActivity().application as MyApplication).mMediaPlayer
    }

    override fun setContentData() {
        adapter = NewsAdapter()
        mBinding.newsRecyclerview.layoutManager =
                LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        mBinding.newsRecyclerview.adapter = adapter

        if (mediaPlayer?.isPlaying != null) {
            viewModel.stateStreaming(mediaPlayer!!.isPlaying)
        }
    }

    override fun onCreateObserver(viewModel: NewsViewModel) {
        viewModel.apply {
            news.observe(this@NewsFragment, Observer {
                if (it.isNotEmpty()) {
                    adapter.submitList(it)
                } else {
                    viewModel.showMessage.value = Event("news not found")
                }
            })
        }

        viewModel.apply {
            isPlaying.observe(viewLifecycleOwner, Observer<Boolean> {
                if (it) {
                    mBinding.newsPlayradioPlayButton.setImageResource(R.mipmap.pause)
                } else {
                    mBinding.newsPlayradioPlayButton.setImageResource(R.mipmap.playbutton)
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


    override fun setMessageType(): String {
        return MESSAGE_TYPE_TOAST
    }


}
