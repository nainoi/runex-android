package com.think.runex.feature.event.registration

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import android.widget.TextView
import com.jozzee.android.core.datetime.dateTimeFormat
import com.jozzee.android.core.datetime.toCalendar
import com.jozzee.android.core.datetime.year
import com.jozzee.android.core.text.isPhoneNumber
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.*
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.util.extension.*
import com.think.runex.config.DISPLAY_DATE_FORMAT_FULL_MONTH
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.datasource.api.ApiExceptionMessage
import com.think.runex.feature.address.data.SubDistrict
import com.think.runex.feature.event.data.EventCategory
import com.think.runex.feature.event.data.Shirt
import com.think.runex.feature.event.data.UserOptionEventRegistration
import com.think.runex.feature.user.GenderDialog
import com.think.runex.feature.user.UserViewModel
import com.think.runex.feature.user.data.Gender
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.fragment_fill_out_user_info.*
import java.util.*
import kotlin.collections.ArrayList

class FillOutUserInfoFragment : BaseScreen(), DatePickerDialog.OnDateSetListener,
        GenderDialog.OnGenderSelectedListener, ShirtsDialog.OnShirtSelectedListener {

    private lateinit var viewModel: RegistrationViewModel

    private var currentBirthDate: String? = null
    private var currentGender: String? = null

    private var zipCodeTextWatcher: TextWatcher? = null
    private var subDistrictTextWatcher: TextWatcher? = null
    private var districtTextWatcher: TextWatcher? = null
    private var provinceTextWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = requireParentFragment().getViewModel(RegistrationViewModel.Factory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fill_out_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) {
            //Subscribe error handle when resume
            viewModel.setOnHandleError(::errorHandler)
        }
    }

    override fun onResume() {
        super.onResume()

        //Subscribe error handle when resume
        viewModel.setOnHandleError(::errorHandler)
    }

    private fun setupComponents() {
        initTextChangedForAddressAutoFill()

        updateUserDataFromUserInfo()

        //Update team and optional data
        viewModel.getCurrentTicketOption()?.userOption?.also {
            optional_team_name_input?.setText(it.team)
            optional_color_input?.setText(it.color)
            optional_zone_input?.setText(it.zone)
        }
    }

    private fun subscribeUi() {

        birth_date_input?.setOnClickListener {
            view?.hideKeyboard()
            showDatePicker()
        }

        gender_input?.setOnClickListener {
            view?.hideKeyboard()
            showDialog(GenderDialog())
        }

        zip_code_input?.setOnItemClickListener { _, view, _, _ ->
            if (view is TextView) {
                onAutoFillSelected(viewModel.getSubDistrictFromFullAddress(view.content()))
            }
        }
        sub_district_input?.setOnItemClickListener { _, view, _, _ ->
            if (view is TextView) {
                onAutoFillSelected(viewModel.getSubDistrictFromFullAddress(view.content()))
            }
        }
        district_input?.setOnItemClickListener { _, view, _, _ ->
            if (view is TextView) {
                onAutoFillSelected(viewModel.getSubDistrictFromFullAddress(view.content()))
            }
        }
        province_input?.setOnItemClickListener { _, view, _, _ ->
            if (view is TextView) {
                onAutoFillSelected(viewModel.getSubDistrictFromFullAddress(view.content()))
            }
        }

        zip_code_input?.setOnDismissListener {
            addTextChangedAddressAutoFill()
        }
        sub_district_input?.setOnDismissListener {
            addTextChangedAddressAutoFill()
        }
        district_input?.setOnDismissListener {
            addTextChangedAddressAutoFill()
        }
        province_input?.setOnDismissListener {
            addTextChangedAddressAutoFill()
        }

        shirt_size_input?.setOnClickListener {
            when (viewModel.eventDetail.value?.shirts?.isNotEmpty() == true) {
                true -> showDialog(ShirtsDialog.newInstance(ArrayList(viewModel.eventDetail.value?.shirts)))
                false -> showToast(R.string.no_shirts_to_choose)
            }
        }

        confirm_button?.setOnClickListener {
            if (isDataValid()) {
                viewModel.setUserData(createUserData())
            }
        }

        observe(viewModel.addressAutoFill) { autoFill ->

            if (view == null || isAdded.not() || autoFill == null) return@observe

            when (autoFill.viewRequestId) {
                zip_code_input?.id -> zip_code_input?.showAddressAutoFill()
                sub_district_input?.id -> sub_district_input?.showAddressAutoFill()
                district_input?.id -> district_input?.showAddressAutoFill()
                province_input?.id -> province_input?.showAddressAutoFill()
            }
        }
    }

    private fun showDatePicker() {
        var calendar = currentBirthDate?.toCalendar(SERVER_DATE_TIME_FORMAT)
                ?: Calendar.getInstance()
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
        currentBirthDate = calendar.dateTimeFormat(SERVER_DATE_TIME_FORMAT)
        birth_date_input?.setText(calendar.dateTimeFormat(DISPLAY_DATE_FORMAT_FULL_MONTH))
        //isDataValid()
    }

    override fun onGenderSelected(gender: String) {
        currentGender = gender
        when (gender) {
            Gender.FEMALE -> gender_input.setText(getString(R.string.female))
            Gender.MALE -> gender_input.setText(getString(R.string.male))
            else -> gender_input.setText(getString(R.string.other))
        }
        //isDataValid()
    }

    override fun onShirtSelected(shirt: Shirt) {
        viewModel.onSelectShirt(shirt)
        shirt_size_input?.setText(shirt.size ?: "")
    }

    private fun AutoCompleteTextView.showAddressAutoFill() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), R.layout.list_item_address_auto_fill,
                viewModel.addressAutoFill.value?.autoFillArray ?: emptyArray())

        removeTextChangedAddressAutoFill()
        setAdapter(adapter)
        showDropDown()
    }

    private fun onAutoFillSelected(subDistrict: SubDistrict?) {
        if (subDistrict == null) {
            addTextChangedAddressAutoFill()
            return
        }

        sub_district_input.setText(subDistrict.subDistrict ?: "")
        sub_district_input?.setSelection(sub_district_input?.length() ?: 0)

        district_input.setText(subDistrict.district ?: "")
        district_input?.setSelection(district_input?.length() ?: 0)

        province_input.setText(subDistrict.province ?: "")
        province_input?.setSelection(province_input?.length() ?: 0)

        zip_code_input.setText(subDistrict.zipCode.toString())
        zip_code_input?.setSelection(zip_code_input?.length() ?: 0)

        addTextChangedAddressAutoFill()
        view?.hideKeyboard()
    }

    private fun initTextChangedForAddressAutoFill() {
        zipCodeTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Logger.debug("Jozzee", "ZipCode: onTextChanged: ${s.toString()}")
                when (s?.length == 5) {
                    true -> viewModel.searchAddressByZipCode(s.toString(), zip_code_input?.id ?: -1)
                    false -> zip_code_input?.setAdapter(null)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        zip_code_input?.addTextChangedListener(zipCodeTextWatcher)

        subDistrictTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Logger.debug("Jozzee", "Sub District: onTextChanged: ${s.toString()}")
                when (s?.length ?: 0 >= 3) {
                    true -> viewModel.searchAddressBySubDistricts(s.toString(), sub_district_input?.id
                            ?: -1)
                    false -> sub_district_input?.setAdapter(null)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        sub_district_input?.addTextChangedListener(subDistrictTextWatcher)

        districtTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Logger.debug("Jozzee", "District: onTextChanged: ${s.toString()}")
                when (s?.length ?: 0 >= 3) {
                    true -> viewModel.searchAddressByDistricts(s.toString(), sub_district_input?.id
                            ?: -1)
                    false -> sub_district_input?.setAdapter(null)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        district_input?.addTextChangedListener(districtTextWatcher)


        provinceTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Logger.debug("Jozzee", "Province: onTextChanged: ${s.toString()}")
                when (s?.length ?: 0 >= 3) {
                    true -> viewModel.searchAddressByProvince(s.toString(), sub_district_input?.id
                            ?: -1)
                    false -> sub_district_input?.setAdapter(null)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        province_input?.addTextChangedListener(provinceTextWatcher)
    }

    private fun removeTextChangedAddressAutoFill() {
        Logger.info("Jozzee", "removeTextChangedAddressAutoFill")
        zip_code_input?.removeTextChangedListener(zipCodeTextWatcher)
        sub_district_input?.removeTextChangedListener(subDistrictTextWatcher)
        district_input?.removeTextChangedListener(districtTextWatcher)
        province_input?.removeTextChangedListener(provinceTextWatcher)
    }

    private fun addTextChangedAddressAutoFill() {
        Logger.info("Jozzee", "addTextChangedAddressAutoFill")
        zip_code_input?.addTextChangedListener(zipCodeTextWatcher)
        sub_district_input?.addTextChangedListener(subDistrictTextWatcher)
        district_input?.addTextChangedListener(districtTextWatcher)
        province_input?.addTextChangedListener(provinceTextWatcher)
    }

    private fun isDataValid(): Boolean {
        if (first_name_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.name))
            return false
        }

        if (last_name_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.last_name))
            return false
        }

        val citizenId = citizen_id_input?.content()

        if (citizenId.isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.citizen_id))
            return false
        }

        if (BuildConfig.DEBUG.not() && citizenId.isThaiCitizenId().not()) {
            showAlertDialog(getString(R.string.warning), getString(R.string.please_enter_correct_citizen_id))
            return false
        }

        val phone = phone_input?.content()

        if (phone.isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.phone))
            return false
        }

        if (BuildConfig.DEBUG.not() && phone.isPhoneNumber().not()) {
            showAlertDialog(getString(R.string.warning), getString(R.string.please_enter_correct_phone_number))
            return false
        }

        if (birth_date_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.birth_date))
            return false
        }

        if (gender_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.gender))
            return false
        }

        if (blood_type_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.blood_type))
            return false
        }

        if (address_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.address))
            return false
        }

        if (address_house_no_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.house_no))
            return false
        }

        if (address_village_no_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.village_no))
            return false
        }

        if (zip_code_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.zip_code))
            return false
        }

        if (sub_district_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.sub_district))
            return false
        }

        if (district_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.district))
            return false
        }

        if (province_input?.content().isNullOrBlank()) {
            showRequiredInputDialog(getString(R.string.province))
            return false
        }

        if (viewModel.eventDetail.value?.shirts?.isNotEmpty() == true && viewModel.getCurrentTicketOption()?.shirt == null) {
            showRequiredInputDialog(getString(R.string.shirt_size))
            return false
        }
        return true
    }

    private fun showRequiredInputDialog(inputName: String) {
        showAlertDialog(getString(R.string.warning), getString(R.string.input_required, inputName))
    }

    private fun createUserData() = UserOptionEventRegistration().apply {
        firstName = first_name_input?.content() ?: ""
        lastName = last_name_input?.content() ?: ""
        fullName = "$firstName $lastName"
        cityCenId = citizen_id_input?.content() ?: ""
        phone = phone_input?.content() ?: ""
        birthDate = currentBirthDate ?: ""
        gender = currentGender ?: ""
        bloodType = blood_type_input?.content() ?: ""
        address = getFullAddress()
        houseNo = address_house_no_input?.content() ?: ""
        villageNo = address_village_no_input?.content() ?: ""
        subDistrict = viewModel.getCurrentSubDistrict()
        team = optional_team_name_input?.content() ?: ""
        color = optional_color_input?.content() ?: ""
        zone = optional_zone_input?.content() ?: ""
    }

    private fun getFullAddress(): String {
        return "${address_input?.content()}, ${address_house_no_input?.content()}, ${address_village_no_input?.content()}, ${sub_district_input?.content()}, " +
                "${district_input?.content()}, ${province_input?.content()}, ${zip_code_input?.content()}"
    }

    private fun updateUserDataFromUserInfo() = launch {
        requireActivity().getViewModel<UserViewModel>(UserViewModel.Factory(requireContext())).getUSerInfoInstance()?.also { userInfo ->
            //Set user info form view model
            first_name_input?.setText(userInfo.firstName ?: "")
            last_name_input?.setText(userInfo.lastName ?: "")
            phone_input?.setText(userInfo.phone ?: "")

            //Birth date
            currentBirthDate = userInfo.birthDate
            userInfo.getBirthDateCalendar()?.also { calendar ->
                when (calendar.year() > 1000) {
                    true -> birth_date_input?.setText(userInfo.getBirthDateDisplay(DISPLAY_DATE_FORMAT_FULL_MONTH))
                    false -> birth_date_input?.setText("")
                }
            }

            //Gender
            currentGender = userInfo.gender
            gender_input?.setText(userInfo.gender ?: "")
        }
    }

    override fun errorHandler(code: Int, message: String, tag: String?) {
        when (tag) {
            "Address" -> {
                val errorMessage = ApiExceptionMessage.getExceptionMessageFromStatusCode(resources, code, message)
                Logger.error(simpleName(), "Error Handler: Status code: $code, Message: $errorMessage")
                showToast(errorMessage)
            }
            else -> super.errorHandler(code, message, tag)
        }
    }

    override fun onDestroyView() {
        zip_code_input?.removeTextChangedListener(zipCodeTextWatcher)
        sub_district_input?.removeTextChangedListener(subDistrictTextWatcher)
        district_input?.removeTextChangedListener(districtTextWatcher)
        province_input?.removeTextChangedListener(provinceTextWatcher)

        super.onDestroyView()
    }

    override fun onDestroy() {
        removeObservers(viewModel.addressAutoFill)
        super.onDestroy()
    }
}