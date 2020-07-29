package com.codelabs.newunikomradio.utilities.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.codelabs.newunikomradio.utilities.extensions.*
import com.codelabs.newunikomradio.utilities.helper.EventObserver
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<T : BaseViewModel, B : ViewDataBinding>(val layoutId: Int) : BottomSheetDialogFragment() {

    lateinit var mParentVM: T
    lateinit var mBinding: B
    private var mMessageType = MESSAGE_TYPE_SNACK

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), layoutId, container, false)
        afterInflateView()
        return mBinding.root
    }

    override fun onViewCreated(paramView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(paramView, savedInstanceState)
        mParentVM.apply {
            showMessage.observe(this@BaseBottomSheetFragment, EventObserver {
                if (it.isNotEmpty()) {
                    when (mMessageType) {
                        MESSAGE_TYPE_SNACK_CUSTOM -> {
                            view?.showCustomSnackBar(it)
                        }
                        MESSAGE_TYPE_SNACK -> {
                            view?.showSnackBar(it)
                        }
                        else -> {
                            requireContext().showToast(it)
                        }
                    }
                }
            })

            showMessageRes.observe(this@BaseBottomSheetFragment, EventObserver {
                if (it != 0) {
                    when (mMessageType) {
                        MESSAGE_TYPE_SNACK_CUSTOM -> {
                            view?.showCustomSnackBarRes(it)
                        }
                        MESSAGE_TYPE_SNACK -> {
                            view?.showSnackBarRes(it)
                        }
                        else -> {
                            requireContext().showToast(it)
                        }
                    }
                }
            })
        }
        onCreateObserver(mParentVM)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setContentData()
        mMessageType = setMessageType()
    }


    abstract fun onCreateObserver(viewModel: T)
    abstract fun setContentData()
    abstract fun setMessageType(): String
    abstract fun afterInflateView()

    companion object {
        const val MESSAGE_TYPE_TOAST = "TOAST_TYPE"
        const val MESSAGE_TYPE_SNACK = "SNACK_TYPE"
        const val MESSAGE_TYPE_SNACK_CUSTOM = "SNACK_CUSTOM_TYPE"
    }
}
