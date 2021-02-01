package com.think.runex.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.think.runex.R
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.ui.base.BaseScreen
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.toolbar.*

class ProfileEditorScreen : BaseScreen() {

    private var confirmButton: AppCompatButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_profile_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.edit_info, R.drawable.ic_navigation_back)
    }

    private fun subscribeUi() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile_editor, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        confirmButton = menu.findItem(R.id.menu_confirm).actionView.findViewById(R.id.confirm_button)
        super.onPrepareOptionsMenu(menu)
    }
}