package com.think.runex.feature.main

import androidx.lifecycle.MutableLiveData
import com.think.runex.base.BaseViewModel

class MainViewModel : BaseViewModel() {

    val refreshScreen: MutableLiveData<Any> by lazy { MutableLiveData() }

    fun refreshScreen() {
        refreshScreen.postValue(null)
    }
}