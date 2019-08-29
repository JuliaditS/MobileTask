package com.codelabs.unikomradio.mvvm.news.newsdetail

import android.content.res.Resources
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.data.model.News
import com.codelabs.unikomradio.data.model.Program
import com.codelabs.unikomradio.databinding.ActivityNewsDetailBinding
import com.codelabs.unikomradio.databinding.ActivityProgramDetailBinding
import com.codelabs.unikomradio.mvvm.programs.programdetail.ProgramDetailAdapter
import com.codelabs.unikomradio.mvvm.programs.programdetail.ProgramDetailViewModel
import com.codelabs.unikomradio.mvvm.programs.programdetail.ProgramDetailViewModelFactory
import com.codelabs.unikomradio.utilities.INTENT_PARCELABLE
import com.codelabs.unikomradio.utilities.base.BaseActivity
import com.codelabs.unikomradio.utilities.helper.Preferences
import com.codelabs.unikomradio.utilities.helper.ThemeMode
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetailActivity :
    BaseActivity<ProgramDetailViewModel, ActivityNewsDetailBinding>(R.layout.activity_news_detail) {

    private lateinit var adapter: ProgramDetailAdapter

    private val viewModel: ProgramDetailViewModel by viewModels {
        ProgramDetailViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "News"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        mParentVM = viewModel
        val newsData: News = intent.getParcelableExtra(INTENT_PARCELABLE)
        mBinding.news = newsData
        mBinding.newsDetailThumbnail.setImageURI(newsData.imageUrl)
        adapter = ProgramDetailAdapter()
        setTheme(R.style.FeedActivityThemeDark)

        if (Preferences(this).isLightMode()){
            setTheme(R.style.FeedActivityThemeLight)
            mBinding.newsDetailDateLogo.setImageDrawable(getDrawable(R.drawable.icon_date_light))
        } else {
            setTheme(R.style.FeedActivityThemeDark)
        }

    }

    override fun setContentData() {

    }

    override fun onCreateObserver(viewModel: ProgramDetailViewModel) {
    }

    override fun setMessageType(): String {
        return MESSAGE_TYPE_TOAST
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        if (Preferences(this).isLightMode()){
            theme.applyStyle(R.style.AppTheme_Light,true)
        } else {
            theme.applyStyle(R.style.AppTheme_Dark,true)
        }
        return super.getTheme()
    }
}
