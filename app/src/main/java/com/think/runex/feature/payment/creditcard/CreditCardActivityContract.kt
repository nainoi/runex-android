package com.think.runex.feature.payment.creditcard

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import co.omise.android.models.Token
import co.omise.android.ui.CreditCardActivity
import co.omise.android.ui.OmiseActivity
import co.omise.android.ui.OmiseActivity.Companion.EXTRA_TOKEN_OBJECT

class CreditCardActivityContract : ActivityResultContract<String, Token?>() {

    override fun createIntent(context: Context, input: String?): Intent {

        val intent = Intent(context, CreditCardActivity::class.java)
        intent.putExtra(OmiseActivity.EXTRA_PKEY, input ?: "")

        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Token? {

        if (resultCode != Activity.RESULT_OK) return null

        return intent?.getParcelableExtra(EXTRA_TOKEN_OBJECT)
    }
}