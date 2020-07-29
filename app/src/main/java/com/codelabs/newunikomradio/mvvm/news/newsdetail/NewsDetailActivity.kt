package com.codelabs.newunikomradio.mvvm.news.newsdetail

import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.codelabs.newunikomradio.R
import com.codelabs.newunikomradio.data.model.News
import com.codelabs.newunikomradio.databinding.ActivityNewsDetailBinding
import com.codelabs.newunikomradio.mvvm.programs.programdetail.ProgramDetailAdapter
import com.codelabs.newunikomradio.mvvm.programs.programdetail.ProgramDetailViewModel
import com.codelabs.newunikomradio.mvvm.programs.programdetail.ProgramDetailViewModelFactory
import com.codelabs.newunikomradio.utilities.INTENT_PARCELABLE
import com.codelabs.newunikomradio.utilities.base.BaseActivity
import com.codelabs.newunikomradio.utilities.helper.Preferences

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
