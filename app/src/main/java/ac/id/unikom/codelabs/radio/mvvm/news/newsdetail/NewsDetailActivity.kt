package ac.id.unikom.codelabs.radio.mvvm.news.newsdetail

import ac.id.unikom.codelabs.radio.R
import ac.id.unikom.codelabs.radio.data.model.News
import ac.id.unikom.codelabs.radio.databinding.ActivityNewsDetailBinding
import ac.id.unikom.codelabs.radio.mvvm.programs.programdetail.ProgramDetailAdapter
import ac.id.unikom.codelabs.radio.mvvm.programs.programdetail.ProgramDetailViewModel
import ac.id.unikom.codelabs.radio.mvvm.programs.programdetail.ProgramDetailViewModelFactory
import ac.id.unikom.codelabs.radio.utilities.INTENT_PARCELABLE
import ac.id.unikom.codelabs.radio.utilities.base.BaseActivity
import ac.id.unikom.codelabs.radio.utilities.helper.Preferences
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels

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
