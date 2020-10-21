package com.think.runex.feature.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.think.runex.datasource.BaseViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class UserViewModel(private val userRepo: UserRepository) : BaseViewModel() {

    val profile: MutableLiveData<UserInfo> by lazy { MutableLiveData<UserInfo>() }

    fun getProfile() {
        viewModelScope.launch(IO) {
            val result = userRepo.getProfile()
            when (result.isSuccessful()) {
                true -> postProfileData(result.data)
                false -> onHandleError(result.statusCode, result.message)
            }
        }
    }

    private fun postProfileData(profile: UserInfo?) = viewModelScope.launch(Main) {
        this@UserViewModel.profile.value = profile
    }
}