package com.edimitre.bllokuim.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edimitre.bllokuim.data.model.Reminder
import com.edimitre.bllokuim.data.service.ReminderService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(private val reminderService: ReminderService) :
    ViewModel() {

    var reminderList = reminderService.allReminders

    var firstReminder = reminderService.firstReminder

    fun saveReminder(reminder: Reminder): Job = viewModelScope.launch {
        reminderService.save(reminder)
    }

    fun deleteReminder(reminder: Reminder): Job = viewModelScope.launch {

        reminderService.delete(reminder)
    }

}