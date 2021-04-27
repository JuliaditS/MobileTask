package ac.id.unikom.codelabs.radio.mvvm.crew


import ac.id.unikom.codelabs.radio.MyApplication
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ac.id.unikom.codelabs.radio.NoInternet
import ac.id.unikom.codelabs.radio.R
import ac.id.unikom.codelabs.radio.databinding.CrewBinding
import ac.id.unikom.codelabs.radio.utilities.base.BaseFragment
import ac.id.unikom.codelabs.radio.utilities.services.MediaPlayerServices
import androidx.fragment.app.viewModels

class CrewFragment : BaseFragment<CrewViewModel, CrewBinding>(R.layout.crew),
    CrewUserActionListener {

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
        mBinding.crewRecyclerview.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mBinding.crewRecyclerview.adapter = adapter

        if (mediaPlayer?.isPlaying != null) {
            viewModel.stateStreaming(mediaPlayer!!.isPlaying)
        }
    }

    override fun setMessageType(): String {
        return MESSAGE_TYPE_TOAST
    }

    override fun onCreateObserver(viewModel: CrewViewModel) {
        viewModel.apply {
            crews.observe(this@CrewFragment, Observer {
                if (it.isNotEmpty()) {
                    adapter.submitList(it)
                } else {
                    startActivity(Intent(activity, NoInternet::class.java))
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
