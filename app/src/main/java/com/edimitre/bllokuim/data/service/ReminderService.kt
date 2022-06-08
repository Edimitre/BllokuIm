package com.edimitre.bllokuim.data.service

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.edimitre.bllokuim.data.dao.ReminderDao
import com.edimitre.bllokuim.data.model.Reminder
import javax.inject.Inject


class ReminderService @Inject constructor(private val reminderDao: ReminderDao) {

    var allReminders = reminderDao.getAllReminders()!!.asLiveData()

    var firstReminder = reminderDao.getFirstReminder().asLiveData()


    suspend fun save(reminder: Reminder) {

        reminderDao.saveReminder(reminder)

        Log.e("PersonalManager", "reminder me emer : ${reminder.description} U ruajt")

    }

    suspend fun delete(reminder: Reminder) {

        reminderDao.deleteReminder(reminder)

        Log.e("PersonalManager", "reminder me emer : ${reminder.description} U fshi")

    }

    fun getClosestReminder(): LiveData<Reminder?> {

        return reminderDao.getFirstReminder().asLiveData()

    }

}