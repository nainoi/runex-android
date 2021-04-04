package com.think.runex.feature.activity

import android.Manifest
import android.app.DatePickerDialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jozzee.android.core.datetime.dateTimeFormat
import com.jozzee.android.core.datetime.toCalendar
import com.jozzee.android.core.datetime.year
import com.jozzee.android.core.fragment.onBackPressed
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.view.content
import com.jozzee.android.core.view.hideKeyboard
import com.jozzee.android.core.view.showToast
import com.think.runex.R
import com.think.runex.base.PermissionsLauncherScreen
import com.think.runex.util.extension.*
import com.think.runex.config.DISPLAY_DATE_FORMAT_FULL_MONTH
import com.think.runex.config.KEY_BODY
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.feature.activity.data.AddActivityBody
import com.think.runex.util.GetContentHelper
import com.think.runex.util.NightMode
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.screen_add_activity.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class AddActivityScreen : PermissionsLauncherScreen(), DatePickerDialog.OnDateSetListener {

    companion object {
        @JvmStatic
        fun newInstance(body: AddActivityBody) = AddActivityScreen().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_BODY, body)
            }
        }
    }

    private lateinit var viewModel: ActivityViewModel
    private var getContentHelper: GetContentHelper? = null

    private var addActivityBody: AddActivityBody? = null
    private var activityImageUri: Uri? = null
    private var activityDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(ActivityViewModel.Factory(requireContext()))
        getContentHelper = GetContentHelper(this)

        addActivityBody = arguments?.getParcelable(KEY_BODY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_add_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        if (addActivityBody == null) {
            showAlertDialog(R.string.error, R.string.data_not_found, false) {
                onBackPressed()
            }
        }
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar_layout, R.string.add_activity, R.drawable.ic_navigation_back)
        setActivityImageForPlaceholder()
    }

    private fun subscribeUi() {

        add_image_button?.setOnClickListener {
            checkPermissionAndGetImageContent()
        }

        activity_date_input?.setOnClickListener {
            view?.hideKeyboard()
            showDatePicker()
        }

        submit_button?.setOnClickListener {
            if (isDataValid()) {
                performAddActivity()
            }
        }

        viewModel.setOnHandleError(::errorHandler)
    }

    private fun performAddActivity() = launch {
        showProgressDialog(R.string.add_activity)
    }

    private fun checkPermissionAndGetImageContent() = requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE) { isGranted ->
        when {
            //User allow all permissions storage
            isGranted -> getContentHelper?.getImage { imageUri ->
                activityImageUri = imageUri
                loadActivityImage()
            }
            //User denied access to storage.
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> showToast(R.string.gallery_permission_denied)
            //User denied and select don't ask again.
            else -> requireContext().showSettingPermissionInSettingDialog()
        }
    }

    private fun loadActivityImage() {
        setActivityImageForImage()
        Glide.with(activity_image)
                .load(activityImageUri)
                .centerCrop()
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        setActivityImageForPlaceholder()
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                })
                .into(activity_image)
    }

    private fun setActivityImageForPlaceholder() {
        val space56Dp = getDimension(R.dimen.space_56dp)
        activity_image?.setPadding(space56Dp, space56Dp, space56Dp, space56Dp)
        val color = activity_image?.context?.getColorAttr(R.attr.iconColorDisable)
        activity_image?.setImageDrawable(getDrawable(R.drawable.ic_running, color))
    }

    private fun setActivityImageForImage() {
        activity_image?.setPadding(0, 0, 0, 0)
        activity_image?.setImageDrawable(null)
    }

    private fun showDatePicker() {
        var calendar = activityDate?.toCalendar(SERVER_DATE_TIME_FORMAT) ?: Calendar.getInstance()
        if (calendar.year() < 1000) {
            calendar = Calendar.getInstance()
        }
        DatePickerDialog(requireContext(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).run {
            datePicker.maxDate = System.currentTimeMillis()
            show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, dayOfMonth, 0, 0, 0)
        }
        activityDate = calendar.dateTimeFormat(SERVER_DATE_TIME_FORMAT)
        activity_date_input?.setText(calendar.dateTimeFormat(DISPLAY_DATE_FORMAT_FULL_MONTH))
    }

    private fun isDataValid(): Boolean {
        if (activityImageUri == null) {
            showAlertDialog(getString(R.string.warning), getString(R.string.activity_image_required))
            return false
        }

        if (distance_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.distances))
            return false
        }

        if (activityDate.isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.activity_date))
            return false
        }

        return true
    }

    private fun showRequiredInputDialog(inputName: String) {
        showAlertDialog(getString(R.string.warning), getString(R.string.input_required, inputName))
    }

    override fun onDestroy() {
        getContentHelper?.unregisterAllResult()
        super.onDestroy()
    }
}