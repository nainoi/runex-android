package com.think.runex.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.think.runex.util.cancelJobs
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private var handleError: ((statusCode: Int, message: String, tag: String?) -> Unit)? = null

    fun setOnHandleError(block: (statusCode: Int, message: String, tag: String?) -> Unit) {
        handleError = block
    }

    protected fun onHandleError(statusCode: Int, message: String?, tag: String? = null) {
        handleError?.run {
            viewModelScope.launch(Main) {
                invoke(statusCode, message ?: "", tag)
            }
        }
    }

    override fun onCleared() {
        cancelJobs()
        Logger.warning(simpleName(), "$this is Cleared")
        super.onCleared()
    }
}