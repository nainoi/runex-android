package com.think.runex.feature.payment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.common.*
import com.think.runex.component.recyclerview.MarginItemDecoration
import com.think.runex.config.KEY_EVENT
import com.think.runex.config.KEY_ID
import com.think.runex.config.KEY_PRICE
import com.think.runex.feature.payment.creditcard.CreditCardActivityContract
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_pay_event.*
import kotlinx.android.synthetic.main.toolbar.*

class PayEventScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(eventName: String, orderId: String, price: Double) = PayEventScreen().apply {
            arguments = Bundle().apply {
                putString(KEY_EVENT, eventName)
                putString(KEY_ID, orderId)
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
            Logger.info("Jozzee", "Oemise Token: ${token.toJson()}")
            // process your token here
        }

        viewModel = getViewModel(PaymentViewModelFactory(requireContext()))
        viewModel.updateOrderDetails(arguments?.getString(KEY_EVENT) ?: "",
                arguments?.getString(KEY_ID) ?: "",
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
            when {
                paymentMethod.name?.contains("Credit", true) == true -> {
                    creditCardLauncher?.launch(getString(R.string.omise_key))
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

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        progress_bar?.gone()
    }

}