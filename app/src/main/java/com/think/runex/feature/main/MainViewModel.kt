package com.think.runex.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.think.runex.datasource.BaseViewModel

class MainViewModel : BaseViewModel() {

    val refreshScreen: MutableLiveData<Any> by lazy { MutableLiveData() }

    fun refreshScreen() {
        refreshScreen.postValue(null)
    }
}