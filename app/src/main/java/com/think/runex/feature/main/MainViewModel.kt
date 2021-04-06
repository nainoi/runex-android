package com.think.runex.feature.main

import androidx.lifecycle.MutableLiveData
import com.think.runex.base.BaseViewModel

class MainViewModel : BaseViewModel() {

    val refreshScreen: MutableLiveData<String> by lazy { MutableLiveData() }

    fun refreshScreen(screenName: String? = null) {
        refreshScreen.postValue(screenName)
    }
}