package com.think.runex.feature.event.pay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.common.*
import com.think.runex.component.recyclerview.MarginItemDecoration
import com.think.runex.config.KEY_CODE
import com.think.runex.config.KEY_EVENT
import com.think.runex.config.KEY_ID
import com.think.runex.config.KEY_PRICE
import com.think.runex.feature.event.SelectEventsBottomSheet
import com.think.runex.feature.payment.PaymentMethodsAdapter
import com.think.runex.feature.payment.PaymentViewModel
import com.think.runex.feature.payment.PaymentViewModelFactory
import com.think.runex.feature.payment.creditcard.CreditCardActivityContract
import com.think.runex.feature.payment.data.PaymentType
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_pay_event.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.delay

class PayEventScreen : BaseScreen() {

    companion object {

        @JvmStatic
        fun newInstance(eventName: String,
                        eventCode: String,
                        registerId: String,
                        orderId: String,
                        ref2: String,
                        price: Double) = PayEventScreen().apply {

            arguments = Bundle().apply {
                putString(KEY_EVENT, eventName)
                putString(KEY_CODE, eventCode)
                putString("register_id", registerId)
                putString(KEY_ID, orderId)
                putString("ref2", ref2)
                putDouble(KEY_PRICE, price)
            }
        }
    }

    private var creditCardLauncher: ActivityResultLauncher<String>? = null

    private lateinit var viewModel: PaymentViewModel
    private lateinit var adapter: PaymentMethodsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        creditCardLauncher = registerForActivityResult(CreditCardActivityContract()) { token ->
            token?.id?.also { performPayEventByCreditOrDebitCard(it) }
        }

        viewModel = getViewModel(PaymentViewModelFactory(requireContext()))
        viewModel.updateOrderDetails(arguments?.getString(KEY_EVENT) ?: "",
                arguments?.getString(KEY_CODE) ?: "",
                arguments?.getString("register_id") ?: "",
                arguments?.getString(KEY_ID) ?: "",
                arguments?.getString("ref2") ?: "",
                arguments?.getDouble(KEY_PRICE) ?: 0.0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_pay_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        performGetPaymentMethods()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.payment, R.drawable.ic_navigation_back)

        //Set price and order details
        price_label?.text = ("${viewModel.price.displayFormat()} ${getString(R.string.thai_bath)}")
        event_name_label?.text = viewModel.eventName
        order_id_label?.text = ("${getString(R.string.order_no)}: ${viewModel.orderId}")

        //Set update recycler view
        adapter = PaymentMethodsAdapter(viewModel.price)
        payment_method_list?.addItemDecoration(MarginItemDecoration(getDimension(R.dimen.space_16dp)))
        payment_method_list?.layoutManager = LinearLayoutManager(requireContext())
        payment_method_list?.adapter = adapter
    }

    private fun subscribeUi() {

        adapter.setOnItemClickListener { paymentMethod ->
            when (paymentMethod.type) {
                PaymentType.CREDIT_CARD -> {
                    viewModel.paymentMethod = paymentMethod
                    creditCardLauncher?.launch(getString(R.string.omise_key))
                }
                PaymentType.QR_CODE -> {
                    viewModel.paymentMethod = paymentMethod
                    showBottomSheet(QRToPayBottomSheet())
                }
                PaymentType.QR -> {
                    viewModel.paymentMethod = paymentMethod
                    showBottomSheet(QRToPayBottomSheet())
                }
            }
        }

        viewModel.setOnHandleError(::errorHandler)
    }

    private fun performGetPaymentMethods() = launch {
        progress_bar?.visible()
        payment_method_list?.gone()

        adapter.submitList(viewModel.getPaymentMethods()?.toMutableList())

        progress_bar?.gone()
        payment_method_list?.visible()
    }

    private fun performPayEventByCreditOrDebitCard(omiseTokenId: String) = launch {

        showProgressDialog(R.string.pay_event_in_progress, showDot = false)

        val isSuccess = viewModel.payEventByCreditOrDebitCard(omiseTokenId)

        hideProgressDialog()

        if (isSuccess) {
            //Update live data to refresh screen.
            getMainViewModel().refreshScreen()

            //Show payment success screen
            addFragment(PayEventSuccessScreen.newInstance(viewModel.eventName, viewModel.orderId))

            //Remove self from fragment back stack
            delay(100)
            removeFragment(this@PayEventScreen)
        }
    }


    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        progress_bar?.gone()
    }

}