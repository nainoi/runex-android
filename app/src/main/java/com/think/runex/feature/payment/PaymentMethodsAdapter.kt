package com.think.runex.feature.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jozzee.android.core.view.showToast
import com.think.runex.R
import com.think.runex.common.requireContext
import com.think.runex.feature.payment.data.PaymentMethod
import kotlinx.android.synthetic.main.list_item_payment_method.view.*

class PaymentMethodsAdapter(var amount: Double) : ListAdapter<PaymentMethod, PaymentMethodsAdapter.ViewHolder>(PaymentMethodsDiffCallback()) {

    var onItemClickListener: ((paymentMethod: PaymentMethod) -> Unit)? = null

    @JvmName("setOnItemClickListenerJava")
    fun setOnItemClickListener(onItemClick: (paymentMethod: PaymentMethod) -> Unit) {
        onItemClickListener = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    class PaymentMethodsDiffCallback : DiffUtil.ItemCallback<PaymentMethod>() {
        override fun areItemsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PaymentMethod, newItem: PaymentMethod): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

//        companion object {
//            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
//                    .inflate(R.layout.list_item_payment_method, parent, false))
//        }

        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_payment_method, parent, false))

        fun bind(data: PaymentMethod?, onItemClick: ((paymentMethod: PaymentMethod) -> Unit)? = null) {

            itemView.payment_method_icon?.setImageDrawable(data?.getPaymentMethodIcon(requireContext()))
            itemView.payment_method_label?.text = data?.name ?: ""
            itemView.payment_method_charge_label?.text = data?.getChargeAmountDisplay(requireContext(), amount)

            itemView.list_item_payment_method?.setOnClickListener {
                when (data != null && data.isActive == true) {
                    true -> onItemClick?.invoke(data)
                    false -> requireContext().showToast(R.string.service_unavailable)
                }
            }
        }

    }
}