package com.think.runex.component

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.jozzee.android.core.text.toIntOrZero
import com.think.runex.util.extension.displayFormat

class NumberTextWatcher(var maxNumberLength: Int,
                        var maxDecimalLength: Int,
                        private val edtText: EditText) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val text = s.toString()

        if (text.isBlank()) {
            return
        }

        edtText.removeTextChangedListener(this)
        val numberSet = text.replace(",", "").split(".")

        var newNumber: String
        newNumber = if (numberSet[0].length > maxNumberLength) numberSet[0].substring(0, maxNumberLength) else numberSet[0]
        newNumber = newNumber.toIntOrZero().displayFormat()

        if (numberSet.size > 1 || text.contains(".")) {
            newNumber += "."
        }

        if (numberSet.size > 1) {
            newNumber += if (numberSet[1].length > maxDecimalLength) numberSet[1].substring(0, maxDecimalLength) else numberSet[1]
        }

        if (newNumber != "") {
            edtText.setText(newNumber)
            edtText.setSelection(newNumber.length)
        }

        edtText.addTextChangedListener(this)
    }
}