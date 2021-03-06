package com.think.runex.feature.event.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.jozzee.android.core.datetime.dateTimeFormat
import com.jozzee.android.core.datetime.toCalendar
import com.jozzee.android.core.datetime.year
import com.jozzee.android.core.view.showDialog
import com.jozzee.android.core.view.showToast
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.common.getViewModel
import com.think.runex.feature.user.GenderDialog
import com.think.runex.config.DISPLAY_DATE_FORMAT_FULL_MONTH
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.feature.event.data.Shirt
import com.think.runex.feature.user.UserViewModel
import com.think.runex.feature.user.UserViewModelFactory
import com.think.runex.feature.user.data.Gender
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.fragment_fill_out_user_info.*
import java.util.*
import kotlin.collections.ArrayList

class FillOutUserInfoFragment : BaseScreen(), DatePickerDialog.OnDateSetListener,
        GenderDialog.OnGenderSelectedListener, ShirtsDialog.OnShirtSelectedListener {

    private lateinit var viewModel: RegisterEventViewModel

    private var currentBirthDate: String? = null
    private var currentGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = requireParentFragment().getViewModel(RegisterEventViewModelFactory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fill_out_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    private fun setupComponents() {
        launch {
            requireActivity().getViewModel<UserViewModel>(UserViewModelFactory(requireContext())).getUSerInfoInstance()?.also { userInfo ->
                //Set user info form view model
                first_name_input?.setText(userInfo.firstName ?: "")
                last_name_input?.setText(userInfo.lastName ?: "")
                phone_input?.setText(userInfo.phone ?: "")

                //Birth date
                currentBirthDate = userInfo.birthDate
                userInfo.getBirthDateCalendar()?.also { calendar ->
                    when (calendar.year() > 1000) {
                        true -> birth_date_input?.setText(userInfo.getBirthDate(DISPLAY_DATE_FORMAT_FULL_MONTH))
                        false -> birth_date_input?.setText("")
                    }
                }

                //Gender
                currentGender = userInfo.gender
                gender_input?.setText(userInfo.gender ?: "")
            }
        }
    }

    private fun subscribeUi() {

        birth_date_input?.setOnClickListener {
            showDatePicker()
        }

        gender_input?.setOnClickListener {
            showDialog(GenderDialog())
        }

        shirt_size_input?.setOnClickListener {
            when (viewModel.eventDetail.value?.shirts?.isNotEmpty() == true) {
                true -> showDialog(ShirtsDialog.newInstance(ArrayList(viewModel.eventDetail.value?.shirts)))
                false -> showToast(R.string.no_shirts_to_choose)
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
    }
}