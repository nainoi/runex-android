package com.think.runex.feature.event.team

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.appcompat.widget.AppCompatButton
import com.jozzee.android.core.fragment.onBackPressed
import com.jozzee.android.core.permission.shouldShowPermissionRationale
import com.jozzee.android.core.view.content
import com.jozzee.android.core.view.hideKeyboard
import com.jozzee.android.core.view.showDialog
import com.jozzee.android.core.view.showToast
import com.think.runex.R
import com.think.runex.base.PermissionsLauncherScreen
import com.think.runex.component.ImageSourcesDialog
import com.think.runex.config.KEY_REGISTER_ID
import com.think.runex.feature.event.data.RegisteredData
import com.think.runex.util.GetContentHelper
import com.think.runex.util.NightMode
import com.think.runex.util.TakePictureHelper
import com.think.runex.util.extension.*
import kotlinx.android.synthetic.main.screen_team_editor.*
import kotlinx.android.synthetic.main.toolbar.*

class TeamEditorScreen : PermissionsLauncherScreen(), ImageSourcesDialog.OnSelectImageSourceListener {

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
        change_team_image_button?.setOnClickListener {
            showDialog(ImageSourcesDialog())
        }

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

    override fun onSelectImageSource(source: Int) {
        when (source) {
            ImageSourcesDialog.SOURCE_CAMERA -> checkPermissionsAndTakePicture()
            ImageSourcesDialog.SOURCE_GALLERY -> checkPermissionAndGetImageContent()
        }
    }

    private fun checkPermissionsAndTakePicture() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        requestPermissions(permissions) { results ->
            when {
                //User allow all permissions (camera, storage) will be take picture
                allPermissionsGranted(results) -> takePictureHelper?.takePicture(requireContext()) { pictureUri ->
                    pictureUri?.also { performUpdateTeamImage(it) }
                }
                //User denied access to camera or storage.
                shouldShowPermissionRationale(permissions) -> showToast(R.string.camera_permission_denied)
                //User denied and select don't ask again.
                else -> requireContext().showSettingPermissionInSettingDialog()
            }
        }
    }

    private fun checkPermissionAndGetImageContent() =
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE) { isGranted ->
            when {
                //User allow all permissions storage
                isGranted -> getContentHelper?.getImage { imageUri ->
                    imageUri?.also { performUpdateTeamImage(it) }
                }
                //User denied access to storage.
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> showToast(R.string.gallery_permission_denied)
                //User denied and select don't ask again.
                else -> requireContext().showSettingPermissionInSettingDialog()
            }
        }

    private fun performUpdateTeamImage(uri: Uri) = launch {

        showProgressDialog(R.string.update_profile_image)
        val teamImageUrl = viewModel.updateTeamImage(requireContext(), uri)
        hideProgressDialog()

        if (teamImageUrl?.isNotBlank() == true) {
            //Update live data for refresh screen().
            getMainViewModel().refreshScreen(TeamManagementScreen::class.java.simpleName)
            performGetTeamImage()
        }
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