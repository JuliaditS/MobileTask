package com.codelabs.unikomradio.mvvm.streaming

import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.res.ColorStateList
import android.media.MediaPlayer
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.codelabs.unikomradio.NoInternet
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.data.model.Program
import com.codelabs.unikomradio.databinding.StreamingBinding
import com.codelabs.unikomradio.mvvm.StateListener
import com.codelabs.unikomradio.mvvm.programs.specifyToday
import com.codelabs.unikomradio.mvvm.streaming.streaming_topcharts.StreamingTopchartsActivity
import com.codelabs.unikomradio.utilities.base.BaseFragment
import com.codelabs.unikomradio.utilities.helper.Preferences
import com.codelabs.unikomradio.utilities.services.ExoPlayerServices
import com.google.android.exoplayer2.SimpleExoPlayer
import timber.log.Timber


class StreamingFragment : BaseFragment<StreamingViewModel, StreamingBinding>(R.layout.streaming),
    StreamingUserActionListener {


    private var isLightMode = false
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var exoPlayer: SimpleExoPlayer

    private lateinit var mExoService: ExoPlayerServices
    private var mBound: Boolean = false
    private lateinit var stateListener: StateListener


    private val viewModel: StreamingViewModel by viewModels {
        StreamingViewModelFactory()
    }

    override fun onCreateObserver(viewModel: StreamingViewModel) {
        activity?.let {
            require(it is StateListener) { "Activity should implement OnSeeAllClickListener" }
            stateListener = activity as StateListener
            viewModel.setStateStreaming(stateListener.isPlayingRadio())
        }

        viewModel.apply {
            isPlaying.observe(viewLifecycleOwner, Observer<Boolean> {
                if (it) {
                    if (isLightMode) {
                        mBinding.streamingPlayStreaming.setImageResource(R.mipmap.pause_light)
                    } else {
                        mBinding.streamingPlayStreaming.setImageResource(R.mipmap.pause)
                    }
                } else {
                    if (isLightMode) {
                        mBinding.streamingPlayStreaming.setImageResource(R.mipmap.playbuttonlight)
                    } else {
                        mBinding.streamingPlayStreaming.setImageResource(R.mipmap.playbutton)
                    }
                }
            })

            programs.observe(this@StreamingFragment, Observer {

                val programTodayList = ArrayList<Program>()

                for (d in it) {
                    if (d.heldDay.contains('-')) {
                        val dummyDay: String = d.heldDay.replace(" ", "")
                        val dummyDayString = dummyDay.split("-")
                        if (specifyToday.isToday(dummyDayString[0], dummyDayString[1])) {
                            programTodayList.add(d)
                        }
                    } else {
                        if (specifyToday.isToday(d.heldDay)) {
                            programTodayList.add(d)
                        }
                    }
                }

                if (it.isNotEmpty()) {
                    for (p in programTodayList) {
                        if ((p.startAt.toDouble() <= specifyToday.getHourSpecify()) && (p.endAt.toDouble() >= specifyToday.getHourSpecify())) {
                            mBinding.streamingSongThumbnail.setImageURI(p.imageUrl)
                            mBinding.streamingProgram.text = p.title
                            mBinding.streamingLabelProgram.text = "radio hits unikom"
                        }
                    }
                } else {
//                    viewModel.showMessage.value = Event("program not found")
                    startActivity(Intent(activity, NoInternet::class.java))
                }
            })

        }
    }


    override fun setContentData() {

        if (isLightMode) {
            mBinding.streamingPlaylistIcon.setImageResource(R.drawable.icon_topchart_light)
            mBinding.streamingSoundActive.setImageResource(R.drawable.icon_sound_light)
        }
    }

    override fun setMessageType(): String {
        return ""
    }

    override fun afterInflateView() {
        mParentVM = viewModel
        mBinding.mListener = this
        mBinding.mViewModel = viewModel
        isLightMode = Preferences(requireContext()).isLightMode()

        if (isLightMode) {
            mBinding.streamingSpectrum.setImageDrawable(requireContext().getDrawable(R.drawable.spectrum_light))
            mBinding.streamingProggressbar.progressTintList =
                ColorStateList.valueOf(resources.getColor(R.color.colorPrimary))
        }
    }

    override fun onPlayMusicClick() {
        val intent = Intent(requireContext(), ExoPlayerServices::class.java)

        if (!isMyServiceRunning(ExoPlayerServices::class.java)) {
            requireContext().startService(intent)
            viewModel.playStreaming()
        } else {
            if (viewModel.isPlaying.value == true) {
                viewModel.stopStreaming()
                intent.action = ExoPlayerServices.ACTION_PAUSE
            } else {
                viewModel.playStreaming()
                intent.action = ExoPlayerServices.ACTION_PLAY
            }
            requireContext().startService(intent)
        }
        stateListener.setStatePlayingRadio(viewModel.isPlaying())
    }

    override fun onTopChartClick() {
        startActivity(Intent(requireContext(), StreamingTopchartsActivity::class.java))
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {

        val manager = activity?.getSystemService(ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }


    override fun onMuteClick() {
        val intent = Intent(requireContext(), ExoPlayerServices::class.java)

        if (!isMyServiceRunning(ExoPlayerServices::class.java)) {
            intent.action = ExoPlayerServices.ACTION_INIT
            requireContext().startService(intent)
        }

        viewModel.muteStreaming()

        Timber.i("cekcek: ${viewModel.isMute.value}")
        if (viewModel.isMute.value == true) {
            intent.action = ExoPlayerServices.ACTION_UNMUTE
            if (isLightMode) {
                mBinding.streamingSoundActive.setImageResource(R.mipmap.volumelight)
            } else {
                mBinding.streamingSoundActive.setImageResource(R.mipmap.volume)
            }
        } else {
            intent.action = ExoPlayerServices.ACTION_MUTE
            if (isLightMode) {
                mBinding.streamingSoundActive.setImageResource(R.mipmap.volumemutelight)
            } else {
                mBinding.streamingSoundActive.setImageResource(R.mipmap.volumemute)
            }
        }

        requireContext().startService(intent)

    }
}

