package com.edimitre.bllokuim.systemservices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.edimitre.bllokuim.data.dao.ReminderDao
import com.edimitre.bllokuim.data.model.Reminder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderDao: ReminderDao

    @Inject
    lateinit var systemService: SystemService

    override fun onReceive(context: Context, intent: Intent) {

        val thread = Thread {
            setFirstReminderFalse()

        }

        thread.start()
        thread.join()

        val thread2 = Thread {
            activateNextReminder()

        }

        thread2.start()
        thread2.join()

    }


    private fun setFirstReminderFalse() {


        val reminder: Reminder? = reminderDao.getFirstReminderOnThread()

        if (reminder != null) {

            systemService.notify("PersonalManager", reminder.description)

            reminder.isActive = false

            reminderDao.saveReminderOnThread(reminder)
        }


    }

    private fun activateNextReminder() {


        val reminder: Reminder? = reminderDao.getFirstReminderOnThread()

        if (reminder != null) {
            systemService.cancelAllAlarms()

            systemService.setAlarm(reminder.alarmTimeInMillis)
        }


    }


}