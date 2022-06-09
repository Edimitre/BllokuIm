package com.edimitre.bllokuim.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edimitre.bllokuim.data.model.MainUser
import com.edimitre.bllokuim.data.service.MainUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainUserViewModel @Inject constructor(private val userService: MainUserService) :
    ViewModel() {


    fun saveUser(user: MainUser): Job = viewModelScope.launch {

        userService.saveUser(user)
    }

    fun getUser(): LiveData<MainUser?> {


        return userService.getUser()
    }

}