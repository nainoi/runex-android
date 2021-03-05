package com.think.runex.feature.event.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.fragment.childFragmentCount
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.resource.getDrawable
import com.jozzee.android.core.view.gone
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.common.getViewModel
import com.think.runex.common.observe
import com.think.runex.common.setColorFilter
import com.think.runex.component.recyclerview.LineSeparatorItemDecoration
import com.think.runex.feature.event.detail.TicketTypeAdapter
import kotlinx.android.synthetic.main.fragment_choose_ticket.*

class ChooseTicketFragment : BaseScreen() {

    private lateinit var viewModel: RegisterEventViewModel
    private lateinit var adapter: TicketTypeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = requireParentFragment().getViewModel(RegisterEventViewModelFactory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_ticket, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        //Initial ticket list
        adapter.submitList(viewModel.eventDetail.value?.tickets?.toMutableList())
    }

    private fun setupComponents() {
        //Set up recycler view
        adapter = TicketTypeAdapter(true)
        val lineSeparator = getDrawable(R.drawable.line_separator_list_item)?.apply {
            setColorFilter(getColor(R.color.border))
        }
        tickets_list?.addItemDecoration(LineSeparatorItemDecoration(lineSeparator))
        tickets_list?.layoutManager = LinearLayoutManager(requireContext())
        tickets_list?.adapter = adapter
    }

    private fun subscribeUi() {

        adapter.setOnItemClickListener { ticket ->
            viewModel.onSelectTicket(ticket)
        }

        //viewModel.setOnHandleError(::errorHandler)
    }
}