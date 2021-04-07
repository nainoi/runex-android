package com.think.runex.feature.payment.creditcard

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import co.omise.android.models.Token
import co.omise.android.ui.CreditCardActivity
import co.omise.android.ui.OmiseActivity
import co.omise.android.ui.OmiseActivity.Companion.EXTRA_TOKEN_OBJECT
import com.think.runex.BuildConfig
import com.think.runex.feature.setting.data.Environment
import com.think.runex.util.AppPreference

class CreditCardActivityContract : ActivityResultContract<Any, Token?>() {

    override fun createIntent(context: Context, input: Any?): Intent {

        val intent = Intent(context, CreditCardActivity::class.java)
        intent.putExtra(OmiseActivity.EXTRA_PKEY, getOmiseKey(AppPreference.getEnvironment(context)))

        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Token? {

        if (resultCode != Activity.RESULT_OK) return null

        return intent?.getParcelableExtra(EXTRA_TOKEN_OBJECT)
    }

    private fun getOmiseKey(@Environment environment: Int): String {
        if (BuildConfig.DEBUG && environment == Environment.DEV) {
            return "pkey_test_5i6ivm4cotoab601bfr" //Dev
        }
        return "pkey_5i1p3nkjgq6vrrrfhkp" //Production
    }
}