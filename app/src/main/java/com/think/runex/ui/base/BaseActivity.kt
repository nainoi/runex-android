package com.think.runex.ui.base

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jozzee.android.core.fragment.FragmentAnimations
import com.jozzee.android.core.fragment.fragmentCount
import com.jozzee.android.core.fragment.replaceFragment
import com.jozzee.android.core.fragment.addFragment
import com.think.runex.util.Localization
import com.think.runex.util.NightMode

open class BaseActivity : AppCompatActivity() {

    @IdRes
    protected var mainFragmentContainerId: Int = -1

    override fun attachBaseContext(newBase: Context?) {
        //TODO("Set language with ui mode")
        //super.attachBaseContext(UiMode.applyUiMode(Localization.applyLanguageContext(newBase)))
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
}