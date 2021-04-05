package com.think.runex.feature.event.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.resource.getDimension
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.feature.event.data.RegisterStatus
import com.think.runex.util.extension.*
import com.think.runex.feature.event.detail.EventDetailsScreen
import com.think.runex.feature.payment.PayEventScreen
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.fragment_confirm_registration.*
import kotlinx.android.synthetic.main.fragment_confirm_registration.event_name_label
import kotlinx.android.synthetic.main.list_item_event_registration.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.delay

class ConfirmRegistrationFragment : BaseScreen() {

    private lateinit var viewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = requireParentFragment().getViewModel(RegistrationViewModel.Factory(requireContext()))
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
        citizen_id_label?.text = ("${getString(R.string.citizen_id)}: ${user?.cityCenId ?: ""}")
        phone_label?.text = ("${getString(R.string.phone)}: ${user?.phone ?: ""}")
        event_name_label_confirm_register?.text = event?.title ?: ""
        distance_label?.text = ("${getString(R.string.distances)}: ${ticket?.distance ?: ""} ${getString(R.string.km)}")
        price_label?.text = ("${getString(R.string.price)}: ${ticket?.getPriceDisplay(requireContext())} ")

        //Update confirm button
        if (viewModel.registerStatus == RegisterStatus.WAITING_CONFIRM) {
            register_label?.text = getString(R.string.update_registration)
        }
    }

    private fun subscribeUi() {
        register_button?.setOnClickListener {
            if (viewModel.registerStatus == RegisterStatus.REGISTER) {
                performRegisterEvent()
            } else if (viewModel.registerStatus == RegisterStatus.WAITING_CONFIRM) {
                performUpdateRegisterInfo()
            }
        }
    }

    private fun performRegisterEvent() = launch {

        showProgressDialog(R.string.register)

        val register = viewModel.registerEvent()

        hideProgressDialog()

        if (register != null) {

            //Update live data for refresh screen().
            getMainViewModel().refreshScreen()

            showAlertDialog(R.string.success, R.string.register_event_success, isCancelEnable = false) {
                //On positive click
                launch {

                    //Add Payment screen
                    addFragment(PayEventScreen.newInstance(
                            eventCode = register.getEventCode(),
                            eventName = register.getEventName(),
                            orderId = register.getOrderId(0),
                            registerId = register.getRegisterId(0),
                            ref2 = register.ref2 ?: "",
                            totalPrice = register.getTotalPrice()))

                    //Remove previous screens (EventDetailsScreen) and remove self from fragment back stack
                    delay(100)
                    findFragment<EventDetailsScreen>()?.also { removeFragment(it) }
                    removeFragment(requireParentFragment())
                }
            }
        }
    }

    //TODO("Handle response")
    private fun performUpdateRegisterInfo() = launch {

        showProgressDialog(R.string.update_registration)

        val isSuccess = viewModel.updateRegisterInfo()

        hideProgressDialog()

        if (isSuccess) {

            //Update live data for refresh screen().
            getMainViewModel().refreshScreen()

            showAlertDialog(R.string.success, R.string.update_registration_success, isCancelEnable = false) {
                //On positive click
                launch {
                    //Remove previous screens (EventDetailsScreen) and remove self from fragment back stack
                    delay(100)
                    findFragment<EventDetailsScreen>()?.also { removeFragment(it) }
                    removeFragment(requireParentFragment())
                }
            }
        }
    }
}