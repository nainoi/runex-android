package com.think.runex.feature.event.register

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.jozzee.android.core.datetime.dateTimeFormat
import com.jozzee.android.core.datetime.toCalendar
import com.jozzee.android.core.datetime.year
import com.jozzee.android.core.view.showDialog
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.component.GenderDialog
import com.think.runex.config.DISPLAY_DATE_FORMAT_FULL_MONTH
import com.think.runex.config.SERVER_DATE_TIME_FORMAT
import com.think.runex.feature.user.data.Gender
import kotlinx.android.synthetic.main.fragment_fill_out_user_info.*
import java.util.*

class FillOutUserInfoFragment : BaseScreen(), DatePickerDialog.OnDateSetListener, GenderDialog.OnGenderSelectedListener {

    private var currentBirthDate: String? = null
    private var currentGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    }

    private fun subscribeUi() {

        birth_date_input?.setOnClickListener {
            showDatePicker()
        }

        gender_input?.setOnClickListener {
            showDialog(GenderDialog())
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
}