package com.think.runex.datasource

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jozzee.android.core.simpleName
import com.jozzee.android.core.utility.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    private var handleError: ((statusCode: Int, message: String) -> Unit)? = null

    fun setOnHandleError(block: (statusCode: Int, message: String) -> Unit) {
        handleError = block
    }

    protected fun onHandleError(statusCode: Int, message: String?) {
        handleError?.run {
            viewModelScope.launch(Main) {
                invoke(statusCode, message ?: "")
            }
        }
    }

    fun cancelJob() {
        viewModelScope.coroutineContext.cancelChildren()
    }

    override fun onCleared() {
        cancelJob()
        Logger.warning(simpleName(), "$this is Cleared")
        super.onCleared()
    }
}