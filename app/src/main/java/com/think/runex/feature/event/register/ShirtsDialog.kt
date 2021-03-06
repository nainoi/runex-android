package com.think.runex.feature.event.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jozzee.android.core.resource.getDrawable
import com.think.runex.R
import com.think.runex.common.toJson
import com.think.runex.component.recyclerview.LineSeparatorItemDecoration
import com.think.runex.feature.event.ShirtsAdapter
import com.think.runex.feature.event.data.Shirt
import com.think.runex.feature.user.GenderDialog
import kotlinx.android.synthetic.main.dialog_shirts.*

class ShirtsDialog : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance(shirts: ArrayList<Shirt>?) = ShirtsDialog().apply {
            Log.e("Jozzee", "Shirt: ${shirts.toJson()}")
            arguments = Bundle().apply {
                putParcelableArrayList("shirts", shirts)
            }
        }
    }

    //private lateinit var rootView: View
    private lateinit var adapter: ShirtsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppAlertDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_shirts, container, false)
    }

//    @SuppressLint("InflateParams")
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_shirt_sizes, null)
//        return MaterialAlertDialogBuilder(requireContext(), R.style.AppAlertDialog).apply {
//            setView(rootView)
//            setupComponents()
//        }.create()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        adapter.submitList(arguments?.getParcelableArrayList("shirts"))
    }

    private fun setupComponents() {
        //Set up recycler view
        adapter = ShirtsAdapter()
        shirt_size_list?.addItemDecoration(LineSeparatorItemDecoration(getDrawable(R.drawable.line_separator_list_item)))
        shirt_size_list?.layoutManager = LinearLayoutManager(requireContext())
        shirt_size_list?.adapter = adapter
    }

    private fun subscribeUi() {
        adapter.setOnItemClickListener { shirt ->
            getOnShirtSelectedListener()?.onShirtSelected(shirt)
            dismissAllowingStateLoss()
        }
    }

    private fun getOnShirtSelectedListener(): OnShirtSelectedListener? {
        if (parentFragment != null && parentFragment is OnShirtSelectedListener) {
            return parentFragment as OnShirtSelectedListener
        } else if (activity != null && activity is OnShirtSelectedListener) {
            return activity as OnShirtSelectedListener
        }
        return null;
    }


    interface OnShirtSelectedListener {
        fun onShirtSelected(shirt: Shirt)
    }
}