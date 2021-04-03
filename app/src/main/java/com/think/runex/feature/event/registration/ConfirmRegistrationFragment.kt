package com.think.runex.feature.event.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.resource.getDimension
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.util.extension.*
import com.think.runex.feature.event.detail.EventDetailsScreen
import com.think.runex.feature.payment.PayEventScreen
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.fragment_confirm_registration.*
import kotlinx.android.synthetic.main.fragment_confirm_registration.event_name_label
import kotlinx.android.synthetic.main.list_item_event_registration.*
import kotlinx.coroutines.delay

class ConfirmRegistrationFragment : BaseScreen() {

//    companion object {
//        @JvmStatic
//        fun newInstance() = RegisterConfirmationFragment().apply {
//        }
//    }

    private lateinit var viewModel: RegisterEventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = requireParentFragment().getViewModel(RegisterEventViewModel.Factory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_registration, container, false)
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
        val ticket = viewModel.getCurrentTicketOption()?.ticket

        event_image?.loadEventsImage(event?.getCoverImage(), cornersRadian = getDimension(R.dimen.space_4dp))
        event_name_label?.text = event?.title ?: ""
        event_price_label?.text = ("${getString(R.string.amount)}: ${ticket?.price?.numberDisplayFormat() ?: ""}")

        val user = viewModel.getCurrentTicketOption()?.userOption
        full_name_label?.text = user?.fullName ?: ""
        address_label?.text = ("${getString(R.string.address)}: ${user?.address ?: ""}")
        identification_number_label?.text = ("${getString(R.string.identification_number)}: ${user?.cityCenId ?: ""}")
        phone_label?.text = ("${getString(R.string.phone)}: ${user?.phone ?: ""}")
        event_name_label_confirm_register?.text = event?.title ?: ""
        distance_label?.text = ("${getString(R.string.distances)}: ${ticket?.distance ?: ""} ${getString(R.string.km)}")
        price_label?.text = ("${getString(R.string.price)}: ${ticket?.getPriceDisplay(requireContext())} ")
    }

    private fun subscribeUi() {
        register_button?.setOnClickListener {
            performRegisterEvent()
        }
    }

    private fun performRegisterEvent() = launch {

        showProgressDialog(R.string.register)

        val event = viewModel.registerEvent()

        hideProgressDialog()

        if (event != null) {

            //Update live data for refresh screen().
            getMainViewModel().refreshScreen()

            showAlertDialog(R.string.register_event_success, isCancelEnable = false) {
                //On positive click
                launch {

                    //Add Payment screen
                    addFragment(PayEventScreen.newInstance(
                            eventCode = event.getEventCode(),
                            eventName = event.getEventName(),
                            orderId = event.getOrderId(),
                            registerId = event.getRegisterId(),
                            ref2 = event.ref2 ?: "",
                            totalPrice = event.getTotalPrice()))

                    //Remove previous screens (EventDetailsScreen) and remove self from fragment back stack
                    delay(100)
                    findFragment<EventDetailsScreen>()?.also { removeFragment(it) }
                    removeFragment(requireParentFragment())
                }
            }
        }
    }
}