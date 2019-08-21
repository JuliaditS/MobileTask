package com.codelabs.unikomradio.mvvm.home

import com.codelabs.unikomradio.R
import com.codelabs.unikomradio.databinding.HomeBinding
import com.codelabs.unikomradio.utilities.base.BaseFragment

class HomeFragment : BaseFragment<HomeViewModel,HomeBinding>(R.layout.home){
    override fun onCreateObserver(viewModel: HomeViewModel) {
    }

    override fun setContentData() {
    }

    override fun setMessageType(): String {
        return MESSAGE_TYPE_TOAST
    }

    override fun afterInflateView() {
    }

}
