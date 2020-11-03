package com.think.runex.feature.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.think.runex.datasource.BaseViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class UserViewModel(private val userRepo: UserRepository) : BaseViewModel() {
}