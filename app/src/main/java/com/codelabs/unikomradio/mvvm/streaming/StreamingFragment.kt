package com.codelabs.unikomradio.mvvm.streaming

import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.media.MediaPlayer
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.codelabs.unikomradio.MyApplication
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.data.model.Program
import com.codelabs.unikomradio.databinding.StreamingBinding
import com.codelabs.unikomradio.mvvm.programs.specifyToday
import com.codelabs.unikomradio.mvvm.streaming.streaming_topcharts.StreamingTopchartsActivity
import com.codelabs.unikomradio.utilities.base.BaseFragment
import com.codelabs.unikomradio.utilities.helper.Event
import com.codelabs.unikomradio.utilities.helper.Preferences
import com.google.android.exoplayer2.SimpleExoPlayer
import timber.log.Timber


class StreamingFragment : BaseFragment<StreamingViewModel, StreamingBinding>(R.layout.streaming),
    StreamingUserActionListener {

    private var isLightMode = false
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var exoPlayer: SimpleExoPlayer


    private val viewModel: StreamingViewModel by viewModels {
        StreamingViewModelFactory()
    }

    override fun onCreateObserver(viewModel: StreamingViewModel) {
        viewModel.apply {
            isPlaying.observe(viewLifecycleOwner, Observer<Boolean> {
                if (it) {
                    if (isLightMode){
                        mBinding.streamingPlayStreaming.setImageResource(R.mipmap.pause_light)
                    } else {
                        mBinding.streamingPlayStreaming.setImageResource(R.mipmap.pause)
                    }
                } else {
                    if (isLightMode){
                        mBinding.streamingPlayStreaming.setImageResource(R.mipmap.playbuttonlight)
                    } else {
                        mBinding.streamingPlayStreaming.setImageResource(R.mipmap.playbutton)
                    }
                }
            })

            programs.observe(this@StreamingFragment, Observer {

                val programTodayList = ArrayList<Program>()
                for ((i, d) in it.withIndex()) {
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
                        if ( (p.startAt.toDouble() <= specifyToday.getHourSpecify()) && (p.endAt.toDouble() >= specifyToday.getHourSpecify())){
                            mBinding.streamingSongThumbnail.setImageURI(p.imageUrl)
                            mBinding.streamingProgram.text = p.title
                            mBinding.streamingLabelProgram.text = "radio hits unikom"
                        }
                    }
                } else {
                    viewModel.showMessage.value = Event("program not found")
                }
            })

        }


    }

    override fun setContentData() {

        if (isLightMode){
            mBinding.streamingPlaylistIcon.setImageResource(R.drawable.icon_topchart_light)
            mBinding.streamingSoundActive.setImageResource(R.drawable.icon_sound_light)
        }

//        viewModel.stateStreaming(exoPlayer.playWhenReady)
    }

    override fun setMessageType(): String {
        return ""
    }

    override fun afterInflateView() {
        mParentVM = viewModel
        mBinding.mListener = this
        mBinding.mViewModel = viewModel
//        mediaPlayer = (requireActivity().application as MyApplication).mMediaPlayer
//        exoPlayer = (requireActivity().application as MyApplication).exoPlayer
        isLightMode = Preferences(requireContext()).isLightMode()

    }





//    override fun onPlayMusicClick() {
//        val intent = Intent(activity, MediaPlayerServices::class.java)
//        if (!isMyServiceRunning(MediaPlayerServices::class.java)) {
//            intent.action = MediaPlayerServices.ACTION_PLAY
//            activity?.startService(intent)
//            viewModel.playStreaming()
//        } else {
//            if (mediaPlayer?.isPlaying == true) {
//                mediaPlayer?.pause()
//                viewModel.stopStreaming()
//            } else {
//                mediaPlayer?.start()
//                viewModel.playStreaming()
//            }
//        }
//    }

    override fun onPlayMusicClick() {
        try {
            if (exoPlayer.playWhenReady) {
                viewModel.stopStreaming()
                exoPlayer.playWhenReady = false
            } else {
                viewModel.playStreaming()
                exoPlayer.playWhenReady = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
        viewModel.muteStreaming()
        if (viewModel.isMute.value == true) {
            mediaPlayer?.setVolume(0f, 0f)
            exoPlayer.volume = 0f
            if (!isLightMode){
                mBinding.streamingSoundActive.setImageResource(R.mipmap.volumemutelight)
            } else {
                mBinding.streamingSoundActive.setImageResource(R.mipmap.volumemute)
            }
        } else {
            exoPlayer.volume = 1f
            if (!isLightMode){
                mBinding.streamingSoundActive.setImageResource(R.mipmap.volumemutelight)
            } else {
                mBinding.streamingSoundActive.setImageResource(R.mipmap.volume)
            }
        }
    }
}

