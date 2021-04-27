package ac.id.unikom.codelabs.radio.mvvm.streaming.streaming_topcharts

import ac.id.unikom.codelabs.radio.R
import ac.id.unikom.codelabs.radio.databinding.StreamingTopchartsBinding
import ac.id.unikom.codelabs.radio.utilities.InjectorUtils
import ac.id.unikom.codelabs.radio.utilities.base.BaseActivity
import ac.id.unikom.codelabs.radio.utilities.helper.Event
import ac.id.unikom.codelabs.radio.utilities.helper.Preferences
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer

class StreamingTopchartsActivity :
    BaseActivity<StreamingTopchartsViewModel, StreamingTopchartsBinding>(R.layout.streaming_topcharts),
    StreamingTopchartsUserActionListener {

    private lateinit var adapter: StreamingTopchartsAdapter

    private val viewModel: StreamingTopchartsViewModel by viewModels {
        InjectorUtils.provideTopchartViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mParentVM = viewModel
        mBinding.mListener = this
        mBinding.mViewModel = viewModel

        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Top Charts"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateObserver(viewModel: StreamingTopchartsViewModel) {
        viewModel.apply {
            topcharts.observe(this@StreamingTopchartsActivity, Observer {
                if (it.isNotEmpty()) {
                    adapter.submitList(it.sortedBy { topChart -> topChart.currentRank })
                } else {
                    viewModel.showMessage.value = Event("Topcharts not found")
                }
            })
        }
    }

    override fun setContentData() {
        viewModel.getTopcharts()
        adapter = StreamingTopchartsAdapter()
        mBinding.streamingBottomsheetRecyclerview.adapter = adapter
    }

    override fun setMessageType(): String {
        return ""
    }

    override fun onclickItem() {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        if (Preferences(this).isLightMode()) {
            theme.applyStyle(R.style.AppTheme_Light, true)
        } else {
            theme.applyStyle(R.style.AppTheme_Dark, true)
        }
        return super.getTheme()
    }

}