package ac.id.unikom.codelabs.radio.mvvm.programs.programdetail

import ac.id.unikom.codelabs.radio.R
import ac.id.unikom.codelabs.radio.data.model.Program
import ac.id.unikom.codelabs.radio.databinding.ActivityProgramDetailBinding
import ac.id.unikom.codelabs.radio.utilities.INTENT_PARCELABLE
import ac.id.unikom.codelabs.radio.utilities.base.BaseActivity
import ac.id.unikom.codelabs.radio.utilities.helper.Preferences
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager

class ProgramDetailActivity :
    BaseActivity<ProgramDetailViewModel, ActivityProgramDetailBinding>(R.layout.activity_program_detail) {

    private lateinit var adapter: ProgramDetailAdapter

    private val viewModel: ProgramDetailViewModel by viewModels {
        ProgramDetailViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = "Program"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0f

        mParentVM = viewModel
        val programData: Program = intent.getParcelableExtra(INTENT_PARCELABLE)
        mBinding.program = programData
        mBinding.programDetailThumbnail.setImageURI(programData.imageUrl)
        adapter = ProgramDetailAdapter()
        mBinding.programDetailAnnouncerRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.programDetailAnnouncerRecyclerview.adapter = adapter
        adapter.submitList(programData.announcer)
    }

    override fun setContentData() {

    }

    override fun onCreateObserver(viewModel: ProgramDetailViewModel) {
    }

    override fun setMessageType(): String {
        return MESSAGE_TYPE_TOAST
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}
