package com.edimitre.bllokuim.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edimitre.bllokuim.data.model.Description
import com.edimitre.bllokuim.data.service.DescriptionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DescriptionViewModel @Inject constructor(private val descriptionService: DescriptionService) :
    ViewModel() {

    var descriptionList: LiveData<List<Description>> = descriptionService.allDescriptionsList


    fun saveDescription(description: Description): Job = viewModelScope.launch {
        descriptionService.saveDescription(description)

    }

    fun deleteDescription(description: Description): Job = viewModelScope.launch {

        descriptionService.deleteDescription(description)
    }

    fun getAllDescriptionsList(): List<Description> {


        return runBlocking { descriptionService.getAllDescriptionsList() }
    }

}