package com.think.runex.feature.event.register.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.fragment.onBackPressed
import com.jozzee.android.core.fragment.popFragment
import com.jozzee.android.core.resource.getDimension
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.common.getViewModel
import com.think.runex.common.loadEventsImage
import com.think.runex.common.numberDisplayFormat
import com.think.runex.common.showAlertDialog
import com.think.runex.feature.event.register.RegisterEventViewModel
import com.think.runex.feature.event.register.RegisterEventViewModelFactory
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.fragment_register_confirmation.*
import kotlinx.android.synthetic.main.fragment_register_confirmation.event_name_label
import kotlinx.android.synthetic.main.list_item_event_registration.*

class RegisterConfirmationFragment : BaseScreen() {

//    companion object {
//        @JvmStatic
//        fun newInstance() = RegisterConfirmationFragment().apply {
//        }
//    }

    private lateinit var viewModel: RegisterEventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = requireParentFragment().getViewModel(RegisterEventViewModelFactory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) {
            //Subscribe error handle when resume
            viewModel.setOnHandleError(::errorHandler)
        }
    }

    override fun onResume() {
        super.onResume()
        //Subscribe error handle when resume
        viewModel.setOnHandleError(::errorHandler)
    }

    private fun setupComponents() {
        val event = viewModel.eventDetail.value
        val ticket = viewModel.ticketData.ticket

        event_image?.loadEventsImage(event?.getCoverImage(), cornersRadian = getDimension(R.dimen.space_4dp))
        event_name_label?.text = event?.title ?: ""
        event_price_label?.text = ("${getString(R.string.amount)}: ${ticket?.price?.numberDisplayFormat() ?: ""}")

        val user = viewModel.ticketData.userData
        full_name_label?.text = user?.fullName ?: ""
        address_label?.text = ("${getString(R.string.address)}: ${user?.address ?: ""}")
        identification_number_label?.text = ("${getString(R.string.identification_number)}: ${user?.cityCenId ?: ""}")
        phone_label?.text = ("${getString(R.string.phone)}: ${user?.phone ?: ""}")
        event_name_label_confirm_register?.text = event?.title ?: ""
        distance_label?.text = ("${getString(R.string.distances)}: ${ticket?.distance ?: ""} ${getString(R.string.km)}")
        price_label?.text = ("${getString(R.string.price)}: ${ticket?.getPrice(requireContext())} ")
    }

    private fun subscribeUi() {
        register_button?.setOnClickListener {
            performRegisterEvent()
        }
    }

    private fun performRegisterEvent() = launch {
        showProgressDialog(R.string.register)
        val isSuccess = viewModel.registerEvent()
        hideProgressDialog()
        if (isSuccess) {
            getMainViewModel().refreshScreen()
            showRegisterSuccessDialog()
        }
    }

    private fun showRegisterSuccessDialog() {
        showAlertDialog(R.string.register_event_success, isCancelEnable = false) {
            //On positive click
            popFragment()
        }
    }
}