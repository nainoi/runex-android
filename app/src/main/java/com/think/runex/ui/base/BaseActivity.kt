package com.think.runex.ui.base

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.jozzee.android.core.fragment.FragmentAnimations
import com.jozzee.android.core.fragment.fragmentCount
import com.jozzee.android.core.fragment.replaceFragment
import com.jozzee.android.core.fragment.addFragment
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.think.runex.common.showAlertDialog
import com.think.runex.datasource.api.ApiExceptionMessage
import com.think.runex.ui.component.ProgressDialog
import com.think.runex.util.Localization
import com.think.runex.util.NightMode

open class BaseActivity : AppCompatActivity() {

    @IdRes
    protected var mainFragmentContainerId: Int = -1

    override fun attachBaseContext(newBase: Context?) {
        //Set ui mode and language
        super.attachBaseContext(NightMode.applyNightMode(Localization.applyLanguage(newBase)))
    }

    /**
     * Update configuration for language and night mode.
     */
    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        overrideConfiguration?.apply {
            val uiMode = uiMode
            setTo(baseContext.resources.configuration)
            this.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    /**
     * Show and update progress dialog.
     */
    fun showProgressDialog(message: String? = null,
                           @ColorRes progressColor: Int = -1,
                           showDot: Boolean = true) {
        if (isDestroyed) return

        var progressDialog = supportFragmentManager.findFragmentByTag(ProgressDialog::class.java.simpleName)
        if (progressDialog != null && progressDialog is ProgressDialog) {
            progressDialog.setProgressColor(progressColor)
            progressDialog.setMessage(message)
        } else {
            progressDialog = ProgressDialog
                    .newInstance("$message${if (showDot) "..." else ""}", progressColor)
            supportFragmentManager.commit(allowStateLoss = true) {
                add(progressDialog, progressDialog::class.java.simpleName)
            }
            supportFragmentManager.executePendingTransactions()
        }
    }

    fun hideProgressDialog() {
        supportFragmentManager.findFragmentByTag(ProgressDialog::class.java.simpleName)?.also { fragment ->
            supportFragmentManager.commit(allowStateLoss = true) {
                remove(fragment)
            }
            supportFragmentManager.executePendingTransactions()
        }
    }

    open fun errorHandler(statusCode: Int, message: String) {
        if (isDestroyed || isFinishing) return

        val errorMessage = ApiExceptionMessage.getExceptionMessageFromStatusCode(resources, statusCode, message)
        Logger.error(simpleName(), "Error Handler: Status code: $statusCode, Message: $errorMessage")

        runOnUiThread {
            hideProgressDialog()
            if (errorMessage.isNotBlank()) {
                //Show alert dialog if have error message.
                showAlertDialog(errorMessage)
            }
        }
    }

    open fun addFragment(fragment: Fragment,
                         animations: FragmentAnimations? = null,
                         hidePrevious: Int = fragmentCount(),
                         tag: String? = null) {
        addFragment(fragment, animations, mainFragmentContainerId, hidePrevious, tag)
    }

    open fun replaceFragment(fragment: Fragment,
                             animations: FragmentAnimations? = null,
                             addToBackStack: Boolean = true,
                             clearFragment: Boolean = false,
                             tag: String? = null) {
        replaceFragment(fragment, animations, mainFragmentContainerId, addToBackStack, clearFragment, tag)
    }

    override fun onDestroy() {
        Glide.get(this).bitmapPool.clearMemory()
        Glide.get(this).clearMemory()
        super.onDestroy()
    }
}