package com.think.runex.feature.event.team

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import com.jozzee.android.core.fragment.onBackPressed
import com.jozzee.android.core.view.content
import com.jozzee.android.core.view.hideKeyboard
import com.think.runex.R
import com.think.runex.base.PermissionsLauncherScreen
import com.think.runex.config.KEY_REGISTER_ID
import com.think.runex.feature.dashboard.DashboardScreen
import com.think.runex.feature.event.data.RegisteredData
import com.think.runex.util.GetContentHelper
import com.think.runex.util.NightMode
import com.think.runex.util.TakePictureHelper
import com.think.runex.util.extension.*
import kotlinx.android.synthetic.main.screen_team_editor.*
import kotlinx.android.synthetic.main.toolbar.*

class TeamEditorScreen : PermissionsLauncherScreen() {

    companion object {
        @JvmStatic
        fun newInstance(registerData: RegisteredData) = TeamEditorScreen().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_REGISTER_ID, registerData)
            }
        }
    }

    private lateinit var viewModel: TeamEditorViewModel

    private var confirmButton: AppCompatButton? = null

    private var takePictureHelper: TakePictureHelper? = null
    private var getContentHelper: GetContentHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = getViewModel(TeamEditorViewModel.Factory(requireContext()))
        viewModel.registerData = arguments?.getParcelable(KEY_REGISTER_ID)

        takePictureHelper = TakePictureHelper(this)
        getContentHelper = GetContentHelper(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.screen_team_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.registerData == null) {
            showAlertDialog(getString(R.string.error), getString(R.string.data_not_found), isCancelEnable = false) {
                onBackPressed()
            }
        }

        setupComponents()
        subscribeUi()
        performGetTeamImage()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar_layout, R.string.edit_info, R.drawable.ic_navigation_back)
        updateTeamInfo()
    }

    private fun subscribeUi() {
        viewModel.setOnHandleError(::errorHandler)
    }

    private fun updateTeamInfo() {
        team_name_input?.removeTextChangedListener(textWatcher)
        color_input?.removeTextChangedListener(textWatcher)
        zone_input?.removeTextChangedListener(textWatcher)

        viewModel.registerData?.ticketOptions?.get(0)?.userOption?.also {
            team_name_input?.setText(it.team)
            color_input?.setText(it.color)
            zone_input?.setText(it.zone)
        }

        team_name_input?.addTextChangedListener(textWatcher)
        color_input?.addTextChangedListener(textWatcher)
        zone_input?.addTextChangedListener(textWatcher)
    }

    private fun performGetTeamImage() = launch {
        team_image?.loadTeamImage(viewModel.getTeamImageUrl())
    }

    private fun performUpdateTeamInfo() = launch {

        showProgressDialog(R.string.update_info)

        val isSuccess = viewModel.updateTeamInfo(
            team_name_input?.content(),
            color_input?.content(),
            zone_input?.content()
        )

        hideProgressDialog()

        if (isSuccess) {
            //Update live data for refresh screen().
            getMainViewModel().refreshScreen(TeamManagementScreen::class.java.simpleName)
            updateTeamInfo()
        }
    }

    private fun isDataValid(): Boolean {

        var isValid = false
        val userOption = viewModel.registerData?.ticketOptions?.get(0)?.userOption

        val teamName = team_name_input?.content()
        val color = color_input?.content()
        val zone = zone_input?.content()

        if (teamName != userOption?.team || color != userOption?.color || zone != userOption?.zone) {
            isValid = true
        }

        //Update state of confirm button
        confirmButton?.isEnabled = isValid

        return isValid
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_editor, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        confirmButton = menu.findItem(R.id.menu_confirm).actionView.findViewById(R.id.confirm_button)
        confirmButton?.setOnClickListener {
            if (isDataValid()) {
                view?.hideKeyboard()
                performUpdateTeamInfo()
            }
        }
        super.onPrepareOptionsMenu(menu)
    }

    private var textWatcher: TextWatcher? = null
        get() {
            if (field == null) {
                field = object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        isDataValid()
                    }

                    override fun afterTextChanged(s: Editable?) {
                    }
                }
            }
            return field
        }

    override fun errorHandler(code: Int, message: String, tag: String?) {
        super.errorHandler(code, message, tag)
    }

    override fun onDestroyView() {
        team_name_input?.removeTextChangedListener(textWatcher)
        color_input?.removeTextChangedListener(textWatcher)
        zone_input?.removeTextChangedListener(textWatcher)
        super.onDestroyView()
    }

    override fun onDestroy() {
        //removeObservers(viewModel.userInfo)
        textWatcher = null
        takePictureHelper?.unregisterTakePictureResult()
        takePictureHelper?.clear(requireContext())
        getContentHelper?.unregisterAllResult()
        super.onDestroy()
    }

}