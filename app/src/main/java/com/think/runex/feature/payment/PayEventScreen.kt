package com.think.runex.feature.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.common.getViewModel
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.component.recyclerview.MarginItemDecoration
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_pay_event.*
import kotlinx.android.synthetic.main.toolbar.*

class PayEventScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance() = PayEventScreen().apply {
        }
    }

    private lateinit var viewModel: PaymentViewModel
    private lateinit var adapter: PaymentMethodsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(PaymentViewModelFactory(requireContext()))
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

        //Set update recycler view
        adapter = PaymentMethodsAdapter(790.0)
        payment_method_list?.addItemDecoration(MarginItemDecoration(getDimension(R.dimen.space_16dp)))
        payment_method_list?.layoutManager = LinearLayoutManager(requireContext())
        payment_method_list?.adapter = adapter
    }

    private fun subscribeUi() {

        adapter.setOnItemClickListener { paymentMethod ->

        }

        viewModel.setOnHandleError(::errorHandler)
    }

    private fun performGetPaymentMethods() = launch {
        progress_bar?.visible()
        payment_method_list?.gone()

        adapter.submitList(viewModel.getPaymentMethods().toMutableList())

        progress_bar?.gone()
        payment_method_list?.visible()
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        progress_bar?.gone()
    }

}