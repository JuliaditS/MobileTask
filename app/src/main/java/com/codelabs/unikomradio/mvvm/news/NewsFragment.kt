package com.codelabs.unikomradio.mvvm.news


import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codelabs.unikomradio.MyApplication
import com.codelabs.unikomradio.NoInternet
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.databinding.NewsBinding
import com.codelabs.unikomradio.utilities.base.BaseFragment
import com.codelabs.unikomradio.utilities.services.MediaPlayerServices
import kotlinx.android.synthetic.main.no_result.view.*

class NewsFragment : BaseFragment<NewsViewModel, NewsBinding>(R.layout.news),
    NewsUserActionListener {


    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private var isSearching = false
    var found = false

    private lateinit var adapter: NewsAdapter
    private lateinit var resultAdapter: NewsAdapter

    private var mediaPlayer: MediaPlayer? = null


    private val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory()
    }

    override fun afterInflateView() {
        setHasOptionsMenu(true)
        mParentVM = viewModel
        mBinding.mViewModel = viewModel
        mediaPlayer = (requireActivity().application as MyApplication).mMediaPlayer
    }

    override fun setContentData() {
        adapter = NewsAdapter()
        resultAdapter = NewsAdapter()
        mBinding.mListener = this

        mBinding.newsRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mBinding.newsRecyclerview.adapter = adapter
        mBinding.newsRecyclerview.isNestedScrollingEnabled = false

        mBinding.newsSearchResultRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        mBinding.newsSearchResultRecyclerview.adapter = resultAdapter

        mBinding.newsSearchResultRecyclerview.isNestedScrollingEnabled = false

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
//                    viewModel.showMessage.value = Event("news not found")
                    startActivity(Intent(activity, NoInternet::class.java))
                }
            })
        }

        viewModel.apply {
            resultNews.observe(this@NewsFragment, Observer {
                if (isSearching) {
                    if (it.isNotEmpty()) {
                        resultAdapter.submitList(it)
                        found = true
                    } else {
                        mBinding.noResultLayout.noresult_label.text =
                            resources.getString(R.string.no_result, viewModel.noresultsearchtext)
                        found = false
                    }
                    viewOnSearching(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)

        searchView = menu.findItem(R.id.search)?.actionView as androidx.appcompat.widget.SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                isSearching = true
                viewModel.searchNews(query)
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

    private fun viewOnSearching(status: Boolean) {
        if (status && found) {
            mBinding.newsSearchResultRecyclerview.visibility = View.VISIBLE
            mBinding.newsRecyclerview.visibility = View.GONE
            mBinding.noResultLayout.visibility = View.GONE
        } else if (status && !found) {
            mBinding.newsSearchResultRecyclerview.visibility = View.GONE
            mBinding.newsRecyclerview.visibility = View.GONE
            mBinding.noResultLayout.visibility = View.VISIBLE
        } else {
            mBinding.newsSearchResultRecyclerview.visibility = View.GONE
            mBinding.newsRecyclerview.visibility = View.VISIBLE
            mBinding.noResultLayout.visibility = View.GONE
        }
    }


}
