package com.think.runex.ui

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.DatePicker
import androidx.appcompat.widget.AppCompatButton
import com.jozzee.android.core.datetime.dateTimeFormat
import com.jozzee.android.core.datetime.toCalendar
import com.jozzee.android.core.datetime.year
import com.jozzee.android.core.permission.allPermissionsGranted
import com.jozzee.android.core.permission.havePermission
import com.jozzee.android.core.permission.havePermissions
import com.jozzee.android.core.permission.shouldShowPermissionRationale
import com.jozzee.android.core.view.content
import com.jozzee.android.core.view.showDialog
import com.jozzee.android.core.view.showToast
import com.think.runex.R
import com.think.runex.common.*
import com.think.runex.config.*
import com.think.runex.feature.user.Gender
import com.think.runex.feature.user.UserInfo
import com.think.runex.feature.user.UserViewModel
import com.think.runex.feature.user.UserViewModelFactory
import com.think.runex.ui.base.BaseScreen
import com.think.runex.ui.component.GenderDialog
import com.think.runex.ui.component.SelectImageSourceDialog
import com.think.runex.util.ImagePickerHelper
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_profile_editor.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class ProfileEditorScreen : BaseScreen(), DatePickerDialog.OnDateSetListener,
        GenderDialog.OnGenderSelectedListener, SelectImageSourceDialog.OnSelectImageSourceListener {

    private lateinit var viewModel: UserViewModel

    private var confirmButton: AppCompatButton? = null

    private var helper: ImagePickerHelper? = null
    private var currentBirthDate: String? = null
    private var currentGender: String? = null
    private var tempProfileImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = requireActivity().getViewModel(UserViewModelFactory(requireContext()))
        helper = ImagePickerHelper()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_profile_editor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        //Get user info initial
        if (viewModel.userInfo.value == null) {
            viewModel.getUSerInfo()
        }
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.edit_info, R.drawable.ic_navigation_back)
    }

    private fun subscribeUi() {

        change_profile_image_button?.setOnClickListener {
            showDialog(SelectImageSourceDialog())
        }

        birth_date_input?.setOnClickListener {
            showDatePicker()
        }

        gender_input?.setOnClickListener {
            showDialog(GenderDialog())
        }

        viewModel.setOnHandleError(::errorHandler)

        observe(viewModel.userInfo) { userInfo ->
            if (view == null || isAdded.not()) return@observe
            updateUserInfo(userInfo)
        }
    }

    private fun updateUserInfo(userInfo: UserInfo?) {
        first_name_input?.removeTextChangedListener(textWatcher)
        last_name_input?.removeTextChangedListener(textWatcher)
        first_name_input?.clearFocus()
        last_name_input?.clearFocus()

        profile_image?.loadProfileImage(userInfo?.avatar)
        email_label?.text = userInfo?.email ?: ""
        first_name_input?.setText(userInfo?.firstName ?: "")
        last_name_input?.setText(userInfo?.lastName ?: "")

        //Birth date
        currentBirthDate = userInfo?.birthDate
        userInfo?.getBirthDateCalendar()?.also { calendar ->
            when (calendar.year() > 1000) {
                true -> birth_date_input?.setText(userInfo.getBirthDate(DISPLAY_DATE_FORMAT_FULL_MONTH))
                false -> birth_date_input?.setText("")
            }
        }

        //Gender
        currentGender = userInfo?.gender
        gender_input?.setText(userInfo?.gender ?: "")

        first_name_input?.addTextChangedListener(textWatcher)
        last_name_input?.addTextChangedListener(textWatcher)
    }

    private fun showDatePicker() {
        var calendar = currentBirthDate?.toCalendar(SERVER_DATE_TIME_FORMAT)
                ?: Calendar.getInstance()
        if (calendar.year() < 1000) {
            calendar = Calendar.getInstance()
        }
        DatePickerDialog(requireContext(), this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).run {
            datePicker.maxDate = System.currentTimeMillis()
            show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, dayOfMonth, 0, 0, 0)
        }
        currentBirthDate = calendar.dateTimeFormat(SERVER_DATE_TIME_FORMAT)
        birth_date_input?.setText(calendar.dateTimeFormat(DISPLAY_DATE_FORMAT_FULL_MONTH))
        isDataValid()
    }

    override fun onGenderSelected(gender: String) {
        currentGender = gender
        when (gender) {
            Gender.FEMALE -> gender_input.setText(getString(R.string.female))
            Gender.MALE -> gender_input.setText(getString(R.string.male))
            else -> gender_input.setText(getString(R.string.other))
        }
        isDataValid()
    }

    private fun performUpdateProfile(userInfo: UserInfo) = launch {
        showProgressDialog(R.string.update_info)
        viewModel.updateUserInfo(userInfo)
        hideProgressDialog()
    }

    private fun isDataValid(): UserInfo? {
        val currentUserInfo = UserInfo(viewModel.userInfo.value)

        /**
         * First name must not empty
         */
        first_name_input?.content().also { firstName ->
            if (firstName.isNullOrBlank()) {
                confirmButton?.isEnabled = false
                return null
            }
            currentUserInfo.firstName = firstName
        }
        /**
         * Last name can empty
         */
        currentUserInfo.lastName = last_name_input?.content()
        currentUserInfo.fullName = "${currentUserInfo.firstName ?: ""} ${currentUserInfo.lastName ?: ""}"

        //Birth date
        currentUserInfo.birthDate = currentBirthDate

        //Gender
        currentUserInfo.gender = currentGender

        //Update state of confirm button
        confirmButton?.isEnabled = currentUserInfo != viewModel.userInfo.value
        return if (confirmButton?.isEnabled == true) currentUserInfo else null
    }


    override fun onSelectImageSource(source: Int) {
        when (source) {
            SelectImageSourceDialog.SOURCE_CAMERA -> checkPermissionAndOpenCamera()
            SelectImageSourceDialog.SOURCE_GALLERY -> checkPermissionAndOpenGallery()
        }
    }

    private fun checkPermissionAndOpenCamera() {
        val permissions = arrayOf(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (havePermissions(permissions, RC_PERMISSION_TAKE_PICTURE)) {
            helper?.provideCameraIntent(requireContext())?.also { intent ->
                //Store temp uri for preview and upload
                intent.extras?.get(MediaStore.EXTRA_OUTPUT).toString()
                tempProfileImageUri = Uri.parse(intent.extras?.get(MediaStore.EXTRA_OUTPUT).toString())
                startActivityForResult(intent, RC_TAKE_PICTURE)
            }
        }
    }

    private fun checkPermissionAndOpenGallery() {
        if (havePermission(Manifest.permission.READ_EXTERNAL_STORAGE, RC_PERMISSION_OPEN_GALLERY)) {
            startActivityForResult(helper?.provideGalleryIntent(), RC_IMAGE_PICKER)
        }
    }

    private fun performUploadProfileImage(uri: Uri) = launch {
        showProgressDialog(R.string.update_profile_image)
        viewModel.updateProfileImage(requireContext(), uri)
        hideProgressDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_TAKE_PICTURE -> if (resultCode == Activity.RESULT_OK) {
                tempProfileImageUri?.also { performUploadProfileImage(it) }
            }
            RC_IMAGE_PICKER -> if (resultCode == Activity.RESULT_OK) {
                tempProfileImageUri = data?.data
                tempProfileImageUri?.also { performUploadProfileImage(it) }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RC_PERMISSION_TAKE_PICTURE -> when {
                //User allow to access location.
                allPermissionsGranted(grantResults) -> checkPermissionAndOpenCamera()
                //User denied access to location.
                shouldShowPermissionRationale(permissions) -> showToast(R.string.camera_permission_denied)
                //User denied and select don't ask again.
                else -> requireContext().showSettingPermissionInSettingDialog()
            }
            RC_PERMISSION_OPEN_GALLERY -> when {
                //User allow to access location.
                allPermissionsGranted(grantResults) -> checkPermissionAndOpenGallery()
                //User denied access to location.
                shouldShowPermissionRationale(permissions) -> showToast(R.string.gallery_permission_denied)
                //User denied and select don't ask again.
                else -> requireContext().showSettingPermissionInSettingDialog()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile_editor, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        confirmButton = menu.findItem(R.id.menu_confirm).actionView.findViewById(R.id.confirm_button)
        confirmButton?.setOnClickListener {
            isDataValid()?.also { performUpdateProfile(it) }
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

    override fun errorHandler(statusCode: Int, message: String) {
        super.errorHandler(statusCode, message)
    }

    override fun onDestroyView() {
        first_name_input?.removeTextChangedListener(textWatcher)
        last_name_input?.removeTextChangedListener(textWatcher)
        super.onDestroyView()
    }

    override fun onDestroy() {
        removeObservers(viewModel.userInfo)
        textWatcher = null
        helper?.clearTempFile(requireContext())
        helper = null
        super.onDestroy()
    }
}